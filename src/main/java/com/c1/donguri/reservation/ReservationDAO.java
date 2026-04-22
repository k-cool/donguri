package com.c1.donguri.reservation;

import com.c1.donguri.scheduler.EmailJobDTO;
import com.c1.donguri.scheduler.EmailScheduler;
import com.c1.donguri.template.TemplateDTO;
import com.c1.donguri.user.UserDTO;
import com.c1.donguri.util.DBManager;
import com.c1.donguri.util.S3Uploader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReservationDAO {
    public static final ReservationDAO RESERVATION_DAO = new ReservationDAO();

    private ReservationDAO() {
    }

    public String insert(HttpServletRequest request) { //정보넣자~

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        InsertReservationDTO ir = (InsertReservationDTO) session.getAttribute("insertReservation");

        Connection con = null;
        PreparedStatement ps = null;
        String emailContentSql = "\n" +
                "INSERT INTO EMAIL_CONTENT(EMAIL_CONTENT_ID ,TEMPLATE_ID, SENDER_ID, SUBJECT, CONTENT, COVER_IMG_URL, BGM_URL)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)\n";
        String reservationSql = "\n" +
                "INSERT INTO RESERVATION(RESERVATION_ID, FROM_ID, EMAIL_CONTENT_ID, RECIPIENT_EMAIL, SCHEDULED_DATE)\n" +
                "VALUES (?, ?, ?, ?, ?)\n";

        String reservationId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String emailContentId = UUID.randomUUID().toString().replace("-", "").toUpperCase();


        try {

            con = DBManager.DB_MANAGER.getConnection();

            con.setAutoCommit(false);

            ps = con.prepareStatement(emailContentSql);

            ps.setString(1, emailContentId);
            ps.setString(2, ir.getTemplateId());
            ps.setString(3, user.getUserId());
            ps.setString(4, ir.getSubject());
            ps.setString(5, ir.getContent());
            ps.setString(6, ir.getCoverImgUrl());
            ps.setString(7, ir.getBgmUrl());

            int firstRow = ps.executeUpdate();

            if (firstRow > 0) {
                System.out.println("EMAIL_CONTENT INSERT 성공");
            }

            ps = con.prepareStatement(reservationSql);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDateTime formatted = LocalDateTime.parse(ir.getScheduledDate(), formatter);

            ps.setString(1, reservationId);
            ps.setString(2, user.getUserId());
            ps.setString(3, emailContentId);
            ps.setString(4, ir.getRecipientEmail());
            ps.setObject(5, formatted);

            int secondRow = ps.executeUpdate();

            if (secondRow > 0) {
                System.out.println("RESERVATION INSERT 성공");
            }

            if (firstRow > 0 && secondRow > 0) {
                System.out.println("트랜잭션 성공");
                con.commit();
            }

            return reservationId;

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                    System.err.println("에러 발생! 데이터가 롤백되었습니다.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (Exception e) {

                }
            }

            DBManager.DB_MANAGER.close(con, ps, null);

        }

        return null;
    }

    public void enrollNewEmailJob(String reservationId) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT r.reservation_id,\n" +
                "       r.recipient_email,\n" +
                "       r.scheduled_date,\n" +
                "       e.subject,\n" +
                "       e.content,\n" +
                "       r.is_done,\n" +
                "       t.bg_color,\n" +
                "       t.cover_img_url\n" +
                "FROM reservation r\n" +
                "         JOIN email_content e ON r.email_content_id = e.email_content_id\n" +
                "         JOIN template t on e.TEMPLATE_ID = t.TEMPLATE_ID\n" +
                "WHERE r.reservation_id = ?";


        try {

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reservationId);
            rs = pstmt.executeQuery();
            EmailJobDTO emailJob = null;

            while (rs.next()) {
                emailJob = new EmailJobDTO(
                        rs.getString("reservation_id"),
                        rs.getString("recipient_email"),
                        rs.getTimestamp("scheduled_date"),
                        rs.getString("subject"),
                        rs.getString("content"),
                        rs.getString("is_done"),
                        rs.getString("bg_color"),
                        rs.getString("cover_img_url")
                );
            }

            EmailScheduler.EMAIL_SCHEDULER.enrollJob(emailJob);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

    }

    public List<ReservationDTO> getAll(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        String status = request.getParameter("status");
        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<ReservationDTO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT R.RESERVATION_ID, R.RECIPIENT_EMAIL, R.SCHEDULED_DATE, R.IS_DONE, E.SUBJECT, T.COVER_IMG_URL, E.COVER_IMG_URL AS E_COVER_IMG_URL\n");
        sql.append("FROM RESERVATION R, EMAIL_CONTENT E, TEMPLATE T\n");
        sql.append("WHERE R.EMAIL_CONTENT_ID = E.EMAIL_CONTENT_ID\n");
        sql.append("AND E.TEMPLATE_ID = T.TEMPLATE_ID\n");
        sql.append("AND R.FROM_ID = ?\n");

        if (status != null && !status.equals("all")) {
            String statusVal = status.equals("완료") ? "Y" : "N";
            sql.append("AND R.IS_DONE = '").append(statusVal).append("' ");
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            if ("recipientEmail".equals(searchType)) {
                sql.append("AND R.RECIPIENT_EMAIL LIKE ? ");
            } else if ("subject".equals(searchType)) {
                sql.append("AND E.SUBJECT LIKE ? ");
            } else {
                sql.append("AND (R.RECIPIENT_EMAIL LIKE ? OR E.SUBJECT LIKE ?) ");
            }
        }

        sql.append("ORDER BY R.SCHEDULED_DATE DESC");

        try {
            con = DBManager.DB_MANAGER.getConnection();
            ps = con.prepareStatement(sql.toString());

            int paramIdx = 1;
            ps.setString(paramIdx++, user.getUserId());

            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchKeyword = "%" + keyword + "%";
                ps.setString(paramIdx++, searchKeyword);
                if ("all".equals(searchType) || searchType == null) {
                    ps.setString(paramIdx++, searchKeyword);
                }
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                ReservationDTO r = new ReservationDTO();

                r.setReservationId(rs.getString("reservation_id"));
                r.setRecipientEmail(rs.getString("recipient_email"));
                r.setSubject(rs.getString("subject"));
                r.setIsDone(rs.getString("is_done"));
                Timestamp ts = rs.getTimestamp("scheduled_date");
                r.setScheduledDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(ts));

                String coverImgUrl = rs.getString("cover_img_url");

                if (rs.getString("e_cover_img_url") != null) {
                    coverImgUrl = rs.getString("e_cover_img_url");
                }

                r.setCoverImgUrl(coverImgUrl);

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, ps, rs);
        }

        return list;
    }

    public void delete(String reservationId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String emailContentId = null;

        String selectEmailIdSql = "SELECT EMAIL_CONTENT_ID FROM RESERVATION WHERE RESERVATION_ID = ?";
        String deleteLogSql = "DELETE FROM SEND_LOG WHERE RESERVATION_ID = ?";
        String deleteResSql = "DELETE FROM RESERVATION WHERE RESERVATION_ID = ?";
        String deleteEmailSql = "DELETE FROM EMAIL_CONTENT WHERE EMAIL_CONTENT_ID = ?";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            con.setAutoCommit(false);

            pstmt = con.prepareStatement(selectEmailIdSql);
            pstmt.setString(1, reservationId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                emailContentId = rs.getString("EMAIL_CONTENT_ID");
            }
            pstmt.close();

            if (emailContentId != null) {
                pstmt = con.prepareStatement(deleteLogSql);
                pstmt.setString(1, reservationId);
                pstmt.executeUpdate();
                pstmt.close();

                pstmt = con.prepareStatement(deleteResSql);
                pstmt.setString(1, reservationId);
                pstmt.executeUpdate();
                pstmt.close();

                pstmt = con.prepareStatement(deleteEmailSql);
                pstmt.setString(1, emailContentId);
                pstmt.executeUpdate();

                con.commit();
            }

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
    }

    public ReservationDTO getOne(HttpServletRequest request) {

        String sql = "SELECT r.reservation_id,\n" +
                "       u.nickname as from_id,\n" +
                "       r.recipient_email,\n" +
                "       r.scheduled_date,\n" +
                "       e.subject,\n" +
                "       e.content,\n" +
                "       e.template_id,\n" +
                "       e.bgm_url\n" +
                "FROM reservation r\n" +
                "JOIN email_content e ON r.email_content_id = e.email_content_id\n" +
                "JOIN users u ON r.from_id = u.user_id\n" +
                "WHERE r.reservation_id=?";
        try (Connection con = DBManager.DB_MANAGER.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String reservationId = request.getParameter("id");
            ps.setString(1, reservationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ReservationDTO r = new ReservationDTO();
                r.setReservationId(rs.getString("reservation_id"));
                r.setFromId(rs.getString("from_id"));
                r.setRecipientEmail(rs.getString("recipient_email"));
                r.setSubject(rs.getString("subject"));
                r.setContent(rs.getString("content"));
                r.setTemplateId(rs.getString("template_id"));
                r.setBgmUrl(rs.getString("bgm_url"));
                r.setScheduledDate(rs.getString("scheduled_date"));
                return r;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int update(HttpServletRequest request) {
        String reservationId = request.getParameter("id");
        String recipientEmail = request.getParameter("recipientEmail");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");
        String scheduledDate = request.getParameter("scheduledDate");
        String templateId = request.getParameter("templateId");
        String bgmUrl = request.getParameter("bgmUrl");

        Connection con = null;
        PreparedStatement ps = null;


        String getEmailContentIdSql = "SELECT EMAIL_CONTENT_ID FROM RESERVATION WHERE RESERVATION_ID = ?";
        String updateEmailSql = "UPDATE EMAIL_CONTENT SET SUBJECT = ?, CONTENT = ?, TEMPLATE_ID = ?, BGM_URL = ? WHERE EMAIL_CONTENT_ID = ?";
        String updateResSql = "UPDATE RESERVATION SET RECIPIENT_EMAIL = ?, SCHEDULED_DATE = TO_DATE(?, 'YYYY-MM-DD HH24:MI') WHERE RESERVATION_ID = ?";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            con.setAutoCommit(false);

            String emailContentId = null;
            // 1. EMAIL_CONTENT_ID 조회
            ps = con.prepareStatement(getEmailContentIdSql);
            ps.setString(1, reservationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                emailContentId = rs.getString("EMAIL_CONTENT_ID");
            }
            ps.close();

            if (emailContentId != null) {
                // 2. EMAIL_CONTENT 테이블 수정
                ps = con.prepareStatement(updateEmailSql);
                ps.setString(1, subject);
                ps.setString(2, content);
                ps.setString(3, templateId);
                ps.setString(4, bgmUrl);
                ps.setString(5, emailContentId);
                int res1 = ps.executeUpdate();
                ps.close();


                ps = con.prepareStatement(updateResSql);
                ps.setString(1, recipientEmail);
                ps.setString(2, scheduledDate);
                ps.setString(3, reservationId);
                int res2 = ps.executeUpdate();

                if (res1 > 0 && res2 > 0) {
                    con.commit(); // 둘 다 성공 시 커밋
                    System.out.println("수정 트랜잭션 성공");
                    return 1;
                } else {
                    try {
                        con.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, ps, null);
        }

        return 0;
    }


    public ArrayList<TemplateDTO> getTemplateList(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        ArrayList<TemplateDTO> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String baseSql = "SELECT *\n" +
                "FROM TEMPLATE\n" +
                "WHERE TYPE = 'BASE'";

        String addedSql = "SELECT t.TEMPLATE_ID,\n" +
                "       t.NAME,\n" +
                "       t.BG_COLOR,\n" +
                "       t.TYPE,\n" +
                "       t.COVER_IMG_URL,\n" +
                "       t.QR_URL,\n" +
                "       t.CREATED_AT,\n" +
                "       t.UPDATED_AT\n" +
                "FROM TEMPLATE T,\n" +
                "     USER_TEMPLATE UT\n" +
                "WHERE t.TEMPLATE_ID = ut.TEMPLATE_ID\n" +
                "  AND ut.USER_ID = ?\n" +
                "ORDER BY ut.CREATED_AT DESC";


        try {
            con = DBManager.DB_MANAGER.getConnection();
            ps = con.prepareStatement(baseSql);
            rs = ps.executeQuery();


            while (rs.next()) {
                TemplateDTO template = new TemplateDTO(
                        rs.getString("template_id"),
                        rs.getString("name"),
                        rs.getString("bg_color"),
                        rs.getString("type"),
                        rs.getString("cover_img_url"),
                        rs.getString("qr_url"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );

                list.add(template);
            }

            ps = con.prepareStatement(addedSql);
            ps.setString(1, user.getUserId());
            rs = ps.executeQuery();

            while (rs.next()) {
                TemplateDTO template = new TemplateDTO(
                        rs.getString("template_id"),
                        rs.getString("name"),
                        rs.getString("bg_color"),
                        rs.getString("type"),
                        rs.getString("cover_img_url"),
                        rs.getString("qr_url"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );

                list.add(template);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, ps, null);
        }

        return list;
    }

    public void setReservationDTOToSession(HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");


        InsertReservationDTO ir = new InsertReservationDTO(
                null,
                user.getUserId(),
                null,
                request.getParameter("recipientEmail"),
                request.getParameter("subject"),
                request.getParameter("content"),
                request.getParameter("templateId"),
                request.getParameter("coverImgUrl"),
                request.getParameter("bgmUrl"),
                request.getParameter("scheduledDate")
        );

        session.setAttribute("insertReservation", ir);

    }

    public TemplateDTO getTemplate(HttpServletRequest request) {

        TemplateDTO template = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT *\n" +
                "FROM TEMPLATE\n" +
                "WHERE TEMPLATE_ID = ?";

        String templateId = request.getParameter("templateId");

        try {
            con = DBManager.DB_MANAGER.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, templateId);
            rs = ps.executeQuery();

            while (rs.next()) {
                template = new TemplateDTO(
                        rs.getString("template_id"),
                        rs.getString("name"),
                        rs.getString("bg_color"),
                        rs.getString("type"),
                        rs.getString("cover_img_url"),
                        rs.getString("qr_url"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );
            }

            return template;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return template;
    }
}
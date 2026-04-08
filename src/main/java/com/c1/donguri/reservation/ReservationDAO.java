package com.c1.donguri.reservation;

import com.c1.donguri.user.UserDTO;
import com.c1.donguri.util.DBManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReservationDAO {
    public static final ReservationDAO RESERVATION_DAO = new ReservationDAO();

    private ReservationDAO() {
    }

    public void insert(HttpServletRequest request) { //정보넣자~

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        InsertReservationDTO ir = (InsertReservationDTO) session.getAttribute("insertReservation");

        Connection con = null;
        PreparedStatement ps = null;
        String emailContentSql = "\n" +
                "INSERT INTO EMAIL_CONTENT(EMAIL_CONTENT_ID ,TEMPLATE_ID, SENDER_ID, SUBJECT, CONTENT, COVER_IMG_URL, BGM_URL)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)\n";
        String reservationSql = "\n" +
                "INSERT INTO RESERVATION(FROM_ID, EMAIL_CONTENT_ID, RECIPIENT_EMAIL, SCHEDULED_DATE)\n" +
                "VALUES (?, ?, ?, ?)\n";

        try {
            con = DBManager.DB_MANAGER.getConnection();

            con.setAutoCommit(false);

            ps = con.prepareStatement(emailContentSql);

            // 1. UUID 생성 (예: 550e8400-e29b-41d4-a716-446655440000)
            String uuid = UUID.randomUUID().toString();

            // 2. 하이픈 제거 및 대문자 변환 (오라클 SYS_GUID 형식)
            String emailContentId = uuid.replace("-", "").toUpperCase();

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

            ps.setString(1, user.getUserId());
            ps.setString(2, emailContentId);
            ps.setString(3, ir.getRecipientEmail());
            ps.setObject(4, formatted);

            int secondRow = ps.executeUpdate();

            if (secondRow > 0) {
                System.out.println("RESERVATION INSERT 성공");
            }

            if (firstRow > 0 && secondRow > 0) {
                System.out.println("트랜잭션 성공");
                con.commit();
            }

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
            DBManager.DB_MANAGER.close(con, ps, null);

            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (Exception e) {
                }
            }
        }
    }

    public List<ReservationDTO> getAll() { //전체정보조회
        List<ReservationDTO> list = new ArrayList<>();
        String sql = "SELECT R.RESERVATION_ID,\n" +
                "       R.RECIPIENT_EMAIL,\n" +
                "       R.SCHEDULED_DATE,\n" +
                "       R.IS_DONE,\n" +
                "       E.SUBJECT\n" +
                "FROM RESERVATION R,\n" +
                "     EMAIL_CONTENT E\n" +
                "WHERE R.EMAIL_CONTENT_ID = E.EMAIL_CONTENT_ID\n" +
                "ORDER BY R.SCHEDULED_DATE";


        try (Connection con = DBManager.DB_MANAGER.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ReservationDTO r = new ReservationDTO();

                r.setReservationId(rs.getString("reservation_id"));
                r.setRecipientEmail(rs.getString("recipient_email"));
                r.setSubject(rs.getString("subject"));
                r.setIsDone(rs.getString("is_done"));
                Timestamp ts = rs.getTimestamp("scheduled_date");
                r.setScheduledDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(ts));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public int delete(String reservationId) {

        String getEmailContentIdSql = "SELECT EMAIL_CONTENT_ID FROM RESERVATION WHERE RESERVATION_ID=?";
        String deleteReservationSql = "DELETE FROM RESERVATION WHERE RESERVATION_ID=?";
        String deleteEmailContentSql = "DELETE FROM EMAIL_CONTENT WHERE EMAIL_CONTENT_ID=?";

        try (Connection con = DBManager.DB_MANAGER.getConnection()) {
            con.setAutoCommit(false);

            String emailContentId = null;

            try (PreparedStatement ps = con.prepareStatement(getEmailContentIdSql)) {
                ps.setString(1, reservationId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    emailContentId = rs.getString("EMAIL_CONTENT_ID");
                }
            }

            if (emailContentId != null) {
                try (PreparedStatement ps1 = con.prepareStatement(deleteReservationSql)) {
                    ps1.setString(1, reservationId);
                    ps1.executeUpdate();
                }

                try (PreparedStatement ps2 = con.prepareStatement(deleteEmailContentSql)) {
                    ps2.setString(1, emailContentId);
                    ps2.executeUpdate();
                }

                con.commit();
                return 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ReservationDTO getOne(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

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
                r.setBgm(rs.getString("bgm_url"));
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

        String baseSql = "SELECT TEMPLATE_ID, NAME, COVER_IMG_URL\n" +
                "FROM TEMPLATE\n" +
                "WHERE TYPE = 'BASE'";
        String addedSql = "SELECT t.template_id,\n" +
                "       t.name,\n" +
                "       t.cover_img_url\n" +
                "FROM user_template ut\n" +
                "         JOIN\n" +
                "     template t ON ut.template_id = t.template_id\n" +
                "WHERE ut.user_id = ?\n" +
                "ORDER BY ut.created_at DESC";


        try {
            con = DBManager.DB_MANAGER.getConnection();
            ps = con.prepareStatement(baseSql);
            rs = ps.executeQuery();


            while (rs.next()) {
                TemplateDTO template = new TemplateDTO();
                template.setTemplateId(rs.getString("template_id"));
                template.setName(rs.getString("name"));
                template.setCoverImgUrl(rs.getString("cover_img_url"));
                list.add(template);
            }

            ps = con.prepareStatement(addedSql);
            ps.setString(1, user.getUserId());
            rs = ps.executeQuery();

            while (rs.next()) {
                TemplateDTO template = new TemplateDTO();
                template.setTemplateId(rs.getString("template_id"));
                template.setName(rs.getString("name"));
                template.setCoverImgUrl(rs.getString("cover_img_url"));
                list.add(template);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void setReservationDTOToSession(HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");


        InsertReservationDTO ir = new InsertReservationDTO(
                request.getParameter("id"),
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
}
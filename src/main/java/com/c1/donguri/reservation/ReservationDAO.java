package com.c1.donguri.reservation;

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
        InsertReservationDTO ir = (InsertReservationDTO) session.getAttribute("insertReservation");

// TODO: 나중에 로그인 , 템플릿 선택 구현 완료되면 변수로 수정하기
        // userId = 118D6CAAC61C48B9B6A666E4FB021C93
        // templateId = 44B0AE7E5F7A4F25B71913BB86B6E17D


        Connection con = null;
        PreparedStatement ps = null;
        String emailContentSql = "\n" +
                "INSERT INTO EMAIL_CONTENT(EMAIL_CONTENT_ID ,TEMPLATE_ID, SENDER_ID, SUBJECT, CONTENT, COVER_IMG_URL, BGM_URL)\n" +
                "VALUES (? ,'44B0AE7E5F7A4F25B71913BB86B6E17D', '118D6CAAC61C48B9B6A666E4FB021C93', ?, ?, ?, ?)\n";
        String reservationSql = "\n" +
                "INSERT INTO RESERVATION(FROM_ID, EMAIL_CONTENT_ID, RECIPIENT_EMAIL, SCHEDULED_DATE)\n" +
                "VALUES ('118D6CAAC61C48B9B6A666E4FB021C93', ?, ?, ?)\n";


//        String subject = request.getParameter("subject");
//        String content = request.getParameter("content");
//        String coverImgUrl = request.getParameter("coverImgUrl");
//        String templateId = request.getParameter("templateId");
//        String bgmUrl = request.getParameter("bgmUrl");


//        String recipientEmail = request.getParameter("recipientEmail");
//        String scheduledDate = request.getParameter("scheduledDate");


        try {
            con = DBManager.DB_MANAGER.getConnection();

            con.setAutoCommit(false);

            ps = con.prepareStatement(emailContentSql);

            // 1. UUID 생성 (예: 550e8400-e29b-41d4-a716-446655440000)
            String uuid = UUID.randomUUID().toString();

            // 2. 하이픈 제거 및 대문자 변환 (오라클 SYS_GUID 형식)
            String emailContentId = uuid.replace("-", "").toUpperCase();

            ps.setString(1, emailContentId);
            ps.setString(2, ir.getSubject());
            ps.setString(3, ir.getContent());
            ps.setString(4, ir.getCoverImgUrl());
            ps.setString(5, ir.getBgmUrl());

            int firstRow = ps.executeUpdate();

            if (firstRow > 0) {
                System.out.println("EMAIL_CONTENT INSERT 성공");
            }

            ps = con.prepareStatement(reservationSql);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDateTime formatted = LocalDateTime.parse(ir.getScheduledDate(), formatter);

            ps.setString(1, emailContentId);
            ps.setString(2, ir.getRecipientEmail());
            ps.setObject(3, formatted);

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
//        String sql = "SELECT * FROM reservation ORDER BY scheduled_date";
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
//                r.setFromId(rs.getString("from_id"));
//                r.setSenderEmail(rs.getString("sender_email"));
                r.setRecipientEmail(rs.getString("recipient_email"));
                r.setSubject(rs.getString("subject"));
//                r.setEmailMessage(rs.getString("content"));
//                r.setTemplateId(rs.getString("template_id"));
//                r.setBgm(rs.getString("bgm"));
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


    public int delete(String id) {
        String getEmailContentIdSql = "SELECT EMAIL_CONTENT_ID FROM RESERVATION WHERE RESERVATION_ID=?";
        String deleteReservationSql = "DELETE FROM RESERVATION R WHERE R.RESERVATION_ID=?";
        String deleteEmailContentSql = "DELETE FROM EMAIL_CONTENT E WHERE E.EMAIL_CONTENT_ID=?";

        try (Connection con = DBManager.DB_MANAGER.getConnection()) {
            con.setAutoCommit(false);

            String emailContentId = null;

            try (PreparedStatement ps = con.prepareStatement(getEmailContentIdSql)) {
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    emailContentId = rs.getString("EMAIL_CONTENT_ID");
                }
            }

            if (emailContentId != null) {
                try (PreparedStatement ps1 = con.prepareStatement(deleteReservationSql)) {
                    ps1.setString(1, id);
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

    public ReservationDTO getOne(String id) {
        String sql = "SELECT r.reservation_id,\n" +
                "       r.recipient_email,\n" +
                "       r.scheduled_date,\n" +
                "       e.subject,\n" +
                "       e.content\n" +
                "FROM reservation r\n" +
                "JOIN email_content e ON r.email_content_id = e.email_content_id\n" +
                "WHERE r.reservation_id=?";
        try (Connection con = DBManager.DB_MANAGER.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ReservationDTO r = new ReservationDTO();
                r.setReservationId(rs.getString("reservation_id"));
                r.setRecipientEmail(rs.getString("recipient_email"));
                r.setSubject(rs.getString("subject"));
                r.setContent(rs.getString("content"));
                r.setScheduledDate(rs.getString("scheduled_date"));
                return r;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


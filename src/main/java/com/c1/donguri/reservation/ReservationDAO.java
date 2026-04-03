package com.c1.donguri.reservation;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReservationDAO {

    private static ReservationDAO instance = new ReservationDAO();

    private ReservationDAO() {
    }

    public static ReservationDAO getInstance() {
        return instance;
    }

    public int insert(ReservationDTO r) {
        int result = 0;

        try (Connection con = DBManager.connect()) {
            con.setAutoCommit(false);


            String emailContentId = UUID.randomUUID().toString();
            String sql1 = "INSERT INTO email_content(email_content_id, template_id, sender_id, subject, content) VALUES(?, ?, ?, ?, ?)";

            try (PreparedStatement ps1 = con.prepareStatement(sql1)) {
                ps1.setString(1, emailContentId);
                ps1.executeUpdate();
            }

            r.setEmailContentId(emailContentId);


            String sql2 = "INSERT INTO reservation (" +
                    "from_id, email_content_id, sender_email, recipient_email, " +
                    "subject, content, template_id, bgm, scheduled_date, is_done) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'N')";

            try (PreparedStatement ps2 = con.prepareStatement(sql2)) {

                ps2.setString(1, r.getFromId());
                ps2.setString(2, r.getEmailContentId());
                ps2.setString(3, r.getSenderEmail());
                ps2.setString(4, r.getRecipientEmail());
                ps2.setString(5, r.getSubject());
                ps2.setString(6, r.getContent());
                ps2.setString(7, r.getTemplateId());
                ps2.setString(8, r.getBgm());


                String dateStr = r.getScheduledDate();

                System.out.println("원본 날짜: " + dateStr);

                try {
                    dateStr = dateStr.replace("T", " ");

                    if (dateStr.length() == 16) {
                        dateStr += ":00";
                    }

                    Timestamp ts = Timestamp.valueOf(dateStr);
                    ps2.setTimestamp(9, ts);

                } catch (Exception e) {
                    System.out.println(" 날짜 변환 실패: " + dateStr);
                    e.printStackTrace();
                    throw e;
                }

                result = ps2.executeUpdate();
                System.out.println("insert 결과: " + result);
            }

            con.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<ReservationDTO> getAll() {
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


        try (Connection con = DBManager.connect();
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
        String sql = "DELETE FROM reservation WHERE reservation_id=?";

        try (Connection con = DBManager.connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ReservationDTO getOne(String id) {
        String sql = "SELECT * FROM reservation WHERE reservation_id=?";
        try (Connection con = DBManager.connect();
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
package com.c1.donguri.scheduler;

import com.c1.donguri.util.DBManager;
import com.c1.donguri.util.EmailSend;
import com.c1.donguri.util.EnvLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SentMailDAO {
    public static final SentMailDAO SENT_MAIL = new SentMailDAO();

    private SentMailDAO() {
    }


    public ArrayList<SentMailDTO> getSuccessSentMails(String keyword) {
        ArrayList<SentMailDTO> sentMails = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            con = DBManager.DB_MANAGER.getConnection();

            if (keyword == null || keyword.trim().isEmpty()) {
                String sql = "SELECT r.reservation_id, r.recipient_email, e.subject, e.content, s.status, s.sent_at " +
                        "FROM send_log s " +
                        "JOIN reservation r ON s.reservation_id = r.reservation_id " +
                        "JOIN email_content e ON r.email_content_id = e.email_content_id " +
                        "WHERE s.status = 'SUCCESS' " +
                        "ORDER BY s.sent_at DESC";

                pstmt = con.prepareStatement(sql);

            } else {
                String sql = "SELECT r.reservation_id, r.recipient_email, e.subject, e.content, s.status, s.sent_at " +
                        "FROM send_log s " +
                        "JOIN reservation r ON s.reservation_id = r.reservation_id " +
                        "JOIN email_content e ON r.email_content_id = e.email_content_id " +
                        "WHERE s.status = 'SUCCESS' " +
                        "AND (r.recipient_email LIKE ? OR e.subject LIKE ? OR e.content LIKE ?) " +
                        "ORDER BY s.sent_at DESC";

                pstmt = con.prepareStatement(sql);

                String search = "%" + keyword.trim() + "%";
                pstmt.setString(1, search);
                pstmt.setString(2, search);
                pstmt.setString(3, search);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                SentMailDTO sentMailDTO = new SentMailDTO();
                sentMailDTO.setReservationId(rs.getString("reservation_id"));
                sentMailDTO.setRecipientEmail(rs.getString("recipient_email"));
                sentMailDTO.setSubject(rs.getString("subject"));
                sentMailDTO.setContent(rs.getString("content"));
                sentMailDTO.setStatus(rs.getString("status"));
                sentMailDTO.setSentAt(rs.getTimestamp("sent_at"));
                sentMails.add(sentMailDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
        return sentMails;

    }


}

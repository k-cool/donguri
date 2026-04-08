package com.c1.donguri.post;

import com.c1.donguri.util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SentPostDAO {
    public static final SentPostDAO SENT_MAIL_DAO = new SentPostDAO();

    private SentPostDAO() {
    }


    public ArrayList<SentPostDTO> getSuccessSentMails(String userId, String keyword) {
        ArrayList<SentPostDTO> sentMails = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.DB_MANAGER.getConnection();

            String sql =
                    "SELECT user_id, " +
                            "       reservation_id, " +
                            "       recipient_email, " +
                            "       subject, " +
                            "       content, " +
                            "       status, " +
                            "       sent_at " +
                            "FROM ( " +
                            "    SELECT RAWTOHEX(r.from_id) AS user_id, " +
                            "           RAWTOHEX(r.reservation_id) AS reservation_id, " +
                            "           r.recipient_email, " +
                            "           e.subject, " +
                            "           e.content, " +
                            "           s.status, " +
                            "           s.created_at AS sent_at, " +
                            "           ROW_NUMBER() OVER ( " +
                            "               PARTITION BY r.reservation_id " +
                            "               ORDER BY s.created_at DESC " +
                            "           ) AS rn " +
                            "    FROM reservation r " +
                            "    JOIN email_content e ON r.email_content_id = e.email_content_id " +
                            "    JOIN send_log s ON r.reservation_id = s.reservation_id " +
                            "    WHERE r.from_id = HEXTORAW(?) " +
                            "      AND s.status = 'SUCCESS' ";

            boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

            if (hasKeyword) {
                sql += "AND (r.recipient_email LIKE ? OR e.subject LIKE ? OR e.content LIKE ?) ";
            }

            sql += ") " +
                    "WHERE rn = 1 " +
                    "ORDER BY sent_at DESC";

            pstmt = con.prepareStatement(sql);

            // 1번 파라미터: 로그인한 유저 userId
            pstmt.setString(1, userId);

            // 검색어가 있을 때만 2,3,4번 파라미터 세팅
            if (hasKeyword) {
                String search = "%" + keyword.trim() + "%";
                pstmt.setString(2, search);
                pstmt.setString(3, search);
                pstmt.setString(4, search);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                SentPostDTO dto = new SentPostDTO();
                dto.setUserId(rs.getString("user_id"));
                dto.setReservationId(rs.getString("reservation_id"));
                dto.setRecipientEmail(rs.getString("recipient_email"));
                dto.setSubject(rs.getString("subject"));
                dto.setContent(rs.getString("content"));
                dto.setStatus(rs.getString("status"));
                dto.setSentAt(rs.getTimestamp("sent_at"));

                sentMails.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

        return sentMails;
    }

    public SentPostDTO getSentMailDetail(String userId, String reservationId) {
        SentPostDTO sentMailDTO = new SentPostDTO();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.DB_MANAGER.getConnection();

            String sql =
                    "SELECT s.SEND_LOG_ID,\n" +
                            "       r.FROM_ID    AS user_id,\n" +
                            "       r.RESERVATION_ID,\n" +
                            "       r.RECIPIENT_EMAIL,\n" +
                            "       e.SUBJECT,\n" +
                            "       e.CONTENT,\n" +
                            "       s.STATUS,\n" +
                            "       s.CREATED_AT AS sent_at\n" +
                            "FROM RESERVATION R,\n" +
                            "     EMAIL_CONTENT E,\n" +
                            "     SEND_LOG S\n" +
                            "WHERE r.EMAIL_CONTENT_ID = e.EMAIL_CONTENT_ID\n" +
                            "  AND r.RESERVATION_ID = s.RESERVATION_ID\n" +
                            "  AND r.RESERVATION_ID = ?\n" +
                            "ORDER BY s.CREATED_AT DESC";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reservationId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                sentMailDTO = new SentPostDTO();
                sentMailDTO.setUserId(rs.getString("user_id"));
                sentMailDTO.setReservationId(rs.getString("reservation_id"));
                sentMailDTO.setRecipientEmail(rs.getString("recipient_email"));
                sentMailDTO.setSubject(rs.getString("subject"));
                sentMailDTO.setContent(rs.getString("content"));
                sentMailDTO.setStatus(rs.getString("status"));
                sentMailDTO.setSentAt(rs.getTimestamp("sent_at"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

        return sentMailDTO;
    }


}

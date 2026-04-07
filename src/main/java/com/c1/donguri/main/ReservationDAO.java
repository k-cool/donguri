package com.c1.donguri.main;

import com.c1.donguri.scheduler.EmailJobDTO;
import com.c1.donguri.util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReservationDAO {
    public static final ReservationDAO RESERVATION_DAO = new ReservationDAO();

    private ReservationDAO() {
    }

    public ArrayList<EmailJobDTO> getUndoneReservation() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT r.reservation_id,\n" +
                "       r.recipient_email,\n" +
                "       r.scheduled_date,\n" +
                "       e.subject,\n" +
                "       e.CONTENT,\n" +
                "       r.is_done\n" +
                "FROM reservation r\n" +
                "         JOIN email_content e ON r.email_content_id = e.email_content_id\n" +
                "WHERE r.is_done = 'N'\n" +
                "ORDER BY r.scheduled_date";


        try {
            ArrayList<EmailJobDTO> emailJobList = new ArrayList<>();

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();


            while (rs.next()) {
                emailJobList.add(
                        new EmailJobDTO(
                                rs.getString("reservation_id"),
                                rs.getString("recipient_email"),
                                rs.getTimestamp("scheduled_date"),
                                rs.getString("subject"),
                                rs.getString("CONTENT"),
                                rs.getString("is_done")
                        )

                );
            }

            System.out.println("조회된 리스트 개수 : " + emailJobList.size());

            return emailJobList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
        return null;
    }

    public EmailJobDTO getReservationById(String reservationId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT r.reservation_id," +
                "       r.recipient_email," +
                "       r.scheduled_date," +
                "       e.subject," +
                "       e.content," +
                "       r.is_done" +
                "FROM reservation r" +
                "         JOIN email_content e ON r.email_content_id = e.email_content_id" +
                "WHERE r.reservation_id = ? ";


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
                        rs.getString("is_done")
                );
            }

            return emailJob;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

        return null;
    }


    public void updateResult(String reservationId, boolean success, String errorMessage) {
        Connection con = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;


        try {
            con = DBManager.DB_MANAGER.getConnection();

            // 1. reservation 업데이트 (성공일 때만)

            if (success) {
                String sql1 = "UPDATE reservation SET is_done = 'Y' WHERE reservation_id = ?";
                pstmt1 = con.prepareStatement(sql1);
                pstmt1.setString(1, reservationId);
                pstmt1.executeUpdate();
            }

            //  2. send_Log 업데이트
            String sql2 = "INSERT INTO send_log "
                    + "(reservation_id, status, error_message) "
                    + "VALUES (?, ?, ?)";
            pstmt2 = con.prepareStatement(sql2);
            pstmt2.setString(1, reservationId);


            if (success) {
                pstmt2.setString(2, "SUCCESS");
                pstmt2.setString(3, null);
            } else {
                pstmt2.setString(2, "FAIL");
                pstmt2.setString(3, errorMessage);
            }
            pstmt2.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (pstmt2 != null) {
                    pstmt2.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            DBManager.DB_MANAGER.close(con, pstmt1, null);
        }
    }


}


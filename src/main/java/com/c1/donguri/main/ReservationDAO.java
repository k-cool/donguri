package com.c1.donguri.main;

import com.c1.donguri.scheduler.EmailDTO;
import com.c1.donguri.scheduler.EmailJobDTO;
import com.c1.donguri.scheduler.EmailScheduler;
import com.c1.donguri.user.UserDTO;
import com.c1.donguri.util.DBManager;
import com.c1.donguri.util.EmailSend;
import com.c1.donguri.util.EnvLoader;
import com.mchange.net.MailSender;
import com.mchange.net.ProtocolException;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mchange.net.SmtpUtils.sendMail;

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
                "ORDER BY r.scheduled_date ASC";


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

//
//    public boolean isOverdue(EmailJobDTO email) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        LocalDateTime scheduledTime = LocalDateTime.parse(email.getScheduled_date(), formatter);
//        LocalDateTime now = LocalDateTime.now();
//
//        return !scheduledTime.isAfter(now);
//    }

    public void updateResult(String reservationId, boolean success, String errorMessage) {
        Connection con = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;


        try {
            con = DBManager.DB_MANAGER.getConnection();
            con.setAutoCommit(false);       // 트랜잭션

            // 1. reservation 업데이트 (성공일 때만)

            if (success) {
                String sql1 = "UPDATE reservation SET is_done = 'Y' WHERE reservation_id = ?";
                pstmt1 = con.prepareStatement(sql1);
                pstmt1.setString(1, reservationId);
                pstmt1.executeUpdate();
            }

            //  2. send_Log 업데이트
            String sql2 = "UPDATE send_log SET status = ?, error_message = ? WHERE reservation_id = ?";
            pstmt2 = con.prepareStatement(sql2);

            if (success) {
                pstmt2.setString(1, "SUCCESS");
                pstmt2.setString(2, null);
            } else {
                pstmt2.setString(1, "FAIL");
                pstmt2.setString(2, errorMessage);
            }
            pstmt2.setString(3, reservationId);
            pstmt2.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt1, null);
            try {
                pstmt2.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void sendReservationEmail(ArrayList<EmailJobDTO> emailJobList) {


//        for (EmailJobDTO email : emailJobList) {
//            if (isOverdue(email)) {
//                EmailSend.EMAIL_SEND.send(
//                        email.getRecipientEmail(),
//                        email.getSubject(),
//                        email.getContent()
//                );
//                updateDone(email.getReservationId());
//            } else {
//                EmailScheduler.enrollJob();
//            }

    }

    //

}


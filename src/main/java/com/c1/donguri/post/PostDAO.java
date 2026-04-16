package com.c1.donguri.post;


import com.c1.donguri.util.DBManager;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostDAO {
    public static final PostDAO POST_DAO = new PostDAO();

    private PostDAO() {
    }

    public PostDTO getPost(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT R.RESERVATION_ID,\n" +
                "       R.SCHEDULED_DATE,\n" +
                "       R.RECIPIENT_EMAIL,\n" +
                "       E.SUBJECT,\n" +
                "       E.CONTENT,\n" +
                "       E.BGM_URL,\n" +
                "       E.COVER_IMG_URL AS COVER_IMG_URL_E,\n" +
                "       T.BG_COLOR,\n" +
                "       T.COVER_IMG_URL\n" +
                "FROM RESERVATION R,\n" +
                "     EMAIL_CONTENT E,\n" +
                "     TEMPLATE T\n" +
                "WHERE R.EMAIL_CONTENT_ID = E.EMAIL_CONTENT_ID\n" +
                "  AND E.TEMPLATE_ID = T.TEMPLATE_ID\n" +
                "  AND R.RESERVATION_ID = ?";

        String reservationId = request.getParameter("id");

        try {
            con = DBManager.DB_MANAGER.getConnection();

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reservationId);

            rs = pstmt.executeQuery();

            PostDTO post = null;

            while (rs.next()) {
                post = new PostDTO(
                        rs.getString("reservation_id"),
                        rs.getDate("scheduled_date"),
                        rs.getString("recipient_email"),
                        rs.getString("subject"),
                        rs.getString("content").replace("\n", "<br>"),
                        rs.getString("bgm_url"),
                        rs.getString("cover_img_url"),
                        rs.getString("bg_color"),
                        null
                );

                if (rs.getString("cover_img_url_e") != null) {
                    post.setCoverImgUrl(rs.getString("cover_img_url_e"));
                }
            }

            System.out.println(post.toString());

            return post;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

        return null;
    }
}

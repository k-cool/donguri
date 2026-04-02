package com.c1.donguri.user;

import com.c1.donguri.util.DBManager;
import com.c1.donguri.util.S3Uploader;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class UserDAO {
    public static final UserDAO USER_DAO = new UserDAO();

    private UserDAO() {
    }

    public void getAllUserList(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM users ORDER BY created_at DESC";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            ArrayList<UserDTO> userList = new ArrayList<>();

            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserId(rs.getString("user_id"));
                user.setNickname(rs.getString("nickname"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setProfileImgUrl(rs.getString("profile_img_url"));
                user.setCreatedAt(rs.getDate("created_at"));
                user.setUpdatedAt(rs.getDate("updated_at"));
                user.setIsDeleted(rs.getString("is_deleted"));

                userList.add(user);
            }

            request.setAttribute("userList", userList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
    }


    public void signUp(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;

//        String savePath = request.getServletContext().getRealPath("image");
//        int maxSize = 1024 * 1024 * 20;

        S3Uploader s3Uploader = new S3Uploader();


        try {
            // 1. 파라미터 추출 (request에서 직접 추출 가능)
            String email = request.getParameter("email");
            String nickname = request.getParameter("nickname");
            String password = request.getParameter("password");

            // 2. 파일 처리 (name="file" 인 input 태그 기준)
            Part filePart = request.getPart("file");
            String imgUrl = null; // 기본값 설정

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = "profile_img/" + UUID.randomUUID().toString();
                InputStream inputStream = filePart.getInputStream();
                String contentType = filePart.getContentType();
                long fileSize = filePart.getSize();

                // S3에 바로 업로드 (서버 로컬에 저장되지 않음)
                imgUrl = s3Uploader.upload(inputStream, fileName, contentType, fileSize);
            }

            con = DBManager.DB_MANAGER.getConnection();
            String sql = "INSERT INTO users(email,nickname,password,profile_img_url) VALUES (?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, email);
            pstmt.setString(2, nickname);
            pstmt.setString(3, password);
            pstmt.setString(4, imgUrl);

            if (pstmt.executeUpdate() == 1) {
                System.out.println("sign up success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("sign up fail");
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }
    }
}

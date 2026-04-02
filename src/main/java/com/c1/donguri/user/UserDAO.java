package com.c1.donguri.user;

import com.c1.donguri.util.DBManager;
import com.c1.donguri.util.S3Uploader;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    public void login(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            con = DBManager.DB_MANAGER.getConnection();

            String sql = "SELECT * FROM users WHERE email = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);

            rs = pstmt.executeQuery();

            String msg = null;
            if (rs.next()) {

                if (rs.getString("password").equals(password)) {

                    UserDTO user = new UserDTO();
                    user.setEmail(rs.getString("email"));
                    user.setNickname(rs.getString("nickname"));
                    user.setProfileImgUrl(rs.getString("profile_img_url")); // 이미지 경로도 세팅

                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setMaxInactiveInterval(30 * 60);

                    msg = "로그인 성공";
                    System.out.println(email + " login success");
                } else {
                    msg = "비밀번호가 일치하지 않습니다.";
                    System.out.println(email + " password error");
                }
            } else {
                msg = "존재하지 않는 계정입니다.";
                System.out.println(email + " user not found");
            }

            request.setAttribute("msg", msg);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "로그인 처리 중 서버 오류 발생");
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
    }

    public boolean loginCheck(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");

        if (user != null) {
            request.setAttribute("loginPage", "user/loginOK.jsp");
            return true;
        } else {
            request.setAttribute("loginPage", "user/login.jsp");
            return false;
        }
    }
}

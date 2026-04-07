package com.c1.donguri.user;

import com.c1.donguri.util.S3Uploader;

import javax.servlet.http.Part;
import java.io.InputStream;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@MultipartConfig(maxFileSize = 1024 * 1024 * 10) // 최대 10MB
@WebServlet({"/mypage", "/update-nickname", "/update-profile-img"})
public class MypageC extends HttpServlet {

    // 화면 띄우기는 기존과 동일하게 doGet에서 처리
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UserDAO.USER_DAO.loginCheck(request)) {
            request.getRequestDispatcher("jsp/user/mypage.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("jsp/user/login.jsp").forward(request, response);
        }
    }

    // Ajax 데이터 통신은 doPost에서 일괄 처리
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 현재 들어온 요청의 정확한 주소 확인
        String uri = request.getRequestURI();

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            out.print("{\"success\": false, \"message\": \"로그인이 만료되었습니다.\"}");
            out.flush();
            return;
        }

        // --- [기능 1] 닉네임 중복확인 및 업데이트 처리 ---
        if (uri.endsWith("/update-nickname")) {
            String newNickname = request.getParameter("nickname");

            // DAO로 중복 확인
            boolean isDuplicate = UserDAO.USER_DAO.checkNickname(newNickname);

            if (isDuplicate) {
                out.print("{\"success\": false, \"message\": \"이미 사용 중인 닉네임입니다.\"}");
            } else {
                // DAO로 DB 업데이트
                boolean isUpdated = UserDAO.USER_DAO.updateNickname(user.getEmail(), newNickname);

                if (isUpdated) {
                    // 세션 갱신
                    user.setNickname(newNickname);
                    session.setAttribute("user", user);
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false, \"message\": \"DB 수정 오류\"}");
                }
            }
        }

        // --- [기능 2] 프로필 이미지 업로드 처리 ---
        else if (uri.endsWith("/update-profile-img")) {
            try {
                Part filePart = request.getPart("profileImage");

                if (filePart != null && filePart.getSize() > 0) {
                    S3Uploader s3Uploader = new S3Uploader();
                    String fileName = "profile_img/" + UUID.randomUUID().toString();
                    InputStream inputStream = filePart.getInputStream();
                    String newImageUrl = s3Uploader.upload(inputStream, fileName,
                            filePart.getContentType(), filePart.getSize());

                    if (newImageUrl != null) {
                        boolean isUpdated = UserDAO.USER_DAO.updateProfileImg(user.getEmail(), newImageUrl);

                        if (isUpdated) {
                            user.setProfileImgUrl(newImageUrl);
                            session.setAttribute("user", user);
                            out.print("{\"success\": true, \"newImageUrl\": \"" + newImageUrl + "\"}");
                        } else {
                            out.print("{\"success\": false, \"message\": \"DB 업데이트 실패\"}");
                        }
                    } else {
                        out.print("{\"success\": false, \"message\": \"S3 업로드 실패\"}");
                    }
                } else {
                    out.print("{\"success\": false, \"message\": \"파일이 선택되지 않았습니다.\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.print("{\"success\": false, \"message\": \"서버 오류 발생\"}");
            }
        }

        out.flush();
    }
}
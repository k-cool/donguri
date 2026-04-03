package com.c1.donguri.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig
@WebServlet(name = "LoginC", value = "/login")
public class LoginC extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 로그인 상태인지 체크 (헤더 변경을 위함)
        UserDAO.USER_DAO.loginCheck(request);

        request.setAttribute("content", "user/login.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 1. 한글 인코딩 설정 (가장 먼저 수행)
        request.setCharacterEncoding("UTF-8");

        // 2. 로그인 로직 수행 (DB 조회 및 세션에 'user' 저장)
        UserDAO.USER_DAO.login(request);

        // 3. 로그인 성공 여부 확인
        if (UserDAO.USER_DAO.loginCheck(request)) {
            // [수정 포인트] 리다이렉트 대신 포워딩을 사용해 세션 상태를 즉시 반영합니다.
            // main.jsp가 로그인 성공 시 보여줄 페이지를 'content'로 지정합니다.
            request.setAttribute("content", "user/loginOK.jsp");

            // main.jsp로 직접 포워딩하여 현재 요청(Request)의 세션 정보를 바로 읽게 합니다.
            request.getRequestDispatcher("main.jsp").forward(request, response);
        } else {
            // 실패 시: 다시 로그인 페이지로
            request.setAttribute("content", "user/login.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);
        }
    }

    public void destroy() {
    }
}
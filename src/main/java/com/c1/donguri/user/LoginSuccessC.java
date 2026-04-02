package com.c1.donguri.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig
@WebServlet(name = "LoginSuccessC", value = "/login-success")
public class LoginSuccessC extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 1. 로그인 체크 (헤더에 loginOK.jsp를 띄우기 위해 필요)
        UserDAO.USER_DAO.loginCheck(request);

        // 2. 컨텐츠 영역에 성공 페이지 설정
        request.setAttribute("content", "user/loginSuccess.jsp");

        // 3. 메인 틀로 포워딩 [cite: 2026-04-02]
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    public void destroy() {
    }
}
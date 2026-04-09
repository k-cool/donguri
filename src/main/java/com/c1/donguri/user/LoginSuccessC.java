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
        // 1. 로그인 성공 후 이동
        request.setAttribute("content", "home.jsp");
        request.setAttribute("loginPage", "jsp/user/login_ok.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        
        // 1. 로그인 수행
        UserDAO.USER_DAO.login(request);
        
        // 2. 로그인 성공 여부 확인
        if (UserDAO.USER_DAO.loginCheck(request)) {
            // 성공 시: main으로 리다이렉트 (login_ok.jsp는 session.user로 자동 표시)
            response.sendRedirect("main");
        } else {
            // 실패 시: 다시 로그인 페이지로
            response.sendRedirect("login");
        }

    }

    public void destroy() {
    }
}
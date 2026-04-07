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
        // 1. 로그아웃 요청인지 확인
        String type = request.getParameter("type");

        if ("logout".equals(type)) {
            UserDAO.USER_DAO.logout(request);
            response.sendRedirect("main");
            return;
        }

        // 2. 일반 로그인 페이지 이동 시
        // 로그인 체크를 수행하되, 로그인 안 된 상태면 헤더(loginPage)를 비워줍니다.
        if (!UserDAO.USER_DAO.loginCheck(request)) {
            request.setAttribute("loginPage", null);
        }

        // 중앙 컨텐츠 영역에만 로그인 페이지를 띄웁니다.
        request.setAttribute("content", "jsp/user/login.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        // 1. 로그인 수행 (세션에 "user" 저장 및 1분 만료 설정)
        UserDAO.USER_DAO.login(request);

        // 2. 로그인 성공 여부 확인
        if (UserDAO.USER_DAO.loginCheck(request)) {
            // 성공 시: 중앙에는 홈 화면, 헤더에는 로그인 정보창을 띄웁니다.
            request.setAttribute("content", "home.jsp");
            request.setAttribute("loginPage", "jsp/user/login_ok.jsp");

            request.getRequestDispatcher("main.jsp").forward(request, response);
        } else {
            // 실패 시: 헤더는 비우고 중앙에만 다시 로그인 창을 띄웁니다.
            request.setAttribute("loginPage", null);
            request.setAttribute("content", "jsp/user/login.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);
        }
    }

    public void destroy() {
    }
}
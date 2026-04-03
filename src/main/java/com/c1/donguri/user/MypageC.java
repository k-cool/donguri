package com.c1.donguri.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig
@WebServlet(name = "MypageC", value = "/mypage")
public class MypageC extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (UserDAO.USER_DAO.loginCheck(request)) {
            request.getRequestDispatcher("user/mypage.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("user/login.jsp").forward(request, response);
        }

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    public void destroy() {
    }
}
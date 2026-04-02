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
        UserDAO.USER_DAO.loginCheck(request);

        request.setAttribute("content", "user/login.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        UserDAO.USER_DAO.login(request);

        if (UserDAO.USER_DAO.loginCheck(request)) {
            response.sendRedirect("login-success");
        } else {
            request.setAttribute("content", "user/login.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);
        }
    }

    public void destroy() {
    }
}
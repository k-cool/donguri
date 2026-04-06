package com.c1.donguri.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig
@WebServlet(name = "SignUpSuccessC", value = "/signup-success")
public class SignUpSuccessC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserDAO.USER_DAO.loginCheck(request);

        request.setAttribute("loginPage", null);

        request.setAttribute("content", "user/signup_success.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


    }

    public void destroy() {
    }
}
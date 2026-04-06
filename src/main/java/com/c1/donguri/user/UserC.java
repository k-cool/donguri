package com.c1.donguri.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;
import com.c1.donguri.user.UserDAO;

@WebServlet(name = "user", value = {"/user"})
public class UserC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserDAO.USER_DAO.getAllUserList(request);

        request.setAttribute("content", "home.jsp");

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // UserC는 /user 경로만 처리
        super.service(req, resp);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    public void destroy() {
    }
}
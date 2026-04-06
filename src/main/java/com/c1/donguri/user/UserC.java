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

@WebServlet(name = "user", value = {"/user", "/check-email-duplicate", "/send-verification-email"})
public class UserC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserDAO.USER_DAO.getAllUserList(request);

        request.setAttribute("content", "home.jsp");

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/check-email-duplicate".equals(path)) {
            checkEmailDuplicate(req, resp);
        } else if ("/send-verification-email".equals(path)) {
            sendVerificationEmail(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    private void checkEmailDuplicate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        boolean exists = UserDAO.USER_DAO.checkEmailExists(email);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new EmailCheckResponse(exists));
        out.print(jsonResponse);
        out.flush();
    }

    private void sendVerificationEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        // 임시로 이메일 발송 기능 비활성화
        boolean success = false;
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new EmailSendResponse(success));
        out.print(jsonResponse);
        out.flush();
    }

    static class EmailCheckResponse {
        boolean exists;

        public EmailCheckResponse(boolean exists) {
            this.exists = exists;
        }
    }

    static class EmailSendResponse {
        boolean success;

        public EmailSendResponse(boolean success) {
            this.success = success;
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    public void destroy() {
    }
}
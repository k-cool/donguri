package com.c1.donguri.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;

@WebServlet(name = "EmailVerifyC", value = {"/email-send", "/email-verify"})
public class EmailVerifyC extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/email-send".equals(path)) {
            sendVerificationEmail(req, resp);
        } else if ("/email-verify".equals(path)) {
            verifyEmailCode(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    private void sendVerificationEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        
        // 이메일 발송을 비동기로 처리하여 즉시 응답
        new Thread(() -> {
            UserDAO.USER_DAO.sendVerificationEmail(email, request);
        }).start();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new EmailSendResponse(true));
        out.print(jsonResponse);
        out.flush();
    }

    private void verifyEmailCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String code = request.getParameter("code");
        boolean verified = UserDAO.USER_DAO.verifyCode(email, code, request);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new EmailVerifyResponse(verified));
        out.print(jsonResponse);
        out.flush();
    }

    static class EmailSendResponse {
        boolean success;

        public EmailSendResponse(boolean success) {
            this.success = success;
        }
    }

    static class EmailVerifyResponse {
        boolean verified;

        public EmailVerifyResponse(boolean verified) {
            this.verified = verified;
        }
    }
}

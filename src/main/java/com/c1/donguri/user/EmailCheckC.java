package com.c1.donguri.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;

@WebServlet(name = "EmailCheckC", value = {"/email-check"})
public class EmailCheckC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    static class EmailCheckResponse {
        boolean exists;

        public EmailCheckResponse(boolean exists) {
            this.exists = exists;
        }
    }
}

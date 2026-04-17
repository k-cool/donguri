package com.c1.donguri.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

@WebServlet(name = "NicknameCheckC", value = "/nickname-check")
public class NicknameCheckC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String nickname = request.getParameter("nickname");
        boolean exists = UserDAO.USER_DAO.checkNickname(nickname);

        System.out.println(nickname);


        // JSON 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Gson gson = new Gson();
        String json = gson.toJson(new Response(exists));
        out.print(json);
        out.flush();
    }

    // 응답 클래스
    static class Response {
        boolean exists;

        public Response(boolean exists) {
            this.exists = exists;
        }
    }
}

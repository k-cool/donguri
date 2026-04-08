package com.c1.donguri.omikuji;

import com.c1.donguri.user.UserDAO;
import com.c1.donguri.util.EmailSend;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "omikuji", value = "/omikuji")
public class OmikujiC extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        boolean isLoggedIn = UserDAO.USER_DAO.loginCheck(request);

        if (!isLoggedIn) {
            response.sendRedirect("login");
            return;
        }

        boolean isOmikujiAvailable = OmikujiDAO.OMIKUJI_DAO.getIsOmikujiAvailable(request);

        request.setAttribute("isOmikujiAvailable", isOmikujiAvailable);

        System.out.println("isOmikujiAvailable: " + isOmikujiAvailable);

        request.setAttribute("content", "jsp/omikuji/omikuji.jsp");

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        boolean isOmikujiAvailable = OmikujiDAO.OMIKUJI_DAO.getIsOmikujiAvailable(request);

        if (isOmikujiAvailable) {
            OmikujiDTO omikuji = OmikujiDAO.OMIKUJI_DAO.drawOmikuji(request);

            JsonObject object = new JsonObject();
            object.add("omikuji", omikuji.toJsonObject());

            response.getWriter().println(object);
        } else {
            response.sendError(409, "이미 뽑았어요!");
        }
    }

    public void destroy() {
    }
}
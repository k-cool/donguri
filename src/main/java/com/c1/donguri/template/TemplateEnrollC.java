package com.c1.donguri.template;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TemplateEnrollC", value = "/template-enroll")
public class TemplateEnrollC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setAttribute("content", "jsp/template/template_enroll.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.sendRedirect("template-list");

    }

    public void destroy() {
    }
}
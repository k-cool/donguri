package com.c1.donguri.template;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TemplateRegC", value = "/template-reg")
public class TemplateRegC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setAttribute("content", "jsp/template/template_reg.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (TemplateDAO.TEMPLATE_DAO.addTemplate(request) == 1) {
            System.out.println("등록 성공");
        } else {
            System.out.println("등록 실패");
        }

        response.sendRedirect("template-list");

    }

    public void destroy() {
    }
}
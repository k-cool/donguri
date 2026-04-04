package com.c1.donguri.template;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TemplateDetailC", value = "/template-detail")
public class TemplateDetailC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        TemplateDTO t = TemplateDAO.TEMPLATE_DAO.getTemplateDetail(request);
        request.setAttribute("t", t);

        request.setAttribute("content", "jsp/template/template_detail.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    public void destroy() {
    }
}
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

        // 1. 기존 데이터 가져오기 (TemplateDTO 객체 반환)
        TemplateDTO t = TemplateDAO.TEMPLATE_DAO.getTemplateDetail(request);
        request.setAttribute("t", t);

        // 2. 관리자 뷰 판별 로직 추가
        // URL에 ?type=admin 이 붙어있으면 isAdminView를 true로 세팅.
        String type = request.getParameter("type");
        if ("admin".equals(type)) {
            request.setAttribute("isAdminView", true);
        }

        request.setAttribute("content", "jsp/template/template_detail.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    public void destroy() {
    }
}
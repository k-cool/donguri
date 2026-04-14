package com.c1.donguri.template;

import com.c1.donguri.user.UserDAO;
import com.c1.donguri.user.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TemplateDetailAdminC", value = "/template-detail-admin")
@MultipartConfig
public class TemplateDetailAdminC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        boolean isLoggedIn = UserDAO.USER_DAO.loginCheck(request);

        if (!isLoggedIn) {
            response.sendRedirect("login");
            return;
        }
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");

        boolean isAdmin = UserDAO.USER_DAO.isAdmin(user.getEmail());

        if (!isAdmin) {
            response.sendRedirect("main");
            return;
        }
        // 1. 기존 데이터 가져오기 (TemplateDTO 객체 반환)
        TemplateDTO template = TemplateDAO.TEMPLATE_DAO.getTemplateDetail(request);
        request.setAttribute("t", template);

        // 2. 관리자 뷰 판별 로직 추가
        // URL에 ?type=admin 이 붙어있으면 isAdminView를 true로 세팅.
//        String type = request.getParameter("type");
//        String mode = request.getParameter("mode");

//        if ("admin".equals(type)) {
//            request.setAttribute("isAdminView", true);
//        }
//
//        // 2. 모드에 따라 페이지 결정
//        if ("edit".equals(mode)) {
//            // 수정 화면 JSP (input 태그들이 있는 곳)
//            request.setAttribute("content", "jsp/template/template_update_admin.jsp");
//        } else {
//            // 일반 상세 보기 화면 JSP
//            request.setAttribute("content", "jsp/template/template_detail.jsp");
//        }

        request.setAttribute("content", "jsp/template/template_detail_admin.jsp");

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        TemplateDAO.TEMPLATE_DAO.updateTemplate(request);

        String templateId = request.getParameter("templateId");

        response.sendRedirect("/template-list?type=admin");
    }

    public void destroy() {
    }
}
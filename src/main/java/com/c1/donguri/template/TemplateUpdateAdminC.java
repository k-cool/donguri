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

@WebServlet(name = "TemplateUpdateAdminC", value = "/template-update-admin")
@MultipartConfig
public class TemplateUpdateAdminC extends HttpServlet {

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

        TemplateDTO template = TemplateDAO.TEMPLATE_DAO.getTemplateDetail(request);

        request.setAttribute("t", template);

        request.setAttribute("content", "jsp/template/template_update_admin.jsp");

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        TemplateDAO.TEMPLATE_DAO.updateTemplate(request);

        response.sendRedirect("template-list-admin");
    }

    public void destroy() {
    }
}
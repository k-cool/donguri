package com.c1.donguri.template;

import com.c1.donguri.user.UserDAO;
import com.c1.donguri.user.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TemplateDeleteAdminC", value = "/template-delete-admin")
public class TemplateDeleteAdminC extends HttpServlet {

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
        TemplateDAO.TEMPLATE_DAO.deleteTemplate(request);

        response.sendRedirect("/template-list-admin");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    public void destroy() {
    }
}
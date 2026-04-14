package com.c1.donguri.template;

import com.c1.donguri.user.UserDAO;
import com.c1.donguri.user.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TemplateAdminC", value = "/template-list-admin")
public class TemplateListAdminC extends HttpServlet {

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

        TemplateDAO.TEMPLATE_DAO.getTemplateList(request);
        request.setAttribute("content", "jsp/template/template_list_admin.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    public void destroy() {
    }
}
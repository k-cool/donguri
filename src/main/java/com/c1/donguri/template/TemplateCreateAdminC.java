package com.c1.donguri.template;

import com.c1.donguri.user.UserDAO;
import com.c1.donguri.user.UserDTO;
import com.c1.donguri.util.QRGenerator;
import com.c1.donguri.util.S3Uploader;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@MultipartConfig
@WebServlet(name = "TemplateCreateAdminC", value = "/template-create-admin")
public class TemplateCreateAdminC extends HttpServlet {

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

        request.setAttribute("content", "jsp/template/template_create_admin.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String type = request.getParameter("type");

        if ("ADDED".equals(type)) {
            TemplateDAO.TEMPLATE_DAO.addQRTemplate(request);
        } else {
            TemplateDAO.TEMPLATE_DAO.addTemplate(request);
        }

        response.sendRedirect("template-list-admin");

    }

    public void destroy() {
    }
}
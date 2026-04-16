package com.c1.donguri.inquiry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet({"/inquiry-admin"})
public class InquiryAdminC extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<InquiryDTO> list = InquiryDAO.selectAllinquiry(request, response);

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        request.setAttribute("list", list);

        request.setAttribute("content", "jsp/inquiry/inquiry_admin.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
//        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        response.sendRedirect(request.getContextPath() + "/admin/inquiry");
    }


}


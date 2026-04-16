package com.c1.donguri.inquiry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteInquiry")
public class DeleteInquiryC extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String no = request.getParameter("no");

        InquiryDAO dao = new InquiryDAO();

        dao.delete(no);

        response.sendRedirect(request.getContextPath() + "/inquiry-admin");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.sendRedirect(request.getContextPath() + "/admin/inquiry");
    }

}

package com.c1.donguri.inquiry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/inquiry")
public class InquiryC extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String message = request.getParameter("message");


        InquiryDTO inquiry = new InquiryDTO();
        inquiry.setName(name);
        inquiry.setPhone(phone);
        inquiry.setEmail(email);
        inquiry.setMessage(message);


        InquiryDAO dao = new InquiryDAO();
        try {
            dao.insert(inquiry);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("msg", "정상 접수되었습니다.");

        request.setAttribute("content", "jsp/inquiry/inquiry.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
//        request.getRequestDispatcher("jsp/inquiry/inquiry.jsp").forward(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("content", "jsp/inquiry/inquiry.jsp");

        request.getRequestDispatcher("main.jsp").forward(request, response);

    }

    public void destroy() {

    }
}

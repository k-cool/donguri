package com.c1.donguri.scheduler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "sentMailDetail", value = "/sent-mail-detail")
public class SentMailDetailC extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");

        String reservationId = request.getParameter("reservationId");
        String keyword = request.getParameter("keyword");

        HttpSession session = request.getSession();
//        String userId = (String) session.getAttribute("userId");
        // 테스트용 값
        String userId = "F90D905CC65E464482848C0884788A47";

        // 비로그인 상태일 경우 로그인 창으로 넘겨주는 기능
//        if (userId == null) {
//            response.sendRedirect("login.jsp");
//            return;
//        }

        SentMailDTO sentMail =
                SentMailDAO.SENT_MAIL_DAO.getSentMailDetail(userId, reservationId);

        System.out.println("reservationId = " + reservationId);
        System.out.println("userId = " + userId);
        System.out.println("sentMail = " + sentMail);

        request.setAttribute("sentMail", sentMail);
        request.setAttribute("keyword", keyword); // 뒤로가기용 검색어 유지
        request.setAttribute("content", "jsp/sentMailDetail.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


    }

    public void destroy() {
    }
}
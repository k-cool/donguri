package com.c1.donguri.post;

import com.c1.donguri.user.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "sentMailDetail", value = "/sent-post-detail")
public class SentPostDetailC extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");

        String reservationId = request.getParameter("reservationId");
        String keyword = request.getParameter("keyword");

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        String userId = user.getUserId();

        SentPostDTO sentMail =
                SentPostDAO.SENT_MAIL_DAO.getSentMailDetail(userId, reservationId);

        System.out.println("reservationId = " + reservationId);
        System.out.println("userId = " + userId);
        System.out.println("sentMail = " + sentMail);

        request.setAttribute("sentMail", sentMail);
        request.setAttribute("keyword", keyword); // 뒤로가기용 검색어 유지
        request.setAttribute("content", "jsp/sentMailDetail.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);

    }

    public void destroy() {
    }
}
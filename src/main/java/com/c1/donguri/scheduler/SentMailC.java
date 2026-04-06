package com.c1.donguri.scheduler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "sentMail", value = "/sentMail")
public class SentMailC extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");

// 로그인 유저 가져오기
//        HttpSession session = request.getSession();
//        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");
//
//        if (loginUser == null) {
//            response.sendRedirect("login.jsp");
//            return;
//        }
//
//        String userId = loginUser.getUserId(); // HEX 문자열

        ArrayList<SentMailDTO> sentMails =
//                SentMailDAO.SENT_MAIL.getSuccessSentMails(getUserId(), keyword);
                SentMailDAO.SENT_MAIL.getSuccessSentMails(keyword);

        request.setAttribute("sentMails", sentMails);
        request.setAttribute("keyword", keyword);

        request.setAttribute("content", "jsp/sentMail.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    public void destroy() {
    }
}
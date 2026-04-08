package com.c1.donguri.post;

import com.c1.donguri.user.UserDAO;
import com.c1.donguri.user.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "sentMail", value = "/sent-post")
public class SentPostC extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");


        if (UserDAO.USER_DAO.loginCheck(request)) {
            // 로그인된 사용자만 접근 가능한 기능
            UserDTO user = (UserDTO) request.getSession().getAttribute("user");
            ArrayList<SentPostDTO> sentMails =
                    SentPostDAO.SENT_MAIL_DAO.getSuccessSentMails(user.getUserId(), keyword);

            System.out.println("user : " + user);
            System.out.println("userId : " + (user != null ? user.getUserId() : null));

            request.setAttribute("sentMails", sentMails);
            request.setAttribute("keyword", keyword);

            request.setAttribute("content", "jsp/sentMail.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);
        } else {
            response.sendRedirect("login");
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    public void destroy() {
    }
}
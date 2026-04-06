package com.c1.donguri.scheduler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "sentMail", value = "/sentMail")
public class SentMailC extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");

        ArrayList<SentMailDTO> sentMails =
                SentMailDAO.SENT_MAIL.getSuccessSentMails(keyword);

        request.setAttribute("sentMails", sentMails);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/jsp/sentMail.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    public void destroy() {
    }
}
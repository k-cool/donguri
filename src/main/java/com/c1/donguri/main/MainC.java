package com.c1.donguri.main;

import com.c1.donguri.util.EmailSend;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "main", value = "/main")
public class MainC extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("한글 깨지니?");
//        try {
//            EmailSend.EMAIL_SEND.send("ksw3721@gmail.com", "HAMBURGER", "<h2>배고팡</h2>");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        request.setAttribute("content", "home.jsp");

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    public void destroy() {
    }
}
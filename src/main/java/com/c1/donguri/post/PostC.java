package com.c1.donguri.post;


import com.c1.donguri.util.EmailSend;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "post", value = "/post")
public class PostC extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("post", PostDAO.POST_DAO.getPost(request));

        request.setAttribute("content", "jsp/post/post.jsp");

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    public void destroy() {
    }
}
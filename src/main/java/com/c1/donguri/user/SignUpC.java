package com.c1.donguri.user;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig

@WebServlet(name = "SignUpC", value = "/signup-do")
public class SignUpC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserDAO.USER_DAO.loginCheck(request);

        request.setAttribute("loginPage", null);

        request.setAttribute("content", "jsp/user/signup.jsp");

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        try {
            UserDAO.USER_DAO.signUp(request);
            response.sendRedirect("signup-success");
        } catch (IllegalArgumentException e) {
            // 비밀번호 유효성 검증 실패 시
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("content", "jsp/user/signup.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);
        } catch (Exception e) {
            // 기타 오류 시
            e.printStackTrace();
            request.setAttribute("errorMessage", "회원가입에 실패했습니다. 다시 시도해주세요.");
            request.setAttribute("content", "jsp/user/signup.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);
        }
    }

    public void destroy() {
    }
}
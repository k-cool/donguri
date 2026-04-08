package com.c1.donguri.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UserDeleteC", value = "/user-delete")
public class UserDeleteC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!UserDAO.USER_DAO.loginCheck(request)) {
            response.sendRedirect("login");
            return;
        }
        
        request.getRequestDispatcher("jsp/user/user_delete.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!UserDAO.USER_DAO.loginCheck(request)) {
            response.sendRedirect("login");
            return;
        }
        
        String password = request.getParameter("password");
        
        if (password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "비밀번호를 입력해주세요.");
            request.setAttribute("content", "jsp/user/user_delete.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);
            return;
        }
        
        // 비밀번호 확인 후 회원 탈퇴 처리
        boolean isDeleted = UserDAO.USER_DAO.deleteUser(request, password);
        
        if (isDeleted) {
            // 회원 탈퇴 성공 시 세션 무효화하고 홈으로 리다이렉트
            request.getSession().invalidate();
            response.sendRedirect("user-delete-success");
        } else {
            request.setAttribute("errorMessage", "비밀번호가 올바르지 않습니다.");
            request.getRequestDispatcher("jsp/user/user_delete.jsp").forward(request, response);
        }
    }

    public void destroy() {
    }
}

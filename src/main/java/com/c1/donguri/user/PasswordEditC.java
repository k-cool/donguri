package com.c1.donguri.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "PasswordEditC", value = "/password-edit")
public class PasswordEditC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!UserDAO.USER_DAO.loginCheck(request)) {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
            response.sendRedirect("login");
            return;
        }

        request.setAttribute("content", "user/password_edit.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!UserDAO.USER_DAO.loginCheck(request)) {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
            response.sendRedirect("login");
            return;
        }

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // 비밀번호 유효성 검사
        if (currentPassword == null || currentPassword.isEmpty() ||
                newPassword == null || newPassword.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {

            request.setAttribute("errorMessage", "모든 필드를 입력해주세요.");
            request.setAttribute("content", "user/password_edit.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다.");
            request.setAttribute("content", "user/password_edit.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 8) {
            request.setAttribute("errorMessage", "비밀번호는 최소 8자 이상이어야 합니다.");
            request.setAttribute("content", "user/password_edit.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);
            return;
        }

        // 비밀번호 변경 처리
        boolean isUpdated = UserDAO.USER_DAO.updatePassword(request, currentPassword, newPassword);

        if (isUpdated) {
            request.setAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
            request.setAttribute("content", "user/mypage.jsp");
        } else {
            request.setAttribute("errorMessage", "현재 비밀번호가 올바르지 않습니다.");
            request.setAttribute("content", "user/password_edit.jsp");
        }

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void destroy() {
    }
}

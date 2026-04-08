package com.c1.donguri.template;

import com.c1.donguri.user.UserDAO;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig; // 꼭 확인!
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@MultipartConfig // 파일 업로드를 위해 반드시 필요
@WebServlet(name = "QRDecodeC", value = "/qr-decode")
public class QRDecodeC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        boolean isLoggedIn = UserDAO.USER_DAO.loginCheck(request);

        if (!isLoggedIn) {
            response.sendRedirect("login");
            return;
        }

        request.setAttribute("content", "jsp/template/template_qr_upload.jsp");

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        boolean isLoggedIn = UserDAO.USER_DAO.loginCheck(request);

        if (!isLoggedIn) {
            response.sendRedirect("login");
            return;
        }

        String redirectUrl = TemplateDAO.TEMPLATE_DAO.decodeQrImage(request, response);

        response.sendRedirect(redirectUrl);
    }

    public void destroy() {
    }
}
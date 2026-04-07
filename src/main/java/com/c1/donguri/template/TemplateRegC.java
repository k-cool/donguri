package com.c1.donguri.template;

import com.c1.donguri.util.QRGenerator;
import com.c1.donguri.util.S3Uploader;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@MultipartConfig
@WebServlet(name = "TemplateRegC", value = "/template-reg")
public class TemplateRegC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setAttribute("content", "jsp/template/template_reg.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        // 1. 폼 데이터 받기
        String name = request.getParameter("name");
        String type = request.getParameter("type");

        // 2. [중요] 고유 ID를 여기서 미리 만듭니다. (DB PK이자 QR 파라미터가 됨)
        String templateId = java.util.UUID.randomUUID().toString().replace("-", "").toUpperCase();

        String s3Url = null;

        // 3. ADDED 타입일 때만 QR 자동 생성
        if ("ADDED".equals(type)) {
            // 유저가 스캔했을 때 실행될 주소를 조립 (방금 만든 ID를 붙임)
            String myIP = "10.1.82.127";
            String targetUrl = "http://" + myIP + ":8080/qr?t_id=" + templateId;

            // 바탕화면 경로 설정
            String path = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "qr";
            // 폴더가 없으면 에러나니까 안전장치 추가
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();

            // 큐알 생성

            String prFileName = QRGenerator.QR_GENERATOR.generateQR(targetUrl, path);

            // 3. [QRC에서 가져온 로직] S3업로드
            S3Uploader s3Uploader = new S3Uploader();
            s3Url = s3Uploader.uploadLocalFile(path, prFileName);

            // 4. DB 저장 (DAO 호출)
            // DAO에서 templateId와 qrUrl을 꺼내 쓸 수 있게 담아줍니다.
            request.setAttribute("templateId", templateId);
            request.setAttribute("qrUrl", s3Url);

            TemplateDAO.TEMPLATE_DAO.addQRTemplate(request);

            // 5. 목록으로 이동
            response.sendRedirect("template-list");
        }
    }

    public void destroy() {
    }
}
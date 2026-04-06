package com.c1.donguri.template;

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
        // 업로드 페이지로 이동
        request.setAttribute("content", "jsp/template/template_qr_upload.jsp");
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String userId = "6483EC9A21894051A75A5D15EACE13D3"; // 고정 유저 ID
        String templateId = null;

        try {
            // 1. 업로드된 파일 받기
            Part filePart = request.getPart("qrFile");
            if (filePart == null || filePart.getSize() == 0) {
                response.sendRedirect("qr-decode?error=nofile");
                return;
            }

            InputStream is = filePart.getInputStream();

            // 2. 이미지 해석 (Zxing 라이브러리 사용)
            BufferedImage bufferedImage = ImageIO.read(is);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // 3. QR 디코딩
            Result result = new MultiFormatReader().decode(bitmap);
            String decodedUrl = result.getText(); // 예: http://.../qr?t_id=UUID

            System.out.println(decodedUrl);

            
            // 4. URL에서 t_id 파라미터 추출
            if (decodedUrl != null && decodedUrl.contains("t_id=")) {
                templateId = decodedUrl.substring(decodedUrl.indexOf("t_id=") + 5);
            }

            // 5. DB 해금 로직 실행
            if (templateId != null && TemplateDAO.TEMPLATE_DAO.unlockTemplate(userId, templateId)) {
                System.out.println("PC 해금 성공: " + templateId);
                response.sendRedirect("template-user?unlocked=true");
            } else {
                System.out.println("해금 실패 또는 이미 보유 중");
                response.sendRedirect("template-user?error=already");
            }

        } catch (NotFoundException e) {
            System.out.println("QR 코드를 인식할 수 없습니다.");
            response.sendRedirect("qr-decode?error=notfound");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("qr-decode?error=fail");
        }
    }

    public void destroy() {
    }
}
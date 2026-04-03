package com.c1.donguri.template;

import com.c1.donguri.user.UserDTO;
import com.c1.donguri.util.DBManager;
import com.c1.donguri.util.S3Uploader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class TemplateDAO {
    public static final TemplateDAO TEMPLATE_DAO = new TemplateDAO();

    private TemplateDAO() {
    }

    // 탬플릿 전체 조회하기
    public void getTemplateList(HttpServletRequest request) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // 추가된 엽서는 내림차순(최신순)으로 정렬
        String sql = "SELECT * FROM template ORDER BY created_at DESC";

        try {

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            TemplateDTO templateDTO = null;

            ArrayList<TemplateDTO> templateList = new ArrayList<>();

            while (rs.next()) {

                templateDTO = new TemplateDTO();

                templateDTO.setTemplateId(rs.getString("template_id"));
                templateDTO.setName(rs.getString("name"));
                templateDTO.setBodyHtml(rs.getString("body_html"));
                templateDTO.setType(rs.getString("type"));
                templateDTO.setCoverImgUrl(rs.getString("cover_img_url"));
                templateDTO.setQrUrl(rs.getString("qr_url"));
                templateDTO.setCreatedAt(rs.getString("created_at"));
                templateDTO.setCreatedAt(rs.getString("updated_at"));

                templateList.add(templateDTO);

            }

            request.setAttribute("templateList", templateList);
            System.out.println("템플릿 로드 성공: " + templateList.size() + "개");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

    }

    public void addTemplate(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        S3Uploader s3Uploader = new S3Uploader();

//      String sql = "INSERT INTO TEMPLATE (name, body_html, type, cover_img_url, qr_url, created_at, updated_at) "
//                + "VALUES (?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";

        try {
            // 1. 파라미터 추출
            String name = request.getParameter("name");
            String bodyHtml = request.getParameter("bodyHtml");
            String type = request.getParameter("type"); // BASE 또는 ADDED(QR ver.)

            // 2. 고유 ID 생성 (QR 주소와 DB PK로 공통 사용)
            String templateId = UUID.randomUUID().toString().replace("-", "");

            // 3. 커버 이미지 처리 (S3 업로드)
            Part filePart = request.getPart("coverImgUrl");
            String imgUrl = null;

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = "cover_img/template/" + UUID.randomUUID().toString();
                InputStream inputStream = filePart.getInputStream();
                String contentType = filePart.getContentType();
                long fileSize = filePart.getSize();

                // S3에 바로 업로드 (서버 로컬에 저장되지 않음)
                imgUrl = s3Uploader.upload(inputStream, fileName, contentType, fileSize);
            }

            // 4. QR 코드 처리 (타입이 ADDED일 때만 실행)
            String qrUrl = null; // 기본값은 null
            if ("ADDED".equals(type)) {
                // QR 생성
                String targetUrl = "http://10.1.82.127:8080/donguri/template.enroll?id=" + templateId;
                String tempPath = request.getServletContext().getRealPath("img");

                // 로컬에 임시 QR 생성
                String qrLocalFileName = QRGenerator.generateQR(targetUrl, tempPath);
                java.io.File qrFile = new java.io.File(tempPath + "/" + qrLocalFileName);

                // [S3 업로드] QR 이미지를 S3에 올리기
                String qrS3Key = "qr_codes/" + qrLocalFileName;
                qrUrl = s3Uploader.upload(new java.io.FileInputStream(qrFile), qrS3Key, "image/png", qrFile.length());

                // 업로드 후 로컬 임시 파일 삭제
                if (qrFile.exists()) qrFile.delete();
                System.out.println("히든 템플릿 QR 생성 완료! 🐿️");
            }


            String sql = "INSERT INTO TEMPLATE (template_id, name, body_html, type, cover_img_url, qr_url) VALUES (HEXTORAW(?), ?,?,?,?,?)";

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);

            // 3. 데이터 세팅 (중요: mr.getParameter를 사용해야 함!)
            pstmt.setString(1, templateId);
            pstmt.setString(2, name);
            pstmt.setString(3, bodyHtml);
            pstmt.setString(4, type);
            pstmt.setString(5, imgUrl);
            pstmt.setString(6, qrUrl);

            // [QR URL 빈 값 처리] 관리자가 비워뒀을 때 에러 안 나게 기본값 세팅
//            String qr = mr.getParameter("qrUrl");
//            if (qr == null || qr.trim().isEmpty()) {
//                qr = "qr_default.png"; // 혹은 "None" 등 원하는 기본값
//            }
//            pstmt.setString(6, qr);

            if (pstmt.executeUpdate() == 1) {
                System.out.println("템플릿 등록 성공! 타입: " + type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }
    }

    // 유저가 QR을 찍었을 때 유저 탬플릿 보관함(user_template)에 추가하는 로직
    public int enrollTemplate(String userId, String templateId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 1. 이미 내 보관함에 있는지 확인하는 SQL
        String checkSql = "SELECT COUNT(*) FROM USER_TEMPLATE WHERE user_id = ? AND template_id = HEXTORAW(?)";
        // 2. 담는 SQL
        String insertSql = "INSERT INTO USER_TEMPLATE (user_id, template_id) VALUES (?, HEXTORAW(?))";

        try {
            con = DBManager.DB_MANAGER.getConnection();

            // [체크 단계]
            pstmt = con.prepareStatement(checkSql);
            pstmt.setString(1, userId);
            pstmt.setString(2, templateId);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("이미 보관함에 있는 템플릿입니다! 🐿️");
                return 2; // 이미 존재함을 알리는 별도의 리턴값 (예: 2)
            }

            // [등록 단계] pstmt 재사용을 위해 닫고 다시 열기
            pstmt.close();
            pstmt = con.prepareStatement(insertSql);
            pstmt.setString(1, userId);
            pstmt.setString(2, templateId);

            return pstmt.executeUpdate(); // 성공하면 1 반환

        } catch (Exception e) {
            e.printStackTrace();
            return 0; // 에러 발생 시 0 반환
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
    }

    public TemplateDAO getTemplateDetail() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM template WHERE template_id = HEXTORAW(?)";
        TemplateDTO templateDTO = null;
        ArrayList<TemplateDTO> templaeList = new  ArrayList<>();

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, request.getParameter("no"));

            rs = pstmt.executeQuery();



        }

}


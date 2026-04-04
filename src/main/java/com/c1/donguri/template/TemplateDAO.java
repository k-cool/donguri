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

    // 어드민 페이지 탬플릿 리스트 전체 조회
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

    // 어드민 페이지 템플릿 상세 조회
    public TemplateDTO getTemplateDetail(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM template WHERE template_id = HEXTORAW(?)";
        TemplateDTO templateDTO = null;
        ArrayList<TemplateDTO> templaeList = new ArrayList<>();

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, request.getParameter("no"));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                templateDTO = new TemplateDTO();
                templateDTO.setTemplateId(rs.getString("template_id"));
                templateDTO.setName(rs.getString("name"));
                templateDTO.setBodyHtml(rs.getString("body_html"));
                templateDTO.setType(rs.getString("type"));
                templateDTO.setCoverImgUrl(rs.getString("cover_img_url"));
                templateDTO.setQrUrl(rs.getString("qr_url"));
                templateDTO.setCreatedAt(rs.getString("created_at"));
                templateDTO.setUpdatedAt(rs.getString("updated_at"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
        return templateDTO;

    }

    // 어드민 페이지 탬플릿 추가
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

//            // 4. QR 코드 처리 (타입이 ADDED일 때만 실행)
//            String qrUrl = null; // 기본값은 null
//            if ("ADDED".equals(type)) {
//                // QR 생성
//                String targetUrl = "http://10.1.82.127:8080/donguri/template.enroll?id=" + templateId;
//                String tempPath = request.getServletContext().getRealPath("img");
//
//                // 로컬에 임시 QR 생성
//                String qrLocalFileName = QRGenerator.generateQR(targetUrl, tempPath);
//                java.io.File qrFile = new java.io.File(tempPath + "/" + qrLocalFileName);
//
//                // [S3 업로드] QR 이미지를 S3에 올리기
//                String qrS3Key = "qr_codes/" + qrLocalFileName;
//                qrUrl = s3Uploader.upload(new java.io.FileInputStream(qrFile), qrS3Key, "image/png", qrFile.length());
//
//                // 업로드 후 로컬 임시 파일 삭제
//                if (qrFile.exists()) qrFile.delete();
//                System.out.println("히든 템플릿 QR 생성 완료! 🐿️");
//            }


            String sql = "INSERT INTO TEMPLATE (template_id, name, body_html, type, cover_img_url, qr_url) VALUES (HEXTORAW(?), ?,?,?,?,?)";

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);

            // 3. 데이터 세팅 (중요: mr.getParameter를 사용해야 함!)
            pstmt.setString(1, templateId);
            pstmt.setString(2, name);
            pstmt.setString(3, bodyHtml);
            pstmt.setString(4, type);
            pstmt.setString(5, imgUrl);
//            pstmt.setString(6, qrUrl);

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

    // 유저 보유 템플릿 리스트 조회
    public void getUserTemplateList(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 1. 세션에서 로그인한 유저 정보 가져오기 (예: UserDTO 객체나 String ID)
        // 로그인 시 "loginUser"라는 이름으로 세션에 저장했다고 가정합니다.
//        UserDTO loginUser = (UserDTO) request.getSession().getAttribute("loginUser");

        // 만약 로그인이 안 되어 있다면 로직 중단 (방어 코드)
//        if (loginUser == null) return;

        // TODO: 로그인 연결 이후 수정해주기
//        String userId = loginUser.getUserId();
        String userId = "7C00B4D005064FD9B3775F73B9DDC374";

        // 추가된 엽서는 내림차순(최신순)으로 정렬
        String sql = "SELECT T.* " + // 끝에 공백
                "FROM USERS U, TEMPLATE T, USER_TEMPLATE UT " + // 끝에 공백
                "WHERE U.USER_ID = UT.USER_ID " + // 끝에 공백
                "  AND T.TEMPLATE_ID = UT.TEMPLATE_ID " + // 끝에 공백
                "  AND U.USER_ID = ? " + // 끝에 공백
                "ORDER BY UT.CREATED_AT DESC";

        try {

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();
            TemplateDTO templateDTO = null;

            ArrayList<TemplateDTO> templateListUser = new ArrayList<>();

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

                templateListUser.add(templateDTO);

            }

            request.setAttribute("templateListUser", templateListUser);
            System.out.println(" 유저 템플릿 로드 성공: " + templateListUser.size() + "개");

        } catch (Exception e) {
            System.out.println("!!! 여기서 에러 터짐 !!!");
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

    }
}




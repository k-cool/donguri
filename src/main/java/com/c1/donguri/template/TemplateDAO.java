package com.c1.donguri.template;

import com.c1.donguri.user.UserDTO;
import com.c1.donguri.util.DBManager;
import com.c1.donguri.util.EnvLoader;
import com.c1.donguri.util.QRGenerator;
import com.c1.donguri.util.S3Uploader;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class TemplateDAO {
    public static final TemplateDAO TEMPLATE_DAO = new TemplateDAO();
    public static Map<String, String> envMap;

    private TemplateDAO() {
        envMap = EnvLoader.loadEnv(".env");
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

    // 유저 보유 템플릿 리스트 조회
    public void getUserTemplateList(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");


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

            pstmt.setString(1, user.getUserId());

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

    // 템플릿 상세 조회
    public TemplateDTO getTemplateDetail(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM template WHERE template_id = ?";
        TemplateDTO templateDTO = null;

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, request.getParameter("templateId"));

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

        try {
            // 1. 파라미터 추출
            String name = request.getParameter("name");
            String bodyHtml = request.getParameter("bodyHtml");
            String type = request.getParameter("type"); // BASE 또는 ADDED(QR ver.)

            // 2. 고유 ID 생성 (QR 주소와 DB PK로 공통 사용)
            String templateId = UUID.randomUUID().toString().replace("-", "").toUpperCase();

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

            String sql = "INSERT INTO TEMPLATE (template_id, name, body_html, type, cover_img_url) VALUES (?,?,?,?,?)";

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);

            // 3. 데이터 세팅 (중요: mr.getParameter를 사용해야 함!)
            pstmt.setString(1, templateId);
            pstmt.setString(2, name);
            pstmt.setString(3, bodyHtml);
            pstmt.setString(4, type);
            pstmt.setString(5, imgUrl);

            if (pstmt.executeUpdate() == 1) {
                System.out.println("템플릿 등록 성공! 타입: " + type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }
    }

    public String decodeQrImage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession httpSession = request.getSession();
        UserDTO user = (UserDTO) httpSession.getAttribute("user");
        String userId = user.getUserId();
        String templateId = null;

        try {
            // 1. 업로드된 파일 받기
            Part filePart = request.getPart("qrFile");

            if (filePart == null || filePart.getSize() == 0) {
                return "qr-decode";
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
            if (decodedUrl != null && decodedUrl.contains("=")) {
                templateId = decodedUrl.substring(decodedUrl.indexOf("=") + 1);
            }

            // 5. DB 해금 로직 실행
            if (templateId != null && TemplateDAO.TEMPLATE_DAO.unlockTemplate(userId, templateId)) {
                System.out.println("PC 해금 성공: " + templateId);
                return "template-detail?templateId=" + templateId;
            } else {
                System.out.println("해금 실패 또는 이미 보유 중");
                return "qr-decode";
            }

        } catch (NotFoundException e) {
            System.out.println("QR 코드를 인식할 수 없습니다.");
            return "qr-decode";
        } catch (Exception e) {
            e.printStackTrace();
            return "qr-decode";
        }

    }


    // QR코드로 템플릿 추가
    public void addQRTemplate(HttpServletRequest request) {
        String templateId = java.util.UUID.randomUUID().toString().replace("-", "").toUpperCase();

        String s3Url = null;

        // 유저가 스캔했을 때 실행될 주소를 조립 (방금 만든 ID를 붙임)
        String targetUrl = envMap.get("BASE_URL") + "/qr?templateId=" + templateId;

        System.out.println("QR URL:" + targetUrl);

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

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            String name = request.getParameter("name");
            String bodyHtml = request.getParameter("bodyHtml");
            String type = request.getParameter("type"); // BASE 또는 ADDED(QR ver.)
            Part filePart = request.getPart("coverImgUrl");

            String imgUrl = null;

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = "cover_img/template/" + UUID.randomUUID().toString();
                imgUrl = s3Uploader.upload(filePart.getInputStream(), fileName, filePart.getContentType(), filePart.getSize());
            }

            // 4. SQL 실행 (qr_url 컬럼에 컨트롤러에서 보낸 s3Url을 바로 넣습니다)
            String sql = "INSERT INTO TEMPLATE (template_id, name, body_html, type, cover_img_url, qr_url, created_at, updated_at) "
                    + "VALUES (?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, templateId);
            pstmt.setString(2, name);
            pstmt.setString(3, bodyHtml);
            pstmt.setString(4, type);
            pstmt.setString(5, imgUrl);
            pstmt.setString(6, s3Url); // <--- 컨트롤러가 준 URL 사용

            if (pstmt.executeUpdate() == 1) {
                System.out.println("✅ " + type + " 템플릿 등록 성공!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }
    }

    // 유저가 QR코드 스캔으로 템플릿 해금
    public boolean unlockTemplate(String userId, String templateId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        // 유저와 템플릿을 연결해주는 테이블 (예: USER_TEMPLATE)
        String sql = "INSERT INTO USER_TEMPLATE (user_id, template_id, created_at) VALUES (?, ?, SYSDATE)";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, templateId);

            return pstmt.executeUpdate() == 1;
        } catch (Exception e) {
            // 이미 해금된 경우(PK 중복 등) 에러가 날 수 있으니 적절히 처리
            e.printStackTrace();
            return false;
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }
    }

    // 관리자가 탬플릿 업데이트
    public void updateTemplate(HttpServletRequest request) {

        Connection con = null;
        PreparedStatement pstmt = null;
        S3Uploader s3Uploader = new S3Uploader();

        try {
            String templateId = request.getParameter("templateId");
            String name = request.getParameter("name");
            String bodyHtml = request.getParameter("bodyHtml");

            // 사진 수정 안 할 때를 대비한 기존 이미지 URL
            String existingImgUrl = request.getParameter("existingImgUrl");

            // 이미지 수정 여부 판별
            Part filePart = request.getPart("coverImgUrl");
            String imgUrl = existingImgUrl;

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = "cover_img/template/" + UUID.randomUUID().toString();
                imgUrl = s3Uploader.upload(filePart.getInputStream(), fileName, filePart.getContentType(), filePart.getSize());
                System.out.println(">>> [UPDATE] 새 이미지 업로드 완료: " + imgUrl);
            }

            String sql = "UPDATE TEMPLATE SET name = ?, body_html = ?, cover_img_url = ?, " +
                    "updated_at = SYSDATE WHERE template_id = ?";

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, name);
            pstmt.setString(2, bodyHtml);
            pstmt.setString(3, imgUrl);
            pstmt.setString(4, templateId);

            if (pstmt.executeUpdate() == 1) {
                System.out.println("✅ 템플릿 수정 성공! (ID: " + templateId + ")");
            }

        } catch (Exception e) {
            System.err.println("!!! 템플릿 수정 중 에러 발생 !!!");
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }

    }

    // 관리자가 탬플릿 삭제
    public void deleteTemplate(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            String templateId = request.getParameter("templateId");

            if (templateId == null || templateId.isEmpty()) {
                System.out.println("❌ 삭제할 ID가 없습니다.");
                return;
            }

            con = DBManager.DB_MANAGER.getConnection();

            // --- 1단계: 자식 테이블(USER_TEMPLATE) 데이터 먼저 삭제 ---
            // 유저들이 이 템플릿을 해금해서 가지고 있는 기록부터 지워줌.
            String sql1 = "DELETE FROM USER_TEMPLATE WHERE template_id = ?";
            pstmt = con.prepareStatement(sql1);
            pstmt.setString(1, templateId);
            pstmt.executeUpdate();
            pstmt.close(); // 다음 쿼리를 위해 한 번 닫아줌

            // --- 2단계: 부모 테이블(TEMPLATE) 데이터 삭제 ---
            String sql2 = "DELETE FROM TEMPLATE WHERE template_id = ?";
            pstmt = con.prepareStatement(sql2);
            pstmt.setString(1, templateId);

            if (pstmt.executeUpdate() == 1) {
                System.out.println("✅ 삭제 성공");
            }

        } catch (Exception e) {
            System.err.println("!!! 오류 발생 !!!");
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }


    }


}



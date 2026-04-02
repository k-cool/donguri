package com.c1.donguri.template;

import com.c1.donguri.user.UserDTO;
import com.c1.donguri.util.DBManager;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
        String sql = "SELECT LOWER(RAWTOHEX(template_id)) AS template_id, " +
                "name, body_html, type, cover_img_url, bgm_url, qr_url, created_at " +
                "FROM TEMPLATE ORDER BY created_at DESC";

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
                templateDTO.setBgmUrl(rs.getString("bgm_url"));
                templateDTO.setQrUrl(rs.getString("qr_url"));
                templateDTO.setCreatedAt(rs.getString("created_at"));

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

    public int addTemplate(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;

        S3Uploader s3Uploader = new S3Uploader();


        String sql = "INSERT INTO TEMPLATE (name, body_html, type, cover_img_url, bgm_url, qr_url, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";

        try {
            // 1. 이미지가 저장될 실제 서버 경로 설정 (webapp/img 폴더)
            String path = request.getServletContext().getRealPath("img");

            // 2. 파일 업로드를 처리하는 MultipartRequest 객체 생성
            // (파일 크기 제한 10MB, 한글 인코딩 UTF-8, 중복 파일명 정책 설정)
            com.oreilly.servlet.MultipartRequest mr = new com.oreilly.servlet.MultipartRequest(
                    request, path, 10 * 1024 * 1024, "UTF-8", new com.oreilly.servlet.multipart.DefaultFileRenamePolicy()
            );

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);

            // 3. 데이터 세팅 (중요: mr.getParameter를 사용해야 함!)
            pstmt.setString(1, mr.getParameter("name"));
            pstmt.setString(2, mr.getParameter("bodyHtml"));
            pstmt.setString(3, mr.getParameter("type"));
            pstmt.setString(4, mr.getFilesystemName("coverImgUrl"));
            pstmt.setString(5, mr.getParameter("bgmUrl"));

            // [QR URL 빈 값 처리] 관리자가 비워뒀을 때 에러 안 나게 기본값 세팅
            String qr = mr.getParameter("qrUrl");
            if (qr == null || qr.trim().isEmpty()) {
                qr = "qr_default.png"; // 혹은 "None" 등 원하는 기본값
            }
            pstmt.setString(6, qr);

            if (pstmt.executeUpdate() == 1) {
                System.out.println("add successfully");
            }

            return pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }
    }

    // 유저가 QR을 찍었을 때 유저 탬플릿 보관함(user_template)에 추가하는 로직
    public int enrollTemplate(String userId, String templateId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        // templateId는 이미 변환된 String이므로 HEXTORAW로 다시 넣음.
        String sql = "INSERT INTO USER_TEMPLATE (user_id, template_id) VALUES (?, HEXTORAW(?))";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, templateId);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }
    }
}

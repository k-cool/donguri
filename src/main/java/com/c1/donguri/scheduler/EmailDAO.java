package com.c1.donguri.scheduler;

import com.c1.donguri.util.DBManager;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;

/*
    email_content 저장
    email_content_id로 1건 조회

*/
public class EmailDAO {
    public static final EmailDAO EMAIL_DAO = new EmailDAO();

    private EmailDAO() {
    }

    /*
        이메일 내용 저장
        - email_content_id가 비어 있으면 여기서 생성
        - created_at / updated_at은 DB default로 처리
    */
    public EmailDTO insertEmailContent(HttpServletRequest request) throws ParseException {
        Connection con = null;
        PreparedStatement ps = null;

        // -------------------------------------------------
        // 1. 화면에서 넘어온 값 받기
        // -------------------------------------------------
        // TODO : 가희언니와 연결한 후에는 불필요한 테스트용
        String templateId = request.getParameter("templateId");
        String senderId = request.getParameter("senderId");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");


        // -------------------------------------------------
        // 4. DTO에 담기
        // -------------------------------------------------
//        EmailDTO email = new EmailDTO();
//        email.setTemplateId((templateId == null || templateId.trim().isEmpty()) ? null : templateId);
//        email.setSenderId(senderId);
//        email.setTitle(title);
//        email.setSubject(subject);
//        email.setContent(content);
//        email.setReceiverEmail(receiverEmail);
//        email.setReserveDateTime(runTime);


        try {
            con = DBManager.DB_MANAGER.getConnection();

//            if (email.getEmailContentId() == null || email.getEmailContentId().trim().isEmpty()) {
//                email.setEmailContentId(makeUUID());
//                email.setEmailContentId(UUID.randomUUID().toString());
//            }


//            String emailContentId =

            // TODO: 테이블 추가되면 수정하기
            String sql = "insert into email_content "
                    + "(email_content_id, template_id, sender_id, subject, content, cover_img_url) "
                    + "values (? ,SYS_GUID(), SYS_GUID(), ?, ?, ?)";

//            String sql = "";
            ps = con.prepareStatement(sql);
//            ps.setString(1, email.getEmailContentId());
//            ps.setString(2, email.getTemplateId());
//            ps.setString(3, email.getSenderId());
//            ps.setString(2, new UUID().toString());
//            ps.setString(3, email.getSenderId());
//            ps.setBytes(1, emailContentId);
//            ps.setString(2, subject);
//            ps.setString(3, content);
//            ps.setString(4, null);


            if (ps.executeUpdate() > 0) {
                System.out.println("INSERT SUCCESS");
            }

            EmailDTO email = new EmailDTO();

//            email.setEmailContentId(emailContentId);
            email.setTemplateId(templateId);
            email.setSenderId(senderId);
            email.setSubject(subject);
            email.setContent(content);


            return email;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, ps, null);
        }

        return null;
    }

    /*
        email_content_id로 저장된 메일 내용 1건 조회
    */
    public EmailDTO getEmailContentById(String emailContentId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBManager.DB_MANAGER.getConnection();

            String sql = "select * from email_content where email_content_id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, emailContentId);

            rs = ps.executeQuery();

            if (rs.next()) {
                EmailDTO email = new EmailDTO();
//                email.setEmailContentId(rs.getString("email_content_id"));
                email.setTemplateId(rs.getString("template_id"));
                email.setSenderId(rs.getString("sender_id"));
                email.setSubject(rs.getString("subject"));
                email.setContent(rs.getString("content"));
                email.setCoverImgUrl(rs.getString("cover_img_url"));
                email.setCreatedAt(rs.getTimestamp("created_at"));
                email.setUpdatedAt(rs.getTimestamp("updated_at"));
                return email;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, ps, rs);
        }

        return null;
    }
}
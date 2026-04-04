package com.c1.donguri.scheduler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    사용자가 writeEmail.jsp에서 작성한 폼을 받아 처리하는 서블릿

    하는 일:
    1) 입력값 받기
    2) 유효성 검사
    3) email_content 테이블 저장
    4) Quartz 예약 등록
*/
@WebServlet("/reserveEmail")
public class ReserveEmailC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            EmailDTO email = EmailDAO.EMAIL_DAO.insertEmailContent(request);


            // -------------------------------------------------
            // 5. DB 저장
            // -------------------------------------------------

//
//            System.out.println("=== ReserveEmailC 시작 ===");
//            System.out.println("receiverEmail = " + receiverEmail);
//            System.out.println("reserveDateTime = " + reserveDateTime);
//            System.out.println("runTime = " + runTime);


            EmailDTO saved = EmailDAO.EMAIL_DAO.getEmailContentById(email.getEmailContentId());

//            System.out.println("=== ReserveEmailC 저장 직전 ===");
//            System.out.println("emailContentId = " + email.getEmailContentId());
//            System.out.println("templateId = " + email.getTemplateId());
//            System.out.println("senderId = " + email.getSenderId());
//            System.out.println("title = " + email.getTitle());
//            System.out.println("subject = " + email.getSubject());
//            System.out.println("content = " + email.getContent());

//            System.out.println("insert 후 emailContentId = " + email.getEmailContentId());
//            System.out.println("insert 직후 재조회 성공 여부 = " + (saved != null));

            if (saved == null) {
                throw new Exception("이메일 내용 저장 실패");
            }

            String receiverEmail = request.getParameter("receiverEmail");
            String title = "[동구리]님에게 동구리가 찾아왔습니다";
            String reserveDateTime = request.getParameter("reserveDateTime");

            email.setReceiverEmail(receiverEmail);


            // -------------------------------------------------
            // 3. 문자열 -> Date 파싱
            // datetime-local 값 예: 2026-04-02T15:35
            // -------------------------------------------------
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date runTime = sdf.parse(reserveDateTime);

            email.setReserveDateTime(runTime);
            email.setTitle(title);

            String emailContentId = email.getEmailContentId();

            System.out.println("ReserveEmailC에서 넘기는 ID = " + emailContentId);

//            EmailScheduler.enrollJob(email);


        } catch (Exception e) {
            e.printStackTrace();

            response.getWriter().println("<script>");
            response.getWriter().println("history.back();");
            response.getWriter().println("</script>");

        }
    }

}
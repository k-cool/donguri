package com.c1.donguri.template;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// [GET]: QR 코드를 찍고 들어왔을 때 실행 (상세 정보 보여주기)
@WebServlet("/template.enroll")
public class TemplateEnrollC extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 1. 파라미터 및 세션 정보 추출
            String templateId = request.getParameter("id");

            // 실제 프로젝트라면 세션에서 로그인한 유저 정보를 가져와야 합니다!
            // User u = (User) request.getSession().getAttribute("loginUser");
            // String userId = u.getUserId();
            String userId = "TEST_USER"; // 지금은 테스트용으로 고정!

            if (templateId != null && !templateId.isEmpty()) {
                // 2. DAO 호출 (싱글톤 방식)
                TemplateDAO dao = TemplateDAO.TEMPLATE_DAO.getTemplateDetail();

                // [A] 템플릿 상세 정보 가져오기
                TemplateDTO t = dao.getTemplateDetail(templateId);

                // [B] 이 유저가 이미 등록했는지 확인 (결과는 true/false)
                boolean isEnrolled = dao.isTemplateEnrolled(userId, templateId);

                // 3. JSP에서 쓸 수 있게 배달 준비 (request 세팅)
                request.setAttribute("t", t);
                request.setAttribute("isEnrolled", isEnrolled);

                // 상세 페이지(detail)를 보여주도록 설정
                request.setAttribute("content", "jsp/template/template_detail.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("main.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}

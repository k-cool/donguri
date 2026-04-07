package com.c1.donguri.reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/reservation")
public class ReservationC extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        // 세션에서 유저 ID 가져오기 (테스트용 기본값 포함)
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            userId = "6EA6BADB01C8493F9277DA9DB7016AC5";
        }

        if (action == null || action.equals("list")) {
            List<ReservationDTO> list = ReservationDAO.RESERVATION_DAO.getAll();
            request.setAttribute("list", list);
            request.getRequestDispatcher("jsp/reservation/list.jsp").forward(request, response);

        } else if ("write".equals(action)) {
            ArrayList<TemplateDTO> templateList = ReservationDAO.RESERVATION_DAO.getTemplateList(userId);
            System.out.println(templateList);

            request.setAttribute("templateList", templateList);
            request.getRequestDispatcher("jsp/reservation/write.jsp").forward(request, response);

        } else if ("main".equals(action)) {
            ArrayList<TemplateDTO> templateList = ReservationDAO.RESERVATION_DAO.getTemplateList(userId);
            request.setAttribute("templateList", templateList);
            request.getRequestDispatcher("jsp/reservation/reservation_main.jsp").forward(request, response);

        } else if ("detail".equals(action)) {
            String id = request.getParameter("id");
            ReservationDTO r = ReservationDAO.RESERVATION_DAO.getOne(id);
            request.setAttribute("r", r);
            request.getRequestDispatcher("jsp/reservation/detail.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            String id = request.getParameter("id");
            ReservationDTO r = ReservationDAO.RESERVATION_DAO.getOne(id);
            ArrayList<TemplateDTO> templateList = ReservationDAO.RESERVATION_DAO.getTemplateList(userId);

            request.setAttribute("r", r);
            request.setAttribute("templateList", templateList);
            request.getRequestDispatcher("jsp/reservation/reservation_edit.jsp").forward(request, response);

        } else if ("delete".equals(action)) {
            String id = request.getParameter("id");
            ReservationDAO.RESERVATION_DAO.delete(id);
            response.sendRedirect("reservation?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("confirm".equals(action)) {
            InsertReservationDTO ir = new InsertReservationDTO();
            ir.setFromId(request.getParameter("fromId"));
            ir.setRecipientEmail(request.getParameter("recipientEmail"));
            ir.setSubject(request.getParameter("subject"));
            ir.setContent(request.getParameter("content"));
            ir.setTemplateId(request.getParameter("templateId"));
            ir.setBgmUrl(request.getParameter("bgmUrl"));
            ir.setScheduledDate(request.getParameter("scheduledDate"));

            String userId = (String) session.getAttribute("userId");
            if (userId == null) {
                userId = "118D6CAAC61C48B9B6A666E4FB021C93";
            }
            ArrayList<TemplateDTO> templateList = ReservationDAO.RESERVATION_DAO.getTemplateList(userId);

            session.setAttribute("insertReservation", ir);
            request.setAttribute("templateList", templateList);
            request.getRequestDispatcher("jsp/reservation/reservation_confirm.jsp").forward(request, response);

        } else if ("insert".equals(action)) {
            InsertReservationDTO ir = (InsertReservationDTO) session.getAttribute("insertReservation");
            if (ir != null) {
                ReservationDAO.RESERVATION_DAO.insert(request);
                session.removeAttribute("insertReservation");
            }
            response.sendRedirect("reservation?action=list");

        } else if ("update".equals(action)) {
            ReservationDTO r = new ReservationDTO();
            r.setReservationId(request.getParameter("id"));
            r.setRecipientEmail(request.getParameter("recipientEmail"));
            r.setSubject(request.getParameter("subject"));
            r.setContent(request.getParameter("content"));
            r.setScheduledDate(request.getParameter("scheduledDate"));
            r.setTemplateId(request.getParameter("templateId"));
            r.setBgm(request.getParameter("bgmUrl"));

            int result = ReservationDAO.RESERVATION_DAO.update(r);
            if (result > 0) {
                response.sendRedirect("reservation?action=detail&id=" + r.getReservationId());
            } else {
                response.sendRedirect("reservation?action=list");
            }
        }
    }
}
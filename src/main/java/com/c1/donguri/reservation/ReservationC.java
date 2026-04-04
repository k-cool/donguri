package com.c1.donguri.reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/reservation")
public class ReservationC extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String action = request.getParameter("action");

        if (action == null || action.equals("list")) {
            List<ReservationDTO> list = ReservationDAO.RESERVATION_DAO.getAll();
            request.setAttribute("list", list);
            request.getRequestDispatcher("reservation/list.jsp").forward(request, response);

        } else if ("write".equals(action)) {
            request.getRequestDispatcher("reservation/write.jsp").forward(request, response);

        } else if ("main".equals(action)) {

            request.getRequestDispatcher("reservation/reservation_main.jsp").forward(request, response);
        } else if ("detail".equals(action)) {
            String id = request.getParameter("id");

            ReservationDTO r = ReservationDAO.RESERVATION_DAO.getOne(id); // 하나 조회
            request.setAttribute("r", r);

            request.getRequestDispatcher("reservation/detail.jsp")
                    .forward(request, response);
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

            session.setAttribute("insertReservation", ir);
            request.getRequestDispatcher("reservation/reservation_confirm.jsp").forward(request, response);

        } else if ("insert".equals(action)) {

            InsertReservationDTO ir = (InsertReservationDTO) session.getAttribute("insertReservation");

            if (ir != null) {
                ReservationDAO.RESERVATION_DAO.insert(request);
                session.removeAttribute("insertReservation");
            }

            response.sendRedirect("reservation?action=list");
        }
    }
}
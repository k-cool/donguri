package com.c1.donguri.reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/reservation")
public class ReservationC extends HttpServlet {

    private ReservationDAO dao = ReservationDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String action = request.getParameter("action");

        if (action == null || action.equals("list")) {
            List<ReservationDTO> list = dao.getAll();
            request.setAttribute("list", list);
            request.getRequestDispatcher("reservation/list.jsp").forward(request, response);

        } else if ("write".equals(action)) {
            request.getRequestDispatcher("reservation/write.jsp").forward(request, response);

        } else if ("main".equals(action)) {

            request.getRequestDispatcher("reservation/reservation_main.jsp").forward(request, response);
        } else if ("detail".equals(action)) {
            String id = request.getParameter("id");

            ReservationDTO r = dao.getOne(id); // 하나 조회
            request.setAttribute("r", r);

            request.getRequestDispatcher("reservation/detail.jsp")
                    .forward(request, response);
        } else if ("delete".equals(action)) {
            String id = request.getParameter("id");

            dao.delete(id);
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
            ReservationDTO r = new ReservationDTO();
            r.setFromId(request.getParameter("fromId"));
            r.setSenderEmail(request.getParameter("senderEmail"));
            r.setRecipientEmail(request.getParameter("recipientEmail"));
            r.setSubject(request.getParameter("title"));
            r.setContent(request.getParameter("message"));
            r.setScheduledDate(request.getParameter("scheduledDate"));
            r.setTemplateId(request.getParameter("templateId"));
            r.setBgm(request.getParameter("bgm"));

            r.setEmailContentId("test_content");
            

            session.setAttribute("reservation_confirm", r);
            request.getRequestDispatcher("reservation/reservation_confirm.jsp").forward(request, response);

        } else if ("insert".equals(action)) {

            ReservationDTO r = (ReservationDTO) session.getAttribute("reservation_confirm");

            if (r != null) {
                dao.insert(r);
                session.removeAttribute("reservation_confirm");
            }

            response.sendRedirect("reservation?action=list");
        }
    }
}
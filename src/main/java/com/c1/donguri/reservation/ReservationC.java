package com.c1.donguri.reservation;

import com.c1.donguri.template.TemplateDTO;
import com.c1.donguri.user.UserDAO;

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

        boolean isLoggedIn = UserDAO.USER_DAO.loginCheck(request);

        if (!isLoggedIn) {
            request.getRequestDispatcher("jsp/reservation/reservation_main.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("isLoggedIn", isLoggedIn);
            request.getRequestDispatcher("jsp/reservation/reservation_main.jsp").forward(request, response);
        } else if (action.equals("list")) {
            List<ReservationDTO> list = ReservationDAO.RESERVATION_DAO.getAll(request);
            request.setAttribute("list", list);
            request.setAttribute("content", "jsp/reservation/list.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);

        } else if ("write".equals(action)) {
            ArrayList<TemplateDTO> templateList = ReservationDAO.RESERVATION_DAO.getTemplateList(request);
            request.setAttribute("templateList", templateList);
            request.setAttribute("content", "jsp/reservation/write.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);

        } else if ("detail".equals(action)) {
            ReservationDTO r = ReservationDAO.RESERVATION_DAO.getOne(request);
            request.setAttribute("r", r);
            request.setAttribute("content", "jsp/reservation/detail.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            String id = request.getParameter("id");
            ReservationDTO r = ReservationDAO.RESERVATION_DAO.getOne(request);
            ArrayList<TemplateDTO> templateList = ReservationDAO.RESERVATION_DAO.getTemplateList(request);

            request.setAttribute("r", r);
            request.setAttribute("templateList", templateList);
            request.setAttribute("content", "jsp/reservation/edit.jsp");
            request.getRequestDispatcher("main.jsp").forward(request, response);

        } else if ("delete".equals(action)) {
            String reservationId = request.getParameter("id");
            ReservationDAO.RESERVATION_DAO.delete(reservationId);
            response.sendRedirect("reservation?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        boolean isLoggedIn = UserDAO.USER_DAO.loginCheck(request);

        if (!isLoggedIn) {
            response.sendRedirect("login");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();


        if ("confirm".equals(action)) {
            TemplateDTO selectedTemplate = ReservationDAO.RESERVATION_DAO.getTemplate(request);
            ReservationDAO.RESERVATION_DAO.setReservationDTOToSession(request);
            request.setAttribute("selectedTemplate", selectedTemplate);

            request.setAttribute("content", "jsp/reservation/confirm.jsp");

            request.getRequestDispatcher("main.jsp").forward(request, response);

        } else if ("insert".equals(action)) {
            InsertReservationDTO ir = (InsertReservationDTO) session.getAttribute("insertReservation");

            String reservationId = null;

            if (ir != null) {
                reservationId = ReservationDAO.RESERVATION_DAO.insert(request);
                session.removeAttribute("insertReservation");
            }

            System.out.println("RESERVATION ID: " + reservationId);

            if (reservationId != null) {
                ReservationDAO.RESERVATION_DAO.enrollNewEmailJob(reservationId);
            }

            response.sendRedirect("reservation?action=list");


        } else if ("update".equals(action)) {
            String reservationId = request.getParameter("id");

            int result = ReservationDAO.RESERVATION_DAO.update(request);

            if (result > 0) {
                response.sendRedirect("reservation?action=detail&id=" + reservationId);
            } else {
                response.sendRedirect("reservation?action=list");
            }
        }
    }
}
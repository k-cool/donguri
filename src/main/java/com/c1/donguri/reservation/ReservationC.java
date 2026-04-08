package com.c1.donguri.reservation;

import com.c1.donguri.user.UserDAO;
import com.c1.donguri.user.UserDTO;

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


        String action = request.getParameter("action");


        if (!isLoggedIn) {
            request.getRequestDispatcher("jsp/reservation/reservation_main.jsp").forward(request, response);
            return;
        }


        if (action == null || action.equals("list")) {
            List<ReservationDTO> list = ReservationDAO.RESERVATION_DAO.getAll();
            request.setAttribute("list", list);
            request.getRequestDispatcher("jsp/reservation/list.jsp").forward(request, response);

        } else if ("write".equals(action)) {
            ArrayList<TemplateDTO> templateList = ReservationDAO.RESERVATION_DAO.getTemplateList(request);
            request.setAttribute("templateList", templateList);
            request.getRequestDispatcher("jsp/reservation/write.jsp").forward(request, response);

        } else if ("detail".equals(action)) {
            String id = request.getParameter("id");
            ReservationDTO r = ReservationDAO.RESERVATION_DAO.getOne(request);
            request.setAttribute("r", r);
            request.getRequestDispatcher("jsp/reservation/detail.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            String id = request.getParameter("id");
            ReservationDTO r = ReservationDAO.RESERVATION_DAO.getOne(request);
            ArrayList<TemplateDTO> templateList = ReservationDAO.RESERVATION_DAO.getTemplateList(request);

            request.setAttribute("r", r);
            request.setAttribute("templateList", templateList);
            request.getRequestDispatcher("jsp/reservation/reservation_edit.jsp").forward(request, response);

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

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if (!isLoggedIn) {
            request.getRequestDispatcher("jsp/reservation/reservation_main.jsp").forward(request, response);
            return;
        }

        if ("confirm".equals(action)) {
            ArrayList<TemplateDTO> templateList = ReservationDAO.RESERVATION_DAO.getTemplateList(request);
            ReservationDAO.RESERVATION_DAO.setReservationDTOToSession(request);
            request.setAttribute("templateList", templateList);
            request.getRequestDispatcher("jsp/reservation/reservation_confirm.jsp").forward(request, response);

        } else if ("insert".equals(action)) {
            InsertReservationDTO ir = (InsertReservationDTO) session.getAttribute("insertReservation");

            if (ir != null) {
                ReservationDAO.RESERVATION_DAO.insert(request);
                session.removeAttribute("insertReservation");
            } else {
                response.sendRedirect("reservation?action=list");
            }
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
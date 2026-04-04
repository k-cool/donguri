package com.c1.donguri.main;

import com.c1.donguri.scheduler.EmailJobDTO;
import com.c1.donguri.scheduler.EmailScheduler;
import com.c1.donguri.util.EmailSend;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@WebListener
public class AppStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 서버 시작 시 실행
        System.out.println("서버 시작됨!");

        // 예약 테이블 조회
        ArrayList<EmailJobDTO> undoneReservations = ReservationDAO.RESERVATION_DAO.getUndoneReservation();

        // 당장 보낼 예약, 등록할 예약 구분
        LocalDateTime now = LocalDateTime.now();

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("1");


        for (int i = 0; i < undoneReservations.size(); i++) {

            EmailJobDTO emailJob = undoneReservations.get(i);


            System.out.println(emailJob.getScheduledDate());
            System.out.println("ID: " + emailJob.getReservationId());
            System.out.println("DATE: " + emailJob.getScheduledDate());

            LocalDateTime scheduledTime = emailJob.getScheduledDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            System.out.println("2");

            if (!scheduledTime.isAfter(now)) {

                System.out.println("3");
                // 당장 보낼 예약 전송
                EmailSend.EMAIL_SEND.send(emailJob.getRecipientEmail(), "[동구리] 동구리가 도착했어요.", emailJob.getContent());
                ReservationDAO.RESERVATION_DAO.updateResult(emailJob.getReservationId(), true, null);

            } else {
                System.out.println("4");

                // 등록할 예약 잡등록
                try {
                    EmailScheduler.EMAIL_SCHEDULER.enrollJob(emailJob);

                } catch (Exception e) {
                    System.out.println("잡등록 실패");
                    throw new RuntimeException(e);
                }

            }

            System.out.println("5");
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 서버 종료 시 실행
        System.out.println("서버 종료됨!");
    }
}

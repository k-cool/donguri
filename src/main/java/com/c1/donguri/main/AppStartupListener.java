package com.c1.donguri.main;

import com.c1.donguri.scheduler.EmailJobDTO;
import com.c1.donguri.scheduler.EmailScheduler;
import com.c1.donguri.util.EmailSend;
import com.c1.donguri.util.EnvLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;


@WebListener
public class AppStartupListener implements ServletContextListener {
    public static Map<String, String> envMap;

    public AppStartupListener() {
        envMap = EnvLoader.loadEnv(".env");
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 서버 시작 시 실행
        System.out.println("서버 부팅 작업 시작");

        // 예약 테이블 조회
        ArrayList<EmailJobDTO> undoneReservations = ReservationDAO.RESERVATION_DAO.getUndoneReservation();

        // 당장 보낼 예약, 등록할 예약 구분
        LocalDateTime now = LocalDateTime.now();

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("지금 서버 시간:" + now);

        int successCnt = 0;
        int errorCnt = 0;

        for (int i = 0; i < undoneReservations.size(); i++) {

            EmailJobDTO emailJob = undoneReservations.get(i);

            System.out.println("=== 이메일 예약 ===");
            System.out.println("DATA: " + emailJob.getReservationId() + " / " + emailJob.getScheduledDate());

            LocalDateTime scheduledTime = emailJob.getScheduledDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();


            if (!scheduledTime.isAfter(now)) {
                System.out.println("이메일 전송 시작(시간 경과 예약)");

                try {

                    System.out.println(envMap.get("SKIP_EMAIL_SEND_ON_BOOT"));
                    // SKIP_EMAIL_SEND_ON_BOOT=true일 경우 부팅할때 이메일 전송을 하지 않음.
                    if (!"true".equals(envMap.get("SKIP_EMAIL_SEND_ON_BOOT"))) {

                        // 당장 보낼 예약 전송
                        EmailSend.EMAIL_SEND.send(
                                emailJob.getRecipientEmail(),
                                "[동구리] 동구리가 도착했어요.",
                                EmailSend.EMAIL_SEND.createEmailContent(emailJob.getReservationId())
                        );

                    } else {

                        System.out.println("이메일 전송을 스킵하였습니다.(SKIP_EMAIL_SEND_ON_BOOT=true)");

                    }

                    ReservationDAO.RESERVATION_DAO.updateResult(emailJob.getReservationId(), true, null);

                    successCnt++;
                } catch (Exception e) {
                    errorCnt++;
                    e.printStackTrace();
                }

            } else {
                System.out.println("이메일 잡등록 시작(시간 미경과)");

                // 등록할 예약 잡등록
                try {
                    EmailScheduler.EMAIL_SCHEDULER.enrollJob(emailJob);

                    successCnt++;
                } catch (Exception e) {
                    errorCnt++;
                    throw new RuntimeException(e);
                }

            }

        }

        System.out.println("✅ SUCCESS: " + successCnt);
        System.out.println("❌ ERROR: " + errorCnt);
        System.out.println("=== 모든 메일 전송 및 예약 등록이 완료되었습니다! ===");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        try {
            // 스케줄러 종료
            if (EmailScheduler.EMAIL_SCHEDULER.scheduler != null) {
                EmailScheduler.EMAIL_SCHEDULER.scheduler.shutdown(true);
            }

            // 사용되고있는 드라이버 종료
            Enumeration<Driver> drivers = DriverManager.getDrivers();

            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 서버 종료 시 실행
        System.out.println("서버 종료 작업 실행완료");

    }
}

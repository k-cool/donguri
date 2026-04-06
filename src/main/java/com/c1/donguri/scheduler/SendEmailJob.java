package com.c1.donguri.scheduler;

import com.c1.donguri.util.EmailSend;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/*
    예약 시간이 되었을 때 Quartz가 실행하는 Job

    실행시 필요한 데이터는 EmailJobDTO로 소통
*/
public class SendEmailJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        String emailJobJSON = (String) context.getMergedJobDataMap().get("emailJob");

        EmailJobDTO emailJob = EmailJobDTO.fromJSON(emailJobJSON);

        try {
            EmailSend.EMAIL_SEND.send(emailJob.getRecipientEmail(), "[동구리] 동구리가 도착했어요.", emailJob.getContent());

            System.out.println("예약 메일 전송 완료: sendEmailJob_" + emailJob.getReservationId());


        } catch (Exception e) {
            System.out.println("예약 메일 전송 실패: sendEmailJob_" + emailJob.getReservationId());
            e.printStackTrace();

            throw new JobExecutionException(e);
        }
    }
}
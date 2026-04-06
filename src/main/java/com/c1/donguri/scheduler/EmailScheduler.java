package com.c1.donguri.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class EmailScheduler {
    public static final EmailScheduler EMAIL_SCHEDULER = new EmailScheduler();

    private EmailScheduler() {
    }

    public Scheduler scheduler = QuartzManager.getScheduler();

    public void enrollJob(EmailJobDTO emailJob) {

        try {
            if (scheduler == null) {
                throw new Exception("Quartz Scheduler 초기화 실패");
            }

            JobDetail job = JobBuilder.newJob(SendEmailJob.class)
                    .withIdentity("sendEmailJob_" + emailJob.getReservationId(), "emailGroup")
                    .usingJobData("emailJob", emailJob.toJSON())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("sendEmailTrigger_" + emailJob.getReservationId(), "emailGroup")
                    .startAt(emailJob.getScheduledDate())
                    .build();

            scheduler.scheduleJob(job, trigger);

            System.out.println("jobKey: " + job.getKey());
            System.out.println("triggerKey: " + trigger.getKey());
            System.out.println("잡등록 완료: " + emailJob.getReservationId());
        } catch (Exception e) {
            System.out.println("email job등록 실패");
            e.printStackTrace();
        }


    }
}

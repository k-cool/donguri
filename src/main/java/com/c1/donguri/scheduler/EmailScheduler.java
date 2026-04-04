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

    private Scheduler scheduler = QuartzManager.getScheduler();

    public void enrollJob(EmailJobDTO emailJob) throws Exception {

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

        System.out.println("EmailJob 등록 완료");
        System.out.println("jobKey: " + job.getKey());
        System.out.println("triggerKey: " + trigger.getKey());
    }
}

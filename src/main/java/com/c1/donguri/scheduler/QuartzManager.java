package com.c1.donguri.scheduler;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManager {

    private static final Scheduler SCHEDULER = createScheduler();

    private QuartzManager() {
    }

    private static Scheduler createScheduler() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            System.out.println("Quartz Scheduler started.");
            return scheduler;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Scheduler getScheduler() {
        return SCHEDULER;
    }
}
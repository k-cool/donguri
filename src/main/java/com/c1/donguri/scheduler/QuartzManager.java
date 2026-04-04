package com.c1.donguri.scheduler;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

/*
    Quartz Scheduler를 한 번만 생성해서 재사용하기 위한 클래스
*/
public class QuartzManager {

    private static Scheduler scheduler;

    static {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            System.out.println("Quartz Scheduler started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Scheduler getScheduler() {
        return scheduler;
    }
}
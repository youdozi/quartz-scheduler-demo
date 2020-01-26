package com.youdozi.demo.scheduler;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
 
/**
 * Scheduler to schedule and start the configured jobs
 */
@Component
public class QuartzScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(QuartzScheduler.class);
 
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    
    @Autowired
    private JobSchedulerModelGenerator jobSchedulerModelGenerator;
    
    @Autowired
    private Environment env;
    
    @PostConstruct
    public void init() {
    	String schedulerStartFlag = env.getProperty("scheduler.start.flag");
    	if(schedulerStartFlag.equals("Y")){
    		scheduleJobs();
    	}
    }
    
    @PreDestroy
    public void shutdown() {
    	String schedulerStartFlag = env.getProperty("scheduler.start.flag");
    	if(schedulerStartFlag.equals("Y")){
    		scheduleJobsShutDown();
    	}
    }
    
 
    public void scheduleJobs() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<JobScheduleModel> jobScheduleModels = jobSchedulerModelGenerator.generateModelList();
        for (JobScheduleModel model : jobScheduleModels) {
            try {
            	
            	if(scheduler.checkExists(model.getJobDetail().getKey())) {
            		scheduler.deleteJob(model.getJobDetail().getKey());
            	}
                scheduler.scheduleJob(model.getJobDetail(), model.getTrigger());
                
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        try {
            scheduler.start();
        } catch (SchedulerException e) {
        	e.printStackTrace();
        }
    }
    
    public void scheduleJobsShutDown() {
        try {
        	schedulerFactoryBean.destroy();
        } catch (SchedulerException e) {
        	e.printStackTrace();
        }
    }
    
    public void schedulerTrigger(String jobName, String jobGroup) throws Exception{
    	
    	Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	JobKey jobKey = new JobKey(jobName, jobGroup);
		scheduler.triggerJob(jobKey);
		
		log.info("스케줄러 트리거 사용 완료");
    }
    
    public void schedulerDelete(String jobName, String jobGroup) throws Exception{
    	
    	Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	JobKey jobKey = new JobKey(jobName, jobGroup);
    	scheduler.deleteJob(jobKey);
    	
    	log.info("스케줄러 삭제 완료");
    }
    
    public void schedulerAdd(long batchJobSeq) throws Exception{
    	
    	Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	JobScheduleModel jobScheduleModel = jobSchedulerModelGenerator.generateModelInfo(batchJobSeq);
    	
    	try {
            scheduler.scheduleJob(jobScheduleModel.getJobDetail(), jobScheduleModel.getTrigger());
            
            log.info("스케줄러 추가 완료");
            
        } catch (SchedulerException e) {
        	e.printStackTrace();
        }
    }
    
    public void schedulerUpdate(long batchJobSeq) throws Exception{
    	
    	Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	Trigger oldTrigger = scheduler.getTrigger(jobSchedulerModelGenerator.getTriggerKey(batchJobSeq));
    	JobScheduleModel jobScheduleModel = jobSchedulerModelGenerator.generateModelInfo(batchJobSeq);
    	
    	scheduler.rescheduleJob(oldTrigger.getKey(), jobScheduleModel.getTrigger());
    	
    	log.info("스케줄러 수정 완료");
    }
    
    public void schedulerRestart(String jobName, String jobGroup) throws Exception{
    	
    	Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	JobKey jobKey = new JobKey(jobName, jobGroup);
    	scheduler.resumeJob(jobKey);
    	
    	log.info("스케줄러 재시작 완료");
    }
    
    public void schedulerStop(String jobName, String jobGroup) throws Exception{
    	
    	Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	JobKey jobKey = new JobKey(jobName, jobGroup);
    	scheduler.pauseJob(jobKey);
    	
    	log.info("스케줄러 일시 정지 완료");
    }
    
}
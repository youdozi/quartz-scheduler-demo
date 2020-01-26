package com.youdozi.demo.scheduler;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.youdozi.demo.service.BatchJobService;
 
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JobRunner implements Job {
	
	private static final Logger log = LoggerFactory.getLogger(JobRunner.class);
 
	@Autowired
	private BatchJobService batchJobService;
	
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	
    	JobDataMap dataMap = context.getJobDetail().getJobDataMap();
    	 
        String command = dataMap.getString("command");
        
        try {
        
        	batchJobService.jobAction(command);
        	
        	log.info(command + " : 배치 실행 중");
        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        } 
    	
    }
    
}
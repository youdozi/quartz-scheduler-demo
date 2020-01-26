package com.youdozi.demo.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobsListenerService implements JobListener {
	
	private static final Logger log = LoggerFactory.getLogger(JobsListenerService.class);

    @Override
    public String getName() {
        return "Main Listener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
    	log.info("Job to be executed " + context.getJobDetail().getKey().getName());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
    	log.info("Job execution vetoed " + context.getJobDetail().getKey().getName());
    }

    @Override
    public void jobWasExecuted(
        JobExecutionContext context, JobExecutionException jobException) {
    	log.info(
            "Job was executed " +
            context.getJobDetail().getKey().getName() +
            (jobException != null ? ", with error" : "")
        );
    }
}
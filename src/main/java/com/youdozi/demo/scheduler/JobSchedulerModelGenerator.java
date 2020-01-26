package com.youdozi.demo.scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.youdozi.demo.entity.BatchJob;
import com.youdozi.demo.repository.BatchJobRepository;

@Component
public class JobSchedulerModelGenerator {
 
    public static final String GROUP_NAME = "Batch";
 
    private BatchJobRepository batchJobRepository;
    
    @Autowired
    public JobSchedulerModelGenerator(BatchJobRepository batchJobRepository) {
        this.batchJobRepository = batchJobRepository;
    }
 
    public List<JobScheduleModel> generateModelList() {
    	
    	List<BatchJob> batchJobList = batchJobRepository.findByUseYn("Y");
    	List<JobScheduleModel> generatedModels = new ArrayList<>();
    	
    	for (int i = 0; i < batchJobList.size(); i++) {
    		JobScheduleModel model = generateModelFrom(batchJobList.get(i));
            generatedModels.add(model);
        }
        return generatedModels;
    }
    
    public JobScheduleModel generateModelInfo(long batchJobSeq){
    	BatchJob batchJob = batchJobRepository.findByBatchJobSeqAndUseYn(batchJobSeq, "Y");
    	
    	JobScheduleModel generatedModel = generateModelFrom(batchJob);
    	
    	return generatedModel;
    }
 
    private JobScheduleModel generateModelFrom(BatchJob batchJob) {
        JobDetail jobDetail = getJobDetailFor(batchJob.getCommand(), GROUP_NAME, batchJob);
 
        Trigger trigger = getTriggerFor(jobDetail, batchJob);
        JobScheduleModel jobScheduleModel = new JobScheduleModel(jobDetail, trigger);
        return jobScheduleModel;
    }
 
    private JobDetail getJobDetailFor(String jobName, String groupName, BatchJob batchJob) {
        JobDetail jobDetail = JobBuilder.newJob(JobRunner.class)
        		.usingJobData(getJobDataMapFrom(batchJob))
                .withDescription("CRON expression : " + batchJob.getCron())
                .withIdentity(jobName, groupName)
                .build();
        return jobDetail;
    }
 
    private JobDataMap getJobDataMapFrom(BatchJob batchJob) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("command", batchJob.getCommand());
        return jobDataMap;
    }
 
    private Trigger getTriggerFor(JobDetail jobDetail, BatchJob batchJob) {
    	
    	Trigger trigger = null;
    	if(batchJob.getJobType().equals("cron")){
    		trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(batchJob.getCommand(), GROUP_NAME)
                    .withSchedule(cronSchedule(batchJob.getCron()))
//                    .startNow()
                    .build();
    	} else if(batchJob.getJobType().equals("delay")){
    		trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(batchJob.getCommand(), GROUP_NAME)
                    .withSchedule(simpleSchedule().withIntervalInSeconds(Integer.parseInt(batchJob.getDelay())).repeatForever().withMisfireHandlingInstructionNowWithExistingCount())
//                    .startNow()
                    .build();
    	}
        
        return trigger;
    }
    
	public TriggerKey getTriggerKey(long batchJobSeq){
		BatchJob batchJob = batchJobRepository.findById(batchJobSeq).get();
    	
    	return new TriggerKey(batchJob.getCommand(), GROUP_NAME);
    }
}

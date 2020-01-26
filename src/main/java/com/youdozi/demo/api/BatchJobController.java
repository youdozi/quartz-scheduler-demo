package com.youdozi.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.youdozi.demo.entity.BatchJob;
import com.youdozi.demo.service.BatchJobService;

@RestController
@RequestMapping("/batchJob")
public class BatchJobController {
	
	@Autowired
	private BatchJobService batchJobService;
	
	@RequestMapping(method = RequestMethod.POST, value="/action")
    public ResponseEntity<BatchJob> action(@RequestBody BatchJob batchJob) throws Exception {
		
		batchJobService.jobAction(batchJob.getCommand());
		
        return new ResponseEntity<BatchJob>(batchJob, HttpStatus.OK);
    }
}
 
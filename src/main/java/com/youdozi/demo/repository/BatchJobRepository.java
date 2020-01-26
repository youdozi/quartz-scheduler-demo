package com.youdozi.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youdozi.demo.entity.BatchJob;

public interface BatchJobRepository extends JpaRepository<BatchJob, Long>{

	public List<BatchJob> findByUseYn(String useYn);
	
	public BatchJob findByBatchJobSeqAndUseYn(long batchJobSeq, String useYn);
	
	@Query(value = "SELECT bj.command " +
	 		 		 "FROM batch_job bj " +
	 		 	 "GROUP BY bj.command", nativeQuery = true)
	public List<String> findAllJobName();
}

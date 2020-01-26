INSERT INTO batch_job (batch_job_seq,command,cron,delay,job_type,use_yn) VALUES 
(1,’testJob','0 0/15 * 1/1 * ? *',NULL,'cron',Y')
;
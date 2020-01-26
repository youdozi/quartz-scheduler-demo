package com.youdozi.demo.scheduler;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class SchedulerShowdown {
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulerShowdown.class);

	@Bean
	public SmartLifecycle GracefulShutdownHookForQuartz(SchedulerFactoryBean schedulerFactoryBean) { 
		
		return new SmartLifecycle() {
	        private boolean isRunning = false;
	        
	        @Override
	        public boolean isAutoStartup() {
	            return true;
	        }

	        @Override
	        public void stop(Runnable callback) {
	            stop();
	            logger.info("Spring container is shutting down.");
	            callback.run();
	        }

	        @Override
	        public void start() {
	            logger.info("Quartz Graceful Shutdown Hook started.");
	            isRunning = true;
	        }

	        @Override
	        public void stop() {
	            isRunning = false;
	            try {
	                logger.info("Quartz Graceful Shutdown... ");
	                schedulerFactoryBean.destroy();
	            } catch (SchedulerException e) {
	                try {
	                    logger.info(
	                            "Error shutting down Quartz: " + e.getMessage(), e);
	                    schedulerFactoryBean.getScheduler().shutdown(false);
	                } catch (SchedulerException ex) {
	                    logger.error("Unable to shutdown the Quartz scheduler.", ex);
	                }
	            }
	        }

	        @Override
	        public boolean isRunning() {
	            return isRunning;
	        }

	        @Override
	        public int getPhase() {
	            return Integer.MAX_VALUE;
	        }
	    };
	}
}

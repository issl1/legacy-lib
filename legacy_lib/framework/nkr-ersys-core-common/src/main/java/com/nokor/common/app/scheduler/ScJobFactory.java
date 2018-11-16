package com.nokor.common.app.scheduler;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author phirun.kong
 *
 */
public class ScJobFactory implements JobFactory {

	private static final Logger logger = LoggerFactory.getLogger(ScJobFactory.class);


	/**
	 * 
	 */
	public ScJobFactory() {
	}

	@Override
	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
		JobDetail jobDetails = bundle.getJobDetail();
		Job job = null;
		try {
			job = jobDetails.getJobClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("Fail to create job instance", e);
			throw new SchedulerException("Fail to create job instance", e);
		}
		if (job instanceof ScJob) {
		} else {
			logger.warn("Job {" + job.toString() + "} is not instance of NkrJob.");
		}
		return job;
	}

}

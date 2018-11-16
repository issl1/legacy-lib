package com.nokor.efinance.gui.job.contract;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.efinance.core.helper.FinServicesHelper;


/**
 * 
 * @author uhout.cheng
 */
public class LockSplitsJob extends QuartzJobBean implements FinServicesHelper {
	
	private Logger LOG = LoggerFactory.getLogger(LockSplitsJob.class);

	@Override
	protected void executeInternal(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
		LOG.info(">> Start LockSplitsJob");
		
		try {
			LCK_SPL_SRV.updateLockSplitsToExpired();
			LCK_SPL_SRV.updatePromisesToExpired();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			LOG.info("== Retry LockSplitsJob 1");
		}
		
		LOG.info("<< End LockSplitsJob");
	}

}

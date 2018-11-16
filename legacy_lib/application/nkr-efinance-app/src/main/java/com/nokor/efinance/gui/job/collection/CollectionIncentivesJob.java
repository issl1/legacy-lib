package com.nokor.efinance.gui.job.collection;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.efinance.core.shared.contract.ContractEntityField;

/**
 * @author youhort.ly
 */
public class CollectionIncentivesJob extends QuartzJobBean implements ContractEntityField {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
		
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO PYI
//CollectionIncentiveReportService collectionIncentiveReportService = SpringUtils.getBean(CollectionIncentiveReportService.class);
//		logger.info(">> Start CollectionIncentivesJob");
//		collectionIncentiveReportService.calculateCollectionIncentiveReport(DateUtils.todayH00M00S00());
//		logger.info("<< End CollectionIncentivesJob");
	}	
}

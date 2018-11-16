package com.nokor.efinance.gui.job.contract;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.efinance.core.shared.contract.ContractEntityField;

/**
 * @author youhort.ly
 */
public class AssignOverdueContractsJob extends QuartzJobBean implements ContractEntityField {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
		
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO PYI
//		CollectionService collectionService = SpringUtils.getBean(CollectionService.class);
//		try {
//			logger.info(">> Start AssignOverdueContractsJob");
//			collectionService.assignOverdueContracts();
//			logger.info("<< End AssignOverdueContractsJob");
//		} catch (DataAccessException e) {
//			logger.error(e.getMessage(), e);
//		}
	}
	
}

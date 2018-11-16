package com.nokor.efinance.gui.job.contract;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.efinance.core.collection.service.ContractOtherDataService;
import com.nokor.efinance.core.shared.contract.ContractEntityField;

/**
 * @author youhort.ly
 */
public class ContractsOtherDataJob extends QuartzJobBean implements ContractEntityField {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		ContractOtherDataService contractOtherDataService = SpringUtils.getBean(ContractOtherDataService.class);
		
		logger.info(">> Start ContractsOtherDataJob");
		try {
			contractOtherDataService.calculateOtherDataContracts();
		} catch (Exception e) {			
			logger.error(e.getMessage(), e);			
		}
		logger.info("<< End ContractsOtherDataJob");
	}
	
}

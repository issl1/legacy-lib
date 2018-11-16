package com.nokor.efinance.gui.job.contract;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.shared.contract.ContractEntityField;

/**
 * @author youhort.ly
 */
public class ContractAutoReconnectJob extends QuartzJobBean implements ContractEntityField {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		ContractService contractService = SpringUtils.getBean(ContractService.class);
		
		logger.info(">> Start ContractAutoReconnectJob");
		contractService.getByReference("GLF-BTB-04-00015327");
		logger.info("<< End ContractAutoReconnectJob");
	}
	
}

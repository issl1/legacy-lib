package com.nokor.efinance.gui.job.contract;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.efinance.core.collection.service.ContractRedemptionService;
import com.nokor.efinance.core.shared.contract.ContractEntityField;

/**
 * 
 * @author buntha.chea
 *
 */
public class ContractRedemptionJob extends QuartzJobBean implements ContractEntityField {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void executeInternal(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {

		ContractRedemptionService contractRedemptionService = SpringUtils.getBean(ContractRedemptionService.class);
		
		logger.info(">> Start Calculate Redemption Period");
		try {
			contractRedemptionService.calculateRedemptionPeriod();
		} catch (Exception e) {			
			logger.error(e.getMessage(), e);			
		}
		logger.info("<< End Calculate Redemption Period");
	}

}

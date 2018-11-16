package com.nokor.efinance.gui.job.contract;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.efinance.core.collection.service.CollectionService;
import com.nokor.efinance.core.shared.contract.ContractEntityField;

public class AssignCollectionContractsJob extends QuartzJobBean implements ContractEntityField {
	
	private Logger LOG = LoggerFactory.getLogger(AssignCollectionContractsJob.class);

	@Override
	protected void executeInternal(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
		LOG.info(">> Start AssignCollectionContractsJob");
		
		CollectionService collectionService = SpringUtils.getBean(CollectionService.class);
		collectionService.assignPhoneContracts(DateUtils.todayH00M00S00());
		collectionService.assignFieldContracts();
		collectionService.assignInsideRepoContracts();
		collectionService.assignOAContracts();
		
		LOG.info("<< End AssignCollectionContractsJob");
	}

}

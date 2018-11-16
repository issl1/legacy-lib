package com.nokor.efinance.gui.job.contract;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.efinance.core.helper.FinServicesHelper;

public class AssignCallCenterContractsJob extends QuartzJobBean implements FinServicesHelper {
	
	private Logger LOG = LoggerFactory.getLogger(AssignCallCenterContractsJob.class);

	@Override
	protected void executeInternal(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
		LOG.info(">> Start AssignCallCenterContractsJob");
			
		Date processDate = DateUtils.addDaysDate(DateUtils.todayH00M00S00(), -7);
		
		try {
			CALL_CTR_SRV.assignContracts(processDate);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			LOG.info("== Retry AssignCallCenterContractsJob 1");
			try {
				CALL_CTR_SRV.assignContracts(processDate);
			} catch (Exception e1) {
				LOG.error(e.getMessage(), e);
				LOG.info("== Retry AssignCallCenterContractsJob 2");
				CALL_CTR_SRV.assignContracts(processDate);
			}
		}
		
		LOG.info("<< End AssignCallCenterContractsJob");
	}

}

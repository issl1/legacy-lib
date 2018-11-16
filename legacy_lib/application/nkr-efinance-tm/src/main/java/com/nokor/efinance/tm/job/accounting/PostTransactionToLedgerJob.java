package com.nokor.efinance.tm.job.accounting;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;

/**
 * @author bunlong.taing
 */
public class PostTransactionToLedgerJob extends QuartzJobBean implements ErsysAccountingAppServicesHelper {
	
	protected Logger logger = LoggerFactory.getLogger(PostTransactionToLedgerJob.class);

	/**
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info(">> Start PostTransactionToLedgerJob");
		
		List<JournalEntry> entries = ACCOUNTING_SRV.getJournalEntryValidated();
		ACCOUNTING_SRV.postJournalEntriesIntoLedger(entries);
		
		logger.info("<< End PostTransactionToLedgerJob");
	}

}

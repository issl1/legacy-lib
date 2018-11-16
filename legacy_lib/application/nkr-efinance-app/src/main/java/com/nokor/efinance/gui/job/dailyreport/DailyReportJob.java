package com.nokor.efinance.gui.job.dailyreport;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.glf.report.service.DailyReportService;

/**
 * @author youhort.ly
 */
public class DailyReportJob extends QuartzJobBean {
		
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	
		DailyReportService dailyReportService = SpringUtils.getBean(DailyReportService.class);
		
		List<Dealer> dealers = DataReference.getInstance().getDealers();
		
		for (Dealer dealer : dealers) {
			dailyReportService.calculateDailyReportByDealer(dealer, DateUtils.today(), true);
		}
			
	}
}

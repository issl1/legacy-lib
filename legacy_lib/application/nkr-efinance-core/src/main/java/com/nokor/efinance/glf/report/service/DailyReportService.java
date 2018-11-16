package com.nokor.efinance.glf.report.service;

import java.util.Date;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.glf.report.model.DailyReport;


/**
 * @author ly.youhort
 */
public interface DailyReportService extends BaseEntityService {
	
	/**
	 * @param report
	 * @param reportParameter
	 */
	DailyReport calculateDailyReportByDealer(Dealer dealer, Date selectDate);
	
	/**
	 * @param report
	 * @param reportParameter
	 * @param closed
	 */
	DailyReport calculateDailyReportByDealer(Dealer dealer, Date selectDate, boolean closed);
	
	/**
	 * @param from
	 * @param to
	 */
	void recalculateDailyReport(Date from, Date to);
	
	/**
	 * @param selectDate
	 */
	void recalculateDailyReportOnSelectDate(Date selectDate);
}

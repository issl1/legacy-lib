package com.nokor.efinance.tools.report.service;

import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.core.shared.report.ReportParameter;


/**
 * @author ly.youhort
 */
public interface ReportService {
	
	/**
	 * @param report
	 * @param reportParameter
	 */
	String extract(Class<? extends Report> report, ReportParameter reportParameter) throws Exception;
}

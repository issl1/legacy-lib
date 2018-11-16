package com.nokor.efinance.tools.report.service.impl;

import org.springframework.stereotype.Service;

import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.service.ReportService;

/**
 * Report service
 * @author ly.youhort
 */
@Service("reportService")
public class ReportServiceImpl implements ReportService {

	@Override
	public String extract(Class<? extends Report> reportClass, ReportParameter reportParameter) throws Exception {
		Report report = reportClass.newInstance();
		return report.generate(reportParameter);
	}
}

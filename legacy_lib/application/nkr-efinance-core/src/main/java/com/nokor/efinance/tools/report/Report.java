package com.nokor.efinance.tools.report;

import com.nokor.efinance.core.shared.report.ReportParameter;


/**
 * 
 * @author ly.youhort
 *
 */
public interface Report  {

	/**
	 * @param reportParameter
	 * @return
	 * @throws Exception
	 */
	String generate(ReportParameter reportParameter) throws Exception;
}

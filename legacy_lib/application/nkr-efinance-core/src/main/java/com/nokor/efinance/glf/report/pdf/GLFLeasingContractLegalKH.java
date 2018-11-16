package com.nokor.efinance.glf.report.pdf;

import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.core.shared.report.ReportParameter;

/**
 * @author ly.youhort
 *
 */
public class GLFLeasingContractLegalKH implements Report {

	public GLFLeasingContractLegalKH() {
	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		boolean stamp = (Boolean) reportParameter.getParameters().get("stamp"); 
        return stamp ? "GLFLeasingContractLegalStamp.pdf" : "GLFLeasingContractLegal.pdf";
	}
	
}

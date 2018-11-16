package com.nokor.efinance.glf.report.pdf;

import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.core.shared.report.ReportParameter;

/**
 * @author ly.youhort
 *
 */
public class GLFGuaranteeContractLegalKH implements Report {

	public GLFGuaranteeContractLegalKH() {
	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		boolean stamp = (Boolean) reportParameter.getParameters().get("stamp"); 
        return stamp ? "GLFGuaranteeContractLegalStamp.pdf" : "GLFGuaranteeContractLegal.pdf";
	}
	
}

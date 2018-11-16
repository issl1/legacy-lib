package com.nokor.efinance.gui.report.parameter;

import java.util.List;

import com.nokor.efinance.core.quotation.model.Quotation;

public class QuotationReportParameter  {

	private Long quotaId;
	private List<Quotation> lstQuotations;
	

	public List<Quotation> getLstQuotations() {
		return lstQuotations;
	}

	public void setLstQuotations(List<Quotation> lstQuotations) {
		this.lstQuotations = lstQuotations;
	}

	public List<?> getListEntity() {
		return lstQuotations;
	}

	public Long getId() {
		return quotaId;
	}

	/**
	 * @return the quotaId
	 */
	public Long getQuotaId() {
		return quotaId;
	}

	/**
	 * @param quotaId the quotaId to set
	 */
	public void setQuotaId(Long quotaId) {
		this.quotaId = quotaId;
	}

}

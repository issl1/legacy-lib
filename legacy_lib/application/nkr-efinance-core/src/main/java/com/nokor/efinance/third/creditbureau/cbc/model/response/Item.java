package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author sun.vanndy
 *
 */
public class Item implements Serializable {

	private static final long serialVersionUID = 2982064994011231491L;
	
	private String enquiryReference;
	private RspReport rspReport;
	private List<Error> errors;
	private Parser parser;
	private Integer noErrors;
	/**
	 * @return the enquiryReference
	 */
	public String getEnquiryReference() {
		return enquiryReference;
	}
	/**
	 * @param enquiryReference the enquiryReference to set
	 */
	public void setEnquiryReference(String enquiryReference) {
		this.enquiryReference = enquiryReference;
	}
	/**
	 * @return the rspReport
	 */
	public RspReport getRspReport() {
		return rspReport;
	}
	/**
	 * @param rspReport the rspReport to set
	 */
	public void setRspReport(RspReport rspReport) {
		this.rspReport = rspReport;
	}
	/**
	 * @return the errors
	 */
	public List<Error> getErrors() {
		return errors;
	}
	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
	/**
	 * @return the parser
	 */
	public Parser getParser() {
		return parser;
	}
	/**
	 * @param parser the parser to set
	 */
	public void setParser(Parser parser) {
		this.parser = parser;
	}
	/**
	 * @return the noErrors
	 */
	public Integer getNoErrors() {
		return noErrors;
	}
	/**
	 * @param noErrors the noErrors to set
	 */
	public void setNoErrors(Integer noErrors) {
		this.noErrors = noErrors;
	}
}

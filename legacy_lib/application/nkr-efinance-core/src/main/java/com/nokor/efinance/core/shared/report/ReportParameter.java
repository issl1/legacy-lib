package com.nokor.efinance.core.shared.report;

import java.util.HashMap;
import java.util.Map;


/**
 * @author ly.youhort
 */
public class ReportParameter {
	
	private Map<String, Object> parameters;

	public ReportParameter() {
		parameters = new HashMap<String, Object>();
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(String key, Object value) {
		parameters.put(key, value);
	}
	
	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
}

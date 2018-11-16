package com.nokor.efinance.gui.ui.panel.map.model;

import java.io.Serializable;

import com.google.gwt.json.client.JSONObject;
import com.nokor.common.app.workflow.model.EWkfStatus;

public class MarkerInfo implements Serializable {

	private static final long serialVersionUID = 2798315691394103879L;
	private String desc;
	private int numQuotation;
	private EWkfStatus quotationStatus;
	private long communeId;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getNumQuotatio() {
		return numQuotation;
	}

	public void setNumQuotatio(int numQuotation) {
		this.numQuotation = numQuotation;
	}

	public EWkfStatus getWkfStatus() {
		return quotationStatus;
	}

	public void setWkfStatus(EWkfStatus quotationStatus) {
		this.quotationStatus = quotationStatus;
	}

	public long getCommuneId() {
		return communeId;
	}

	public void setCommuneId(long communeId) {
		this.communeId = communeId;
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();
		/*jsonObject.put("desc", desc);
		jsonObject.put("numQuotation", numQuotation);
		jsonObject.put("quotationStatus", quotationStatus.getId());
		jsonObject.put("communeId", communeId);
*/
		return jsonObject;
	}
}

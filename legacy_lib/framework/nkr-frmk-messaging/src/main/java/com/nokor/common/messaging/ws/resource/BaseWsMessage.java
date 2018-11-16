package com.nokor.common.messaging.ws.resource;


/**
 * 
 * @author prasnar
 *
 */
public class BaseWsMessage {
	public static final BaseWsMessage OK = new BaseWsMessage("OK");
	public static final BaseWsMessage ERROR_EXCEPTION = new BaseWsMessage("Error exception");
	public static final BaseWsMessage DATA_MISSING = new BaseWsMessage("Data is missing");
	
	
	protected String desc;
	
	/**
	 */
	protected BaseWsMessage(String desc) {
		this.desc = desc;
	}
	

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}


	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}		
}

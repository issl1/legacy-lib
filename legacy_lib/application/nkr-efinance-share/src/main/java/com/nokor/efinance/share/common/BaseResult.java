package com.nokor.efinance.share.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseResult implements Serializable {
	/** */
	private static final long serialVersionUID = 5555451718113354265L;

	public final static int OK = 1;
	public final static int KO = 0;
	
	private int status;
	private List<FinWsMessage> messages = new ArrayList<>();
	
	/**
	 * 
	 */
	public BaseResult() {
		
	}
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * @return the messages
	 */
	public List<FinWsMessage> getMessages() {
		return messages;
	}
	
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<FinWsMessage> messages) {
		this.messages = messages;
	}
		
	/**
	 * 
	 * @param msg
	 */
	public void addMessage(FinWsMessage msg) {
		this.messages.add(msg);
	}
	
}

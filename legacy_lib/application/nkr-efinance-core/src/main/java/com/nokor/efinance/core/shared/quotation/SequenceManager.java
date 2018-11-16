package com.nokor.efinance.core.shared.quotation;


/**
 * Sequence manager
 * @author ly.youhort
 *
 */
public final class SequenceManager {

	private long quotationReferenceNumber;
			
	private SequenceManager() {}
	
	/** Holder */
	private static class SingletonHolder {
		private final static SequenceManager instance = new SequenceManager();
	}
	 
	/**
	 * @return
	 */
	public static SequenceManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * @return the quotationReferenceNumber
	 */
	public synchronized long getQuotationReferenceNumber() {
		return quotationReferenceNumber;
	}

	/**
	 * @param quotationReferenceNumber the quotationReferenceNumber to set
	 */
	public synchronized void setQuotationReferenceNumber(long quotationReferenceNumber) {
		if (this.quotationReferenceNumber < quotationReferenceNumber) {
			this.quotationReferenceNumber = quotationReferenceNumber;
		}
	}	
}

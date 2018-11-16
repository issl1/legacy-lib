package com.nokor.efinance.share.locksplit;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.efinance.share.dealer.DealerDTO;


/**
 * 
 * @author uhout.cheng
 */
public class LockSplitDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 6981133378203747622L;

	private Long id;
	private String createdUser;
	private Date createdDate;
	private String lockSplitNo;
	private String contractID;
	private Date from;
	private Date to;
	private Double totalAmount;
	private Double totalVatAmount;
	private LockSplitStatus status;
	private LockSplitEventType eventType;
	private boolean sendEmail;
	private boolean sendSms;
	private boolean promise;
	private String comment;
	private DealerDTO dealer;
	private RefDataDTO paymentChannel;
	
	private List<String> callBackUrls;
	private List<LockSplitItemDTO> lockSplitItemDTOs;
	private List<LockSplitRecapDTO> lockSplitRecapDTOs;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the createdUser
	 */
	public String getCreatedUser() {
		return createdUser;
	}

	/**
	 * @param createdUser the createdUser to set
	 */
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the lockSplitNo
	 */
	public String getLockSplitNo() {
		return lockSplitNo;
	}

	/**
	 * @param lockSplitNo the lockSplitNo to set
	 */
	public void setLockSplitNo(String lockSplitNo) {
		this.lockSplitNo = lockSplitNo;
	}

	/**
	 * @return the contractID
	 */
	public String getContractID() {
		return contractID;
	}

	/**
	 * @param contractID the contractID to set
	 */
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}

	/**
	 * @return the from
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Date from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Date to) {
		this.to = to;
	}

	/**
	 * @return the totalAmount
	 */
	public Double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the totalVatAmount
	 */
	public Double getTotalVatAmount() {
		return totalVatAmount;
	}

	/**
	 * @param totalVatAmount the totalVatAmount to set
	 */
	public void setTotalVatAmount(Double totalVatAmount) {
		this.totalVatAmount = totalVatAmount;
	}

	/**
	 * @return the lockSplitItemDTOs
	 */
	public List<LockSplitItemDTO> getLockSplitItemDTOs() {
		return lockSplitItemDTOs;
	}

	/**
	 * @param lockSplitItemDTOs the lockSplitItemDTOs to set
	 */
	public void setLockSplitItemDTOs(List<LockSplitItemDTO> lockSplitItemDTOs) {
		this.lockSplitItemDTOs = lockSplitItemDTOs;
	}	
	
	/**
	 * @return the status
	 */
	public LockSplitStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(LockSplitStatus status) {
		this.status = status;
	}

	/**
	 * @return the eventType
	 */
	public LockSplitEventType getEventType() {
		return eventType;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(LockSplitEventType eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the callBackUrls
	 */
	public List<String> getCallBackUrls() {
		return callBackUrls;
	}

	/**
	 * @param callBackUrls the callBackUrls to set
	 */
	public void setCallBackUrls(List<String> callBackUrls) {
		this.callBackUrls = callBackUrls;
	}
	
	/**
	 * @return the sendEmail
	 */
	public boolean isSendEmail() {
		return sendEmail;
	}

	/**
	 * @param sendEmail the sendEmail to set
	 */
	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	/**
	 * @return the sendSms
	 */
	public boolean isSendSms() {
		return sendSms;
	}

	/**
	 * @param sendSms the sendSms to set
	 */
	public void setSendSms(boolean sendSms) {
		this.sendSms = sendSms;
	}
	
	/**
	 * @return the promise
	 */
	public boolean isPromise() {
		return promise;
	}

	/**
	 * @param promise the promise to set
	 */
	public void setPromise(boolean promise) {
		this.promise = promise;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * @return the dealer
	 */
	public DealerDTO getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(DealerDTO dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the paymentChannel
	 */
	public RefDataDTO getPaymentChannel() {
		return paymentChannel;
	}

	/**
	 * @param paymentChannel the paymentChannel to set
	 */
	public void setPaymentChannel(RefDataDTO paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	/**
	 * @return the lockSplitRecapDTOs
	 */
	public List<LockSplitRecapDTO> getLockSplitRecapDTOs() {
		return lockSplitRecapDTOs;
	}

	/**
	 * @param lockSplitRecapDTOs the lockSplitRecapDTOs to set
	 */
	public void setLockSplitRecapDTOs(List<LockSplitRecapDTO> lockSplitRecapDTOs) {
		this.lockSplitRecapDTOs = lockSplitRecapDTOs;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof LockSplitDTO)) {
			 return false;
		 }
		 LockSplitDTO lockSplitDTO = (LockSplitDTO) arg0;
		 return getId() != null && getId().equals(lockSplitDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}

}

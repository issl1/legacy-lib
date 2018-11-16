package com.nokor.efinance.core.payment.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.PaymentFile;
import com.nokor.efinance.core.payment.model.PaymentFileItem;

/**
 * @author bunlong.taing
 */
public class PaymentFileItemRestriction extends BaseRestrictions<PaymentFileItem> {

	/** */
	private static final long serialVersionUID = 4253598036231519518L;
	
	private Long paymentFileId;
	private EWkfStatus[] wkfStatuses;
	private String dealerCode;
	private String contractReference;
	private Date paymentDateFrom;
	private Date paymentDateTo;
	private Date uploadDateFrom;
	private Date uploadDateTo;
	private double amountFrom;
	private double amountTo;
	private List<EPaymentChannel> paymentChannels;
	private List<EPaymentMethod> paymentMethods;

	/**
	 */
	public PaymentFileItemRestriction() {
		super(PaymentFileItem.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		addOrder(Order.desc(PaymentFileItem.ID));
		if (paymentFileId != null) {
			addCriterion(Restrictions.eq(PaymentFileItem.PAYMENTFILE + DOT + PaymentFile.ID, paymentFileId));
		}
		if (wkfStatuses != null && wkfStatuses.length > 0) {
			addCriterion(Restrictions.in(PaymentFileItem.WKFSTATUS, wkfStatuses));
		}
		if (StringUtils.isNotEmpty(dealerCode)) {
			addCriterion(Restrictions.eq(PaymentFileItem.DEALERNO, dealerCode));
		}
		if (StringUtils.isNotEmpty(contractReference)) {
			addCriterion(Restrictions.eq(PaymentFileItem.CUSTOMERREF1, contractReference));
		}
		if (paymentDateFrom != null) {
			addCriterion(Restrictions.ge(PaymentFileItem.PAYMENTDATE, DateUtils.getDateAtBeginningOfDay(paymentDateFrom)));
		} 
		if (paymentDateTo != null) {
			addCriterion(Restrictions.le(PaymentFileItem.PAYMENTDATE, DateUtils.getDateAtEndOfDay(paymentDateTo)));
		}
		if (uploadDateFrom != null) {
			addCriterion(Restrictions.ge(PaymentFileItem.CREATEDATE, DateUtils.getDateAtBeginningOfDay(uploadDateFrom)));
		} 
		if (uploadDateTo != null) {
			addCriterion(Restrictions.le(PaymentFileItem.CREATEDATE, DateUtils.getDateAtEndOfDay(uploadDateTo)));
		}
		if (amountFrom > 0) {
			addCriterion(Restrictions.ge(PaymentFileItem.AMOUNT, amountFrom));
		}
		if (amountTo > 0) {
			addCriterion(Restrictions.le(PaymentFileItem.AMOUNT, amountTo));
		}
		if (paymentChannels != null && !paymentChannels.isEmpty()) {
			addCriterion(Restrictions.in(PaymentFileItem.PAYMENTCHANNEL, paymentChannels));
		}
		if (paymentMethods != null && !paymentMethods.isEmpty()) {
			addCriterion(Restrictions.in(PaymentFileItem.PAYMENTMETHOD, paymentMethods));
		}
	}

	/**
	 * @return the paymentFileId
	 */
	public Long getPaymentFileId() {
		return paymentFileId;
	}

	/**
	 * @param paymentFileId the paymentFileId to set
	 */
	public void setPaymentFileId(Long paymentFileId) {
		this.paymentFileId = paymentFileId;
	}

	/**
	 * @return the wkfStatuses
	 */
	public EWkfStatus[] getWkfStatuses() {
		return wkfStatuses;
	}

	/**
	 * @param wkfStatuses the wkfStatuses to set
	 */
	public void setWkfStatuses(EWkfStatus[] wkfStatuses) {
		this.wkfStatuses = wkfStatuses;
	}

	/**
	 * @return the dealerCode
	 */
	public String getDealerCode() {
		return dealerCode;
	}

	/**
	 * @param dealerCode the dealerCode to set
	 */
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	/**
	 * @return the contractReference
	 */
	public String getContractReference() {
		return contractReference;
	}

	/**
	 * @param contractReference the contractReference to set
	 */
	public void setContractReference(String contractReference) {
		this.contractReference = contractReference;
	}

	/**
	 * @return the paymentDateFrom
	 */
	public Date getPaymentDateFrom() {
		return paymentDateFrom;
	}

	/**
	 * @param paymentDateFrom the paymentDateFrom to set
	 */
	public void setPaymentDateFrom(Date paymentDateFrom) {
		this.paymentDateFrom = paymentDateFrom;
	}

	/**
	 * @return the paymentDateTo
	 */
	public Date getPaymentDateTo() {
		return paymentDateTo;
	}

	/**
	 * @param paymentDateTo the paymentDateTo to set
	 */
	public void setPaymentDateTo(Date paymentDateTo) {
		this.paymentDateTo = paymentDateTo;
	}

	/**
	 * @return the uploadDateFrom
	 */
	public Date getUploadDateFrom() {
		return uploadDateFrom;
	}

	/**
	 * @param uploadDateFrom the uploadDateFrom to set
	 */
	public void setUploadDateFrom(Date uploadDateFrom) {
		this.uploadDateFrom = uploadDateFrom;
	}

	/**
	 * @return the uploadDateTo
	 */
	public Date getUploadDateTo() {
		return uploadDateTo;
	}

	/**
	 * @param uploadDateTo the uploadDateTo to set
	 */
	public void setUploadDateTo(Date uploadDateTo) {
		this.uploadDateTo = uploadDateTo;
	}

	/**
	 * @return the amountFrom
	 */
	public double getAmountFrom() {
		return amountFrom;
	}

	/**
	 * @param amountFrom the amountFrom to set
	 */
	public void setAmountFrom(double amountFrom) {
		this.amountFrom = amountFrom;
	}

	/**
	 * @return the amountTo
	 */
	public double getAmountTo() {
		return amountTo;
	}

	/**
	 * @param amountTo the amountTo to set
	 */
	public void setAmountTo(double amountTo) {
		this.amountTo = amountTo;
	}

	/**
	 * @return the paymentChannels
	 */
	public List<EPaymentChannel> getPaymentChannels() {
		return paymentChannels;
	}

	/**
	 * @param paymentChannels the paymentChannels to set
	 */
	public void setPaymentChannels(List<EPaymentChannel> paymentChannels) {
		this.paymentChannels = paymentChannels;
	}

	/**
	 * @return the paymentMethods
	 */
	public List<EPaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

	/**
	 * @param paymentMethods the paymentMethods to set
	 */
	public void setPaymentMethods(List<EPaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

}

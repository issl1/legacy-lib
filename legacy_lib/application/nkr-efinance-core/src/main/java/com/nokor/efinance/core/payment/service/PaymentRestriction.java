package com.nokor.efinance.core.payment.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.FMEntityField;

/**
 * Payment Restriction
 * @author bunlong.taing
 */
public class PaymentRestriction extends BaseRestrictions<Payment> implements FMEntityField {
	/** */
	private static final long serialVersionUID = 4569521898973335558L;
	
	private String fullName;
	private Date birthDate;
	private Date paymentFrom;
	private Date paymentTo;
	private Date activatedDateFrom;
	private Date activatedDateTo;
	private Long contractId;
	private String contractReference;
	private Dealer dealer;
	private EPaymentType[] paymentTypes;

	/**
	 */
	public PaymentRestriction() {
		super(Payment.class);
	}

	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		addOrder(Order.desc(PAYMENT_DATE));
		if (contractId != null) {
			addCriterion(Restrictions.eq(Payment.CONTRACT + DOT + Payment.ID, contractId));
		}
		if (StringUtils.isNotEmpty(contractReference)) {
			DetachedCriteria userSubCriteria = DetachedCriteria.forClass(Cashflow.class, "cash");
			userSubCriteria.createAlias("cash.contract", "cont", JoinType.INNER_JOIN);
			userSubCriteria.add(Restrictions.ilike("cont."+ REFERENCE, contractReference, MatchMode.ANYWHERE));			
			userSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cash.payment.id")));
			addCriterion(Property.forName(Payment.ID).in(userSubCriteria));
		}
		if (paymentFrom != null) {       
			addCriterion(Restrictions.ge(PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(paymentFrom)));
		}
		if (paymentTo != null) {
			addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(paymentTo)));
		}
		
		if (activatedDateFrom != null || activatedDateTo != null) {       
			addAssociation(Payment.CONTRACT, "con", JoinType.INNER_JOIN);
			if (activatedDateFrom != null) {
				addCriterion(Restrictions.ge("con" + "." + Contract.STARTDATE, DateUtils.getDateAtBeginningOfDay(activatedDateFrom)));
			} 
			if (activatedDateTo != null) {
				addCriterion(Restrictions.le("con" + "." + Contract.STARTDATE, DateUtils.getDateAtEndOfDay(activatedDateTo)));
			}
		}
		
		if (StringUtils.isNoneEmpty(fullName)) {
			addAssociation("applicant", "app", JoinType.INNER_JOIN);
			addAssociation("app.individual", "ind", JoinType.INNER_JOIN);
			addCriterion(Restrictions.or(Restrictions.ilike("ind." + LAST_NAME, fullName, MatchMode.ANYWHERE),
					Restrictions.ilike("ind." + FIRST_NAME, fullName, MatchMode.ANYWHERE)));
		}
		if (dealer != null) {
			addCriterion(Restrictions.eq(DEALER + DOT + ID, dealer.getId()));
		}
		if (paymentTypes != null && paymentTypes.length > 0) {
			addCriterion(Restrictions.in(Payment.PAYMENTTYPE, paymentTypes));
		}
	}

	/**
	 * @return the contractId
	 */
	public Long getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return the paymentTypes
	 */
	public EPaymentType[] getPaymentTypes() {
		return paymentTypes;
	}

	/**
	 * @param paymentTypes the paymentTypes to set
	 */
	public void setPaymentTypes(EPaymentType[] paymentTypes) {
		this.paymentTypes = paymentTypes;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the paymentFrom
	 */
	public Date getPaymentFrom() {
		return paymentFrom;
	}

	/**
	 * @param paymentFrom the paymentFrom to set
	 */
	public void setPaymentFrom(Date paymentFrom) {
		this.paymentFrom = paymentFrom;
	}

	/**
	 * @return the paymentTo
	 */
	public Date getPaymentTo() {
		return paymentTo;
	}

	/**
	 * @param paymentTo the paymentTo to set
	 */
	public void setPaymentTo(Date paymentTo) {
		this.paymentTo = paymentTo;
	}

	/**
	 * @return the activatedDateFrom
	 */
	public Date getActivatedDateFrom() {
		return activatedDateFrom;
	}

	/**
	 * @param activatedDateFrom the activatedDateFrom to set
	 */
	public void setActivatedDateFrom(Date activatedDateFrom) {
		this.activatedDateFrom = activatedDateFrom;
	}

	/**
	 * @return the activatedDateTo
	 */
	public Date getActivatedDateTo() {
		return activatedDateTo;
	}

	/**
	 * @param activatedDateTo the activatedDateTo to set
	 */
	public void setActivatedDateTo(Date activatedDateTo) {
		this.activatedDateTo = activatedDateTo;
	}

	/**
	 * @return the dealer
	 */
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
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
	
}

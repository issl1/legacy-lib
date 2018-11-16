package com.nokor.efinance.core.collection.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tm_collectionincentive_report")
public class CollectionIncentiveReport extends EntityA {
	
	private static final long serialVersionUID = -5850483262377014737L;
	
	private Date date;
	private SecUser collectionOfficer;
	private Payment payment;
	private EWkfStatus collectionStatus;
	private EColTask collectionTask;
	private EColGroup collectionGroup;
	private ColCustField customerAttribute; 
	private Double amountPromiseToPayUsd;
	private Date startPeriodPromiseToPay;
	private Date endPeriodPromiseToPay;
	private Integer nbOverdueInDay;
	


	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "colec_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @return the date
	 */
	@Column(name = "colec_date", nullable = true)
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the payment
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymn_id")
	public Payment getPayment() {
		return payment;
	}

	/**
	 * @param payment the payment to set
	 */
	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	/**
	 * @return the collectionOfficer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seusr_id_cc")
	public SecUser getAssignee() {
		return collectionOfficer;
	}

	/**
	 * @param collectionOfficer the collectionOfficer to set
	 */
	public void setAssignee(SecUser collectionOfficer) {
		this.collectionOfficer = collectionOfficer;
	}

	/**
	 * @return the collectionStatus
	 */
    @Column(name = "wkf_sta_id", nullable = false)
    @Convert(converter = EWkfStatus.class)
	public EWkfStatus getCollectionStatus() {
		return collectionStatus;
	}

	/**
	 * @param collectionStatus the collectionStatus to set
	 */
	public void setCollectionStatus(EWkfStatus collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	/**
	 * @return the collectionTask
	 */
    @Column(name = "col_tas_id", nullable = true)
    @Convert(converter = EColTask.class)
	public EColTask getCollectionTask() {
		return collectionTask;
	}

	/**
	 * @param collectionTask the collectionTask to set
	 */
	public void setCollectionTask(EColTask collectionTask) {
		this.collectionTask = collectionTask;
	}	
	
	/**
	 * @return the collectionGroup
	 */
    //@Column(name = "col_grp_id", nullable = true)
    //@Convert(converter = EColGroup.class)
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_grp_id", nullable = true)
	public EColGroup getGroup() {
		return collectionGroup;
	}

	/**
	 * @param collectionGroup the collectionGroup to set
	 */
	public void setGroup(EColGroup collectionGroup) {
		this.collectionGroup = collectionGroup;
	}

	/**
	 * @return the customerAttribute
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cusat_id", nullable = true)
	public ColCustField getCustomerAttribute() {
		return customerAttribute;
	}

	/**
	 * @param customerAttribute the customerAttribute to set
	 */
	public void setCustomerAttribute(ColCustField customerAttribute) {
		this.customerAttribute = customerAttribute;
	}
	
	/**
	 * @return the amountPromiseToPayUsd
	 */
	@Column(name = "colec_am_amount_promise_to_pay_usd", nullable = true)
	public Double getAmountPromiseToPayUsd() {
		return amountPromiseToPayUsd;
	}

	/**
	 * @param amountPromiseToPayUsd the amountPromiseToPayUsd to set
	 */
	public void setAmountPromiseToPayUsd(Double amountPromiseToPayUsd) {
		this.amountPromiseToPayUsd = amountPromiseToPayUsd;
	}

	/**
	 * @return the startPeriodPromiseToPay
	 */
	@Column(name = "colec_dt_start_period_promise_to_pay", nullable = true)
	public Date getStartPeriodPromiseToPay() {
		return startPeriodPromiseToPay;
	}

	/**
	 * @param startPeriodPromiseToPay the startPeriodPromiseToPay to set
	 */
	public void setStartPeriodPromiseToPay(Date startPeriodPromiseToPay) {
		this.startPeriodPromiseToPay = startPeriodPromiseToPay;
	}

	/**
	 * @return the endPeriodPromiseToPay
	 */
	@Column(name = "colec_dt_end_period_promise_to_pay", nullable = true)
	public Date getEndPeriodPromiseToPay() {
		return endPeriodPromiseToPay;
	}

	/**
	 * @param endPeriodPromiseToPay the endPeriodPromiseToPay to set
	 */
	public void setEndPeriodPromiseToPay(Date endPeriodPromiseToPay) {
		this.endPeriodPromiseToPay = endPeriodPromiseToPay;
	}

	/**
	 * @return the nbOverdueInDay
	 */
	@Column(name = "colec_nb_overdue_in_day", nullable = true)
	public Integer getNbOverdueInDay() {
		return nbOverdueInDay;
	}

	/**
	 * @param nbOverdueInDay the nbOverdueInDay to set
	 */
	public void setNbOverdueInDay(Integer nbOverdueInDay) {
		this.nbOverdueInDay = nbOverdueInDay;
	}
	
}

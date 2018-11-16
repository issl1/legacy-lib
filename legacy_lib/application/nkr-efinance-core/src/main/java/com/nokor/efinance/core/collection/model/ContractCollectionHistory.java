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
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_contract_collection_history")
public class ContractCollectionHistory extends EntityA {

	private static final long serialVersionUID = -1893813037868210744L;
	
	private EWkfStatus status;
	private ColCustField custField; 
	private EColTask task;
	private EColGroup group;
	
	private Double amountPromiseToPayUsd;
	private Date startPeriodPromiseToPay;
	private Date endPeriodPromiseToPay;
	private SecUser collectionOfficer;
	private Integer nbOverdueInDays;
	private SecUser collectionOfficer2;
	
	private Collection collection;
	
	/**
	 * @return id
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_col_his_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    
	/**
	 * @return the collection
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_id", nullable = false)	
	public Collection getCollection() {
		return collection;
	}


	/**
	 * @param collection the collection to set
	 */
	public void setCollection(Collection collection) {
		this.collection = collection;
	}


	/**
	 * @return the status
	 */
    @Column(name = "wkf_sta_id", nullable = false)
    @Convert(converter = EWkfStatus.class)
	public EWkfStatus getCollectionStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setCollectionStatus(EWkfStatus status) {
		this.status = status;
	}

	/**
	 * @return the task
	 */
    @Column(name = "col_tas_id", nullable = true)
    @Convert(converter = EColTask.class)
	public EColTask getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(EColTask task) {
		this.task = task;
	}

	/**
	 * @return the group
	 */
    //@Column(name = "col_grp_id", nullable = true)
    //@Convert(converter = EColGroup.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_grp_id")
	public EColGroup getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(EColGroup group) {
		this.group = group;
	}

	/**
	 * @return the amountPromiseToPayUsd
	 */
    @Column(name = "con_col_his_am_amount_promise_to_pay_usd", nullable = true)
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
	@Column(name = "con_col_his_dt_start_period_promise_to_pay", nullable = true)
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
	@Column(name = "con_col_his_dt_end_period_promise_to_pay", nullable = true)
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
	 * @return the collectionOfficer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_cc")
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
	 * @return the nbOverdueInDays
	 */
    @Column(name = "con_col_his_nu_nb_overdue_in_days", nullable = true)
	public Integer getNbOverdueInDays() {
		return nbOverdueInDays;
	}

	/**
	 * @param nbOverdueInDays the nbOverdueInDays to set
	 */
	public void setNbOverdueInDays(Integer nbOverdueInDays) {
		this.nbOverdueInDays = nbOverdueInDays;
	}

	/**
	 * @return the custField
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_cus_fie_id", nullable = true)
	public ColCustField getCustField() {
		return custField;
	}

	/**
	 * @param custField the custField to set
	 */
	public void setCustField(ColCustField custField) {
		this.custField = custField;
	}

	/**
	 * @return the collectionOfficer2
	 */
	
	@Column(name = "con_col_his_collection_officer", nullable = true)
	public SecUser getCollectionOfficer2() {
		return collectionOfficer2;
	}

	/**
	 * @param collectionOfficer2 the collectionOfficer2 to set
	 */
	public void setCollectionOfficer2(SecUser collectionOfficer2) {
		this.collectionOfficer2 = collectionOfficer2;
	}
}

package com.nokor.efinance.core.contract.model;

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

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.collection.model.EPromiseStatus;
import com.nokor.efinance.core.collection.model.EPromiseType;

/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "td_contract_promise")
public class ContractPromise extends EntityA implements MPromise {
	
	/** */
	private static final long serialVersionUID = -3421473958774087433L;
	
	private Contract contract;
	private EPromiseStatus promiseStatus;
	private EPromiseType promiseType;
	private Double promiseAmount;
	private Date promiseDate;
	private String remark;
	private String createdBy;
	
	/**
     * @return
     */
    public static ContractPromise createInstance() {
    	ContractPromise colAssist = EntityFactory.createInstance(ContractPromise.class);
        return colAssist;
    }

    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prm_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}	

	/**
	 * @return the contract
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id")
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the promiseStatus
	 */
	@Column(name = "prm_sta_id", nullable = true)
	@Convert(converter = EPromiseStatus.class)
	public EPromiseStatus getPromiseStatus() {
		return promiseStatus;
	}

	/**
	 * @param promiseStatus the promiseStatus to set
	 */
	public void setPromiseStatus(EPromiseStatus promiseStatus) {
		this.promiseStatus = promiseStatus;
	}

	/**
	 * @return the promiseType
	 */
	@Column(name = "prm_typ_id", nullable = true)
	@Convert(converter = EPromiseType.class)
	public EPromiseType getPromiseType() {
		return promiseType;
	}

	/**
	 * @param promiseType the promiseType to set
	 */
	public void setPromiseType(EPromiseType promiseType) {
		this.promiseType = promiseType;
	}

	/**
	 * @return the promiseAmount
	 */
	@Column(name = "prm_am_promise_amount", nullable = true)
	public Double getPromiseAmount() {
		return promiseAmount;
	}

	/**
	 * @param promiseAmount the promiseAmount to set
	 */
	public void setPromiseAmount(Double promiseAmount) {
		this.promiseAmount = promiseAmount;
	}

	/**
	 * @return the promiseDate
	 */
	@Column(name = "prm_dt_promise_date", nullable = true)
	public Date getPromiseDate() {
		return promiseDate;
	}

	/**
	 * @param promiseDate the promiseDate to set
	 */
	public void setPromiseDate(Date promiseDate) {
		this.promiseDate = promiseDate;
	}

	/**
	 * @return the remark
	 */
	@Column(name = "prm_va_remark", nullable = true, length = 255)
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the createdBy
	 */
	@Column(name = "prm_va_created_by", nullable = true, length = 50)	
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}

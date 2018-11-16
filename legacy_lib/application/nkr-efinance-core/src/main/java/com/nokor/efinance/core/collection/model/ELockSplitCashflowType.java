package com.nokor.efinance.core.collection.model;

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

import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.MCashflowLockSplitType;
import com.nokor.efinance.core.financial.model.FinService;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_lock_split_cashflow_type")
public class ELockSplitCashflowType extends EntityA implements MCashflowLockSplitType {
		
	/**
	 */
	private static final long serialVersionUID = 3893753410944577908L;
	
	private ELockSplitType lockSplitType;
	private ECashflowType cashflowType;
	private FinService service;
	private Integer priority;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loc_spl_cfw_typ_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
		
	/**
	 * @return the lockSplitType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loc_spl_typ_id", nullable = true)
	public ELockSplitType getLockSplitType() {
		return lockSplitType;
	}

	/**
	 * @param lockSplitType the lockSplitType to set
	 */
	public void setLockSplitType(ELockSplitType lockSplitType) {
		this.lockSplitType = lockSplitType;
	}

	/**
	 * @return the cashflowType
	 */
    @Column(name = "cfw_typ_id", nullable = true)
    @Convert(converter = ECashflowType.class)
	public ECashflowType getCashflowType() {
		return cashflowType;
	}
    
	/**
	 * @param cashflowType the cashflowType to set
	 */
	public void setCashflowType(ECashflowType cashflowType) {
		this.cashflowType = cashflowType;
	}

	/**
	 * @return the priority
	 */
	@Column(name = "loc_spl_typ_nu_priority", nullable = true)
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * @return the service
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fin_srv_id")
	public FinService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(FinService service) {
		this.service = service;
	}	
}

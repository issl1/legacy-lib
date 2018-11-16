package com.nokor.efinance.core.collection.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * @author prasnar
 */
@Entity
@Table(name = "tu_lock_split_rule_item")
public class LockSplitRuleItem extends EntityA {
	
	private LockSplitRule lockSplitRule;
	private ELockSplitType lockSplitType;
	private Integer priority;
	
	/**
	 */
	private static final long serialVersionUID = 3893753410944577908L;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loc_spl_rul_item_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }	
	
	/**
	 * @return the lockSplitRule
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loc_spl_rul_id")
	public LockSplitRule getLockSplitRule() {
		return lockSplitRule;
	}

	/**
	 * @param lockSplitRule the lockSplitRule to set
	 */
	public void setLockSplitRule(LockSplitRule lockSplitRule) {
		this.lockSplitRule = lockSplitRule;
	}

	/**
	 * @return the lockSplitType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loc_spl_typ_id")
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
	 * @return the priority
	 */
	@Column(name = "loc_spl_rul_item_nu_priority", nullable = true)
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}	
}

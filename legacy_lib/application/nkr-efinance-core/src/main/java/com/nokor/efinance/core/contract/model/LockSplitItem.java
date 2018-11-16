package com.nokor.efinance.core.contract.model;

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

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.collection.model.ContractOperation;
import com.nokor.efinance.core.collection.model.ELockSplitCategory;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_lock_split_item")
public class LockSplitItem extends EntityWkf implements MLockSplitItem {

	private static final long serialVersionUID = 8935751397622214350L;
	
	private LockSplit lockSplit;
	private JournalEvent journalEvent; 
	private Double tiAmount;
	private Double vatAmount;
	private Integer priority;
	private ELockSplitCategory lockSplitCategory;
	private ContractOperation operation;
	
	/**
     * 
     * @return
     */
    public static LockSplitItem createInstance() {
    	LockSplitItem instance = EntityFactory.createInstance(LockSplitItem.class);
    	instance.setWkfStatus(LockSplitWkfStatus.LNEW);
        return instance;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loc_spl_ite_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the lockSplit
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loc_spl_id")
	public LockSplit getLockSplit() {
		return lockSplit;
	}

	/**
	 * @param lockSplit the lockSplit to set
	 */
	public void setLockSplit(LockSplit lockSplit) {
		this.lockSplit = lockSplit;
	}

	/**
	 * @return the journalEvent
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jou_eve_id")
	public JournalEvent getJournalEvent() {
		return journalEvent;
	}

	/**
	 * @param journalEvent the journalEvent to set
	 */
	public void setJournalEvent(JournalEvent journalEvent) {
		this.journalEvent = journalEvent;
	}

	/**
	 * @return the tiAmount
	 */
	@Column(name = "loc_spl_ite_am_ti_amount", nullable = true)
	public Double getTiAmount() {
		return tiAmount;
	}

	/**
	 * @param tiAmount the tiAmount to set
	 */
	public void setTiAmount(Double tiAmount) {
		this.tiAmount = tiAmount;
	}	
	
	/**
	 * @return the vatAmount
	 */
	@Column(name = "loc_spl_ite_am_vat_amount", nullable = true)
	public Double getVatAmount() {
		return vatAmount;
	}

	/**
	 * @param vatAmount the vatAmount to set
	 */
	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
	}

	/**
	 * @return the priority
	 */
	@Column(name = "loc_spl_ite_nu_priority", nullable = true)
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
	 * @return the lockSplitCategory
	 */
	@Column(name = "loc_spl_cat_id", nullable = true)
	@Convert(converter = ELockSplitCategory.class)
	public ELockSplitCategory getLockSplitCategory() {
		return lockSplitCategory;
	}

	/**
	 * @param lockSplitCategory the lockSplitCategory to set
	 */
	public void setLockSplitCategory(ELockSplitCategory lockSplitCategory) {
		this.lockSplitCategory = lockSplitCategory;
	}

	/**
	 * @return the operation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_ope_id", nullable = true)
	public ContractOperation getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(ContractOperation operation) {
		this.operation = operation;
	}
	
}

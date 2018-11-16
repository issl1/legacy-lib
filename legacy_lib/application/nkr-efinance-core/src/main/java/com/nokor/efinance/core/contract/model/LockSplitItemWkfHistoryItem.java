package com.nokor.efinance.core.contract.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nokor.common.app.workflow.model.WkfBaseHistoryItem;

/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "td_lock_split_item_wkf_history_item")
public class LockSplitItemWkfHistoryItem extends WkfBaseHistoryItem  {
	
	/** */
	private static final long serialVersionUID = -2023037331689743916L;

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_his_ite_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	@ManyToOne
	@JoinColumn(name="wkf_his_id", nullable = true)
    @Override
    public LockSplitItemWkfHistory getWkfHistory() {
		return (LockSplitItemWkfHistory) super.getWkfHistory();
	}

}

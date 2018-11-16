package com.nokor.ersys.finance.accounting.workflow;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.nokor.common.app.workflow.model.WkfBaseHistory;

/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "td_transaction_entry_wkf_history")
public class TransactionEntryWkfHistory extends WkfBaseHistory {

	/** */
	private static final long serialVersionUID = -7738881377542546003L;

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_his_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	@SuppressWarnings("unchecked")
	@OneToMany(mappedBy = "wkfHistory", fetch = FetchType.LAZY)
	@Override
	public List<TransactionEntryWkfHistoryItem> getHistItems() {
		return (List<TransactionEntryWkfHistoryItem>) super.getHistItems();
	}

}

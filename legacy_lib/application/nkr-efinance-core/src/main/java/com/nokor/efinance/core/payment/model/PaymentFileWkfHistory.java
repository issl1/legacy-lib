package com.nokor.efinance.core.payment.model;

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
@Table(name = "td_payment_file_wkf_history")
public class PaymentFileWkfHistory extends WkfBaseHistory {
	
	/** */
	private static final long serialVersionUID = 7886810524222361468L;

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
	public List<PaymentFileWkfHistoryItem> getHistItems() {
		return (List<PaymentFileWkfHistoryItem>) super.getHistItems();
	}

}

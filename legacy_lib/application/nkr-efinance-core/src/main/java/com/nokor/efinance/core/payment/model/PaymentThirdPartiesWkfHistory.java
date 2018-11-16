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
@Table(name = "td_payment_third_party_wkf_history")
public class PaymentThirdPartiesWkfHistory extends WkfBaseHistory {

	/** */
	private static final long serialVersionUID = -6850379447128445567L;

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
	public List<PaymentThirdPartiesWkfHistoryItem> getHistItems() {
		return (List<PaymentThirdPartiesWkfHistoryItem>) super.getHistItems();
	}

}

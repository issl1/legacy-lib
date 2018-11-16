package com.nokor.efinance.core.payment.model;

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
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_payment_wkf_history_item")
public class PaymentWkfHistoryItem extends WkfBaseHistoryItem  {
	/** */
	private static final long serialVersionUID = 2346013328098625786L;

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
    public PaymentWkfHistory getWkfHistory() {
		return (PaymentWkfHistory) super.getWkfHistory();
	}

}

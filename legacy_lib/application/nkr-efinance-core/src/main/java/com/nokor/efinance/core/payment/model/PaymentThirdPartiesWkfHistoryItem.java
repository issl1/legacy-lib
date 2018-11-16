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
 * @author uhout.cheng
 */
@Entity
@Table(name = "td_payment_third_party_wkf_history_item")
public class PaymentThirdPartiesWkfHistoryItem extends WkfBaseHistoryItem  {
	
	/** */
	private static final long serialVersionUID = -5042051123061351100L;

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
    public PaymentThirdPartiesWkfHistory getWkfHistory() {
		return (PaymentThirdPartiesWkfHistory) super.getWkfHistory();
	}

}

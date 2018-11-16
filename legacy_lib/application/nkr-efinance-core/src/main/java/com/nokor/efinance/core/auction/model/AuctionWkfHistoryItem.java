package com.nokor.efinance.core.auction.model;

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
@Table(name = "td_auction_wkf_history_item")
public class AuctionWkfHistoryItem extends WkfBaseHistoryItem  {
	/** */
	private static final long serialVersionUID = -5638798542017146706L;

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
    public AuctionWkfHistory getWkfHistory() {
		return (AuctionWkfHistory) super.getWkfHistory();
	}

}

package com.nokor.common.app.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Possibility to historize the fields before switching fromStatus->toStatus in WkfFlowItem.
 * 
 * @author prasnar
 * 
 */
@Entity
@Table(name = "td_wkf_history_item")
public class WkfHistoryItem extends WkfBaseHistoryItem {
    /** */
	private static final long serialVersionUID = 1967252486028977430L;

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
    public WkfHistory getWkfHistory() {
		return (WkfHistory) super.getWkfHistory();
	}
    
}

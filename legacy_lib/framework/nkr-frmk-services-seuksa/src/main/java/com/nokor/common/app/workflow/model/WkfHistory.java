package com.nokor.common.app.workflow.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * History of statuses amended (fromStatus/toStatus) of a given WkfFlow.
 * Possibility to historize the fields before switching fromStatus->toStatus in WkfFlowItem.
 * 
 * @author prasnar
 * 
 */
@Entity
@Table(name = "td_wkf_history")
public class WkfHistory extends WkfBaseHistory {
	/** */
	private static final long serialVersionUID = -8343184327312734046L;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_his_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	@OneToMany(mappedBy = "wkfHistory", fetch = FetchType.LAZY)
	@Override
	public List<WkfHistoryItem> getHistItems() {
		return (List<WkfHistoryItem>) super.getHistItems();
	}
	
	@OneToMany(mappedBy = "wkfHistory", fetch = FetchType.LAZY)
	@Override
	public List<WkfTempItem> getTempItems() {
		return (List<WkfTempItem>) super.getTempItems();
	}

}

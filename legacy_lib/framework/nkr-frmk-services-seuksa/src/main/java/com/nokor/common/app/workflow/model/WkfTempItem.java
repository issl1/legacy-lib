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
@Table(name = "td_wkf_temp_item")
public class WkfTempItem extends WkfBaseTempItem {
	/** */
	private static final long serialVersionUID = -6738155796613441822L;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_tmp_ite_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    @ManyToOne
	@JoinColumn(name="wkf_tmp_id", nullable = true)
    @Override
    public WkfHistory getWkfHistory() {
		return (WkfHistory) super.getWkfHistory();
	}
    
}

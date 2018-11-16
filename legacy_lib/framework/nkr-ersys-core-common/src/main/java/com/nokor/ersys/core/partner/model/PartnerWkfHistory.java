package com.nokor.ersys.core.partner.model;

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
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_partner_wkf_history")
public class PartnerWkfHistory extends WkfBaseHistory {
	/** */
	private static final long serialVersionUID = -3655865500087003895L;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_his_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	@OneToMany(mappedBy = "wkfHistory", fetch = FetchType.LAZY)
	@Override
	public List<PartnerWkfHistoryItem> getHistItems() {
		return (List<PartnerWkfHistoryItem>) super.getHistItems();
	}

}

package com.nokor.ersys.core.hr.model.organization;

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
@Table(name = "td_organization_wkf_history")
public class OrganizationWkfHistory extends WkfBaseHistory {
	/** */
	private static final long serialVersionUID = -3771210023747688129L;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_his_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	@OneToMany(mappedBy = "wkfHistory", fetch = FetchType.LAZY)
	@Override
	public List<OrganizationWkfHistoryItem> getHistItems() {
		return (List<OrganizationWkfHistoryItem>) super.getHistItems();
	}

}

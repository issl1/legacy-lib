package com.nokor.ersys.core.hr.model.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nokor.common.app.workflow.model.WkfBaseTempItem;

/**
 * 
 * @author phirun.kong
 *
 */
@Entity
@Table(name = "td_employee_wkf_temp_item")
public class EmployeeWkfTempItem extends WkfBaseTempItem  {
	/** */
	private static final long serialVersionUID = 244854823032784660L;

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
    public EmployeeWkfHistory getWkfHistory() {
		return (EmployeeWkfHistory) super.getWkfHistory();
	}

}

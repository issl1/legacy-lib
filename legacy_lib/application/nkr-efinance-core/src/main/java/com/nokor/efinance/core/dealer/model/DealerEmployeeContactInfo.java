package com.nokor.efinance.core.dealer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.ersys.core.hr.model.organization.BaseContactInfo;

/**
 * @author bunlong.taing
 */
@Entity
@Table(name = "td_dealer_employee_contact_info")
public class DealerEmployeeContactInfo extends BaseContactInfo {
	/** */
	private static final long serialVersionUID = 5959504820818929836L;
	
	private DealerEmployee dealerEmployee;
	
	/**
	 * @return
	 */
	public static DealerEmployeeContactInfo createInstance() {
		DealerEmployeeContactInfo contactInfo = EntityFactory.createInstance(DealerEmployeeContactInfo.class);
        return contactInfo;
    }

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_emp_cnt_inf_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @return the dealerEmployee
	 */
	@ManyToOne
	@JoinColumn(name="dea_emp_id", nullable = false)
	public DealerEmployee getDealerEmployee() {
		return dealerEmployee;
	}

	/**
	 * @param dealerEmployee the dealerEmployee to set
	 */
	public void setDealerEmployee(DealerEmployee dealerEmployee) {
		this.dealerEmployee = dealerEmployee;
	}

}

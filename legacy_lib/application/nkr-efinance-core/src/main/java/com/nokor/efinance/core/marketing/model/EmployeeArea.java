package com.nokor.efinance.core.marketing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.ersys.core.hr.model.organization.Employee;

/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "tu_employee_area")
public class EmployeeArea extends EntityA {	
	
	private Employee employee;
	private Area area;

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_area_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the employee
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = true)
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * @return the area
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "are_id", nullable = true)
	public Area getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}

}

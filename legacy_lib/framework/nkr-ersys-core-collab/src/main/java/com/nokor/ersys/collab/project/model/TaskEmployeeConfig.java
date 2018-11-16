package com.nokor.ersys.collab.project.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.organization.Employee;

/**
 * 
 * @author youhort.ly
 *
 */
@Entity
@Table(name = "tu_task_employee_config")
public class TaskEmployeeConfig extends EntityA implements MTaskEmployeeConfig {
	/** */
	private static final long serialVersionUID = -5045559914591113827L;

	private double weight;
	private double maxNumberTasks;
	private ETimeUnit unit;
	private Employee employee;
	
    /**
     * Create Instance
     * @return
     */
    public static TaskEmployeeConfig createInstance() {
    	return EntityFactory.createInstance(TaskEmployeeConfig.class);
	}

	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tas_emp_cfg_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}    
    
	/**
	 * @return the weight
	 */
    @Column(name = "tas_emp_cfg_weight", nullable = true)
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}


	/**
	 * @return the maxNumberTasks
	 */
    @Column(name = "tas_emp_cfg_max_number_task", nullable = true)
	public double getMaxNumberTasks() {
		return maxNumberTasks;
	}

	/**
	 * @param maxNumberTasks the maxNumberTasks to set
	 */
	public void setMaxNumberTasks(double maxNumberTasks) {
		this.maxNumberTasks = maxNumberTasks;
	}


	/**
	 * @return the unit
	 */
	@Column(name = "tim_uni_id", nullable = false)
    @Convert(converter = ETimeUnit.class)
	public ETimeUnit getUnit() {
		return unit;
	}


	/**
	 * @param unit the unit to set
	 */
	public void setUnit(ETimeUnit unit) {
		this.unit = unit;
	}


	/**
	 * @return the employee
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="emp_id", nullable = true)
	public Employee getEmployee() {
		return employee;
	}


	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}	
}

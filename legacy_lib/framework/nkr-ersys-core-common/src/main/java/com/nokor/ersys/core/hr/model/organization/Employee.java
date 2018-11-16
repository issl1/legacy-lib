package com.nokor.ersys.core.hr.model.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.IVersion;

import com.nokor.ersys.core.hr.model.address.Address;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_employee")
public class Employee extends Staff implements MEmployee, IVersion {
	/** */
	private static final long serialVersionUID = 5359689117110682174L;
	
	private Organization organization;
	private OrgStructure branch;
	private OrgStructure department;

	/**
     * 
     * @return
     */
    public static Employee createInstance() {
        Employee emp = EntityFactory.createInstance(Employee.class);
        emp.setAddress(Address.createInstance());
        emp.setCascadeAtCreation();
        return emp;
    }
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = true)
	public Organization getOrganization() {
		return organization;
	}

    /**
	 * 
	 * @param company
	 */
	public void setOrganization(Organization company) {
		this.organization = company;
	}
	
	/**
	 * @return the branch
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_str_id", nullable = true)
	public OrgStructure getBranch() {
		return branch;
	}

	/**
	 * @param branch the branch to set
	 */
	public void setBranch(OrgStructure branch) {
		this.branch = branch;
	}

	/**
	 * @return the department
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_str_id_dept", nullable = true)
	public OrgStructure getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(OrgStructure department) {
		this.department = department;
	}

	/**
	 * 
	 */
	@Override
	public void setCascadeAtCreation() {
		addCascadeAtCreation(getAddress());
	}

}

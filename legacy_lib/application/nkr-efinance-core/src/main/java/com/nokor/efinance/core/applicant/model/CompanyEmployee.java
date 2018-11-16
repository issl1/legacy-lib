package com.nokor.efinance.core.applicant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.IVersion;

import com.nokor.ersys.core.hr.model.organization.Staff;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="tu_company_employee")
public class CompanyEmployee extends Staff implements IVersion {
	
	/** */
	private static final long serialVersionUID = 5359689117110682174L;
	
	private Company company;
	

	/**
     * 
     * @return
     */
    public static CompanyEmployee createInstance() {
        CompanyEmployee emp = EntityFactory.createInstance(CompanyEmployee.class);
        return emp;
    }
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_emp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "com_id")
 	public Company getCompany() {
		return company;
	}

    /**
	 * 
	 * @param company
	 */
	public void setCompany(Company company) {
		this.company = company;
	}
	
	@Override
	@Transient
	public boolean isWkfEnabled() {
		return false;
	}
}


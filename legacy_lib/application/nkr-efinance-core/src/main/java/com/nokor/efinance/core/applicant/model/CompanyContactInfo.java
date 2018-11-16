package com.nokor.efinance.core.applicant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.organization.ContactInfo;


/**
 * @author poevminea.sann
 * @created 10 Feb 2016
 */
@Entity
@Table(name = "td_company_contact_info")
public class CompanyContactInfo extends EntityA {

	private static final long serialVersionUID = 9116233885593641677L;
	
	private Company company;
	private ContactInfo contactInfo;
	
	/**
	 * 
	 * @return
	 */
	public static CompanyContactInfo createInstance() {
		CompanyContactInfo contactInfo = EntityFactory.createInstance(CompanyContactInfo.class);
        return contactInfo;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ind_cnt_inf_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    /**
	 * @return the contactInfo
	 */
    @ManyToOne
    @JoinColumn(name="cnt_inf_id", nullable = false)
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	/**
	 * @param contactInfo the contactInfo to set
	 */
	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}
	
	
	/**
	 * @return the company
	 */
	@ManyToOne
	@JoinColumn(name="com_id", nullable = false)
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
	}	
}

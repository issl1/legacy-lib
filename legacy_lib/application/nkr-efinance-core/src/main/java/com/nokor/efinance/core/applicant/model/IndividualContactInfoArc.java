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
 * @author youhort.ly
 *
 */
@Entity
@Table(name = "td_individual_contact_info_arc")
public class IndividualContactInfoArc extends EntityA {

	private static final long serialVersionUID = 9116233885593641677L;
	
	private IndividualArc individual;
	private ContactInfo contactInfo;
	
	/**
	 * 
	 * @return
	 */
	public static IndividualContactInfoArc createInstance() {
		IndividualContactInfoArc contactInfo = EntityFactory.createInstance(IndividualContactInfoArc.class);
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
	 * @return the individual
	 */
	@ManyToOne
	@JoinColumn(name="ind_id", nullable = false)
	public IndividualArc getIndividual() {
		return individual;
	}

	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(IndividualArc individual) {
		this.individual = individual;
	}	
}

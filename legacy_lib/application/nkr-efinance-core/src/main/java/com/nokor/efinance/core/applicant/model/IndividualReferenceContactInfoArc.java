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
@Table(name = "td_individual_reference_contact_info_arc")
public class IndividualReferenceContactInfoArc extends EntityA {

	private static final long serialVersionUID = -3681622139661971430L;
	
	private IndividualReferenceInfoArc individualReferenceInfo;
	private ContactInfo contactInfo;
	
	/**
	 * 
	 * @return
	 */
	public static IndividualReferenceContactInfoArc createInstance() {
		IndividualReferenceContactInfoArc contactInfo = EntityFactory.createInstance(IndividualReferenceContactInfoArc.class);
        return contactInfo;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ind_ref_cnt_inf_id", unique = true, nullable = false)
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
	 * @return the individualReferenceInfo
	 */
	@ManyToOne
	@JoinColumn(name="ind_ref_inf_id", nullable = false)	
	public IndividualReferenceInfoArc getIndividualReferenceInfo() {
		return individualReferenceInfo;
	}

	/**
	 * @param individualReferenceInfo the individualReferenceInfo to set
	 */
	public void setIndividualReferenceInfo(
			IndividualReferenceInfoArc individualReferenceInfo) {
		this.individualReferenceInfo = individualReferenceInfo;
	}	
}

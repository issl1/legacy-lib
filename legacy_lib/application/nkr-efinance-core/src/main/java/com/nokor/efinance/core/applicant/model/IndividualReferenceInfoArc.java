package com.nokor.efinance.core.applicant.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.eref.ERelationship;

/**
 * @author youhort.ly
 *
 */
@Entity
@Table(name = "td_individual_reference_info_arc")
public class IndividualReferenceInfoArc extends EntityA {

	private static final long serialVersionUID = -3373781532200327701L;
	
	private IndividualArc individual;
	private EIndividualReferenceType referenceType;
	private ERelationship relationship;
	private String lastNameEn;
	private String firstNameEn;
	private List<IndividualReferenceContactInfoArc> individualReferenceContactInfos;
	
	/**
	 * 
	 * @return
	 */
	public static IndividualReferenceInfoArc createInstance() {
		IndividualReferenceInfoArc contactInfo = EntityFactory.createInstance(IndividualReferenceInfoArc.class);
        return contactInfo;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ind_ref_inf_id", unique = true, nullable = false)
	public Long getId() {
		return id;
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

	/**
	 * @return the relationship
	 */
	@Column(name = "rel_id", nullable = true)
    @Convert(converter = ERelationship.class)
	public ERelationship getRelationship() {
		return relationship;
	}

	/**
	 * @param relationship the relationship to set
	 */
	public void setRelationship(ERelationship relationship) {
		this.relationship = relationship;
	}

	/**
	 * @return the lastNameEn
	 */
	@Column(name = "ind_ref_inf_lastname", nullable = true, length = 100)
	public String getLastNameEn() {
		return lastNameEn;
	}

	/**
	 * @param lastNameEn the lastNameEn to set
	 */
	public void setLastNameEn(String lastNameEn) {
		this.lastNameEn = lastNameEn;
	}

	/**
	 * @return the firstNameEn
	 */
	@Column(name = "ind_ref_inf_firstname", nullable = true, length = 100)
	public String getFirstNameEn() {
		return firstNameEn;
	}

	/**
	 * @param firstNameEn the firstNameEn to set
	 */
	public void setFirstNameEn(String firstNameEn) {
		this.firstNameEn = firstNameEn;
	}	
	
	/**
	 * @return the referenceType
	 */
	@Column(name = "ind_ref_typ_id", nullable = true)
    @Convert(converter = EIndividualReferenceType.class)
	public EIndividualReferenceType getReferenceType() {
		return referenceType;
	}

	/**
	 * @param referenceType the referenceType to set
	 */
	public void setReferenceType(EIndividualReferenceType referenceType) {
		this.referenceType = referenceType;
	}
	
	/**
	 * @return the individualReferenceContactInfos
	 */
	@OneToMany(mappedBy="individualReferenceInfo", fetch = FetchType.LAZY)
	public List<IndividualReferenceContactInfoArc> getIndividualReferenceContactInfos() {
		return individualReferenceContactInfos;
	}

	/**
	 * @param individualReferenceContactInfos the individualReferenceContactInfos to set
	 */
	public void setIndividualReferenceContactInfos(
			List<IndividualReferenceContactInfoArc> individualReferenceContactInfos) {
		this.individualReferenceContactInfos = individualReferenceContactInfos;
	}	
}

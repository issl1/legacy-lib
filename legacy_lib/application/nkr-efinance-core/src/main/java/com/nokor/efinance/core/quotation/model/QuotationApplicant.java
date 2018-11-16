package com.nokor.efinance.core.quotation.model;

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

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.ersys.core.hr.model.eref.ERelationship;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_quotation_applicant")
public class QuotationApplicant extends EntityA {

	private static final long serialVersionUID = 3774511918627834154L;

	private Applicant applicant;
	private Quotation quotation;
	private EApplicantType applicantType;
	private ERelationship relationship;
	private boolean sameApplicantAddress;
	
	/**
     * Get quotation applicant's is.
     * @return The quotation applicant's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quo_app_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the applicant
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
	public Applicant getApplicant() {
		return applicant;
	}

	/**
	 * @param applicant the applicant to set
	 */
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	/**
	 * @return the quotation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quo_id")
	public Quotation getQuotation() {
		return quotation;
	}

	/**
	 * @param quotation the quotation to set
	 */
	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	/**
	 * @return the applicantType
	 */
    @Column(name = "app_typ_id", nullable = false)
    @Convert(converter = EApplicantType.class)
	public EApplicantType getApplicantType() {
		return applicantType;
	}

	/**
	 * @param applicantType the applicantType to set
	 */
	public void setApplicantType(EApplicantType applicantType) {
		this.applicantType = applicantType;
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
	 * @return the sameApplicantAddress
	 */
	@Column(name = "quo_app_bl_same_applicant_address", nullable = true)
	public boolean isSameApplicantAddress() {
		return sameApplicantAddress;
	}

	/**
	 * @param sameApplicantAddress the sameApplicantAddress to set
	 */
	public void setSameApplicantAddress(boolean sameApplicantAddress) {
		this.sameApplicantAddress = sameApplicantAddress;
	}
	
}

package com.nokor.efinance.core.document.model;

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

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.efinance.core.applicant.model.EApplicantType;

/**
 * Document
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_document")
public class Document extends EntityRefA {

	private static final long serialVersionUID = 2057497814590826522L;
	
	private boolean mandatory;
	private Integer numGroup;
	private boolean referenceRequired;
	private boolean issueDateRequired;
	private boolean expireDateRequired;
	private boolean submitCreditBureau;
	private boolean fieldCheck;
	private boolean allowUpdateChangeAsset;

	private EDocumentState documentState;
	
	private EApplicantType applicantType;
	private DocumentGroup documentGroup;
	
	private List<DocumentScoring> documentsScoring;
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "doc_code", nullable = false, length = 10, unique = true)
	@Override
	public String getCode() {
		return super.getCode();
	}


	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "doc_desc", nullable = true, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
     * Get the document's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "doc_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }
    
	/**
	 * @return the mandatory
	 */
    @Column(name = "doc_bl_mandatory", nullable = true, columnDefinition = "boolean default true")
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * @return the numGroup
	 */
	@Column(name = "doc_nu_num_group", nullable = true)
	public Integer getNumGroup() {
		return numGroup;
	}

	/**
	 * @param numGroup the numGroup to set
	 */
	public void setNumGroup(Integer numGroup) {
		this.numGroup = numGroup;
	}
	
	/**
	 * @return the referenceRequired
	 */
	@Column(name = "doc_bl_reference_required", nullable = true, columnDefinition = "boolean default true")
	public boolean isReferenceRequired() {
		return referenceRequired;
	}

	/**
	 * @param referenceRequired the referenceRequired to set
	 */
	public void setReferenceRequired(boolean referenceRequired) {
		this.referenceRequired = referenceRequired;
	}
	

	/**
	 * @return the issueDateRequired
	 */
	@Column(name = "doc_bl_issue_date_required", nullable = true, columnDefinition = "boolean default true")
	public boolean isIssueDateRequired() {
		return issueDateRequired;
	}

	/**
	 * @param issueDateRequired the issueDateRequired to set
	 */
	public void setIssueDateRequired(boolean issueDateRequired) {
		this.issueDateRequired = issueDateRequired;
	}

	/**
	 * @return the expireDateRequired
	 */
	@Column(name = "doc_bl_expire_date_required", nullable = true, columnDefinition = "boolean default true")
	public boolean isExpireDateRequired() {
		return expireDateRequired;
	}

	/**
	 * @param expireDateRequired the expireDateRequired to set
	 */
	public void setExpireDateRequired(boolean expireDateRequired) {
		this.expireDateRequired = expireDateRequired;
	}
	
	/**
	 * @return the submitCreditBureau
	 */
	@Column(name = "doc_bl_submit_credit_bureau", nullable = true, columnDefinition = "boolean default true")
	public boolean isSubmitCreditBureau() {
		return submitCreditBureau;
	}

	/**
	 * @param submitCreditBureau the submitCreditBureau to set
	 */
	public void setSubmitCreditBureau(boolean submitCreditBureau) {
		this.submitCreditBureau = submitCreditBureau;
	}

	
	/**
	 * @return the fieldCheck
	 */
	@Column(name = "doc_bl_field_check", nullable = true, columnDefinition = "boolean default true")
	public boolean isFieldCheck() {
		return fieldCheck;
	}

	/**
	 * @param fieldCheck the fieldCheck to set
	 */
	public void setFieldCheck(boolean fieldCheck) {
		this.fieldCheck = fieldCheck;
	}

	/**
	 * @return the allowUpdateChangeAsset
	 */
	@Column(name = "doc_bl_allow_change_update_asset", nullable = true, columnDefinition = "boolean default true")
	public boolean isAllowUpdateChangeAsset() {
		return allowUpdateChangeAsset;
	}

	/**
	 * @param allowUpdateChangeAsset the allowUpdateChangeAsset to set
	 */
	public void setAllowUpdateChangeAsset(boolean allowUpdateChangeAsset) {
		this.allowUpdateChangeAsset = allowUpdateChangeAsset;
	}
	
	/**
	 * @return the documentState
	 */
    @Column(name = "doc_sta_id", nullable = true)
    @Convert(converter = EDocumentState.class)
	public EDocumentState getDocumentState() {
		return documentState;
	}

	/**
	 * @param documentState the documentState to set
	 */
	public void setDocumentState(EDocumentState documentState) {
		this.documentState = documentState;
	}
	
	/**
	 * @return the applicantType
	 */
    @Column(name = "app_typ_id", nullable = true)
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
	 * @return the documentGroup
	 */
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dogrp_id")
	public DocumentGroup getDocumentGroup() {
		return documentGroup;
	}

	/**
	 * @param documentGroup the documentGroup to set
	 */
	public void setDocumentGroup(DocumentGroup documentGroup) {
		this.documentGroup = documentGroup;
	}

	/**
	 * @return the documentsScoring
	 */
	@OneToMany(mappedBy="document", fetch = FetchType.LAZY)
	public List<DocumentScoring> getDocumentsScoring() {
		return documentsScoring;
	}

	/**
	 * @param documentsScoring the documentsScoring to set
	 */
	public void setDocumentsScoring(List<DocumentScoring> documentsScoring) {
		this.documentsScoring = documentsScoring;
	}
	
}

package com.nokor.efinance.core.document.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.efinance.core.applicant.model.EApplicantType;

/**
 * Document Group
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_document_uw_group")
public class DocumentUwGroup extends EntityRefA {
	
	private static final long serialVersionUID = -5023898084478773192L;

	private EApplicantType applicantType;
	
	private List<DocumentScoring> documentsScoring;
	private List<DocumentConfirmEvidence> documentsConfirmEvidence;
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_uw_grp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
    @Column(name = "doc_uw_grp_code", nullable = false, length = 10, unique = true)
	@Override
	public String getCode() {
		return super.getCode();
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "doc_uw_grp_desc", nullable = false, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
     * @return <String>
     */
    @Override
    @Column(name = "doc_uw_grp_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
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
	 * @return the documentsScoring
	 */
    @OneToMany(mappedBy="documentUwGroup", fetch = FetchType.LAZY)
	public List<DocumentScoring> getDocumentsScoring() {
		return documentsScoring;
	}

	/**
	 * @param documentsScoring the documentsScoring to set
	 */
	public void setDocumentsScoring(List<DocumentScoring> documentsScoring) {
		this.documentsScoring = documentsScoring;
	}

	/**
	 * @return the documentsConfirmEvidence
	 */
	@OneToMany(mappedBy="documentUwGroup", fetch = FetchType.LAZY)
	public List<DocumentConfirmEvidence> getDocumentsConfirmEvidence() {
		return documentsConfirmEvidence;
	}

	/**
	 * @param documentsConfirmEvidence the documentsConfirmEvidence to set
	 */
	public void setDocumentsConfirmEvidence(
			List<DocumentConfirmEvidence> documentsConfirmEvidence) {
		this.documentsConfirmEvidence = documentsConfirmEvidence;
	}
}

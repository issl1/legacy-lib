package com.nokor.efinance.core.document.model;

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


/**
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_document_confirm_evidence")
public class DocumentConfirmEvidence extends EntityA {

	private static final long serialVersionUID = 2400108137978954273L;
	
	private EConfirmEvidence confirmEvidence;
	private DocumentUwGroup documentUwGroup;
		
	public DocumentConfirmEvidence() {
    }
    
    /**
     * @return The id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_con_evi_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
	/**
	 * @return the document
	 */
    @Column(name = "con_evi_id", nullable = true)
    @Convert(converter = EConfirmEvidence.class)
	public EConfirmEvidence getConfirmEvidence() {
		return confirmEvidence;
	}

	/**
	 * @param confirmEvidence the confirmEvidence to set
	 */
	public void setConfirmEvidence(EConfirmEvidence confirmEvidence) {
		this.confirmEvidence = confirmEvidence;
	}

	/**
	 * @return the documentUwGroup
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_uw_grp_id")
	public DocumentUwGroup getDocumentUwGroup() {
		return documentUwGroup;
	}

	/**
	 * @param documentUwGroup the documentUwGroup to set
	 */
	public void setDocumentUwGroup(DocumentUwGroup documentUwGroup) {
		this.documentUwGroup = documentUwGroup;
	}
}

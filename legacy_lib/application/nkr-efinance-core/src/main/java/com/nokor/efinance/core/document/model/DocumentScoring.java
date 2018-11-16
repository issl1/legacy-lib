package com.nokor.efinance.core.document.model;

import javax.persistence.Column;
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
 * Service Class.
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_document_scoring")
public class DocumentScoring extends EntityA {

	private static final long serialVersionUID = -1394756853227096160L;

	private Document document;
	private DocumentUwGroup documentUwGroup;
	private int score;
	
	public DocumentScoring() {
    }
    
    /**
     * @return The id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_soc_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
	/**
	 * @return the document
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
	public Document getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(Document document) {
		this.document = document;
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

	/**
	 * @return the score
	 */
	@Column(name = "doc_soc_nu_score", nullable = false)
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

}

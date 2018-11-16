package com.nokor.efinance.core.applicant.model;

import java.util.Date;

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

import com.nokor.efinance.core.document.model.Document;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_applicant_document")
public class ApplicantDocument extends EntityA {
	/** */
	private static final long serialVersionUID = 7556450149731798268L;

	private Applicant applicant;
	private Document document;
	private String path;
	private String reference;
	private Date issueDate;
	private Date expireDate;
	private boolean original;
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_doc_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the dealer
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
	 * @return the path
	 */
	@Column(name = "app_doc_va_path", nullable = true, length = 150)
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the reference
	 */
	@Column(name = "app_doc_va_reference", nullable = true, length = 50)
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	/**
	 * @return the issueDate
	 */
	@Column(name = "app_doc_dt_issue", nullable = true)
	public Date getIssueDate() {
		return issueDate;
	}

	/**
	 * @param issueDate the issueDate to set
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * @return the expireDate
	 */
	@Column(name = "app_doc_dt_expire", nullable = true)
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate the expireDate to set
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return the original
	 */
	@Column(name = "app_doc_bl_original", nullable = true)
	public boolean isOriginal() {
		return original;
	}

	/**
	 * @param original the original to set
	 */
	public void setOriginal(boolean original) {
		this.original = original;
	}
}

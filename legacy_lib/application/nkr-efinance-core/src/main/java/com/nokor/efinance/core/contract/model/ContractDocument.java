package com.nokor.efinance.core.contract.model;

import java.util.Date;

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

import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.EDocumentStatus;

/**
 * The Document of the contract
 * @author bunlong.taing
 */
@Entity
@Table(name = "td_contract_document")
public class ContractDocument extends EntityA implements MContractDocument {
	
	private String path;
	private String reference;
	private Contract contract;
	private Document document;
	private Date issueDate;
	private Date expireDate;
	private String comment;
	private EDocumentStatus status;	

	/** */
	private static final long serialVersionUID = 7238849826019960498L;

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_doc_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the path
	 */
    @Column(name = "con_doc_va_path", nullable = true, length = 150)
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
	@Column(name = "con_doc_va_reference", nullable = true, length = 50)
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
	 * @return the contract
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id")
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
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
	 * @return the issueDate
	 */
	@Column(name = "con_doc_dt_issue", nullable = true)
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
	@Column(name = "con_doc_dt_expire", nullable = true)
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
	 * @return the status
	 */
	@Column(name = "doc_sta_id", nullable = true)
    @Convert(converter = EDocumentStatus.class)
	public EDocumentStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(EDocumentStatus status) {
		this.status = status;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "con_doc_va_comment", nullable = true, length = 255)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}	
}

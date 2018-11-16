package com.nokor.efinance.core.quotation.model;

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
 * @author ly.youhort
 */
@Entity
@Table(name = "td_quotation_document")
public class QuotationDocument extends EntityA {

	private static final long serialVersionUID = 2090275270431157444L;
	
	private Document document;
	private Quotation quotation;
	private String path;
	private String reference;
	private Date issueDate;
	private Date expireDate;
	private boolean original;
	private boolean covConfirmationCo;
	private boolean covConfirmationUw;
	private boolean employerConfirmationCo;
	private boolean employerConfirmationUw;
		
	/**
     * Get quotation applicant's is.
     * @return The quotation applicant's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quo_doc_id", unique = true, nullable = false)
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
	 * @return the path
	 */
	@Column(name = "quo_doc_va_path", nullable = true, length = 150)
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
	@Column(name = "quo_doc_va_reference", nullable = true, length = 50)
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
	@Column(name = "quo_doc_dt_issue", nullable = true)
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
	@Column(name = "quo_doc_dt_expire", nullable = true)
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
	@Column(name = "quo_doc_bl_original", nullable = true)
	public boolean isOriginal() {
		return original;
	}

	/**
	 * @param original the original to set
	 */
	public void setOriginal(boolean original) {
		this.original = original;
	}

	/**
	 * @return the covConfirmationCo
	 */
	@Column(name = "quo_doc_bl_cov_confirmation_co", nullable = true)
	public boolean isCovConfirmationCo() {
		return covConfirmationCo;
	}

	/**
	 * @param covConfirmationCo the covConfirmationCo to set
	 */
	public void setCovConfirmationCo(boolean covConfirmationCo) {
		this.covConfirmationCo = covConfirmationCo;
	}

	/**
	 * @return the covConfirmationUw
	 */
	@Column(name = "quo_doc_bl_cov_confirmation_uw", nullable = true)
	public boolean isCovConfirmationUw() {
		return covConfirmationUw;
	}

	/**
	 * @param covConfirmationUw the covConfirmationUw to set
	 */
	public void setCovConfirmationUw(boolean covConfirmationUw) {
		this.covConfirmationUw = covConfirmationUw;
	}

	/**
	 * @return the employerConfirmationCo
	 */
	@Column(name = "quo_doc_bl_employer_confirmation_co", nullable = true)
	public boolean isEmployerConfirmationCo() {
		return employerConfirmationCo;
	}

	/**
	 * @param employerConfirmationCo the employerConfirmationCo to set
	 */
	public void setEmployerConfirmationCo(boolean employerConfirmationCo) {
		this.employerConfirmationCo = employerConfirmationCo;
	}

	/**
	 * @return the employerConfirmationUw
	 */
	@Column(name = "quo_doc_bl_employer_confirmation_uw", nullable = true)
	public boolean isEmployerConfirmationUw() {
		return employerConfirmationUw;
	}

	/**
	 * @param employerConfirmationUw the employerConfirmationUw to set
	 */
	public void setEmployerConfirmationUw(boolean employerConfirmationUw) {
		this.employerConfirmationUw = employerConfirmationUw;
	}
	
}

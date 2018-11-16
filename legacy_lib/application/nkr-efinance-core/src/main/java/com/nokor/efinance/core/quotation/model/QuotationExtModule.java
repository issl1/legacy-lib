package com.nokor.efinance.core.quotation.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.frmk.security.model.SecUser;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_quotation_ext_module")
public class QuotationExtModule extends EntityA {

	private static final long serialVersionUID = 7426353888335603466L;

	private Quotation quotation;
	private EExtModule extModule;
	private Date processDate;
	private Boolean status;
	private String result;
	private SecUser processByUser;
	private String reference;
	private EApplicantType applicantType;
		
	/**
     * Get quotation service's is.
     * @return The quotation service's id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quo_ext_mod_id", unique = true, nullable = false)
    public Long getId() {
        return id;
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
	 * @return the extModule
	 */
    @Column(name = "ex_mod_id", nullable = false)
    @Convert(converter = EExtModule.class)
	public EExtModule getExtModule() {
		return extModule;
	}

	/**
	 * @param extModule the extModule to set
	 */
	public void setExtModule(EExtModule extModule) {
		this.extModule = extModule;
	}
	
	/**
	 * @return the status
	 */
    @Column(name = "quo_ext_mod_sta_id", nullable = false)
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * @return the processDate
	 */
	@Column(name = "quo_ext_mod_dt_process", nullable = true)
	public Date getProcessDate() {
		return processDate;
	}

	/**
	 * @param processDate the processDate to set
	 */
	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	/**
	 * @return the result
	 */
	@Column(name = "quo_ext_mod_cl_result", nullable = true)
	@Lob
	@Type(type="org.hibernate.type.StringType")
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the processByUser
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id")
	public SecUser getProcessByUser() {
		return processByUser;
	}

	/**
	 * @param processByUser the processByUser to set
	 */
	public void setProcessByUser(SecUser processByUser) {
		this.processByUser = processByUser;
	}

	/**
	 * @return the reference
	 */
	@Column(name = "quo_ext_mod_va_reference", nullable = true)
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	
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

}

package com.nokor.ersys.collab.membership.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.finance.billing.model.Bill;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_member_registration")
public class MemberRegistration extends EntityWkf {
    /** */
    private static final long serialVersionUID = -7505433897514664007L;

    private Member member;
    private Date requestDate;
    private Bill invoice;
    private String comment;

    private List<RegistrationDoc> documents;

    /**
     * 
     * @return
     */
    public static MemberRegistration createInstance() {
        MemberRegistration reg = EntityFactory.createInstance(MemberRegistration.class);
        return reg;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_reg_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
     * @return the member
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id", nullable = false)
    public Member getMember() {
        return member;
    }

    /**
     * @param member the member to set
     */
    public void setMember(final Member member) {
        this.member = member;
    }

  
    /**
	 * @return the invoice
	 */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inv_id", nullable = true)
	public Bill getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice to set
	 */
	public void setInvoice(Bill invoice) {
		this.invoice = invoice;
	}

	/**
     * @return the requestDate
     */
    @Column(name = "mem_reg_request_date", nullable = false)
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate the requestDate to set
     */
    public void setRequestDate(final Date requestDate) {
        this.requestDate = requestDate;
    }

      /**
     * @return the comment
     */
    @Column(name = "mem_reg_comment", nullable = true)
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

	/**
	 * @return the documents
	 */
	@OneToMany(mappedBy = "registration", fetch = FetchType.LAZY)
	public List<RegistrationDoc> getDocuments() {
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(List<RegistrationDoc> documents) {
		this.documents = documents;
	}

	
}

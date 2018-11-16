package com.nokor.ersys.collab.membership.model;

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
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_document_type_member")
public class RegistrationDocTypeMember extends EntityA {
	/** */
	private static final long serialVersionUID = 3261517490236893021L;
	
	private RegistrationDocType docType;
	private MemberType memberType;
	private Boolean isMandatory;

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_typ_mem_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the docType
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_typ_id", nullable = false)
	public RegistrationDocType getDocType() {
		return docType;
	}

	/**
	 * @param docType the docType to set
	 */
	public void setDocType(RegistrationDocType docType) {
		this.docType = docType;
	}

	/**
	 * @return the memberType
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_typ_id", nullable = false)
	public MemberType getMemberType() {
		return memberType;
	}

	/**
	 * @param memberType the memberType to set
	 */
	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	/**
	 * @return the isMandatory
	 */
    @Column(name = "doc_typ_mem_is_mandatory", nullable = false)
	public Boolean getIsMandatory() {
		return isMandatory;
	}

	/**
	 * @param isMandatory the isMandatory to set
	 */
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

   
}

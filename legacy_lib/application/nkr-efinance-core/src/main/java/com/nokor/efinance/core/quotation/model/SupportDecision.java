package com.nokor.efinance.core.quotation.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.common.app.workflow.model.EWkfStatus;


/**
 * Color
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_support_decision")
public class SupportDecision extends EntityRefA {
	
	private static final long serialVersionUID = -3437583780703636039L;

	private EWkfStatus quotationStatus;
	private boolean mandatory;
	private boolean commentRequired;

	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sup_dec_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
	@Override
	@Column(name = "sup_dec_code", nullable = false, length = 10, unique = true)
	public String getCode() {
		return super.getCode();
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "sup_dec_desc", nullable = false, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
     * Get the asset financial product's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "sup_dec_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the quotationStatus
	 */
    @Column(name = "quo_sta_id", nullable = false)
    @Convert(converter = EWkfStatus.class)
	public EWkfStatus getWkfStatus() {
		return quotationStatus;
	}

	/**
	 * @param quotationStatus the quotationStatus to set
	 */
	public void setWkfStatus(EWkfStatus quotationStatus) {
		this.quotationStatus = quotationStatus;
	}
	
	/**
	 * @return the mandatory
	 */
	@Column(name = "sup_dec_bl_mandatory", nullable = true)
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
	 * @return the commentRequired
	 */
	@Column(name = "sup_dec_bl_comment_required", nullable = true, columnDefinition = "boolean default false")
	public boolean isCommentRequired() {
		return commentRequired;
	}

	/**
	 * @param commentRequired the commentRequired to set
	 */
	public void setCommentRequired(boolean commentRequired) {
		this.commentRequired = commentRequired;
	}
    
	
}

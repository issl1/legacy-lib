package com.nokor.efinance.core.issue.model;

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

import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "td_contract_issue")
public class ContractIssue extends EntityA implements MContractIssue {

	/** */
	private static final long serialVersionUID = -4598020207547996369L;
	
	private Contract contract;
	private EIssueType issueType;
	private EIssueAttribute issueAttribute;
	private EIssueDocument issueDocument1;
	private EIssueDocument issueDocument2;
	private String comment;
	
	private String remark;
	private Date dateFixed;	
	private boolean fixed;
	private boolean forced;

	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_isu_id", unique = true, nullable = false)
	public Long getId() {
		return id;
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
	 * @return the issueType
	 */
	@Column(name = "isu_typ_id", nullable = true)
	@Convert(converter = EIssueType.class)
	public EIssueType getIssueType() {
		return issueType;
	}

	/**
	 * @param issueType the issueType to set
	 */
	public void setIssueType(EIssueType issueType) {
		this.issueType = issueType;
	}

	/**
	 * @return the issueAttribute
	 */
	@Column(name = "isu_att_id", nullable = true)
	@Convert(converter = EIssueAttribute.class)
	public EIssueAttribute getIssueAttribute() {
		return issueAttribute;
	}

	/**
	 * @param issueAttribute the issueAttribute to set
	 */
	public void setIssueAttribute(EIssueAttribute issueAttribute) {
		this.issueAttribute = issueAttribute;
	}

	/**
	 * @return the issueDocument1
	 */
	@Column(name = "isu_doc1_id", nullable = true)
	@Convert(converter = EIssueDocument.class)
	public EIssueDocument getIssueDocument1() {
		return issueDocument1;
	}

	/**
	 * @param issueDocument1 the issueDocument1 to set
	 */
	public void setIssueDocument1(EIssueDocument issueDocument1) {
		this.issueDocument1 = issueDocument1;
	}

	/**
	 * @return the issueDocument2
	 */
	@Column(name = "isu_doc2_id", nullable = true)
	@Convert(converter = EIssueDocument.class)
	public EIssueDocument getIssueDocument2() {
		return issueDocument2;
	}

	/**
	 * @param issueDocument2 the issueDocument2 to set
	 */
	
	public void setIssueDocument2(EIssueDocument issueDocument2) {
		this.issueDocument2 = issueDocument2;
	}

	/**
	 * @return the comment
	 */
	 @Column(name = "con_isu_comment", nullable = true)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the remark
	 */
	 @Column(name = "con_isu_remark", nullable = true)
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the dateFixed
	 */
	@Column(name = "con_isu_date_fixed", nullable = true)
	public Date getDateFixed() {
		return dateFixed;
	}

	/**
	 * @param dateFixed the dateFixed to set
	 */
	public void setDateFixed(Date dateFixed) {
		this.dateFixed = dateFixed;
	}

	/**
	 * @return the fixed
	 */
	@Column(name = "con_isu_bl_fixed", nullable = true, columnDefinition = "boolean default false")
	public boolean isFixed() {
		return fixed;
	}

	/**
	 * @param fixed the fixed to set
	 */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	/**
	 * @return the forced
	 */
	@Column(name = "con_isu_bl_forced", nullable = true, columnDefinition = "boolean default false")
	public boolean isForced() {
		return forced;
	}

	/**
	 * @param forced the forced to set
	 */
	public void setForced(boolean forced) {
		this.forced = forced;
	}	
}

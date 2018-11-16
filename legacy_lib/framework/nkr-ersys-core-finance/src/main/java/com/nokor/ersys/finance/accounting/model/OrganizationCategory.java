package com.nokor.ersys.finance.accounting.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_account_org_category")
public class OrganizationCategory extends EntityWkf {
	/** */
	private static final long serialVersionUID = 6139271816120300493L;

	private OrgStructure orgStructure;
	private AccountCategory category;
	private Date startDate;
	private Date endDate;
	private String comment;
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_acc_cat_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the category
	 */
    @ManyToOne
    @JoinColumn(name="acc_cat_id", nullable=false)
	public AccountCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(AccountCategory category) {
		this.category = category;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "org_acc_cat_comment", nullable = true)
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
	 * @return the orgStructure
	 */
    @ManyToOne
    @JoinColumn(name="org_str_id", nullable = false)
	public OrgStructure getOrgStructure() {
		return orgStructure;
	}

	/**
	 * @param orgStructure the orgStructure to set
	 */
	public void setOrgStructure(OrgStructure orgStructure) {
		this.orgStructure = orgStructure;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "org_acc_cat_start_date", nullable = true)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(name = "org_acc_cat_end_date", nullable = true)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

    
}

package com.nokor.common.app.content.model.base;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.nokor.ersys.core.hr.model.organization.BaseOrganization;
import com.nokor.ersys.core.hr.model.organization.BasePerson;

/**
 * A content can have several Contributors
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class AbstractContributor extends BasePerson {
	/** */
	private static final long serialVersionUID = 3306347747002324608L;

	private ContributorType type;
	private String comment;
    private Integer sortIndex;

	/**
	 * @return the company
	 */
    @Transient
	public abstract BaseOrganization getCompany();


	/**
	 * @return the type
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="con_typ_id", nullable = false)
	public ContributorType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ContributorType type) {
		this.type = type;
	}
	
	/**
	 * @return the comment
	 */
	@Column(name = "con_comment", nullable = true)
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
	 * @return the sortIndex
	 */
	@Column(name = "sort_index", nullable = false)
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}
			
}
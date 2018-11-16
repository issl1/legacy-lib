package com.nokor.common.app.content.model.base;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * A content can have several classifications
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class AbstractClassification extends EntityA {
	/** */
	private static final long serialVersionUID = -5443328779144506851L;
	
	private Classification classification;
    private Integer sortIndex;


	/**
	 * @return the classification
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cla_id", nullable = false)
	public Classification getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(Classification classification) {
		this.classification = classification;
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

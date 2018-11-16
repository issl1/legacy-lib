package com.nokor.common.app.content.model.base;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.content.model.file.FileData;


/**
 * A content can have several attachments
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class AbstractAttachment extends EntityA {
	/** */
	private static final long serialVersionUID = 7147740520260206628L;

	private FileData file;
    private Boolean isVisible;

    private Integer sortIndex;


	/**
	 * @return the file
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fil_id", nullable = false)
	public FileData getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(FileData file) {
		this.file = file;
	}

	/**
	 * @return the isVisible
	 */
	@Column(name = "att_is_visible", nullable = false)
	public Boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible the isVisible to set
	 */
	public void setVisible(Boolean isVisible) {
		this.isVisible = isVisible;
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

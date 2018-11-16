package com.nokor.efinance.core.document.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

@Entity
@Table(name = "tu_document_template")
public class DocumentTemplate extends EntityRefA {

	/** */
	private static final long serialVersionUID = 3564036521779184444L;
	
	private String document;

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_tpl_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "doc_tpl_code", nullable = false, length = 10)
	@Override
	public String getCode() {
		return super.getCode();
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "doc_tpl_desc", nullable = true, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
    @Override
    @Column(name = "doc_tpl_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the document
	 */
    @Column(name = "doc_tpl_document", nullable = false)
	public String getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(String document) {
		this.document = document;
	}

}

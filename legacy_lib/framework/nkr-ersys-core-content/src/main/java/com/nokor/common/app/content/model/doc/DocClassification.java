package com.nokor.common.app.content.model.doc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nokor.common.app.content.model.base.AbstractClassification;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_cms_doc_classification")
public class DocClassification extends AbstractClassification {
	/** */
	private static final long serialVersionUID = 2980529204406298529L;

	private DocContent document;

	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doc_cla_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the document
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="doc_id", nullable = false)
	public DocContent getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(DocContent document) {
		this.document = document;
	}

	
}

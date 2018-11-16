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

import com.nokor.common.app.content.model.base.AbstractAttachment;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_cms_doc_attachment")
public class DocAttachment extends AbstractAttachment {
	/** */
	private static final long serialVersionUID = 4942057081142143061L;

	private DocContent document;

	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doc_att_id", unique = true, nullable = false)
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

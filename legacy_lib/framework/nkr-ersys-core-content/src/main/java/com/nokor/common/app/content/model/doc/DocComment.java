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

import com.nokor.common.app.content.model.base.AbstractContent;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_cms_doc_comment")
public class DocComment extends AbstractContent {
	/** */
	private static final long serialVersionUID = 8354569267890088364L;

	private DocContent doc;

	/**
     * 
     * @return
     */
	public static DocComment createInstance() {
		DocComment annotation = new DocComment();
		return annotation;
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the doc
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="doc_id", nullable = false)
	public DocContent getDoc() {
		return doc;
	}

	/**
	 * @param doc the doc to set
	 */
	public void setDoc(DocContent doc) {
		this.doc = doc;
	}
	
	
	
}

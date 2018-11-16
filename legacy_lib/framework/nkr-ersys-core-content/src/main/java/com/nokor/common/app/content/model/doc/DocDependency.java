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

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.content.model.base.AbstractDependency;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_cms_doc_dependency")
public class DocDependency extends AbstractDependency {
	/** */
	private static final long serialVersionUID = -4901518845400575248L;

	private DocContent doc;
	private DocContent other;


   /**
    * 
    * @return
    */
	public static DocDependency createInstance() {
		DocDependency docDep = EntityFactory.createInstance(DocDependency.class);
        return docDep;
	}
	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doc_dep_id", unique = true, nullable = false)
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

	/**
	 * @return the other
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="other_doc_id", nullable = false)
	public DocContent getOther() {
		return other;
	}

	/**
	 * @param other the other to set
	 */
	public void setOther(DocContent other) {
		this.other = other;
	}


}

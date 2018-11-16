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
import javax.persistence.Transient;

import com.nokor.common.app.content.model.base.AbstractContributor;
import com.nokor.ersys.core.hr.model.organization.BaseOrganization;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_cms_doc_contributor")
public class DocContributor extends AbstractContributor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1272287944160394457L;

	private DocContent document;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doc_con_id", unique = true, nullable = false)
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

	@Transient
	@Override
	public BaseOrganization getCompany() {
		// TODO Auto-generated method stub
		throw new IllegalStateException("getCompany() not implemented yet.");
	}
	
	@Transient
	public void setCompany(BaseOrganization company) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("setCompany() not implemented yet.");
	}
				
}
package com.nokor.common.app.content.model.article;

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
@Table(name="td_cms_art_contributor")
public class ArtContributor extends AbstractContributor {
	/** */
	private static final long serialVersionUID = 2916361824553894499L;
	
	private Article article;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "art_con_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the article
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="art_id", nullable = false)
	public Article getArticle() {
		return article;
	}

	/**
	 * @param article the article to set
	 */
	public void setArticle(Article article) {
		this.article = article;
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
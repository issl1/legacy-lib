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

import com.nokor.common.app.content.model.base.AbstractClassification;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_cms_art_classification")
public class ArtClassification extends AbstractClassification {
	/** */
	private static final long serialVersionUID = 7912205172868868119L;
	private Article article;

	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "art_cla_id", unique = true, nullable = false)
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
}

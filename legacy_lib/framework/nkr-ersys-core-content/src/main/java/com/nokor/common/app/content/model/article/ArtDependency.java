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

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.content.model.base.AbstractDependency;


@Entity
@Table(name="td_cms_art_dependency")
public class ArtDependency extends AbstractDependency {
	/** */
	private static final long serialVersionUID = -3256396422493191145L;

	private Article article;
	private Article other;
	

   /**
    * 
    * @return
    */
	public static ArtDependency createInstance() {
		ArtDependency docDep = EntityFactory.createInstance(ArtDependency.class);
        return docDep;
	}

	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "art_dep_id", unique = true, nullable = false)
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
	
	
	/**
	 * @return the other
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="other_art_id", nullable = false)
	public Article getOther() {
		return other;
	}
	
	
	/**
	 * @param other the other to set
	 */
	public void setOther(Article other) {
		this.other = other;
	}
	
    
}

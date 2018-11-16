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

import com.nokor.common.app.content.model.base.AbstractAttachment;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_cms_art_attachment")
public class ArtAttachment extends AbstractAttachment {
	/** */
	private static final long serialVersionUID = 4294354345805743858L;

	private Article article;

	/**
     * 
     */
    public static ArtAttachment createInstance() {
    	ArtAttachment artAtt = EntityFactory.createInstance(ArtAttachment.class);
    	artAtt.setVisible(Boolean.TRUE);
   	
    	return artAtt;
    }
	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "art_att_id", unique = true, nullable = false)
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

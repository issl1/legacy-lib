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
@Table(name = "td_cms_text_comment")
public class TextComment extends AbstractContent {
	/** */
	private static final long serialVersionUID = 2012717291247122585L;

	private Text text;

	/**
     * 
     * @return
     */
	public static TextComment createInstance() {
		TextComment annotation = new TextComment();
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
	 * @return the text
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tex_id", nullable = false)
	public Text getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(Text text) {
		this.text = text;
	}
	
}

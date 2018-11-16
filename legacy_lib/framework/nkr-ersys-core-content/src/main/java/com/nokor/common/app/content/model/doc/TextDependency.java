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

import com.nokor.common.app.content.model.base.AbstractDependency;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_cms_text_dependency")
public class TextDependency extends AbstractDependency {
	/** */
	private static final long serialVersionUID = -7567580834696228261L;

	private Text text;
	private Text other;

	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tex_dep_id", unique = true, nullable = false)
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



	/**
	 * @return the other
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="other_tex_id", nullable = false)
	public Text getOther() {
		return other;
	}



	/**
	 * @param other the other to set
	 */
	public void setOther(Text other) {
		this.other = other;
	}


}

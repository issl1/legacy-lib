package com.nokor.common.app.content.model.doc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.content.model.base.AbstractContent;
import com.nokor.common.app.content.model.eref.ETextDependencyType;
import com.nokor.common.app.eref.ELanguage;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_cms_text")
public class Text extends AbstractContent {
	/** */
	private static final long serialVersionUID = -6531914292980411551L;

	private static final String FIELD_DOCUMENT = "document";
	private static final String FIELD_TITLE = "title";
	private static final String FIELD_CONTENT = "content";
	private static final String FIELD_KEYWORDS = "keywords";
	public static final String VERSION_COMMENT_INIT = "creation";

	private DocContent document;
	private Integer sortIndex;
	private List<TextDependency> dependencies;
	private List<TextComment> comments;


	/**
	 * 
	 * @return
	 */
	public static Text createInstance() {
		Text text = EntityFactory.createInstance(Text.class);
		text.setVersionNumber(1);
		text.setVersionComment(VERSION_COMMENT_INIT);
		return text;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tex_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the document
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_id", nullable = false)
	public DocContent getDocument() {
		return document;
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocument(DocContent document) {
		this.document = document;
	}


	/**
	 * @return the sortIndex
	 */
	@Column(name = "sort_index", nullable = false)
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex
	 *            the sortIndex to set
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}
	
	/**
	 * @return the dependencies
	 */
	@OneToMany(mappedBy = "other", fetch = FetchType.LAZY)
	@Where(clause="sta_rec_id = 1 or sta_rec_id is null")
	public List<TextDependency> getDependencies() {
		if (dependencies == null) {
			dependencies = new ArrayList<>();
		}
		return dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(List<TextDependency> dependencies) {
		this.dependencies = dependencies;
	}

	@Transient
	public List<TextDependency> getDependencies(ETextDependencyType type) {
		List<TextDependency> dependencies = new ArrayList<>();
		for (TextDependency dep : getDependencies()) {
			if (dep.getType().getId().equals(type.getId())) {
				dependencies.add(dep);
			}
		}
		
		return dependencies;
	}

	@Transient
	public List<TextDependency> getChildren() {
		return getDependencies(ETextDependencyType.CHILD_CONTENT);
	}
	/**
	 * @return the comments
	 */
	@OneToMany(mappedBy = "text", fetch = FetchType.LAZY)
	@Where(clause="sta_rec_id = 1 or sta_rec_id is null")
	public List<TextComment> getComments() {
		if (comments == null) {
			comments = new ArrayList<>();
		}
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<TextComment> comments) {
		this.comments = comments;
	}
	
	/**
	 * 
	 * @param lang
	 * @return
	 */
	public static String getFieldTitle(ELanguage lang) {
		String langCode = lang.getLocaleLanguage();
		String capLang = langCode.substring(0, 1).toUpperCase() + langCode.substring(1);
		return FIELD_TITLE + capLang;
	}

	/**
	 * 
	 * @param lang
	 * @return
	 */
	public static String getFieldKeywords(ELanguage lang) {
		String langCode = lang.getLocaleLanguage();
		String capLang = langCode.substring(0, 1).toUpperCase() + langCode.substring(1);
		return FIELD_KEYWORDS + capLang;
	}

	/**
	 * 
	 * @param lang
	 * @return
	 */
	public static String getFieldContent(ELanguage lang) {
		String langCode = lang.getLocaleLanguage();
		String capLang = langCode.substring(0, 1).toUpperCase() + langCode.substring(1);
		return FIELD_CONTENT + capLang;
	}

	/**
	 * 
	 * @param lang
	 * @return
	 */
	public static String getFieldDocument() {
		return FIELD_DOCUMENT;
	}

}
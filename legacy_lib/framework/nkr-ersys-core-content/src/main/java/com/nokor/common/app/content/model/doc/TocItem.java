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

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.content.model.base.ContentLevel;

@Entity
@Table(name = "tu_cms_toc_item")
public class TocItem extends EntityA {
	/** */
	private static final long serialVersionUID = -3287433462602593748L;

	private TocTemplate tocTemplate;
	private ContentLevel level; // 1
	private String prefix; // Part
	private String sortedList; // I, II, III, IV
	private String suffix; // -

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
	 */
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "toc_ite_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the level
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cnt_lev_id", nullable = false)
	public ContentLevel getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(ContentLevel level) {
		this.level = level;
	}

	/**
	 * @return the prefix
	 */
	@Column(name = "toc_ite_prefix", nullable = true)
	public String getPrefix() {
		return prefix;
	}
	
	@Transient
	public String getPrefixLabel() {
		return I18N.message(prefix);
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the sortedList
	 */
	@Column(name = "toc_ite_sorted_list", nullable = false)
	public String getSortedList() {
		return sortedList;
	}

	/**
	 * @param sortedList
	 *            the sortedList to set
	 */
	public void setSortedList(String sortedList) {
		this.sortedList = sortedList;
	}

	/**
	 * @return the suffix
	 */
	@Column(name = "toc_ite_suffixe", nullable = true)
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix
	 *            the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Transient
	public Integer getSortIndex() {
		return level.getSortIndex();
	}

	/**
	 * @return the tocTemplate
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "toc_tem_id", nullable = false)
	public TocTemplate getTocTemplate() {
		return tocTemplate;
	}

	/**
	 * @param tocTemplate
	 *            the tocTemplate to set
	 */
	public void setTocTemplate(TocTemplate tocTemplate) {
		this.tocTemplate = tocTemplate;
	}

}

package com.nokor.common.app.content.model.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import org.apache.commons.lang3.LocaleUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.common.app.eref.ELanguage;

@Entity
@Table(name="ts_cms_content_group")
public class ContentGroup extends EntityRefA {
	/** */
	private static final long serialVersionUID = -8410694636366811048L;

	public final static long DEFAULT = 1L;

	private String keywordsKh;
	private String keywordsFr;
	private String keywordsEn;
	
	private String summaryKh;
	private String summaryFr;
	private String summaryEn;
	
	private ContentGroup parent;
	private List<ContentGroup> children = new ArrayList<ContentGroup>();
	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cnt_grp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "cnt_grp_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}
	
	@Column(name = "cnt_grp_desc", nullable = false, length = 255)
	@Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @return the keywordsKh
	 */
	@Column(name = "cnt_grp_keywords_kh", nullable = true)
	public String getKeywordsKh() {
		return keywordsKh;
	}

	/**
	 * @param keywordsKh the keywordsKh to set
	 */
	public void setKeywordsKh(String keywordsKh) {
		this.keywordsKh = keywordsKh;
	}
	
	
	/**
	 * @return the keywordsFr
	 */
	@Column(name = "cnt_grp_keywords_fr", nullable = true)
	public String getKeywordsFr() {
		return keywordsFr;
	}

	/**
	 * @param keywordsFr the keywordsFr to set
	 */
	public void setKeywordsFr(String keywordsFr) {
		this.keywordsFr = keywordsFr;
	}

	/**
	 * @return the keywordsEn
	 */
	@Column(name = "cnt_grp_keywords_en", nullable = true)
	public String getKeywordsEn() {
		return keywordsEn;
	}

	/**
	 * @param keywordsEn the keywordsEn to set
	 */
	public void setKeywordsEn(String keywordsEn) {
		this.keywordsEn = keywordsEn;
	}

	@Transient
	public String getSummary(ELanguage lang) {
		if (lang == ELanguage.KHMER) {
			return summaryKh; 
		} else if (lang == ELanguage.FRENCH) {
			return summaryFr; 
		} else {
			return summaryEn; 
		}
	}
	
	/**
	 * @return the summaryKh
	 */
	@Column(name = "cnt_grp_summary_kh", nullable = true)
	public String getSummaryKh() {
		return summaryKh;
	}

	/**
	 * @param summaryKh the summaryKh to set
	 */
	public void setSummaryKh(String summaryKh) {
		this.summaryKh = summaryKh;
	}

	/**
	 * @return the summaryFr
	 */
	@Column(name = "cnt_grp_summary_fr", nullable = true)
	public String getSummaryFr() {
		return summaryFr;
	}

	/**
	 * @param summaryFr the summaryFr to set
	 */
	public void setSummaryFr(String summaryFr) {
		this.summaryFr = summaryFr;
	}

	/**
	 * @return the summaryEn
	 */
	@Column(name = "cnt_grp_summary_en", nullable = true)
	public String getSummaryEn() {
		return summaryEn;
	}

	/**
	 * @param summaryEn the summaryEn to set
	 */
	public void setSummaryEn(String summaryEn) {
		this.summaryEn = summaryEn;
	}

	/**
	 * @return the parent
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_cnt_grp_id", nullable = true)
	public ContentGroup getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(ContentGroup parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	public List<ContentGroup> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<ContentGroup> children) {
		this.children = children;
	}

	/**
	 * 
	 * @return
	 */
	@Transient
	public String getDesc(ELanguage language) {
		Locale local = LocaleUtils.toLocale(language.getCode().toLowerCase());
		Map<String, String> resourceI18N = I18N.getResources().get(local);
		
		return resourceI18N.get(ContentGroup.class.getSimpleName().toLowerCase() + "." + language.getCode().toLowerCase() + "." + code.toLowerCase());
	}
		
	/**
	 * 
	 * @return
	 */
	@Transient
	public String getDescKh() {	
		return getDesc(ELanguage.KHMER);
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public String getDescFr() {	
		return getDesc(ELanguage.FRENCH);
	}
	/**
	 * 
	 * @return
	 */
	@Override
	@Transient
	public String getDescEn() {
		return getDesc(ELanguage.ENGLISH);
	}
}

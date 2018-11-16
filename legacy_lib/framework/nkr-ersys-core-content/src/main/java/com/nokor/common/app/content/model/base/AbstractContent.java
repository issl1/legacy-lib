package com.nokor.common.app.content.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import com.nokor.common.app.content.model.eref.EContentCategoryDefault;
import com.nokor.common.app.content.model.eref.EContentType;
import com.nokor.common.app.eref.ELanguage;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class AbstractContent extends EntityWkf {
	/** */
	private static final long serialVersionUID = -8021140801214668364L;

	public static final String VERSION_COMMENT_INIT = "creation";

	private static final String FIELD_TITLE = "title";
	private static final String FIELD_SUMMARY = "summary";
	private static final String FIELD_KEYWORDS = "keywords";
	private static final String FIELD_PDF_FILE = "pdfFile";

	private ContentCategory category;
	private ContentType type;
	private ContentGroup group;
	private ContentLevel level;

	private String reference;

	private Date contentDate;
	private Date startDate; // valid between startDate/endDate
	private Date endDate;

	private String titleKh;
	private String titleFr;
	private String titleEn;

	private String subTitleKh;
	private String subTitleFr;
	private String subTitleEn;

	private String summaryKh;
	private String summaryFr;
	private String summaryEn;

	private String contentKh;
	private String contentFr;
	private String contentEn;

	private String keywordsKh;
	private String keywordsFr;
	private String keywordsEn;

	private String pdfFileKh;
	private String pdfFileFr;
	private String pdfFileEn;

	private Integer versionNumber;
	private String versionComment;
	private Boolean isPublic;
	private Boolean isVisible;
	private Boolean isLocked;
	private SecUser lockedBy;
	private LockingReason lockingReason;

	private SecUser createdBy;
	private SecUser validatedBy;
	private Date validationDate;
	private SecUser publishedBy;
	private Date publishingDate;
	private Date reviewingDate;
	private SecUser reviewedBy;

	/**
	 * 
	 */
	public void initDefault() {
		setCategory(EContentCategoryDefault.getDefault());
		setType(EContentType.getDefault());
		setIsPublic(Boolean.TRUE);
		setWkfStatus(EWkfStatus.NEW);
		setVersionNumber(1);
		setVersionComment(VERSION_COMMENT_INIT);
	}

	@Transient
	public String getTitleLabel() {
		if (StringUtils.isNotEmpty(getTitle(ELanguage.KHMER))) {
			return getTitle(ELanguage.KHMER) + " (" + getId().toString() + ")";
		} else if (StringUtils.isNotEmpty(getTitle(ELanguage.FRENCH))) {
			return getTitle(ELanguage.FRENCH) + " (" + getId().toString() + ")";
		} else {
			return getTitle(ELanguage.ENGLISH) + " (" + getId().toString() + ")";
		}
	}

	@Transient
	public String getTitle(ELanguage lang) {
		if (lang == ELanguage.KHMER) {
			return titleKh;
		} else if (lang == ELanguage.FRENCH) {
			return titleFr;
		} else {
			return titleEn;
		}
	}

	/**
	 * @return the category
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cnt_cat_id", nullable = false)
	public ContentCategory getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(ContentCategory category) {
		this.category = category;
	}

	/**
	 * @return the type
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cnt_typ_id", nullable = false)
	public ContentType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ContentType type) {
		this.type = type;
	}

	/**
	 * @return the group
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cnt_grp_id", nullable = false)
	public ContentGroup getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(ContentGroup group) {
		this.group = group;
	}

	/**
	 * @return the level
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cnt_lev_id", nullable = true)
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
	 * Normally should be unique (but pb in the old db)
	 * 
	 * @return the reference
	 */
	@Column(name = "cnt_reference", nullable = true)
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the titleKh
	 */
	@Column(name = "cnt_title_kh", nullable = true, length = 1000)
	public String getTitleKh() {
		return titleKh;
	}

	/**
	 * @param titleKh
	 *            the titleKh to set
	 */
	public void setTitleKh(String titleKh) {
		this.titleKh = titleKh;
	}

	/**
	 * @return the titleFr
	 */
	@Column(name = "cnt_title_fr", nullable = true, length = 1000)
	public String getTitleFr() {
		return titleFr;
	}

	/**
	 * @param titleFr
	 *            the titleFr to set
	 */
	public void setTitleFr(String titleFr) {
		this.titleFr = titleFr;
	}

	/**
	 * @return the titleEn
	 */
	@Column(name = "cnt_title_en", nullable = true, length = 1000)
	public String getTitleEn() {
		return titleEn;
	}

	/**
	 * @param titleEn
	 *            the titleEn to set
	 */
	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}

	/**
	 * @return the subTitleKh
	 */
	@Column(name = "cnt_sub_title_kh", nullable = true, length = 1000)
	public String getSubTitleKh() {
		return subTitleKh;
	}

	/**
	 * @param subTitleKh
	 *            the subTitleKh to set
	 */
	public void setSubTitleKh(final String subTitleKh) {
		this.subTitleKh = subTitleKh;
	}

	/**
	 * @return the subTitleFr
	 */
	@Column(name = "cnt_sub_title_fr", nullable = true, length = 1000)
	public String getSubTitleFr() {
		return subTitleFr;
	}

	/**
	 * @param subTitleFr
	 *            the subTitleFr to set
	 */
	public void setSubTitleFr(final String subTitleFr) {
		this.subTitleFr = subTitleFr;
	}

	/**
	 * @return the subTitleEn
	 */
	@Column(name = "cnt_sub_title_en", nullable = true, length = 1000)
	public String getSubTitleEn() {
		return subTitleEn;
	}

	/**
	 * @param subTitleEn
	 *            the subTitleEn to set
	 */
	public void setSubTitleEn(final String subTitleEn) {
		this.subTitleEn = subTitleEn;
	}

	@Transient
	public String getSubTitle(ELanguage lang) {
		if (lang == ELanguage.KHMER) {
			return subTitleKh;
		} else if (lang == ELanguage.FRENCH) {
			return subTitleFr;
		} else {
			return subTitleEn;
		}
	}

	@Transient
	public String getSummary(ELanguage lang) {
		if (lang == ELanguage.KHMER) {
			return summaryKh;
		} else if (lang == ELanguage.FRENCH) {
			return summaryFr;
		} else {
			return titleEn;
		}
	}

	/**
	 * @return the summaryKh
	 */
	@Column(name = "cnt_summary_kh", nullable = true, columnDefinition = "TEXT")
	public String getSummaryKh() {
		return summaryKh;
	}

	/**
	 * @param summaryKh
	 *            the summaryKh to set
	 */
	public void setSummaryKh(String summaryKh) {
		this.summaryKh = summaryKh;
	}

	/**
	 * @return the summaryFr
	 */
	@Column(name = "cnt_summary_fr", nullable = true, columnDefinition = "TEXT")
	public String getSummaryFr() {
		return summaryFr;
	}

	/**
	 * @param summaryFr
	 *            the summaryFr to set
	 */
	public void setSummaryFr(String summaryFr) {
		this.summaryFr = summaryFr;
	}

	/**
	 * @return the summaryEn
	 */
	@Column(name = "cnt_summary_en", nullable = true, columnDefinition = "TEXT")
	public String getSummaryEn() {
		return summaryEn;
	}

	/**
	 * @param summaryEn
	 *            the summaryEn to set
	 */
	public void setSummaryEn(String summaryEn) {
		this.summaryEn = summaryEn;
	}

	@Transient
	public String getContent(ELanguage lang) {
		if (lang == ELanguage.KHMER) {
			return contentKh;
		} else if (lang == ELanguage.FRENCH) {
			return contentFr;
		} else {
			return contentEn;
		}
	}

	/**
	 * @return the contentKh
	 */
	@Column(name = "cnt_content_kh", nullable = true, columnDefinition = "TEXT")
	public String getContentKh() {
		return contentKh;
	}

	/**
	 * @param contentKh
	 *            the contentKh to set
	 */
	public void setContentKh(String contentKh) {
		this.contentKh = contentKh;
	}

	/**
	 * @return the contentFr
	 */
	@Column(name = "cnt_content_fr", nullable = true, columnDefinition = "TEXT")
	public String getContentFr() {
		return contentFr;
	}

	/**
	 * @param contentFr
	 *            the contentFr to set
	 */
	public void setContentFr(String contentFr) {
		this.contentFr = contentFr;
	}

	/**
	 * @return the contentEn
	 */
	@Column(name = "cnt_content_en", nullable = true, columnDefinition = "TEXT")
	public String getContentEn() {
		return contentEn;
	}

	/**
	 * @param contentEn
	 *            the contentEn to set
	 */
	public void setContentEn(String contentEn) {
		this.contentEn = contentEn;
	}

	/**
	 * @return the keywordsKh
	 */
	@Column(name = "cnt_keywords_kh", nullable = true, columnDefinition = "TEXT")
	public String getKeywordsKh() {
		return keywordsKh;
	}

	/**
	 * @param keywordsKh
	 *            the keywordsKh to set
	 */
	public void setKeywordsKh(String keywordsKh) {
		this.keywordsKh = keywordsKh;
	}

	/**
	 * @return the keywordsFr
	 */
	@Column(name = "cnt_keywords_fr", nullable = true, columnDefinition = "TEXT")
	public String getKeywordsFr() {
		return keywordsFr;
	}

	/**
	 * @param keywordsFr
	 *            the keywordsFr to set
	 */
	public void setKeywordsFr(String keywordsFr) {
		this.keywordsFr = keywordsFr;
	}

	/**
	 * @return the keywordsEn
	 */
	@Column(name = "cnt_keywords_en", nullable = true, columnDefinition = "TEXT")
	public String getKeywordsEn() {
		return keywordsEn;
	}

	/**
	 * @param keywordsEn
	 *            the keywordsEn to set
	 */
	public void setKeywordsEn(String keywordsEn) {
		this.keywordsEn = keywordsEn;
	}

	@Transient
	public String getKeywords(ELanguage lang) {
		if (lang == ELanguage.KHMER) {
			return keywordsKh;
		} else if (lang == ELanguage.FRENCH) {
			return keywordsFr;
		} else {
			return keywordsEn;
		}
	}

	/**
	 * @return the pdfFileKh
	 */
	@Column(name = "cnt_pdf_kh", nullable = true)
	public String getPdfFileKh() {
		return pdfFileKh;
	}

	/**
	 * @param pdfFileKh
	 *            the pdfFileKh to set
	 */
	public void setPdfFileKh(String pdfFileKh) {
		this.pdfFileKh = pdfFileKh;
	}

	/**
	 * @return the pdfFileFr
	 */
	@Column(name = "cnt_pdf_fr", nullable = true)
	public String getPdfFileFr() {
		return pdfFileFr;
	}

	/**
	 * @param pdfFileFr
	 *            the pdfFileFr to set
	 */
	public void setPdfFileFr(String pdfFileFr) {
		this.pdfFileFr = pdfFileFr;
	}

	/**
	 * @return the pdfFileEn
	 */
	@Column(name = "cnt_pdf_en", nullable = true)
	public String getPdfFileEn() {
		return pdfFileEn;
	}

	/**
	 * @param pdfFileEn
	 *            the pdfFileEn to set
	 */
	public void setPdfFileEn(String pdfFileEn) {
		this.pdfFileEn = pdfFileEn;
	}

	@Transient
	public String getPdfFile(ELanguage lang) {
		if (lang == ELanguage.KHMER) {
			return pdfFileKh;
		} else if (lang == ELanguage.FRENCH) {
			return pdfFileFr;
		} else {
			return pdfFileEn;
		}
	}

	@Transient
	public void setPdfFile(ELanguage lang, String pdfFile) {
		if (lang == ELanguage.KHMER) {
			pdfFileKh = pdfFile;
		} else if (lang == ELanguage.FRENCH) {
			pdfFileFr = pdfFile;
		} else {
			pdfFileEn = pdfFile;
		}
	}

	/**
	 * @return the contentDate
	 */
	@Column(name = "cnt_content_date", nullable = true)
	public Date getContentDate() {
		return contentDate;
	}

	/**
	 * @param contentDate
	 *            the contentDate to set
	 */
	public void setContentDate(Date contentDate) {
		this.contentDate = contentDate;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "cnt_start_date", nullable = true)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(name = "cnt_end_date", nullable = true)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the versionNumber
	 */
	@Column(name = "cnt_version_number", nullable = false)
	public Integer getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber
	 *            the versionNumber to set
	 */
	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return the versionComment
	 */
	@Column(name = "cnt_version_comment", nullable = false)
	public String getVersionComment() {
		return versionComment;
	}

	/**
	 * @param versionComment
	 *            the versionComment to set
	 */
	public void setVersionComment(String versionComment) {
		this.versionComment = versionComment;
	}

	/**
	 * @return the lockedBy
	 */
	@Transient
	public SecUser getLockedBy() {
		return lockedBy;
	}

	/**
	 * @param lockedBy
	 *            the lockedBy to set
	 */
	public void setLockedBy(SecUser lockedBy) {
		this.lockedBy = lockedBy;
	}

	/**
	 * @return the lockingReason
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loc_rea_id", nullable = true)
	public LockingReason getLockingReason() {
		return lockingReason;
	}

	/**
	 * @param lockingReason
	 *            the lockingReason to set
	 */
	public void setLockingReason(LockingReason lockingReason) {
		this.lockingReason = lockingReason;
	}

	@Transient
	public String getTextKh() {
		return getContentKh();
	}

	@Transient
	public void setTextKh(final String textKh) {
		setContentKh(textKh);
	}

	@Transient
	public String getTextFr() {
		return getContentFr();
	}

	@Transient
	public void setTextFr(final String textFr) {
		setContentFr(textFr);
	}

	@Transient
	public String getTextEn() {
		return getContentEn();
	}

	@Transient
	public void setTextEn(final String textEn) {
		setContentEn(textEn);
	}

	@Transient
	public String getText(ELanguage lang) {
		return getContent(lang);
	}

	@Transient
	public boolean inTitle(String text) {
		return StringUtils.contains(titleKh, text) || StringUtils.contains(titleFr, text) || StringUtils.contains(titleEn, text);
	}

	@Transient
	public boolean inKeywords(String text) {
		return StringUtils.contains(keywordsKh, text) || StringUtils.contains(keywordsFr, text) || StringUtils.contains(keywordsEn, text);
	}

	@Transient
	public String getShortDescKh() {
		return getSummaryKh();
	}

	/**
	 * 
	 * @param shortDescKh
	 */
	public void setShortDescKh(final String shortDescKh) {
		setSummaryKh(shortDescKh);
	}

	@Transient
	public String getShortDescFr() {
		return getSummaryFr();
	}

	/**
	 * 
	 * @param shortDescFr
	 */
	public void setShortDescFr(final String shortDescFr) {
		setSummaryFr(shortDescFr);
	}

	@Transient
	public String getShortDescEn() {
		return getSummaryEn();
	}

	/**
	 * 
	 * @param shortDescEn
	 */
	public void setShortDescEn(final String shortDescEn) {
		setSummaryEn(shortDescEn);
	}

	@Transient
	public String getShortDesc(ELanguage lang) {
		return getSummary(lang);
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
	 * @return the createdBy
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_sec_usr_id", nullable = true)
	public SecUser getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(SecUser createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the validatedBy
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "validated_by_sec_usr_id", nullable = true)
	public SecUser getValidatedBy() {
		return validatedBy;
	}

	/**
	 * @param validatedBy
	 *            the validatedBy to set
	 */
	public void setValidatedBy(SecUser validatedBy) {
		this.validatedBy = validatedBy;
	}

	/**
	 * @return
	 */
	@Transient
	public boolean hasBeenValidated() {
		return validationDate != null;
	}

	/**
	 * @return the validationDate
	 */
	@Column(name = "cnt_validation_date", nullable = true)
	public Date getValidationDate() {
		return validationDate;
	}

	/**
	 * @param validationDate
	 *            the validationDate to set
	 */
	public void setValidationDate(Date validationDate) {
		this.validationDate = validationDate;
	}

	/**
	 * @return the publishingDate
	 */
	@Column(name = "cnt_publishing_date", nullable = true)
	public Date getPublishingDate() {
		return publishingDate;
	}

	/**
	 * @param publishingDate
	 *            the publishingDate to set
	 */
	public void setPublishingDate(Date publishingDate) {
		this.publishingDate = publishingDate;
	}

	/**
	 * @return
	 */
	@Transient
	public boolean hasBeenPublished() {
		return publishingDate != null;
	}

	/**
	 * @return the publishedBy
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "published_by_sec_usr_id", nullable = true)
	public SecUser getPublishedBy() {
		return publishedBy;
	}

	/**
	 * @param publishedBy
	 *            the publishedBy to set
	 */
	public void setPublishedBy(SecUser publishedBy) {
		this.publishedBy = publishedBy;
	}

	/**
	 * @return
	 */
	@Transient
	public boolean hasBeenReviewed() {
		return reviewingDate != null;
	}

	/**
	 * @return the reviewDate
	 */
	@Column(name = "cnt_reviewing_date", nullable = true)
	public Date getReviewingDate() {
		return reviewingDate;
	}

	/**
	 * @param reviewingDate
	 *            the reviewingDate to set
	 */
	public void setReviewingDate(Date reviewingDate) {
		this.reviewingDate = reviewingDate;
	}

	/**
	 * @return the reviewedBy
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewed_by_sec_usr_id", nullable = true)
	public SecUser getReviewedBy() {
		return reviewedBy;
	}

	/**
	 * @param reviewedBy
	 *            the reviewedBy to set
	 */
	public void setReviewedBy(SecUser reviewedBy) {
		this.reviewedBy = reviewedBy;
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
	public static String getFieldSummary(ELanguage lang) {
		String langCode = lang.getLocaleLanguage();
		String capLang = langCode.substring(0, 1).toUpperCase() + langCode.substring(1);
		return FIELD_SUMMARY + capLang;
	}

	/**
	 * 
	 * @param lang
	 * @return
	 */
	public static String getFieldPdfFile(ELanguage lang) {
		String langCode = lang.getLocaleLanguage();
		String capLang = langCode.substring(0, 1).toUpperCase() + langCode.substring(1);
		return FIELD_PDF_FILE + capLang;
	}

	@Column(name = "cnt_is_visible", nullable = false)
	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Column(name = "cnt_is_locked", nullable = false)
	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	@Column(name = "cnt_is_public", nullable = false)
	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

}

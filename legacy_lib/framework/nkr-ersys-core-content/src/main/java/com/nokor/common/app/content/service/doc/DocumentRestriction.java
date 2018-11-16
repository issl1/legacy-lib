package com.nokor.common.app.content.service.doc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.util.CollectionUtils;

import com.nokor.common.app.content.model.base.ContentCategory;
import com.nokor.common.app.content.model.doc.DocContent;
import com.nokor.common.app.content.model.doc.Text;
import com.nokor.common.app.eref.ELanguage;

/**
 * 
 * @author keomorakort.man
 * @version $revision$
 */
public class DocumentRestriction extends BaseRestrictions<DocContent> {
	/** */
	private static final long serialVersionUID = -4956335173486473265L;
	


	private static final String AS_TEXTS = "texts";
	

	private List<Long> docIds;
	
	private String searchedText;

	private boolean inTitle;
	private boolean inKeywords;
	private boolean inReference;
	private boolean inSummary;
	private boolean inContent;
	private boolean inAlias;

	private ELanguage language;

	private boolean hasOnlyPdfFileKh;
	private boolean hasPdfFile;
	private List<String> fileNames;

	private Boolean isPublic;
	private Boolean isVisible;
	private Boolean isLocked;
	
	private List<Long> categoryIds;
	private Long categoryId;

    private Date fromPublicationDate;
    private Date toPublicationDate;

	/**
	 * 
	 */
	public DocumentRestriction() {
		super(DocContent.class);
	}

	@Override
	public void preBuildAssociation() {
		addAssociation("texts", AS_TEXTS, JoinType.LEFT_OUTER_JOIN);
	}

	@Override
	public void preBuildSpecificCriteria() {
		if (docIds != null && docIds.size() > 0) { 
			addCriterion(Restrictions.in(DocContent.ID, docIds.toArray()));
		}
		
		if (StringUtils.isNotEmpty(searchedText)) {
			String trimSearchTitle = searchedText.trim();
			
			if (trimSearchTitle.charAt(0) == '"' && trimSearchTitle.charAt(trimSearchTitle.length() - 1) == '"') {
				trimSearchTitle = trimSearchTitle.substring(1, trimSearchTitle.length() - 1);
				
				Disjunction or = Restrictions.disjunction();
				buildSearchTextRestriction(or, trimSearchTitle);
				buildFileNameSearch(or);
				
				addCriterion(or);
				
			} 
			else if (trimSearchTitle.contains("+") || trimSearchTitle.contains(" ")) {
				
				String[] customField = null;
				
				if (trimSearchTitle.contains("+")) {
					customField = trimSearchTitle.split("\\+");
				} 
				else {
					customField = trimSearchTitle.split(" ");
				}
				
				Disjunction or = Restrictions.disjunction();
				for (String custField : customField) {
					buildSearchTextRestriction(or, custField);
				}
				
				buildFileNameSearch(or);
				addCriterion(or);
				
			} else {
				Disjunction or = Restrictions.disjunction();
				buildSearchTextRestriction(or, trimSearchTitle);
				buildFileNameSearch(or);
				
				addCriterion(or);
			}
			
		}

		if (categoryId != null && categoryId > 0) {
			addCriterion(Restrictions.eq(DocContent.CATEGORY + DOT + ContentCategory.ID, categoryId));
		}
		else if (categoryIds != null && categoryIds.size() > 0) {
			addCriterion(Restrictions.in(DocContent.CATEGORY + DOT + ContentCategory.ID, categoryIds.toArray()));
		}
		
		if (hasOnlyPdfFileKh) {
			addCriterion(Restrictions.isNotNull(DocContent.getFieldPdfFile(ELanguage.KHMER)));
		} else if (hasPdfFile) {
			addCriterion(Restrictions.or(
							Restrictions.isNotNull(DocContent.getFieldPdfFile(ELanguage.KHMER)), 
							Restrictions.isNotNull(DocContent.getFieldPdfFile(ELanguage.FRENCH)), 
							Restrictions.isNotNull(DocContent.getFieldPdfFile(ELanguage.ENGLISH))));
		}

		if (BooleanUtils.isTrue(isPublic)) {
			addCriterion(Restrictions.eq(DocContent.ISPUBLIC, isPublic));
		}
		
		if (BooleanUtils.isTrue(isVisible)) {
			addCriterion(Restrictions.eq(DocContent.ISVISIBLE,isVisible));
		}
		
		if (BooleanUtils.isTrue(isLocked)) {
			addCriterion(Restrictions.eq(DocContent.ISLOCKED, isLocked));
		}
		
		if (fromPublicationDate != null && toPublicationDate != null) {
        	fromPublicationDate = DateUtils.getDateAtBeginningOfDay(fromPublicationDate);
        	toPublicationDate = DateUtils.getDateAtEndOfDay(toPublicationDate);
            addCriterion(DocContent.PUBLISHINGDATE, FilterMode.BETWEEN, fromPublicationDate, toPublicationDate);
        }
        else if (fromPublicationDate != null) {
        	fromPublicationDate = DateUtils.getDateAtBeginningOfDay(fromPublicationDate);
            addCriterion(DocContent.PUBLISHINGDATE, FilterMode.GREATER_OR_EQUALS, fromPublicationDate);
        }
        else if (toPublicationDate != null) {
        	toPublicationDate = DateUtils.getDateAtEndOfDay(toPublicationDate);
            addCriterion(DocContent.PUBLISHINGDATE, FilterMode.LESS_OR_EQUALS, toPublicationDate);
        }
	}
	
	/**
	 * 
	 * @param searchedText
	 * @return
	 */
	private void buildSearchTextRestriction(Disjunction or, String searchedText) {
		if (language != null) {
			if (inTitle) {
				or.add(Restrictions.ilike(DocContent.getFieldTitle(language), searchedText, MatchMode.ANYWHERE));
			}
			
			if (inKeywords) {
				or.add(Restrictions.ilike(DocContent.getFieldKeywords(language), searchedText, MatchMode.ANYWHERE));
			}
			
			if (inSummary) {
				or.add(Restrictions.ilike(DocContent.getFieldSummary(language), searchedText, MatchMode.ANYWHERE));
			}
			
			if (inContent) {
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldTitle(language), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldKeywords(language), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldContent(language), searchedText, MatchMode.ANYWHERE));
			}
		} else {
			if (inTitle) {
				or.add(Restrictions.ilike(DocContent.getFieldTitle(ELanguage.KHMER), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(DocContent.getFieldTitle(ELanguage.FRENCH), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(DocContent.getFieldTitle(ELanguage.ENGLISH), searchedText, MatchMode.ANYWHERE));
			}
			
			if (inKeywords) {
				or.add(Restrictions.ilike(DocContent.getFieldKeywords(ELanguage.KHMER), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(DocContent.getFieldKeywords(ELanguage.FRENCH), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(DocContent.getFieldKeywords(ELanguage.ENGLISH), searchedText, MatchMode.ANYWHERE));
			}
			
			if (inSummary) {
				or.add(Restrictions.ilike(DocContent.getFieldSummary(ELanguage.KHMER), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(DocContent.getFieldSummary(ELanguage.FRENCH), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(DocContent.getFieldSummary(ELanguage.ENGLISH), searchedText, MatchMode.ANYWHERE));
			}
			
			if (inContent) {
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldTitle(ELanguage.KHMER), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldTitle(ELanguage.FRENCH), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldTitle(ELanguage.ENGLISH), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldKeywords(ELanguage.KHMER), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldKeywords(ELanguage.FRENCH), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldKeywords(ELanguage.ENGLISH), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldContent(ELanguage.KHMER), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldContent(ELanguage.FRENCH), searchedText, MatchMode.ANYWHERE));
				or.add(Restrictions.ilike(AS_TEXTS + DOT + Text.getFieldContent(ELanguage.ENGLISH), searchedText, MatchMode.ANYWHERE));
			}
		}

		if (inReference) {
			or.add(Restrictions.ilike(DocContent.REFERENCE, searchedText, MatchMode.ANYWHERE));
		}
		
		if (inAlias) {
			or.add(Restrictions.ilike("alias", searchedText, MatchMode.ANYWHERE));
		}

	}
	
	/**
	 * 
	 * @param or
	 */
	private void buildFileNameSearch(Disjunction or)  {
		if (fileNames != null && !fileNames.isEmpty()) {
			or.add(Restrictions.in(DocContent.getFieldPdfFile(ELanguage.KHMER), fileNames.toArray()));
			or.add(Restrictions.in(DocContent.getFieldPdfFile(ELanguage.ENGLISH), fileNames.toArray()));
			or.add(Restrictions.in(DocContent.getFieldPdfFile(ELanguage.ENGLISH), fileNames.toArray()));
		}
	}
    
    

	/**
	 * @return the searchedText
	 */
	public String getSearchedText() {
		return searchedText;
	}

	/**
	 * @param searchedText
	 *            the searchedText to set
	 */
	public void setSearchedText(String searchedText) {
		this.searchedText = searchedText;
	}

	/**
	 * @return the inTitle
	 */
	public boolean isInTitle() {
		return inTitle;
	}

	/**
	 * @param inTitle
	 *            the inTitle to set
	 */
	public void setInTitle(boolean inTitle) {
		this.inTitle = inTitle;
	}

	/**
	 * @return the inKeywords
	 */
	public boolean isInKeywords() {
		return inKeywords;
	}

	/**
	 * @param inKeywords
	 *            the inKeywords to set
	 */
	public void setInKeywords(boolean inKeywords) {
		this.inKeywords = inKeywords;
	}

	/**
	 * @return the inReference
	 */
	public boolean isInReference() {
		return inReference;
	}

	/**
	 * @param inReference
	 *            the inReference to set
	 */
	public void setInReference(boolean inReference) {
		this.inReference = inReference;
	}

	/**
	 * @return the inSummary
	 */
	public boolean isInSummary() {
		return inSummary;
	}

	/**
	 * @param inSummary
	 *            the inSummary to set
	 */
	public void setInSummary(boolean inSummary) {
		this.inSummary = inSummary;
	}

	/**
	 * @return the inContent
	 */
	public boolean isInContent() {
		return inContent;
	}

	/**
	 * @param inContent
	 *            the inContent to set
	 */
	public void setInContent(boolean inContent) {
		this.inContent = inContent;
	}

	

	/**
	 * @return the language
	 */
	public ELanguage getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(ELanguage language) {
		this.language = language;
	}

	/**
	 * @return the categoryIds
	 */
	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	/**
	 * @param categoryIds
	 *            the categoryIds to set
	 */
	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	/**
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}



	/**
	 * @return the hasOnlyPdfFileKh
	 */
	public boolean isHasOnlyPdfFileKh() {
		return hasOnlyPdfFileKh;
	}

	/**
	 * @param hasOnlyPdfFileKh the hasOnlyPdfFileKh to set
	 */
	public void setHasOnlyPdfFileKh(boolean hasOnlyPdfFileKh) {
		this.hasOnlyPdfFileKh = hasOnlyPdfFileKh;
	}

	/**
	 * @return the hasPdfFile
	 */
	public boolean isHasPdfFile() {
		return hasPdfFile;
	}

	/**
	 * @param hasPdfFile the hasPdfFile to set
	 */
	public void setHasPdfFile(boolean hasPdfFile) {
		this.hasPdfFile = hasPdfFile;
	}

	/**
	 * @return the inAlias
	 */
	public boolean isInAlias() {
		return inAlias;
	}

	/**
	 * @param inAlias the inAlias to set
	 */
	public void setInAlias(boolean inAlias) {
		this.inAlias = inAlias;
	}

	/**
	 * @return the fromPublicationDate
	 */
	public Date getFromPublicationDate() {
		return fromPublicationDate;
	}

	/**
	 * @param fromPublicationDate the fromPublicationDate to set
	 */
	public void setFromPublicationDate(Date fromPublicationDate) {
		this.fromPublicationDate = fromPublicationDate;
	}

	/**
	 * @return the toPublicationDate
	 */
	public Date getToPublicationDate() {
		return toPublicationDate;
	}

	/**
	 * @param toPublicationDate the toPublicationDate to set
	 */
	public void setToPublicationDate(Date toPublicationDate) {
		this.toPublicationDate = toPublicationDate;
	}

	/**
	 * @return the docIds
	 */
	public List<Long> getDocIds() {
		return docIds;
	}

	/**
	 * @param docIds the docIds to set
	 */
	public void setDocIds(List<Long> docIds) {
		this.docIds = docIds;
	}
	
	/**
	 * 
	 * @param docId
	 */
	public void addDocId(Long docId) {
		if (this.docIds == null) {
			docIds = new ArrayList<Long>();
		}
		
		docIds.add(docId);
	}

	/**
	 * @return the fileNames
	 */
	public List<String> getFileNames() {
		return fileNames;
	}

	/**
	 * @param fileNames the fileNames to set
	 */
	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}
	
	/**
	 * 
	 * @param coll
	 * @param delim
	 * @param prefix
	 * @return
	 */
	public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			if (it.hasNext()) {
				sb.append(delim).append(prefix);
			}
		}
		return sb.toString();
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}	
}

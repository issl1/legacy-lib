/**
 * 
 */
package com.nokor.common.app.content.service.article;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.criteria.StringFilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.dao.hql.OrderDirection;

import com.nokor.common.app.content.model.article.Article;

/**
 * 
 * @author prasnar
 *
 */
public class ArticleRestriction extends BaseRestrictions<Article> {
    private Integer orderColumnIndex;
    private OrderDirection orderDirection;

    private Long artId;
    private String artTitle;
    private String text;

    private String[] keywords;

    private Long typArtId;
    private List<Long> typArtIdList;
    private Long topArtId;
    private List<Long> topArtIdList;
    private Boolean  isChildrenOnly;
    
    private Boolean isGlobalSearch;
    private Boolean isRestrict;
    
    private Boolean isArchiveMonth = false;

    /**
     * 
     */
    public ArticleRestriction() {
        super(Article.class);
        setOrder(Order.asc("sortIndex"));
    }
    
    @Override 
    public void preBuildAssociation() {
        addAssociation("topic", "top", JoinType.INNER_JOIN);
        addAssociation("top.parent", "parSec", JoinType.LEFT_OUTER_JOIN);
        addAssociation("type", "typeArt", JoinType.INNER_JOIN); 
    }

    @Override
    public void preBuildSpecificCriteria() {
        
        if (artId != null && artId > 0) {
            addCriterion("id", artId);
        }
        
        if (Boolean.TRUE.equals(isChildrenOnly)) {
            addCriterion(Restrictions.and(Restrictions.isNotNull("parent.id"), Restrictions.gt("parent.id", 0L)));
        }
        
        if (StringUtils.isNotEmpty(artTitle)) {
            addCriterion("titleEn", StringFilterMode.CONTAINS, getArtTitle());
        }
        
        
        if (StringUtils.isNotEmpty(text)) {
    		String keyword = "%" + text + "%";
            addCriterion(Restrictions.or(
            			Restrictions.ilike("title", keyword),
            			Restrictions.ilike("subTitle", keyword),
            			Restrictions.ilike("shortDesc", keyword),
            			Restrictions.ilike("text", keyword)
            		));
        }
        
        if (keywords != null && keywords.length > 0) {
        	Criterion[] criterions = new Criterion[keywords.length]; 
        	for (int i = 0; i < keywords.length; i++) {
        		String keyword = "%" + keywords[i] + "%";
        		if (StringUtils.isNotEmpty(keyword)) {
		        	Criterion crit = Restrictions.or(
		        			Restrictions.ilike("title", keyword),
		        			Restrictions.ilike("subTitle", keyword),
		        			Restrictions.ilike("shortDesc", keyword),
		        			Restrictions.ilike("text", keyword));
		        	criterions[i] = crit;
        		}
        	}
        	
        	if (criterions.length > 0) {
        		addCriterion(Restrictions.or(criterions));
        	}
        }
        
        if (topArtId != null && topArtId > 0) {
            addCriterion("top.id", topArtId);
        } else if (topArtIdList != null && topArtIdList.size() > 0) {
        	addCriterion("top.id", FilterMode.IN, topArtIdList);
        }
        
        if (typArtId != null && typArtId > 0) {
            addCriterion("typeArt.id", typArtId);
        } else if (typArtIdList != null && typArtIdList.size() > 0) {
        	addCriterion("typeArt.id", FilterMode.IN, typArtIdList);
        }
        
        if (getIsRestrict() != null && getIsRestrict() == false) {
        	addCriterion("isRestrict", false);
        }
        
        // If have Archive Month
//        if (isArchiveMonth) {
//        	 int archiveNbMonths = CmsAppConfigFileHelper.getArchiveNbMonths();
//        	 Date dtArchiveMonth = DateUtils.addMonths(new Date(), -archiveNbMonths);
//        	 addCriterion("updateDate", StringFilterMode.LESS_THAN, dtArchiveMonth);
//        }
        
    }


    /**
     * @return the artTitle
     */
    public String getArtTitle() {
        return artTitle;
    }

    /**
     * @param artTitle the artTitle to set
     */
    public void setArtTitle(final String artTitle) {
        this.artTitle = artTitle;
    }

	/**
	 * @return the artId
	 */
	public Long getArtId() {
		return artId;
	}

	/**
	 * @param artId the artId to set
	 */
	public void setArtId(Long artId) {
		this.artId = artId;
	}

	/**
	 * @return the topArtId
	 */
	public Long getTopArtId() {
		return topArtId;
	}

	/**
	 * @param topArtId the topArtId to set
	 */
	public void setTopArtId(Long topArtId) {
		this.topArtId = topArtId;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the topArtIdList
	 */
	public List<Long> getTopArtIdList() {
		return topArtIdList;
	}

	/**
	 * @param topArtIdList the topArtIdList to set
	 */
	public void setTopArtIdList(List<Long> topArtIdList) {
		this.topArtIdList = topArtIdList;
	}


	/**
	 * @return the typArtId
	 */
	public Long getTypArtId() {
		return typArtId;
	}

	/**
	 * @param typArtId the typArtId to set
	 */
	public void setTypArtId(Long typArtId) {
		this.typArtId = typArtId;
	}

	/**
	 * @return the typArtIdList
	 */
	public List<Long> getTypArtIdList() {
		return typArtIdList;
	}

	/**
	 * @param typArtIdList the typArtIdList to set
	 */
	public void setTypArtIdList(List<Long> typArtIdList) {
		this.typArtIdList = typArtIdList;
	}

	/**
	 * @return the isChildrenOnly
	 */
	public Boolean getIsChildrenOnly() {
		return isChildrenOnly;
	}

	/**
	 * @param isChildrenOnly the isChildrenOnly to set
	 */
	public void setIsChildrenOnly(Boolean isChildrenOnly) {
		this.isChildrenOnly = isChildrenOnly;
	}

	/**
	 * @return the orderColumnIndex
	 */
	public Integer getOrderColumnIndex() {
		return orderColumnIndex;
	}

	/**
	 * @param orderColumnIndex the orderColumnIndex to set
	 */
	public void setOrderColumnIndex(Integer orderColumnIndex) {
		this.orderColumnIndex = orderColumnIndex;
	}

	/**
	 * @return the orderDirection
	 */
	public OrderDirection getOrderDirection() {
		return orderDirection;
	}

	/**
	 * @param orderDirection the orderDirection to set
	 */
	public void setOrderDirection(OrderDirection orderDirection) {
		this.orderDirection = orderDirection;
	}

	/**
	 * @return the keywords
	 */
	public String[] getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the isGlobalSearch
	 */
	public Boolean getIsGlobalSearch() {
		return isGlobalSearch;
	}

	/**
	 * @param isGlobalSearch the isGlobalSearch to set
	 */
	public void setIsGlobalSearch(Boolean isGlobalSearch) {
		this.isGlobalSearch = isGlobalSearch;
	}

	/**
	 * @return the isRestrict
	 */
	public Boolean getIsRestrict() {
		return isRestrict;
	}

	/**
	 * @param isRestrict the isRestrict to set
	 */
	public void setIsRestrict(Boolean isRestrict) {
		this.isRestrict = isRestrict;
	}

	/**
	 * @return the isArchiveMonth
	 */
	public Boolean getIsArchiveMonth() {
		return isArchiveMonth;
	}

	/**
	 * @param isArchiveMonth the isArchiveMonth to set
	 */
	public void setIsArchiveMonth(Boolean isArchiveMonth) {
		this.isArchiveMonth = isArchiveMonth;
	}
	
}

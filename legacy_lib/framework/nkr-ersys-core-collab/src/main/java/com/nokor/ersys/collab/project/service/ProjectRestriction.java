package com.nokor.ersys.collab.project.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.ersys.collab.project.model.EProjectCategory;
import com.nokor.ersys.collab.project.model.EProjectType;
import com.nokor.ersys.collab.project.model.Project;

/**
 * @author bunlong.taing
 */
public class ProjectRestriction extends BaseRestrictions<Project> {
	/** */
	private static final long serialVersionUID = 3027574215645011786L;
	
	private String text;
	private EProjectType type;
	private EProjectCategory category;
	private Date startDate;
	private Date endDate;

	public ProjectRestriction() {
		super(Project.class);
	}
	
	/**
	 * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
	 */
	@Override
	public void preBuildSpecificCriteria() {
		if (StringUtils.isNotEmpty(text)) {
    		addCriterion(Restrictions.or(
    					Restrictions.ilike(Project.CODE, text, MatchMode.ANYWHERE),
    					Restrictions.ilike(Project.DESC, text, MatchMode.ANYWHERE),
    					Restrictions.ilike(Project.DESCEN, text, MatchMode.ANYWHERE)));
    	}
		
		if (type != null) {
			addCriterion(Restrictions.eq(Project.TYPE, type));
		}
		
		if (category != null) {
			addCriterion(Restrictions.eq(Project.CATEGORY, category));
		}
		
		if (startDate != null) {
			addCriterion(Restrictions.ge(Project.STARTDATE, DateUtils.getDateAtBeginningOfDay(startDate)));
		}
		
		if (endDate != null) {
			addCriterion(Restrictions.le(Project.ENDDATE, DateUtils.getDateAtEndOfDay(endDate)));
		}
		addOrder(Order.asc(Project.ID));
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
	 * @return the type
	 */
	public EProjectType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EProjectType type) {
		this.type = type;
	}

	/**
	 * @return the category
	 */
	public EProjectCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(EProjectCategory category) {
		this.category = category;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}

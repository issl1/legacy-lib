package com.nokor.common.app.workflow.service;

import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.history.model.BaseHistoryItem;
import com.nokor.common.app.history.model.EHistoReason;
import com.nokor.common.app.workflow.model.WkfHistoryItem;

/**
 * @author prasnar
 * 
 */
public class WkfHistoryItemRestriction<T extends BaseHistoryItem> extends BaseRestrictions<T> {
	/** */
	private static final long serialVersionUID = -8237344580303357239L;

	private EMainEntity entity;
	private Long entityId;
    private String propertyName;
    private EHistoReason reason;
	private Date changeDate;
	private Date fromChangeDate;
	private Date toChangeDate;
    
    /**
     *
     */
    public WkfHistoryItemRestriction(Class<T> hisItemClazz) {
        super(hisItemClazz);
    }

    @Override
	public void preBuildAssociation() {
//    	addAssociation("wkfHistory", "his", JoinType.INNER_JOIN);
    	
	}

    
    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {

    	if (entity != null) {
    		addCriterion(Restrictions.eq(WkfHistoryItem.ENTITY, entity));
    	}
    	
    	if (entityId != null && entityId > 0) {
    		addCriterion(Restrictions.eq("entityId", entityId));
    	}
    	
    	if (propertyName != null) {
    		addCriterion(Restrictions.eq("propertyName", propertyName));
    	}
    	
    	if (reason != null) {
    		addCriterion(Restrictions.eq("reason", reason));
    	}
    	
    	if (changeDate != null) {
    		Date fromChangeDate = DateUtils.getDateAtBeginningOfDay(changeDate);
    		Date toChangeDate = DateUtils.getDateAtEndOfDay(changeDate);
            addCriterion("changeDate", FilterMode.BETWEEN, fromChangeDate, toChangeDate);
    	}
    	else if (fromChangeDate != null && toChangeDate != null) {
        	fromChangeDate = DateUtils.getDateAtBeginningOfDay(fromChangeDate);
        	toChangeDate = DateUtils.getDateAtEndOfDay(toChangeDate);
            addCriterion("changeDate", FilterMode.BETWEEN, fromChangeDate, toChangeDate);
        }
        else if (fromChangeDate != null) {
        	fromChangeDate = DateUtils.getDateAtBeginningOfDay(fromChangeDate);
            addCriterion("changeDate", FilterMode.GREATER_OR_EQUALS, fromChangeDate);
        }
        else if (toChangeDate != null) {
        	toChangeDate = DateUtils.getDateAtEndOfDay(toChangeDate);
            addCriterion("changeDate", FilterMode.LESS_OR_EQUALS, toChangeDate);
        }
    }
	/**
	 * @return the entity
	 */
	public EMainEntity getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(EMainEntity entity) {
		this.entity = entity;
	}

	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the changeDate
	 */
	public Date getChangeDate() {
		return changeDate;
	}

	/**
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	/**
	 * @return the fromChangeDate
	 */
	public Date getFromChangeDate() {
		return fromChangeDate;
	}

	/**
	 * @param fromChangeDate the fromChangeDate to set
	 */
	public void setFromChangeDate(Date fromChangeDate) {
		this.fromChangeDate = fromChangeDate;
	}

	/**
	 * @return the toChangeDate
	 */
	public Date getToChangeDate() {
		return toChangeDate;
	}

	/**
	 * @param toChangeDate the toChangeDate to set
	 */
	public void setToChangeDate(Date toChangeDate) {
		this.toChangeDate = toChangeDate;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the reason
	 */
	public EHistoReason getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(EHistoReason reason) {
		this.reason = reason;
	}

	
    
}

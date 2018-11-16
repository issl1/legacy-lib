package com.nokor.common.app.history;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.history.model.History;

/**
 * @author prasnar
 * @version $revision$
 */
public class HistoryRestriction extends BaseRestrictions<History> {
	private String entity;
    private String property;
    private Long entityId;
    private String reason;
    private String userCreation;
    private Date dateCreationMin;
    private Date dateCreationMax;

    /**
     * 
     */
    public HistoryRestriction() {
        super(History.class);
    }

    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {
        
    	if (StringUtils.isNotEmpty(getEntity())) {
    		addCriterion("entity", getEntity());
    	}
    	
    	if (StringUtils.isNotEmpty(getProperty())) {
    		addCriterion("property", getProperty());
    	}
    	
    	if (getEntityId() != null && getEntityId() > 0) {
            addCriterion("entityId", getEntityId());
        }
    	
    }

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
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
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}


	/**
	 * @return the userCreation
	 */
	public String getUserCreation() {
		return userCreation;
	}

	/**
	 * @param userCreation the userCreation to set
	 */
	public void setUserCreation(String userCreation) {
		this.userCreation = userCreation;
	}

	/**
	 * @return the dateCreationMin
	 */
	public Date getDateCreationMin() {
		return dateCreationMin;
	}

	/**
	 * @param dateCreationMin the dateCreationMin to set
	 */
	public void setDateCreationMin(Date dateCreationMin) {
		this.dateCreationMin = dateCreationMin;
	}

	/**
	 * @return the dateCreationMax
	 */
	public Date getDateCreationMax() {
		return dateCreationMax;
	}

	/**
	 * @param dateCreationMax the dateCreationMax to set
	 */
	public void setDateCreationMax(Date dateCreationMax) {
		this.dateCreationMax = dateCreationMax;
	}

    
}

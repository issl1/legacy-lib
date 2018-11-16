package com.nokor.frmk.security.service;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.frmk.security.model.ESecControlType;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.security.model.SecProfile;

/**
 * @author prasnar
 * 
 */
public class SecControlRestriction extends BaseRestrictions<SecControl> {
	/** */
	private static final long serialVersionUID = -8053594939567868823L;

	private Long appId;
	private String appCode;
	private String controlCode;
	private Long parentId;
	private Boolean parentOnly;
	private Long profileId;
	private Long groupId;
	private Boolean groupOnly;
	private ESecControlType type;
	
	/**
	 * 
	 */
    public SecControlRestriction() {
		super(SecControl.class);
	}

    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildAssociation()
     */
    public void preBuildAssociation() {
    	addAssociation(SecControl.APPLICATION);
    }
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
//       	addOrder(Order.asc(SecControl.CODE));
       	
    	if (appCode != null) {
			addCriterion(Restrictions.eq(SecControl.APPLICATION + DOT + SecControl.CODE, appCode));
		}
    	if (appId != null) {
			addCriterion(Restrictions.eq(SecControl.APPLICATION + DOT + SecControl.ID, appId));
		}
    	
    	if (controlCode != null) {
			addCriterion(Restrictions.eq(SecControl.CODE, controlCode));
		}
    	
    	if (parentId != null) {
        	addAssociation(SecControl.PARENT, "parent", JoinType.LEFT_OUTER_JOIN);
			addCriterion(Restrictions.eq("parent" + DOT + SecControl.ID, parentId));
    	} else if (Boolean.TRUE.equals(parentOnly)) {
        	addAssociation(SecControl.PARENT, "parent", JoinType.LEFT_OUTER_JOIN);
			addCriterion(Restrictions.isNull("parent"));
    	}
    	
    	if (groupId != null) {
        	addAssociation(SecControl.GROUP, "group", JoinType.LEFT_OUTER_JOIN);
			addCriterion(Restrictions.eq("group" + DOT + SecControl.ID, groupId));
    	} else if (Boolean.TRUE.equals(groupOnly)) {
        	addAssociation(SecControl.GROUP, "group", JoinType.LEFT_OUTER_JOIN);
			addCriterion(Restrictions.isNull("group"));
    	}
    	
    	if (type != null) {
			addCriterion(Restrictions.eq(SecControl.TYPE, type));
    	} 
    	
    	if (profileId != null) {
        	addAssociation(SecControl.CONTROLPROFILEPRIVILEGES, "ctlProPri", JoinType.LEFT_OUTER_JOIN);
        	addAssociation("ctlProPri" + DOT + SecControlProfilePrivilege.PROFILE, "pro", JoinType.LEFT_OUTER_JOIN);
			setDistinctRootEntity(true);
			addCriterion(Restrictions.eq("pro" + DOT + SecProfile.ID, profileId));
		}
	}


	/**
	 * @return the appId
	 */
	public Long getAppId() {
		return appId;
	}


	/**
	 * @param appId the appId to set
	 */
	public void setAppId(Long appId) {
		this.appId = appId;
	}


	/**
	 * @return the appCode
	 */
	public String getAppCode() {
		return appCode;
	}


	/**
	 * @param appCode the appCode to set
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	/**
	 * @return the controlCode
	 */
	public String getControlCode() {
		return controlCode;
	}

	/**
	 * @param controlCode the controlCode to set
	 */
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the parentOnly
	 */
	public Boolean getParentOnly() {
		return parentOnly;
	}

	/**
	 * @param parentOnly the parentOnly to set
	 */
	public void setParentOnly() {
		this.parentOnly = true;
	}

	/**
	 * @return the groupOnly
	 */
	public Boolean getGroupOnly() {
		return groupOnly;
	}

	/**
	 * @param groupOnly the groupOnly to set
	 */
	public void setGroupOnly() {
		this.groupOnly = true;
	}


	/**
	 * @return the profileId
	 */
	public Long getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the type
	 */
	public ESecControlType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ESecControlType type) {
		this.type = type;
	}


    
}

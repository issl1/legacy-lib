package com.nokor.common.app.eventlog;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.criteria.StringFilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.eventlog.model.SecEventLog;

/**
 * @author prasnar
 * @version $revision$
 */
public class SecAppEventRestriction extends BaseRestrictions<SecEventLog> {
	/**	 */
	private static final long serialVersionUID = -7397372241544700228L;

	//    private Long secUsrId;
    private String userName;
//    private String userLogin;
    private String remoteIPAddress;
    private Long appId;
    private List<Long> appIdList;
    private List<Long> typeIdList;
    private List<Long> modeIdList;
    private List<Long> levelIdList;
    private Date fromDate;
    private Date toDate;
    private String eventCode;
    private String eventDesc;
    private Boolean isAnonymous;


    /**
     * 
     */
    public SecAppEventRestriction() {
        super(SecEventLog.class);
    }
  

    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {
    	
        addAssociation("secApplication", "app", JoinType.INNER_JOIN);
        addAssociation("employee", "emp", JoinType.LEFT_OUTER_JOIN);
        addAssociation("emp.actor", "act", JoinType.LEFT_OUTER_JOIN);
//        addAssociation("secUser", "usr", JoinType.LEFT_OUTER_JOIN);
        addAssociation("typeSecEvent", "typ", JoinType.INNER_JOIN);
        addAssociation("modeSecEvent", "mod", JoinType.INNER_JOIN);
        addAssociation("levelSecEvent", "lev", JoinType.INNER_JOIN);

        
//        if (secUsrId != null && secUsrId > 0) {
//            addCriterion("usr.id", secUsrId);
//        }
//        if (StringUtils.isNotEmpty(userLogin)) {
//            addCriterion("usr.login", StringFilterMode.CONTAINS, userLogin);
//        }
        
        if (Boolean.TRUE.equals(isAnonymous)) {
        	addCriterion(Restrictions.isNull("emp"));
        } else if (Boolean.FALSE.equals(isAnonymous)) {
        	addCriterion(Restrictions.isNotNull("emp"));
        } else {
	        if (StringUtils.isNotEmpty(userName)) {
	            addCriterion(Restrictions.or(
	            		Restrictions.like("act.lastName", userName, MatchMode.ANYWHERE),
	            		Restrictions.like("act.firstName", userName, MatchMode.ANYWHERE),
	            		Restrictions.like("act.email", userName, MatchMode.ANYWHERE)));
	        }
        }
        
        if (StringUtils.isNotEmpty(remoteIPAddress)) {
            addCriterion("remoteIPAddress", StringFilterMode.CONTAINS, remoteIPAddress);
        }
        
        if (StringUtils.isNotEmpty(eventCode)) {
            addCriterion("eventCode", StringFilterMode.CONTAINS, eventCode);
        }
        if (StringUtils.isNotEmpty(eventDesc)) {
            addCriterion("eventDesc", StringFilterMode.CONTAINS, eventDesc);
        }

        if (fromDate != null && toDate != null) {
        	fromDate = DateUtils.getDateAtBeginningOfDay(fromDate);
        	toDate = DateUtils.getDateAtEndOfDay(toDate);
            addCriterion("evenDate", FilterMode.BETWEEN, fromDate, toDate);
        }
        else if (fromDate != null) {
        	fromDate = DateUtils.getDateAtBeginningOfDay(fromDate);
            addCriterion("evenDate", FilterMode.GREATER_OR_EQUALS, fromDate);
        }
        else if (toDate != null) {
        	toDate = DateUtils.getDateAtEndOfDay(toDate);
            addCriterion("evenDate", FilterMode.LESS_OR_EQUALS, toDate);
        }
        
        if (appIdList != null && appIdList.size() > 0) {
            Criterion c1 = Restrictions.in("app.id", appIdList);
            addCriterion(c1);
        }
        
        if (typeIdList != null && typeIdList.size() > 0) {
            Criterion c1 = Restrictions.in("typ.id", typeIdList);
            addCriterion(c1);
        }
       
        if(modeIdList != null && modeIdList.size() > 0) {
        	Criterion c1 = Restrictions.in("mod.id", modeIdList);
            addCriterion(c1);
        }
        
        if(levelIdList != null && levelIdList.size() > 0) {
        	Criterion c1 = Restrictions.in("lev.id", levelIdList);
            addCriterion(c1);
        }
       
    }

	/**
	 * @return the secUsrId
//	 */
//	public Long getSecUsrId() {
//		return secUsrId;
//	}
//
//	/**
//	 * @param secUsrId the secUsrId to set
//	 */
//	public void setSecUsrId(Long secUsrId) {
//		this.secUsrId = secUsrId;
//	}
//
//	/**
//	 * @return the userLogin
//	 */
//	public String getUserLogin() {
//		return userLogin;
//	}
//
//	/**
//	 * @param userLogin the userLogin to set
//	 */
//	public void setUserLogin(String userLogin) {
//		this.userLogin = userLogin;
//	}

	

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the typeIdList
	 */
	public List<Long> getTypeIdList() {
		return typeIdList;
	}

	/**
	 * @param typeIdList the typeIdList to set
	 */
	public void setTypeIdList(List<Long> typeIdList) {
		this.typeIdList = typeIdList;
	}

	/**
	 * @return the modeIdList
	 */
	public List<Long> getModeIdList() {
		return modeIdList;
	}

	/**
	 * @param modeIdList the modeIdList to set
	 */
	public void setModeIdList(List<Long> modeIdList) {
		this.modeIdList = modeIdList;
	}

	/**
	 * @return the appIdList
	 */
	public List<Long> getAppIdList() {
		return appIdList;
	}

	/**
	 * @param appIdList the appIdList to set
	 */
	public void setAppIdList(List<Long> appIdList) {
		this.appIdList = appIdList;
	}

	/**
	 * @return the levelIdList
	 */
	public List<Long> getLevelIdList() {
		return levelIdList;
	}

	/**
	 * @param levelIdList the levelIdList to set
	 */
	public void setLevelIdList(List<Long> levelIdList) {
		this.levelIdList = levelIdList;
	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the eventCode
	 */
	public String getEventCode() {
		return eventCode;
	}

	/**
	 * @param eventCode the eventCode to set
	 */
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	/**
	 * @return the eventDesc
	 */
	public String getEventDesc() {
		return eventDesc;
	}

	/**
	 * @param eventDesc the eventDesc to set
	 */
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	/**
	 * @return the remoteIPAddress
	 */
	public String getRemoteIPAddress() {
		return remoteIPAddress;
	}

	/**
	 * @param remoteIPAddress the remoteIPAddress to set
	 */
	public void setRemoteIPAddress(String remoteIPAddress) {
		this.remoteIPAddress = remoteIPAddress;
	}

	/**
	 * @return the isAnonymous
	 */
	public Boolean getIsAnonymous() {
		return isAnonymous;
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
	 * @param isAnonymous the isAnonymous to set
	 */
	public void setIsAnonymous(Boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}


    
}

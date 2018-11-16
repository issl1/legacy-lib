package com.nokor.frmk.security.sys.service;

import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.frmk.security.sys.model.SecSession;

/**
 * @author prasnar
 * 
 */
public class SecSessionRestriction extends BaseRestrictions<SecSession> {
	/** */
	private static final long serialVersionUID = 1617897569235487215L;

    private String sortCriteria = null;
    private Long usrId;
    private Date lastHeartbeatTS;
    private Date startTS;
    
	/**
	 * 
	 */
    public SecSessionRestriction() {
		super(SecSession.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
       	

        if (lastHeartbeatTS != null) {
    		addCriterion(Restrictions.eq(SecSession.LASTHEARTBEATTS, DateUtils.date2StringDB(lastHeartbeatTS)));
        }

        if (usrId != null) {
    		addCriterion(Restrictions.eq(SecSession.USRID, usrId));
        }

        if (startTS != null) {
    		addCriterion(Restrictions.eq(SecSession.STARTTS, DateUtils.date2StringDB(lastHeartbeatTS)));
        }
		
	}


	/**
	 * @return the sortCriteria
	 */
	public String getSortCriteria() {
		return sortCriteria;
	}


	/**
	 * @param sortCriteria the sortCriteria to set
	 */
	public void setSortCriteria(String sortCriteria) {
		this.sortCriteria = sortCriteria;
	}


	/**
	 * @return the usrId
	 */
	public Long getUsrId() {
		return usrId;
	}


	/**
	 * @param usrId the usrId to set
	 */
	public void setUsrId(Long usrId) {
		this.usrId = usrId;
	}


	/**
	 * @return the lastHeartbeatTS
	 */
	public Date getLastHeartbeatTS() {
		return lastHeartbeatTS;
	}


	/**
	 * @param lastHeartbeatTS the lastHeartbeatTS to set
	 */
	public void setLastHeartbeatTS(Date lastHeartbeatTS) {
		this.lastHeartbeatTS = lastHeartbeatTS;
	}


	/**
	 * @return the startTS
	 */
	public Date getStartTS() {
		return startTS;
	}


	/**
	 * @param startTS the startTS to set
	 */
	public void setStartTS(Date startTS) {
		this.startTS = startTS;
	}

	
			
}

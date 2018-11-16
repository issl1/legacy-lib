package com.nokor.frmk.security.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.frmk.security.model.SecProfile;

/**
 * 
 * @author prasnar
 *
 */
public class SecProfileRestriction extends BaseRestrictions<SecProfile> {
	/** */
	private static final long serialVersionUID = -6406423914310772482L;

	private Boolean fetchOnlyNormalProfiles;
	
	/**
	 * 
	 */
    public SecProfileRestriction() {
		super(SecProfile.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
 
    	if (Boolean.TRUE.equals(fetchOnlyNormalProfiles)) {
    		addCriterion(Restrictions.ne(SecProfile.ID, SecProfile.ADMIN.getId()));
    	}
    	
    	
		
	}


	/**
	 * @return the fetchOnlyNormalProfiles
	 */
	public Boolean getFetchOnlyNormalProfiles() {
		return fetchOnlyNormalProfiles;
	}


	/**
	 * @param fetchOnlyNormalProfiles the fetchOnlyNormalProfiles to set
	 */
	public void setFetchOnlyNormalProfiles() {
		this.fetchOnlyNormalProfiles = true;
	}

	
	

}

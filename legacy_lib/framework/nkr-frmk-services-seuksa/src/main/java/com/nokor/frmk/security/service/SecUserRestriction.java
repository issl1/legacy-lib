package com.nokor.frmk.security.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
public class SecUserRestriction extends BaseRestrictions<SecUser> {
	/** */
	private static final long serialVersionUID = 3197352599696395704L;
	
	private String login;
	
	/**
	 * 
	 */
    public SecUserRestriction() {
		super(SecUser.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
 
    	if (StringUtils.isNotEmpty(login)) {
    		addCriterion(Restrictions.eq("login", login));
    	}
    	
    	
		
	}

	
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}


}

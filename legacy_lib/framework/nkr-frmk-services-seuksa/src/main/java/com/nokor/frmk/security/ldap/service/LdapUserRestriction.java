package com.nokor.frmk.security.ldap.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.frmk.security.ldap.model.LdapUser;

/**
 * 
 * @author prasnar
 *
 */
public class LdapUserRestriction extends BaseRestrictions<LdapUser> {
	/** */
	private static final long serialVersionUID = -1273781235272275854L;

	private String login;
	
	/**
	 * 
	 */
    public LdapUserRestriction() {
		super(LdapUser.class);
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

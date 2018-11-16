package com.nokor.ersys.core.hr.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.security.model.SecUser;

/**
 * @author prasnar
 * 
 */
public class EmployeeRestriction extends BaseRestrictions<Employee> {
	/** */
	private static final long serialVersionUID = -1882760470706823122L;

    private String login;
    private Long secUsrId;
	private String name;
	
	/**
	 * 
	 */
    public EmployeeRestriction() {
		super(Employee.class);
	}

    /**
     * 
     */
    @Override
    public void preBuildAssociation() {
    	addAssociation(Employee.SECUSER, JoinType.LEFT_OUTER_JOIN);
    }
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
       	addOrder(Order.asc(Employee.LASTNAME));
    	if (name != null) {
    		addCriterion(Restrictions.or(
    				Restrictions.ilike(Employee.LASTNAME, name), 
    				Restrictions.ilike(Employee.LASTNAMEEN, name),
    				Restrictions.ilike(Employee.FIRSTNAME, name),
    				Restrictions.ilike(Employee.FIRSTNAMEEN, name))
    				);
		}
		
    	
    	if (StringUtils.isNotEmpty(login)) {
    		addCriterion(Restrictions.ilike(Employee.SECUSER + DOT + SecUser.LOGIN, login));
    	}
    	
    	if (secUsrId != null && secUsrId > 0) {
            addCriterion(Employee.SECUSER + DOT + SecUser.ID, secUsrId);
        }
    	
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * @return the secUsrId
	 */
	public Long getSecUsrId() {
		return secUsrId;
	}

	/**
	 * @param secUsrId the secUsrId to set
	 */
	public void setSecUsrId(Long secUsrId) {
		this.secUsrId = secUsrId;
	}



	
}

package com.nokor.frmk.security.service;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecControlPrivilege;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;

/**
 * @author prasnar
 * 
 */
public class SecControlPrivilegeRestriction extends BaseRestrictions<SecControlPrivilege> {
	/** */
	private static final long serialVersionUID = -5495911788309811461L;

	private SecControl control;
	
	/**
	 * 
	 */
    public SecControlPrivilegeRestriction() {
		super(SecControlPrivilege.class);
	}

    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildAssociation()
     */
    public void preBuildAssociation() {
    	addAssociation(SecControlProfilePrivilege.CONTROL, "ctl", JoinType.INNER_JOIN);
    }
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (control != null) {
			addCriterion(Restrictions.eq("ctl" + DOT + SecControl.ID, control.getId()));
		}
    	
	}

	/**
	 * @return the control
	 */
	public SecControl getControl() {
		return control;
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(SecControl control) {
		this.control = control;
	}


	
}

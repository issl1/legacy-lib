package com.nokor.efinance.core.contract.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.Appointment;
import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author uhout.cheng
 */
public class AppointmentRestriction extends BaseRestrictions<Appointment> {

	/** */
	private static final long serialVersionUID = 8042189264773306367L;

	private String contractNo;
	
	/**
	 * 
	 */
    public AppointmentRestriction() {
		super(Appointment.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (StringUtils.isNotEmpty(contractNo)) {
    		addAssociation(Contract.class, "CON", JoinType.INNER_JOIN);
    		addCriterion(Restrictions.eq("CON" + DOT + Contract.REFERENCE, contractNo));
    	}
	}

	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}


	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

}

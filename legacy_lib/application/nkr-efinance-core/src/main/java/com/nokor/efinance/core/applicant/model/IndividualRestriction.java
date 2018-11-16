package com.nokor.efinance.core.applicant.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

/**
 * 
 * @author uhout.cheng
 */
public class IndividualRestriction extends BaseRestrictions<Individual> implements MApplicant {
	
	/** */
	private static final long serialVersionUID = -4179675046813959383L;
	
	private String idNumber;
    private String firstName;
	private String lastName;
	private Date birthDate;
	
	/**
	 * 
	 */
    public IndividualRestriction() {
		super(Individual.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (StringUtils.isNotEmpty(idNumber)) {
    		addCriterion(Restrictions.ilike(IDNUMBER, idNumber));
    	}
    	if (StringUtils.isNotEmpty(firstName)) {
    		addCriterion(Restrictions.or(
					Restrictions.ilike(FIRSTNAME, firstName, MatchMode.ANYWHERE),
					Restrictions.ilike(FIRSTNAMEEN, firstName, MatchMode.ANYWHERE))
					);
    	}
    	if (StringUtils.isNotEmpty(lastName)) {
    		addCriterion(Restrictions.or(
					Restrictions.ilike(LASTNAME, lastName, MatchMode.ANYWHERE),
					Restrictions.ilike(LASTNAMEEN, lastName, MatchMode.ANYWHERE))
					);
    	}
    	if (birthDate != null) {
    		addCriterion(Restrictions.ge(BIRTHDATE, DateUtils.getDateAtBeginningOfDay(birthDate)));
    		addCriterion(Restrictions.le(BIRTHDATE, DateUtils.getDateAtEndOfDay(birthDate)));
		}
	}

	/**
	 * @return the idNumber
	 */
	public String getIdNumber() {
		return idNumber;
	}

	/**
	 * @param idNumber the idNumber to set
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
}

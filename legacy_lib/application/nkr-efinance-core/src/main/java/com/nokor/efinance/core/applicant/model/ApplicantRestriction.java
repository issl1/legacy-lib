package com.nokor.efinance.core.applicant.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.ersys.core.hr.model.organization.MBasePerson;
import com.nokor.ersys.core.hr.model.organization.MOrganization;

/**
 * 
 * @author uhout.cheng
 */
public class ApplicantRestriction extends BaseRestrictions<Applicant> implements MApplicant {
	
	/** */
	private static final long serialVersionUID = -3014902270006368924L;
	
	private static final String IND = "ind";
	private static final String COM = "com";
	private static final String DOT = ".";
	
	private String applicantID;
	private Date createDate;
	private String status;
	private String idNumber;
	private String phoneNumber;
	private EApplicantCategory applicantCategory = EApplicantCategory.INDIVIDUAL;
	private Long individualId;
	private Long companyId;
	private String nickName;
	private String firstName;
	private String lastName;
	private String companyName;

	/**
	 * 
	 */
    public ApplicantRestriction() {
		super(Applicant.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (applicantCategory != null) {
    		addCriterion(Restrictions.eq(MApplicant.APPLICANTCATEGORY, applicantCategory));
    	}
    	if (EApplicantCategory.INDIVIDUAL.equals(applicantCategory) || EApplicantCategory.GLSTAFF.equals(applicantCategory)) {
    		addAssociation(INDIVIDUAL, IND, JoinType.INNER_JOIN);
    		if (StringUtils.isNotEmpty(applicantID)) {
        		addCriterion(Restrictions.ilike(IND + DOT + REFERENCE, applicantID, MatchMode.ANYWHERE));
        	}
    		if (StringUtils.isNotEmpty(idNumber)) {
        		addCriterion(Restrictions.ilike(IND + DOT + IDNUMBER, idNumber, MatchMode.ANYWHERE));
        	}
    		if (StringUtils.isNotEmpty(phoneNumber)) {
        		addCriterion(Restrictions.ilike(IND + DOT + MBasePerson.MOBILEPERSO, phoneNumber, MatchMode.ANYWHERE));
        	}
    		if (individualId != null) {
    			addCriterion(Restrictions.eq(IND + DOT + ID, individualId));
    		}
    		if (StringUtils.isNotEmpty(nickName)) {
    			addCriterion(Restrictions.ilike(IND + DOT + Individual.NICKNAME, nickName, MatchMode.ANYWHERE));
    		}
    		if (StringUtils.isNotEmpty(firstName)) {
    			addCriterion(Restrictions.or(Restrictions.ilike(IND + DOT + Individual.FIRSTNAME, firstName, MatchMode.ANYWHERE),
    					Restrictions.ilike(IND + DOT + Individual.FIRSTNAMEEN, firstName, MatchMode.ANYWHERE)));
    		}
    		if (StringUtils.isNotEmpty(lastName)) {
    			addCriterion(Restrictions.or(Restrictions.ilike(IND + DOT + Individual.LASTNAME, lastName, MatchMode.ANYWHERE),
    					Restrictions.ilike(IND + DOT + Individual.LASTNAMEEN, lastName, MatchMode.ANYWHERE)));
    		}
		} else if (EApplicantCategory.COMPANY.equals(applicantCategory)) {
			addAssociation(COMPANY, COM, JoinType.INNER_JOIN);
			if (StringUtils.isNotEmpty(phoneNumber)) {
        		addCriterion(Restrictions.ilike(COM + DOT + MOBILE, phoneNumber, MatchMode.ANYWHERE));
        	}
			if (companyId != null) {
    			addCriterion(Restrictions.eq(COM + DOT + ID, companyId));
    		}
			if (StringUtils.isNotEmpty(companyName)) {
				addCriterion(Restrictions.or(Restrictions.ilike(COM + DOT + MOrganization.NAME, companyName, MatchMode.ANYWHERE),
						Restrictions.ilike(COM + DOT + MOrganization.NAMEEN, companyName, MatchMode.ANYWHERE)));
			}
		}
    	if (createDate != null) {
    		addCriterion(Restrictions.ge(CREATEDATE, DateUtils.getDateAtBeginningOfDay(createDate)));
    		addCriterion(Restrictions.le(CREATEDATE, DateUtils.getDateAtEndOfDay(createDate)));
		}
	}

	/**
	 * @return the applicantID
	 */
	public String getApplicantID() {
		return applicantID;
	}

	/**
	 * @param applicantID the applicantID to set
	 */
	public void setApplicantID(String applicantID) {
		this.applicantID = applicantID;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the applicantCategory
	 */
	public EApplicantCategory getApplicantCategory() {
		return applicantCategory;
	}

	/**
	 * @param applicantCategory the applicantCategory to set
	 */
	public void setApplicantCategory(EApplicantCategory applicantCategory) {
		this.applicantCategory = applicantCategory;
	}

	/**
	 * @return the individualId
	 */
	public Long getIndividualId() {
		return individualId;
	}

	/**
	 * @param individualId the individualId to set
	 */
	public void setIndividualId(Long individualId) {
		this.individualId = individualId;
	}
	
	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
}

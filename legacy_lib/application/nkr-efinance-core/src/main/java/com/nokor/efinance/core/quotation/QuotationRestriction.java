package com.nokor.efinance.core.quotation;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.MApplicant;
import com.nokor.efinance.core.asset.model.MAsset;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.dealer.model.MDealer;
import com.nokor.efinance.core.quotation.model.MQuotation;
import com.nokor.efinance.core.quotation.model.MQuotationApplicant;
import com.nokor.efinance.core.quotation.model.Quotation;


/**
 * 
 * @author prasnar
 *
 */
public class QuotationRestriction extends BaseRestrictions<Quotation> implements MQuotation, MContract, MQuotationApplicant, MApplicant, MDealer, MAsset {
	/** */
	private static final long serialVersionUID = 2760517786996115736L;

	private static final  String DOT = ".";
	private static final  String CON = "con";
	private static final  String QUO_APP = "quoApp";
	private static final  String APP = "app";
	private static final  String DEA = "dea";
	private static final  String ASS = "ass";
	
	private EApplicantType applicantType = EApplicantType.C;
	
	private String applicantID;
	private String contractID;
	private String applicationID;

	private String lastName;
	private String firstName;
	private String nickName;
	private String idNumber;
	private Date birthDate;
	private String phoneNumber;
	private String companyCode;
	
	private String plateNumber;
	private String chassisNumber;
	private String engineNumber;
	
//	private Double amtInstallment;
	private String dealerName;
	private EWkfStatus contractStatus;
	private Date activationDate;
	
	/**
	 * 
	 */
    public QuotationRestriction() {
		super(Quotation.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	addAssociation("contract", CON, JoinType.LEFT_OUTER_JOIN);
       	addAssociation("dealer", DEA, JoinType.INNER_JOIN);
       	addAssociation("asset", ASS, JoinType.INNER_JOIN);

       	if (applicantType == EApplicantType.G) {
       		addAssociation("quotationApplicants", QUO_APP, JoinType.INNER_JOIN);
           	addAssociation("quotationApplicants.applicant", APP, JoinType.INNER_JOIN);
			addCriterion(Restrictions.eq(QUO_APP + DOT + MQuotationApplicant.APPLICANTTYPE, applicantType));
		} else {
			addAssociation("applicant", APP, JoinType.INNER_JOIN);
		}
       	
       	if (contractStatus != null) {
			addCriterion(Restrictions.eq(CON + DOT + MContract.WKFSTATUS, contractStatus));
			
		}
       	
    	if (StringUtils.isNotEmpty(applicantID)) {
			addCriterion(Restrictions.ilike(APP + DOT + MApplicant.REFERENCE, applicantID, MatchMode.ANYWHERE));
		}

    	if (StringUtils.isNotEmpty(contractID)) {
			addCriterion(Restrictions.ilike(QUO_APP + DOT + MQuotation.REFERENCE, contractID, MatchMode.ANYWHERE));
		}

    	if (StringUtils.isNotEmpty(applicationID)) {
			addCriterion(Restrictions.ilike(MQuotation.EXTERNALREFERENCE, applicationID, MatchMode.ANYWHERE));
		}
    	
    	if (StringUtils.isNotEmpty(lastName)) {
			addCriterion(Restrictions.or(
					Restrictions.ilike(APP + DOT + LASTNAME, lastName, MatchMode.ANYWHERE),
					Restrictions.ilike(APP + DOT + LASTNAMEEN, lastName, MatchMode.ANYWHERE))
					);
		}
    	
    	if (StringUtils.isNotEmpty(firstName)) {
			addCriterion(Restrictions.or(
					Restrictions.ilike(APP + DOT + FIRSTNAME, firstName, MatchMode.ANYWHERE),
					Restrictions.ilike(APP + DOT + FIRSTNAMEEN, firstName, MatchMode.ANYWHERE))
					);
		}
    	
    	if (StringUtils.isNotEmpty(nickName)) {
			addCriterion(Restrictions.ilike(APP + DOT + NICKNAME, nickName, MatchMode.ANYWHERE));
		}
    	
    	if (StringUtils.isNotEmpty(idNumber)) {
			addCriterion(Restrictions.ilike(APP + DOT + IDNUMBER, idNumber, MatchMode.ANYWHERE));
		}
    	
    	if (birthDate != null) {
			addCriterion(Restrictions.eq(APP + DOT + BIRTHDATE, DateUtils.date2StringDB_YYYYMMDD(birthDate)));
		}
    	
    	// in Applicant.ApplicantContactInfo.ContactInfo + ETypeContactInfo(LANDLINE, TEL..) ??
//    	if (phoneNumber != null) {
//    	PhoneNumberUtils
//    	phoneNumber.trim().replace(" ", "").replace("/", "").replace("-", "");
//    	http://stackoverflow.com/questions/5401714/search-phone-numbers-in-database-ignoring-special-characters
//			addCriterion(Restrictions.ilike(APP + , applicationID, MatchMode.ANYWHERE));
//		}
    	
    	// company employmnt
//    	if (companyCode != null) {
//			addCriterion(Restrictions.ilike(APP + , companyCode, MatchMode.ANYWHERE));
//		}
    	
    	if (StringUtils.isNotEmpty(dealerName)) {
			addCriterion(Restrictions.or(
					Restrictions.ilike(DEA + DOT + MDealer.NAME, dealerName, MatchMode.ANYWHERE),
					Restrictions.ilike(DEA + DOT + MDealer.NAMEEN, dealerName, MatchMode.ANYWHERE),
					Restrictions.ilike(DEA + DOT + MDealer.CODE, dealerName, MatchMode.ANYWHERE))
					);
    	}
	}
    
    

	/**
	 * @return the applicantType
	 */
	public EApplicantType getApplicantType() {
		return applicantType;
	}


	/**
	 * @param applicantType the applicantType to set
	 */
	public void setApplicantType(EApplicantType applicantType) {
		this.applicantType = applicantType;
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
	 * @return the contractID
	 */
	public String getContractID() {
		return contractID;
	}

	/**
	 * @param contractID the contractID to set
	 */
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}

	/**
	 * @return the applicationID
	 */
	public String getApplicationID() {
		return applicationID;
	}

	/**
	 * @param applicationID the applicationID to set
	 */
	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
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
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	/**
	 * @return the plateNumber
	 */
	public String getPlateNumber() {
		return plateNumber;
	}

	/**
	 * @param plateNumber the plateNumber to set
	 */
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	/**
	 * @return the chassisNumber
	 */
	public String getChassisNumber() {
		return chassisNumber;
	}

	/**
	 * @param chassisNumber the chassisNumber to set
	 */
	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	/**
	 * @return the engineNumber
	 */
	public String getEngineNumber() {
		return engineNumber;
	}

	/**
	 * @param engineNumber the engineNumber to set
	 */
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

//	/**
//	 * @return the amtInstallment
//	 */
//	public Double getAmtInstallment() {
//		return amtInstallment;
//	}
//
//	/**
//	 * @param amtInstallment the amtInstallment to set
//	 */
//	public void setAmtInstallment(Double amtInstallment) {
//		this.amtInstallment = amtInstallment;
//	}

	/**
	 * @return the dealerName
	 */
	public String getDealerName() {
		return dealerName;
	}

	/**
	 * @param dealerName the dealerName to set
	 */
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	/**
	 * @return the contractStatus
	 */
	public EWkfStatus getContractStatus() {
		return contractStatus;
	}

	/**
	 * @param contractStatus the contractStatus to set
	 */
	public void setContractStatus(EWkfStatus contractStatus) {
		this.contractStatus = contractStatus;
	}

	/**
	 * @return the activationDate
	 */
	public Date getActivationDate() {
		return activationDate;
	}

	/**
	 * @param activationDate the activationDate to set
	 */
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	
	
}

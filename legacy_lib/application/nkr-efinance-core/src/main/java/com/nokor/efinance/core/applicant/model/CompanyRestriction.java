package com.nokor.efinance.core.applicant.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.organization.MOrganization;

/**
 * 
 * @author uhout.cheng
 */
public class CompanyRestriction extends BaseRestrictions<Company> implements MOrganization {
	
	/** */
	private static final long serialVersionUID = -7889888823443755474L;
	
	private String code;
    private String name;
    private String externalCode;
	private String vatRegistrationNo;
	private String licenceNo;
    
	/**
	 * 
	 */
    public CompanyRestriction() {
		super(Company.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (StringUtils.isNotEmpty(code)) {
    		addCriterion(Restrictions.ilike(CODE, code));
    	}
    	if (StringUtils.isNotEmpty(name)) {
    		addCriterion(Restrictions.or(
					Restrictions.ilike(NAME, name, MatchMode.ANYWHERE),
					Restrictions.ilike(NAMEEN, name, MatchMode.ANYWHERE))
					);
    	}
    	if (StringUtils.isNotEmpty(externalCode)) {
    		addCriterion(Restrictions.ilike(EXTERNALCODE, externalCode));
    	}
    	if (StringUtils.isNotEmpty(vatRegistrationNo)) {
    		addCriterion(Restrictions.ilike(VATREGISTRATIONNO, vatRegistrationNo));
    	}
    	if (StringUtils.isNotEmpty(licenceNo)) {
    		addCriterion(Restrictions.ilike(LICENCENO, licenceNo));
    	}
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return the externalCode
	 */
	public String getExternalCode() {
		return externalCode;
	}

	/**
	 * @param externalCode the externalCode to set
	 */
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	/**
	 * @return the vatRegistrationNo
	 */
	public String getVatRegistrationNo() {
		return vatRegistrationNo;
	}

	/**
	 * @param vatRegistrationNo the vatRegistrationNo to set
	 */
	public void setVatRegistrationNo(String vatRegistrationNo) {
		this.vatRegistrationNo = vatRegistrationNo;
	}

	/**
	 * @return the licenceNo
	 */
	public String getLicenceNo() {
		return licenceNo;
	}

	/**
	 * @param licenceNo the licenceNo to set
	 */
	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}
}

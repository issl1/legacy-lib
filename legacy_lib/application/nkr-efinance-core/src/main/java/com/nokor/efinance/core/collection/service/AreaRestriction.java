package com.nokor.efinance.core.collection.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColType;

/**
 * 
 * @author uhout.cheng
 */
public class AreaRestriction extends BaseRestrictions<Area> {
	
	private Long areaId;
	private Long provinceId;
	private Long districtId;
	private Long communeId;
	private String postalCode;
	private EColType colType;
	private String code;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9029972862257200359L;

	/**
	 */
    public AreaRestriction() {
		super(Area.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (areaId != null) {
    		addCriterion(Restrictions.eq(Area.ID, areaId));
    	}
    	if (provinceId != null) {
    		addCriterion(Restrictions.eq("province.id", provinceId));
    	}
    	if (districtId != null) {
    		addCriterion(Restrictions.eq("district.id", districtId));
    	}
    	if (communeId != null) {
    		addCriterion(Restrictions.eq("commune.id", communeId));
    	}
    	if (StringUtils.isNotBlank(postalCode)) {
    		addCriterion(Restrictions.eq("postalCode", postalCode));
    	}
    	if (colType != null) {
    		addCriterion(Restrictions.eq("colType", colType));
    	}
    	if (code != null) {
    		addCriterion(Restrictions.eq("code", code));
    	}
	}

	/**
	 * @return the areaId
	 */
	public Long getAreaId() {
		return areaId;
	}

	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	/**
	 * @return the provinceId
	 */
	public Long getProvinceId() {
		return provinceId;
	}

	/**
	 * @param provinceId the provinceId to set
	 */
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	/**
	 * @return the districtId
	 */
	public Long getDistrictId() {
		return districtId;
	}

	/**
	 * @param districtId the districtId to set
	 */
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	/**
	 * @return the communeId
	 */
	public Long getCommuneId() {
		return communeId;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return code;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.code = desc;
	}

	/**
	 * @param communeId the communeId to set
	 */
	public void setCommuneId(Long communeId) {
		this.communeId = communeId;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the colType
	 */
	public EColType getColType() {
		return colType;
	}

	/**
	 * @param colType the colType to set
	 */
	public void setColType(EColType colType) {
		this.colType = colType;
	}
}

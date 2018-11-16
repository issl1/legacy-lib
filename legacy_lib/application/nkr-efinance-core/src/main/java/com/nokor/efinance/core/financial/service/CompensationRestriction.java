package com.nokor.efinance.core.financial.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.financial.model.ManufacturerCompensation;
import com.nokor.efinance.core.shared.FMEntityField;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class CompensationRestriction extends BaseRestrictions<ManufacturerCompensation> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 5741491677620173752L;

	private Long brandId;
	private Long modelId;
	
	public CompensationRestriction() {
		super(ManufacturerCompensation.class);
	}
	
	@Override
	public void preBuildSpecificCriteria() {
		if (brandId != null) {
			addCriterion(Restrictions.eq(ASSET_MAKE + "." + ID, brandId));
		}
		if (modelId != null) {
			addCriterion(Restrictions.eq(ASSET_MODEL + "." + ID, modelId));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getBrandId() {
		return brandId;
	}
	
	/**
	 * 
	 * @param brandId
	 */
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getModelId() {
		return modelId;
	}
	
	/**
	 * 
	 * @param modelId
	 */
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

}

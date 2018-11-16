package com.nokor.efinance.core.asset.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;

/**
 * 
 * @author prasnar
 *
 */
public class AssetModelRestriction extends BaseRestrictions<AssetModel> {
	/** */
	private static final long serialVersionUID = 3197352599696395704L;
	
	private Long braId;
	private Long modelId;
	private AssetRange assetRange;
	private AssetCategory assetCategory;
	private String code;
	private String serie;
	private String desc;
	
	/**
	 * 
	 */
    public AssetModelRestriction() {
		super(AssetModel.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (StringUtils.isNotEmpty(code)) {
    		addCriterion(Restrictions.ilike(AssetModel.CODE, code, MatchMode.ANYWHERE));
    	}
    	if (assetRange != null) {
    		addCriterion(Restrictions.eq(AssetModel.ASSETRANGE, assetRange));
    	} else if (modelId != null) {
			addCriterion(Restrictions.eq(AssetModel.ASSETRANGE + DOT + AssetRange.ID, modelId));
		}
    	if (braId != null) {
    		addAssociation("assetRange", "asr", JoinType.INNER_JOIN);
    		addCriterion(Restrictions.eq("asr.assetMake.id", braId));
    	}
    	if (assetCategory != null) {
    		addCriterion(Restrictions.eq(AssetModel.ASSETCATEGORY, assetCategory));
    	}
    	if (StringUtils.isNotEmpty(serie)) {
    		addCriterion(Restrictions.ilike(AssetModel.SERIE, serie, MatchMode.ANYWHERE));
    	}
    	if (StringUtils.isNotEmpty(desc)) {
    		addCriterion(Restrictions.or(Restrictions.ilike(AssetModel.DESC, desc, MatchMode.ANYWHERE),
					Restrictions.ilike(AssetModel.DESCEN, desc, MatchMode.ANYWHERE)));
    	}
	}

	/**
	 * @return the braId
	 */
	public Long getBraId() {
		return braId;
	}

	/**
	 * @param braId the braId to set
	 */
	public void setBraId(Long braId) {
		this.braId = braId;
	}

	/**
	 * @return the assetRange
	 */
	public AssetRange getAssetRange() {
		return assetRange;
	}

	/**
	 * @param assetRange the assetRange to set
	 */
	public void setAssetRange(AssetRange assetRange) {
		this.assetRange = assetRange;
	}
	
	/**
	 * @return the serie
	 */
	public String getSerie() {
		return serie;
	}

	/**
	 * @param serie the serie to set
	 */
	public void setSerie(String serie) {
		this.serie = serie;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
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
	 * @return the assetCategory
	 */
	public AssetCategory getAssetCategory() {
		return assetCategory;
	}

	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(AssetCategory assetCategory) {
		this.assetCategory = assetCategory;
	}

	/**
	 * @return the modelId
	 */
	public Long getModelId() {
		return modelId;
	}

	/**
	 * @param modelId the modelId to set
	 */
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	
}

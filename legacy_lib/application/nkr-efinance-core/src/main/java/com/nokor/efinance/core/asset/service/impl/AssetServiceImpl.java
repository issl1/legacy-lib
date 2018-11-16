package com.nokor.efinance.core.asset.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.nokor.efinance.core.asset.dao.AssetDao;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.MAsset;
import com.nokor.efinance.core.asset.service.AssetService;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.service.ContractFlagRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.exception.ValidationFields;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;
import com.nokor.efinance.core.shared.system.DomainType;

/**
 * Asset service
 * @author ly.youhort
 *
 */
@Service("assetService")
public class AssetServiceImpl extends BaseEntityServiceImpl implements AssetService, MAsset {
	/** */
	private static final long serialVersionUID = -1027766663950218887L;

	@Autowired
    private AssetDao dao;
	
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public AssetDao getDao() {
		return dao;
	}


	@Override
	public Asset saveOrUpdateAsset(Asset asset) {
		Assert.notNull(asset, "Asset could not be null.");
		super.saveOrUpdate(asset);
		return asset;
	}

	/**
	 * Control asset
	 * @param asset
	 */
	@Override
	public void checkAsset(Asset asset) throws ValidationFieldsException {
		ValidationFields validationFields = new ValidationFields();		
		
		validationFields.addRequired(asset.getModel() == null, DomainType.ASS, "field.required.1", I18N.message("asset.model"));
		validationFields.addRequired(asset.getTiAssetPrice() == null || asset.getTiAssetPrice() <= 0, DomainType.ASS, "field.required.1", I18N.message("asset.price"));
		
		if (!validationFields.getErrorMessages().isEmpty()) {
			throw new ValidationFieldsException(validationFields.getErrorMessages());
		}
	}
	
	/**
	 * @param chassisNumber
	 * @return
	 */
	public List<Asset> getAssetsByChassisNumber(String chassisNumber) {
		BaseRestrictions<Asset> restrictions = new BaseRestrictions<>(Asset.class);
		restrictions.addCriterion(Restrictions.eq(CHASSISNUMBER, chassisNumber));
		return list(restrictions);
	}
	
	/**
	 * @param enginNumber
	 * @return
	 */
	public List<Asset> getAssetsByEnginNumber(String enginNumber) {
		BaseRestrictions<Asset> restrictions = new BaseRestrictions<>(Asset.class);
		restrictions.addCriterion(Restrictions.eq(ENGINENUMBER, enginNumber));
		return list(restrictions);
	}
	
	/**
	 * @param enginNumber
	 * @param asset
	 * @return
	 */
	public boolean isChassisNumberExist(String chassisNumber, Asset asset) {
		BaseRestrictions<Asset> restrictions = new BaseRestrictions<>(Asset.class);
		restrictions.addCriterion(Restrictions.eq(CHASSISNUMBER, chassisNumber));
		if (asset != null && asset.getId() != null) {
			restrictions.addCriterion(Restrictions.ne(ID, asset.getId()));
		}
		List<Asset> assets = list(restrictions);
		return assets != null && !assets.isEmpty();
	}
	
	/**
	 * @param enginNumber
	 * @param asset
	 * @return
	 */
	public boolean isEnginNumberExist(String enginNumber, Asset asset) {
		BaseRestrictions<Asset> restrictions = new BaseRestrictions<>(Asset.class);
		restrictions.addCriterion(Restrictions.eq(ENGINENUMBER, enginNumber));
		if (asset != null && asset.getId() != null) {
			restrictions.addCriterion(Restrictions.ne(ID, asset.getId()));
		}
		List<Asset> assets = list(restrictions);
		return assets != null && !assets.isEmpty();
	}

	/**
	 * @see com.nokor.efinance.core.asset.service.AssetService#getAssetByPlateNumber(java.lang.String)
	 */
	@Override
	public Asset getAssetByPlateNumber(String plateNumber) {
		BaseRestrictions<Asset> restrictions = new BaseRestrictions<>(Asset.class);
		restrictions.addCriterion(Restrictions.eq(PLATENUMBER, plateNumber));
		List<Asset> assets = list(restrictions);
		if (assets != null && !assets.isEmpty()) {
			return assets.get(0);
		}
		return null;
	}

	/**
	 * 
	 */
	@Override
	public String getAssetStatus(Contract contract) {
		ContractFlagRestriction restrictions = new ContractFlagRestriction();
		restrictions.setConId(contract.getId());
		List<ContractFlag> contractFlags = list(restrictions);
		if (contractFlags != null && !contractFlags.isEmpty()) {
			ContractFlag contractFlag = contractFlags.get(0);
			if (contractFlag != null) {
				return  contractFlag.getFlag().getDesc();
			}
		}
		return StringUtils.EMPTY;
	}
	
}

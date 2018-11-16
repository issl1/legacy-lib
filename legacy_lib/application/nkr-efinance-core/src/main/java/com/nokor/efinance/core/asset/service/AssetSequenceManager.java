package com.nokor.efinance.core.asset.service;

import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.config.model.SettingConfig;

/**
 * Sequence manager for all asset
 * @author uhout.cheng
 */
public final class AssetSequenceManager implements FinServicesHelper {

	private final static String ASSETMAKECODE = "ASSMAKCODE";
	private final static String ASSETRANGECODE = "ASSRANCODE";
	private final static String ASSETMODELCODE = "ASSMODCODE";
	
	private Logger LOG = LoggerFactory.getLogger(AssetSequenceManager.class);
	
	private Integer sequenceAssetMake = 0;
	private Integer sequenceAssetRange = 0;
	private Integer sequenceAssetModel = 0;
			
	private AssetSequenceManager() {
		
	}
	
	/** Holder */
	private static class SingletonHolder {
		private final static AssetSequenceManager instance = new AssetSequenceManager();
	}
	 
	/**
	 * @return
	 */
	public static AssetSequenceManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * @return the sequenceAssetMake
	 */
	public Integer getSequenceAssetMake() {
		synchronized (this.sequenceAssetMake) {
			AssetMakeRestriction restrictions = new AssetMakeRestriction();
			restrictions.addOrder(Order.desc(AssetMake.CODE));
			SettingConfig setting = CONT_SRV.getByCode(SettingConfig.class, ASSETMAKECODE);
			if (setting == null) {
				setting = new SettingConfig();
				setting.setStatusRecord(EStatusRecord.ACTIV);
				setting.setDesc("Running code of asset make");
				setting.setReadOnly(false);
				setting.setCode(ASSETMAKECODE);
				setting.setValue(String.valueOf(1));
				List<AssetMake> assetMakes = ENTITY_SRV.list(restrictions);
				if (assetMakes != null && !assetMakes.isEmpty()) {
					sequenceAssetMake = MyNumberUtils.getInteger(assetMakes.get(0).getCode(), 0);
				}
			} else {
				sequenceAssetMake = Integer.parseInt(setting.getValue());
			}
			sequenceAssetMake++;
			setting.setValue(String.valueOf(sequenceAssetMake));
			LOG.debug(">> saveOrUpdate SettingConfig Asset Make");
			CONT_SRV.saveOrUpdate(setting);
			LOG.debug("<< saveOrUpdate SettingConfig Asset Make");
		}
		return sequenceAssetMake;
	}
	
	/**
	 * 
	 * @param brandCode
	 * @return
	 */
	public Integer getSequenceAssetRange(String brandCode) {
		synchronized (this.sequenceAssetRange) {
			AssetMake assetMake = ASS_MAKE_SRV.getByCode(AssetMake.class, brandCode);
			AssetRangeRestriction restrictions = new AssetRangeRestriction();
			restrictions.setAssetMake(assetMake);
			restrictions.addOrder(Order.desc(AssetRange.CODE));
			SettingConfig setting = CONT_SRV.getByCode(SettingConfig.class, ASSETRANGECODE + brandCode);
			if (setting == null) {
				setting = new SettingConfig();
				setting.setStatusRecord(EStatusRecord.ACTIV);
				setting.setDesc("Running code of asset range");
				setting.setReadOnly(false);
				setting.setCode(ASSETRANGECODE + brandCode);
				setting.setValue(String.valueOf(1));
				List<AssetRange> assetRanges = ENTITY_SRV.list(restrictions);
				if (assetRanges != null && !assetRanges.isEmpty()) {
					sequenceAssetRange = MyNumberUtils.getInteger(assetRanges.get(0).getCode(), 0);
				}
			} else {
				sequenceAssetRange = Integer.parseInt(setting.getValue());
			}
			sequenceAssetRange++;
			setting.setValue(String.valueOf(sequenceAssetRange));
			LOG.debug(">> saveOrUpdate SettingConfig Asset Range");
			CONT_SRV.saveOrUpdate(setting);
			LOG.debug("<< saveOrUpdate SettingConfig Asset Range");
		}
		return sequenceAssetRange;
	}
	
	/**
	 * 
	 * @param modelCode
	 * @return
	 */
	public Integer getSequenceAssetModel(String modelCode) {
		synchronized (this.sequenceAssetModel) {
			AssetRange assetRange = ASS_RANGE_SRV.getByCode(AssetRange.class, modelCode);
			AssetModelRestriction restrictions = new AssetModelRestriction();
			restrictions.setAssetRange(assetRange);
			restrictions.addOrder(Order.desc(AssetModel.CODE));
			SettingConfig setting = CONT_SRV.getByCode(SettingConfig.class, ASSETMODELCODE + modelCode);
			if (setting == null) {
				setting = new SettingConfig();
				setting.setStatusRecord(EStatusRecord.ACTIV);
				setting.setDesc("Running code of asset model");
				setting.setReadOnly(false);
				setting.setCode(ASSETMODELCODE + modelCode);
				setting.setValue(String.valueOf(1));
				List<AssetModel> assetModels = ENTITY_SRV.list(restrictions);
				if (assetModels != null && !assetModels.isEmpty()) {
					sequenceAssetModel = MyNumberUtils.getInteger(assetModels.get(0).getCode(), 0);
				}
			} else {
				sequenceAssetModel = Integer.parseInt(setting.getValue());
			}
			sequenceAssetModel++;
			setting.setValue(String.valueOf(sequenceAssetModel));
			LOG.debug(">> saveOrUpdate SettingConfig Asset Model");
			CONT_SRV.saveOrUpdate(setting);
			LOG.debug("<< saveOrUpdate SettingConfig Asset Model");
		}
		return sequenceAssetModel;
	}
	
}

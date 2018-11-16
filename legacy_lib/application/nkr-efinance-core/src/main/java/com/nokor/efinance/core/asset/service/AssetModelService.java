package com.nokor.efinance.core.asset.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;

/**
 * Asset model service interface
 * @author uhout.cheng
 */
public interface AssetModelService extends BaseEntityService {

	/**
	 * saveOrUpdate Asset Model
	 * @param assetModel
	 */
	void saveOrUpdateAssetModel(AssetModel assetModel);
	
	/**
	 * 
	 * @param assetRange
	 * @return
	 */
	List<AssetModel> getAssetModelsByAssetRange(AssetRange assetRange);
	
}

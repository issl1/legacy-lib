package com.nokor.efinance.core.asset.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetRange;

/**
 * Asset range service interface
 * @author uhout.cheng
 */
public interface AssetRangeService extends BaseEntityService {

	/**
	 * saveOrUpdate Asset Range
	 * @param assetRange
	 */
	void saveOrUpdateAssetRange(AssetRange assetRange);
	
	/**
	 * 
	 * @param assetMake
	 * @return
	 */
	List<AssetRange> getAssetRangesByAssetMake(AssetMake assetMake);
	
}

package com.nokor.efinance.core.asset.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.asset.model.AssetMake;

/**
 * Asset make service interface
 * @author uhout.cheng
 */
public interface AssetMakeService extends BaseEntityService {

	/**
	 * saveOrUpdate Asset Make
	 * @param assetMake
	 */
	void saveOrUpdateAssetMake(AssetMake assetMake);
	
}

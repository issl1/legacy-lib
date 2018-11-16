package com.nokor.efinance.core.asset.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.exception.ValidationFieldsException;

/**
 * Customer service interface
 * @author ly.youhort
 *
 */
public interface AssetService extends BaseEntityService {

	/**
	 * Save or update an asset
	 * @param asset
	 * @return
	 */
	Asset saveOrUpdateAsset(Asset asset);
	
	/**
	 * Control asset
	 * @param asset
	 */
	void checkAsset(Asset asset) throws ValidationFieldsException;
	
	/**
	 * @param chassisNumber
	 * @return
	 */
	List<Asset> getAssetsByChassisNumber(String chassisNumber);
	
	/**
	 * @param enginNumber
	 * @return
	 */
	List<Asset> getAssetsByEnginNumber(String enginNumber);
	
	/**
	 * @param enginNumber
	 * @param asset
	 * @return
	 */
	boolean isChassisNumberExist(String chassisNumber, Asset asset);
	
	/**
	 * @param enginNumber
	 * @param asset
	 * @return
	 */
	boolean isEnginNumberExist(String enginNumber, Asset asset);
	
	/**
	 * 
	 * @param plateNumber
	 * @return
	 */
	Asset getAssetByPlateNumber(String plateNumber);
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	String getAssetStatus(Contract contract);
	
}

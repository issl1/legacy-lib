package com.nokor.efinance.core.financial.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.financial.model.AssetMatrixPrice;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.ersys.core.hr.model.eref.EColor;

/**
 * @author ly.youhort
 *
 */
public interface MatrixPricingService  extends BaseEntityService {

	/**
	 * @param assetModel
	 * @param dealer
	 * @param color
	 * @param year
	 * @return
	 */
	AssetMatrixPrice getAssetModelMatrixPrice(AssetModel assetModel, Dealer dealer, EColor color, Integer year);
	
	/**
	 * @param assetModel
	 * @param service
	 * @return
	 */
	AssetMatrixPrice getServiceMatrixPrice(AssetModel assetModel, FinService service);
}

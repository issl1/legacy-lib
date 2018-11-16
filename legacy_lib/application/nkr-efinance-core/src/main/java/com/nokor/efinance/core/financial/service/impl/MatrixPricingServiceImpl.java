package com.nokor.efinance.core.financial.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.financial.model.AssetMatrixPrice;
import com.nokor.efinance.core.financial.service.MatrixPricingService;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.ersys.core.hr.model.eref.EColor;

/**
 * Matrix Pricing service
 * @author ly.youhort
 */
@Service("matrixPricingService")
public class MatrixPricingServiceImpl extends BaseEntityServiceImpl implements AssetEntityField, MatrixPricingService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private EntityDao dao;
	
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}
	
	@Override
	public AssetMatrixPrice getAssetModelMatrixPrice(AssetModel assetModel, Dealer dealer, EColor color, Integer year) {
				
		AssetMatrixPrice matrixPrice = getAssetModelPrice(assetModel, dealer, color, year);
		if (matrixPrice == null) {
			matrixPrice = getAssetModelPrice(assetModel, dealer, null, year);
			if (matrixPrice == null) {
				matrixPrice = getAssetModelPrice(assetModel, dealer, color, null);
				if (matrixPrice == null) {
					matrixPrice = getAssetModelPrice(assetModel, dealer, null, null);
				}
			}
		}
		return matrixPrice;
	}
	
	/**
	 * @param assetModel
	 * @param dealer
	 * @param color
	 * @param year
	 * @return
	 */
	private AssetMatrixPrice getAssetModelPrice(AssetModel assetModel, Dealer dealer, EColor color, Integer year) {
		BaseRestrictions<AssetMatrixPrice> restrictions = new BaseRestrictions<AssetMatrixPrice>(AssetMatrixPrice.class);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.eq(ASSET_MODEL + "." + ID, assetModel.getId()));
		if (year != null) {
			restrictions.addCriterion(Restrictions.eq(YEAR, year));
		}
		if (color != null) {
			restrictions.addCriterion(Restrictions.eq(COLOR + "." + ID, color.getId()));
		}
		restrictions.addOrder(Order.desc("date"));
		List<AssetMatrixPrice> matrixPrices = list(restrictions);
		if (matrixPrices != null && !matrixPrices.isEmpty()) {
			return matrixPrices.get(0);
		}
		return null;
	}
	
	@Override
	public AssetMatrixPrice getServiceMatrixPrice(AssetModel assetModel, com.nokor.efinance.core.financial.model.FinService service) {
		BaseRestrictions<AssetMatrixPrice> restrictions = new BaseRestrictions<AssetMatrixPrice>(AssetMatrixPrice.class);
		restrictions.addCriterion(Restrictions.eq(SERVICE + "." + ID, service.getId()));
		restrictions.addCriterion(Restrictions.eq(ASSET_MODEL + "." + ID, assetModel.getId()));
		restrictions.addOrder(Order.desc("date"));
		List<AssetMatrixPrice> matrixPrices = list(restrictions);
		if (matrixPrices != null && !matrixPrices.isEmpty()) {
			return matrixPrices.get(0);
		}
		return null;
	}
}

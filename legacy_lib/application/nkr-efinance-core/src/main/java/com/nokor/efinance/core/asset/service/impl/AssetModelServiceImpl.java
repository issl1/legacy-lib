package com.nokor.efinance.core.asset.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.service.AssetModelRestriction;
import com.nokor.efinance.core.asset.service.AssetModelSequenceImpl;
import com.nokor.efinance.core.asset.service.AssetModelService;
import com.nokor.efinance.core.asset.service.AssetSequenceManager;
import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Asset model service
 * @author uhout.cheng
 */       
@Service("assetModelService")
public class AssetModelServiceImpl extends BaseEntityServiceImpl implements AssetModelService {
	
	/** */
	private static final long serialVersionUID = -6293684761226832807L;
	
	@Autowired
    private EntityDao dao;
	
	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}
	
	/**
	 * @see com.nokor.efinance.core.asset.service.AssetModelService#saveOrUpdateAssetModel(com.nokor.efinance.core.asset.model.AssetModel)
	 */
	@Override
	public void saveOrUpdateAssetModel(AssetModel assetModel) {
		if (assetModel.getCode().length() < 9) {
			String assRangeCode = StringUtils.EMPTY;
			if (assetModel.getAssetRange() != null) {
				assRangeCode = assetModel.getAssetRange().getCode();
			}
			Integer sequence = AssetSequenceManager.getInstance().getSequenceAssetModel(assRangeCode);
			SequenceGenerator sequenceGenerator = new AssetModelSequenceImpl(assRangeCode, sequence);
			assetModel.setCode(sequenceGenerator.generate());
		}
		saveOrUpdate(assetModel);
	}
	
	/**
	 * @see com.nokor.efinance.core.asset.service.AssetModelService#getAssetModelsByAssetRange(com.nokor.efinance.core.asset.model.AssetRange)
	 */
	@Override
	public List<AssetModel> getAssetModelsByAssetRange(AssetRange assetRange) {
		AssetModelRestriction restrictions = new AssetModelRestriction();
		restrictions.setAssetRange(assetRange);
		return list(restrictions);
	}
	
}

package com.nokor.efinance.core.asset.service.impl;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.service.AssetMakeSequenceImpl;
import com.nokor.efinance.core.asset.service.AssetMakeService;
import com.nokor.efinance.core.asset.service.AssetSequenceManager;
import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Asset make service
 * @author uhout.cheng
 */       
@Service("assetMakeService")
public class AssetMakeServiceImpl extends BaseEntityServiceImpl implements AssetMakeService {
	
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
	 * @see com.nokor.efinance.core.asset.service.AssetMakeService#saveOrUpdateAssetMake(com.nokor.efinance.core.asset.model.AssetMake)
	 */
	@Override
	public void saveOrUpdateAssetMake(AssetMake assetMake) {
		if (assetMake.getCode().length() != 3) {
			SequenceGenerator sequenceGenerator = new AssetMakeSequenceImpl(AssetSequenceManager.getInstance().getSequenceAssetMake());
			assetMake.setCode(sequenceGenerator.generate());
		}
		saveOrUpdate(assetMake);
	}
	
}

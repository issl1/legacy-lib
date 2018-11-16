package com.nokor.efinance.core.asset.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.service.AssetRangeRestriction;
import com.nokor.efinance.core.asset.service.AssetRangeSequenceImpl;
import com.nokor.efinance.core.asset.service.AssetRangeService;
import com.nokor.efinance.core.asset.service.AssetSequenceManager;
import com.nokor.efinance.core.quotation.SequenceGenerator;

/**
 * Asset range service
 * @author uhout.cheng
 */
@Service("assetRangeService")
public class AssetRangeServiceImpl extends BaseEntityServiceImpl implements AssetRangeService {
	
	/** */
	private static final long serialVersionUID = 3839730827037552185L;
	
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
	 * @see com.nokor.efinance.core.asset.service.AssetRangeService#saveOrUpdateAssetRange(com.nokor.efinance.core.asset.model.AssetRange)
	 */
	@Override
	public void saveOrUpdateAssetRange(AssetRange assetRange) {
		if (assetRange.getCode().length() != 6) {
			String assMakeCode = StringUtils.EMPTY;
			if (assetRange.getAssetMake() != null) {
				assMakeCode = assetRange.getAssetMake().getCode();
			}
			Integer sequence = AssetSequenceManager.getInstance().getSequenceAssetRange(assMakeCode);
			SequenceGenerator sequenceGenerator = new AssetRangeSequenceImpl(assMakeCode, sequence);
			assetRange.setCode(sequenceGenerator.generate());
		}
		saveOrUpdate(assetRange);
	}
	
	/**
	 * @see com.nokor.efinance.core.asset.service.AssetRangeService#getAssetRangesByAssetMake(com.nokor.efinance.core.asset.model.AssetMake)
	 */
	@Override
	public List<AssetRange> getAssetRangesByAssetMake(AssetMake assetMake) {
		AssetRangeRestriction restrictions = new AssetRangeRestriction();
		restrictions.setAssetMake(assetMake);
		return list(restrictions);
	}
	
}

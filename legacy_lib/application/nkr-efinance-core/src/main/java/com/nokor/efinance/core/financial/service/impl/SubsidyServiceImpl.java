package com.nokor.efinance.core.financial.service.impl;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.financial.model.ManufacturerSubsidy;
import com.nokor.efinance.core.financial.service.SubsidyService;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Service("subsidyService")
public class SubsidyServiceImpl extends BaseEntityServiceImpl implements SubsidyService {

	/** */
	private static final long serialVersionUID = -3799107617120703940L;

	@Autowired
    private EntityDao dao;
	
	/**
	 * @see com.nokor.efinance.core.financial.service.SubsidyService#saveOrUpdateSubsidy(com.nokor.efinance.core.financial.model.ManufacturerSubsidy)
	 */
	@Override
	public void saveOrUpdateSubsidy(ManufacturerSubsidy manufacturerSubsidy) {
		saveOrUpdate(manufacturerSubsidy);
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}

}

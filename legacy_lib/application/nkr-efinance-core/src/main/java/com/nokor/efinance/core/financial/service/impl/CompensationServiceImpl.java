package com.nokor.efinance.core.financial.service.impl;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.financial.model.ManufacturerCompensation;
import com.nokor.efinance.core.financial.service.CompensationService;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Service("compensationService")
public class CompensationServiceImpl extends BaseEntityServiceImpl implements CompensationService {

	/** */
	private static final long serialVersionUID = 8625250845743689099L;
	
	@Autowired
    private EntityDao dao;
	
	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}

	@Override
	public void saveOrUpdateCompensation(ManufacturerCompensation manufacturerCompensation) {
		saveOrUpdate(manufacturerCompensation);
		
	}


}

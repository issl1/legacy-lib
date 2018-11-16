package com.nokor.efinance.core.financial.service.impl;

import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.efinance.core.financial.dao.FinancialProductDao;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinProductService;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.financial.service.FinServiceRestriction;
import com.nokor.efinance.core.financial.service.FinancialProductService;


/**
 * Financial product service
 * @author uhout.cheng
 */
@Service("finProductService")
@Transactional
public class FinancialProductServiceImpl extends BaseEntityServiceImpl implements FinancialProductService {
	
	/** */
	private static final long serialVersionUID = -5735549630724098577L;
	
	@Autowired
    private FinancialProductDao dao;
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public FinancialProductDao getDao() {
		return dao;
	}

	/**
	 * @see com.nokor.efinance.core.financial.service.FinancialProductService#createFinProduct(com.nokor.efinance.core.financial.model.FinProduct)
	 */
	@Override
	public FinProduct createFinProduct(FinProduct finProduct) {
		super.saveOrUpdate(finProduct);
		
		// Create financial product service
		if (finProduct.getFinancialProductServices() != null) {
			for (FinProductService finProductService : finProduct.getFinancialProductServices()) {
				super.saveOrUpdate(finProductService);
			}
		}
		return finProduct;
	}

	/**
	 * @see com.nokor.efinance.ra.financial.service.FinProductService#deteleFinProduct(com.nokor.efinance.core.financial.model.FinProduct)
	 */
	@Override
	public void deleteFinProduct(FinProduct finProduct) {
		// Delete financial product service
		if (finProduct.getFinancialProductServices() != null) {
			for (FinProductService finProductService : finProduct.getFinancialProductServices()) {
				super.delete(finProductService);
			}
		}
		super.delete(finProduct);
	}
	
	
	/**
	 * @param serviceType
	 * @return
	 */
	@Override
	public FinService getFinServiceByType(EServiceType serviceType) {
		FinServiceRestriction restrictions = new FinServiceRestriction();
		restrictions.setServiceType(serviceType);
		return getFirst(restrictions);
	}
}

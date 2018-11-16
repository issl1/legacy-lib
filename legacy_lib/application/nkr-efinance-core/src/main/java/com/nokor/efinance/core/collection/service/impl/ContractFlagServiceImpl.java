package com.nokor.efinance.core.collection.service.impl;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.service.ContractFlagService;
import com.nokor.efinance.core.contract.model.Contract;

/**
 * Contract Flag Service
 * @author uhout.cheng
 */       
@Service("contractFlagService")
public class ContractFlagServiceImpl extends BaseEntityServiceImpl implements ContractFlagService {
	
	/** */
	private static final long serialVersionUID = -8289733967836096833L;
	
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
	 * @see com.nokor.efinance.core.collection.service.ContractFlagService#saveOrUpdateLegalCase(com.nokor.efinance.core.collection.model.ContractFlag)
	 */
	@Override
	public void saveOrUpdateLegalCase(ContractFlag contractFlag) {
		update(contractFlag.getContract());
		saveOrUpdate(contractFlag);
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.ContractFlagService#withdrawLegalCase(com.nokor.efinance.core.collection.model.ContractFlag)
	 */
	@Override
	public void withdrawLegalCase(ContractFlag contractFlag) {
		Contract contract = contractFlag.getContract();
		if (contract != null) {
			contract.setWkfSubStatus(null);
			update(contract);
		}
		delete(contractFlag);
	}
	
}

package com.nokor.efinance.core.history.service.impl;

import java.util.List;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.history.FinHistory;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.history.service.FinHistoryRestriction;
import com.nokor.efinance.core.history.service.FinHistoryService;

/**
 * 
 * @author uhout.cheng
 */
@Service("finHistoryService")
public class FinHistoryServiceImpl extends BaseEntityServiceImpl implements FinHistoryService {

	/** */
	private static final long serialVersionUID = -989132212019489930L;

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
	 * @see com.nokor.efinance.core.history.service.FinHistoryService#addFinHistory(com.nokor.efinance.core.contract.model.Contract, com.nokor.efinance.core.history.FinHistoryType, java.lang.String)
	 */
	@Override
	public void addFinHistory(Contract contract, FinHistoryType historyType, String comment) {
		FinHistory history = FinHistory.createInstance();
		history.setContract(contract);
		history.setType(historyType);
		history.setComment(comment);
		create(history);
	}
	
	/**
	 * @see com.nokor.efinance.core.history.service.FinHistoryService#getFinHistories(java.lang.Long, com.nokor.efinance.core.history.FinHistoryType[])
	 */
	@Override
	public List<FinHistory> getFinHistories(Long conId, FinHistoryType[] historyTypes) {
		FinHistoryRestriction restrictions = new FinHistoryRestriction();
		restrictions.setConId(conId);
		restrictions.setFinHistoryTypes(historyTypes);
		return list(restrictions);
	}
	
}

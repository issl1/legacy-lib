package com.nokor.ersys.core.finance.service.bank.impl;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.ersys.core.finance.model.Bank;
import com.nokor.ersys.core.finance.service.bank.BankService;

/**
 * 
 * @author prasnar
 *
 */
@Service("bankService")
public class BankServiceImpl extends MainEntityServiceImpl implements BankService {
	/** */
	private static final long serialVersionUID = 868279872843548923L;

	@Autowired
	private EntityDao dao;
	
	public BankServiceImpl() { }

	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}

	@Override
	public void createProcess(MainEntity entity) {
		final Bank bankInfo = (Bank) entity;
        saveOnAction(bankInfo.getSubEntitiesToCascadeAction());
        getDao().create(bankInfo);
	}

	@Override
	public void updateProcess(MainEntity entity) {
		Bank bankInfo = (Bank) entity;
        saveOnAction(bankInfo.getSubEntitiesToCascadeAction());
        bankInfo = getDao().merge(bankInfo);
	}


	@Override
	public void deleteProcess(MainEntity entity) {
		final Bank bankInfo = (Bank) entity;

        // TODO: delete other entities first
        getDao().delete(bankInfo);
	}
}

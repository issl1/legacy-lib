package com.nokor.efinance.core.contract.service.impl;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.contract.model.Letter;
import com.nokor.efinance.core.contract.service.LetterService;

/**
 * 
 * @author uhout.cheng
 */
@Service("letterService")
public class LetterServiceImpl extends BaseEntityServiceImpl implements LetterService {
	
	/** */
	private static final long serialVersionUID = -2395345551107277839L;
	
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
	 * @see com.nokor.efinance.core.contract.service.LetterService#saveOrUpdateLetter(com.nokor.efinance.core.contract.model.Letter)
	 */
	@Override
	public void saveOrUpdateLetter(Letter letter) {
		saveOrUpdate(letter.getSendAddress());
		saveOrUpdate(letter);
	}
	
}

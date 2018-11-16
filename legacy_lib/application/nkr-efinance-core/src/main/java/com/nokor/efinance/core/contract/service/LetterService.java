package com.nokor.efinance.core.contract.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.contract.model.Letter;

/**
 * 
 * @author uhout.cheng
 */
public interface LetterService extends BaseEntityService {

	/**
	 * saveOrUpdate letter
	 * @param letter
	 */
	void saveOrUpdateLetter(Letter letter);
	
}

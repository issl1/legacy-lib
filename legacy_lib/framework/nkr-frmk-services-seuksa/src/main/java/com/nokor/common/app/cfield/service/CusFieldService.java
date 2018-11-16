package com.nokor.common.app.cfield.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.cfield.model.CusField;

/**
 * 
 * @author prasnar
 *
 */
public interface CusFieldService extends BaseEntityService {

	
	List<CusField> listFields(long tableId);
	List<CusField> listFields(String tableName);
	
}

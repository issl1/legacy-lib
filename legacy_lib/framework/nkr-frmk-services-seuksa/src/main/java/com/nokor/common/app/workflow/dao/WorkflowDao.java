package com.nokor.common.app.workflow.dao;

import org.seuksa.frmk.dao.BaseEntityDao;

import com.nokor.common.app.workflow.model.WkfBaseHistory;

/**
 * 
 * @author prasnar
 *
 */
public interface WorkflowDao extends BaseEntityDao {

	<T extends WkfBaseHistory> void saveHistory(T his);
	
}

package com.nokor.common.app.workflow.dao;

import org.seuksa.frmk.dao.impl.BaseEntityDaoImpl;
import org.springframework.stereotype.Repository;

import com.nokor.common.app.workflow.model.WkfBaseHistory;

/**
 * 
 * @author prasnar
 *
 */
@Repository
public class WorkflowDaoImpl extends BaseEntityDaoImpl implements WorkflowDao {

	/**
	 * 
	 */
	public WorkflowDaoImpl() {
		
	}

	@Override
	public <T extends WkfBaseHistory> void saveHistory(T his)  {
		saveOrUpdate(his);
		saveOrUpdateList(his.getHistItems());
	}

}

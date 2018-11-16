package com.nokor.common.app.scheduler.service;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author prasnar
 *
 */
@Service("schedulerService")
public class SchedulerServiceImpl extends BaseEntityServiceImpl implements SchedulerService {
	/** */
	private static final long serialVersionUID = -4550390793521029785L;
	
	@Autowired
	private EntityDao entityDao;
	
	@Override
	public BaseEntityDao getDao() {
		return entityDao;
	}
	    
}

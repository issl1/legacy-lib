package com.nokor.ersys.core.hr.service.impl;

import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.ersys.core.hr.dao.OrgStructureDao;
import com.nokor.ersys.core.hr.service.OrgStructureService;

/**
 * 
 * @author prasnar
 *
 */
@Service("orgStructureService")
public class OrgStructureServiceImpl extends BaseEntityServiceImpl implements OrgStructureService {
	/** */
	private static final long serialVersionUID = 4725178339279330204L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private OrgStructureDao dao;
	
	
	/**
	 * 
	 */
    public OrgStructureServiceImpl() {
    	super();
    }


	@Override
	public OrgStructureDao getDao() {
		return dao;
	}
       
}

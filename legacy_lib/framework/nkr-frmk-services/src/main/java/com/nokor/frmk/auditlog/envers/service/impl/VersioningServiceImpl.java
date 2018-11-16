package com.nokor.frmk.auditlog.envers.service.impl;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.frmk.auditlog.envers.service.VersioningService;

/**
 * 
 * @author prasnar
 *
 */
@Service("versioningService")
public class VersioningServiceImpl extends BaseEntityServiceImpl implements VersioningService {
	/** */
	private static final long serialVersionUID = 2013761260532899660L;
	
	@Autowired
	private EntityDao entityDao;
	private AuditReader auditReader;

	@Override
	public BaseEntityDao getDao() {
		return entityDao;
	}

	/**
	 * @return the auditReader
	 */
	@Override
	public AuditReader getAuditReader() {
		if (auditReader == null) {
			auditReader = AuditReaderFactory.get(entityDao.getCurrentSession());
		}
		return auditReader;
	}

	

}

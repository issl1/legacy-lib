package com.nokor.frmk.auditlog.envers.service;

import org.hibernate.envers.AuditReader;
import org.seuksa.frmk.service.BaseEntityService;

/**
 * 
 * @author prasnar
 *
 */
public interface VersioningService extends BaseEntityService {

	AuditReader getAuditReader();

}

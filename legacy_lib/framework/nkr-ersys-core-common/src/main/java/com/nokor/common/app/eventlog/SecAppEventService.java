package com.nokor.common.app.eventlog;

import java.util.Date;

import org.seuksa.frmk.service.BaseEntityService;

/**
 * 
 * @author prasnar
 *
 */
public interface SecAppEventService extends BaseEntityService {

	void cleanFrom(Long appId, Date fromDate, Date toDate);

	void cleanBeforeNbMonths(Long appId, int beforeNbMonths);

	void cleanBeforeNbDays(Long appId, int beforeNbDays);

	
}

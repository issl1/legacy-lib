package com.nokor.common.app.history;

import java.util.Date;

import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.history.model.EHistoReason;
import com.nokor.common.app.history.model.History;

/**
 * 
 * @author prasnar
 *
 */
public interface HistoryService extends BaseEntityService {

    History getLastHistory(Date dtReference, String className, Long entityId, String property);

	History getLastHistory(String entity, Long entityId, String property);

	void addHistory(EntityA entity,String propertyName, EHistoReason reason, String oldValue, String newValue);	

}

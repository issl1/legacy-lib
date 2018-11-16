package com.nokor.common.app.workflow.service;

import java.util.List;

import com.nokor.common.app.eref.EProductLineCode;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.history.HistoryException;
import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.common.app.workflow.model.WkfBaseHistory;
import com.nokor.common.app.workflow.model.WkfBaseHistoryItem;
import com.nokor.common.app.workflow.model.WkfBaseTempItem;
import com.nokor.common.app.workflow.model.WkfHistoConfig;



/**
 * 
 * @author prasnar
 * 
 */
public interface WorkflowService extends BaseEntityService {
	< T extends EntityWkf> WkfHistoConfig getHistoConfig(Class<T> entityWkfClass);

	WkfHistoConfig getHistoConfig(String entityWkfClassName);
	
	WkfHistoConfig getHistoConfig(EMainEntity mainEntity);

	List<EWkfStatus> getWkfStatuses(EWkfFlow flow, EProductLineCode productLineCode);

	List<EWkfStatus> getNextWkfStatuses(EWkfFlow flow, EProductLineCode productLineCode,EWkfStatus eStatus) throws HistoryException;

	<T extends WkfBaseHistory> void saveHistory(T his) throws HistoryException;

	<T extends WkfBaseHistoryItem> List<T> getHistories(WkfHistoryItemRestriction restrictions) throws HistoryException;

	<T extends WkfBaseHistoryItem> List<T> getHistoriesByFlow(EWkfFlow flow, long entityId) throws HistoryException;
	
	<T extends WkfBaseHistoryItem> List<T> getHistoriesByFlow(EWkfFlow flow, long entityId, Order order) throws HistoryException;

	<T extends WkfBaseHistoryItem> List<T> getHistoriesByFlow(EWkfFlow flow, long entityId, String propertyName) throws HistoryException;

	<T extends WkfBaseHistoryItem> List<T> getHistoriesByFlow(EWkfFlow flow, long entityId, String propertyName, Order order) throws HistoryException;

	<T extends WkfBaseHistoryItem> T getPreviousHistoSubStatus(EWkfFlow flow, long entityId) throws HistoryException;

	<T extends WkfBaseHistoryItem> T getPreviousHistoStatus(EWkfFlow flow, long entityId) throws HistoryException;

	<T extends WkfBaseHistoryItem> T getPreviousHistoProperty(EWkfFlow flow, long entityId, String propertyName) throws HistoryException;
	
	<T extends WkfBaseTempItem> List<T> getTempsByFlow(EWkfFlow flow, long entityId) throws HistoryException;
	
	<T extends WkfBaseTempItem> List<T> getTempsByFlow(EWkfFlow flow, long entityId, String propertyName, Order order) throws HistoryException;
	
	void initHistoConfigCache();

	void init();
	
	void updateValidatedEntityByTemps(EntityWkf entityWkf, Class clazz);
	
	void updateRefusedEntityByTemps(EntityWkf entityWkf, Class clazz);

}

package com.nokor.common.app.workflow.service;

import com.nokor.common.app.eref.EProductLineCode;
import com.nokor.common.app.history.HistoryException;
import com.nokor.common.app.workflow.dao.WorkflowDao;
import com.nokor.common.app.workflow.model.*;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.reflection.MyClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author prasnar
 */
@Service("workflowService")
public class WorkflowServiceImpl extends BaseEntityServiceImpl implements WorkflowService {
    /** */
    private static final long serialVersionUID = 2192877383257129378L;

    @Autowired
    private WorkflowDao dao;

    private static List<WkfHistoConfig> lstHistoConfigCache = null;


//	<T extends Entity> List<String> getRequiredPropertiesForActive(Class<T> entityClass);


    /**
     *
     */
    public WorkflowServiceImpl() throws HistoryException {
    }

    /**
     * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
     */
    @Override
    public BaseEntityDao getDao() {
        return dao;
    }
//	
//	@Override
//	public  <T extends EntityWkf> WkfFlow getWkfFlow(Class<T> clazz) {
//		try {
//			WkfFlow eFlow = WkfFlow.getById(getWkfFlow(clazz).getId());
//			if (eFlow == null) {
//				throw new IllegalStateException("WkfFlow [" + getClass().getCanonicalName() + "] can not be found.");
//			}
//			return eFlow;
//		} catch (Exception e) {
//			LoggerFactory.getLogger(getClass()).warn("Erreur while getting getWkfFlow", e);
//			throw new IllegalStateException("getWkfFlow", e);
//		}
//	}


    @Override
    public List<EWkfStatus> getWkfStatuses(EWkfFlow flow, EProductLineCode productLineCode) {
        try {
//			EWkfFlow flow2 = dao.getById(EWkfFlow.class, flow.getId());
//			return flow2.getStatuses();
            return flow.getEntity().getWrkFlowEntityByCode(productLineCode).getStatuses();
        } catch (Exception e) {
            throw new IllegalStateException("getWkfStatuses", e);
        }
    }

    @Override
    public List<EWkfStatus> getNextWkfStatuses(EWkfFlow flow, EProductLineCode productLineCode,EWkfStatus eStatus) throws HistoryException {
        try {
//			EWkfFlow flow2 = dao.getById(EWkfFlow.class, flow.getId());
//			return flow2.getNextWkfStatuses(eStatus);
            return flow.getEntity().getWrkFlowEntityByCode(productLineCode).getNextWkfStatuses(eStatus);
        } catch (Exception e) {
            throw new HistoryException("getNextWkfStatuses", e);
        }
    }

    @Override
    public <T extends WkfBaseHistory> void saveHistory(T his) throws HistoryException {
        try {
            dao.saveOrUpdate(his);
            dao.saveOrUpdateList(his.getHistItems());
            dao.saveOrUpdateList(his.getTempItems());
        } catch (Exception e) {
            throw new HistoryException("saveHistory", e);
        }
    }


    @Override
    public <T extends WkfBaseHistoryItem> List<T> getHistories(WkfHistoryItemRestriction restrictions) throws HistoryException {
        try {
            List<T> lst = (List<T>) dao.list(restrictions);
            return lst;
        } catch (Exception e) {
            throw new HistoryException("getHistories", e);
        }
    }

    @Override
    public <T extends WkfBaseHistoryItem> List<T> getHistoriesByFlow(EWkfFlow flow, long entityId) throws HistoryException {
        return getHistoriesByFlow(flow, entityId, null, null);
    }

    @Override
    public <T extends WkfBaseHistoryItem> List<T> getHistoriesByFlow(EWkfFlow flow, long entityId, Order order) throws HistoryException {
        return getHistoriesByFlow(flow, entityId, null, order);
    }

    @Override
    public <T extends WkfBaseHistoryItem> List<T> getHistoriesByFlow(EWkfFlow flow, long entityId, String propertyName) throws HistoryException {
        return getHistoriesByFlow(flow, entityId, propertyName, null);

    }

    @Override
    public <T extends WkfBaseHistoryItem> List<T> getHistoriesByFlow(EWkfFlow flow, long entityId, String propertyName, Order order) throws HistoryException {
        try {
            WkfHistoryItemRestriction<T> restrictions = new WkfHistoryItemRestriction<T>((Class<T>) flow.getWkfHistoryItemClass());
            restrictions.setEntity(flow.getEntity());
            restrictions.setEntityId(entityId);
            restrictions.setPropertyName(propertyName);
            if (order != null) {
                restrictions.addOrder(order);
            } else {
                restrictions.addOrder(Order.desc(WkfHistoryItem.CHANGEDATE));
            }
            return (List<T>) list(restrictions);
        } catch (Exception e) {
            throw new HistoryException("getHistoriesByFlow", e);
        }
    }

    @Override
    public <T extends WkfBaseHistoryItem> T getPreviousHistoSubStatus(EWkfFlow flow, long entityId) throws HistoryException {
        return getPreviousHistoProperty(flow, entityId, EntityWkf.WKFSUBSTATUS);
    }

    @Override
    public <T extends WkfBaseHistoryItem> T getPreviousHistoStatus(EWkfFlow flow, long entityId) throws HistoryException {
        return getPreviousHistoProperty(flow, entityId, EntityWkf.WKFSTATUS);
    }

    @Override
    public <T extends WkfBaseHistoryItem> T getPreviousHistoProperty(EWkfFlow flow, long entityId, String propertyName) throws HistoryException {
        try {
            WkfHistoryItemRestriction<T> restrictions = new WkfHistoryItemRestriction<T>((Class<T>) flow.getWkfHistoryItemClass());
            restrictions.setEntity(flow.getEntity());
            restrictions.setEntityId(entityId);
            restrictions.setPropertyName(propertyName);
            restrictions.addOrder(Order.desc(WkfBaseHistoryItem.CHANGEDATE));
            T his = (T) dao.getFirst(restrictions);
            return his;
        } catch (Exception e) {
            throw new HistoryException("getPreviousHistoryStatus", e);
        }
    }

    @Override
    public WkfHistoConfig getHistoConfig(EMainEntity mainEntity) {
        return getHistoConfig(mainEntity.getCode());
    }

    @Override
    public <T extends EntityWkf> WkfHistoConfig getHistoConfig(Class<T> entityWkfClass) {
        return getHistoConfig(entityWkfClass.getCanonicalName());
    }

    @Override
    public WkfHistoConfig getHistoConfig(String entityWkfClassName) {
//		WkfHistoConfigRestriction restrictions = new WkfHistoConfigRestriction();
//		restrictions.setEntity(mainEntity);
//		return dao.getUnique(restrictions);

        if (lstHistoConfigCache == null) {
            initHistoConfigCache();
        }
        return findInHistoConfigCache(entityWkfClassName);
    }

    @Override
    public void init() {
        initHistoConfigCache();
    }

    @Override
    public void initHistoConfigCache() {
        WkfHistoConfigRestriction restrictions = new WkfHistoConfigRestriction();
        lstHistoConfigCache = dao.list(restrictions);
        if (lstHistoConfigCache == null || lstHistoConfigCache.size() == 0) {
            throw new IllegalStateException("No WkfHistoConfig has been found.");
        }
    }

    /**
     * @param entityWkfClassName
     * @return
     */
    private WkfHistoConfig findInHistoConfigCache(String entityWkfClassName) {
        for (WkfHistoConfig cfg : lstHistoConfigCache) {
            if (cfg.getEntity().getCode().equals(entityWkfClassName)) {
                return cfg;
            }
        }
        return null;
    }

    /**
     * @param
     * @return
     */
//  public <T extends MainEntity> List<String> getRequiredInfosForActive(T entity) throws HistoryException {
//    logger.debug("Check RequiredInfosForActive");
//    final List<String> missingProperties = new ArrayList<String>();
//    try {
//      final List<String> requiredProperties = SETTING_SRV.getRequiredPropertiesForActive(entity.getClass());
//
//      for (final String property : requiredProperties) throws HistoryException {
//        boolean missing = false;
//
//        final Field field = this.getClass().getDeclaredField(property);
//
//        final Object value = field.get(this);
//        logger.debug(" - [" + property + "] [" + (value != null ? value.toString() : "<null>") + "]");
//        if (Number.class.isAssignableFrom(value.getClass())) throws HistoryException {
//          missing = ((Number) value).doubleValue() < 0;
//        }
//        else if (value instanceof String) throws HistoryException {
//          missing = StringUtils.isEmpty((String) value);
//        }
//        else {
//          missing = value == null;
//        }
//
//        if (missing) throws HistoryException {
//          missingProperties.add(property);
//        }
//      }
//
//    }
//    catch (Exception e) {
//      logger.error("getRequiredInfosForActive", e);
//    }
//    return missingProperties;
//
//  }
//
//  	
    @Override
    public <T extends WkfBaseTempItem> List<T> getTempsByFlow(EWkfFlow flow, long entityId) throws HistoryException {
        return getTempsByFlow(flow, entityId, null, null);
    }

    @Override
    public <T extends WkfBaseTempItem> List<T> getTempsByFlow(EWkfFlow flow, long entityId, String propertyName, Order order) throws HistoryException {
        try {
            WkfHistoryItemRestriction<T> restrictions = new WkfHistoryItemRestriction<T>((Class<T>) flow.getWkfTempItemClass());
            restrictions.setEntity(flow.getEntity());
            restrictions.setEntityId(entityId);
            restrictions.setPropertyName(propertyName);
            if (order != null) {
                restrictions.addOrder(order);
            } else {
                restrictions.addOrder(Order.desc(WkfTempItem.CHANGEDATE));
            }
            return (List<T>) list(restrictions);
        } catch (Exception e) {
            throw new HistoryException("getHistoriesByFlow", e);
        }
    }

    @Override
    public void updateValidatedEntityByTemps(EntityWkf entityWkf, Class clazz) {
        List<? extends WkfBaseTempItem> list = entityWkf.getTemps();
        if (list != null && list.size() > 0) {
            for (WkfBaseTempItem wkfBaseTempItem : list) {
                MyClassUtils.setValue(entityWkf, clazz, wkfBaseTempItem.getPropertyName(), wkfBaseTempItem.getNewValue());
                delete(wkfBaseTempItem);
            }
        }
        entityWkf.setWkfSubStatus(TempWkfStatus.AMENDED_VALID);
        update(entityWkf);
    }

    @Override
    public void updateRefusedEntityByTemps(EntityWkf entityWkf, Class clazz) {
        entityWkf.setWkfSubStatus(TempWkfStatus.AMENDED_REFUS);
        update(entityWkf);
    }
}
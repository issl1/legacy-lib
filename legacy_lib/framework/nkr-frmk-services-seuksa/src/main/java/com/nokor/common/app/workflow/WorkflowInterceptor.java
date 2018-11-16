package com.nokor.common.app.workflow;

import com.nokor.common.app.history.model.BaseHistoryItem;
import com.nokor.common.app.history.model.EHistoReason;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.workflow.model.*;
import com.nokor.frmk.auditlog.FieldProperty;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.interceptor.TypeInterceptor;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.RefDataId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

/**
 * @author prasnar
 */
public class WorkflowInterceptor extends EmptyInterceptor implements TypeInterceptor, SeuksaServicesHelper {
    /** */
    private static final long serialVersionUID = 887953788050192667L;

    private Logger logger = LoggerFactory.getLogger(WorkflowInterceptor.class);

    private static final String _NULL = "NULL";

    private EWkfStatus previousWkfStatus;
    private EWkfStatus previousWkfSubStatus;

    // from onSave
    private EntityWkf createdWkfEntity;
    private Object[] createdCurrentState;
    private String[] createdPropertyNames;

    /**
     *
     */
    public WorkflowInterceptor() {
        super();
    }

    /**
     * @see com.nokor.frmk.interceptor.TypeInterceptor#supports(java.lang.Object)
     */
    public final boolean supports(final Object object) {
        return object instanceof EntityWkf && ((EntityWkf) object).isWkfEnabled();
    }

    @Override
    public boolean onLoad(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) throws CallbackException {
//    	boolean res = super.onLoad(entity, id, state, propertyNames, types);
        ((EntityWkf) entity).setPreviousState(state);
        return false;
    }

    /**
     * Update
     *
     * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    @Override
    public boolean onFlushDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) {
        if (!supports(entity)) {
            return false;
        }
        EntityWkf entityWkf = (EntityWkf) entity;
        this.createdWkfEntity = null;
        boolean res = processWorkflow(entityWkf, id, currentState, entityWkf.getPreviousState(), propertyNames, types);

        if (res) {
            entityWkf.setPreviousState(currentState);
        }
        return res;
        //    	return false; //super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    /**
     * Create/Update
     *
     * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    @Override
    public final boolean onSave(final Object entity, final Serializable id, final Object[] currentState, final String[] propertyNames, final Type[] types) {
        if (!supports(entity)) {
            return false;
        }
        EntityWkf entityWkf = (EntityWkf) entity;
        // creation case: is processed in afterTransactionCompletion()
        if (entityWkf.getId() == null) {
            this.createdWkfEntity = entityWkf;
            this.previousWkfStatus = null;
            this.previousWkfSubStatus = null;
            this.createdCurrentState = currentState;
            this.createdPropertyNames = propertyNames;

            entityWkf.setPreviousState(currentState);
            return false;
        }
        return processWorkflow(entityWkf, id, currentState, new Object[propertyNames.length], propertyNames, types);
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
//    	if (tx.wasCommitted() && createdWkfEntity != null) {
        if (createdWkfEntity != null) {
            try {
                boolean isChangeWkfStatus = true;
                boolean isTempStatus = false;
                boolean res = processHistory(isChangeWkfStatus, isTempStatus, createdWkfEntity, createdCurrentState, null, createdPropertyNames);
                if (res) {
//    				createdWkfEntity.setPreviousState(createdCurrentState);
//    				createdWkfEntity.checkAfterChangeStatus();
                    createdWkfEntity = null;
                }
            } catch (Exception e) {
                logger.error("afterTransactionCompletion", e);
            }
        }
    }


    @Override
    public void preFlush(Iterator entities) {
        logger.debug("Entered preFlush");
    }

    /**
     * @param id
     * @param currentState
     * @param previousState
     * @param propertyNames
     * @param types
     * @return
     */
    private boolean processWorkflow(final EntityWkf entityWkf, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) {
        boolean modified = false;
        boolean isChangeWkfStatus = hasChangedWkfStatus(currentState, previousState, propertyNames);
        boolean isTempStatus = entityWkf.isTempWkfStatus();

        if (!entityWkf.isForcedHistory() && !isChangeWkfStatus && !isTempStatus) {
            return modified;
        }
        if (entityWkf.checkBeforeChangeStatus()) {
            try {
                modified |= processHistory(isChangeWkfStatus, isTempStatus, entityWkf, currentState, previousState, propertyNames);
            } catch (Exception e) {
                logger.error("processWorkflow", e);
            }
        }
        return modified;
    }


    /**
     * @param propertyNames
     * @return
     */
    private int getWkfStatusIndex(final String[] propertyNames) {
        for (int i = 0; i < propertyNames.length; ++i) {
            if (propertyNames[i].equals(EntityWkf.WKFSTATUS)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param propertyNames
     * @return
     */
    private int getWkfSubStatusIndex(final String[] propertyNames) {
        for (int i = 0; i < propertyNames.length; ++i) {
            if (propertyNames[i].equals(EntityWkf.WKFSUBSTATUS)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param currentState
     * @param previousState
     * @param propertyNames
     * @return
     */
    private boolean hasChangedWkfStatus(final Object[] currentState, final Object[] previousState, final String[] propertyNames) {
        int indexStatus = getWkfStatusIndex(propertyNames);
        int indexSubStatus = getWkfSubStatusIndex(propertyNames);

        if (currentState == null && previousState == null) {
            throw new WorkflowException("Should never happen: currentState == null && previousState == null");
        }
        if (currentState == null && previousState != null) {
            throw new WorkflowException("Should never happen: currentState == null");
        }
        if (currentState != null && previousState == null) {
            return true;
        }

        boolean res = isChanged(currentState[indexStatus], previousState[indexStatus])
                || isChanged(currentState[indexSubStatus], previousState[indexSubStatus]);
        if (res) {
            previousWkfStatus = (EWkfStatus) previousState[indexStatus];
            previousWkfSubStatus = (EWkfStatus) previousState[indexSubStatus];
        }
        return res;
    }

    /**
     * @param entityWkf
     * @param currentState
     * @param previousState
     * @param propertyNames
     * @throws WkfHistoryException
     */
    private <T extends WkfBaseHistory> boolean processHistory(boolean isChangeWkfStatus, boolean isTempStatus, final EntityWkf entityWkf, final Object[] currentState, final Object[] previousState, final String[] propertyNames) throws WkfHistoryException {
        try {
            // Build the main History: WkfHistory + WkfHistoryEntry for EWkfStatus
            T wkfHis = buildMainHistory(entityWkf, previousWkfStatus, previousWkfSubStatus, isChangeWkfStatus);

            if (isTempStatus) {
                addTempProperties(wkfHis, entityWkf, currentState, previousState, propertyNames);
            } else {
                // Add to the main History: the history entries for AuditProperties
                //addHistoryProperties(wkfHis, entityWkf, currentState, previousState, propertyNames);
            }

            // Persist in database
            boolean res = saveHistory(wkfHis);

            return res;
        } catch (Exception e) {
            throw new WkfHistoryException("processHistory", e);
        }

    }

    /**
     * @param entityWkf
     * @param previousWkfStatus
     * @return
     */
    private <T extends WkfBaseHistory> T buildMainHistory(final EntityWkf entityWkf, EWkfStatus previousWkfStatus, EWkfStatus previousWkfSubStatus, boolean isChangeWkfStatus) {
        T wkfHis = (T) WkfBaseHistory.createInstance(entityWkf.getWkfHistoryClass(), entityWkf.getWkfFlow());

        EMainEntity entity = EMainEntity.getByClass(entityWkf.getClass());
        wkfHis.setEntity(entity);
        wkfHis.setEntityId(entityWkf.getId());

        if (entityWkf.getId() == null) {
            throw new WkfHistoryException("buildMainHistory - Error of null id while saving history [" + entity.getCode() + "]");
        }

        if (entityWkf.getWkfStatus() == null) {
            entityWkf.initWkStatus();
        }

        if (isChangeWkfStatus) {
            addHistoryItem(wkfHis,
                    entityWkf, EntityWkf.WKFSTATUS,
                    previousWkfStatus != null ? previousWkfStatus.getCode() : null,
                    entityWkf.getWkfStatus().getCode(),
                    isChangeWkfStatus);
            if (entityWkf.getWkfSubStatus() != null) {
                addHistoryItem(wkfHis,
                        entityWkf, EntityWkf.WKFSUBSTATUS,
                        previousWkfSubStatus != null ? previousWkfSubStatus.getCode() : null,
                        entityWkf.getWkfSubStatus().getCode(),
                        isChangeWkfStatus);
            }
        }

        return wkfHis;
    }

    /**
     * @param wkfHis
     * @param entityWkf
     * @param currentState
     * @param previousState
     * @param propertyNames
     */
    private <T extends WkfBaseHistory> void addTempProperties(final T wkfHis, final EntityWkf entityWkf, final Object[] currentState, final Object[] previousState, final String[] propertyNames) {
        try {
            for (FieldProperty prop : entityWkf.getTempProperties()) {
                boolean foundProperty = false;
                for (int i = 0; i < propertyNames.length && !foundProperty; ++i) {
                    if (propertyNames[i].equals(prop.getName())) {
                        boolean hasChanged = previousState == null // is a creation
                                || isChanged(currentState[i], previousState[i]);
                        if (hasChanged) {
                            addTempItem(wkfHis, entityWkf, prop.getName(), previousState != null ? getNullSafeString(previousState[i]) : null, getNullSafeString(currentState[i]), hasChanged);
                            currentState[i] = previousState[i];
                            foundProperty = true;
                        }
                    }
                }

            }
        } catch (Exception e) {
            logger.error("While building WkfTempItem items from TempProperties [" + entityWkf.getClass().getCanonicalName() + "]", e);
            return;
        }

    }

    /**
     * @param wkfHis
     * @param entityWkf
     * @param currentState
     * @param previousState
     * @param propertyNames
     */
    private <T extends WkfBaseHistory> void addHistoryProperties(final T wkfHis, final EntityWkf entityWkf, final Object[] currentState, final Object[] previousState, final String[] propertyNames) {

        try {
            for (FieldProperty prop : entityWkf.getHistProperties()) {
                boolean foundProperty = false;
                for (int i = 0; i < propertyNames.length && !foundProperty; ++i) {
                    if (propertyNames[i].equals(prop.getName())) {
                        boolean hasChanged = previousState == null // is a creation
                                || isChanged(currentState[i], previousState[i]);
                        if (hasChanged) {
                            addHistoryItem(wkfHis, entityWkf, prop.getName(), previousState != null ? getNullSafeString(previousState[i]) : null, getNullSafeString(currentState[i]), hasChanged);
                            foundProperty = true;
                        }
                    }
                }

            }
        } catch (Exception e) {
            logger.error("While building WkfHistoryItem items from HistProperties [" + entityWkf.getClass().getCanonicalName() + "]", e);
            return;
        }

    }

    /**
     * @param wkfHis
     * @param entityWkf
     * @param propertyName
     * @param oldValue
     * @param newValue
     * @param isChangeWkfStatus
     */
    private <T extends WkfBaseHistory, I extends WkfBaseTempItem> void addTempItem(final T wkfHis, final EntityWkf entityWkf, final String propertyName, final String oldValue, final String newValue, boolean isChangeWkfStatus) {
        I entry = (I) WkfBaseTempItem.createInstance(entityWkf.getWkfTempItemClass(), wkfHis);
        addHistoryItem(entry, entityWkf, propertyName, oldValue, newValue, isChangeWkfStatus);
        wkfHis.addTempItem(entry);
    }

    /**
     * @param wkfHis
     * @param entityWkf
     * @param propertyName
     * @param oldValue
     * @param newValue
     * @param isChangeWkfStatus
     */
    private <T extends WkfBaseHistory, I extends WkfBaseHistoryItem> void addHistoryItem(final T wkfHis, final EntityWkf entityWkf, final String propertyName, final String oldValue, final String newValue, boolean isChangeWkfStatus) {
        I entry = (I) WkfBaseHistoryItem.createInstance(entityWkf.getWkfHistoryItemClass(), wkfHis);
        addHistoryItem(entry, entityWkf, propertyName, oldValue, newValue, isChangeWkfStatus);
        wkfHis.addHistItem(entry);
    }

    /**
     * @param hisItem
     * @param entityWkf
     * @param propertyName
     * @param oldValue
     * @param newValue
     * @param isChangeWkfStatus
     */
    private void addHistoryItem(BaseHistoryItem hisItem, final EntityWkf entityWkf, final String propertyName, final String oldValue, final String newValue, boolean isChangeWkfStatus) {
        EMainEntity entity = EMainEntity.getByClass(entityWkf.getClass());
        hisItem.setEntity(entity);
        hisItem.setEntityId(entityWkf.getId());

        if (entityWkf.getId() == null) {
            throw new WkfHistoryException("addHistoryItem - Error of null id while saving hisItem [" + entity.getCode() + "]");
        }

        if (entityWkf.getWkfChangedDate() != null) {
            hisItem.setChangeDate(entityWkf.getWkfChangedDate());
        } else {
            hisItem.setChangeDate(new Date());
        }
        hisItem.setPropertyName(propertyName);
        hisItem.setOldValue(oldValue);
        if (newValue == null) {
            throw new WkfHistoryException("addHistoryItem - Error of null newValue while saving hisItem [" + entity.getCode() + "(" + (entityWkf.getId() != null ? entityWkf.getId() : "<empty>id") + ")." + propertyName + "]");
        }
        hisItem.setNewValue(newValue);
        hisItem.setComment(entityWkf.getHistComment());
        hisItem.setComment2(entityWkf.getHistComment2());
        if (entityWkf.getWkfReason() != null) {
            hisItem.setReason(entityWkf.getWkfReason());
        } else {
            hisItem.setReason(isChangeWkfStatus ? EHistoReason.WKF_CHANGE_STATUS : EHistoReason.WKF_HISTO_ACTIVITY);
        }
        if (entityWkf.getWkfReason2() != null) {
            hisItem.setReason2(entityWkf.getWkfReason2());
        }
        if (entityWkf.getWkfModifiedBy() != null) {
            hisItem.setModifiedBy(entityWkf.getWkfModifiedBy());
            hisItem.setSecUserProfile(hisItem.getModifiedBy() != null ? hisItem.getModifiedBy().getDefaultProfile().getDesc() : null);
        } else {
            hisItem.setModifiedBy(UserSessionManager.getCurrentUser() != null ? UserSessionManager.getCurrentUser() : SeuksaServicesHelper.SECURITY_SRV.getAnonynmousUser());
            hisItem.setSecUserProfile(hisItem.getModifiedBy() != null ? hisItem.getModifiedBy().getDefaultProfile().getDesc() : null);
        }
    }

    /**
     * @param wkfHis
     * @return
     */
    private <T extends WkfBaseHistory> boolean saveHistory(T wkfHis) {
        try {
            WKF_SRV.saveHistory(wkfHis);
            return true;
        } catch (Exception e) {
            logger.error("Error while persisting History in the database", e);
        }
        return false;
    }


    /**
     * @param currentState
     * @param previousState
     * @return
     */
    private boolean isChanged(Object currentState, Object previousState) {
        return (previousState == null && currentState != null) // nothing to something
                || (previousState != null && currentState == null) // something to nothing
                || (previousState != null && !previousState.equals(currentState)); // something to something else
    }

    /**
     * @param obj
     * @return
     */
    private String getNullSafeString(Object obj) {
        if (obj == null) {
            return _NULL;
        }
        if (obj instanceof EntityA) {
            return String.valueOf(((EntityA) obj).getId());
        }
        if (obj instanceof RefDataId) {
            return String.valueOf(((RefDataId) obj).getId());
        }
        return String.valueOf(obj); // obj.toString()
    }


    /**
     * @param l
     * @return
     */
    private Long getNullSafeLong(Long l) {
        return l != null ? l : 0L;
    }
}

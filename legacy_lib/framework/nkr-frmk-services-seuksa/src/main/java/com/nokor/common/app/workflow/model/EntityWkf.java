package com.nokor.common.app.workflow.model;

import com.nokor.common.app.eref.EProductLineCode;
import com.nokor.common.app.history.model.EHistoReason;
import com.nokor.common.app.workflow.WorkflowException;
import com.nokor.common.app.workflow.service.WkfHistoryItemRestriction;
import com.nokor.frmk.auditlog.FieldProperty;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.entity.MainEntity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Entity Workflow
 *
 * @author prasnar
 */
@MappedSuperclass
public abstract class EntityWkf extends MainEntity {
    /** */
    private static final long serialVersionUID = -3464533524950381802L;

    public final static String _COMMA = ";";
    public final static String WKFSTATUS = "wkfStatus";
    public final static String WKFSUBSTATUS = "wkfSubStatus";

    private EWkfStatus wkfStatus;
    private EWkfStatus wkfSubStatus;
    private WkfBaseHistoryItem previousWkfHistoStatus; // previous wkfStatus in history
    private WkfBaseHistoryItem previousWkfHistoSubStatus; // previous wkfSubStatus in history

    private WkfHistoConfig histoConfig;
    private Class<? extends WkfBaseHistory> historyClass;
    private Class<? extends WkfBaseHistoryItem> historyItemClass;
    private Class<? extends WkfBaseTempItem> tempItemClass;
    private List<FieldProperty> histProperties;
    private List<FieldProperty> tempProperties;

    private boolean forcedHistory;
    private Date wkfChangedDate;
    private SecUser wkfModifiedBy;
    private EHistoReason wkfReason;
    private EHistoReason wkfReason2;
    private String histComment;
    private String histComment2;

    private List<WkfBaseHistoryItem> histEntries;
    private List<WkfBaseTempItem> tempEntries;

    private Object[] previousState; // saved by WorkflowInterceptor.onLoad()

    /**
     * @return the status
     */
    @Column(name = "wkf_sta_id", nullable = false)
    @Convert(converter = EWkfStatus.class)
    public EWkfStatus getWkfStatus() {
        if (wkfStatus == null) {
            wkfStatus = getDefaultWkfStatusAtCreation();
        }
        return wkfStatus;
    }


    /**
     * @param status the status to set
     */
    public void setWkfStatus(EWkfStatus status) {
        this.wkfStatus = status;
    }


    /**
     * @return the wkfSubStatus
     */
    @Column(name = "wkf_sub_sta_id", nullable = true)
    @Convert(converter = EWkfStatus.class)
    public EWkfStatus getWkfSubStatus() {
        return wkfSubStatus;
    }


    /**
     * @param wkfSubStatus the wkfSubStatus to set
     */
    public void setWkfSubStatus(EWkfStatus wkfSubStatus) {
        this.wkfSubStatus = wkfSubStatus;
    }


    /**
     * Can be overridden to false to disable the Workflow
     *
     * @return
     */
    @Transient
    public boolean isWkfEnabled() {
        return true;
    }

    /**
     * @return the histComment
     */
    @Transient
    public String getHistComment() {
        return histComment;
    }

    /**
     * @param histComment the histComment to set
     */
    public void setHistComment(String histComment) {
        this.histComment = histComment;
    }

    /**
     * @return the histComment2
     */
    @Transient
    public String getHistComment2() {
        return histComment2;
    }


    /**
     * @param histComment2 the histComment2 to set
     */
    public void setHistComment2(String histComment2) {
        this.histComment2 = histComment2;
    }


    /**
     * @return the previousState
     */
    @Transient
    public Object[] getPreviousState() {
        return previousState;
    }


    /**
     * @param previousState the previousState to set
     */
    public void setPreviousState(Object[] previousState) {
        this.previousState = previousState;
    }


    /**
     * @return the forcedHistory
     */
    @Transient
    public boolean isForcedHistory() {
        return forcedHistory;
    }

    /**
     * @param forcedHistory the forcedHistory to set
     */
    public void setForcedHistory(boolean forcedHistory) {
        this.forcedHistory = forcedHistory;
    }


    /**
     * @return the wkfChangedDate
     */
    @Transient
    public Date getWkfChangedDate() {
        return wkfChangedDate;
    }

    /**
     * @param wkfChangedDate
     */
    public void setWkfChangedDate(Date wkfChangedDate) {
        this.wkfChangedDate = wkfChangedDate;
    }

    /**
     * @return the wkfModifiedBy
     */
    @Transient
    public SecUser getWkfModifiedBy() {
        return wkfModifiedBy;
    }

    /**
     * @param wkfModifiedBy
     */
    public void setWkfModifiedBy(SecUser wkfModifiedBy) {
        this.wkfModifiedBy = wkfModifiedBy;
    }

    /**
     * @return the histReason
     */
    @Transient
    public EHistoReason getWkfReason() {
        return wkfReason;
    }

    /**
     * @param wkfReason
     */
    public void setWkfReason(EHistoReason wkfReason) {
        this.wkfReason = wkfReason;
    }

    /**
     * @return the wkfReason2
     */
    @Transient
    public EHistoReason getWkfReason2() {
        return wkfReason2;
    }

    /**
     * @param wkfReason2 the wkfReason2 to set
     */
    public void setWkfReason2(EHistoReason wkfReason2) {
        this.wkfReason2 = wkfReason2;
    }


    /**
     * @return
     */
    @Transient
    public List<EWkfStatus> getNextWkfStatuses(EProductLineCode productLineCode) {
        List<EWkfStatus> lstStatus;
        try {
            lstStatus = SeuksaServicesHelper.WKF_SRV.getNextWkfStatuses(getWkfFlow(), productLineCode, getWkfStatus());
        } catch (Exception e) {
            throw new WorkflowException("getNextWkfStatuses", e);
        }
        return lstStatus;
    }

    /**
     * @return
     */
    @Transient
    public WkfBaseHistoryItem getPreviousHistoStatus() {
        if (previousWkfHistoStatus == null) {
            try {
                previousWkfHistoStatus = SeuksaServicesHelper.WKF_SRV.getPreviousHistoStatus(getWkfFlow(), getId());
            } catch (Exception e) {
                throw new WorkflowException("getPreviousWkfHistoItem", e);
            }
        }
        return previousWkfHistoStatus;
    }

    /**
     * @return
     */
    @Transient
    public WkfBaseHistoryItem getPreviousHistoSubStatus() {
        if (previousWkfHistoSubStatus == null) {
            try {
                previousWkfHistoSubStatus = SeuksaServicesHelper.WKF_SRV.getPreviousHistoSubStatus(getWkfFlow(), getId());
            } catch (Exception e) {
                throw new WorkflowException("previousWkfHistoSubStatus", e);
            }
        }
        return previousWkfHistoSubStatus;
    }

    /**
     * @return
     */
    @Transient
    public EWkfStatus getPreviousWkfStatus() {
        WkfBaseHistoryItem histoStatus = getPreviousHistoStatus();
        return histoStatus != null ? histoStatus.getWkfPreviousStatus() : null;
    }

    /**
     * @return
     */
    @Transient
    public EWkfStatus getPreviousWkfSubStatus() {
        WkfBaseHistoryItem histoSubStatus = getPreviousHistoSubStatus();
        return histoSubStatus != null ? histoSubStatus.getWkfPreviousStatus() : null;
    }

    /**
     * @return
     */
    @Transient
    public Date getPreviousWkfChangedDate() {
        WkfBaseHistoryItem histoStatus = getPreviousHistoStatus();
        return histoStatus != null ? histoStatus.getChangeDate() : null;
    }

    /**
     * @return
     */
    @Transient
    public SecUser getPreviousWkfModifiedBy() {
        WkfBaseHistoryItem histoStatus = getPreviousHistoStatus();
        return histoStatus != null ? histoStatus.getModifiedBy() : null;
    }

    /**
     * @return
     */
    @Transient
    public EHistoReason getPreviousWkfReason() {
        WkfBaseHistoryItem histoStatus = getPreviousHistoStatus();
        return histoStatus != null ? histoStatus.getReason() : null;
    }


    @Transient
    public List<? extends WkfBaseHistoryItem> getHistories() {
        return getHistories(false);
    }

    /**
     * @return
     */
    @Transient
    public <T extends WkfBaseHistoryItem> List<T> getHistories(boolean forceReload) {
        if (histEntries == null || histEntries.size() == 0 || forceReload) {
            try {
                histEntries = SeuksaServicesHelper.WKF_SRV.getHistoriesByFlow(getWkfFlow(), getId());
            } catch (Exception e) {
                throw new WorkflowException("getHistories", e);
            }
        }
        return (List<T>) histEntries;
    }

    @Transient
    public List<? extends WkfBaseTempItem> getTemps() {
        return getTemps(false);
    }

    /**
     * @return
     */
    @Transient
    public <T extends WkfBaseTempItem> List<T> getTemps(boolean forceReload) {
        if (tempEntries == null || tempEntries.size() == 0 || forceReload) {
            try {
                tempEntries = SeuksaServicesHelper.WKF_SRV.getTempsByFlow(getWkfFlow(), getId());
            } catch (Exception e) {
                throw new WorkflowException("getTemps", e);
            }
        }
        return (List<T>) tempEntries;
    }

    /**
     * @param reason
     * @return
     */
    @Transient
    public <T extends WkfBaseHistoryItem> List<T> getHistoriesByReason(EHistoReason reason) {
        WkfHistoryItemRestriction<T> restrictions = new WkfHistoryItemRestriction<T>((Class<T>) getWkfHistoryItemClass());
        restrictions.setReason(reason);
        return getHistories(restrictions);
    }

    /**
     * @param propertyName
     * @return
     */
    @Transient
    public <T extends WkfBaseHistoryItem> List<T> getHistoriesByProperty(String propertyName) {
        WkfHistoryItemRestriction<T> restrictions = new WkfHistoryItemRestriction<T>((Class<T>) getWkfHistoryItemClass());
        restrictions.setPropertyName(propertyName);
        return getHistories(restrictions);
    }

    /**
     * @param restrictions
     * @return
     */
    @Transient
    public <T extends WkfBaseHistoryItem> List<T> getHistories(WkfHistoryItemRestriction<T> restrictions) {
        List<T> lstEntries = (List<T>) getHistories();
        List<T> lstRes = new ArrayList<>();
        for (T his : lstEntries) {
            boolean match = false;
            match = StringUtils.isNotEmpty(restrictions.getPropertyName()) && his.getPropertyName().equals(restrictions.getPropertyName());
            match &= restrictions.getReason() != null && his.getReason().equals(restrictions.getReason());

            if (match) {
                lstRes.add(his);
            }
        }

        return lstRes;
    }

    @Transient
    public EMainEntity getMainEntity() {
        EMainEntity entity = EMainEntity.getByCode(getClass().getCanonicalName());
        return entity;
    }

    /**
     * @return
     */
    @Transient
    public EWkfFlow getWkfFlow() {
        EWkfFlow flow = EWkfFlow.getByClass(getClass());
        if (flow == null) {
            flow = EWkfFlow.getDefault();
        }
        if (flow == null) {
            throw new WorkflowException("Error - Neither EWkfFlow [Default] &  EWkfFlow [" + getClass() + "] can not be found");
        }
        return flow;
    }


    @Transient
    public EWkfStatus getDefaultWkfStatusAtCreation() {
        return getWkfFlow().getDefaultWkfStatusAtCreation();
    }

    @Transient
    public void initWkStatus() {
        wkfStatus = getDefaultWkfStatusAtCreation();
    }

    /**
     * @return
     */
    public boolean checkBeforeChangeStatus() {
        return true;
    }

    /**
     * @return
     */
    public boolean checkAfterChangeStatus() {
        return true;
    }

    /**
     * @return
     */
    @Transient
    public List<FieldProperty> getHistProperties() {
        if (histProperties == null) {
            histProperties = new ArrayList<FieldProperty>();
            if (getHistoConfig() != null && histoConfig.getHistProperties() != null) {
                List<String> properties = Arrays.asList(histoConfig.getHistProperties().split(_COMMA));
                for (String prop : properties) {
                    FieldProperty field = new FieldProperty(prop);
                    histProperties.add(field);
                }
            }
        }
        return histProperties;
    }

    /**
     * @return
     */
    @Transient
    public List<FieldProperty> getTempProperties() {
        if (tempProperties == null) {
            tempProperties = new ArrayList<FieldProperty>();
            if (getHistoConfig() != null && histoConfig.getTempProperties() != null) {
                List<String> properties = Arrays.asList(histoConfig.getTempProperties().split(_COMMA));
                for (String prop : properties) {
                    FieldProperty field = new FieldProperty(prop);
                    tempProperties.add(field);
                }
            }
        }
        return tempProperties;
    }

    /**
     * @return
     */
    @Transient
    public final <T extends WkfBaseHistory> Class<T> getWkfHistoryClass() {
        if (historyClass == null) {
            if (getHistoConfig() == null) {
                return (Class<T>) WkfHistory.class;
            }
            historyClass = createWkfHistoryClass(histoConfig.getHistClassName());
        }
        return (Class<T>) historyClass;
    }

    /**
     * @return
     */
    @Transient
    public final <T extends WkfBaseHistoryItem> Class<T> getWkfHistoryItemClass() {
        if (historyItemClass == null) {
            if (getHistoConfig() == null) {
                return (Class<T>) WkfHistoryItem.class;
            }
            historyItemClass = createWkfHistoryItemClass(histoConfig.getHistItemClassName());
        }
        return (Class<T>) historyItemClass;
    }

    /**
     * @return
     */
    @Transient
    public final <T extends WkfBaseTempItem> Class<T> getWkfTempItemClass() {
        if (tempItemClass == null) {
            if (getHistoConfig() == null) {
                return (Class<T>) WkfTempItem.class;
            }
            tempItemClass = createWkfTempItemClass(histoConfig.getTempItemClassName());
        }
        return (Class<T>) tempItemClass;
    }


    /**
     * @param hisClassName
     * @return
     */
    private static <T extends WkfBaseHistory> Class<T> createWkfHistoryClass(String hisClassName) {
        try {
            Class<T> hisClazz = (Class<T>) Class.forName(hisClassName);
            return hisClazz;
        } catch (ClassNotFoundException e) {
            String errMsg = "Can not found the WkfHistory class [" + hisClassName + "]";
            throw new IllegalStateException(errMsg, e);
        }
    }

    /**
     * @param hisItemClassName
     * @return
     */
    private static <T extends WkfBaseHistoryItem> Class<T> createWkfHistoryItemClass(String hisItemClassName) {
        try {
            Class<T> hisClazz = (Class<T>) Class.forName(hisItemClassName);
            return hisClazz;
        } catch (ClassNotFoundException e) {
            String errMsg = "Can not found the WkfHistoryItemclass [" + hisItemClassName + "]";
            throw new IllegalStateException(errMsg, e);
        }
    }


    /**
     * @param tempItemClassName
     * @return
     */
    private static <T extends WkfBaseTempItem> Class<T> createWkfTempItemClass(String tempItemClassName) {
        try {
            Class<T> tmpClazz = (Class<T>) Class.forName(tempItemClassName);
            return tmpClazz;
        } catch (ClassNotFoundException e) {
            String errMsg = "Can not found the WkfTempItemclass [" + tempItemClassName + "]";
            throw new IllegalStateException(errMsg, e);
        }
    }


    /**
     * @return the histoConfig
     */
    @Transient
    public WkfHistoConfig getHistoConfig() {
        if (histoConfig == null) {
            histoConfig = SeuksaServicesHelper.WKF_SRV.getHistoConfig(getMainEntity());
        }
        return histoConfig;
    }


    /**
     * @param histoConfig the histoConfig to set
     */
    public void setHistoConfig(WkfHistoConfig histoConfig) {
        this.histoConfig = histoConfig;
    }

    /**
     * @return
     */
    @Transient
    public boolean isTempWkfStatus() {
        if (this.getWkfSubStatus() == null) {
            return false;
        }
        return TempWkfStatus.AMENDED.equals(this.getWkfSubStatus())
                || TempWkfStatus.AMENDED_SUBMI.equals(this.getWkfSubStatus());
    }

}

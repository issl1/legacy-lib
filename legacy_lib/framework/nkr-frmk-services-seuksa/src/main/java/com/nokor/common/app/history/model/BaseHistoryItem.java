package com.nokor.common.app.history.model;

import com.nokor.common.app.eventlog.model.SecEventLog;
import com.nokor.frmk.security.model.SecUser;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.entity.EntityA;

import javax.persistence.*;
import java.util.Date;


/**
 * History table
 *
 * @author prasnar
 */
@MappedSuperclass
public abstract class BaseHistoryItem extends EntityA implements MBaseHistoryItem {
    /** */
    private static final long serialVersionUID = -6789372863258483296L;

    private EMainEntity entity;
    private Long entityId;
    private Date changeDate;
    private String propertyName;
    private String valueClassName;
    private String oldValue;
    private String newValue;
    private EHistoReason reason;
    private EHistoReason reason2;
    private String comment;
    private String comment2;
    private SecUser modifiedBy;
    private String secUserProfile;
    private SecEventLog eventLog;

    /**
     * @param histoClazz
     * @return
     */
    public static <T extends BaseHistoryItem> T createInstance(Class<T> histoClazz) {
        T his = EntityFactory.createInstance(histoClazz);
        return his;
    }

    /**
     * @return the entityId
     */
    @Column(name = "his_entity_id", nullable = false)
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(final Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the entity
     */
    @Column(name = "mai_ent_id", nullable = false)
    @Convert(converter = EMainEntity.class)
    public EMainEntity getEntity() {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(EMainEntity entity) {
        this.entity = entity;
    }


    /**
     * @return the changeDate
     */
    @Column(name = "his_change_date", nullable = false)
    public Date getChangeDate() {
        return changeDate;
    }

    /**
     * @param changeDate the changeDate to set
     */
    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    /**
     * @return the oldValue
     */
    @Column(name = "his_old_value", nullable = true)
    public String getOldValue() {
        return oldValue;
    }

    /**
     * @param oldValue the oldValue to set
     */
    public void setOldValue(final String oldValue) {
        this.oldValue = oldValue;
    }

    /**
     * @return the newValue
     */
    @Column(name = "his_new_value", nullable = false)
    public String getNewValue() {
        return newValue;
    }

    /**
     * @param newValue the newValue to set
     */
    public void setNewValue(final String newValue) {
        this.newValue = newValue;
    }

    /**
     * @return the reason
     */
    @Column(name = "his_rea_id", nullable = false)
    @Convert(converter = EHistoReason.class)
    public EHistoReason getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(EHistoReason reason) {
        this.reason = reason;
    }

    /**
     * @return the reason2
     */
    @Column(name = "his_rea_id_2", nullable = true)
    @Convert(converter = EHistoReason.class)
    public EHistoReason getReason2() {
        return reason2;
    }

    /**
     * @param reason2 the reason2 to set
     */
    public void setReason2(EHistoReason reason2) {
        this.reason2 = reason2;
    }

    /**
     * @return the comment
     */
    @Column(name = "his_comment", nullable = true)
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * @return the comment2
     */
    @Column(name = "his_comment_2", nullable = true)
    public String getComment2() {
        return comment2;
    }

    /**
     * @param comment2 the comment2 to set
     */
    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    /**
     * @return the valueClassName
     */
    @Column(name = "his_value_class_name", nullable = true)
    public String getValueClassName() {
        return valueClassName;
    }

    /**
     * @param valueClassName the valueClassName to set
     */
    public void setValueClassName(String valueClassName) {
        this.valueClassName = valueClassName;
    }

    /**
     * @return the propertyName
     */
    @Column(name = "his_property_name", nullable = false)
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @param propertyName the propertyName to set
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }


    /**
     * @return the eventLog
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_eve_log_id", nullable = true)
    public SecEventLog getEventLog() {
        return eventLog;
    }

    /**
     * @param eventLog the eventLog to set
     */
    public void setEventLog(SecEventLog eventLog) {
        this.eventLog = eventLog;
    }

    /**
     * @return the modifiedBy
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id", nullable = false)
    public SecUser getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(SecUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Column(name = "sec_user_profile")
    public String getSecUserProfile() {
        return secUserProfile;
    }

    public void setSecUserProfile(String secUserProfile) {
        this.secUserProfile = secUserProfile;
    }
}
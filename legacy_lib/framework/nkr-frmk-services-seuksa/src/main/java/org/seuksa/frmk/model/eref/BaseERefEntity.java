package org.seuksa.frmk.model.eref;

import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.model.eref.EntityRef;
import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.DateUtils;

import javax.persistence.*;
import java.util.Date;


/**
 * @author prasnar
 */
@MappedSuperclass
public abstract class BaseERefEntity extends BaseERefData implements MBaseERefEntity, EntityRef {
    /** */
    private static final long serialVersionUID = -6437552201471803552L;

    private EntityRef entityRef;

    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    /**
     *
     */
    public BaseERefEntity() {
        // DO NOT REMOVE
        // USED BY HIBERNATE
        super();
    }

    public static BaseERefEntity newInstance(final Class<? extends BaseERefEntity> clazz) {
        try {
            BaseERefEntity entity = clazz.newInstance();
            entity.setStatusRecord(EStatusRecord.ACTIV);
            entity.setCreateDate(DateUtils.today());
            entity.setUpdateDate(DateUtils.today());
            entity.setCreateUser("admin");
            entity.setUpdateUser("admin");
            return entity;
        } catch (Exception e) {
            throw new IllegalStateException("Error while instantiating EntityRefA [" + clazz.getCanonicalName() + "]", e);
        }
    }

    /**
     * @param value
     * @param id
     */
    public BaseERefEntity(final String code, final long id) {
        super(code, id);
    }

    /**
     * @param code
     * @param desc
     * @param id
     */
    public BaseERefEntity(final String code, final String desc, final long id) {
        super(code, desc, id);
    }

    /**
     * @param code
     * @param desc
     * @param descEn
     * @param id
     */
    public BaseERefEntity(final String code, final String desc, final String descEn, final long id) {
        super(code, desc, descEn, id);
    }


    @Column(name = "ref_code", nullable = false)
    @Override
    public String getCode() {
        return super.getCode();
    }

    @Column(name = "ref_desc", nullable = true)
    @Override
    public String getDesc() {
        return super.getDesc();
    }

    @Column(name = "ref_desc_en", nullable = true)
    @Override
    public String getDescEn() {
        return super.getDescEn();
    }

    /**
     * @return SortIndex
     */
    @Column(name = "sort_index", nullable = true)
    public Integer getSortIndex() {
        return super.getSortIndex();
    }

    /**
     * @return the group
     */
    @Column(name = "group_by", nullable = true)
    @Convert(converter = EGroup.class)
    public EGroup getGroup() {
        return group;
    }

    /**
     * @see com.nokor.frmk.model.entity.EntityStatusRecordAware#getStatusRecord()
     */
    @Column(name = "sta_rec_id", nullable = true)
    @Convert(converter = EStatusRecord.class)
    @Override
    public EStatusRecord getStatusRecord() {
        return super.getStatusRecord();
    }

    /**
     * @return the createUser
     */
    @Column(name = "usr_cre", nullable = false, length = 30)
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
    public void setCreateUser(final String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return the createDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_cre", nullable = false, updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateUser
     */
    @Column(name = "usr_upd", nullable = false, length = 30)
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the updateUser to set
     */
    public void setUpdateUser(final String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the updateDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_upd", nullable = false)
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(final Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the entityRef
     */
    @Transient
    public EntityRef getEntityRef() {
        return entityRef;
    }

    /**
     * @param entityRef the entityRef to set
     */
    public void setEntityRef(EntityRef entityRef) {
        this.entityRef = entityRef;
    }

    /**
     * @param username
     */
    @Override
    @Transient
    public void fillSysBlock(final String username) {
        if (StringUtils.isEmpty(createUser)) {
            createUser = username;
            createDate = DateUtils.today();
            updateUser = createUser;
            updateDate = createDate;
        } else {
            updateUser = username;
            updateDate = DateUtils.today();
            ;
        }
    }

    /**
     * @param refDataClazz
     * @param id
     * @return
     */
    public static <T extends RefDataId> T getById(Class<T> refDataClazz, Long id) {
        return BaseERefData.getById(refDataClazz, id);
    }

    /**
     * @param refEntity
     * @return
     */
    public static <T extends BaseERefEntity> T getFromRefEntity(EntityRef refEntity, RefTable table) {
        Class<T> refDataClazz = null;
        T eRefData = null;

        try {
            refDataClazz = (Class<T>) Class.forName(table.getCode());
            if (refEntity.getClass().isAssignableFrom(refDataClazz)) {
                eRefData = (T) refEntity;
            } else {
                eRefData = EntityFactory.createInstance(refDataClazz);
                eRefData.setId(refEntity.getId());
                eRefData.setCode(refEntity.getCode());
                eRefData.setSortIndex(refEntity.getSortIndex());
                eRefData.setEntityRef(refEntity);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return eRefData;
    }

}
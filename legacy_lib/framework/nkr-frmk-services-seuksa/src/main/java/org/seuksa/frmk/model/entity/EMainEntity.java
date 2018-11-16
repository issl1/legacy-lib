package org.seuksa.frmk.model.entity;

import com.nokor.common.app.eref.EProductLineCode;
import com.nokor.common.app.workflow.model.WkfFlowEntity;
import org.seuksa.frmk.model.eref.BaseERefEntity;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * The Main Entity: used in History, WkfFlow
 * (full classname)
 *
 * @author prasnar
 */
@Entity
@Table(name = "ts_main_entity")
public class EMainEntity extends BaseERefEntity implements AttributeConverter<EMainEntity, Long>, MEMainEntity {
    /** */
    private static final long serialVersionUID = -1383161314391475561L;
    private Boolean isCustomHistory;
    private List<WkfFlowEntity> wkfFlowEntities;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mai_ent_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
     *
     */
    public EMainEntity() {
    }

    /**
     * @param code
     * @param id
     */
    public EMainEntity(String code, long id) {
        super(code, id);
    }


    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY)
    public List<WkfFlowEntity> getWkfFlowEntities() {
        if (wkfFlowEntities == null)
            wkfFlowEntities = new ArrayList<>();
        return wkfFlowEntities;
    }

    public void setWkfFlowEntities(List<WkfFlowEntity> wkfFlowEntities) {
        this.wkfFlowEntities = wkfFlowEntities;
    }

    @Override
    public EMainEntity convertToEntityAttribute(Long id) {
        return super.convertToEntityAttribute(id);
    }

    @Override
    public Long convertToDatabaseColumn(EMainEntity arg0) {
        return super.convertToDatabaseColumn(arg0);
    }


    /**
     * @return
     */
    public static List<EMainEntity> values() {
        return getValues(EMainEntity.class);
    }

    /**
     * @param id
     * @return
     */
    public static EMainEntity getById(long id) {
        EMainEntity entity = getById(EMainEntity.class, id);
        if (entity == null) {
            throw new IllegalStateException("Error - Entity [" + id + "] can not be found");
        }
        return entity;
    }

    /**
     * @param code
     * @return
     */
    public static EMainEntity getByCode(String code) {
        EMainEntity entity = getByCode(EMainEntity.class, code);
        if (entity == null) {
            throw new IllegalStateException("Error - Entity [" + code + "] can not be found");
        }
        return entity;
    }

    /**
     * @param entityClazz
     * @return
     */
    public static EMainEntity getByClass(Class entityClazz) {
        return getByCode(entityClazz.getCanonicalName());
    }

    @Column(name = "mai_ent_bl_cus_his", nullable = true)
    public Boolean getCustomHistory() {
        return isCustomHistory;
    }

    public void setCustomHistory(Boolean customHistory) {
        isCustomHistory = customHistory;
    }

    public WkfFlowEntity getWrkFlowEntityByCode(EProductLineCode productLineCode) {
        if (productLineCode != null) {
            for (WkfFlowEntity wkfFlowEntity : getWkfFlowEntities()) {
                if (wkfFlowEntity.getProductCode().getId().equals(productLineCode.getId()))
                    return wkfFlowEntity;
            }
        }
        return null;
    }
}
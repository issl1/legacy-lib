package com.nokor.common.app.workflow.model;

import com.nokor.common.app.eref.EProductLineCode;
import com.nokor.frmk.security.model.SecProfile;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.entity.EntityA;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ki.kao on 6/20/2017.
 */
@Entity
@Table(name = "tu_wkf_entity")
public class WkfFlowEntity extends EntityA {

    private EMainEntity entity;
    private List<WkfFlowItem> items;
    private List<WkfFlowStatus> flowStatuses;
    private List<WkfFlowProfile> flowProfiles;
    private EProductLineCode productCode;
    private Integer sortIndex;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_flo_ent_id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * @return the entity
     */
    @Column(name = "mai_ent_id", nullable = true) // to false
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
     * @return the items
     */
    @OneToMany(mappedBy = "wkfFlowEntity", fetch = FetchType.LAZY)
    public List<WkfFlowItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<WkfFlowItem> items) {
        this.items = items;
    }

    /**
     * @return the flowStatuses
     */
    @OneToMany(mappedBy = "wkfFlowEntity", fetch = FetchType.LAZY)
    public List<WkfFlowStatus> getFlowStatuses() {
        if (flowStatuses == null) {
            flowStatuses = new ArrayList<>();
        }
        return flowStatuses;
    }

    /**
     * @param flowStatuses the flowStatuses to set
     */
    public void setFlowStatuses(List<WkfFlowStatus> flowStatuses) {
        this.flowStatuses = flowStatuses;
    }

    /**
     * @return the flowProfiles
     */
    @OneToMany(mappedBy = "wkfFlowEntity", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<WkfFlowProfile> getFlowProfiles() {
        return flowProfiles;
    }

    /**
     * @param flowProfiles the flowProfiles to set
     */
    public void setFlowProfiles(List<WkfFlowProfile> flowProfiles) {
        this.flowProfiles = flowProfiles;
    }

    @Column(name = "pro_lin_typ_id", nullable = false)
    @Convert(converter = EProductLineCode.class)
    public EProductLineCode getProductCode() {
        return productCode;
    }

    public void setProductCode(EProductLineCode productCode) {
        this.productCode = productCode;
    }

    /**
     * @return SortIndex
     */
    @Column(name = "sort_index")
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * @param SortIndex
     */
    public void setSortIndex(final Integer SortIndex) {
        this.sortIndex = SortIndex;
    }

    /**
     * @param pro
     * @return
     */
    @Transient
    public List<WkfFlowItem> getItems(SecProfile pro) {
        List<WkfFlowItem> allItems = getItems();
        List<WkfFlowItem> itemsPro = new ArrayList<>();
        for (WkfFlowItem item : allItems) {
            if (item.hasProfile(pro)) {
                itemsPro.add(item);
            }
        }
        return itemsPro;
    }

    /**
     * @return
     */
    @Transient
    public List<EWkfStatus> getStatuses() {
        List<WkfFlowStatus> flowStatuses = getFlowStatuses();
        List<EWkfStatus> statusList = new ArrayList<>();
        for (WkfFlowStatus floSta : flowStatuses) {
            statusList.add(floSta.getStatus());
        }
        return statusList;
    }

    /**
     * @param eStatus
     * @return
     */
    @Transient
    public List<EWkfStatus> getNextWkfStatuses(EWkfStatus eStatus) {
        List<WkfFlowItem> allItems = getItems();
        List<EWkfStatus> statusList = new ArrayList<EWkfStatus>();
        for (WkfFlowItem item : allItems) {
            if ((item.getFromStatus() == null && eStatus == null)
                    || (item.getFromStatus() != null && item.getFromStatus().equals(eStatus))
                    ) {
                statusList.add(item.getToStatus());
            }
        }

        return statusList;
    }

    /**
     * @param clazz
     * @return
     */
    public static EWkfFlow getByClass(Class clazz) {
        EMainEntity entity = EMainEntity.getByCode(clazz.getCanonicalName());
        EWkfFlow flow = EWkfFlow.getByCode(clazz.getSimpleName());
        flow.setEntity(entity);
        // Error loop in WorkflowInterceptor
        //ENTITY_SRV.getByField(EWkfFlow.class, EWkfFlow.ENTITY, entity);

//		if (!((EWkfFlow)flow.getEntityRef()).getEntity().equals(entity)) {
//			throw new IllegalStateException("Error - Workflow [" + flow.getCode() + "] is not the same as Entity [" + entity.getCode() + "] can not be found");
//		}
        return flow;
    }
}

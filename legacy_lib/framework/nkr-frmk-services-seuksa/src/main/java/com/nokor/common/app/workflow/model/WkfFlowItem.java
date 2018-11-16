package com.nokor.common.app.workflow.model;

import com.nokor.common.app.action.model.ActionExecution;
import com.nokor.frmk.security.model.SecProfile;
import org.seuksa.frmk.model.entity.EntityA;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * List of all possibility (fromStatus/toStatus) for a given WkfFlow.
 *
 * @author prasnar
 */
@Entity
@Table(name = "tu_wkf_flow_item")
public class WkfFlowItem extends EntityA implements MWkfFlowItem {
    /** */
    private static final long serialVersionUID = 5176186683593373696L;

    private EWkfFlow flow;
    private EWkfStatus fromStatus;
    private EWkfStatus toStatus;
    private ActionExecution beforeAction;    // control before changing the status
    private ActionExecution afterAction;    // control after having changed the status
    private Integer sortIndex;
    private WkfFlowEntity wkfFlowEntity;

    private List<WkfFlowControl> flowActions;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_flo_ite_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }


    /**
     * @return the flow
     */
    @Column(name = "wkf_flo_id", nullable = false)
    @Convert(converter = EWkfFlow.class)
    public EWkfFlow getFlow() {
        return flow;
    }


    /**
     * @param flow the flow to set
     */
    public void setFlow(EWkfFlow flow) {
        this.flow = flow;
    }

    @ManyToOne
    @JoinColumn(name = "wkf_flo_ent_id", nullable = true)
    public WkfFlowEntity getWkfFlowEntity() {
        return wkfFlowEntity;
    }

    public void setWkfFlowEntity(WkfFlowEntity wkfFlowEntity) {
        this.wkfFlowEntity = wkfFlowEntity;
    }

    /**
     * @return the fromStatus
     */
    @Column(name = "wkf_from_sta_id", nullable = true)
    @Convert(converter = EWkfStatus.class)
    public EWkfStatus getFromStatus() {
        return fromStatus;
    }

    /**
     * @param fromStatus the fromStatus to set
     */
    public void setFromStatus(EWkfStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    /**
     * @return the toStatus
     */
    @Column(name = "wkf_to_sta_id", nullable = false)
    @Convert(converter = EWkfStatus.class)
    public EWkfStatus getToStatus() {
        return toStatus;
    }

    /**
     * @param toStatus the toStatus to set
     */
    public void setToStatus(EWkfStatus toStatus) {
        this.toStatus = toStatus;
    }

    /**
     * @return the beforeAction
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "before_act_exe_id", nullable = true)
    public ActionExecution getBeforeAction() {
        return beforeAction;
    }

    /**
     * @param beforeAction the beforeAction to set
     */
    public void setBeforeAction(ActionExecution beforeAction) {
        this.beforeAction = beforeAction;
    }

    /**
     * @return the afterAction
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_act_exe_id", nullable = true)
    public ActionExecution getAfterAction() {
        return afterAction;
    }

    /**
     * @param afterAction the afterAction to set
     */
    public void setAfterAction(ActionExecution afterAction) {
        this.afterAction = afterAction;
    }


    /**
     * @return the flowActions
     */
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public List<WkfFlowControl> getFlowActions() {
        if (flowActions == null) {
            flowActions = new ArrayList<>();
        }
        return flowActions;
    }


    /**
     * @param flowActions the flowActions to set
     */
    public void setFlowActions(List<WkfFlowControl> flowActions) {
        this.flowActions = flowActions;
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
     *
     */
    @Transient
    public List<SecProfile> getProfiles() {
        List<SecProfile> profiles = new ArrayList<>();
        List<WkfFlowControl> flowActions = getFlowActions();
        for (WkfFlowControl flowAct : flowActions) {
//			profiles.add(flowAct.getAction().getControlProfilePrivileges().);
        }
        return profiles;
    }

    /**
     * @param pro
     * @return
     */
    @Transient
    public boolean hasProfile(SecProfile pro) {
        List<WkfFlowControl> flowActions = getFlowActions();
        for (WkfFlowControl flowAct : flowActions) {
        }
        return false;
    }
}

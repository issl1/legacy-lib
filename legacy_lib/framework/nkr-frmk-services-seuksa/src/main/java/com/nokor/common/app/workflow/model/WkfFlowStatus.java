package com.nokor.common.app.workflow.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * List of statuses defined for a given WkfFlow.
 * 
 * @author prasnar
 * 
 */
@Entity
@Table(name = "tu_wkf_flow_status")
public class WkfFlowStatus extends EntityA {
	/** */
	private static final long serialVersionUID = -6976659498760207747L;

	private EWkfFlow flow;
	private EWkfStatus status;
	private Integer sortIndex;
	private WkfFlowEntity wkfFlowEntity;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_flo_sta_id", unique = true, nullable = false)
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
	 * @return the status
	 */
    @ManyToOne
    @JoinColumn(name="wkf_sta_id", nullable = false)
	public EWkfStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(EWkfStatus status) {
		this.status = status;
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
}

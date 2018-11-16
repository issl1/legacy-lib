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

import com.nokor.frmk.security.model.SecProfile;

/**
 * List of profiles authorized to access/read a given status for a given WkfFlow.
 *  
 * @author prasnar
 * 
 */
@Entity
@Table(name = "tu_wkf_flow_profile")
public class WkfFlowProfile extends EntityA {
	/** */
	private static final long serialVersionUID = -5330768871617139726L;

	private EWkfFlow flow;
	private SecProfile profile;
	private EWkfStatus status;
	private WkfFlowEntity wkfFlowEntity;
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_pro_id", unique = true, nullable = false)
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
	 * @return the profile
	 */
    @ManyToOne
    @JoinColumn(name="sec_pro_id", nullable = false)
	public SecProfile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(SecProfile profile) {
		this.profile = profile;
	}

	/**
	 * @return the status
	 */
    @Column(name = "wkf_sta_id", nullable = false)
    @Convert(converter = EWkfStatus.class)
	public EWkfStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(EWkfStatus status) {
		this.status = status;
	}


}

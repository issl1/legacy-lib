package com.nokor.common.app.history.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.eref.BaseERefEntity;

import com.nokor.common.app.workflow.model.EWkfStatus;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="ts_histo_reason")
public class EHistoReason extends BaseERefEntity implements AttributeConverter<EHistoReason, Long> {
	/** */
	private static final long serialVersionUID = 7373672127875930550L;

	public final static EHistoReason WKF_CHANGE_STATUS = new EHistoReason("WKF_CHANGE_STATUS", 1); 
	public final static EHistoReason WKF_HISTO_ACTIVITY = new EHistoReason("WKF_HISTO_ACTIVITY", 2); 

	private EMainEntity entity;
	private EWkfStatus wkfStatus;
	
	/**
	 * 
	 */
	public EHistoReason() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EHistoReason(String code, long id) {
		super(code, id);
	}

	@Override
	public EHistoReason convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EHistoReason arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "his_rea_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
		
	/**
	 * @return the entity
	 */
    @Column(name = "mai_ent_id", nullable = true)
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
	 * @return the status
	 */
    @Column(name = "wkf_sta_id", nullable = true)
    @Convert(converter = EWkfStatus.class)
	public EWkfStatus getWkfStatus() {
		return wkfStatus;
	}


	/**
	 * @param status the status to set
	 */
	public void setWkfStatus(EWkfStatus status) {
		this.wkfStatus = status;
	}
	/**
	 * 
	 * @return
	 */
	public static List<EHistoReason> values() {
		return getValues(EHistoReason.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EHistoReason getById(long id) {
		return getById(EHistoReason.class, id);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EHistoReason getByCode(String code) {
		return getByCode(EHistoReason.class, code);
	}

	/**
	 * @return
	 */
	public static List<EHistoReason> getForceHistoriesReason() {
		List<EHistoReason> reasons = new ArrayList<>();
		reasons.add(getByCode("CONTRACT_MKT_REQUEST"));
		return reasons;
	}
	
	/**
	 * @return
	 */
	public static List<EHistoReason> getHoldPaymentHistoriesReason() {
		List<EHistoReason> reasons = new ArrayList<>();
		reasons.add(getByCode("CONTRACT_AUCTON_DUE"));
		reasons.add(getByCode("CONTRACT_MKT_REQUEST"));
		return reasons;
	}
}
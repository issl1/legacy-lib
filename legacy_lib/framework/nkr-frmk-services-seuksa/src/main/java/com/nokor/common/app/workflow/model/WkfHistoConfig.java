package com.nokor.common.app.workflow.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_wkf_cfg_histo_config")
public class WkfHistoConfig extends EntityA implements MWkfHistoConfig {
	/** */
	private static final long serialVersionUID = -1847574219148464070L;

	private EMainEntity entity;
	private String histClassName;
	private String histItemClassName;
	private String tempItemClassName;
	private String histProperties;
	private String tempProperties;

	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_cfg_his_id", unique = true, nullable = false)
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
	 * @return the histClassName
	 */
	@Column(name = "wkf_cfg_his_class_name", nullable = true)
	public String getHistClassName() {
		return histClassName;
	}


	/**
	 * @param histClassName the histClassName to set
	 */
	public void setHistClassName(String histClassName) {
		this.histClassName = histClassName;
	}


	/**
	 * @return the histItemClassName
	 */
	@Column(name = "wkf_cfg_his_item_class_name", nullable = true)
	public String getHistItemClassName() {
		return histItemClassName;
	}


	/**
	 * @param histItemClassName the histItemClassName to set
	 */
	public void setHistItemClassName(String histItemClassName) {
		this.histItemClassName = histItemClassName;
	}


	/**
	 * @return the tempItemClassName
	 */
	@Column(name = "wkf_cfg_tmp_item_class_name", nullable = true)
	public String getTempItemClassName() {
		return tempItemClassName;
	}


	/**
	 * @param tempItemClassName the tempItemClassName to set
	 */
	public void setTempItemClassName(String tempItemClassName) {
		this.tempItemClassName = tempItemClassName;
	}


	/**
	 * @return the histProperties
	 */
	@Column(name = "wkf_cfg_his_histo_properties", nullable = true)
	public String getHistProperties() {
		return histProperties;
	}


	/**
	 * @param histProperties the histProperties to set
	 */
	public void setHistProperties(String histProperties) {
		this.histProperties = histProperties;
	}


	/**
	 * @return the tempProperties
	 */
	@Column(name = "wkf_cfg_his_temp_properties", nullable = true)
	public String getTempProperties() {
		return tempProperties;
	}


	/**
	 * @param tempProperties the tempProperties to set
	 */
	public void setTempProperties(String tempProperties) {
		this.tempProperties = tempProperties;
	}


}

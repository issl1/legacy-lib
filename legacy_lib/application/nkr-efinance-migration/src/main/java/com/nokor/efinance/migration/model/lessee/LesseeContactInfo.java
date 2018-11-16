package com.nokor.efinance.migration.model.lessee;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tm_lessee_contact_info")
public class LesseeContactInfo extends EntityRefA implements Serializable {
	
	/** 
	 */
	private static final long serialVersionUID = 4185518500380589690L;
	
	private Long id;
	private String migrationID;
	private String typeInfoCode;
	private String typeAddressCode;
	private String value;
	
	/**
	 * @return the id
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the migrationID
	 */
	public String getMigrationID() {
		return migrationID;
	}
	/**
	 * @param migrationID the migrationID to set
	 */
	public void setMigrationID(String migrationID) {
		this.migrationID = migrationID;
	}
	/**
	 * @return the typeInfoCode
	 */
	public String getTypeInfoCode() {
		return typeInfoCode;
	}
	/**
	 * @param typeInfoCode the typeInfoCode to set
	 */
	public void setTypeInfoCode(String typeInfoCode) {
		this.typeInfoCode = typeInfoCode;
	}
	/**
	 * @return the typeAddressCode
	 */
	public String getTypeAddressCode() {
		return typeAddressCode;
	}
	/**
	 * @param typeAddressCode the typeAddressCode to set
	 */
	public void setTypeAddressCode(String typeAddressCode) {
		this.typeAddressCode = typeAddressCode;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}

package com.nokor.ersys.core.hr.model.organization;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;

/**
 * Tel, Email, Fax, Skype, Yahoo, LinkedIn...
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class BaseContactInfo extends EntityA {
	/** */
	private static final long serialVersionUID = 8034645608224296142L;

	private ETypeContactInfo typeInfo;
	private ETypeAddress typeAddress;
	private String value;
	private String remark;
	private boolean primary;
	


	/**
	 * @return the typeInfo
	 */
    @Column(name = "typ_cnt_inf_id", nullable = false)
    @Convert(converter = ETypeContactInfo.class)
	public ETypeContactInfo getTypeInfo() {
		return typeInfo;
	}

	/**
	 * @param typeInfo the typeInfo to set
	 */
	public void setTypeInfo(ETypeContactInfo typeInfo) {
		this.typeInfo = typeInfo;
	}

	/**
	 * @return the typeAddress
	 */
	@Column(name = "typ_add_id", nullable = true)
    @Convert(converter = ETypeAddress.class)
	public ETypeAddress getTypeAddress() {
		return typeAddress;
	}

	/**
	 * @param typeAddress the typeAddress to set
	 */
	public void setTypeAddress(ETypeAddress typeAddress) {
		this.typeAddress = typeAddress;
	}
	
	/**
	 * @return the value
	 */
	@Column(name = "cnt_inf_value", nullable = false, length = 255)
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the primary
	 */
	@Column(name = "cnt_inf_primary", nullable = true, columnDefinition = "boolean default false")
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * @param primary the primary to set
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * @return the remark
	 */
	@Column(name = "cnt_inf_remark", nullable = true, length = 100)
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
   	
}

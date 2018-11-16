package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.common.app.eref.ECountry;
import com.nokor.frmk.security.model.SecUser;

/**
 * An area can covers one or several districts
 * 
 * @author prasnar
 */
@Entity
@Table(name = "tu_col_area")
public class EColArea extends EntityRefA {
	/** */
	private static final long serialVersionUID = -1689786438480248606L;

	private String street;
	private String line1;
	private String line2;
	private String postalCode;
	private String remark;
	
	private Province province;
	private District district;
	private Commune commune;
	private Village village;
	private ECountry country;
	
	private List<SecUser> staffs;
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_are_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "col_are_code", nullable = false, length=10)
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "col_are_desc", nullable = true, length=255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "col_are_desc_en", nullable = false, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * @return the street
	 */
	@Column(name = "col_are_street", nullable = true, length = 100)
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the line1
	 */
	@Column(name = "col_are_line1", nullable = true, length = 100)
	public String getLine1() {
		return line1;
	}

	/**
	 * @param line1 the line1 to set
	 */
	public void setLine1(String line1) {
		this.line1 = line1;
	}

	/**
	 * @return the line2
	 */
	@Column(name = "col_are_line2", nullable = true, length = 100)
	public String getLine2() {
		return line2;
	}

	/**
	 * @param line2 the line2 to set
	 */
	public void setLine2(String line2) {
		this.line2 = line2;
	}	

	/**
	 * @return the postalCode
	 */
	@Column(name = "col_are_postal_code", nullable = true, length = 10)
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the province
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id", nullable = true)
	public Province getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(Province province) {
		this.province = province;
	}

	/**
	 * @return the district
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dis_id", nullable = true)
	public District getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(District district) {
		this.district = district;
	}

	/**
	 * @return the commune
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "com_id", nullable = true)
	public Commune getCommune() {
		return commune;
	}

	/**
	 * @param commune the commune to set
	 */
	public void setCommune(Commune commune) {
		this.commune = commune;
	}

	/**
	 * @return the village
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vil_id", nullable = true)
	public Village getVillage() {
		return village;
	}

	/**
	 * @param village the village to set
	 */
	public void setVillage(Village village) {
		this.village = village;
	}

	/**
	 * @return the country
	 */
    @Column(name = "cou_id", nullable = true)
    @Convert(converter = ECountry.class)
	public ECountry getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(ECountry country) {
		this.country = country;
	}
	
	/**
	 * @return the remark
	 */
	@Column(name = "col_are_remark", nullable = true, length = 255)
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}	
	
	/**
	 * @return the colTypes
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="tu_col_area_staff",
				joinColumns = { @JoinColumn(name = "col_are_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "sec_usr_id") })
	public List<SecUser> getStaffs() {
		return staffs;
	}

	/**
	 * @param staffs the staffs to set
	 */
	public void setStaffs(List<SecUser> staffs) {
		this.staffs = staffs;
	}
		
}

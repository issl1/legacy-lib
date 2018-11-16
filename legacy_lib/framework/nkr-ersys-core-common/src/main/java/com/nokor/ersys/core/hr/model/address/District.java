package com.nokor.ersys.core.hr.model.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_district",
	indexes = {
		@Index(name = "tu_district_pro_id_idx", columnList = "pro_id")
	}
)
public class District extends EntityRefA implements MDistrict {
	/** */
	private static final long serialVersionUID = 6964092816403743153L;

	private Province province;
	private String gpsCoordinates;
	private Double latitude;
	private Double longitude;

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dis_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "dis_code", nullable = false, length=10)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "dis_desc", nullable = false, length=255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "dis_desc_en", nullable = true, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * @return the province
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id")
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
	 * @return the gpsCoordinates
	 */
	@Column(name = "dis_gps_coordinates", nullable = true, columnDefinition = "TEXT")
	public String getGpsCoordinates() {
		return gpsCoordinates;
	}

	/**
	 * @param gpsCoordinates the gpsCoordinates to set
	 */
	public void setGpsCoordinates(String gpsCoordinates) {
		this.gpsCoordinates = gpsCoordinates;
	}
	
	/**
	 * @return the latitude
	 */
    @Column(name = "dis_latitude", nullable = true)
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	@Column(name = "dis_longitude", nullable = true)
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}

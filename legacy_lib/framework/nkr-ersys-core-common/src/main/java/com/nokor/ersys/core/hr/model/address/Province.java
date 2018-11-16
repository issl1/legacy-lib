package com.nokor.ersys.core.hr.model.address;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.common.app.eref.ECountry;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_province",
	indexes = {
		@Index(name = "tu_province_cou_id_idx", columnList = "cou_id")
	}
)
public class Province extends EntityRefA implements MProvince {
	/** */
	private static final long serialVersionUID = -7266873441516436821L;

	private ECountry country;
	private String shortCode;
	private String gpsCoordinates;
	private Double latitude;
	private Double longitude;

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "pro_code", nullable = false, length=10)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "pro_desc", nullable = false, length=255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "pro_desc_en", nullable = true, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }	
	
    /**
	 * @return the shortCode
	 */
	@Column(name = "pro_short_code", nullable = true, length = 15)
	public String getShortCode() {
		return shortCode;
	}

	/**
	 * @param shortCode the shortCode to set
	 */
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
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
	 * @return the gpsCoordinates
	 */
	@Column(name = "pro_gps_coordinates", nullable = true, columnDefinition = "TEXT")
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
    @Column(name = "pro_latitude", nullable = true)
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
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the longitude
	 */
	@Column(name = "pro_longitude", nullable = true)
	public Double getLongitude() {
		return longitude;
	}
}

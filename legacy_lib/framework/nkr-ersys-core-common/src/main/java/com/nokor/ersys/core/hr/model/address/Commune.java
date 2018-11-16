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
@Table(name = "tu_commune",
	indexes = {
		@Index(name = "tu_commune_dis_id_idx", columnList = "dis_id")
	}
)
public class Commune extends EntityRefA implements MCommune {
	/** */
	private static final long serialVersionUID = -3304174388125531239L;

	private District district;
	private String gpsCoordinates;
	private Double latitude;
	private Double longitude;
	private String postalCode;
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "com_code", nullable = false, length=20)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "com_desc", nullable = false, length=255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "com_desc_en", nullable = true, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * @return the district
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dis_id")
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
	 * @return the gpsCoordinates
	 */
	@Column(name = "com_gps_coordinates", nullable = true, columnDefinition = "TEXT")
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
    @Column(name = "com_latitude", nullable = true)
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
	@Column(name = "com_longitude", nullable = true)
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the postalCode
	 */
	@Column(name = "com_postal_code", nullable = true, length = 10)
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}	
}

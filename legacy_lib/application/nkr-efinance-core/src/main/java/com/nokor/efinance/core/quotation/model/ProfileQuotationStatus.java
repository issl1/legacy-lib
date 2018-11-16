package com.nokor.efinance.core.quotation.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.frmk.security.model.SecProfile;

/**
 * Color
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_profile_quotation_status")
public class ProfileQuotationStatus extends EntityA {
	
	private static final long serialVersionUID = -3182929089997722399L;

	private SecProfile profile;
	private EWkfStatus quotationStatus; 
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_quo_sta_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the profile
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_pro_id")
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
	 * @return the quotationStatus
	 */
    @Column(name = "quo_sta_id", nullable = false)
    @Convert(converter = EWkfStatus.class)
	public EWkfStatus getWkfStatus() {
		return quotationStatus;
	}

	/**
	 * @param quotationStatus the quotationStatus to set
	 */
	public void setWkfStatus(EWkfStatus quotationStatus) {
		this.quotationStatus = quotationStatus;
	}

}

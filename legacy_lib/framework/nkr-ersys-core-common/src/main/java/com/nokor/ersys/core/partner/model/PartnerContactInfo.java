package com.nokor.ersys.core.partner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.ersys.core.hr.model.organization.BaseContactInfo;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_partner_contact_info")
public class PartnerContactInfo extends BaseContactInfo {
	/** */
	private static final long serialVersionUID = -3792626973835168839L;

	private Partner partner;
	
	/**
	 * @return
	 */
	public static PartnerContactInfo createInstance() {
		PartnerContactInfo contactInfo = EntityFactory.createInstance(PartnerContactInfo.class);
        return contactInfo;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cnt_inf_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the partner
	 */
	@ManyToOne
	@JoinColumn(name="par_id", nullable = false)
	public Partner getPartner() {
		return partner;
	}

	/**
	 * @param partner the partner to set
	 */
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
}

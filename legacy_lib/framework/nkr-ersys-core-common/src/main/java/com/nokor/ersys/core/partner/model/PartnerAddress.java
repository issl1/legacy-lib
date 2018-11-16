package com.nokor.ersys.core.partner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.address.Address;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_partner_address")
public class PartnerAddress extends EntityA {
	/** */
	private static final long serialVersionUID = -4645415405941017697L;

	private Partner partner;
	private Address address;
	
	/**
	 * 
	 * @return
	 */
	public static PartnerAddress createInstance() {
        final PartnerAddress partnerAddr = EntityFactory.createInstance(PartnerAddress.class);
        partnerAddr.setAddress(Address.createInstance());
        return partnerAddr;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "par_add_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the partner
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "par_id", nullable = false)
	public Partner getPartner() {
		return partner;
	}

	/**
	 * @param partner the partner to set
	 */
	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	/**
	 * @return the address
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_id", nullable = false)
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	
	
}

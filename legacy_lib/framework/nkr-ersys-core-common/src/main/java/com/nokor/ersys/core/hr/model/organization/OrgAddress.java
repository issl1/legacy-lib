package com.nokor.ersys.core.hr.model.organization;

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
@Table(name = "tu_org_address")
public class OrgAddress extends EntityA {
	/** */
	private static final long serialVersionUID = 2025501521773069222L;

	private Organization organization;
	private OrgStructure orgStructure;
	private Address address;
	
	public static OrgAddress createInstance() {
        final OrgAddress orgAddr = EntityFactory.createInstance(OrgAddress.class);
        orgAddr.setAddress(Address.createInstance());
        return orgAddr;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_add_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the organization
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = true)
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the orgStructure
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_str_id", nullable = true)
	public OrgStructure getOrgStructure() {
		return orgStructure;
	}

	/**
	 * @param orgStructure the orgStructure to set
	 */
	public void setOrgStructure(OrgStructure orgStructure) {
		this.orgStructure = orgStructure;
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

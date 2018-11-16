package com.nokor.ersys.core.hr.model.organization;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Zone;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_org_structure",
	indexes = {
		@Index(name = "tu_org_structure_parent_org_str_id_idx", columnList = "parent_org_str_id"),
		@Index(name = "tu_org_structure_org_id_idx", columnList = "org_id"),
		@Index(name = "tu_org_structure_contact_emp_id_idx", columnList = "contact_emp_id"),
		@Index(name = "tu_org_structure_org_grp_id_idx", columnList = "org_grp_id")
	}
)
public class OrgStructure extends BaseOrgStructure implements MOrgStructure {
	/** */
	private static final long serialVersionUID = -1588512402814307220L;

	private OrgStructure parent;
	private Organization organization;
	private Employee contact;

	private List<OrgAddress> orgAddresses;
	private List<OrgStructure> children;
	
	private OrgGroup group;
	private Zone zone;
    
    /**
     * Create Instance
     * @return
     */
    public static OrgStructure createInstance() {
    	OrgStructure orgEntity = EntityFactory.createInstance(OrgStructure.class);
		return orgEntity;
    }

	/**
	 * @return the organization
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="org_id", nullable = true)
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
	 * @return the parent
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_org_str_id", nullable = true)
	public OrgStructure getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(OrgStructure parent) {
		this.parent = parent;
	}
	

	/**
	 * @return the children
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	public List<OrgStructure> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<OrgStructure> children) {
		this.children = children;
	}	
	
	/**
	 * @return the orgAddresses
	 */
	@OneToMany(mappedBy="orgStructure", fetch = FetchType.LAZY)
	public List<OrgAddress> getOrgAddresses() {
		return orgAddresses;
	}

	/**
	 * @param orgAddresses the orgAddresses to set
	 */
	public void setOrgAddresses(List<OrgAddress> orgAddresses) {
		this.orgAddresses = orgAddresses;
	}

	/**
	 * @return the contact
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="contact_emp_id", nullable = true)
	public Employee getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(Employee contact) {
		this.contact = contact;
	}

	/**
	 * @return the zone
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="zon_id", nullable = true)
	public Zone getZone() {
		return zone;
	}

	/**
	 * @param zone the zone to set
	 */
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	@Transient
	public Address getMainAddress() {
		if (orgAddresses != null) {
			for (OrgAddress orgAddress : orgAddresses) {
				if (orgAddress.getAddress().getType().equals(ETypeAddress.MAIN)) {
					return orgAddress.getAddress();
				}
			}
		}
		return null;
	}

	/**
	 * @return the group
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="org_grp_id", nullable = true)
	public OrgGroup getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(OrgGroup group) {
		this.group = group;
	}
	
}

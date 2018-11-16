package com.nokor.ersys.core.hr.model.organization;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.IVersion;

import com.nokor.common.app.tools.helper.AppSettingConfigHelper;
import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;

/**
 * @author prasnar
 *
 */
@Entity
@Table(name="tu_organization")
public class Organization extends BaseOrganization implements MOrganization, IVersion {
	/** */
	private static final long serialVersionUID = 4940164029182139445L;

	private List<OrgStructure> orgStructures;
	private List<Employee> employees;	
	private Organization parent;
	private List<Organization> children;
	private List<OrgAddress> orgAddresses;
	private List<OrgAccountHolder> orgAccountHolders;
	private List<OrgBankAccount> orgBankAccounts;

	private Long version;


	/**
     * 
     * @return
     */
    public static Organization createInstance() {
        Organization com = BaseOrganization.createInstance(Organization.class);
        return com;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
   
    
    /**
	 * @return the orgStructures
	 */
	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
	public List<OrgStructure> getOrgStructures() {
		if (orgStructures == null) {
			orgStructures = new ArrayList<OrgStructure>();
		}
		return orgStructures;
	}

	/**
	 * @param orgStructures the orgStructures to set
	 */
	public void setOrgStructures(List<OrgStructure> orgStructures) {
		this.orgStructures = orgStructures;
	}

	/**
	 * @return the employees
	 */
	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    public List<Employee> getEmployees() {
    	if (employees == null) {
    		employees = new ArrayList<Employee>();
    	}
    	return employees;
    }

	/**
	 * @param employees the employees to set
	 */
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}	
	


	/**
	 * 
	 * @return
	 */
	public static Organization getMainOrganization() {
		return AppSettingConfigHelper.getMainOrganization();
	}
	
	/**
	 * @return the parent
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_org_id", nullable = true)
	public Organization getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Organization parent) {
		this.parent = parent;
	}
  
	/**
	 * @return the children
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	public List<Organization> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Organization> children) {
		this.children = children;
	}
	
	/**
	 * 
	 * @return
	 */
//	@Transient
//	public List<Organization> getSubsidiaries() {
//		List<Organization> subsidiairies = new ArrayList<Organization>();
//		List<OrgStructure> structures = getOrgStructuresByLevel(EOrgLevel.SUBSIDIARY);
//		
//		for (OrgStructure org : structures) {
//			subsidiairies.add(org.getCompany());
//		}
//		
//		return subsidiairies;
//	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public List<OrgStructure> getBranches() {
		return getOrgStructuresByLevel(EOrgLevel.BRANCH);
	}
	
	
	
	/**
	 * 
	 * @param orgLevel
	 * @return
	 */
	@Transient
	public List<OrgStructure> getOrgStructuresByLevel(EOrgLevel orgLevel) {
		List<OrgStructure> structures = new ArrayList<OrgStructure>();
		List<OrgStructure> orgAll = getOrgStructures();
		for (OrgStructure org : orgAll) {
			if (org.getLevel().equals(orgLevel)) {
				structures.add(org);
			}
		}
		return structures;
	}
	

	/**
	 * @return the orgAddresses
	 */
	@OneToMany(mappedBy="organization", fetch = FetchType.LAZY)
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
	 * @return the orgAccountHolders
	 */
	@OneToMany(mappedBy="organization", fetch = FetchType.LAZY)
	public List<OrgAccountHolder> getOrgAccountHolders() {
		return orgAccountHolders;
	}

	/**
	 * @param orgAccountHolders the orgAccountHolders to set
	 */
	public void setOrgAccountHolders(List<OrgAccountHolder> orgAccountHolders) {
		this.orgAccountHolders = orgAccountHolders;
	}

	/**
	 * @return the orgBankAccounts
	 */
	@OneToMany(mappedBy="organization", fetch = FetchType.LAZY)
	public List<OrgBankAccount> getOrgBankAccounts() {
		return orgBankAccounts;
	}

	/**
	 * @param orgBankAccounts the orgBankAccounts to set
	 */
	public void setOrgBankAccounts(List<OrgBankAccount> orgBankAccounts) {
		this.orgBankAccounts = orgBankAccounts;
	}

	/**
     * @return the mainContacts
     */
    @Transient
    public List<Employee> getMainContacts() {
    	List<Employee> mainContacts = new ArrayList<Employee>();
        List<Employee> employees = getEmployees();

        if (employees == null) {
            return mainContacts;
        }
    
        for (Employee emp : employees) {
        	boolean isValid = emp.getStatusRecord() == null;
        	if (emp.getStatusRecord() != null) {
        		isValid = emp.getStatusRecord().equals(EStatusRecord.ACTIV);
        	}
        	if (isValid) {
                if (emp.getTypeContact() != null && ETypeContact.MAIN.equals(emp.getTypeContact().getId())) {
                	mainContacts.add(emp);
                }
        	}
        }
    
        return mainContacts;
    }
    
    @Transient
    public Employee getFirstMainContact() {
    	List<Employee> mainContacts = getMainContacts();
    	if (mainContacts.size() > 0) {
    		return mainContacts.get(0);
    	}
        return null;
    }
    
    @Transient
    public boolean isInMainContacts() {
    	List<Employee> mainContacts = getMainContacts();
    	for (Employee contact : mainContacts) {
			if (contact.getId().equals(getId())) {
				return true;
			}
		}
    	return false;
    }

	/**
	 * @return the version
	 */
    @Version
    @Column(name = "version", nullable = false)
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * Can be overridden to false to disable the Workflow
	 * @return
	 */
	@Transient
	public boolean isWkfEnabled() {
		return false;
	}	
}

package com.nokor.efinance.core.applicant.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_individual")
public class Individual extends AbstractIndividual {
	/** */
	private static final long serialVersionUID = 6949885917223461002L;

	private List<IndividualAddress> individualAddresses;
	private List<Employment> employments;
	
	private List<IndividualContactInfo> individualContactInfos;
	private List<IndividualReferenceInfo> individualReferenceInfos;
	private List<IndividualSpouse> individualSpouses;
		
	/**
     * 
     * @return
     */
    public static Individual createInstance() {
        Individual individual = EntityFactory.createInstance(Individual.class);
        return individual;
    }
    
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ind_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @return the individualAddresses
	 */
	@OneToMany(mappedBy="individual", fetch = FetchType.LAZY)
	public List<IndividualAddress> getIndividualAddresses() {
		return individualAddresses;
	}

	/**
	 * @param individualAddresses the individualAddresses to set
	 */
	public void setIndividualAddresses(List<IndividualAddress> individualAddresses) {
		this.individualAddresses = individualAddresses;
	}

	/**
	 * @return the employments
	 */
	@OneToMany(mappedBy="individual", fetch = FetchType.LAZY)
	public List<Employment> getEmployments() {
		if (employments == null) {
			employments = new ArrayList<>();
		}
		return employments;
	}

	/**
	 * @param employments the employments to set
	 */
	public void setEmployments(List<Employment> employments) {
		this.employments = employments;
	}	
	
	/**
	 * @return the individualContactInfos
	 */
	@OneToMany(mappedBy="individual", fetch = FetchType.LAZY)
	public List<IndividualContactInfo> getIndividualContactInfos() {
		return individualContactInfos;
	}


	/**
	 * @param individualContactInfos the individualContactInfos to set
	 */
	public void setIndividualContactInfos(
			List<IndividualContactInfo> individualContactInfos) {
		this.individualContactInfos = individualContactInfos;
	}


	/**
	 * @return the individualReferenceInfos
	 */
	@OneToMany(mappedBy="individual", fetch = FetchType.LAZY)
	public List<IndividualReferenceInfo> getIndividualReferenceInfos() {
		return individualReferenceInfos;
	}


	/**
	 * @param individualReferenceInfos the individualReferenceInfos to set
	 */
	public void setIndividualReferenceInfos(
			List<IndividualReferenceInfo> individualReferenceInfos) {
		this.individualReferenceInfos = individualReferenceInfos;
	}	

	
	/**
	 * @return the individualSpouses
	 */
	@OneToMany(mappedBy="individual", fetch = FetchType.LAZY)
	public List<IndividualSpouse> getIndividualSpouses() {
		if (individualSpouses == null) {
			individualSpouses = new ArrayList<IndividualSpouse>();
		}
		return individualSpouses;
	}


	/**
	 * @param individualSpouses the individualSpouses to set
	 */
	public void setIndividualSpouses(List<IndividualSpouse> individualSpouses) {
		this.individualSpouses = individualSpouses;
	}
	
	/**
	 * @return
	 */
	@Transient
	public IndividualSpouse getIndividualSpouse() {
		if (individualSpouses != null && !individualSpouses.isEmpty()) {
			return individualSpouses.get(0);
		}
		return null;
	}


	/**
	 * Add a new employment
	 * @param employment
	 */
	@Transient
	public void addEmployment(Employment employment) {
		if (employments == null) {
			employments = new ArrayList<Employment>();
		}
		employments.add(employment);
	}
	
	/**
	 * Get current employment
	 * @return
	 */
	@Transient
	public Employment getCurrentEmployment() {
		if (employments != null && !employments.isEmpty()) {
			for (Employment employment : employments) {
				if (employment.getEmploymentType().equals(EEmploymentType.CURR)) {
					return employment;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get previous employments
	 * @return
	 */
	@Transient
	public List<Employment> getEmployments(EEmploymentType employmentType) {
		List<Employment> prevEmployments = new ArrayList<Employment>();
		if (employments != null && !employments.isEmpty()) {
			for (Employment employment : employments) {
				if (employment.getEmploymentType().equals(employmentType)) {
					prevEmployments.add(employment);
				}
			}
		}
		return prevEmployments;
	}
		
	
	/**
	 * Add a new address
	 * @param address
	 * @param addressType
	 */
	@Transient
	public void setAddress(Address address, ETypeAddress addressType) {
		if (individualAddresses == null) {
			individualAddresses = new ArrayList<>();
		}
		
		IndividualAddress individualAddress = getIndividualAddress(addressType);
		if (individualAddress == null) {
			individualAddress = new IndividualAddress();
			individualAddresses.add(individualAddress);
		}
		address.setType(addressType);
		individualAddress.setIndividual(this);
		individualAddress.setAddress(address);
	}
	
	
	/**
	 * Get main address
	 * @return
	 */
	@Transient
	public IndividualAddress getIndividualAddress(ETypeAddress addressType) {
		if (individualAddresses != null && !individualAddresses.isEmpty()) {
			for (IndividualAddress individualAddress : individualAddresses) {
				if (addressType.equals(individualAddress.getAddress().getType())) {
					return individualAddress;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param isPrimaryPhone
	 * @return
	 */
	@Transient
	public String getIndividualPrimaryContactInfo() {
		if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
			for (IndividualContactInfo indContactInfo : individualContactInfos) {
				String primaryContact = getPrimaryContactInfo(indContactInfo.getContactInfo());
				if (StringUtils.isNotEmpty(primaryContact)) {
					return primaryContact;
				}
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @param info
	 * @return
	 */
	@Transient
	public String getPrimaryContactInfo(ContactInfo info) {
		if (info.isPrimary()) {
			if (ETypeContactInfo.LANDLINE.equals(info.getTypeInfo()) 
					|| ETypeContactInfo.MOBILE.equals(info.getTypeInfo())) {
				return info.getValue() == null ? "" : info.getValue();
			}
		} 
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public String getIndividualSecondaryContactInfo() {
		if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
			for (IndividualContactInfo indContactInfo : individualContactInfos) {
				ContactInfo contactInfo = indContactInfo.getContactInfo();
				if (ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo()) 
						|| ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo())) {
					if (!contactInfo.isPrimary()) {
						return contactInfo.getValue() == null ? "" : contactInfo.getValue();
					}
				}
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * Get main address
	 * @return
	 */
	@Transient
	public Address getMainAddress() {
		IndividualAddress individualAddress = getIndividualAddress(ETypeAddress.MAIN);
		return individualAddress == null ? null : individualAddress.getAddress();
	}

	
}

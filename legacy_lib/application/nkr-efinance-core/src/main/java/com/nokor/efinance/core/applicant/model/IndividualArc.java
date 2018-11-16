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

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.address.model.AddressArc;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_individual_arc")
public class IndividualArc extends AbstractIndividual {
	/**
	 */
	private static final long serialVersionUID = -6975405630255952727L;
	
	private List<IndividualAddressArc> individualAddresses;
	private List<EmploymentArc> employments;
	
	private List<IndividualContactInfoArc> individualContactInfos;
	private List<IndividualReferenceInfoArc> individualReferenceInfos;
		
	/**
     * 
     * @return
     */
    public static IndividualArc createInstance() {
        IndividualArc applicant = EntityFactory.createInstance(IndividualArc.class);
        applicant.setWkfStatus(EWkfStatus.NEW);
        return applicant;
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
	public List<IndividualAddressArc> getIndividualAddresses() {
		return individualAddresses;
	}

	/**
	 * @param individualAddresses the individualAddresses to set
	 */
	public void setIndividualAddresses(List<IndividualAddressArc> individualAddresses) {
		this.individualAddresses = individualAddresses;
	}

	/**
	 * @return the employments
	 */
	@OneToMany(mappedBy="individual", fetch = FetchType.LAZY)
	public List<EmploymentArc> getEmployments() {
		if (employments == null) {
			employments = new ArrayList<>();
		}
		return employments;
	}

	/**
	 * @param employments the employments to set
	 */
	public void setEmployments(List<EmploymentArc> employments) {
		this.employments = employments;
	}	
	
	/**
	 * @return the individualContactInfos
	 */
	@OneToMany(mappedBy="individual", fetch = FetchType.LAZY)
	public List<IndividualContactInfoArc> getIndividualContactInfos() {
		return individualContactInfos;
	}


	/**
	 * @param individualContactInfos the individualContactInfos to set
	 */
	public void setIndividualContactInfos(
			List<IndividualContactInfoArc> individualContactInfos) {
		this.individualContactInfos = individualContactInfos;
	}


	/**
	 * @return the individualReferenceInfos
	 */
	@OneToMany(mappedBy="individual", fetch = FetchType.LAZY)
	public List<IndividualReferenceInfoArc> getIndividualReferenceInfos() {
		return individualReferenceInfos;
	}


	/**
	 * @param individualReferenceInfos the individualReferenceInfos to set
	 */
	public void setIndividualReferenceInfos(
			List<IndividualReferenceInfoArc> individualReferenceInfos) {
		this.individualReferenceInfos = individualReferenceInfos;
	}


	/**
	 * Add a new employment
	 * @param employment
	 */
	@Transient
	public void addEmployment(EmploymentArc employment) {
		if (employments == null) {
			employments = new ArrayList<>();
		}
		employments.add(employment);
	}
	
	/**
	 * Get current employment
	 * @return
	 */
	@Transient
	public EmploymentArc getCurrentEmployment() {
		if (employments != null && !employments.isEmpty()) {
			for (EmploymentArc employment : employments) {
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
	public List<EmploymentArc> getEmployments(EEmploymentType employmentType) {
		List<EmploymentArc> prevEmployments = new ArrayList<>();
		if (employments != null && !employments.isEmpty()) {
			for (EmploymentArc employment : employments) {
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
	public void setAddress(AddressArc address, ETypeAddress addressType) {
		if (individualAddresses == null) {
			individualAddresses = new ArrayList<>();
		}
		
		IndividualAddressArc individualAddress = getIndividualAddress(addressType);
		if (individualAddress == null) {
			individualAddress = new IndividualAddressArc();
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
	public IndividualAddressArc getIndividualAddress(ETypeAddress addressType) {
		if (individualAddresses != null && !individualAddresses.isEmpty()) {
			for (IndividualAddressArc individualAddress : individualAddresses) {
				if (individualAddress.getAddress().getType() == addressType) {
					return individualAddress;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Get main address
	 * @return
	 */
	@Transient
	public AddressArc getMainAddress() {
		IndividualAddressArc individualAddress = getIndividualAddress(ETypeAddress.MAIN);
		return individualAddress == null ? null : individualAddress.getAddress();
	}

	
}

package com.nokor.efinance.core.applicant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.nokor.efinance.core.address.model.AddressArc;
import com.nokor.ersys.core.hr.model.address.BaseAddress;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_employment_arc")
public class EmploymentArc extends AbstractEmployment {
	/** */
	private static final long serialVersionUID = 5334420857098134276L;

	private IndividualArc individual;
	private AddressArc address;
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}	

	/**
	 * @return the individual
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ind_id")
	public IndividualArc getIndividual() {
		return individual;
	}

	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(IndividualArc individual) {
		this.individual = individual;
	}

	/**
	 * @return the address
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_id")
	public AddressArc getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(AddressArc address) {
		this.address = address;
	}
	
	@Transient
	@Override
	public BaseAddress getBaseAddress() {
		return address;
	}

}

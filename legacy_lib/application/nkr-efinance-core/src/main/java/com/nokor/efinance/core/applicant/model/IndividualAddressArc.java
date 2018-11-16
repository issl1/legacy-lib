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

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.address.model.AddressArc;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_individual_address_arc")
public class IndividualAddressArc extends EntityA {
	/** */
	private static final long serialVersionUID = 7556450149731798268L;

	private IndividualArc individual;
	private AddressArc address;
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ind_add_id", unique = true, nullable = false)
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

	
}

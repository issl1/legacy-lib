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

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.address.Address;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_individual_address")
public class IndividualAddress extends EntityA {
	/** */
	private static final long serialVersionUID = 7556450149731798268L;

	private Individual individual;
	private Address address;
	
	/**
     * 
     * @return
     */
    public static IndividualAddress createInstance() {
    	IndividualAddress instance = EntityFactory.createInstance(IndividualAddress.class);
        return instance;
    }
	
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
	public Individual getIndividual() {
		return individual;
	}

	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	/**
	 * @return the address
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_id")
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

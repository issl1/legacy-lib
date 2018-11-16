package com.nokor.efinance.core.dealer.model;

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
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "tu_dealer_address")
public class DealerAddress extends EntityA {

	private static final long serialVersionUID = -4463252824397091517L;
	
	private Dealer dealer;
	private Address address;
	
	/**
     * 
     * @return
     */
    public static DealerAddress createInstance() {
    	DealerAddress instance = EntityFactory.createInstance(DealerAddress.class);
        return instance;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_add_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id", nullable = false)
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
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

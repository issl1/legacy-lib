package com.nokor.efinance.core.dealer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.organization.ContactInfo;


/**
 * @author youhort.ly
 *
 */
@Entity
@Table(name = "tu_dealer_contact_info")
public class DealerContactInfo extends EntityA {

	private static final long serialVersionUID = 9116233885593641677L;
	
	private Dealer dealer;
	private ContactInfo contactInfo;
	
	/**
	 * @return
	 */
	public static DealerContactInfo createInstance() {
		DealerContactInfo contactInfo = EntityFactory.createInstance(DealerContactInfo.class);
        return contactInfo;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_cnt_inf_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    /**
	 * @return the contactInfo
	 */
    @ManyToOne
    @JoinColumn(name="cnt_inf_id", nullable = false)
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	/**
	 * @param contactInfo the contactInfo to set
	 */
	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	/**
	 * @return the dealer
	 */
	@ManyToOne
	@JoinColumn(name="dea_id", nullable = false)
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}
	
}

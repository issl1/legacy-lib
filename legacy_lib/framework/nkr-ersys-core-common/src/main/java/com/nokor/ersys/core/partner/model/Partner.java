package com.nokor.ersys.core.partner.model;

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

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_partner")
public class Partner extends BasePartner {
	/** */
	private static final long serialVersionUID = 8938077665658252996L;

	private Partner parent;
	
	/**
     * 
     * @return
     */
    public static Partner createInstance() {
    	Partner cus = EntityFactory.createInstance(Partner.class);
        return cus;
    }
    
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "par_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @return the parent
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_par_id", nullable = true)
	public Partner getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Partner parent) {
		this.parent = parent;
	}
}

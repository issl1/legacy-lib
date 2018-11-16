package com.nokor.ersys.core.hr.model.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

/**
 * Tel, Email, Fax, Skype, Yahoo, LinkedIn...
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_contact_info")
public class ContactInfo extends BaseContactInfo {
	/** */
	private static final long serialVersionUID = -3275002858593481822L;
	
	/**
	 * 
	 * @return
	 */
	public static ContactInfo createInstance() {
		ContactInfo contactInfo = EntityFactory.createInstance(ContactInfo.class);
        return contactInfo;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cnt_inf_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	
    
}

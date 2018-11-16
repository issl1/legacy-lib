package com.nokor.efinance.core.actor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.ersys.core.hr.model.organization.BasePerson;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_third_party")
public class ThirdParty extends BasePerson {
	
	/**
	 */
	private static final long serialVersionUID = 5588684312778380400L;
	
	private String reference;
	
	/** 
     * @return
     */
    public static ThirdParty createInstance() {
        ThirdParty thirdParty = EntityFactory.createInstance(ThirdParty.class);
        thirdParty.setWkfStatus(EWkfStatus.NEW);
        return thirdParty;
    }
   
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "thi_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @return the reference
	 */
	@Column(name = "thi_va_reference", nullable = true, length = 25)
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	@Override
	@Transient
	public boolean isWkfEnabled() {
		return false;
	}
}

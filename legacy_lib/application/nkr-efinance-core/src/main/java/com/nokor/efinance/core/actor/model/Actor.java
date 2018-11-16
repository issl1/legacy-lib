package com.nokor.efinance.core.actor.model;

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

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_actor")
public class Actor extends EntityA {
	
	/**
	 */
	private static final long serialVersionUID = 5588684312778380400L;
	
	private ActorType type;
	
	private Applicant applicant;
	private Dealer dealer;
	private Organization financialCompany;
	
	/** 
     * @return
     */
    public static Actor createInstance(ActorType type) {
        Actor actor = EntityFactory.createInstance(Actor.class);
        actor.setType(type);
        return actor;
    }
   
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "act_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}	


	/**
	 * @return the type
	 */
	@Transient
	public ActorType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ActorType type) {
		this.type = type;
	}


	/**
	 * @return the applicant
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_id", nullable = true)
	public Applicant getApplicant() {
		return applicant;
	}

	/**
	 * @param applicant the applicant to set
	 */
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	/**
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dea_id", nullable = true)
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
	 * @return the financialCompany
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_com_id", nullable = true)
	public Organization getFinancialCompany() {
		return financialCompany;
	}


	/**
	 * @param financialCompany the financialCompany to set
	 */
	public void setFinancialCompany(Organization financialCompany) {
		this.financialCompany = financialCompany;
	}
	
	@Transient
	public String getName() {
		if (ActorType.APP == type) {
			return applicant.getName();
		} else {
			return "";
		}
	}
}

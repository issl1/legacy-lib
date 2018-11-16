package com.nokor.efinance.core.financial.model;

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

import com.nokor.efinance.core.dealer.model.Dealer;

/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_insurance_campaign_dealer")
public class InsuranceCampaignDealer extends EntityA {
			
	/**
	 */
	private static final long serialVersionUID = -5717048752155776586L;
	
	private Dealer dealer;
	private InsuranceCampaign insuranceCampaign;
	
	/**
	 */
	public InsuranceCampaignDealer() {
		
	}
	
	/**
	 * @param dealer
	 * @param insuranceCampaign
	 */
	public InsuranceCampaignDealer(Dealer dealer, InsuranceCampaign insuranceCampaign) {
		this.dealer = dealer;
		this.insuranceCampaign = insuranceCampaign;
	}
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ins_cam_dea_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }	

	/**
	 * @return the dealer
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id")
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
	 * @return the insuranceCampaign
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ins_cam_id")
	public InsuranceCampaign getInsuranceCampaign() {
		return insuranceCampaign;
	}

	/**
	 * @param insuranceCampaign the insuranceCampaign to set
	 */
	public void setInsuranceCampaign(InsuranceCampaign insuranceCampaign) {
		this.insuranceCampaign = insuranceCampaign;
	}	   
}

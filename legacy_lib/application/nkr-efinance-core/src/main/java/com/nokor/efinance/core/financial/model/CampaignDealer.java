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
@Table(name = "tu_campaign_dealer")
public class CampaignDealer extends EntityA implements MCampaignDealer {
			
	/**
	 */
	private static final long serialVersionUID = 6118641155558901423L;
	
	private Dealer dealer;
	private Campaign campaign;
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cam_dea_id", unique = true, nullable = false)
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
	 * @return the campaign
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cam_id")
	public Campaign getCampaign() {
		return campaign;
	}

	/**
	 * @param campaign the campaign to set
	 */
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}    
}

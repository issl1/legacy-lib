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

import com.nokor.efinance.core.asset.model.AssetMake;

/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_campaign_asset_make")
public class CampaignAssetMake extends EntityA implements MCampaignAssetMake {
			
	/**
	 */
	private static final long serialVersionUID = 6118641155558901423L;
	
	private AssetMake assetMake;
	private Campaign campaign;
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cam_ass_mak_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }			

	/**
	 * @return the assetMake
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ass_mak_id")
	public AssetMake getAssetMake() {
		return assetMake;
	}

	/**
	 * @param assetMake the assetMake to set
	 */
	public void setAssetMake(AssetMake assetMake) {
		this.assetMake = assetMake;
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

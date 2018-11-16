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

import com.nokor.efinance.core.asset.model.AssetCategory;

/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_campaign_asset_category")
public class CampaignAssetCategory extends EntityA implements MCampaignAssetCategory {
			
	/**
	 */
	private static final long serialVersionUID = 6118641155558901423L;
	
	private AssetCategory assetCategory;
	private Campaign campaign;
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cam_ass_cat_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }				

	/**
	 * @return the assetCategory
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ass_cat_id")
	public AssetCategory getAssetCategory() {
		return assetCategory;
	}

	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(AssetCategory assetCategory) {
		this.assetCategory = assetCategory;
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

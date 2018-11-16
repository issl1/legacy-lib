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

import com.nokor.efinance.core.asset.model.AssetModel;

/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_campaign_asset_model")
public class CampaignAssetModel extends EntityA implements MCampaignAssetModel {
			
	/**
	 */
	private static final long serialVersionUID = 6118641155558901423L;
	
	private AssetModel assetModel;
	private Campaign campaign;
	private Double standardFinanceAmount;
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cam_ass_mod_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }		

	/**
	 * @return the assetModel
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ass_mod_id")
	public AssetModel getAssetModel() {
		return assetModel;
	}

	/**
	 * @param assetModel the assetModel to set
	 */
	public void setAssetModel(AssetModel assetModel) {
		this.assetModel = assetModel;
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

	/**
	 * @return the standardFinanceAmount
	 */
	@Column(name = "cam_ass_mod_standard_finance_amount", nullable = true)
	public Double getStandardFinanceAmount() {
		return standardFinanceAmount;
	}

	/**
	 * @param standardFinanceAmount the standardFinanceAmount to set
	 */
	public void setStandardFinanceAmount(Double standardFinanceAmount) {
		this.standardFinanceAmount = standardFinanceAmount;
	}	
	
	
	
}

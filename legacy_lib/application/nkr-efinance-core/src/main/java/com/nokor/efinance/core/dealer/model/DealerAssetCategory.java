package com.nokor.efinance.core.dealer.model;

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
@Table(name = "tu_dealer_asset_category")
public class DealerAssetCategory extends EntityA implements MDealerAssetCategory {
			
	/**
	 */
	private static final long serialVersionUID = 6118641155558901423L;
	
	private AssetCategory assetCategory;
	private Dealer dealer;
	
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
}

package com.nokor.efinance.core.dealer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.asset.model.AssetModel;


/**
 * @author youhort.ly
 *
 */
@Entity
@Table(name = "tu_dealer_asset_model")
public class DealerAssetModel extends EntityA {

	private static final long serialVersionUID = 9116233885593641677L;
	
	private Dealer dealer;
	private AssetModel assetModel;
	
	/**
	 * @return
	 */
	public static DealerAssetModel createInstance() {
		DealerAssetModel dealerAssetModel = EntityFactory.createInstance(DealerAssetModel.class);
        return dealerAssetModel;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_ass_mod_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}    
    
    /**
	 * @return the assetModel
	 */
    @ManyToOne
    @JoinColumn(name="ass_mod_id", nullable = false)
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
	 * @return the dealer
	 */
	@ManyToOne
	@JoinColumn(name="dea_id", nullable = false)
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

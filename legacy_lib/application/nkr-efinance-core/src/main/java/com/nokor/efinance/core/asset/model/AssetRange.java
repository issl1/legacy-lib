package com.nokor.efinance.core.asset.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Asset Range Class.
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_asset_range")
public class AssetRange extends EntityRefA implements MAssetRange {

	private static final long serialVersionUID = -2109001780570872581L;

	private AssetMake assetMake;
	
	/**
     * 
     * @return
     */
    public static AssetRange createInstance() {
    	AssetRange instance = EntityFactory.createInstance(AssetRange.class);
        return instance;
    }
    
    /**
     * Get asset type's is.
     * @return The asset type's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ass_ran_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Override
    @Column(name = "ass_ran_code", nullable = true, length=10, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * Get the asset range's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "ass_ran_desc", nullable = true, length=255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the asset range's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "ass_ran_desc_en", nullable = false, length=255)
    public String getDescEn() {
        return super.getDescEn();
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
	public void setAssetMake(final AssetMake assetMake) {
		this.assetMake = assetMake;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AssetRange)) {
			return false;
		}
		AssetRange assetRange = (AssetRange) obj;
		return this.getId().equals(assetRange.getId());
	}
}

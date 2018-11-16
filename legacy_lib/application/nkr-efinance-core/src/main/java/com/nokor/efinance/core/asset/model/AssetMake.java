package com.nokor.efinance.core.asset.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Asset Make Class.
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_asset_make")
public class AssetMake extends EntityRefA implements MAssetMake {

    private static final long serialVersionUID = -8954187823136477015L;
    
    private EFinAssetType assetType;
    
    /**
     * 
     * @return
     */
    public static AssetMake createInstance() {
    	AssetMake instance = EntityFactory.createInstance(AssetMake.class);
        return instance;
    }

    /**
     * Get asset type's is.
     * @return The asset type's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ass_mak_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Override
    @Column(name = "ass_mak_code", nullable = true, length=10, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * Get the asset make's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "ass_mak_desc", nullable = true, length=255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the asset make's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "ass_mak_desc_en", nullable = false, length=255)
    public String getDescEn() {
        return super.getDescEn();
    }
    

	/**
	 * @return the assetType
	 */
    @Column(name = "ass_typ_id", nullable = true)
    @Convert(converter = EFinAssetType.class)
	public EFinAssetType getAssetType() {
		return assetType;
	}

	/**
	 * @param assetType the assetType to set
	 */
	public void setAssetType(final EFinAssetType assetType) {
		this.assetType = assetType;
	}
}

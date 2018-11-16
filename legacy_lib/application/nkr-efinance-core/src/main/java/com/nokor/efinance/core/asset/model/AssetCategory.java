package com.nokor.efinance.core.asset.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Asset Category Class.
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_asset_category")
public class AssetCategory extends EntityRefA implements MAssetCategory {

	/** */
	private static final long serialVersionUID = 3344239267196568225L;
	
	/**
     * 
     * @return
     */
    public static AssetCategory createInstance() {
    	AssetCategory assetCategory = EntityFactory.createInstance(AssetCategory.class);
        return assetCategory;
    }

    /**
     * Get asset category's is.
     * @return The asset category's is.
     */
    @Override
    @Id
    @Column(name = "ass_cat_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Override
    @Transient
    public String getCode() {
        return "TMP";
    }

    /**
     * Get the asset category's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "ass_cat_desc", nullable = true, length=255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the asset category's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "ass_cat_desc_en", nullable = false, length=255)
    public String getDescEn() {
        return super.getDescEn();
    }

}

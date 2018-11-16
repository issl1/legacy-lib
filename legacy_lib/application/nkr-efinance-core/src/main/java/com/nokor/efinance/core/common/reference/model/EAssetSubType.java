package com.nokor.efinance.core.common.reference.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Asset Type
 * 
 * @author ly.youhort
 */
public class EAssetSubType extends BaseERefData implements AttributeConverter<EAssetSubType, Long> {
	/** */
	private static final long serialVersionUID = 1632426458370405460L;
	
	private EAssetType assetType;
	
	/**
	 */
	public EAssetSubType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EAssetSubType(String code, long id, EAssetType assetType) {
		super(code, id);
		this.assetType = assetType;
	}	
	
	/**
	 * @return the assetType
	 */
	public EAssetType getAssetType() {
		return assetType;
	}

	/**
	 * @param assetType the assetType to set
	 */
	public void setAssetType(EAssetType assetType) {
		this.assetType = assetType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EAssetSubType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EAssetSubType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EAssetSubType> values() {
		return getValues(EAssetSubType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EAssetSubType getByCode(String code) {
		return getByCode(EAssetSubType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EAssetSubType getById(long id) {
		return getById(EAssetSubType.class, id);
	}

}
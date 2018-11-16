package com.nokor.efinance.core.common.reference.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Asset Type
 * 
 * @author ly.youhort
 */
public class EAssetType extends BaseERefData implements AttributeConverter<EAssetType, Long> {
	/** */
	private static final long serialVersionUID = 1632426458370405460L;
	
	/**
	 */
	public EAssetType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EAssetType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EAssetType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EAssetType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EAssetType> values() {
		return getValues(EAssetType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EAssetType getByCode(String code) {
		return getByCode(EAssetType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EAssetType getById(long id) {
		return getById(EAssetType.class, id);
	}

}
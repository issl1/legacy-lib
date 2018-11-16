package com.nokor.efinance.core.asset.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Asset Type
 * 
 * @author ly.youhort
 */
public class EFinAssetType extends BaseERefData implements AttributeConverter<EFinAssetType, Long> {
	/** */
	private static final long serialVersionUID = 1632426458370405460L;

	public final static EFinAssetType MOTO = new EFinAssetType("MOTO", 1);
	public final static EFinAssetType CAR = new EFinAssetType("CAR", 2);

	
	/**
	 */
	public EFinAssetType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EFinAssetType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EFinAssetType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EFinAssetType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EFinAssetType> values() {
		return getValues(EFinAssetType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EFinAssetType getByCode(String code) {
		return getByCode(EFinAssetType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EFinAssetType getById(long id) {
		return getById(EFinAssetType.class, id);
	}

}
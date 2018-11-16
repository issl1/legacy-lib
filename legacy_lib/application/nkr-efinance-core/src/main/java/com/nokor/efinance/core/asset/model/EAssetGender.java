package com.nokor.efinance.core.asset.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Represents the gender of n asset
 * 
 * @author prasnar
 *
 */
public class EAssetGender extends BaseERefData implements AttributeConverter<EAssetGender, Long> {
	/** */
	private static final long serialVersionUID = -5726941611798488855L;

	public final static EAssetGender M = new EAssetGender("M", 1);
	public final static EAssetGender F = new EAssetGender("F", 2);

	
	/**
	 * 
	 */
	public EAssetGender() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EAssetGender(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EAssetGender convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EAssetGender arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EAssetGender> values() {
		return getValues(EAssetGender.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EAssetGender getByCode(String code) {
		return getByCode(EAssetGender.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EAssetGender getById(long id) {
		return getById(EAssetGender.class, id);
	}

}
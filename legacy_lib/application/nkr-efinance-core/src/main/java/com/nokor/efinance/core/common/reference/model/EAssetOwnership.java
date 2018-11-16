package com.nokor.efinance.core.common.reference.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * EAsset Owner Ship
 * 
 * @author buntha.chea
 *
 */
public class EAssetOwnership extends BaseERefData implements AttributeConverter<EAssetOwnership, Long> {
	
	/** */
	private static final long serialVersionUID = 6545982586113772877L;
	
	public final static EAssetOwnership OWNER = new EAssetOwnership("OWNER", 1);
	public final static EAssetOwnership RENTER = new EAssetOwnership("RENTER", 2);
	public final static EAssetOwnership SUB_RENTER = new EAssetOwnership("SUB_RENTER", 3);

	/***/
	public EAssetOwnership() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EAssetOwnership(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EAssetOwnership convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EAssetOwnership arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EAssetOwnership> values() {
		return getValues(EAssetOwnership.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EAssetOwnership getByCode(String code) {
		return getByCode(EAssetOwnership.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EAssetOwnership getById(long id) {
		return getById(EAssetOwnership.class, id);
	}

}

package com.nokor.efinance.core.aftersale;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * After sale event type
 * 
 * @author ly.youhort
 *
 */

public class EAfterSaleEventType extends BaseERefData implements AttributeConverter<EAfterSaleEventType, Long> {

	/**
	 */
	private static final long serialVersionUID = 4479071815042229338L;
	
	public final static EAfterSaleEventType EARLYSETTLEMENT = new EAfterSaleEventType("EAR", 1); 
	public final static EAfterSaleEventType REPOSSESSION = new EAfterSaleEventType("REPO", 2); 
	public final static EAfterSaleEventType TRANSFER_APPLICANT = new EAfterSaleEventType("TRAN", 3); 

	/**
	 */
	public EAfterSaleEventType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EAfterSaleEventType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EAfterSaleEventType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EAfterSaleEventType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EAfterSaleEventType> values() {
		return getValues(EAfterSaleEventType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EAfterSaleEventType getByCode(String code) {
		return getByCode(EAfterSaleEventType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EAfterSaleEventType getById(long id) {
		return getById(EAfterSaleEventType.class, id);
	}
}
package com.nokor.efinance.third.integration.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;


/**
 * ThirdParty
 * 
 * @author ly.youhort
 */
public class EThirdParty extends BaseERefData implements AttributeConverter<EThirdParty, Long> {
	/** */
	private static final long serialVersionUID = 3856976736316912194L;

	public final static EThirdParty WING = new EThirdParty("WING", 1);
	public final static EThirdParty PAYGO = new EThirdParty("PAYGO", 2);

	
	/**
	 * 
	 */
	public EThirdParty() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EThirdParty(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EThirdParty convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EThirdParty arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EThirdParty> values() {
		return getValues(EThirdParty.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EThirdParty getByCode(String code) {
		return getByCode(EThirdParty.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EThirdParty getById(long id) {
		return getById(EThirdParty.class, id);
	}

}
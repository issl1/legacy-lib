package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Media of promoting
 * 
 * @author ly.youhort
 */
public class EMediaPromoting extends BaseERefData implements AttributeConverter<EMediaPromoting, Long> {
	/** */
	private static final long serialVersionUID = -1353795686846272112L;

	public final static EMediaPromoting TV = new EMediaPromoting("TV", 1);
	public final static EMediaPromoting RADIO = new EMediaPromoting("RADIO", 2);
	public final static EMediaPromoting NEWSPAPER = new EMediaPromoting("NEWSPAPER", 3);
	public final static EMediaPromoting WEBSITE = new EMediaPromoting("WEBSITE", 4);
	public final static EMediaPromoting FACEBOOK = new EMediaPromoting("FACEBOOK", 5);
	public final static EMediaPromoting LEAFLET = new EMediaPromoting("LEAFLET", 6);
	public final static EMediaPromoting PEOPLE = new EMediaPromoting("PEOPLE", 7);

	/**
	 * 
	 */
	public EMediaPromoting() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EMediaPromoting(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EMediaPromoting convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EMediaPromoting arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EMediaPromoting> values() {
		return getValues(EMediaPromoting.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EMediaPromoting getByCode(String code) {
		return getByCode(EMediaPromoting.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EMediaPromoting getById(long id) {
		return getById(EMediaPromoting.class, id);
	}
}

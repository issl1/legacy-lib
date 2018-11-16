package com.nokor.frmk.config.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;


/**
 * Table LocaleType
 * @author prasnar
 */
public class ELocale extends BaseERefData implements AttributeConverter<ELocale, Long> {
	/** */
	private static final long serialVersionUID = -7814018732630715000L;

	public final static ELocale ENG = new ELocale("en_US", 1); 
	public final static ELocale FRA = new ELocale("fr_FR", 2); 
	public final static ELocale KHM = new ELocale("kh_KH", 3); 

	/**
	 * 
	 */
	public ELocale() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ELocale(String code, long id) {
		super(code, id);
	}

	@Override
	public ELocale convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ELocale arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	public String getLanguageTag() {
		String[] split = code.split("_");
		
		if (split.length > 0) {
			return split[0];
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<ELocale> values() {
		return getValues(ELocale.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ELocale getById(long id) {
		return getById(ELocale.class, id);
	}

}
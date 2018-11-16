package com.nokor.common.app.eref;

import java.util.List;
import java.util.Locale;

import javax.persistence.AttributeConverter;

import org.apache.commons.lang3.LocaleUtils;
import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ECountry extends BaseERefData implements AttributeConverter<ECountry, Long> {
	
	/** */
	private static final long serialVersionUID = -4999227861148273674L;
	
	public final static ECountry FRA = new ECountry("FRA", "fr", 1);
	public final static ECountry USA = new ECountry("USA", "en", 2);
	public final static ECountry KHM = new ECountry("KHM", "kh", 100);
	public final static ECountry THA = new ECountry("THA", "th", 101);
	public final static ECountry VNA = new ECountry("VNA", "vn", 102);
	public final static ECountry LAO = new ECountry("LAO", "la", 103);
	
	protected Locale locale;

	
	/**
	 * 
	 */
	public ECountry() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECountry(String code, String lang, long id) {
		super(code, id);
		this.locale = LocaleUtils.toLocale(lang);
	}
	
	/**
	 * 
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}

	@Override
	public ECountry convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECountry arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ECountry getByCode(String code) {
		return getByCode(ECountry.class, code);
	}

	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ECountry getById(long id) {
		return getById(ECountry.class, id);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ECountry> values() {
		return getValues(ECountry.class);
	}
}
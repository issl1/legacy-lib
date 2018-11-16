package com.nokor.common.app.eref;

import java.util.List;
import java.util.Locale;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ELanguage extends BaseERefData implements AttributeConverter<ELanguage, Long> {
	
	/** */
	private static final long serialVersionUID = -3747555579572811496L;
	
	public final static ELanguage FRENCH = new ELanguage (ECountry.FRA);
	public final static ELanguage KHMER = new ELanguage (ECountry.KHM);
	public final static ELanguage ENGLISH = new ELanguage (ECountry.USA);
	public final static ELanguage THAI = new ELanguage (ECountry.THA);
	
	public final static String ALL = "#ALL#";

	protected Locale locale;

	/**
	 * 
	 */
	public ELanguage() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ELanguage(ECountry ref) {
		super(ref.getCode(), ref.getId());
		this.locale = ref.getLocale();
	}

	@Override
	public ELanguage convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ELanguage arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLocaleLanguage() {
		return getLocale().toLanguageTag();
	}
	
	/**
	 * 
	 * @param locale
	 * @return
	 */
	public static ELanguage getFromLocale(Locale locale) {
		return getFromString(locale.getLanguage());
	}
	
	/**
	 * 
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}
	
	/**
	 * 
	 * @param lang
	 * @return
	 */
	public static ELanguage getFromString(String lang) {
		return getFromString(lang, null);
	}
	
	/**
	 * 
	 * @param lang
	 * @return
	 */
	public static ELanguage getFromString(String lang, ELanguage defaultLanguage) {
		if (KHMER.getCode().equalsIgnoreCase(lang)) {
			return KHMER;
		} else if (FRENCH.getCode().equalsIgnoreCase(lang)) {
			return FRENCH;
		} else if (ENGLISH.getCode().equalsIgnoreCase(lang)) {
			return ENGLISH;
		} else if (THAI.getCode().equalsIgnoreCase(lang)) {
			return THAI;
		} else {
			return defaultLanguage != null ? defaultLanguage : ENGLISH;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<ELanguage> values() {
		return getValues(ELanguage.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ELanguage getById(long id) {
		return getById(ELanguage.class, id);
	}
}

package com.nokor.ersys.core.hr.model.eref;

import java.util.List;
import java.util.Locale;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

import com.nokor.common.app.eref.ECountry;

/**
 * 
 * @author prasnar
 *
 */
public class ENationality extends BaseERefData implements AttributeConverter<ENationality, Long> {
	
	/** */
	private static final long serialVersionUID = 3649193180252291864L;
	
	public final static ENationality FRENCH = new ENationality (ECountry.FRA);
	public final static ENationality KHMER = new ENationality (ECountry.KHM);
	public final static ENationality ENGLISH = new ENationality (ECountry.USA);
	public final static ENationality THAI = new ENationality (ECountry.THA);
	
	public final static String ALL = "#ALL#";

	protected Locale locale;

	/**
	 * 
	 */
	public ENationality() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ENationality(ECountry ref) {
		super(ref.getCode(), ref.getId());
		this.locale = ref.getLocale();
	}

	@Override
	public ENationality convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ENationality arg0) {
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
	public static ENationality getFromLocale(Locale locale) {
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
	public static ENationality getFromString(String lang) {
		return getFromString(lang, null);
	}
	
	/**
	 * 
	 * @param lang
	 * @return
	 */
	public static ENationality getFromString(String lang, ENationality defaultLanguage) {
		if (KHMER.getCode().equalsIgnoreCase(lang)) {
			return KHMER;
		} else if (FRENCH.getCode().equalsIgnoreCase(lang)) {
			return FRENCH;
		} else if (ENGLISH.getCode().equalsIgnoreCase(lang)) {
			return ENGLISH;
		} else {
			return defaultLanguage != null ? defaultLanguage : ENGLISH;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<ENationality> values() {
		return getValues(ENationality.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ENationality getByCode(String code) {
		return getByCode(ENationality.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ENationality getById(long id) {
		return getById(ENationality.class, id);
	}
}

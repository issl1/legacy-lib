package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author ly.youhort
 */
public class EOptionality extends BaseERefData implements AttributeConverter<EOptionality, Long> {
	/** */
	private static final long serialVersionUID = -8443812214835054497L;

	public final static EOptionality MANDATORY = new EOptionality("MANDATORY", 1);
	public final static EOptionality OPTIONAL = new EOptionality("OPTIONAL", 2);

	/**
	 */
	public EOptionality() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EOptionality(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EOptionality convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EOptionality arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EOptionality> values() {
		return getValues(EOptionality.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EOptionality getByCode(String code) {
		return getByCode(EOptionality.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EOptionality getById(long id) {
		return getById(EOptionality.class, id);
	}

}
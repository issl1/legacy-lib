package com.nokor.common.app.tools;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.SimpleERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ESessionType extends SimpleERefData implements AttributeConverter<ESessionType, Long> {
	/** */
	private static final long serialVersionUID = -735861196509947333L;

	public final static ESessionType HTTP = new ESessionType("HTTP", 1);
	public final static ESessionType VAADIN = new ESessionType("VAADIN", 2);
	public final static ESessionType TEST = new ESessionType("TEST", 3);
	
//    public static final List<ESessionType> VALUES = BaseERefData.getStaticValues(ESessionType.class);


	/**
	 * 
	 */
	public ESessionType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ESessionType(String code, long id) {
		super(code, id);
	}
	
	@Override
	public ESessionType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ESessionType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ESessionType getById(long id) {
		return SimpleERefData.getById(ESessionType.class, id);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ESessionType getByCode(String code) {
		return SimpleERefData.getByCode(ESessionType.class, code);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ESessionType valueOf(String code) {
		return SimpleERefData.valueOf(ESessionType.class, code);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ESessionType> values() {
		return SimpleERefData.getValues(ESessionType.class);
	}
	
	
}

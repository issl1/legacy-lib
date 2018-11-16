package com.nokor.efinance.core.financial.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Plan type for financial product
 * 
 * @author ly.youhort
 */
public class EPlanType extends BaseERefData implements AttributeConverter<EPlanType, Long> {
	/** */
	private static final long serialVersionUID = -7626913478709850631L;

	public final static EPlanType STAND = new EPlanType("STAND", 1); // standard
	public final static EPlanType DEROG = new EPlanType("DEROG", 2); // derogation

	/**
	 * 
	 */
	public EPlanType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EPlanType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EPlanType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPlanType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EPlanType> values() {
		return getValues(EPlanType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EPlanType getByCode(String code) {
		return getByCode(EPlanType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EPlanType getById(long id) {
		return getById(EPlanType.class, id);
	}
}

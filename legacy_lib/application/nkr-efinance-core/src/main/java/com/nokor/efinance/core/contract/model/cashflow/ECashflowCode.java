package com.nokor.efinance.core.contract.model.cashflow;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author ly.youhort
 *
 */
public class ECashflowCode extends BaseERefData implements AttributeConverter<ECashflowCode, Long> {
	/** */
	private static final long serialVersionUID = -4535030361738047269L;
	
	public final static ECashflowCode EAR = new ECashflowCode("EAR", 1); // early.settlement
	public final static ECashflowCode LOS = new ECashflowCode("LOS", 2); // loss

	/**
	 * 
	 */
	public ECashflowCode() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECashflowCode(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ECashflowCode convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECashflowCode arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ECashflowCode> values() {
		return getValues(ECashflowCode.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ECashflowCode getByCode(String code) {
		return getByCode(ECashflowCode.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ECashflowCode getById(long id) {
		return getById(ECashflowCode.class, id);
	}
}

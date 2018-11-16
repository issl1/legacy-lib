package com.nokor.efinance.core.contract.model.cashflow;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author ly.youhort
 *
 */
public class ECashflowLinkType extends BaseERefData implements AttributeConverter<ECashflowLinkType, Long> {
	/** */
	private static final long serialVersionUID = -4535030361738047269L;
	
	public final static ECashflowLinkType PARTIAL = new ECashflowLinkType("PARTIAL", 1);
	

	/**
	 * 
	 */
	public ECashflowLinkType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECashflowLinkType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ECashflowLinkType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECashflowLinkType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ECashflowLinkType> values() {
		return getValues(ECashflowLinkType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ECashflowLinkType getByCode(String code) {
		return getByCode(ECashflowLinkType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ECashflowLinkType getById(long id) {
		return getById(ECashflowLinkType.class, id);
	}
}

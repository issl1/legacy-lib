package com.nokor.efinance.core.contract.model.cashflow;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Treasury type
 * 
 * @author ly.youhort
 *
 */
public class ETreasuryType extends BaseERefData implements AttributeConverter<ETreasuryType, Long> {
	/** */
	private static final long serialVersionUID = 8773689763758918561L;

	public final static ETreasuryType FCO = new ETreasuryType("FCO", 1);
	public final static ETreasuryType DEA = new ETreasuryType("DEA", 2);
	public final static ETreasuryType APP = new ETreasuryType("APP", 3);
	public final static ETreasuryType MAN = new ETreasuryType("MAN", 4);
	public final static ETreasuryType PRO = new ETreasuryType("PRO", 5);
	
	/**
	 * 
	 */
	public ETreasuryType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETreasuryType(String code, long id) {
		super(code, id);
	}

	@Override
	public ETreasuryType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ETreasuryType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETreasuryType> values() {
		return getValues(ETreasuryType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ETreasuryType getByCode(String code) {
		return getByCode(ETreasuryType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETreasuryType getById(long id) {
		return getById(ETreasuryType.class, id);
	}
}

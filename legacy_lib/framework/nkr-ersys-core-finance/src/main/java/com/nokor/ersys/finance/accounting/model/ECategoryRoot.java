package com.nokor.ersys.finance.accounting.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ECategoryRoot extends BaseERefData implements AttributeConverter<ECategoryRoot, Long> {
	/** */
	private static final long serialVersionUID = -6374963045155461091L;

	public final static ECategoryRoot ASSETS = new ECategoryRoot("ASSET", 1);
	public final static ECategoryRoot LIABILITIES = new ECategoryRoot("LIABILITIES", 2);
	public final static ECategoryRoot INCOMES = new ECategoryRoot("INCOMES", 3);
	public final static ECategoryRoot EXPENSES = new ECategoryRoot("EXPENSES", 4);
	public final static ECategoryRoot EQUITIES = new ECategoryRoot("EQUITIES", 5);
	public final static ECategoryRoot OTHERS = new ECategoryRoot("OTHERS", 6);

	/**
	 */
	public ECategoryRoot() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ECategoryRoot(String code, long id) {
		super(code, id);
	}

	@Override
	public ECategoryRoot convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECategoryRoot arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<ECategoryRoot> values() {
		return getValues(ECategoryRoot.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ECategoryRoot getByCode(String code) {
		return getByCode(ECategoryRoot.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ECategoryRoot getById(long id) {
		return getById(ECategoryRoot.class, id);
	}
}

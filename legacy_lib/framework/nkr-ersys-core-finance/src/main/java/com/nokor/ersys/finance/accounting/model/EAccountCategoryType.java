package com.nokor.ersys.finance.accounting.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EAccountCategoryType extends BaseERefData implements AttributeConverter<EAccountCategoryType, Long> {
	/** */
	private static final long serialVersionUID = 1687029343431257263L;

	public final static EAccountCategoryType RECEIVABLE = new EAccountCategoryType("RECEIVABLE", 1);
	public final static EAccountCategoryType PAYABLE = new EAccountCategoryType("PAYABLE", 2);
	public final static EAccountCategoryType LIQUIDITY = new EAccountCategoryType("LIQUIDITY", 3);
	public final static EAccountCategoryType OTHER = new EAccountCategoryType("OTHER", 4);

	/**
	 */
	public EAccountCategoryType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EAccountCategoryType(String code, long id) {
		super(code, id);
	}

	@Override
	public EAccountCategoryType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EAccountCategoryType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EAccountCategoryType> values() {
		return getValues(EAccountCategoryType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EAccountCategoryType getByCode(String code) {
		return getByCode(EAccountCategoryType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EAccountCategoryType getById(long id) {
		return getById(EAccountCategoryType.class, id);
	}
}

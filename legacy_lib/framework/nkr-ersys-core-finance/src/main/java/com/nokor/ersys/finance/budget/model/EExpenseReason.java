package com.nokor.ersys.finance.budget.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EExpenseReason extends BaseERefData implements AttributeConverter<EExpenseReason, Long> {
	/** */
	private static final long serialVersionUID = -1215567499959892976L;

	public final static EExpenseReason DEFAULT = new EExpenseReason("DEFAULT", 1);


	/**
	 */
	public EExpenseReason() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EExpenseReason(String code, long id) {
		super(code, id);
	}

	@Override
	public EExpenseReason convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EExpenseReason arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EExpenseReason> values() {
		return getValues(EExpenseReason.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EExpenseReason getByCode(String code) {
		return getByCode(EExpenseReason.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EExpenseReason getById(long id) {
		return getById(EExpenseReason.class, id);
	}
}

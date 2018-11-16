package com.nokor.ersys.finance.billing.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EDirection extends BaseERefData implements AttributeConverter<EDirection, Long> {
	/** */
	private static final long serialVersionUID = -2982756237880851696L;

	public final static EDirection RECEIVABLE = new EDirection("RECEIVABLE", 1);
	public final static EDirection PAYABLE = new EDirection("PAYABLE", 2);


	/**
	 */
	public EDirection() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EDirection(String code, long id) {
		super(code, id);
	}

	@Override
	public EDirection convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EDirection arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EDirection> values() {
		return getValues(EDirection.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EDirection getByCode(String code) {
		return getByCode(EDirection.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EDirection getById(long id) {
		return getById(EDirection.class, id);
	}
}

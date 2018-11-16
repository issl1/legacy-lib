package com.nokor.efinance.core.stock.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Stock Movement
 * @author ly.youhort
 *
 */
public class EStockMovement extends BaseERefData implements AttributeConverter<EStockMovement, Long> {
	/** */
	private static final long serialVersionUID = -350903772788327751L;

	public final static EStockMovement IN = new EStockMovement("IN", 1);
	public final static EStockMovement OUT = new EStockMovement("OUT", 2);

	
	/**
	 * 
	 */
	public EStockMovement() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EStockMovement(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EStockMovement convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EStockMovement arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EStockMovement> values() {
		return getValues(EStockMovement.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EStockMovement getByCode(String code) {
		return getByCode(EStockMovement.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EStockMovement getById(long id) {
		return getById(EStockMovement.class, id);
	}

}
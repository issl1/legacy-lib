package com.nokor.efinance.core.contract.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Request type
 * 
 * @author youhort.ly
 */
public class ERequestType extends BaseERefData implements AttributeConverter<ERequestType, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4203559442903370358L;

	public final static ERequestType TAXSMS = new ERequestType("TAXSMS", 1);
	public final static ERequestType CONSMS = new ERequestType("CONSMS", 2);
	public final static ERequestType PAYSLI = new ERequestType("PAYSLI", 3);
	public final static ERequestType BALSMS = new ERequestType("BALSMS", 4);
	public final static ERequestType CUSCAD = new ERequestType("CUSCAD", 5);
	public final static ERequestType CONMAI = new ERequestType("CONMAI", 6);
	public final static ERequestType CBRMAI = new ERequestType("CBRMAI", 7);
	public final static ERequestType OTH = new ERequestType("OTH", 8);
	
	/**
	 */
	public ERequestType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ERequestType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ERequestType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ERequestType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ERequestType> values() {		
		return getValues(ERequestType.class);		
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ERequestType getByCode(String code) {
		return getByCode(ERequestType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ERequestType getById(long id) {
		return getById(ERequestType.class, id);
	}
}

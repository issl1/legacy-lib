package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ECategoryAddress extends BaseERefData implements AttributeConverter<ECategoryAddress, Long> {
	
	/** */
	private static final long serialVersionUID = -8651670494354364062L;
	
	public final static ECategoryAddress MAIN = new ECategoryAddress("MAIN", 1);
	

	
	/**
	 * 
	 */
	public ECategoryAddress() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECategoryAddress(String code, long id) {
		super(code, id);
	}

	@Override
	public ECategoryAddress convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECategoryAddress arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ECategoryAddress> values() {
		return getValues(ECategoryAddress.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ECategoryAddress getById(long id) {
		return getById(ECategoryAddress.class, id);
	}
}
package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETypeIdNumber extends BaseERefData implements AttributeConverter<ETypeIdNumber, Long> {
	
	private static final long serialVersionUID = 6624646012322288585L;
		
	/**
	 */
	public ETypeIdNumber() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETypeIdNumber(String code, long id) {
		super(code, id);
	}

	@Override
	public ETypeIdNumber convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ETypeIdNumber arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	/**
	 * @return
	 */
	public static List<ETypeIdNumber> values() {
		return getValues(ETypeIdNumber.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ETypeIdNumber getByCode(String code) {
		return getByCode(ETypeIdNumber.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ETypeIdNumber getById(long id) {
		return getById(ETypeIdNumber.class, id);
	}
}
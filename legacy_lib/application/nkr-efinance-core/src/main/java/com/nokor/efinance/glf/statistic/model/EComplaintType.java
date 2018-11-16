package com.nokor.efinance.glf.statistic.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Complaint Type
 * 
 * @author ly.youhort
 */
public class EComplaintType extends BaseERefData implements AttributeConverter<EComplaintType, Long> {
	/** */
	private static final long serialVersionUID = -5432195035486304029L;

	
	/**
	 * 
	 */
	public EComplaintType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EComplaintType(String code, long id) {
		super(code, id);
	}

	@Override
	public EComplaintType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EComplaintType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EComplaintType> values() {
		return getValues(EComplaintType.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EComplaintType getByCode(String code) {
		return getByCode(EComplaintType.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EComplaintType getById(long id) {
		return getById(EComplaintType.class, id);
	}
}

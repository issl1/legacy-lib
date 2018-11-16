package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author uhout.cheng
 */
public class EPromiseStatus extends BaseERefData implements AttributeConverter<EPromiseStatus, Long> {
	
	/** */
	private static final long serialVersionUID = -1986130691327581013L;
	
	public final static EPromiseStatus PENDING = new EPromiseStatus("PENDING", 1l);
	public final static EPromiseStatus BROKEN = new EPromiseStatus("BROKEN", 2l);
	public final static EPromiseStatus KEPT = new EPromiseStatus("KEPT", 3l);

	/**
	 */
	public EPromiseStatus() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EPromiseStatus(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	public EPromiseStatus convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EPromiseStatus arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EPromiseStatus> values() {
		return getValues(EPromiseStatus.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EPromiseStatus getByCode(String code) {
		return getByCode(EPromiseStatus.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EPromiseStatus getById(long id) {
		return getById(EPromiseStatus.class, id);
	}
	
}

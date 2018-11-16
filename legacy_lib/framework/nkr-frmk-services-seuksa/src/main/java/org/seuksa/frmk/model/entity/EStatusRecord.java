package org.seuksa.frmk.model.entity;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.SimpleERefData;

/**
 * List of StatusRecord
 * 
 * @author prasnar
 *
 */
public class EStatusRecord extends SimpleERefData implements AttributeConverter<EStatusRecord, Long> {

	/** */
	private static final long serialVersionUID = -2708745992451417068L;
	
	public final static EStatusRecord ACTIV = new EStatusRecord("ACTIV", 1);
	public final static EStatusRecord INACT = new EStatusRecord("INACT", 2);
	public final static EStatusRecord RECYC = new EStatusRecord("RECYC", 3);
	public final static EStatusRecord ARCHI = new EStatusRecord("ARCHI", 4);
	
//    public static final List<EStatusRecord> VALUES = BaseERefData.getStaticValues(EStatusRecord.class);


	/**
	 * 
	 */
	public EStatusRecord() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EStatusRecord(String code, long id) {
		super(code, id);
	}
	
	@Override
	public EStatusRecord convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EStatusRecord arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EStatusRecord getById(long id) {
		return SimpleERefData.getById(EStatusRecord.class, id);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EStatusRecord getByCode(String code) {
		return SimpleERefData.getByCode(EStatusRecord.class, code);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EStatusRecord valueOf(String code) {
		return SimpleERefData.valueOf(EStatusRecord.class, code);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EStatusRecord> values() {
		return SimpleERefData.getValues(EStatusRecord.class);
	}
	
	/**
	 * 
	 * @param status
	 * @return
	 */
	public static  boolean isActive(EStatusRecord status) {
		return EStatusRecord.ACTIV.equals(status) || status == null;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return super.toString();
	}

}
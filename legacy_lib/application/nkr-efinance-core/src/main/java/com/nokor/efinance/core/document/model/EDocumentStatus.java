package com.nokor.efinance.core.document.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Document state
 * @author ly.youhort
 *
 */

public class EDocumentStatus extends BaseERefData implements AttributeConverter<EDocumentStatus, Long> {
	/** */
	private static final long serialVersionUID = -1759466004694761322L;

	public final static EDocumentStatus COMPLET = new EDocumentStatus("COMPLET", 1);
	public final static EDocumentStatus INCOMPLET = new EDocumentStatus("INCOMPLET", 2);
	public final static EDocumentStatus MISSING = new EDocumentStatus("MISSING", 3);
	public final static EDocumentStatus WRONG = new EDocumentStatus("WRONG", 4);
	public final static EDocumentStatus NOTCLEAR = new EDocumentStatus("NOTCLEAR", 5);

	/**
	 * 
	 */
	public EDocumentStatus() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EDocumentStatus(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EDocumentStatus convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EDocumentStatus arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EDocumentStatus> values() {
		return getValues(EDocumentStatus.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EDocumentStatus getByCode(String code) {
		return getByCode(EDocumentStatus.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EDocumentStatus getById(long id) {
		return getById(EDocumentStatus.class, id);
	}
}

package com.nokor.efinance.core.document.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Document state
 * @author ly.youhort
 *
 */

public class EDocumentState extends BaseERefData implements AttributeConverter<EDocumentState, Long> {
	/** */
	private static final long serialVersionUID = -1759466004694761322L;

	public final static EDocumentState QUO = new EDocumentState("QUO", 1);
	public final static EDocumentState ACT = new EDocumentState("ACT", 2);
	public final static EDocumentState OTH = new EDocumentState("OTH", 3);

	/**
	 * 
	 */
	public EDocumentState() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EDocumentState(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EDocumentState convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EDocumentState arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EDocumentState> values() {
		return getValues(EDocumentState.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EDocumentState getByCode(String code) {
		return getByCode(EDocumentState.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EDocumentState getById(long id) {
		return getById(EDocumentState.class, id);
	}
}

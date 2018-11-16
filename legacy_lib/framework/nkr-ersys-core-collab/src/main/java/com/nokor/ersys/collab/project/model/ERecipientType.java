package com.nokor.ersys.collab.project.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ERecipientType extends BaseERefData implements AttributeConverter<ERecipientType, Long> {
	/** */
	private static final long serialVersionUID = -1784157515935713278L;

	public final static ERecipientType CC = new ERecipientType("CC", 1);
	public final static ERecipientType BCC = new ERecipientType("BCC", 2);

	/**
	 * 
	 */
	public ERecipientType() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ERecipientType(String code, long id) {
		super(code, id);
	}

	@Override
	public ERecipientType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ERecipientType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ERecipientType> values() {
		return getValues(ERecipientType.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ERecipientType getById(long id) {
		return getById(ERecipientType.class, id);
	}
}
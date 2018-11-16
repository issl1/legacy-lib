package com.nokor.common.app.content.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * List of File Type 
 * 
 * @author prasnar
 *
 */
public class EFileType extends BaseERefData implements AttributeConverter<EFileType, Long> {
	/** */
	private static final long serialVersionUID = -7239674924055861442L;

	public final static EFileType PDF = new EFileType("PDF", 1);
	public final static EFileType WORD = new EFileType("WORD", 2);
	public final static EFileType EXCEL = new EFileType("EXCEL", 3);
	public final static EFileType IMG = new EFileType("IMG", 4);
	public final static EFileType VIDEO = new EFileType("VIDEO", 5);
	public final static EFileType FLASH = new EFileType("FLASH", 6);
	public final static EFileType EXE = new EFileType("EXE", 7);

	private EFileCategory category;
	
	/**
	 * 
	 */
	public EFileType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EFileType(String code, long id) {
		super(code, id);
	}

	@Override
	public EFileType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EFileType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EFileType> values() {
		return getValues(EFileType.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EFileType getById(long id) {
		return getById(EFileType.class, id);
	}

	/**
	 * @return the category
	 */
	public EFileCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(EFileCategory category) {
		this.category = category;
	}

	
	
	
}
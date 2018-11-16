package com.nokor.common.app.content.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * List of File Category 
 * 
 * @author prasnar
 *
 */
public class EFileCategory extends BaseERefData implements AttributeConverter<EFileCategory, Long> {
	/** */
	private static final long serialVersionUID = -4463950541715895866L;
	
	public final static EFileCategory FILE = new EFileCategory("FILE", 1);
	public final static EFileCategory URL = new EFileCategory("URL", 2);
	public final static EFileCategory YOUTUBE = new EFileCategory("YOUTUBE", 3);
	public final static EFileCategory FTP = new EFileCategory("FTP", 4);
	public final static EFileCategory CLOUD = new EFileCategory("CLOUD", 5);

	
	/**
	 * 
	 */
	public EFileCategory() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EFileCategory(String code, long id) {
		super(code, id);
	}

	@Override
	public EFileCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EFileCategory arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EFileCategory> values() {
		return getValues(EFileCategory.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EFileCategory getById(long id) {
		return getById(EFileCategory.class, id);
	}
}
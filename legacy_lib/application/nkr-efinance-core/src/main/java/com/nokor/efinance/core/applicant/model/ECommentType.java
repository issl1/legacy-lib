package com.nokor.efinance.core.applicant.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;
/**
 * 
 * @author buntha.chea
 *
 */
public class ECommentType extends BaseERefData implements AttributeConverter<ECommentType, Long>{
	/** */
	private static final long serialVersionUID = 7660199357618823055L;
	
	public static final ECommentType COMMENT = new ECommentType("COMMENT", 1l);
	
	/**
	 * 
	 */
	public ECommentType() {
	}
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECommentType(String code, long id) {
		super(code, id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECommentType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ECommentType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	/**
	 * 
	 * @return
	 */
	public static List<ECommentType> values() {
		return getValues(ECommentType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ECommentType getByCode(String code) {
		return getByCode(ECommentType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ECommentType getById(long id) {
		return getById(ECommentType.class, id);
	}

}

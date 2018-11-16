package com.nokor.efinance.core.dealer.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Dealer Category
 * @author bunlong.taing
 */
public class EDealerCategory extends BaseERefData implements AttributeConverter<EDealerCategory, Long>{
	/** */
	private static final long serialVersionUID = -7078067147121507332L;
	
	public final static EDealerCategory AUTHORIZED = new EDealerCategory("AUTHORIZED", 1l);
	public final static EDealerCategory BROKER = new EDealerCategory("BROKER", 2l);
	
	/** */
	public EDealerCategory() {
	}
	
	/**
	 * EDealerCategory
	 * @param code
	 * @param id
	 */
	public EDealerCategory(String code, Long id) {
		super(code, id);
	}

	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EDealerCategory dealerCategory) {
		return super.convertToDatabaseColumn(dealerCategory);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EDealerCategory convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<EDealerCategory> values() {
		return getValues(EDealerCategory.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EDealerCategory getByCode(String code) {
		return getByCode(EDealerCategory.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EDealerCategory getById(long id) {
		return getById(EDealerCategory.class, id);
	}

}

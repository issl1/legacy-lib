package com.nokor.efinance.core.quotation.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author ly.youhort
 *
 */
public class EExtModule extends BaseERefData implements AttributeConverter<EExtModule, Long> {
	/** */
	private static final long serialVersionUID = 5860451397552590976L;

	public final static EExtModule CREDIT_BURO = new EExtModule("CREDIT_BURO", 1);

	/**
	 * 
	 */
	public EExtModule() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EExtModule(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EExtModule convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EExtModule arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EExtModule> values() {
		return getValues(EExtModule.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EExtModule getByCode(String code) {
		return getByCode(EExtModule.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EExtModule getById(long id) {
		return getById(EExtModule.class, id);
	}
}

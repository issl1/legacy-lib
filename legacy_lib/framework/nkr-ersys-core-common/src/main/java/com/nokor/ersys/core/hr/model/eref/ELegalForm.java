package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Employment status
 * 
 * @author ly.youhort
 */
public class ELegalForm extends BaseERefData implements AttributeConverter<ELegalForm, Long> {
	/** */
	private static final long serialVersionUID = -7975283856613461134L;

	/**
	 */
	public ELegalForm() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ELegalForm(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ELegalForm convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ELegalForm arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ELegalForm> values() {
		return getValues(ELegalForm.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ELegalForm getByCode(String code) {
		return getByCode(ELegalForm.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ELegalForm getById(long id) {
		return getById(ELegalForm.class, id);
	}
}

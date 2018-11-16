package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ECivility extends BaseERefData implements AttributeConverter<ECivility, Long> {
	
	/** */
	private static final long serialVersionUID = -9119337035539433625L;
	
	public final static ECivility MR = new ECivility("MR", 1); 
	public final static ECivility MRS = new ECivility("MRS", 2); 
	public final static ECivility MS = new ECivility("MS", 3); 

	/**
	 * 
	 */
	public ECivility() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECivility(String code, long id) {
		super(code, id);
	}

	@Override
	public ECivility convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECivility arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<ECivility> values() {
		return getValues(ECivility.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ECivility getByCode(String code) {
		return getByCode(ECivility.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ECivility getById(long id) {
		return getById(ECivility.class, id);
	}
}
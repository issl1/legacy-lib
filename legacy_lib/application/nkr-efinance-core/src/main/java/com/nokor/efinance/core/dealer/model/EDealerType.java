package com.nokor.efinance.core.dealer.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Dealer type
 * 
 * @author youhort.ly
 */
public class EDealerType extends BaseERefData implements AttributeConverter<EDealerType, Long> {
	/** */
	private static final long serialVersionUID = -5713129714458211130L;

	public final static EDealerType HEAD = new EDealerType("HEAD", 1);
	public final static EDealerType BRANCH = new EDealerType("BRANCH", 2);
	

	/**
	 */
	public EDealerType() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EDealerType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EDealerType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EDealerType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EDealerType> values() {
		return getValues(EDealerType.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EDealerType getByCode(String code) {
		return getByCode(EDealerType.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EDealerType getById(long id) {
		return getById(EDealerType.class, id);
	}
}

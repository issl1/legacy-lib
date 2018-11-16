package com.nokor.efinance.core.dealer.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Dealer Customer
 * @author poevminea.sann
 *
 */
public class EDealerCustomer extends BaseERefData implements AttributeConverter<EDealerCustomer, Long>{
	/** */
	private static final long serialVersionUID = -7078067147121507332L;
	
	public final static EDealerCustomer IND = new EDealerCustomer("IND", 1l);
	public final static EDealerCustomer COM = new EDealerCustomer("COM", 2l);
	
	/** */
	public EDealerCustomer() {
	}
	
	/**
	 * EDealerCategory
	 * @param code
	 * @param id
	 */
	public EDealerCustomer(String code, Long id) {
		super(code, id);
	}

	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EDealerCustomer dealerCategory) {
		return super.convertToDatabaseColumn(dealerCategory);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EDealerCustomer convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<EDealerCustomer> values() {
		return getValues(EDealerCustomer.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EDealerCustomer getByCode(String code) {
		return getByCode(EDealerCustomer.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EDealerCustomer getById(long id) {
		return getById(EDealerCustomer.class, id);
	}

}

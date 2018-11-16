package com.nokor.ersys.finance.billing.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EPaymentMethod extends BaseERefData implements AttributeConverter<EPaymentMethod, Long> {
	/** */
	private static final long serialVersionUID = 22523621564292758L;

	public final static EPaymentMethod CHEQUE = new EPaymentMethod("CHEQUE", 1);
	public final static EPaymentMethod BANK = new EPaymentMethod("BANK", 2);
	public final static EPaymentMethod CASH = new EPaymentMethod("CASH", 3);
	public final static EPaymentMethod CREDIT_NOTE = new EPaymentMethod("CREDIT_NOTE", 4);

	private boolean automatic;
	
	/**
	 */
	public EPaymentMethod() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EPaymentMethod(String code, long id) {
		super(code, id);
	}

	@Override
	public EPaymentMethod convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPaymentMethod arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EPaymentMethod> values() {
		return getValues(EPaymentMethod.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EPaymentMethod getByCode(String code) {
		return getByCode(EPaymentMethod.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EPaymentMethod getById(long id) {
		return getById(EPaymentMethod.class, id);
	}

	/**
	 * @return the automatic
	 */
	public boolean isAutomatic() {
		return automatic;
	}

	/**
	 * @param automatic the automatic to set
	 */
	public void setAutomatic(boolean automatic) {
		this.automatic = automatic;
	}


}

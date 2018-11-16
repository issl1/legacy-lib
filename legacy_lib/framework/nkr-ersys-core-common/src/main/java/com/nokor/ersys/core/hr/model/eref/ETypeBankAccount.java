package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETypeBankAccount extends BaseERefData implements AttributeConverter<ETypeBankAccount, Long> {
	/** */
	private static final long serialVersionUID = -6514019879951810371L;
	
	public final static ETypeBankAccount MAIN = new ETypeBankAccount("MAIN", 1);
	public final static ETypeBankAccount OTHER = new ETypeBankAccount("OTHER", 2);

	/**
	 * 
	 */
	public ETypeBankAccount() {
		
	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETypeBankAccount(String code, long id) {
		super(code, id);
	}

	@Override
	public ETypeBankAccount convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ETypeBankAccount arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETypeBankAccount> values() {
		return getValues(ETypeBankAccount.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETypeBankAccount getById(long id) {
		return getById(ETypeBankAccount.class, id);
	}
}
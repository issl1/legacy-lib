package com.nokor.efinance.core.quotation.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;


/**
 * Place of Installment
 * 
 * @author ly.youhort
 */
public class EPlaceInstallment extends BaseERefData implements AttributeConverter<EPlaceInstallment, Long> {
	/** */
	private static final long serialVersionUID = 2208139401020752726L;

	/**
	 * 
	 */
	public EPlaceInstallment() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EPlaceInstallment(String code, long id) {
		super(code, id);
	}
	
	/**
	 * @return the defaultSelected
	 */
	public Boolean isDefaultSelected() {
		// TODO PYI
		return true; // from Setting
	}

	/**
	 * @param defaultSelected the defaultSelected to set
	 */
	public void setDefaultSelected(Boolean defaultSelected) {
		// to Setting
	}

	@SuppressWarnings("unchecked")
	@Override
	public EPlaceInstallment convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPlaceInstallment arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EPlaceInstallment> values() {
		return getValues(EPlaceInstallment.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EPlaceInstallment getByCode(String code) {
		return getByCode(EPlaceInstallment.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EPlaceInstallment getById(long id) {
		return getById(EPlaceInstallment.class, id);
	}
}

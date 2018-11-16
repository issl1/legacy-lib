package com.nokor.ersys.core.hr.model.eref;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETypeAddress extends BaseERefData implements Serializable, AttributeConverter<ETypeAddress, Long> {
	/** */
	private static final long serialVersionUID = -4184391306029983947L;
	
	public final static ETypeAddress MAIN = new ETypeAddress("MAIN", 1);
	public final static ETypeAddress HOME = new ETypeAddress("HOME", 2);
	public final static ETypeAddress WORK = new ETypeAddress("WORK", 3);
	public final static ETypeAddress INVOICE = new ETypeAddress("INVOICE", 4);
	public final static ETypeAddress DELIVERY = new ETypeAddress("DELIVERY", 5);
	
	public final static ETypeAddress IDCARDADDRESS = new ETypeAddress("IDCARDADDRESS", 6);
	public final static ETypeAddress HRADDRESS = new ETypeAddress("HRADDRESS", 7);
	public final static ETypeAddress MAILADDRESS = new ETypeAddress("MAILADDRESS", 8);
	public final static ETypeAddress SHOPADDRESS = new ETypeAddress("SHOPADDRESS", 11);
	

	
	/**
	 * 
	 */
	public ETypeAddress() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETypeAddress(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ETypeAddress convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ETypeAddress arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETypeAddress> values() {
		return getValues(ETypeAddress.class);
	}
	
	/** 
	 * @return
	 */
	public static List<ETypeAddress> valuesOfApplicants() {
		List<ETypeAddress> addressTypes = new ArrayList<>(); 
		addressTypes.add(MAIN);
		addressTypes.add(WORK);
		addressTypes.add(IDCARDADDRESS);
		addressTypes.add(HRADDRESS);
		addressTypes.add(MAILADDRESS);
		return addressTypes;
	}
	
	/** 
	 * @return
	 */
	public static List<ETypeAddress> valuesOfDealers() {
		List<ETypeAddress> addressTypes = new ArrayList<>(); 
		addressTypes.add(SHOPADDRESS);
		return addressTypes;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETypeAddress getById(long id) {
		return getById(ETypeAddress.class, id);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ETypeAddress getByCode(String code) {
		return getByCode(ETypeAddress.class, code);
	}
}
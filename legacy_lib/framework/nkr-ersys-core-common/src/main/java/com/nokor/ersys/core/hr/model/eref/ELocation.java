package com.nokor.ersys.core.hr.model.eref;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Employment type
 * 
 * @author ly.youhort
 *
 */
public class ELocation extends BaseERefData implements AttributeConverter<ELocation, Long> {
	/** 
	 */
	private static final long serialVersionUID = 745046236686469058L;
	
	public final static ELocation HEADQUARTER = new ELocation("HEADQUARTER", 1); // Headquarter
	public final static ELocation BRANCH = new ELocation("BRANCH", 2); // Branch
	public final static ELocation DEALERSHOP = new ELocation("DEALERSHOP", 3); // Dealer shop
	public final static ELocation CURRENTADDRESS = new ELocation("CURRENTADDRESS", 4);
	public final static ELocation REGISTRATIONADDRESS = new ELocation("REGISTRATIONADDRESS", 5);
	public final static ELocation OTHER = new ELocation("OTHER", 6);
	public final static ELocation CUSTOMERADDRESSS = new ELocation("CUSTOMERADDRESSS", 7);
	

	/**
	 * 
	 */
	public ELocation() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ELocation(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ELocation convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ELocation arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ELocation> values() {
		return getValues(ELocation.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ELocation getByCode(String code) {
		return getByCode(ELocation.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ELocation getById(long id) {
		return getById(ELocation.class, id);
	}
	
	/**
	 * get list of ELocation to CbxLocation1
	 * @return
	 */
	public static List<ELocation> getReturnLocationValues() { 
		List<ELocation> locations = new ArrayList<>();
		locations.add(ELocation.BRANCH);
		locations.add(ELocation.DEALERSHOP);
		locations.add(ELocation.CUSTOMERADDRESSS);
		locations.add(ELocation.OTHER);
		return locations;
	}
}

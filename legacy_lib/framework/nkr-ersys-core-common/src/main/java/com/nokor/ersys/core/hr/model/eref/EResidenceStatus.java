package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EResidenceStatus extends BaseERefData implements AttributeConverter<EResidenceStatus, Long> {
	/** */
	private static final long serialVersionUID = -2068583609270694341L;
	
	public final static EResidenceStatus OWNER = new EResidenceStatus("OWNER", 1);
	public final static EResidenceStatus RENTER = new EResidenceStatus("RENTER", 2);
	public final static EResidenceStatus SUB_RENTER = new EResidenceStatus("SUB_RENTER", 3);
	public final static EResidenceStatus ACCOMODATED = new EResidenceStatus("ACCOMODATED", 4);
	public final static EResidenceStatus HOSTEDBYFAMILY = new EResidenceStatus("HOSTEDBYFAMILY", 5);
	public final static EResidenceStatus HOSTEDBYFRIEND = new EResidenceStatus("HOSTEDBYFRIEND", 6);
		
	/**
	 */
	public EResidenceStatus() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EResidenceStatus(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EResidenceStatus convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EResidenceStatus arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EResidenceStatus> values() {
		return getValues(EResidenceStatus.class);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EResidenceStatus getById(long id) {
		return getById(EResidenceStatus.class, id);
	}
}
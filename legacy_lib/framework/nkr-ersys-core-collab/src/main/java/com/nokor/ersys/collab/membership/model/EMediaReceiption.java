package com.nokor.ersys.collab.membership.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EMediaReceiption extends BaseERefData implements AttributeConverter<EMediaReceiption, Long> {
	/** */
	private static final long serialVersionUID = -6871838507871119262L;

	public final static EMediaReceiption EMAIL = new EMediaReceiption("EMAIL", 1);
	public final static EMediaReceiption COURRIER = new EMediaReceiption("COURRIER", 2);
	public final static EMediaReceiption OTHER = new EMediaReceiption("OTHER", 3);

	/**
	 * 
	 */
	public EMediaReceiption() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EMediaReceiption(String code, long id) {
		super(code, id);
	}

	@Override
	public EMediaReceiption convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EMediaReceiption arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EMediaReceiption> values() {
		return getValues(EMediaReceiption.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EMediaReceiption getByCode(String code) {
		return getByCode(EMediaReceiption.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EMediaReceiption getById(long id) {
		return getById(EMediaReceiption.class, id);
	}
}

package com.nokor.efinance.core.asset.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author buntha.chea
 *
 */
public class EEngineStatus extends BaseERefData implements AttributeConverter<EEngineStatus, Long> {
	/** */
	private static final long serialVersionUID = 8800717908072047609L;

	/**
	 * 
	 */
	public EEngineStatus() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EEngineStatus(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EEngineStatus convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EEngineStatus arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EEngineStatus> values() {
		return getValues(EEngineStatus.class);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EEngineStatus getByCode(String code) {
		return getByCode(EEngineStatus.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EEngineStatus getById(long id) {
		return getById(EEngineStatus.class, id);
	}
}

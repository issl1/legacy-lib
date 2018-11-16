package com.nokor.efinance.core.asset.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Engine of vehicule
 * 
 * @author ly.youhort
 */
public class EEngine extends BaseERefData implements AttributeConverter<EEngine, Long> {
	/** */
	private static final long serialVersionUID = 8800717908072047609L;

	/**
	 */
	public EEngine() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EEngine(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EEngine convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EEngine arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/** 
	 * @return
	 */
	public static List<EEngine> values() {
		return getValues(EEngine.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EEngine getByCode(String code) {
		return getByCode(EEngine.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EEngine getById(long id) {
		return getById(EEngine.class, id);
	}
}

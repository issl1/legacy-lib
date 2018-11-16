package com.nokor.efinance.core.dealer.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Dealer type
 * 
 * @author youhort.ly
 */
public class ELadderOption extends BaseERefData implements AttributeConverter<ELadderOption, Long> {
	/** */
	private static final long serialVersionUID = -5713129714458211130L;

	public final static ELadderOption GROUP = new ELadderOption("GROUP", 1);
	public final static ELadderOption HEAD = new ELadderOption("HEAD", 2);	
	public final static ELadderOption UNIQUE = new ELadderOption("UNIQUE", 3);
	

	/**
	 */
	public ELadderOption() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ELadderOption(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ELadderOption convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ELadderOption arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<ELadderOption> values() {
		return getValues(ELadderOption.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ELadderOption getByCode(String code) {
		return getByCode(ELadderOption.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ELadderOption getById(long id) {
		return getById(ELadderOption.class, id);
	}
}

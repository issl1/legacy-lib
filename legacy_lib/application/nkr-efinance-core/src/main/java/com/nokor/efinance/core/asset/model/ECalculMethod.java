package com.nokor.efinance.core.asset.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Asset Calcul Method
 * 
 * @author ly.youhort
 *
 */
public class ECalculMethod extends BaseERefData implements AttributeConverter<ECalculMethod, Long> {
	/** */
	private static final long serialVersionUID = 941498702694462549L;

	public final static ECalculMethod FIX = new ECalculMethod("FIX", 1); // fixed.amount
	public final static ECalculMethod MAT = new ECalculMethod("MAT", 2); // pricing.matrix
	public final static ECalculMethod FOR = new ECalculMethod("FOR", 3); // formula
	public final static ECalculMethod PAP = new ECalculMethod("PAP", 4); // premium.based.on.percentage.asset.price

	/**
	 */
	public ECalculMethod() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public ECalculMethod(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ECalculMethod convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECalculMethod arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<ECalculMethod> values() {
		return getValues(ECalculMethod.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ECalculMethod getByCode(String code) {
		return getByCode(ECalculMethod.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static ECalculMethod getById(long id) {
		return getById(ECalculMethod.class, id);
	}
}

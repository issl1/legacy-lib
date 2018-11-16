package com.nokor.efinance.core.payment.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Penalty Calcul Method
 * @author ly.youhort
 *
 */
public class EPenaltyCalculMethod extends BaseERefData implements AttributeConverter<EPenaltyCalculMethod, Long> {
	/** */
	private static final long serialVersionUID = 5008957459929055286L;

	public final static EPenaltyCalculMethod PIS = new EPenaltyCalculMethod("PIS", 1); // percentage.of.installment
	public final static EPenaltyCalculMethod FPD = new EPenaltyCalculMethod("FPD", 2); // fixed.amount.per.day
	public final static EPenaltyCalculMethod FOR = new EPenaltyCalculMethod("FOR", 3); // formula
	public final static EPenaltyCalculMethod MRR = new EPenaltyCalculMethod("MRR", 4); // Minimum Return Rate

	/**
	 */
	public EPenaltyCalculMethod() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EPenaltyCalculMethod(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EPenaltyCalculMethod convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPenaltyCalculMethod arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EPenaltyCalculMethod> values() {
		return getValues(EPenaltyCalculMethod.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EPenaltyCalculMethod getByCode(String code) {
		return getByCode(EPenaltyCalculMethod.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EPenaltyCalculMethod getById(long id) {
		return getById(EPenaltyCalculMethod.class, id);
	}
}

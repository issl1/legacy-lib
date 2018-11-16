package com.nokor.efinance.core.contract.model.cashflow;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Cashflow type
 * @author ly.youhort
 *
 */
public class ECashflowType extends BaseERefData implements AttributeConverter<ECashflowType, Long> {
	/** */
	private static final long serialVersionUID = 8773689763758918561L;

	public final static String FIN_JOURNAL_EVENT = "0101";
	public final static String IAP_JOURNAL_EVENT = "01";
	public final static String CAP_JOURNAL_EVENT = "01";
	public final static String PEN_JOURNAL_EVENT = "02";
	
	
	public final static ECashflowType FIN = new ECashflowType("FIN", 1); // finance
	public final static ECashflowType IAP = new ECashflowType("IAP", 2); // interest.applicant
	public final static ECashflowType IMA = new ECashflowType("IMA", 3); // interest.manufacturer
	public final static ECashflowType FEE = new ECashflowType("FEE", 4); // fee
	public final static ECashflowType CAP = new ECashflowType("CAP", 5); // capital
	public final static ECashflowType PEN = new ECashflowType("PEN", 6); // penalty
	public final static ECashflowType SRV = new ECashflowType("SRV", 7); // service
	public final static ECashflowType THI = new ECashflowType("THI", 8); // Third Party

	/**
	 * 
	 */
	public ECashflowType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECashflowType(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ECashflowType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECashflowType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ECashflowType> values() {
		return getValues(ECashflowType.class);
	}
		
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ECashflowType getByCode(String code) {
		return getByCode(ECashflowType.class, code);
	}
		
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ECashflowType getById(long id) {
		return getById(ECashflowType.class, id);
	}
}

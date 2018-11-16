package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;


/**
 * 
 * @author ly.youhort
 *
 */
public class EMaritalStatus extends BaseERefData implements AttributeConverter<EMaritalStatus, Long> {
	
	/** */
	private static final long serialVersionUID = -5816477380150961496L;
	
	public final static EMaritalStatus MARRIED = new EMaritalStatus("MARRIED", 1); // married
	public final static EMaritalStatus SINGLE = new EMaritalStatus("SINGLE", 2); // single
	public final static EMaritalStatus WIDOW = new EMaritalStatus("WIDOW", 3);  // widow
	public final static EMaritalStatus DIVORCED = new EMaritalStatus("DIVORCED", 4); // divorced
	public final static EMaritalStatus SEPARATED = new EMaritalStatus("SEPARATED", 5); // separated
	public final static EMaritalStatus DEFACTO = new EMaritalStatus("DEFACTO", 6); // defacto
	public final static EMaritalStatus UNKNOWN = new EMaritalStatus("UNKNOWN", 7); // unknown

	
	/**
	 * 
	 */
	public EMaritalStatus() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EMaritalStatus(String code, long id) {
		super(code, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EMaritalStatus convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EMaritalStatus arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EMaritalStatus> values() {
		return getValues(EMaritalStatus.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EMaritalStatus getByCode(String code) {
		return getByCode(EMaritalStatus.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EMaritalStatus getById(long id) {
		return getById(EMaritalStatus.class, id);
	}
}

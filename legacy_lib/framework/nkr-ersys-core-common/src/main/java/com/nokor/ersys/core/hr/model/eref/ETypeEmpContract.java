package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ETypeEmpContract extends BaseERefData implements AttributeConverter<ETypeEmpContract, Long> {
	/** */
	private static final long serialVersionUID = -7819234341878887856L;

	public final static ETypeEmpContract FIXED_DURATION = new ETypeEmpContract("FIXED_DURATION", 1);
	public final static ETypeEmpContract UNSPECIFIED_DURATION = new ETypeEmpContract("UNSPECIFIED_DURATION", 2);
	public final static ETypeEmpContract TEMPORARY = new ETypeEmpContract("TEMPORARY", 3);
	public final static ETypeEmpContract INTERNSHIP = new ETypeEmpContract("INTERNSHIP", 4);
	public final static ETypeEmpContract CONTRACTOR = new ETypeEmpContract("CONTRACTOR", 5);

	/**
	 * 
	 */
	public ETypeEmpContract() {

	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ETypeEmpContract(String code, long id) {
		super(code, id);
	}

	@Override
	public ETypeEmpContract convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}

	@Override
	public Long convertToDatabaseColumn(ETypeEmpContract arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ETypeEmpContract> values() {
		return getValues(ETypeEmpContract.class);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ETypeEmpContract getById(long id) {
		return getById(ETypeEmpContract.class, id);
	}
}
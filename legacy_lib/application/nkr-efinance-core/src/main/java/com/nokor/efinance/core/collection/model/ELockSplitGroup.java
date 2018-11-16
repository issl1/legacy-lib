package com.nokor.efinance.core.collection.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Lock Split Group
 * @author uhout.cheng
 */
public class ELockSplitGroup extends BaseERefData implements AttributeConverter<ELockSplitGroup, Long> {
	
	/** */
	private static final long serialVersionUID = 6115989437914918730L;
	
	public final static ELockSplitGroup INS = new ELockSplitGroup("INS", 1l); //installments
	public final static ELockSplitGroup FEE = new ELockSplitGroup("FEE", 2l); //fees
	public final static ELockSplitGroup AFTERSALE = new ELockSplitGroup("AFTERSALE", 3l); //after sale event

	/**
	 * 
	 */
	public ELockSplitGroup() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ELockSplitGroup(String code, long id) {
		super(code, id);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ELockSplitGroup convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(ELockSplitGroup arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ELockSplitGroup> values() {
		List<ELockSplitGroup> values = new ArrayList<>();
		values.add(ELockSplitGroup.INS);
		values.add(ELockSplitGroup.FEE);
		values.add(ELockSplitGroup.AFTERSALE);
		return values;
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static ELockSplitGroup getByCode(String code) {
		return getByCode(ELockSplitGroup.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ELockSplitGroup getById(long id) {
		return getById(ELockSplitGroup.class, id);
	}
}

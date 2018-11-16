package com.nokor.ersys.core.hr.model.eref;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * Example for Private Company
 *    Holding, Subsidiary, Head Office/Branch, Department, Division, Office,..
 * Example for Ministry
 * 	   Ministry, Secretariat, General Department, Department, Cabinet, Institute, National Hospital, HC (standard), Referral Hospital...
 *  
 * @author prasnar
 *
 */
public class EOrgLevel extends BaseERefData implements AttributeConverter<EOrgLevel, Long> {
	/** */
	private static final long serialVersionUID = 8065584824049734482L;

	public final static EOrgLevel HOLDING = 	new EOrgLevel("HOLDING", 		1, 1);
	public final static EOrgLevel SUBSIDIARY = 	new EOrgLevel("SUBSIDIARY", 	2, 2);
	public final static EOrgLevel HEAD_OFFICE = new EOrgLevel("HEAD_OFFICE", 	3, 3);
	public final static EOrgLevel BRANCH = 		new EOrgLevel("BRANCH", 		4, 3);
	public final static EOrgLevel DEPARTMENT = 	new EOrgLevel("DEPARTMENT", 	5, 4);
	public final static EOrgLevel OFFICE = 		new EOrgLevel("OFFICE", 		6, 5);

	private int level;

	/**
	 * 
	 */
	public EOrgLevel() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 * @param level
	 */
	public EOrgLevel(String code, long id, int level) {
		super(code, id);
		this.level = level;
	}

	@Override
	public EOrgLevel convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EOrgLevel arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EOrgLevel> values() {
		return getValues(EOrgLevel.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EOrgLevel getById(long id) {
		return getById(EOrgLevel.class, id);
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
    
	/**
	 * 
	 * @return
	 */
	public boolean isBranch() {
		return BRANCH.equals(this);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isDepartment() {
		return DEPARTMENT.equals(this);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isOffice() {
		return OFFICE.equals(this);
	}
}

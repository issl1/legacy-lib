package com.nokor.common.app.cfield.model;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.model.eref.BaseERefData;
import org.seuksa.frmk.model.eref.SimpleERefData;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.helper.SeuksaServicesHelper;


/**
 * Customize Types
 * Type 
 * @author prasnar
 */
public class ECusType extends SimpleERefData implements AttributeConverter<ECusType, Long> {
	/** */
	private static final long serialVersionUID = -2353637099089010291L;

	public final static ECusType STRING 		= new ECusType(String.class.getCanonicalName(), 	1); 
	public final static ECusType DATE 			= new ECusType(Date.class.getCanonicalName(), 		2); 
	public final static ECusType BOOLEAN 		= new ECusType(Boolean.class.getCanonicalName(), 	3); 
	public final static ECusType INTEGER 		= new ECusType(Integer.class.getCanonicalName(), 	4); 
	public final static ECusType DECIMAL 		= new ECusType(Float.class.getCanonicalName(), 		5); 
	public final static ECusType ENTITYA 		= new ECusType(EntityA.class.getCanonicalName(), 	6); 
	public final static ECusType ENTITYREF 		= new ECusType(EntityRefA.class.getCanonicalName(), 7); 
	public final static ECusType ENUM_REFDATA 	= new ECusType(RefData.class.getCanonicalName(), 	8); 

    
	/**
	 * 
	 */
	public ECusType() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public ECusType(String code, long id) {
		super(code, id);
	}

	
	@Override
	public ECusType convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(ECusType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ECusType getById(long id) {
		return SimpleERefData.getById(ECusType.class, id);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ECusType getByCode(String code) {
		return SimpleERefData.getByCode(ECusType.class, code);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static ECusType valueOf(String code) {
		return SimpleERefData.valueOf(ECusType.class, code);
	}

	/**
	 * 
	 * @return
	 */
	public static List<ECusType> values() {
		return SimpleERefData.getValues(ECusType.class);
	}
		
	/**
	 * 
	 * @return
	 */
	public boolean isString() {
		return this.equals(STRING);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isNumber() {
		return isInteger() || isDecimal();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isInteger() {
		return this.equals(INTEGER);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isDecimal() {
		return this.equals(DECIMAL);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isDate() {
		return this.equals(DATE);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isBoolean() {
		return this.equals(BOOLEAN);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isEnumRefData() {
		return this.equals(ENUM_REFDATA);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public Object getCusValue (String value, Class<?> typeClass) {
		if (isString()) {
			return value;
		}
		if (isInteger()) {
			return Integer.valueOf(value);
		} 
		if (isDecimal()) {
			return Float.valueOf(value);
		} 
		if (isBoolean()) {
			return Boolean.valueOf(value);
		} 
		if (isDate()) {
			return DateUtils.string2DateDDMMYYYY(value);
		}
		if (isEnumRefData() && value != null) {
			String refDataClazzName = ((Class<? extends RefDataId>) typeClass).getName();
			SeuksaServicesHelper.REFDATA_SRV.forceBuildCacheRefData(refDataClazzName);
			return BaseERefData.getById((Class<? extends RefDataId>) typeClass, Long.valueOf(value));
		}
		
		return null;
	}

}
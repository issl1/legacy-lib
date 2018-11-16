package com.nokor.common.app.action.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * List of Action to check/apply before switching fromStatus->toStatus in WkfFlowItem
 * 
 * @author prasnar
 * 
 */
public class EActionType extends BaseERefData implements AttributeConverter<EActionType, Long> {
	/** */
	private static final long serialVersionUID = 2341635066828352197L;

	public static final EActionType CONSTR_VALUES = new EActionType("CONSTR_VALUES", 1L); 				// list of constraint values separated by comma
	public static final EActionType MANDATORY_FIELDS = new EActionType("CONSTR_VALUES", 2L); 			// list of constraint values separated by comma
	public static final EActionType JAVASCRIPT = new EActionType("JAVASCRIPT", 3L);						// trigger a JavaScriptEngine function
	public static final EActionType DROOLS = new EActionType("DROOLS", 4L);								// trigger a Drools function
	public static final EActionType URL = new EActionType("URL", 5L);									// trigger a URL redirection
	public static final EActionType JAVA_ACTION_SUPPORT = new EActionType("JAVA_ACTION_SUPPORT", 10L);	// trigger a Java method

	/**
	 * 
	 */
	public EActionType() {
	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EActionType(String code, long id) {
		super(code, id);
	}

	@Override
	public EActionType convertToEntityAttribute(Long id) {
		return ((EActionType) super.convertToEntityAttribute(id));
	}
	
	@Override
	public Long convertToDatabaseColumn(EActionType arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EActionType> values() {
		return getValues(EActionType.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EActionType getById(long id) {
		return getById(EActionType.class, id);
	}
	
}
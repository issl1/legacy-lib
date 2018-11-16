package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author buntha.chea
 *
 */
public class EColAction extends BaseERefData implements AttributeConverter<EColAction, Long> {

	/** */
	private static final long serialVersionUID = 6953819543849976337L;
	
	public final static EColAction SCHEDULE = new EColAction("001", 1);
	public final static EColAction SEELATER = new EColAction("002", 2);
	public final static EColAction NOFURTHER = new EColAction("003", 3);
	public final static EColAction CANTPRO = new EColAction("004", 4);
	public final static EColAction NONE = new EColAction("005", 5);

	/**
	 */
	public EColAction() {
	}
	
	/**
	 * @param code
	 * @param id
	 */
	public EColAction(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EColAction convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EColAction arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * @return
	 */
	public static List<EColAction> values() {
		return getValues(EColAction.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EColAction getByCode(String code) {
		return getByCode(EColAction.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EColAction getById(long id) {
		return getById(EColAction.class, id);
	}

}

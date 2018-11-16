package com.nokor.efinance.core.common.reference.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author uhout.cheng
 */
public class EBlackListReason extends BaseERefData implements AttributeConverter<EBlackListReason, Long> {
	
	/** */
	private static final long serialVersionUID = 5394129089046801124L;
	
	private EBlackListSource source;
	
	/**
	 * 
	 */
	public EBlackListReason() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 * @param source
	 */
	public EBlackListReason(String code, long id, EBlackListSource source) {
		super(code, id);
		this.source = source;
	}

	/**
	 * 
	 * @return
	 */
	public EBlackListSource getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(EBlackListSource source) {
		this.source = source;
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EBlackListReason convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EBlackListReason arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EBlackListReason> values() {
		return getValues(EBlackListReason.class);
	}
	
	/**
	 * 
	 * @param blackListSource
	 * @return
	 */
	public static List<EBlackListReason> values(EBlackListSource blackListSource) {
		List<EBlackListReason> blackListReasons = EBlackListReason.values();
		
		List<EBlackListReason> lstResBlackListReasons = new ArrayList<EBlackListReason>();
		for (EBlackListReason blackListReason : blackListReasons) {
			if (blackListReason.getSource().equals(blackListSource)) {
				lstResBlackListReasons.add(blackListReason);
			}
		}
		return lstResBlackListReasons;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EBlackListReason getByCode(String code) {
		return getByCode(EBlackListReason.class, code);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EBlackListReason getById(long id) {
		return getById(EBlackListReason.class, id);
	}
}

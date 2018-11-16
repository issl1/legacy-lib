package com.nokor.ersys.finance.accounting.model;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * @author bunlong.taing
 */
public class EJournalEventGroup extends BaseERefData implements AttributeConverter<EJournalEventGroup, Long> {
	/** */
	private static final long serialVersionUID = -9185688724416230979L;
	
	public EJournalEventGroup() {
	}
	
	public EJournalEventGroup(String code, long id) {
		super(code, id);
	}

	/**
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Long convertToDatabaseColumn(EJournalEventGroup journalEventGroup) {
		return super.convertToDatabaseColumn(journalEventGroup);
	}

	/**
	 * @see org.seuksa.frmk.model.eref.BaseERefData#convertToEntityAttribute(java.lang.Long)
	 */
	@Override
	public EJournalEventGroup convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	/**
	 * @return
	 */
	public static List<EJournalEventGroup> values() {
		return getValues(EJournalEventGroup.class);
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static EJournalEventGroup getByCode(String code) {
		return getByCode(EJournalEventGroup.class, code);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static EJournalEventGroup getById(Long id) {
		return getById(EJournalEventGroup.class, id);
	}

}

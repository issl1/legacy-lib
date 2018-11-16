package com.nokor.efinance.core.widget;

import java.util.List;

import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * @author bunlong.taing
 */
public class JournalEventComboBox extends EntityAComboBox<JournalEvent> {

	/** */
	private static final long serialVersionUID = -1536423462666487764L;

	/**
	 * 
	 * @param journalEvent
	 */
	public JournalEventComboBox(List<JournalEvent> journalEvent) {
		super(journalEvent);
	}
	
	/**
	 * 
	 * @param caption
	 * @param journalEvent
	 */
	public JournalEventComboBox(String caption, List<JournalEvent> journalEvent) {
		super(caption, journalEvent);
	}

	/**
	 * @see com.nokor.efinance.core.widget.EntityAComboBox#getEntityACaption(org.seuksa.frmk.model.entity.EntityA)
	 */
	@Override
	protected String getEntityACaption(JournalEvent journalEvent) {
		return journalEvent.getCode() + " - " + journalEvent.getDescLocale();
	}

}

package com.nokor.efinance.core.accounting.model;

import javax.persistence.DiscriminatorValue;

import com.nokor.ersys.finance.accounting.model.JournalEntry;

/**
 * Journal entry
 * 
 * @author prasnar
 *
 */
@DiscriminatorValue(value="E")
public class FinJournalEntry extends JournalEntry {
	/** */
	private static final long serialVersionUID = -4124346139519232502L;

	
}

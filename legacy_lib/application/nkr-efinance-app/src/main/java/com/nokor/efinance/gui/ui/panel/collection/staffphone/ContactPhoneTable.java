package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Contact phones table 
 * @author uhout.cheng
 */
public class ContactPhoneTable extends VerticalLayout implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 4201147348366336523L;
	
	private static final String BORROWER = "borrower";
	private static final String SPOUSE = "spouse";
	private static final String GUARANTOR = "guarantor";
	private static final String REFERENCE = "reference";
	
	private SimpleTable<Entity> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	
	/**
	 * 
	 * @param userPanel
	 */
	public ContactPhoneTable() {
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		
		Panel tablePanel = new Panel(simpleTable);
		tablePanel.setCaption(I18N.message("phones"));
		tablePanel.setStyleName(Reindeer.PANEL_LIGHT);
		addComponent(tablePanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(BORROWER, I18N.message("borrower"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(SPOUSE, I18N.message("spouse"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(GUARANTOR, I18N.message("guarantor"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 170));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param phones
	 */
	public void assignValues(Map<EApplicantType, List<String>> phones) {
		simpleTable.setContainerDataSource(getPhonesIndexedContainer(phones));
	}
	
	/**
	 * 
	 * @param phones
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getPhonesIndexedContainer(Map<EApplicantType, List<String>> phones) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (phones != null && !phones.isEmpty()) {
			int conMaxLength = getMaxLength(phones);
			for (int i = 0; i < conMaxLength; i++) {
				Item item = indexedContainer.addItem(i);
				item.getItemProperty(BORROWER).setValue(getContactInfo(phones, EApplicantType.C, i));
				item.getItemProperty(SPOUSE).setValue(getContactInfo(phones, EApplicantType.S, i));
				item.getItemProperty(GUARANTOR).setValue(getContactInfo(phones, EApplicantType.G, i));
				item.getItemProperty(REFERENCE).setValue(getContactInfo(phones, EApplicantType.O, i));
			}
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param phones
	 * @param applicantType
	 * @param i
	 * @return
	 */
	private String getContactInfo(Map<EApplicantType, List<String>> phones, EApplicantType applicantType, int i) {
		List<String> infos = phones.get(applicantType);
		if (infos != null && !infos.isEmpty()) {
			return infos.size() >= i + 1 ? infos.get(i) : "";
		}
		return null;
	}
	
	/**
	 * 
	 * @param phones
	 * @return
	 */
	private int getMaxLength(Map<EApplicantType, List<String>> phones) {
		int maxLength = 0;
		for (List<String> listPhones : phones.values()) {
			maxLength = maxLength < listPhones.size() ? listPhones.size() : maxLength;
		}
		return maxLength;
	}

}

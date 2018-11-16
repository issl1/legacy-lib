package com.nokor.efinance.gui.ui.panel.contract.notes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.contract.model.MContractNote;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;

/**
 * Note table 
 * @author uhout.cheng
 */
public class NotesTablePanel extends Panel implements ItemClickListener, SelectedItem, FinServicesHelper, MContractNote {

	/** */
	private static final long serialVersionUID = 5804312889880447009L;

	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	private Contract contract;
	
	/**
	 * 
	 */
	public NotesTablePanel() {
		init();
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeUndefined();
		simpleTable.setPageLength(3);
		simpleTable.addItemClickListener(this);
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = getSimpleTable(getColumnDefinitions());
		simpleTable.setSizeFull();
		setContent(simpleTable);
		setStyleName(Reindeer.PANEL_LIGHT);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("creation.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CREATEUSER, I18N.message("user"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(SUBJECT, I18N.message("subject"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(NOTE, I18N.message("note"), String.class, Align.LEFT, 200));
		// columnDefinitions.add(new ColumnDefinition(COMMENT, I18N.message("comment"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		refreshTable();
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer(List<ContractNote> contractNotes) {
		simpleTable.removeAllItems();
		selectedItem = null;
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (contractNotes != null && !contractNotes.isEmpty()) {
			for (ContractNote contractNote : contractNotes) {
				if (contractNote != null) {
					Item item = indexedContainer.addItem(contractNote.getId());
					item.getItemProperty(ID).setValue(contractNote.getId());
					item.getItemProperty(CREATEDATE).setValue(contractNote.getCreateDate());
					item.getItemProperty(CREATEUSER).setValue(contractNote.getUserLogin());
					item.getItemProperty(SUBJECT).setValue(contractNote.getSubject());
					item.getItemProperty(NOTE).setValue(contractNote.getNote());
					// item.getItemProperty(COMMENT).setValue(contractNote.getComment());
				}
			}
		}
	}
	
	/**
	 */
	public void refreshTable() {
		if (contract != null) {
			setTableIndexedContainer(NOTE_SRV.getNotesByContract(contract));
		}
	}

	/**
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty("id").getValue());
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			displayPopup(false);
		}
	}
	
	/**
	 * 
	 */
	public void displayPopup(boolean isNew) {
		NotesPopupPanel notesPopupPanel = NotesPopupPanel.show(contract, new NotesPopupPanel.Listener() {
			private static final long serialVersionUID = -8930832327089009034L;
			@Override
			public void onClose(NotesPopupPanel dialog) {
				refreshTable();
			}
		});
		if (isNew) {
			notesPopupPanel.reset();
		} else {
			ContractNote contractNote = null;
			if (getItemSelectedId() != null) {
				contractNote = CONT_SRV.getById(ContractNote.class, getItemSelectedId());
			}
			notesPopupPanel.assignValues(contractNote);
		}
	}

}

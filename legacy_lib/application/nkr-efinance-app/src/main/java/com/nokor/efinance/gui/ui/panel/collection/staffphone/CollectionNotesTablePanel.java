package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.contract.model.MContractNote;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.notes.NotesPopupPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;

/**
 * Note table in collection left panel
 * @author uhout.cheng
 */
public class CollectionNotesTablePanel extends AbstractControlPanel implements FinServicesHelper, MContractNote, ClickListener {

	/** */
	private static final long serialVersionUID = -6928109586521572575L;
	
	private SimpleTable<Entity> simpleTable;
	
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	private Contract contract;
	
	private Item selectedItem;
	
	/**
	 * 
	 */
	public CollectionNotesTablePanel() {
		setMargin(true);
		setSpacing(true);
		init();
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setPageLength(3);
		simpleTable.setSizeFull();
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		btnAdd = getButton("add", FontAwesome.PLUS);
		btnEdit = getButton("edit", FontAwesome.EDIT);
		btnDelete = getButton("delete", FontAwesome.TRASH_O);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		
		simpleTable = getSimpleTable(getColumnDefinitions());
		simpleTable.addItemClickListener(new ItemClickListener() {

			private static final long serialVersionUID = -8377328240778297442L;

			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				if (event.isDoubleClick()) {
					displayPopup(false);
				}
				
			}
		});
		
		addComponent(navigationPanel);
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @param caption
	 * @param icon
	 * @return
	 */
	private Button getButton(String caption, Resource icon) {
		Button button = new NativeButton(I18N.message(caption));
		button.setIcon(icon);
		button.addClickListener(this);
		return button;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 30));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("creation.date"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(CREATEUSER, I18N.message("user"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(SUBJECT, I18N.message("subject"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(NOTE, I18N.message("note"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		simpleTable.removeAllItems();
		selectedItem = null;
		if (contract != null) {
			setTableIndexedContainer(NOTE_SRV.getNotesByContract(contract));
		}
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	private void setTableIndexedContainer(List<ContractNote> contractNotes) {
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
				}
			}
		}
	}
	
	/**
	 * displayPopup
	 */
	public void displayPopup(boolean isNew) {
		NotesPopupPanel notesPopupPanel = NotesPopupPanel.show(contract, new NotesPopupPanel.Listener() {
			private static final long serialVersionUID = -8930832327089009034L;
			@Override
			public void onClose(NotesPopupPanel dialog) {
				assignValues(contract);
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
	
	/**
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty("id").getValue());
		}
		return null;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			displayPopup(true);
		} else if (event.getButton() == btnEdit) {
			if (selectedItem == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				displayPopup(false);
			}
		} else if (event.getButton() == btnDelete) {
			if (selectedItem == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
						new String[] {getItemSelectedId().toString()}),
						new ConfirmDialog.Listener() {
					
					/** */
					private static final long serialVersionUID = 6224381821671532392L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							Long id = getItemSelectedId();
							NOTE_SRV.delete(ContractNote.class, id);
							ComponentLayoutFactory.getNotificationDesc(id.toString(), "item.deleted.successfully");
							assignValues(contract);
							selectedItem = null;
						}
					}
				});
			}
		}
		
	}

}

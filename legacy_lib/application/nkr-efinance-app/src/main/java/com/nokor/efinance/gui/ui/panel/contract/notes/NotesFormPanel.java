package com.nokor.efinance.gui.ui.panel.contract.notes;

import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Notes form panel
 * @author uhout.cheng
 */
public class NotesFormPanel extends Panel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -4127939593851615582L;
	
	private NotesTablePanel notesTablePanel;
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	
	/**
	 * 
	 * @param caption
	 */
	public NotesFormPanel(String caption) {
		setCaption(I18N.message(caption));
		init();
	}
	
	/**
	 * 
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
	 */
	private void init() {
		btnAdd = getButton("new", FontAwesome.PLUS);
		btnEdit = getButton("edit", FontAwesome.PENCIL);
		btnDelete = getButton("delete", FontAwesome.TRASH_O);
		
		notesTablePanel = new NotesTablePanel();
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		
		VerticalLayout contentLayout = new VerticalLayout();
		
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(notesTablePanel);
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(contentLayout);
	}

	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		notesTablePanel.assignValues(contract);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			notesTablePanel.displayPopup(true);
		} else if (event.getButton().equals(btnEdit)) {
			if (notesTablePanel.getSelectedItem() == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				notesTablePanel.displayPopup(false);
			}
		} else if (event.getButton().equals(btnDelete)) {
			if (notesTablePanel.getSelectedItem() == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
						new String[] {notesTablePanel.getItemSelectedId().toString()}),
						new ConfirmDialog.Listener() {
					
					/** */
					private static final long serialVersionUID = 6224381821671532392L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							Long id = notesTablePanel.getItemSelectedId();
							NOTE_SRV.delete(ContractNote.class, id);
							ComponentLayoutFactory.getNotificationDesc(id.toString(), "item.deleted.successfully");
							notesTablePanel.refreshTable();
						}
					}
				});
			}
		}
	}

}

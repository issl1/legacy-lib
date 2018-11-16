package com.nokor.efinance.gui.ui.panel.contract.notes;

import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.notes.popup.NewNotePopupPanel;
import com.nokor.efinance.gui.ui.panel.contract.notes.popup.NewReminderPopupPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class NotePanel extends VerticalLayout implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3489132475879144696L;
	
	private TabSheet tabSheet;
	
	private Button btnNewNote;
	private Button btnNewReminder;
	
	private Contract contract;
	
	private HistoryTabPanel historyTabPanel;
	private UserNoteTabPanel userNoteTabPanel;
	private ReminderTabPanel reminderTabPanel;
	
	public NotePanel() {
		tabSheet = new TabSheet();
		
		btnNewNote = ComponentLayoutFactory.getButtonStyle("new.note", FontAwesome.PLUS, 100, "btn btn-success");
		btnNewNote.addClickListener(this);
		btnNewReminder = ComponentLayoutFactory.getButtonStyle("new.reminder", FontAwesome.PLUS, 110, "btn btn-success");
		btnNewReminder.addClickListener(this);
		
		historyTabPanel = new HistoryTabPanel();
		userNoteTabPanel = new UserNoteTabPanel();
		reminderTabPanel = new ReminderTabPanel();
		
		tabSheet.addTab(historyTabPanel, I18N.message("history"));
		tabSheet.addTab(userNoteTabPanel, I18N.message("user.notes"));
		tabSheet.addTab(reminderTabPanel, I18N.message("reminders"));
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnNewNote);
		buttonLayout.addComponent(btnNewReminder);
		
		VerticalLayout noteLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		noteLayout.addComponent(buttonLayout);
		noteLayout.addComponent(tabSheet);
		noteLayout.setComponentAlignment(buttonLayout, Alignment.TOP_RIGHT);
		
		addComponent(noteLayout);
	}
	
	/**
	 * AssignValue
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		historyTabPanel.assignValues(contract);
		userNoteTabPanel.assignValues(contract);
		reminderTabPanel.assignValues(contract, historyTabPanel);
	}
	

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnNewNote) {
			NewNotePopupPanel notePopupPanel = NewNotePopupPanel.show(contract, new NewNotePopupPanel.Listener() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void onClose(NewNotePopupPanel dialog) {
					assignValues(contract);
				}
			});
			notePopupPanel.assignValues(new ContractNote());
			UI.getCurrent().addWindow(notePopupPanel);
		} else if (event.getButton() == btnNewReminder) {
			NewReminderPopupPanel reminderPopupPanel = NewReminderPopupPanel.show(contract, new NewReminderPopupPanel.Listener() {
				
				private static final long serialVersionUID = 7477610833213006841L;

				@Override
				public void onClose(NewReminderPopupPanel dialog) {
					assignValues(contract);
				}
			});
			reminderPopupPanel.assignValues(new Reminder());
			UI.getCurrent().addWindow(reminderPopupPanel);
		}
		
	}

}

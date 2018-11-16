package com.nokor.efinance.gui.ui.panel.collection.phone.history;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractHistoryFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.history.all.ColPhoneAllHistoriesTable;
import com.nokor.efinance.gui.ui.panel.collection.phone.history.reminder.ReminderFormPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;


/**
 * Histories panel in collection phone
 * @author uhout.cheng
 */
public class ColPhoneHistoriesPanel extends VerticalLayout implements SelectedTabChangeListener, ClickListener {

	/** */
	private static final long serialVersionUID = 2983086160753252108L;
	
	private ReminderFormPanel reminderFormPanel;
	
	private Button btnRefresh;
	
	private TabSheet accordionPanel;
	
	private ColPhoneAllHistoriesTable phoneAllHistoriesTable;
	private ColPhoneHistoryTable contactsHistoryTable;
	private ColPhoneHistoryTable commentstHistoryTable;
	private ColPhoneHistoryTable remindersHistoryTable;
	private ColPhoneHistoryTable smstHistoryTable;
	private ColPhoneHistoryTable paymentsHistoryTable;
	private ColPhoneHistoryTable lockSplitsHistoryTable;
	private ColPhoneHistoryTable requestsHistoryTable;
	private ColPhoneHistoryTable updatesHistoryTable;
	private ColPhoneHistoryTable systemsHistoryTable;
	
	private Contract contract;
	
	public ColPhoneHistoriesPanel() {
		init(true);
	}
	
	/**
	 * 
	 * @param deleget
	 */
	public ColPhoneHistoriesPanel(ColContractHistoryFormPanel deleget) {
		init(false);
		reminderFormPanel.setDeleget(deleget);
	}
	
	/**
	 * 
	 * @param isSetMagine
	 */
	private void init(boolean isSetMagine) {
		reminderFormPanel = new ReminderFormPanel();
		
		btnRefresh = ComponentLayoutFactory.getDefaultButton("refresh", FontAwesome.REFRESH, 80);
		btnRefresh.addClickListener(this);

		accordionPanel = new TabSheet();
		accordionPanel.addSelectedTabChangeListener(this);
		
		phoneAllHistoriesTable = new ColPhoneAllHistoriesTable();
		
		contactsHistoryTable = new ColPhoneHistoryTable(FinHistoryType.FIN_HIS_CNT);
		commentstHistoryTable = new ColPhoneHistoryTable(FinHistoryType.FIN_HIS_CMT);
		remindersHistoryTable = new ColPhoneHistoryTable(FinHistoryType.FIN_HIS_REM);
		smstHistoryTable = new ColPhoneHistoryTable(FinHistoryType.FIN_HIS_SMS);
		paymentsHistoryTable = new ColPhoneHistoryTable(FinHistoryType.FIN_HIS_PAY);
		lockSplitsHistoryTable = new ColPhoneHistoryTable(FinHistoryType.FIN_HIS_LCK);
		requestsHistoryTable = new ColPhoneHistoryTable(FinHistoryType.FIN_HIS_REQ);
		updatesHistoryTable = new ColPhoneHistoryTable(FinHistoryType.FIN_HIS_UPD);
		systemsHistoryTable = new ColPhoneHistoryTable(FinHistoryType.FIN_HIS_SYS);
		
		accordionPanel.addTab(phoneAllHistoriesTable, I18N.message("all"));
		accordionPanel.addTab(contactsHistoryTable, I18N.message("contacts"));
		accordionPanel.addTab(commentstHistoryTable, I18N.message("comments"));
		accordionPanel.addTab(remindersHistoryTable, I18N.message("reminders"));
		accordionPanel.addTab(smstHistoryTable, I18N.message("sms"));
		accordionPanel.addTab(paymentsHistoryTable, I18N.message("payments"));
		accordionPanel.addTab(lockSplitsHistoryTable, I18N.message("lock.splits"));
		accordionPanel.addTab(updatesHistoryTable, I18N.message("update"));
		accordionPanel.addTab(systemsHistoryTable, I18N.message("system"));
		accordionPanel.addTab(requestsHistoryTable, I18N.message("requests"));
		
		setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("histories") + "</h3>");
		setCaptionAsHtml(true);
		setSpacing(true);
		addComponent(reminderFormPanel);
		if (isSetMagine) {
			VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, true, true), true);
			verLayout.addComponent(btnRefresh);
			verLayout.addComponent(accordionPanel);
			addComponent(verLayout);
		} else {
			addComponent(btnRefresh);
			addComponent(accordionPanel);
		}
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		assignValuesSelectedTab();
	}
	
	/**
	 * 
	 */
	private void assignValuesSelectedTab() {
		if (accordionPanel.getSelectedTab().equals(contactsHistoryTable)) {
			contactsHistoryTable.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(commentstHistoryTable)) {
			commentstHistoryTable.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(remindersHistoryTable)) {
			remindersHistoryTable.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(smstHistoryTable)) {
			smstHistoryTable.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(paymentsHistoryTable)) {
			paymentsHistoryTable.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(lockSplitsHistoryTable)) {
			lockSplitsHistoryTable.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(requestsHistoryTable)) {
			requestsHistoryTable.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(updatesHistoryTable)) {
			updatesHistoryTable.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(systemsHistoryTable)) {
			systemsHistoryTable.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(phoneAllHistoriesTable)) {
			phoneAllHistoriesTable.assignValues(contract);
		} 
	}
	
	/**
	 * 
	 * @param contract
	 * @param firstTabSelect
	 */
	public void assignValues(Contract contract, boolean firstTabSelect) {
		this.contract = contract;
		reminderFormPanel.assignValues(contract);
		if (firstTabSelect) {
			accordionPanel.setSelectedTab(contactsHistoryTable);
		}
	}
	
	/**
	 * 
	 */
	public void reset() {
		reminderFormPanel.reset();
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnRefresh)) {
			assignValuesSelectedTab(); 
		}
	}
	
}

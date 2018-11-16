package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.reminder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.MainUI;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractHistoryFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColPhoneContractHistoryPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class CurrentReminderWindowPanel extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 2092629023750199000L;
	
	private static Logger logger = LoggerFactory.getLogger(CurrentReminderWindowPanel.class);
	
	private SimpleTable<Reminder> simpleTable;
	
	private ColContractHistoryFormPanel deleget;
	private MainUI mainUI;
	
	/**
	 * 
	 */
	public CurrentReminderWindowPanel() {
		setCaption(I18N.message("reminders"));
		setModal(true);
		setResizable(false);
		
		simpleTable = new SimpleTable<Reminder>(getColumnDefinitions());
		simpleTable.setPageLength(5);
		simpleTable.setSizeUndefined();
		simpleTable.setCaption(I18N.message("reminders"));
		
		setContent(ComponentLayoutFactory.setMargin(simpleTable));
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(Reminder.ID, I18N.message("id"), Long.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition(Reminder.CONTRACT, I18N.message("contract"), Button.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Reminder.DATE, I18N.message("date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Reminder.COMMENT, I18N.message("comment"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(Reminder.DISMISS, I18N.message("dismiss"), Button.class, Align.CENTER, 70));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param reminders
	 */
	private void assignValues(List<Reminder> reminders) {
		setIndexedContainer(reminders);
	}
	
	/**
	 * 
	 * @param reminders
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Reminder> reminders) {
		simpleTable.removeAllItems();
		Container container = simpleTable.getContainerDataSource();
		if (reminders != null && !reminders.isEmpty()) {
			for (Reminder reminder : reminders) {
				Item item = container.addItem(reminder);
				item.getItemProperty(Reminder.ID).setValue(reminder.getId());
				item.getItemProperty(Reminder.CONTRACT).setValue(new ContractLinkButtonRenderer(reminder.getContract()));
				item.getItemProperty(Reminder.DATE).setValue(reminder.getDate());
				item.getItemProperty(Reminder.COMMENT).setValue(reminder.getComment());
				item.getItemProperty(Reminder.DISMISS).setValue(getRenderButtonDismiss(reminder));
			}
		}
	} 
	
	/**
	 * @param reminder
	 * @return
	 */
	private Button getRenderButtonDismiss(Reminder reminder) {
		final Button button = ComponentLayoutFactory.getDefaultButton("dismiss", null, 60);
		button.setImmediate(true);
		button.setData(reminder);
		
		button.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -498803040180803119L;

			/** 
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				Reminder rem = (Reminder) button.getData();
				rem.setDismiss(true);
				
				ConfirmDialog.show(UI.getCurrent(), I18N.message("dismiss.mgs.single"),	new ConfirmDialog.Listener() {
								
					/** */
					private static final long serialVersionUID = -4392067543652315524L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							try {
								REMINDER_SRV.update(rem);
								dialog.close();
								ComponentLayoutFactory.displaySuccessMsg("msg.dismiss.successfully");
								if (deleget != null) {
									assignValues(REMINDER_SRV.list(deleget.getReminderRestrictions()));
									deleget.assignReminders();
								} else {
									assignValues(REMINDER_SRV.list(mainUI.getReminderRestrictions()));
									mainUI.createReminederButton();
								}
							} catch (Exception e) {
								logger.error(e.getMessage());
							}
						}
					}
				});
			}
		});
		return button;
	}
	
	/**
	 * 
	 * @param reminders
	 * @param deleget
	 * @return
	 */
	public static CurrentReminderWindowPanel show(List<Reminder> reminders, ColContractHistoryFormPanel deleget) {   	
		CurrentReminderWindowPanel currentReminderWindowPanel = new CurrentReminderWindowPanel();
		currentReminderWindowPanel.deleget = deleget;
		currentReminderWindowPanel.assignValues(reminders);
	    return currentReminderWindowPanel;
	}
	
	/**
	 * 
	 * @param reminders
	 * @param mainUI
	 * @return
	 */
	public static CurrentReminderWindowPanel show(List<Reminder> reminders, MainUI mainUI) {   	
		CurrentReminderWindowPanel currentReminderWindowPanel = new CurrentReminderWindowPanel();
		currentReminderWindowPanel.mainUI = mainUI;
		currentReminderWindowPanel.assignValues(reminders);
	    return currentReminderWindowPanel;
	}
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class ContractLinkButtonRenderer extends Button {

		private static final long serialVersionUID = -2789150565195318571L;

		public ContractLinkButtonRenderer(Contract contract) {
			
			setCaption(contract.getReference());
			setStyleName(Reindeer.BUTTON_LINK);
			addClickListener(new ClickListener() {
		
				private static final long serialVersionUID = 8209188913689404417L;

				@Override
				public void buttonClick(ClickEvent event) {
					Page.getCurrent().setUriFragment("!" + ColPhoneContractHistoryPanel.NAME + "/" + contract.getId());
					close();
				}
			});
		}
	}

}

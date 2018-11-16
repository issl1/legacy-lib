package com.nokor.efinance.gui.ui.panel.contract.notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.MNote;
import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;

/**
 * 
 * @author buntha.chea
 *
 */
public class ReminderTabPanel extends AbstractControlPanel implements MNote, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1097296430380358747L;
	
	private SimplePagedTable<Entity> simplePagedTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private HistoryTabPanel historyTabPanel;
	
	private Contract contract;
	
	public ReminderTabPanel() {
		setMargin(true);
		setSpacing(true);
		
		this.columnDefinitions = createColumnDifinitions();
		simplePagedTable = new SimplePagedTable<>(this.columnDefinitions);
				
		addComponent(simplePagedTable);
		addComponent(simplePagedTable.createControls());
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDifinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(DATE_TIME, I18N.message("date.time"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(USER_DEPARTMENT, I18N.message("user.department"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(TARGET_DATE_TIME, I18N.message("target.date.time"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(TO_DO, I18N.message("to.do"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(OPTION, I18N.message("option"), Button.class, Align.LEFT, 80));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contracts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getIndexedContainer(List<Reminder> reminders) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		
		if (reminders != null && !reminders.isEmpty()) {
			for (Reminder reminder : reminders) {
				Item item = indexedContainer.addItem(reminder.getId());
				item.getItemProperty(DATE_TIME).setValue(dateFormat.format(reminder.getDate()));
				item.getItemProperty(USER_DEPARTMENT).setValue(NOTE_SRV.getUserDepartment(reminder.getCreateUser()));
				item.getItemProperty(TARGET_DATE_TIME).setValue(dateFormat.format(reminder.getDate()));
				item.getItemProperty(TO_DO).setValue(reminder.getComment());
				item.getItemProperty(OPTION).setValue(new DismissButtonRenderer(reminder));
			}
		}
		
		return indexedContainer;
	}
	
	/**
	 * AssignValue
	 * @param contract
	 */
	public void assignValues(Contract contract, HistoryTabPanel historyTabPanel) {
		this.contract = contract;
		this.historyTabPanel = historyTabPanel;
		simplePagedTable.setContainerDataSource(getIndexedContainer(REMINDER_SRV.getReminderByContract(contract)));
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class DismissButtonRenderer extends NativeButton {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -6102335959190462218L;

		public DismissButtonRenderer(Reminder reminder) {
			setCaption(I18N.message("dismiss"));
			setIcon(FontAwesome.TIMES);
			setStyleName("btn btn-danger button-small");
			
			addClickListener(new ClickListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = -2457447372817153725L;

				@Override
				public void buttonClick(ClickEvent event) {
					
					
					ConfirmDialog.show(UI.getCurrent(), I18N.message("dismiss.mgs.single"),	new ConfirmDialog.Listener() {
									
						/** */
						private static final long serialVersionUID = -4392067543652315524L;

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								try {
									reminder.setDismiss(true);
									REMINDER_SRV.update(reminder);
									dialog.close();
									ComponentLayoutFactory.displaySuccessMsg("msg.dismiss.successfully");
									assignValues(contract, historyTabPanel);
									historyTabPanel.assignValues(contract);
								} catch (Exception e) {
									logger.error(e.getMessage());
								}
							}
						}
					});
				}
			});
		}
	}

}

package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.EColAction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.dashboard.InboxCollectionPhoneStaff;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class CollectionPhoneStaffDashboardPanel extends VerticalLayout implements ClickListener, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5160775955453227270L;
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimpleTable<Entity> taskRecapTable;
	private SimpleTable<Entity> taskScheduleTable;
	private SimpleTable<Entity> performanceTable;
	
	private Button btnSeeAll;
	private InboxCollectionPhoneStaff inboxCollectionPhoneStaff;
	
	private int nbMissAction;
	private int nbPending;
	private int nbUnprocessContract;
	private int nbNoAction;
	
	private int nbContractAssigned;
	private int nbContractPayments;
	private int nbContractCleared;
	private double totalAmountPress;
	private double totalAmountReceived;
	
	/**
	 * 
	 */
	public CollectionPhoneStaffDashboardPanel(InboxCollectionPhoneStaff inboxCollectionPhoneStaff) {
		this.inboxCollectionPhoneStaff = inboxCollectionPhoneStaff;
		setSpacing(true);
		setMargin(true);
		
		Panel taskRecapPanel = new Panel();
		taskRecapPanel.setCaption(I18N.message("tasks.recap"));
		this.columnDefinitions = createColumnTaskRecapDefinitions();
		taskRecapTable = new SimpleTable<>(this.columnDefinitions);
		taskRecapTable.setPageLength(2);
		taskRecapPanel.setContent(taskRecapTable);
		
		Panel taskSchedulePanel = new Panel(I18N.message("tasks.schedule"));
		this.columnDefinitions = createColumnTaskScheduleDefinitions();
		taskScheduleTable = new SimpleTable<>(this.columnDefinitions);
		taskScheduleTable.setPageLength(10);
		taskSchedulePanel.setContent(taskScheduleTable);
		
		Panel performancePanel = new Panel();
		performancePanel.setCaption(I18N.message("performance"));
		this.columnDefinitions = createColumnPerformanceDefinitions();
		performanceTable = new SimpleTable<>(this.columnDefinitions);
		performanceTable.setPageLength(2);
		performancePanel.setContent(performanceTable);
		
		btnSeeAll = new NativeButton(I18N.message("see.all"), this);
		btnSeeAll.setStyleName("btn btn-success button-small");
		btnSeeAll.setIcon(FontAwesome.EYE);
		btnSeeAll.setWidth("100px");
		
		this.assignValue();
		
		addComponent(taskRecapPanel);
		addComponent(taskSchedulePanel);
		addComponent(btnSeeAll);
		setComponentAlignment(btnSeeAll, Alignment.BOTTOM_RIGHT);
		addComponent(performancePanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnTaskRecapDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("empty", I18N.message(""), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("unprocessed.contract", I18N.message("unprocessed.contract"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("missed.action", I18N.message("missed.action"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("pending", I18N.message("pending"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("no.action", I18N.message("no.action"), String.class, Align.LEFT, 125));
		return columnDefinitions;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void setTaskRecapIndexContainer() {
		taskRecapTable.removeAllItems();
		Container indexedContainer = taskRecapTable.getContainerDataSource();
		String taskRecap[] = {"Value","Percent"};
		for(int i = 0; i < taskRecap.length ; i++) {
			if (i == 0) {
				Item item = indexedContainer.addItem(i);
				item.getItemProperty("empty").setValue(taskRecap[i]);
				item.getItemProperty("unprocessed.contract").setValue(String.valueOf(nbUnprocessContract));
				item.getItemProperty("missed.action").setValue(String.valueOf(nbMissAction));
				item.getItemProperty("pending").setValue(String.valueOf(nbPending));
				item.getItemProperty("no.action").setValue(String.valueOf(nbNoAction));
			} else {
				double totalValue = nbUnprocessContract + nbMissAction + nbPending + nbNoAction;
				double unprocessContractPercent = nbUnprocessContract > 0 ? nbUnprocessContract / totalValue : 0d;
				double missActionPercent = nbMissAction > 0 ? nbMissAction / totalValue : 0d;
				double pendingPercent = nbPending > 0 ? nbPending / totalValue : 0d;
				double noActionPercent = nbNoAction > 0 ? nbNoAction / totalValue : 0d;
				
				Item item = indexedContainer.addItem(i);
				item.getItemProperty("empty").setValue(taskRecap[i]);
				item.getItemProperty("unprocessed.contract").setValue(AmountUtils.format(unprocessContractPercent));
				item.getItemProperty("missed.action").setValue(AmountUtils.format(missActionPercent));
				item.getItemProperty("pending").setValue(AmountUtils.format(pendingPercent));
				item.getItemProperty("no.action").setValue(AmountUtils.format(noActionPercent));
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnTaskScheduleDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("action", I18N.message(""), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("missed", I18N.message("missed"), Integer.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("today", I18N.message("today"), Integer.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("tomorrow", I18N.message("tomorrow"), Integer.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("later", I18N.message("later"), Integer.class, Align.LEFT, 125));
		return columnDefinitions;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void setTaskScheduleIndexContainer() {
		taskScheduleTable.removeAllItems();
		Container indexedContainer = taskScheduleTable.getContainerDataSource();
		List<EColAction> colActions = COL_ACT_SRV.getCollectionActionInCollection();
		Date today = DateUtils.todayH00M00S00();
		for (EColAction action : colActions) {
			Item item = indexedContainer.addItem(action.getId());
			item.getItemProperty("action").setValue(action.getDescEn());
			int nbColActMissed = COL_ACT_SRV.countCollectionAction(action, null);
			int nbColActToday = COL_ACT_SRV.countCollectionAction(action, today);
			int nbColActTomorrow = COL_ACT_SRV.countCollectionAction(action, DateUtils.addDaysDate(today, 1));
			int nbColActLater = COL_ACT_SRV.countCollectionAction(action, DateUtils.addDaysDate(today, 2));
			if (nbColActMissed > 0) {
				item.getItemProperty("missed").setValue(nbColActMissed);
				nbMissAction += nbColActMissed;
			}
			if (nbColActToday > 0) {
				item.getItemProperty("today").setValue(nbColActToday);
				nbPending += nbColActToday;
			}
			if (nbColActTomorrow > 0) {
				item.getItemProperty("tomorrow").setValue(nbColActTomorrow);
				nbPending += nbColActTomorrow;
			}
			if (nbColActLater > 0) {
				item.getItemProperty("later").setValue(nbColActLater);
				nbPending += nbColActLater;
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnPerformanceDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("empty", I18N.message(""), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("number.of.contract.assigned", I18N.message("number.of.contract.assigned"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("number.of.pmts", I18N.message("number.of.pmts"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("total.amount.press", I18N.message("total.amount.press"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("total.amoutn.received", I18N.message("total.amount.received"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition("number.of.cleared", I18N.message("number.of.cleared"), String.class, Align.LEFT, 125));
		return columnDefinitions;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void setPerformanceIndexContainer() {
		performanceTable.removeAllItems();
		Container indexedContainer = performanceTable.getContainerDataSource();
		String performance[] = {"Value","Percent"};
		for(int i = 0; i < performance.length ; i++) {
			if (i == 0) {
				Item item = indexedContainer.addItem(i);
				nbContractAssigned = COL_ACT_SRV.getContractsAssigned().size();
				/*nbContractPayments = COL_ACT_SRV.countPayments();
				nbContractCleared = COL_ACT_SRV.countCleared();
				totalAmountPress = COL_ACT_SRV.getTotalAmountPress();
				totalAmountReceived = COL_ACT_SRV.getTotalAmountPress();*/
				item.getItemProperty("empty").setValue(performance[i]);
				item.getItemProperty("number.of.contract.assigned").setValue(String.valueOf(nbContractAssigned));
				item.getItemProperty("number.of.pmts").setValue(String.valueOf(nbContractPayments));
				item.getItemProperty("total.amount.press").setValue(AmountUtils.format(totalAmountPress));
				item.getItemProperty("total.amoutn.received").setValue(AmountUtils.format(totalAmountReceived));
				item.getItemProperty("number.of.cleared").setValue(String.valueOf(nbContractCleared));
			} else {
				Item item = indexedContainer.addItem(i);
				double totalValueNumber = nbContractAssigned + nbContractPayments + nbContractCleared;
				double totalValueAmount = totalAmountPress + totalAmountReceived;
				double contractAssignedPercent = nbContractAssigned > 0 ? nbContractAssigned / totalValueNumber : 0d;
				double contractPmtsPercent = nbContractPayments > 0 ? nbContractPayments / totalValueNumber : 0d;
				double contractClearedPercent = nbContractCleared > 0 ? nbContractCleared / totalValueNumber : 0d;
				double totalAmountPressPercent = totalAmountPress > 0d ? totalAmountPress / totalValueAmount : 0d;
				double totalAmountReceivedPercent = totalAmountReceived > 0d ? totalAmountReceived / totalValueAmount : 0d;
				
				item.getItemProperty("empty").setValue(performance[i]);
				item.getItemProperty("number.of.contract.assigned").setValue(AmountUtils.format(contractAssignedPercent));
				item.getItemProperty("number.of.pmts").setValue(AmountUtils.format(contractPmtsPercent));
				item.getItemProperty("total.amount.press").setValue(AmountUtils.format(totalAmountPressPercent));
				item.getItemProperty("total.amoutn.received").setValue(AmountUtils.format(totalAmountReceivedPercent));
				item.getItemProperty("number.of.cleared").setValue(AmountUtils.format(contractClearedPercent));
			}
		}
	}
	
	/**
	 * Assign Value
	 */
	public void assignValue() {
		nbMissAction = 0;
		nbPending = 0;
		nbUnprocessContract = 0;
		nbNoAction = COL_ACT_SRV.countNoAction();
		setTaskScheduleIndexContainer();
		setTaskRecapIndexContainer();
		setPerformanceIndexContainer();
	}
	
	/**
	 * 
	 * @param event
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSeeAll) {
			if (inboxCollectionPhoneStaff != null) {
				inboxCollectionPhoneStaff.getTabSheet().setSelectedTab(inboxCollectionPhoneStaff.getColTaskTabPanel());
			}
		}
	}
}

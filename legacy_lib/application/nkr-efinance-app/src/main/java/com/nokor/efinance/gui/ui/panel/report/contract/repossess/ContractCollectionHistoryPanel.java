package com.nokor.efinance.gui.ui.panel.report.contract.repossess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.ColCustField;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.ContractCollectionHistory;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.vaadin.data.Container.Indexed;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Collection history panel
 * @author bunlong.taing
 */
public class ContractCollectionHistoryPanel extends AbstractTabPanel implements AddClickListener {

	/** */
	private static final long serialVersionUID = -4574256934617987307L;
	
	private ERefDataComboBox<EWkfStatus> cbxCollecitonStatus;
	private EntityRefComboBox<ColCustField> cbxcustomerAttribute;
	private NumberField txtAmountPromiseToPay;
	private DateField dfStartPeriodPromiseToPay;
	private DateField dfEndPeriodPromiseToPay;
	private SimplePagedTable<ContractCollectionHistory> tableHistory;
	private NavigationPanel navigationPanel;
	
	private Contract contract;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		navigationPanel = new NavigationPanel();
		//navigationPanel.addAddClickListener(this);
		
		tableHistory = new SimplePagedTable<ContractCollectionHistory>(createColumnDefinitions());
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(tableHistory);
		verticalLayout.addComponent(tableHistory.createControls());
		
		return verticalLayout;
	}
	
	/**
	 * ColumnDefinitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		
		columnDefinitions.add(new ColumnDefinition("date", I18N.message("date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("num.overdue.in.days", I18N.message("num.overdue.in.days"), Integer.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition("collection.status", I18N.message("collection.status"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition("customer.attribute", I18N.message("customer.attribute"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("collection.task", I18N.message("collection.task"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("collection.officer", I18N.message("collection.officer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("amount.promise.to.pay", I18N.message("amount.promise.to.pay"), String.class, Align.RIGHT, 130));
		columnDefinitions.add(new ColumnDefinition("start.period.promise.to.pay", I18N.message("start.period.promise.to.pay"), Date.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("end.period.promise.to.pay", I18N.message("end.period.promise.to.pay"), Date.class, Align.LEFT, 120));
		
		return columnDefinitions;
	}
	
	/** */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer() {
		Indexed indexedContainer = tableHistory.getContainerDataSource();
		indexedContainer.removeAllItems();
		// TODO PYI
//		if (contract.getContractCollectionHistories() == null || contract.getContractCollectionHistories().isEmpty()) {
//			return;
//		}
//		
//		Collections.sort(contract.getContractCollectionHistories(),	new Comparator<CollectionHistory>() {
//
//			private static final int REVERSE = -1;
//			/**
//			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
//			 */
//			@Override
//			public int compare(CollectionHistory o1, CollectionHistory o2) {
//				if (o1 == null || o2 == null) {
//					return 0;
//				}
//				return o1.getCreateDate().compareTo(o2.getCreateDate()) * REVERSE;
//			}
//			
//		});
//		
//		for (CollectionHistory history : contract.getContractCollectionHistories()) {
//			Item item = indexedContainer.addItem(history.getId());
//			item.getItemProperty("date").setValue(history.getCreateDate());
//			item.getItemProperty("num.overdue.in.days").setValue(history.getNbOverdueInDays());
//			item.getItemProperty("collection.status").setValue(history.getCollectionStatus() != null ? history.getCollectionStatus().getDescEn() : "");
//			item.getItemProperty("customer.attribute").setValue(history.getCustomerAttribute() != null ? history.getCustomerAttribute().getDescEn() : "");
//			item.getItemProperty("collection.task").setValue(history.getCollectionTask() != null ? history.getCollectionTask().getDescEn() : "");
//			item.getItemProperty("collection.officer").setValue(history.getAssignee() != null ? history.getAssignee().getDesc() : "");
//			item.getItemProperty("amount.promise.to.pay").setValue(AmountUtils.format(history.getAmountPromiseToPayUsd()));
//			item.getItemProperty("start.period.promise.to.pay").setValue(history.getStartPeriodPromiseToPay());
//			item.getItemProperty("end.period.promise.to.pay").setValue(history.getEndPeriodPromiseToPay());
//		}
		tableHistory.refreshContainerDataSource();
	}
	
	/**
	 * @param contract
	 */
	public void assignValues (Contract contract) {
		this.contract = contract;
		setIndexedContainer();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent paramClickEvent) {
		final Window window = new Window();
		window.setModal(true);
		window.setClosable(false);
		window.setResizable(false);
		window.setWidth(550, Unit.PIXELS);
		window.setHeight(300, Unit.PIXELS);
		window.setCaption(I18N.message("collection.history"));
		
		cbxCollecitonStatus = new ERefDataComboBox<EWkfStatus>(I18N.message("collection.status"), EWkfStatus.class);
		
		cbxcustomerAttribute = new EntityRefComboBox<ColCustField>(I18N.message("customer.attributes"));
		cbxcustomerAttribute.setRestrictions(new BaseRestrictions<ColCustField>(ColCustField.class));
		cbxcustomerAttribute.renderer();
		
		txtAmountPromiseToPay = ComponentFactory.getNumberField("amount.promise.to.pay", false, 50, 150);
		dfStartPeriodPromiseToPay = ComponentFactory.getAutoDateField("start.period.promise.to.pay", false);
		dfEndPeriodPromiseToPay = ComponentFactory.getAutoDateField("end.period.promise.to.pay", false);
		
		dfStartPeriodPromiseToPay.setValue(null);
		dfEndPeriodPromiseToPay.setValue(null);
		
		Button btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		btnSave.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = 8902578830364522457L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (contract == null) {
					return;
				}
				
				// Add new ContractCollectionHistory and update Contract Status
				ContractCollectionHistory history = new ContractCollectionHistory();
				// TODO PYI
//				history.setContract(contract);
//				
//				if (cbxCollecitonStatus.getSelectedEntity() != null) {
//					history.setCollectionStatus(cbxCollecitonStatus.getSelectedEntity());
//					contract.setCollectionStatus(cbxCollecitonStatus.getSelectedEntity());
//				}
				if (StringUtils.isNotEmpty(txtAmountPromiseToPay.getValue())) {
					history.setAmountPromiseToPayUsd(Double.valueOf(txtAmountPromiseToPay.getValue()));
				}
				if (dfStartPeriodPromiseToPay.getValue() != null) {
					history.setStartPeriodPromiseToPay(dfStartPeriodPromiseToPay.getValue());
				}
				if (dfEndPeriodPromiseToPay.getValue() != null) {
					history.setEndPeriodPromiseToPay(dfEndPeriodPromiseToPay.getValue());
				}
				if (contract.getCollection() != null) {
					Collection otherData = contract.getCollection();
					history.setNbOverdueInDays(otherData.getNbOverdueInDays());
				}
				// TODO PYI
//				if(cbxcustomerAttribute.getSelectedEntity() != null){
//					history.setCustomerAttribute(cbxcustomerAttribute.getSelectedEntity());
//				}
//				if (contract.getCollectionTask() != null) {
//					history.setCollectionTask(contract.getCollectionTask());
//				}
//				if (contract.getGroup() != null) {
//					history.setGroup(contract.getGroup());
//				}
//			
//				SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//				history.setAssignee(secUser);
//				entityService.saveOrUpdate(history);
//				
//				if (contract.getContractCollectionHistories() == null) {
//					contract.setContractCollectionHistories(new ArrayList<CollectionHistory>());
//				}
//				
//				contract.getContractCollectionHistories().add(history);
//				entityService.saveOrUpdate(contract);
				setIndexedContainer();
				window.close();
			}
		});
		Button btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		btnCancel.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = 8902578830364522457L;
			@Override
			public void buttonClick(ClickEvent event) {
				window.close();
			}
		});
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.addComponent(cbxCollecitonStatus);
		formLayout.addComponent(cbxcustomerAttribute);
		formLayout.addComponent(txtAmountPromiseToPay);
		formLayout.addComponent(dfStartPeriodPromiseToPay);
		formLayout.addComponent(dfEndPeriodPromiseToPay);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(formLayout);
		
		window.setContent(verticalLayout);
		UI.getCurrent().addWindow(window);
	}

}

package com.nokor.efinance.gui.ui.panel.contract.tranfer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.contract.model.ContractSimulationApplicant;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.payment.locksplit.LockSplitFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * 
 * @author buntha.chea
 *
 */
public class ContractTranferTabPanel extends AbstractTabPanel implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9180948951290361987L;
	
	private AutoDateField dfTransfer;
	private Button btnRequest;
	
	private Contract contract;
	
	private SimplePagedTable<Entity> pageTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private Button btnOpen;
	private Button btnCancel;
	
	private Panel tranferPanel;
	private HorizontalLayout tranferLayout;
	
	private LockSplitFormPanel formPanel;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		dfTransfer = ComponentFactory.getAutoDateField();
		dfTransfer.setValue(DateUtils.today());
		btnRequest = ComponentLayoutFactory.getButtonStyle("request", null, 80, "btn btn-success button-small");
		btnRequest.addClickListener(this);
		
		this.columnDefinitions = createColumnDefinition();
		pageTable = new SimplePagedTable<>(columnDefinitions);
		pageTable.setCaption(I18N.message("new.customer"));
		
		Label lblTranfer = ComponentFactory.getLabel("transfer.date");
		
		btnOpen = ComponentLayoutFactory.getButtonStyle("open", null, 100, "btn btn-success button-small");
		btnOpen.addClickListener(this);
		btnCancel = ComponentLayoutFactory.getButtonCancel();
		btnCancel.setWidth("80px");
		btnCancel.addClickListener(this);
		
		tranferLayout = new HorizontalLayout();
		tranferLayout.setSpacing(true);
		tranferLayout.addComponent(lblTranfer);
		tranferLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		tranferLayout.addComponent(dfTransfer);
		tranferLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		tranferLayout.addComponent(btnRequest);
		tranferLayout.addComponent(btnCancel);
		
		tranferLayout.setComponentAlignment(lblTranfer, Alignment.MIDDLE_CENTER);
		
		tranferPanel = new Panel();
		tranferPanel.setStyleName(Reindeer.PANEL_LIGHT);
		tranferPanel.setContent(btnOpen);
		
		formPanel = new LockSplitFormPanel();
		formPanel.setVisibleToLocksplitTable(false);
		Panel lockSplitPanel = ComponentLayoutFactory.getPanel(formPanel, true, false);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(tranferPanel);
		mainLayout.addComponent(lockSplitPanel);
		mainLayout.addComponent(pageTable);
		mainLayout.addComponent(pageTable.createControls());
		
		pageTable.setPageLength(5);
		return mainLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition("customer", I18N.message("customer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("guarantor", I18N.message("guarantor"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition("status", I18N.message("status"), String.class, Align.LEFT, 130));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param schedules
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<ContractSimulation> contractSimulations) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		
		for (ContractSimulation contractSimulation : contractSimulations) {
			Item item = indexedContainer.addItem(contractSimulation.getId());
			Applicant applicant = contractSimulation.getApplicant();
			
			if (applicant != null) {
				item.getItemProperty("customer").setValue(applicant.getNameEn());
			}
			item.getItemProperty("status").setValue(contractSimulation.getWkfStatus().getDescEn());
			
			List<ContractSimulationApplicant> contractSimulationApplicants = contractSimulation.getContractSimulationApplicants();
			item.getItemProperty("guarantor").setValue(getGuarantorNames(contractSimulationApplicants));
		}		
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private List<ContractSimulation> getContractSimulation(Contract contract) {
		BaseRestrictions<ContractSimulation> restrictions = new BaseRestrictions<>(ContractSimulation.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		return ENTITY_SRV.list(restrictions);
	}
	
	
	/**
	 * Assign Value
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		BaseRestrictions<LockSplit> restrictions = new BaseRestrictions<>(LockSplit.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addOrder(Order.desc("createDate"));
		List<LockSplit> lockSplits = ENTITY_SRV.list(restrictions);
		if (lockSplits != null && !lockSplits.isEmpty()) {
			formPanel.assignValues(lockSplits.get(0).getId());
		} else {
			formPanel.setContract(contract);
			formPanel.assignValues(null);
		}
		pageTable.setContainerDataSource(getIndexedContainer(getContractSimulation(contract)));
	}
	
	/**
	 * do requestTranfer after click button request
	 */
	private void requestTranfer() {
		try {
			List<Cashflow> cashflows = getCashFlowsUnpaid();
			if (cashflows != null && !cashflows.isEmpty()) {
				
				double totalTiCashflowsAmount = 0d;
				double totalVatCashflowsAmout = 0d;
				
				List<LockSplitItem> lockSplitItems = new ArrayList<LockSplitItem>();
				for (Cashflow cashflow : cashflows) {
					totalTiCashflowsAmount += cashflow.getTiInstallmentAmount();
					totalVatCashflowsAmout += cashflow.getVatInstallmentAmount();
					
					LockSplitItem lockSplitItem = LockSplitItem.createInstance();
					lockSplitItem.setJournalEvent(cashflow.getJournalEvent());
					lockSplitItem.setTiAmount(cashflow.getTiInstallmentAmount());
					lockSplitItem.setVatAmount(cashflow.getVatInstallmentAmount());
					lockSplitItems.add(lockSplitItem);
				}
				
				Date lockSplitFrom = getDateLastWeek(DateUtils.getDateAtBeginningOfDay(dfTransfer.getValue()));
				Date lockSplitTo = DateUtils.getDateAtEndOfDay(dfTransfer.getValue());
				
				LockSplit lockSplit = LockSplit.createInstance();
				lockSplit.setContract(contract);
				lockSplit.setFrom(lockSplitFrom);
				lockSplit.setTo(lockSplitTo);
				lockSplit.setTotalAmount(totalTiCashflowsAmount);
				lockSplit.setTotalVatAmount(totalVatCashflowsAmout);
				ENTITY_SRV.saveOrUpdate(lockSplit);
				
				for (LockSplitItem lockSplitItem : lockSplitItems) {
					lockSplitItem.setLockSplit(lockSplit);
					ENTITY_SRV.saveOrUpdate(lockSplitItem);
				}
				Notification.show("", I18N.message("msg.info.tranfer.successfully"), Type.HUMANIZED_MESSAGE);
			} else {
				Notification.show("", I18N.message("no.cashflow.for.tranfer"), Type.HUMANIZED_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Notification.show("", ex.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	
	/**
	 * get list cashflows that unpaid
	 * @return
	 */
	private List<Cashflow> getCashFlowsUnpaid() {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addCriterion(Restrictions.eq("paid", Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq("cancel", Boolean.FALSE));
		restrictions.addCriterion(Restrictions.le("installmentDate", DateUtils.getDateAtEndOfDay(dfTransfer.getValue())));
		
		List<Cashflow> cashflows = ENTITY_SRV.list(restrictions);
		return cashflows;
	}
	
	/**
	 * get last week date
	 * @param date
	 * @return
	 */
	private Date getDateLastWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -7);
		return cal.getTime();
	}
	
	/**
	 * 
	 * @param contractSimulationApplicants
	 * @return
	 */
	private String getGuarantorNames(List<ContractSimulationApplicant> contractSimulationApplicants) {
		StringBuffer stringBuffer = new StringBuffer();
		int index = 1;
		if (contractSimulationApplicants != null && !contractSimulationApplicants.isEmpty()) {
			for (ContractSimulationApplicant contractSimulationApplicant : contractSimulationApplicants) {
				if (contractSimulationApplicant.getApplicant() != null && EApplicantType.G.equals(contractSimulationApplicant.getApplicantType())) {
					if (index == contractSimulationApplicants.size()) {
						stringBuffer.append(contractSimulationApplicant.getApplicant().getNameEn());
					} else {
						stringBuffer.append(contractSimulationApplicant.getApplicant().getNameEn());
						stringBuffer.append(" | ");
					}
				}
				index++;
			}
		}

		return stringBuffer.toString();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnRequest) {			
			RequestTranferPopupPanel.show(contract, new RequestTranferPopupPanel.Listener() {
				
				/** */
				private static final long serialVersionUID = -7780964314725197926L;

				/**
				 * @see com.nokor.efinance.gui.ui.panel.contract.tranfer.RequestTranferPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.contract.tranfer.RequestTranferPopupPanel)
				 */
				@Override
				public void onClose(RequestTranferPopupPanel dialog) {
					requestTranfer();
				}
			});
		} else if (event.getButton() == btnOpen) {
			tranferPanel.setContent(tranferLayout);
		} else if (event.getButton() == btnCancel) {
			tranferPanel.setContent(btnOpen);
		}
	}

}

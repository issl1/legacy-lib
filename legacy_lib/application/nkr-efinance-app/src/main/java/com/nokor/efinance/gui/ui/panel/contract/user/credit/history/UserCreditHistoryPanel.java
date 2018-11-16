package com.nokor.efinance.gui.ui.panel.contract.user.credit.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * User Credit History Panel
 * @author bunlong.taing
 */
public class UserCreditHistoryPanel extends AbstractTabPanel implements FinServicesHelper {
	/** */
	private static final long serialVersionUID = 2835011505134748631L;
	
	private SimpleTable<Contract> applicationTable;
	private SimpleTable<Contract> contractTable;
	private SimpleTable<Contract> guaranteeTable;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		applicationTable = new SimpleTable<>(I18N.message("applications"), createApplicantColumnDefinition());
		contractTable = new SimpleTable<>(I18N.message("contracts"), createContractColumnDefinition());
		guaranteeTable = new SimpleTable<>(I18N.message("guarantees"), createContractColumnDefinition());
		
		applicationTable.setPageLength(5);
		contractTable.setPageLength(5);
		guaranteeTable.setPageLength(5);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		
		content.addComponent(applicationTable);
		content.addComponent(contractTable);
		content.addComponent(guaranteeTable);
		
		return content;
	}
	
	/**
	 * Create Applicant Column Definition
	 * @return
	 */
	private List<ColumnDefinition> createApplicantColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(Contract.ID, I18N.message("id"), Long.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Contract.EXTERNALREFERENCE, I18N.message("application.id"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(Contract.STARTDATE, I18N.message("application.date"), Date.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(MContract.WKFSTATUS, I18N.message("status"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * Create Contract Column Definition
	 * @return
	 */
	private List<ColumnDefinition> createContractColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(Contract.ID, I18N.message("id"), Long.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Contract.REFERENCE, I18N.message("contract.id"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(Contract.EXTERNALREFERENCE, I18N.message("application.id"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(Contract.STARTDATE, I18N.message("contract.date"), Date.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(Contract.LOANAMOUNT, I18N.message("loan.amount"), Amount.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition(Contract.TIREALOUSTANDINGUSD, I18N.message("outstanding"), Amount.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition(Contract.DPD, I18N.message("dpd"), Integer.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * @param contracts
	 */
	@SuppressWarnings("unchecked")
	private void setApplicantTableContainerDataSource(List<Contract> contracts) {
		Container container = applicationTable.getContainerDataSource();
		for (Contract contract : contracts) {
			if (ContractUtils.isBeforeActive(contract)) {
				EWkfStatus status = contract.getWkfStatus();
				Item item = container.addItem(contract.getId());
				item.getItemProperty(Contract.ID).setValue(contract.getId());
				item.getItemProperty(Contract.EXTERNALREFERENCE).setValue(contract.getExternalReference());
				item.getItemProperty(Contract.STARTDATE).setValue(contract.getQuotationDate());
				item.getItemProperty(MContract.WKFSTATUS).setValue(status != null ? status.getDescLocale() : "");
			}
		}
	}
	
	/**
	 * @param contracts
	 */
	private void setContractTableContainerDataSource(List<Contract> contracts) {
		Container container = contractTable.getContainerDataSource();
		container.removeAllItems();
		if (contracts != null && !contracts.isEmpty()) {
			for (Contract contract : contracts) {
				if (!ContractUtils.isBeforeActive(contract)) {
					Item item = container.addItem(contract.getId());
					addContractItem(item, contract);
				}
			}
		}
	}
	
	/**
	 * Set Guarantee Table Container Data Source
	 * @param contractApplicants
	 */
	private void setGuaranteeTableContainerDataSource(List<Contract> contracts) {
		Container container = guaranteeTable.getContainerDataSource();
		container.removeAllItems();
		if (contracts != null && !contracts.isEmpty()) {
			for (Contract contract : contracts) {
				if (!ContractUtils.isBeforeActive(contract)) {
					Item item = container.addItem(contract.getId());
					addContractItem(item, contract);
				}
			}
		}
	}
	
	/**
	 * @param item
	 * @param contract
	 * @param individual
	 */
	@SuppressWarnings("unchecked")
	private void addContractItem(Item item, Contract contract) {
		item.getItemProperty(Contract.ID).setValue(contract.getId());
		item.getItemProperty(Contract.REFERENCE).setValue(contract.getReference());
		item.getItemProperty(Contract.EXTERNALREFERENCE).setValue(contract.getExternalReference());
		item.getItemProperty(Contract.STARTDATE).setValue(contract.getStartDate());
		item.getItemProperty(Contract.LOANAMOUNT).setValue(contract.getLoanAmount());
		
		Amount amount = CONT_SRV.getRealOutstanding(DateUtils.today(), contract.getId());
		item.getItemProperty(Contract.TIREALOUSTANDINGUSD).setValue(amount);
		
		Collection collection = contract.getCollection();
		if (collection != null) {
			item.getItemProperty(Contract.DPD).setValue(collection.getNbOverdueInDays());
		}
	}
	
	/**
	 * @param individual
	 */
	public void assignValues(Applicant applicant) {
		reset();
		if (applicant != null) {
			List<Contract> contracts = APP_SRV.getContractsByApplicant(applicant);
			setApplicantTableContainerDataSource(contracts);
			setContractTableContainerDataSource(contracts);
			List<Contract> contractsGuarantor = APP_SRV.getContractsGuarantorByApplicant(applicant);
			setGuaranteeTableContainerDataSource(contractsGuarantor);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		applicationTable.removeAllItems();
		contractTable.removeAllItems();
		guaranteeTable.removeAllItems();
	}

}

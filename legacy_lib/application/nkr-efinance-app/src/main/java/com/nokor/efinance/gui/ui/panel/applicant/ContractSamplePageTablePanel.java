package com.nokor.efinance.gui.ui.panel.applicant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.gui.ui.panel.contract.ContractsPanel;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * Addresses panel
 * @author sok.vina
 */
public class ContractSamplePageTablePanel extends AbstractTabPanel implements AddClickListener, FMEntityField {
	
	private static final long serialVersionUID = 2202264472024719484L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Address> pagedTable;
	/**
	 */
	public ContractSamplePageTablePanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Address>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					Long itemId = (Long) event.getItemId();
					Page.getCurrent().setUriFragment("!" + ContractsPanel.NAME + "/" + itemId);
				}
			}
		});
				
		contentLayout.addComponent(pagedTable);
		contentLayout.setSpacing(true);
		contentLayout.addComponent(pagedTable.createControls());
        return contentLayout;
				
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<ContractApplicant> contractApplicantList) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {				
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (contractApplicantList != null && !contractApplicantList.isEmpty()) {	
			for (ContractApplicant contractApplicant : contractApplicantList) {
				Contract contract = contractApplicant.getContract();
				Item item = indexedContainer.addItem(contract.getId());
				
				item.getItemProperty(ID).setValue(contract.getId());
				item.getItemProperty(REFERENCE).setValue(contract.getReference());
				item.getItemProperty("applicant." + LAST_NAME_EN).setValue(contract.getApplicant().getIndividual().getLastNameEn());
				item.getItemProperty("applicant." + FIRST_NAME_EN).setValue(contract.getApplicant().getIndividual().getFirstNameEn());
				item.getItemProperty("asset." + DESC_EN).setValue(contract.getAsset().getDescEn());
				item.getItemProperty("financialProduct." + DESC_EN).setValue(contract.getFinancialProduct().getDescEn());
				item.getItemProperty("dealer." + NAME_EN).setValue(contract.getDealer().getNameEn());
				item.getItemProperty(START_DATE).setValue(contract.getStartDate());
			}
		}
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("applicant." + LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("applicant." + FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("asset." + DESC_EN, I18N.message("asset"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("financialProduct." + DESC_EN, I18N.message("financial.product"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("dealer." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * @param contractApplicantList
	 */
	public void assignValues(List<ContractApplicant> contractApplicantList) {
		if (contractApplicantList.size() > 0) {
			pagedTable.setContainerDataSource(getIndexedContainer(contractApplicantList));
		} else {
			pagedTable.removeAllItems();
		}
	}

	@Override
	public void addButtonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
	}


}

package com.nokor.efinance.gui.ui.panel.dashboard.summaries;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.service.ContractUserInboxRestriction;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ContractSummariedTablePanel extends AbstractControlPanel implements FMEntityField {

	private static final long serialVersionUID = 2009365627691029853L;
	
	private SimplePagedTable<Contract> detailPageTable;
	private SimpleTable<Entity> summaryTable;
	private List<ColumnDefinition> columnDefinitions;
	private Panel mainPanel;
	private VerticalLayout mainLayout;
	private List<Contract> contracts;
	
	/**
	 * 
	 */
	public ContractSummariedTablePanel() {
		this.columnDefinitions = createDetailColumnDefinition();
		detailPageTable = new SimplePagedTable<>(this.columnDefinitions);
		
		summaryTable = new SimpleTable<>(createSummaryColumnDefinition());
		summaryTable.setSizeUndefined();
		
		mainLayout = new VerticalLayout();
		mainLayout.addComponent(detailPageTable);
		mainLayout.addComponent(detailPageTable.createControls());
		
		mainPanel = ComponentLayoutFactory.getPanel(mainLayout, true, true);
		
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 */
	public void isDetail() {
		assignValue(contracts);
		mainPanel.setContent(mainLayout);
	}
	/**
	 * 
	 */
	public void isSummary() {
		assignValue(contracts);
		mainPanel.setContent(summaryTable);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ColumnDefinition> createDetailColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60, false));
		columnDefinitions.add(new ColumnDefinition(SEC_USER, I18N.message("staff"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(APPLICANT + "." + NAME_EN, I18N.message("name"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(EXTERNAL_REFERENCE, I18N.message("application.id"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(CREATE_DATE, I18N.message("application.date"), Date.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(ASSET + "." + DESC_EN, I18N.message("asset"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(FINANCIAL_PRODUCT + "." + DESC_EN, I18N.message("product"), String.class, Align.LEFT, 125));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ColumnDefinition> createSummaryColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(SEC_USER, I18N.message("staff"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("total", I18N.message("total"), Integer.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	@SuppressWarnings("unchecked")
	public IndexedContainer getIndexedContainer(List<Contract> contracts) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (Contract contract : contracts) {
			String secUserDesc = contract.getUpdateUser();
			Item item = indexedContainer.addItem(contract.getId());
			item.getItemProperty(ID).setValue(contract.getId());
			if (contract.getApplicant() != null) {
				item.getItemProperty(APPLICANT + "." + NAME_EN).setValue(contract.getApplicant().getNameLocale());
			}
		
			item.getItemProperty(EXTERNAL_REFERENCE).setValue(contract.getExternalReference());
			item.getItemProperty(CREATE_DATE).setValue(contract.getCreateDate());
			if (contract.getDealer() != null) {
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer().getNameLocale());
			}
			if (contract.getAsset() != null) {
				item.getItemProperty(ASSET + "." + DESC_EN).setValue(contract.getAsset().getDescEn());
			}
			if (contract.getFinancialProduct() != null) {
				item.getItemProperty(FINANCIAL_PRODUCT + "." + DESC_EN).setValue(contract.getFinancialProduct().getDescEn());
			}
			item.getItemProperty(SEC_USER).setValue(secUserDesc);
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setSummaryTableIndexedContainer(List<Contract> contracts) {
		summaryTable.removeAllItems();
		Container indexedContainer = summaryTable.getContainerDataSource();
		if (contracts != null && !contracts.isEmpty()) {
			HashMap<String, List<Integer>> map = new HashMap<>();
			List<String> secUsers = new ArrayList<>();
			for (Contract contract : contracts) {
				String secUserDesc = contract.getUpdateUser();
				if (secUsers.contains(secUserDesc)) {
					map.get(secUserDesc).add(1);
				} else {
					map.put(secUserDesc, new ArrayList<>());
					map.get(secUserDesc).add(1);
					secUsers.add(secUserDesc);
				}
			}
			
			int index = 0;
			for (Entry<String, List<Integer>> ee : map.entrySet()) {
			    String secUser = ee.getKey();
			    List<Integer> total = ee.getValue();
			    Item item = indexedContainer.addItem(index++);
			    item.getItemProperty(SEC_USER).setValue(secUser);
				item.getItemProperty("total").setValue(total.size());
			}
		}
	}
	
	/**
	 * get userbooking
	 * @param conId
	 * @return
	 */
	private String getUser(Long conId) {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.setConId(conId);
		//restrictions.addCriterion(Restrictions.isNotNull("secUser"));
		restrictions.addOrder(Order.desc("updateDate"));
		List<ContractUserInbox> contractUserInboxs = ENTITY_SRV.list(restrictions);
		if (!contractUserInboxs.isEmpty()) {
			return contractUserInboxs.get(0).getUpdateUser();
		}
		return null;
	}
	
	/**
	 * assignValue
	 * @param contract
	 */
	public void assignValue(List<Contract> contracts) {
		this.contracts = contracts;
		setSummaryTableIndexedContainer(contracts);
		detailPageTable.setContainerDataSource(getIndexedContainer(contracts));
	}
}

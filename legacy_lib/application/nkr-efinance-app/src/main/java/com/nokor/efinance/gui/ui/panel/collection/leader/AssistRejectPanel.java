package com.nokor.efinance.gui.ui.panel.collection.leader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author buntha.chea
 *
 */
public class AssistRejectPanel extends VerticalLayout implements FMEntityField, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -4013133659927562272L;
			
	private SimplePagedTable<Contract> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	
	public AssistRejectPanel() {
		setCaption(I18N.message("assist.reject"));
		setSizeFull();
		setHeight("100%");
		setMargin(true);
		setSpacing(true);
		
		columnDefinitions = createColumnDefinitions();
	    pagedTable = new SimplePagedTable<Contract>(columnDefinitions);
	    pagedTable.setContainerDataSource(getIndexedContainer());
	    
	    addComponent(pagedTable);
	    addComponent(pagedTable.createControls());
	}
	
	/**
	 * 
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("applicant." + NAME_EN, I18N.message("name.en"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("asset." + DESC_EN, I18N.message("asset"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("financialProduct." + DESC_EN, I18N.message("financial.product"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("dealer." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(WKF_STATUS + "." + DESC_EN, I18N.message("contract.status"), String.class, Align.LEFT, 150));
		
		return columnDefinitions;
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer() {
		IndexedContainer indexedContainer = new IndexedContainer();
					
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		int index = 0;
		List<Contract> contracts = COL_SRV.getRejectAssistContracts();
		for (Contract contract : contracts) {			
			Item item = indexedContainer.addItem(index);
			item.getItemProperty(ID).setValue(contract.getId());
			item.getItemProperty(REFERENCE).setValue(contract.getReference());
			item.getItemProperty("applicant." + NAME_EN).setValue(contract.getApplicant().getNameEn());
			
			if (contract.getAsset() != null) {
				item.getItemProperty("asset." + DESC_EN).setValue(contract.getAsset().getDescEn());
			}
			
			if (contract.getFinancialProduct() != null) {
				item.getItemProperty("financialProduct." + DESC_EN).setValue(contract.getFinancialProduct().getDescEn());
			}
			
			if (contract.getDealer() != null) {
				item.getItemProperty("dealer." + NAME_EN).setValue(contract.getDealer().getDescEn());
			}
			
			item.getItemProperty(START_DATE).setValue(contract.getStartDate());
			
			if (contract.getWkfStatus() != null) {
				item.getItemProperty(WKF_STATUS + "." + DESC_EN).setValue(contract.getWkfStatus().getDescEn());
			}
			
			index++;
		}

		return indexedContainer;
	}
	
	/**
	 * 
	 */
	public void refresh() {
		pagedTable.setContainerDataSource(getIndexedContainer());
	}
}

package com.nokor.efinance.ra.ui.panel.dealer;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author sok.vina
 *
 */
public class DealerBranchesPanel extends AbstractTabPanel implements FinServicesHelper, FMEntityField {
	
	private static final long serialVersionUID = -7538697565112892189L;
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<Dealer> pagedTable;
	
	/**
	 */
	public DealerBranchesPanel() {
		super();
		setSizeFull();
		setMargin(true);
		setSpacing(true);			
	}
	
	@Override
	protected Component createForm() {
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<>(this.columnDefinitions);
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		return contentLayout;
	}
	
	/**
	 * @param dealerId
	 */
	public void assignValues(Dealer dealer) {
		if (dealer != null) {
			pagedTable.setContainerDataSource(getIndexedContainer(DEA_SRV.getBranches(dealer)));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<Dealer> dealers) {
		IndexedContainer indexedContainer = new IndexedContainer();
			
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (Dealer dealer : dealers) {
			Item item = indexedContainer.addItem(dealer.getId());
			item.getItemProperty(ID).setValue(dealer.getId());
			item.getItemProperty(INTERNAL_CODE).setValue(dealer.getCode());
			item.getItemProperty(NAME_EN).setValue(dealer.getNameEn());
			item.getItemProperty(LICENCE_NO).setValue(dealer.getLicenceNo());
		}
		
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(INTERNAL_CODE, I18N.message("dealershop.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(NAME_EN, I18N.message("name"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(LICENCE_NO, I18N.message("commercial.no"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	
}

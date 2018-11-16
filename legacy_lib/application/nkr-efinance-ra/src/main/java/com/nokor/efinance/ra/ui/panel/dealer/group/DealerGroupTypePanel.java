package com.nokor.efinance.ra.ui.panel.dealer.group;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerGroup;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author buntha.chea
 *
 */
public class DealerGroupTypePanel extends AbstractTabPanel implements FinServicesHelper, FMEntityField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8285023575573019083L;
	
	private EDealerType dealerType;
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<Dealer> pagedTable;
	
	public DealerGroupTypePanel(EDealerType dealerType) {
		super();
		this.dealerType = dealerType;
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
	public void assignValues(Long groupId) {
		if (groupId != null) {
			DealerGroup group = DEA_SRV.getById(DealerGroup.class, groupId);
			pagedTable.setContainerDataSource(getIndexedContainer(DEA_SRV.getGroupType(group, dealerType)));
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
			item.getItemProperty(DEALER).setValue(dealer.getNameEn());
		}
		
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(DEALER, I18N.message(DEALER), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	

}

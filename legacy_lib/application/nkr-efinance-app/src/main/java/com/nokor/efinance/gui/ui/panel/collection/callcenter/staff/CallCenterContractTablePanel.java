package com.nokor.efinance.gui.ui.panel.collection.callcenter.staff;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * Contract detail table in call center staff
 * @author uhout.cheng
 */
public class CallCenterContractTablePanel extends VerticalLayout implements FinServicesHelper, ItemClickListener, SelectedItem, MContract {

	/** */
	private static final long serialVersionUID = 4608214969909008629L;
	
	private static final String ASSIGNMENTDATE = "assignment.date";
	private static final String DAYSSINCEASSIGNMENT = "days.since.assignment";
	
	private SimplePagedTable<Entity> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private Item selectedItem;
	
	private CallCenterStaffPanel callCenterStaffPanel;
	
	/**
	 * 
	 * @param callCenterStaffPanel
	 */
	public CallCenterContractTablePanel(CallCenterStaffPanel callCenterStaffPanel) {
		this.callCenterStaffPanel = callCenterStaffPanel;
		pagedTable = new SimplePagedTable<Entity>(getColumnDefinitions());
		pagedTable.setWidth(600, Unit.PIXELS);
		pagedTable.addItemClickListener(this);
		
		VerticalLayout tableLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		tableLayout.setWidth(600, Unit.PIXELS);
		tableLayout.addComponent(pagedTable);		
		tableLayout.addComponent(pagedTable.createControls());
		pagedTable.setPageLength(25);
		
		assignValues();
		
		setWidth(60, Unit.PERCENTAGE);
		addComponent(tableLayout);
	}
		
	/**
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("contract.id"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(STARTDATE, I18N.message("contract.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(Individual.FIRSTNAME, I18N.message("firstname"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Individual.LASTNAME, I18N.message("lastname"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(ASSIGNMENTDATE, I18N.message("assignment.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(DAYSSINCEASSIGNMENT, I18N.message("days.since.assignment"), Integer.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * @param contracts
	 * @param endDate
	 * @param isUnProcessed
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getIndexedContainer(List<Contract> contracts) {
		selectedItem = null;
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (contracts != null && !contracts.isEmpty()) {
			int index = 0;
			for (Contract contract : contracts) {
				Item item = indexedContainer.addItem(index);
				item.getItemProperty(REFERENCE).setValue(contract.getReference());
				item.getItemProperty(STARTDATE).setValue(contract.getStartDate());
				Applicant applicant = contract.getApplicant();
				if (applicant != null) {
					item.getItemProperty(Individual.FIRSTNAME).setValue(applicant.getFirstNameLocale());
					item.getItemProperty(Individual.LASTNAME).setValue(applicant.getLastNameLocale());
				}
				item.getItemProperty(ASSIGNMENTDATE).setValue(null);
				item.getItemProperty(DAYSSINCEASSIGNMENT).setValue(1);
				index++;
			}
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 */
	public void assignValues() {
		pagedTable.setContainerDataSource(getIndexedContainer(COL_SRV.getCollectionContractsByUser()));
	}
	
	/**
	 * @return
	 */
	public String getContractId() {
		if (this.selectedItem != null) {
			return ((String) this.selectedItem.getItemProperty(REFERENCE).getValue());
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}

	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
	}
	
}

package com.nokor.efinance.gui.ui.panel.inbox.collection.ar.cashier;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColCompansationTabPanel extends AbstractControlPanel implements ClickListener, SelectedTabChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5787762163538333585L;
	
	private static final String BIDDER_ID = "bidder.id";
	private static final String BIDDER_FULLNAME = "bidder.fullname";
	private static final String BALANCE = "balance";
	private static final String ACTIONS = "actions";
	
	private TextField txtBidderFullName;
	private TextField txtBidderId;
	
	private Button btnSearch;
	private Button btnReset;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Entity> simplePagedTable;
	
	private VerticalLayout mainLayout;
	
	private TabSheet tabSheet;
	private ColAuctionBidderDetailPanel bidderDetailPanel;
	
	public ColCompansationTabPanel() {
		setMargin(true);
		init();
		
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		bidderDetailPanel = new ColAuctionBidderDetailPanel();
		
		VerticalLayout tablePanel = ComponentLayoutFactory.getVerticalLayout(false, true);
		tablePanel.addComponent(simplePagedTable);
		tablePanel.addComponent(simplePagedTable.createControls());
		
		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.addComponent(createFilterPanel());
		mainLayout.addComponent(tablePanel);
		
		tabSheet.addTab(mainLayout, I18N.message("compansation"));
		
		addComponent(tabSheet);
	}
	
	/**
	 * 
	 */
	private void init() {
		txtBidderFullName = ComponentFactory.getTextField();
		txtBidderId = ComponentFactory.getTextField();
		
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnSearch.addClickListener(this);
		btnReset = ComponentLayoutFactory.getButtonReset();
		btnReset.addClickListener(this);
		
		this.columnDefinitions = createColumnDifinition();
		simplePagedTable = new SimplePagedTable<>(this.columnDefinitions);
		simplePagedTable.setContainerDataSource(getIndexedContainer());
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel createFilterPanel() {
		GridLayout filterGridPanel = ComponentLayoutFactory.getGridLayout(4, 1);
		filterGridPanel.setSpacing(true);
		 
		Label lblBidderFullName = ComponentLayoutFactory.getLabelCaption("bidder.fullname");
		Label lblBidderId = ComponentLayoutFactory.getLabelCaption("bidder.id");
		
		filterGridPanel.addComponent(lblBidderFullName, 0, 0);
		filterGridPanel.addComponent(txtBidderFullName, 1, 0);
		filterGridPanel.addComponent(lblBidderId, 2, 0);
		filterGridPanel.addComponent(txtBidderId, 3, 0);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		buttonLayout.addComponent(btnSearch);
		buttonLayout.addComponent(btnReset);
		
		VerticalLayout filterLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		filterLayout.addComponent(filterGridPanel);
		filterLayout.addComponent(buttonLayout);
		filterLayout.setComponentAlignment(filterGridPanel, Alignment.TOP_CENTER);
		filterLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("filters"));
		fieldSet.setContent(filterLayout);
		
		Panel filterPanel = new Panel();
		filterPanel.setStyleName(Reindeer.PANEL_LIGHT);
		filterPanel.setContent(fieldSet);
		
		return filterPanel;
	}
	
	/**
	 * AssignValue to table
	 */
	public void assignValue() {
		simplePagedTable.setContainerDataSource(getIndexedContainer());
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDifinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(BIDDER_ID, I18N.message("bidder.id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(BIDDER_FULLNAME, I18N.message("bidder.fullname"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(BALANCE, I18N.message("balance"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ACTIONS, I18N.message("actions"), Button.class, Align.LEFT, 100));
		
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contracts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getIndexedContainer() {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		Item item = indexedContainer.addItem(1);
		item.getItemProperty(BIDDER_ID).setValue(1l);
		item.getItemProperty(BIDDER_FULLNAME).setValue("test");
		item.getItemProperty(BALANCE).setValue(AmountUtils.format(0d));
		item.getItemProperty(ACTIONS).setValue(new ButtonRenderer());
		
		return indexedContainer;
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		txtBidderFullName.setValue("");
		txtBidderId.setValue("");
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSearch) {
			
		} else if (event.getButton() == btnReset) {
			reset();
		}
		
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class ButtonRenderer extends Button {
		
		private static final long serialVersionUID = 1054792636555766271L;

		public ButtonRenderer() {
			setStyleName(Reindeer.BUTTON_LINK);
			setIcon(FontAwesome.EDIT);
			addClickListener(new ClickListener() {
				private static final long serialVersionUID = 5421345974152699417L;

				@Override
				public void buttonClick(ClickEvent event) {
					tabSheet.addTab(bidderDetailPanel, I18N.message("detail"));
					tabSheet.setSelectedTab(bidderDetailPanel);
				}
			});
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSheet.getSelectedTab().equals(mainLayout)) {
			if (tabSheet.getTab(bidderDetailPanel) != null) {
				tabSheet.removeTab(tabSheet.getTab(bidderDetailPanel));
			}
		} 
	}
}

package com.nokor.efinance.gui.ui.panel.cartography;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 *
 */
public class CartographyReportsPanel extends AbstractTabPanel {
	
	private static final long serialVersionUID = -4618786633559261506L;	
	private SimplePagedTable<Dealer> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	
	public CartographyReportsPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Dealer>(this.columnDefinitions);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        return contentLayout;
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	public void setIndexedContainer(List<QuotationGroupByDealer> quotationCartographys) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		int id = 1;
		if (quotationCartographys != null & !quotationCartographys.isEmpty()) {
			for (QuotationGroupByDealer quotationCartography : quotationCartographys) {
				final Item item = indexedContainer.addItem(id);
				item.getItemProperty("dealer.name").setValue(quotationCartography.getDealer().getNameEn());	
				item.getItemProperty("approved").setValue(quotationCartography.getNumApproved());	
				item.getItemProperty("rejected").setValue(quotationCartography.getNumRejected());
				item.getItemProperty("declined").setValue(quotationCartography.getNumDeclined());
				item.getItemProperty("proposal").setValue(quotationCartography.getNumProposal());
				item.getItemProperty("total").setValue(quotationCartography.getNumTotal());
				id++;
			} 	
		}
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("dealer.name", I18N.message("dealer"), String.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("approved", I18N.message("approved"), Integer.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("rejected", I18N.message("rejected"), Integer.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("declined", I18N.message("declined"), Integer.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("proposal", I18N.message("proposal"), Integer.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("total", I18N.message("total"), Integer.class, Align.RIGHT, 100));
		return columnDefinitions;
	}
}

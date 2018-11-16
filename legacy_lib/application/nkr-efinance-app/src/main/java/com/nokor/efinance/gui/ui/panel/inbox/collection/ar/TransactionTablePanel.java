package com.nokor.efinance.gui.ui.panel.inbox.collection.ar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;

import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author buntha.chea
 *
 */
public class TransactionTablePanel extends AbstractControlPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EVENT_ID = "event.id";
	private static final String EVENT_DATE = "event.date";
	private static final String ITEM = "item";
	private static final String CONTRACT_ID = "contract.id";
	private static final String TOTAL = "total";
	private static final String PENDING = "pending";
	private static final String PAID = "paid";
	private static final String BALANCE = "balance";

	private SimplePagedTable<Entity> simplePagedTable;
	
	public TransactionTablePanel() {
		setMargin(true);
		setSpacing(true);
		
		simplePagedTable = new SimplePagedTable<>(createColumnDefinition());
		addComponent(simplePagedTable);
		addComponent(simplePagedTable.createControls());
		
		simplePagedTable.setPageLength(8);
	}
	
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(EVENT_ID, I18N.message("event.id"), Long.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(EVENT_DATE, I18N.message("event.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ITEM, I18N.message("item"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(CONTRACT_ID, I18N.message("contract.id"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(TOTAL, I18N.message("total"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PENDING, I18N.message("pending"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PAID, I18N.message("paid"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(BALANCE, I18N.message("balance"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}

}

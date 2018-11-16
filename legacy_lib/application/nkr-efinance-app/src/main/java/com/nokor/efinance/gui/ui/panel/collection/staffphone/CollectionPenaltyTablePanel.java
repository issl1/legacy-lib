package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Penalty table in collection right panel
 * @author uhout.cheng
 */
public class CollectionPenaltyTablePanel extends Panel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 2574363513466025736L;
	
	private static final String PENALTYTYPE = "penalty.type";
	private static final String AMOUNTOUTSTANDING = "amount.outstanding";
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public CollectionPenaltyTablePanel() {
		init();
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setWidth(528, Unit.PIXELS);
		simpleTable.setPageLength(4);
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = getSimpleTable(getColumnDefinitions());
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(new VerticalLayout(simpleTable));
		setContent(horLayout);
		setStyleName(Reindeer.PANEL_LIGHT);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(PENALTYTYPE, I18N.message("penalty.type"), String.class, Align.LEFT, 250));
		columnDefinitions.add(new ColumnDefinition(AMOUNTOUTSTANDING, I18N.message("amount.outstanding"), Amount.class, Align.LEFT, 250));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
	
	}

}

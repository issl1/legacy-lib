package com.nokor.efinance.gui.ui.panel.contract.asset.registration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;

/**
 * Registration operation table 
 * @author uhout.cheng
 */
public class RegistrationOperationTable extends Panel {

	/** */
	private static final long serialVersionUID = -912169266524670892L;
	
	private final static String OPERATION = "operation";
	private final static String CREATIONDATE = "creation.date";
	private final static String DEADLINE = "deadline";
	private final static String STATUS = "status";
	private final static String COST = "cost";
	private final static String PRICE = "price";
	private final static String REFERENCE = "lock.split.ref";
	
	private SimpleTable<Entity> simpleTable;

	/**
	 * 
	 */
	public RegistrationOperationTable() {
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		setCaption(I18N.message("operation"));
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(simpleTable);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(OPERATION, I18N.message("operation"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(CREATIONDATE, I18N.message("creation.date"), Date.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(DEADLINE, I18N.message("deadline"), Date.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(COST, I18N.message("cost"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(PRICE, I18N.message("price"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("lock.split.ref"), String.class, Align.LEFT, 130));
		return columnDefinitions;
	}
}

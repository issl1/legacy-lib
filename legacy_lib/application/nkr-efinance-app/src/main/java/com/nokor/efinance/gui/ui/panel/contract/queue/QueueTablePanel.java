package com.nokor.efinance.gui.ui.panel.contract.queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.contract.model.MQueue;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class QueueTablePanel extends Panel implements MQueue {

	/** */
	private static final long serialVersionUID = 8634100781148207753L;
	
	private SimpleTable<Entity> simpleTable;
	
	/** */
	public QueueTablePanel() {
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(5);
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(simpleTable);
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 120, false));
		columnDefinitions.add(new ColumnDefinition(TYPE, I18N.message("type"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("create.date"), Date.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(CLOSE_DATE, I18N.message("close.date"), Date.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(STAFF_IN_CHARGE, I18N.message("staff.in.charge"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(DEPARTMENT, I18N.message("department"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}

}

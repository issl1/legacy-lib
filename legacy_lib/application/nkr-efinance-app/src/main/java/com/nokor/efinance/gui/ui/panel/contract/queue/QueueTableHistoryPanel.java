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
public class QueueTableHistoryPanel extends Panel implements MQueue {

	/** **/
	private static final long serialVersionUID = -6384732146693409680L;
	
	private SimpleTable<Entity> simpleTable;
	
	/** */
	public QueueTableHistoryPanel() {
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(5);
		setCaption(I18N.message("history"));
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(simpleTable);
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 130, false));
		columnDefinitions.add(new ColumnDefinition(DATE, I18N.message("date"), Date.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(ACTION, I18N.message("action"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
}

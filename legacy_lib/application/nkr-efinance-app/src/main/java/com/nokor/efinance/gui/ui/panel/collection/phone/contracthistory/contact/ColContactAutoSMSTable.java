package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

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
public class ColContactAutoSMSTable extends Panel {

	/** */
	private static final long serialVersionUID = -1906807826209598966L;

	private SimpleTable<Entity> simpleTableAutoSMS;
	
	public ColContactAutoSMSTable() {
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		simpleTableAutoSMS = new SimpleTable<Entity>(getAutoSMSColumnDefinitions());
		simpleTableAutoSMS.setPageLength(5);
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(simpleTableAutoSMS);
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getAutoSMSColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 100, false));
		columnDefinitions.add(new ColumnDefinition("desc", I18N.message("desc"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("trigger", I18N.message("trigger"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("status", I18N.message("status"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("option", I18N.message("option"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
}

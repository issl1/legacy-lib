package com.nokor.efinance.gui.ui.panel.contract.document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.MEntityA;
import org.seuksa.frmk.model.entity.MEntityRefA;

import com.nokor.efinance.core.contract.model.MContractDocument;
import com.nokor.efinance.core.dealer.model.MDealer;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;

/**
 * Document table panel
 * @author uhout.cheng
 */
public class DocumentsTablePanel extends Panel {
	
	/** */
	private static final long serialVersionUID = -3416021138112053976L;
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public DocumentsTablePanel() {
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(5);
		setCaption(null);
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(simpleTable);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(MContractDocument.DOCUMENTTYPE, I18N.message("document.type"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(MEntityA.ID, I18N.message("id"), String.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(MDealer.NAME, I18N.message("name"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(MEntityRefA.DESC, I18N.message("desc"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(MEntityA.CREATEUSER, I18N.message("uploaded.by"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(MEntityA.CREATEDATE, I18N.message("uploaded.on"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(MEntityA.UPDATEDATE, I18N.message("last.update"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(MContractDocument.ACTIONS, I18N.message("actions"), HorizontalLayout.class, Align.LEFT, 80));
		return columnDefinitions;
	}
	
}

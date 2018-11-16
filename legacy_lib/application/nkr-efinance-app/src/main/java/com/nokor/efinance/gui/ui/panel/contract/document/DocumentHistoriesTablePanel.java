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
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;

/**
 * Document history table panel
 * @author uhout.cheng
 */
public class DocumentHistoriesTablePanel extends Panel {
	
	/** */
	private static final long serialVersionUID = -4642641804466435233L;
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public DocumentHistoriesTablePanel() {
		init();
	}
	
	/**
	 * 
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
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(MContractDocument.DOCUMENTTYPE, I18N.message("document.type"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(MEntityA.ID, I18N.message("id"), String.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(MDealer.NAME, I18N.message("name"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(MEntityRefA.DESC, I18N.message("desc"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(MEntityA.CREATEUSER, I18N.message("printed.by"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(MContractDocument.SENTON, I18N.message("sent.on"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(MContractDocument.ADDRESS, I18N.message("address"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(MContractDocument.DETAIL, I18N.message("detail"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(MContractDocument.RESULT, I18N.message("result"), String.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition(MContractDocument.RESULTDATE, I18N.message("result.date"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(MContractDocument.COMMENT, I18N.message("comment"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
}

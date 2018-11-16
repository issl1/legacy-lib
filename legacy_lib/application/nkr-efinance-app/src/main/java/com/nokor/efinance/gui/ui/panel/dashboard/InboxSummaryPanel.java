package com.nokor.efinance.gui.ui.panel.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * @author buntha.chea
 */
public class InboxSummaryPanel extends AbstractControlPanel {

	private static final long serialVersionUID = 8341162309347917428L;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Applicant> pagedTable;

	// private SimplePagedTable<Applicant> pagedTable1;

	// private SimplePagedTable<Applicant> pagedTable2;
	
	public InboxSummaryPanel() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<>(this.columnDefinitions);
		// pagedTable1 = new SimplePagedTable<>(createColumnDefinitions1());
		// pagedTable2 = new SimplePagedTable<>(createColumnDefinitions2());
		setIndexedContainer();
		setIndexedContainer1();
		setIndexedContainer2();
		addComponent(pagedTable);
		// addComponent(pagedTable1);
		// addComponent(pagedTable2);
	}
	
	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("date.book", I18N.message("date.book"), String.class, Align.LEFT, 411));
		columnDefinitions.add(new ColumnDefinition("to.process", I18N.message("to.process"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition("hold", I18N.message("hold"), String.class, Align.LEFT,170));
		columnDefinitions.add(new ColumnDefinition("total", I18N.message("total"), String.class, Align.LEFT, 170));
		return columnDefinitions;
	}
	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions1() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("date.book", I18N.message("date.book"), String.class, Align.LEFT, 411));
		columnDefinitions.add(new ColumnDefinition("to.process", I18N.message("to.process"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition("hold", I18N.message("hold"), String.class, Align.LEFT,170));
		columnDefinitions.add(new ColumnDefinition("total", I18N.message("total"), String.class, Align.LEFT, 170));
		return columnDefinitions;
	}
	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions2() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("date.received", I18N.message("date.received"), String.class, Align.LEFT, 411));
		columnDefinitions.add(new ColumnDefinition("to.process", I18N.message("to.process"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition("hold", I18N.message("hold"), String.class, Align.LEFT,170));
		columnDefinitions.add(new ColumnDefinition("total", I18N.message("total"), String.class, Align.LEFT, 170));
		return columnDefinitions;
	}
	/**
	 * Set Value into ColumnDefinitions
	 */
	@SuppressWarnings({ "unchecked" })
	private void setIndexedContainer() {
		
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		String[] dateBooks = {"02/03/2015 (Mon)","01/03/2015 (Sun)","28/02/2015(Sat)","27/02/2015 (Fri)",
								"26/02/2015 (Thu)","Last week","Current month","Current year"};
		
		for (int i=0 ; i < dateBooks.length; i++) {
			Item item = indexedContainer.addItem(i);
			item.getItemProperty("date.book").setValue(dateBooks[i]);
			item.getItemProperty("to.process").setValue("50");
			item.getItemProperty("hold").setValue("30");
			item.getItemProperty("total").setValue("100");
		}
		pagedTable.refreshContainerDataSource();
		
	}
	
	/**
	 * Set Value into ColumnDefinitions1
	 */
	private void setIndexedContainer1() {
		
		/*Indexed indexedContainer = pagedTable1.getContainerDataSource();
		indexedContainer.removeAllItems();
		String[] dateBooks = {"02/03/2015","01/03/2015","28/02/2015","Total"};
		
		for (int i=0 ; i < dateBooks.length; i++) {
			Item item = indexedContainer.addItem(i);
			item.getItemProperty("date.book").setValue(dateBooks[i]);
			item.getItemProperty("to.process").setValue("50");
			item.getItemProperty("hold").setValue("30");
			item.getItemProperty("total").setValue("100");
		}
		pagedTable.refreshContainerDataSource();*/
		
	}
	
	/**
	 * Set Value into ColumnDefinitions2
	 */	
	private void setIndexedContainer2() {
		
		/*Indexed indexedContainer = pagedTable2.getContainerDataSource();
		indexedContainer.removeAllItems();
		String[] dateBooks = {"02/03/2015 (Mon)","01/03/2015 (Sun)","28/02/2015(Sat)","27/02/2015 (Fri)",
								"26/02/2015 (Thu)","Last week","Current month","Current year"};
		
		for (int i=0 ; i < dateBooks.length; i++) {
			Item item = indexedContainer.addItem(i);
			item.getItemProperty("date.received").setValue(dateBooks[i]);
			item.getItemProperty("to.process").setValue("600");
			item.getItemProperty("hold").setValue("");
			item.getItemProperty("total").setValue("");
		}
		pagedTable.refreshContainerDataSource();*/
		
	}
}

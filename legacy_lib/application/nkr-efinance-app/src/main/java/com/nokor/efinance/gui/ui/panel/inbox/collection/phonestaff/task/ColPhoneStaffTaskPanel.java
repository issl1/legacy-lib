package com.nokor.efinance.gui.ui.panel.inbox.collection.phonestaff.task;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.gui.ui.panel.inbox.collection.CollectionContractsTablePanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColPhoneStaffTaskPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6163635627058169523L;
	
	private TabSheet tabSheet;
	private ColPhoneStaffTaskFilterPanel filterPanel;
	private CollectionContractsTablePanel tablePanel;
	
	public ColPhoneStaffTaskPanel() {
		
		tabSheet = new TabSheet();
		tablePanel = new CollectionContractsTablePanel(false);
		filterPanel = new ColPhoneStaffTaskFilterPanel(tablePanel);
		tablePanel.refresh(filterPanel.getRestrictions());
		
		VerticalLayout verticalLayout = new VerticalLayout();
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("filter"));
		fieldSet.setContent(filterPanel);
		
		verticalLayout.addComponent(fieldSet);
		verticalLayout.addComponent(tablePanel);
		
		
		tabSheet.addTab(verticalLayout, I18N.message("all"));
		tabSheet.addTab(new Label(""), I18N.message("follow.up"));
		tabSheet.addTab(new Label(""), I18N.message("assisted"));
		tabSheet.addTab(new Label(""), I18N.message("promised"));
		tabSheet.addTab(new Label(""), I18N.message("flagged"));
		tabSheet.addTab(new Label(""), I18N.message("foreclosed"));
		tabSheet.addTab(new Label(""), I18N.message("unprocessed"));
		tabSheet.addTab(new Label(""), I18N.message("complete"));
		
		addComponent(tabSheet);
	}
	

}

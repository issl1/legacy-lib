package com.nokor.efinance.gui.ui.panel.inbox.collection.fieldstaff;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.gui.ui.panel.inbox.collection.CollectionContractsTablePanel;
import com.nokor.efinance.gui.ui.panel.inbox.collection.phonestaff.task.ColPhoneStaffTaskFilterPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColFieldStaffMonthlyPressPanel extends VerticalLayout {

	private static final long serialVersionUID = 8620145401616180300L;
	
	private TabSheet tabSheet;
	private ColPhoneStaffTaskFilterPanel filterPanel;
	private CollectionContractsTablePanel tablePanel;
	
	public ColFieldStaffMonthlyPressPanel() {
		tabSheet = new TabSheet();
		
		tablePanel = new CollectionContractsTablePanel(false);
		filterPanel = new ColPhoneStaffTaskFilterPanel(tablePanel);
		tablePanel.refresh(filterPanel.getRestrictions());
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("filter"));
		fieldSet.setContent(filterPanel);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.addComponent(fieldSet);
		mainLayout.addComponent(tablePanel);
		
		tabSheet.addTab(mainLayout, I18N.message("all"));
		tabSheet.addTab(new Label(""), I18N.message("bypassed"));
		tabSheet.addTab(new Label(""), I18N.message("delayed"));
		tabSheet.addTab(new Label(""), I18N.message("extended"));
		tabSheet.addTab(new Label(""), I18N.message("follow.up"));
		tabSheet.addTab(new Label(""), I18N.message("assisted"));
		tabSheet.addTab(new Label(""), I18N.message("promised"));
		tabSheet.addTab(new Label(""), I18N.message("flagged"));
		tabSheet.addTab(new Label(""), I18N.message("foreclose"));
		tabSheet.addTab(new Label(""), I18N.message("unprecessed"));
		tabSheet.addTab(new Label(""), I18N.message("commplete"));
		
		addComponent(tabSheet);
	}

}

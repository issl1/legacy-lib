package com.nokor.efinance.gui.ui.panel.inbox.collection.insidestaff.taks.monthlypressing;

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
public class ColInsideStaffMonthlyPressingPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8849376054913651704L;
	
	private TabSheet tabSheet;
	private ColPhoneStaffTaskFilterPanel filterPanel;
	private CollectionContractsTablePanel tablePanel;
	
	public ColInsideStaffMonthlyPressingPanel() {
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

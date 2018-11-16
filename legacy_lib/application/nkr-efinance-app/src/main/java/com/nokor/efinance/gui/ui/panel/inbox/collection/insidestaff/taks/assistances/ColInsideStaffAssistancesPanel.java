package com.nokor.efinance.gui.ui.panel.inbox.collection.insidestaff.taks.assistances;

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
public class ColInsideStaffAssistancesPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1980827259867760363L;
	
	private TabSheet tabSheet;
	private ColPhoneStaffTaskFilterPanel colPhoneStaffTaskFilterPanel;
	private CollectionContractsTablePanel collectionContractsTablePanel;
	
	public ColInsideStaffAssistancesPanel() {
		tabSheet = new TabSheet();
		
		collectionContractsTablePanel = new CollectionContractsTablePanel(true);
		colPhoneStaffTaskFilterPanel = new ColPhoneStaffTaskFilterPanel(collectionContractsTablePanel);
		collectionContractsTablePanel.refresh(colPhoneStaffTaskFilterPanel.getRestrictions());
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("filter"));
		fieldSet.setContent(colPhoneStaffTaskFilterPanel);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.addComponent(fieldSet);
		mainLayout.addComponent(collectionContractsTablePanel);
		
		tabSheet.addTab(mainLayout, I18N.message("all"));
		tabSheet.addTab(new Label(""), I18N.message("unprocessed"));
		tabSheet.addTab(new Label(""), I18N.message("commplete"));
		
		addComponent(tabSheet);
	}
	

}

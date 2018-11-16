package com.nokor.efinance.gui.ui.panel.collection.oa.supervisor;

import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.service.DebtLevelUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.ColAreaTablePanel;
import com.nokor.efinance.gui.ui.panel.collection.field.supervisor.UnmatchedContractTablePanel;
import com.nokor.efinance.gui.ui.panel.collection.oa.OutsourceAgencyTablePanel;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.PhoneUnassignedOdmPanel;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.ResourcesCollectionPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class OutsourceAgencyUnassignedPanel extends AbstractTabPanel implements FinServicesHelper, SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = 8352111784238948101L;
	
	private TabSheet mainTab;
	private TabSheet oaTab;
	private OutsourceAgencyTablePanel outsourceAgencyTablePanel;
	
	private PhoneUnassignedOdmPanel odm0Tab;
	private PhoneUnassignedOdmPanel odm1Tab;
	private PhoneUnassignedOdmPanel odm2Tab;
	private PhoneUnassignedOdmPanel odm3Tab;
	private PhoneUnassignedOdmPanel odm4Tab;
	private PhoneUnassignedOdmPanel odm5Tab;
	private PhoneUnassignedOdmPanel odm6Tab;
	private PhoneUnassignedOdmPanel odm7Tab;
	
	private ResourcesCollectionPanel resourcesPanel;
	private ColAreaTablePanel areaTablePanel;
	private UnmatchedContractTablePanel unmatchedContractTab;
	
	private Button btnReassign;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		
		btnReassign = new Button();
		btnReassign.setStyleName(Reindeer.BUTTON_LINK);
		btnReassign.setIcon(FontAwesome.REFRESH);
		
		btnReassign.addClickListener(new ClickListener() {			
			/** */
			private static final long serialVersionUID = -3942953559526352725L;			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				
			}
		});
		
		mainTab = new TabSheet();
		mainTab.setNeedRefresh(true);
		
		oaTab = new TabSheet();
		
		oaTab.setNeedRefresh(true);
		oaTab.addSelectedTabChangeListener(this);
		
		odm0Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_0, EColType.OA);
		odm1Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_1, EColType.OA);
		odm2Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_2, EColType.OA);
		odm3Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_3, EColType.OA);
		odm4Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_4, EColType.OA);
		odm5Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_5, EColType.OA);
		odm6Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_6, EColType.OA);
		odm7Tab = new PhoneUnassignedOdmPanel(DebtLevelUtils.DEBT_LEVEL_7, EColType.OA);
		
		outsourceAgencyTablePanel = new OutsourceAgencyTablePanel();
		outsourceAgencyTablePanel.setMainTab(oaTab);
		
		areaTablePanel = new ColAreaTablePanel();
		resourcesPanel = new ResourcesCollectionPanel(EColType.OA);
		unmatchedContractTab = new UnmatchedContractTablePanel();
		
		oaTab.addTab(outsourceAgencyTablePanel, I18N.message("outsource.agents"));
				
		mainTab.addTab(ComponentLayoutFactory.setMargin(oaTab), I18N.message("oa"));
		mainTab.addTab(areaTablePanel, I18N.message("area"));
		mainTab.addTab(resourcesPanel, I18N.message("resources"));
		mainTab.addTab(odm0Tab, I18N.message("odm0"));
		mainTab.addTab(odm1Tab, I18N.message("odm1"));
		mainTab.addTab(odm2Tab, I18N.message("odm2"));
		mainTab.addTab(odm3Tab, I18N.message("odm3"));
		mainTab.addTab(odm4Tab, I18N.message("odm4"));
		mainTab.addTab(odm5Tab, I18N.message("odm5"));
		mainTab.addTab(odm6Tab, I18N.message("odm6"));
		mainTab.addTab(odm7Tab, I18N.message("odm7"));
		mainTab.addTab(unmatchedContractTab, I18N.message("unmatched"));
		
		mainTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {		
			/** */
			private static final long serialVersionUID = 4896753665233783040L;			
			/**
			 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
			 */
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {				
				if (mainTab.getSelectedTab() == odm1Tab) {
					odm1Tab.assignValues();
				} else if (mainTab.getSelectedTab() == odm2Tab) {
					odm2Tab.assignValues();
				} else if (mainTab.getSelectedTab() == odm3Tab) {
					odm3Tab.assignValues();
				} else if (mainTab.getSelectedTab() == odm4Tab) {
					odm4Tab.assignValues();
				} else if (mainTab.getSelectedTab() == odm5Tab) {
					odm5Tab.assignValues();
				} else if (mainTab.getSelectedTab() == odm6Tab) {
					odm6Tab.assignValues();
				} else if (mainTab.getSelectedTab() == odm7Tab) {
					odm7Tab.assignValues();
				} else if (mainTab.getSelectedTab() == resourcesPanel) {
					resourcesPanel.assignValues();
				} else if (mainTab.getSelectedTab() == unmatchedContractTab) {
					unmatchedContractTab.assignValues();
				} else if (mainTab.getSelectedTab() == areaTablePanel) {
					if (mainTab.isNeedRefresh()) {
						areaTablePanel.removeAllComponents();
						areaTablePanel.init();
						mainTab.setNeedRefresh(false);
					} else {
						areaTablePanel.refresh();
					}
				} 				
			}
		});
		
		layout.addComponent(btnReassign);
		layout.addComponent(mainTab);
		
		return layout;
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (oaTab.getSelectedTab().equals(outsourceAgencyTablePanel)) {
			oaTab.removeComponent(outsourceAgencyTablePanel.getFormPanel());
			oaTab.setSelectedTab(outsourceAgencyTablePanel);
			if (oaTab.isNeedRefresh()) {
				outsourceAgencyTablePanel.removeAllComponents();
				outsourceAgencyTablePanel.init();
				oaTab.setNeedRefresh(false);
			} else {
				outsourceAgencyTablePanel.refresh();
			}
		}
	}

}

package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * INBOX Staff Phone Detail Panel 
 * @author uhout.cheng
 */
public class ColContractDetailPanel extends AbstractControlPanel implements SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = 4888845065812508684L;
	
	private TabSheet mainTab;
	
	private InfosContactApplicantPanel infosContactApplicantPanel;
	private CollectionButtonsPanel collectionButtonsPanel;
	private InfosPanel infosPanel;
	private ResultsPanel resultsPanel;
	private InfosFinancePanel infosFinancePanel;
	
	private Contract contract;
	
	/**
	 * 
	 */
	public ColContractDetailPanel() {
		setWidth(550, Unit.PIXELS);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		mainTab = new TabSheet();
		mainTab.addSelectedTabChangeListener(this);
		mainTab.setWidth(550, Unit.PIXELS);
		
		infosContactApplicantPanel = new InfosContactApplicantPanel();
		infosPanel = new InfosPanel();
		resultsPanel = new ResultsPanel();
		infosFinancePanel = new InfosFinancePanel();
		collectionButtonsPanel = new CollectionButtonsPanel(resultsPanel);
		collectionButtonsPanel.setCollectionLockSplitsPanel(infosFinancePanel.getCollectionLockSplitsPanel());
		
		mainTab.addTab(infosPanel, I18N.message("infos"));
		mainTab.addTab(infosFinancePanel, I18N.message("finance"));
		mainTab.addTab(resultsPanel, I18N.message("result"));
		
		contentLayout.addComponent(infosContactApplicantPanel);
		contentLayout.addComponent(collectionButtonsPanel);
		contentLayout.addComponent(mainTab);
		
		addComponent(contentLayout);
	}
	
	/**
	 * 
	 * @param isVisible
	 */
	public void setVisibleLayout(boolean isVisible) {
		this.setVisible(isVisible);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		setVisibleLayout(this.contract != null);
		infosContactApplicantPanel.assignValues(contract);
		collectionButtonsPanel.assignValues(contract);
		assignValuesSeletedTab();
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		assignValuesSeletedTab();
	}
	
	/**
	 * 
	 */
	private void assignValuesSeletedTab() {
		if (this.contract != null) {
			if (mainTab.getSelectedTab().equals(infosPanel)) {
				infosPanel.assignValues(this.contract);				
			} else if (mainTab.getSelectedTab().equals(resultsPanel)) {
				resultsPanel.assignValues(this.contract);
			} else if (mainTab.getSelectedTab().equals(infosFinancePanel)) {
				infosFinancePanel.assignValues(contract);
			}
		}
	}

}

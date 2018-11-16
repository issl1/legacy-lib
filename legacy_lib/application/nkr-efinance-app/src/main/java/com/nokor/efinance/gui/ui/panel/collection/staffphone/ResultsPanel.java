package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Results & Actions panel in collection phone staff
 * @author uhout.cheng
 */
public class ResultsPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = -3224225236300170129L;
	
	private CollectionResultsTablePanel resultsTablePanel;
	private InfoFlagPanel infoFlagPanel;
	private InfoAssistPanel infoAssistPanel;
	private ReturnMotobikeRequestPanel returnMotobikeRequestPanel;
	private SeizedPanel seizedPanel;

	/**
	 * 
	 */
	public ResultsPanel() {
		setWidth(540, Unit.PIXELS);
		setSpacing(true);
		
		resultsTablePanel = new CollectionResultsTablePanel();
		infoFlagPanel = new InfoFlagPanel();
		infoAssistPanel = new InfoAssistPanel();
		returnMotobikeRequestPanel = new ReturnMotobikeRequestPanel();
		seizedPanel = new SeizedPanel();
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		verLayout.addComponent(resultsTablePanel);
		verLayout.addComponent(infoFlagPanel);
		verLayout.addComponent(infoAssistPanel);
		verLayout.addComponent(returnMotobikeRequestPanel);
		verLayout.addComponent(seizedPanel);
		
		Panel mainPanel = ComponentFactory.getPanel();
		mainPanel.setContent(verLayout);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		addComponent(mainPanel);
	}
	
	/**
	 * @return
	 */
	public InfoFlagPanel getInfoFlagPanel() {
		return infoFlagPanel;
	}
	
	/**
	 * @return
	 */
	public InfoAssistPanel getInfoAssistPanel() {
		return infoAssistPanel;
	}
	

	/**
	 * @return the returnMotobikeRequestPanel
	 */
	public ReturnMotobikeRequestPanel getReturnMotobikeRequestPanel() {
		return returnMotobikeRequestPanel;
	}

	/**
	 * @return the seizedPanel
	 */
	public SeizedPanel getSeizedPanel() {
		return seizedPanel;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		resultsTablePanel.assignValues(contract);
		infoFlagPanel.assignValues(contract);		
		infoAssistPanel.assignValues(contract);
		returnMotobikeRequestPanel.assignValues(contract);
		seizedPanel.assignValues(contract);
	}
}

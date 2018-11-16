package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Informations panel in collection phone staff
 * @author uhout.cheng
 */
public class InfosPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 7070330962102801886L;

	private InfosAssetPanel infosAssetPanel;
	private CollectionContactsPanel contactsPanel;
	private ContractOperationsPanel contractOperationsPanel;

	/**
	 * 
	 */
	public InfosPanel() {
		setWidth(540, Unit.PIXELS);
		setSpacing(true);
		
		infosAssetPanel = new InfosAssetPanel();
		contactsPanel = new CollectionContactsPanel();
		contractOperationsPanel = new ContractOperationsPanel();
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		verLayout.setWidth(540, Unit.PIXELS);
		verLayout.addComponent(infosAssetPanel);
		verLayout.addComponent(contactsPanel);
		verLayout.addComponent(contractOperationsPanel);
		
		Panel mainPanel = ComponentFactory.getPanel();
		mainPanel.setContent(verLayout);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		addComponent(mainPanel);
	}
	
	/**
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		infosAssetPanel.assignValues(contract);
		contactsPanel.assignValues(contract);
		contractOperationsPanel.assignValue(contract);
	}

}

package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;

/**
 * @author uhout.cheng
 */
public class InfosFinancePanel extends AbstractControlPanel {
	/** 
	 */
	private static final long serialVersionUID = 6570715890182405531L;
	
	private CollectionHighlightsPanel highlightsPanel;
	
	private CollectionLockSplitsPanel collectionLockSplitsPanel;
	
	/**
	 * @return the collectionLockSplitsPanel
	 */
	public CollectionLockSplitsPanel getCollectionLockSplitsPanel() {
		return collectionLockSplitsPanel;
	}

	/**
	 * @param collectionLockSplitsPanel the collectionLockSplitsPanel to set
	 */
	public void setCollectionLockSplitsPanel(CollectionLockSplitsPanel collectionLockSplitsPanel) {
		this.collectionLockSplitsPanel = collectionLockSplitsPanel;
	}

	/**
	 * 
	 */
	public InfosFinancePanel() {
		setWidth(540, Unit.PIXELS);
		highlightsPanel = new CollectionHighlightsPanel();
		setCollectionLockSplitsPanel(highlightsPanel.getLockSplitsPanel());
		addComponent(highlightsPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		highlightsPanel.assignValues(contract);
	}

}

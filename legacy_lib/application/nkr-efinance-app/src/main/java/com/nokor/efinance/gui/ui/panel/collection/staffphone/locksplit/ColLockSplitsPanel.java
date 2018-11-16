package com.nokor.efinance.gui.ui.panel.collection.staffphone.locksplit;

import java.util.List;

import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class ColLockSplitsPanel extends AbstractTabPanel {

	/** */
	private static final long serialVersionUID = -1989706019843187407L;
	
	private ColLockSplitTopPanel topPanel;
//	private TabSheet mainTab;
//	private ColLockSplitInstallmentsPanel installmentsPanel;
//	private ColLockSplitAfterSalesPanel afterSalesPanel;
//	private ColLockSplitFeesPanel feesPanel;
	private ColLockSplitButtonsPanel buttonsPanel;
	private ColLockSplitRecapTablePanel lockSplitRecapTablePanel;
	private ColLockSplitItemTablePanel lockSplitItemTablePanel;
	
	private CollectionLockSplitPopup colLockSplitPopup;
	
	/**
	 * 
	 * @param colLockSplitPopup
	 */
	public ColLockSplitsPanel(CollectionLockSplitPopup colLockSplitPopup) {
		this.colLockSplitPopup = colLockSplitPopup;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		topPanel = new ColLockSplitTopPanel();
		
		/*installmentsPanel = new ColLockSplitInstallmentsPanel();
		afterSalesPanel = new ColLockSplitAfterSalesPanel();
		feesPanel = new ColLockSplitFeesPanel();*/
		
		buttonsPanel = new ColLockSplitButtonsPanel(this);
		lockSplitRecapTablePanel = new ColLockSplitRecapTablePanel();
		
//		mainTab = new TabSheet();
//		mainTab.addTab(installmentsPanel, I18N.message("installments"));
//		mainTab.addTab(feesPanel, I18N.message("fees"));
//		mainTab.addTab(afterSalesPanel, I18N.message("after.sales"));
		
		lockSplitItemTablePanel = new ColLockSplitItemTablePanel();
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.addComponent(topPanel);
//		contentLayout.addComponent(mainTab);
		contentLayout.addComponent(lockSplitRecapTablePanel);
		contentLayout.addComponent(lockSplitItemTablePanel);
		contentLayout.addComponent(buttonsPanel);
		return contentLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	public LockSplit getLockSplit() {
		LockSplit lockSplit = topPanel.getLockSplit();
		
		List<LockSplitItem> lockSplitItems = lockSplitItemTablePanel.getLockSplitItems();

		double totalAmount = 0d;
		if (!lockSplitItems.isEmpty()) {
			for (LockSplitItem lockSplitItem : lockSplitItems) {
				totalAmount += MyNumberUtils.getDouble(lockSplitItem.getTiAmount());
			}
		}
		lockSplit.setTotalAmount(totalAmount);
		lockSplit.setItems(lockSplitItems);
		return lockSplit;
	}
	
	/**
	 * 
	 * @return
	 */
	public String validated() {
		return topPanel.validated();
	}

	/**
	 * 
	 * @param lockSplit
	 */
	public void assignValues(LockSplit lockSplit) {
		topPanel.assignValues(lockSplit);
//		installmentsPanel.assignValues(lockSplit);
//		afterSalesPanel.assignValues(lockSplit);
//		feesPanel.assignValues(lockSplit);
		lockSplitRecapTablePanel.assignValues(lockSplit);
		lockSplitItemTablePanel.assignValues(lockSplit);
		buttonsPanel.assignValues(lockSplit);
		buttonsPanel.setColLockSplitPopup(colLockSplitPopup);
	}

}

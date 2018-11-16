package com.nokor.efinance.gui.ui.panel.payment.locksplit;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment.ColLockSplitPaymentPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;

/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(LockSplitsPanel.NAME)
public class LockSplitsPanel extends AbstractTabsheetPanel implements View {

	/**
	 */
	private static final long serialVersionUID = -7865558383649659450L;
	
	public static final String NAME = "lock.splits";
	
	private LockSplitTablePanel lockSplitTablePanel;
	//private LockSplitFormPanel lockSplitFormPanel;
	private ColLockSplitPaymentPanel colLockSplitPaymentPanel;
	

	@Override
	public void onAddEventClick() {	
	}

	@Override
	public void onEditEventClick() {
		getLockSplitFormPanel().reset();
		getTabSheet().addFormPanel(colLockSplitPaymentPanel);
		initSelectedTab(getLockSplitFormPanel());
	}
	
	@Override
	public void initSelectedTab(Component selectedTab) {
		if (selectedTab == getLockSplitFormPanel()) {
			LockSplit lockSplit = ENTITY_SRV.getById(LockSplit.class, lockSplitTablePanel.getItemSelectedId());
			getLockSplitFormPanel().assignValues(lockSplit);
		} else if (selectedTab == lockSplitTablePanel && colLockSplitPaymentPanel.isNeedRefresh()) {
			lockSplitTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);	
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		super.init();
		lockSplitTablePanel = new LockSplitTablePanel();
		lockSplitTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(lockSplitTablePanel);
	}
	
	/**
	 * @return
	 */
	private ColLockSplitPaymentPanel getLockSplitFormPanel() {
		if (colLockSplitPaymentPanel == null) {
			colLockSplitPaymentPanel = new ColLockSplitPaymentPanel();
			colLockSplitPaymentPanel.setCaption(I18N.message("locksplit"));
		}
		return colLockSplitPaymentPanel;
	}
}

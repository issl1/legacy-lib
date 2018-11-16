package com.nokor.efinance.core.quotation.panel.history;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Reindeer;

/**
 * Lock split history pop up window
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(LockSplitHistoryPopupTablePanel.NAME)
public class LockSplitHistoryPopupTablePanel extends AbstractTabPanel implements View {

	/** */
	private static final long serialVersionUID = -3937570831334805449L;

	public static final String NAME = "lock.split.history";
	
	private OperateSuspensePopupWindow recentLockSplitHistoryPanel;
	private OperateSuspensePopupWindow operateSuspenseAmountPanel;
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
			
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		// Recent lock split
		recentLockSplitHistoryPanel = new OperateSuspensePopupWindow();
		Panel recentLockSplitPanel = new Panel(I18N.message("recent.lock.split"));
		recentLockSplitPanel.setStyleName(Reindeer.PANEL_LIGHT);
		recentLockSplitPanel.setContent(recentLockSplitHistoryPanel);
		
		// Operate suspense amount
		operateSuspenseAmountPanel = new OperateSuspensePopupWindow();
		Panel operateSuspensePanel = new Panel(I18N.message("operate.suspense.amount"));
		operateSuspensePanel.setStyleName(Reindeer.PANEL_LIGHT);
		operateSuspensePanel.setContent(operateSuspenseAmountPanel);
		
		HorizontalLayout mainLayout = new HorizontalLayout();
		//mainLayout.setSpacing(true);
		mainLayout.addComponent(recentLockSplitPanel);
		mainLayout.addComponent(operateSuspensePanel);
		
        return mainLayout;
	}
}

package com.nokor.efinance.gui.ui.panel.contract.reverse;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author meng.kim
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ReverseContractPanel.NAME)
public class ReverseContractPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -470403725943364622L;

	public static final String NAME = "reverse.contract.status";
	
	@Autowired
	private ReverseContractStatusFormPanel reverseContractStatusFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		reverseContractStatusFormPanel.setCaption(I18N.message("reverse.contract"));
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		String cotraId = event.getParameters();
		reverseContractStatusFormPanel.assignValues(new Long(cotraId));
		getTabSheet().setTablePanel(reverseContractStatusFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		
	}
}

package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory;
import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * Full contract page in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ColPhoneContractHistoryPanel.NAME)
public class ColPhoneContractHistoryPanel extends VerticalLayout implements View, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -2281345289839472688L;

	public static final String NAME = "contract.histories";
	
	private TabSheet mainTab;
	private ColContractHistoryFormPanel formPanel;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		mainTab = new TabSheet();
		formPanel = new ColContractHistoryFormPanel(false);
		
		mainTab.addTab(formPanel, I18N.message("contract.histories"));
		addComponent(mainTab);
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		String cotraId = event.getParameters();
		if (StringUtils.isNotEmpty(cotraId)) {
			formPanel.assignValues(new Long(cotraId), true);
		} else {
			formPanel.reset();
		}
	}
	
}

package com.nokor.efinance.gui.ui.panel.quotation;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(QuotationsMgtPanel.NAME)
public class QuotationsMgtPanel extends AbstractTabsheetPanel implements View, AssetEntityField {

	private static final long serialVersionUID = -6651024937294153368L;

	public static final String NAME = "quotations.mgt";
	
	@Autowired
	private QuotationMgtTablePanel quotationMgtTablePanel;
		
		
	@PostConstruct
	public void PostConstruct() {
		super.init();
		quotationMgtTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(quotationMgtTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
	}

	@Override
	public void onEditEventClick() {
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
	}	
}

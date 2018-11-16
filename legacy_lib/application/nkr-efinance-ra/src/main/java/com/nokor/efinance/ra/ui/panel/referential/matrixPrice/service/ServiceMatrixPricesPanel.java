package com.nokor.efinance.ra.ui.panel.referential.matrixPrice.service;

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
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ServiceMatrixPricesPanel.NAME)
public class ServiceMatrixPricesPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = 8497439108307144040L;

	public static final String NAME = "service.matrix.price";
		
	@Autowired
	private ServiceMatrixPriceTablePanel matrixPriceTablePanel;
	@Autowired
	private ServiceMatrixPriceFormPanel matrixPriceFormPanel;
		
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		matrixPriceTablePanel.setMainPanel(this);
		matrixPriceFormPanel.setCaption( I18N.message("matrix.price"));
		getTabSheet().setTablePanel(matrixPriceTablePanel);		
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	@Override
	public void onAddEventClick() {
		matrixPriceFormPanel.reset();
		getTabSheet().addFormPanel(matrixPriceFormPanel);
		getTabSheet().setSelectedTab(matrixPriceFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(matrixPriceFormPanel);
		initSelectedTab(matrixPriceFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == matrixPriceFormPanel) {
			matrixPriceFormPanel.assignValues(matrixPriceTablePanel.getItemSelectedId());
		} else if (selectedTab == matrixPriceTablePanel && getTabSheet().isNeedRefresh()) {
			matrixPriceTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}

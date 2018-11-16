package com.nokor.efinance.ra.ui.panel.finproduct.product;

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
 * 
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ProductsPanel.NAME)
public class ProductsPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -7824274868470600215L;

	public static final String NAME = "product";
	
	@Autowired
	private ProductTablePanel productTablePanel;
	@Autowired
	private ProductFormPanel productFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		productTablePanel.setMainPanel(this);
		productFormPanel.setCaption(I18N.message("product"));
		getTabSheet().setTablePanel(productTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		productFormPanel.reset();
		getTabSheet().addFormPanel(productFormPanel);
		getTabSheet().setSelectedTab(productFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(productFormPanel);
		initSelectedTab(productFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == productFormPanel) {
			productFormPanel.assignValues(productTablePanel.getItemSelectedId());
		} else if (selectedTab == productTablePanel && getTabSheet().isNeedRefresh()) {
			productTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}

package com.nokor.efinance.ra.ui.panel.finproduct.productline;

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
 *  Produce Lines Panel
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ProductLinesPanel.NAME)
public class ProductLinesPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = 3460134954488692920L;

	public static final String NAME = "product.lines";
	
	@Autowired
	private ProductLineTablePanel productLineTablePanel;
	@Autowired
	private ProductLineFormPanel productLineFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		productLineTablePanel.setMainPanel(this);
		productLineFormPanel.setCaption(I18N.message("product.line"));
		getTabSheet().setTablePanel(productLineTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		productLineFormPanel.reset();
		getTabSheet().addFormPanel(productLineFormPanel);
		getTabSheet().setSelectedTab(productLineFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(productLineFormPanel);
		initSelectedTab(productLineFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == productLineFormPanel) {
			productLineFormPanel.assignValues(productLineTablePanel.getItemSelectedId());
		} else if (selectedTab == productLineTablePanel && getTabSheet().isNeedRefresh()) {
			productLineTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}

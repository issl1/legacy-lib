package com.nokor.efinance.core.document.template;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Document Template panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DocumentTemplatePanel.NAME)
public class DocumentTemplatePanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = 6956738144733189384L;
	public static final String NAME = "document.templates";
	
	@Autowired
	private DocumentTemplateTablePanel tablePanel;
	@Autowired
	private DocumentTemplateFormPanel formPanel;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		tablePanel.setMainPanel(this);
		formPanel.setCaption(I18N.message("document.template"));
		getTabSheet().setTablePanel(tablePanel);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == formPanel) {
			formPanel.assignValues(tablePanel.getItemSelectedId());
		} else if (selectedTab == tablePanel && getTabSheet().isNeedRefresh()) {
			tablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		formPanel.reset();
		getTabSheet().addFormPanel(formPanel);
		getTabSheet().setSelectedTab(formPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		if (!ProfileUtil.isPOS()) {
			getTabSheet().addFormPanel(formPanel);
			initSelectedTab(formPanel);
		}
	}

}

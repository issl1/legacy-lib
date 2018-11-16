package com.nokor.efinance.ra.ui.panel.finproduct.term.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.ra.ui.panel.finproduct.term.detail.TermFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;

/**
 * Term Holder Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(TermHolderPanel.NAME)
public class TermHolderPanel extends AbstractTabsheetPanel implements View {
	/** */
	private static final long serialVersionUID = -4307044573898036735L;
	
	public static final String NAME = "terms";
	
	@Autowired
	private TermTablePanel tablePanel;
	@Autowired
	private TermFormPanel formPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		tablePanel.setMainPanel(this);
		formPanel.setCaption(I18N.message("term"));
		getTabSheet().setTablePanel(tablePanel);
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
		getTabSheet().addFormPanel(formPanel);
		initSelectedTab(formPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(Component selectedTab) {
		if (selectedTab == formPanel) {
			formPanel.assignValues(tablePanel.getItemSelectedId());
		} else if (selectedTab == tablePanel && getTabSheet().isNeedRefresh()) {
			tablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

}

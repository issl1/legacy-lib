package com.nokor.efinance.ra.ui.panel.credit.risksegment.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.ra.ui.panel.credit.risksegment.detail.RiskSegmentFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;

/**
 * Risk Segment Holder Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(RiskSegmentHolderPanel.NAME)
public class RiskSegmentHolderPanel extends AbstractTabsheetPanel implements View {
	/** */
	private static final long serialVersionUID = 7801838023122143211L;
	
	public static final String NAME = "risk.segments";
	
	@Autowired
	private RiskSegmentTablePanel tablePanel;
	@Autowired
	private RiskSegmentFormPanel formPanel;
	
	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		tablePanel.setMainPanel(this);
		formPanel.setCaption(I18N.message("risk.segment"));
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

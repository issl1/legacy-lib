package com.nokor.efinance.ra.ui.panel.dealer.group;

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
@VaadinView(DealerGroupsPanel.NAME)
public class DealerGroupsPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -2983055467054135680L;
	
	public static final String NAME = "dealer.groups";
	
	@Autowired
	private DealerGroupTablePanel dealerGroupTablePanel;
	@Autowired
	private DealerGroupFormPanel dealerGroupFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		dealerGroupTablePanel.setMainPanel(this);
		dealerGroupFormPanel.setCaption(I18N.message("groups"));
		dealerGroupFormPanel.setMainPanel(this);
		getTabSheet().setTablePanel(dealerGroupTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		dealerGroupFormPanel.reset();
		getTabSheet().addFormPanel(dealerGroupFormPanel);
		getTabSheet().setSelectedTab(dealerGroupFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(dealerGroupFormPanel);
		initSelectedTab(dealerGroupFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == dealerGroupFormPanel) {
			dealerGroupFormPanel.assignValues(dealerGroupTablePanel.getItemSelectedId());
		} else if (selectedTab == dealerGroupTablePanel && getTabSheet().isNeedRefresh()) {
			dealerGroupTablePanel.refresh();
		} 
		getTabSheet().setSelectedTab(selectedTab);
	}
}

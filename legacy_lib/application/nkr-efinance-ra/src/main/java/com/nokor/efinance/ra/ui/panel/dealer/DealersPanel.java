package com.nokor.efinance.ra.ui.panel.dealer;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DealersPanel.NAME)
public class DealersPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = -2149707177396177780L;

	public static final String NAME = "dealers";
	
	@Autowired
	private DealerTablePanel dealerTablePanel;
	@Autowired
	private DealerFormPanel dealerFormPanel;
		
	@PostConstruct
	public void PostConstruct() {
		super.init();
		dealerTablePanel.setMainPanel(this);
		dealerFormPanel.setCaption(I18N.message("dealer"));
		getTabSheet().setTablePanel(dealerTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	public void onAddEventClick() {
		dealerFormPanel.reset();
		getTabSheet().addFormPanel(dealerFormPanel);
		getTabSheet().setSelectedTab(dealerFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(dealerFormPanel);
		initSelectedTab(dealerFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == dealerFormPanel) {
			dealerFormPanel.assignValues(dealerTablePanel.getItemSelectedId());
		} else if (selectedTab == dealerTablePanel && getTabSheet().isNeedRefresh()) {
			dealerTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}

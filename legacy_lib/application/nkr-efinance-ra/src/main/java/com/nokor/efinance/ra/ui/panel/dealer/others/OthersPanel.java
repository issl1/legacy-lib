package com.nokor.efinance.ra.ui.panel.dealer.others;

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
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(OthersPanel.NAME)
public class OthersPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = -2149707177396177780L;

	public static final String NAME = "others";
	
	@Autowired
	private OthersTablePanel subDealerTablePanel;
	@Autowired
	private OthersFormPanel subDealerFormPanel;
	
	@Autowired
	private OthersBankAccountTablePanel subDealerBankAccountTablePanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		subDealerTablePanel.setMainPanel(this);
		subDealerFormPanel.setCaption(I18N.message("other"));
		subDealerBankAccountTablePanel.setCaption(I18N.message("bank.account"));
		getTabSheet().setTablePanel(subDealerTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	public void onAddEventClick() {
		subDealerFormPanel.reset();
		getTabSheet().addFormPanel(subDealerFormPanel);
		getTabSheet().setSelectedTab(subDealerFormPanel);
	}

	@Override
	public void onEditEventClick() {
		subDealerBankAccountTablePanel.assignValues(subDealerTablePanel.getItemSelectedId());
		getTabSheet().addFormPanel(subDealerFormPanel);
		getTabSheet().addFormPanel(subDealerBankAccountTablePanel);
		initSelectedTab(subDealerFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == subDealerFormPanel) {
			subDealerFormPanel.assignValues(subDealerTablePanel.getItemSelectedId());
		} else if (selectedTab == subDealerTablePanel && getTabSheet().isNeedRefresh()) {
			subDealerTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}

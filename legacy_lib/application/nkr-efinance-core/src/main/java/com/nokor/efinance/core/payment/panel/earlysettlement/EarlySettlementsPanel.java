package com.nokor.efinance.core.payment.panel.earlysettlement;

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
 * EarlySettlements panel
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(EarlySettlementsPanel.NAME)
public class EarlySettlementsPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -470403725943364622L;

	public static final String NAME = "early.settlement";
	
	@Autowired
	private EarlySettlementFormPanel earlySettlementFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		earlySettlementFormPanel.setCaption(I18N.message("paid.off"));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String cotraId = event.getParameters();
		earlySettlementFormPanel.assignValues(new Long(cotraId));
		getTabSheet().setTablePanel(earlySettlementFormPanel);
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

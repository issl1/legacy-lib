package com.nokor.efinance.ra.ui.panel.insurance.campaign;

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
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(InsuranceCampaignsPanel.NAME)
public class InsuranceCampaignsPanel extends AbstractTabsheetPanel implements View {
	
	public static final String NAME = "insurance.campaigns";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private InsuranceCampaignsTablePanel insCampaignsTablePanel;
	@Autowired
	private InsuranceCampaignsFormPanel insCampaignsFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		insCampaignsTablePanel.setMainPanel(this);
		insCampaignsFormPanel.setCaption(I18N.message("insurance.campaign"));
		getTabSheet().setTablePanel(insCampaignsTablePanel);
	}

	@Override
	public void onAddEventClick() {
		insCampaignsFormPanel.reset();
		getTabSheet().addFormPanel(insCampaignsFormPanel);
		getTabSheet().setSelectedTab(insCampaignsFormPanel);
		
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(insCampaignsFormPanel);
		initSelectedTab(insCampaignsFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == insCampaignsFormPanel) {
			insCampaignsFormPanel.assignValues(insCampaignsTablePanel.getItemSelectedId());
		} else if (selectedTab == insCampaignsTablePanel) {
			insCampaignsTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}

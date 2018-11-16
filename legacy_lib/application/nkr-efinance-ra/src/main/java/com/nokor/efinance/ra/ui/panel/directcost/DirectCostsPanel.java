package com.nokor.efinance.ra.ui.panel.directcost;

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
@VaadinView(DirectCostsPanel.NAME)
public class DirectCostsPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -7330166795254236089L;

	public static final String NAME = "direct.costs";
	
	@Autowired
	private DirectCostTablePanel directCostTablePanel;
	@Autowired
	private DirectCostFormPanel directCostFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		directCostTablePanel.setMainPanel(this);
		directCostFormPanel.setCaption(I18N.message("direct.cost"));
		getTabSheet().setTablePanel(directCostTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		directCostFormPanel.reset();
		getTabSheet().addFormPanel(directCostFormPanel);
		getTabSheet().setSelectedTab(directCostFormPanel);
	}

	@Override
	public void onEditEventClick() {
		directCostFormPanel.reset();
		getTabSheet().addFormPanel(directCostFormPanel);
		initSelectedTab(directCostFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == directCostFormPanel) {
			directCostFormPanel.assignValues(directCostTablePanel.getItemSelectedId());
		} else if (selectedTab == directCostTablePanel) {
			directCostTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}

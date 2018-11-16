package com.nokor.efinance.ra.ui.panel.collections.minimunreturnrate;

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
@VaadinView(MinReturnRatePanel.NAME)
public class MinReturnRatePanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = 6775148873579632717L;
	
	public static final String NAME = "min.return.rate";
	
	@Autowired
	private MinReturnRateTablePanel minReturnRateTablePanel;
	@Autowired
	private MinReturnRateFormPanel minReturnRateFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		minReturnRateTablePanel.setMainPanel(this);
		minReturnRateFormPanel.setCaption( I18N.message("min.return.rate"));
		getTabSheet().setTablePanel(minReturnRateTablePanel);		
	}

	@Override
	public void onAddEventClick() {
		minReturnRateFormPanel.reset();
		getTabSheet().addFormPanel(minReturnRateFormPanel);
		getTabSheet().setSelectedTab(minReturnRateFormPanel);
		
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(minReturnRateFormPanel);
		initSelectedTab(minReturnRateFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == minReturnRateFormPanel) {
			minReturnRateFormPanel.assignValues(minReturnRateTablePanel.getItemSelectedId());
		} else if (selectedTab == minReturnRateTablePanel && getTabSheet().isNeedRefresh()) {
			minReturnRateTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}

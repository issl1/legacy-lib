package com.nokor.efinance.ra.ui.panel.vat;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(VatsPanel.NAME)
public class VatsPanel extends AbstractTabsheetPanel implements View {
	public static final String NAME = "vats";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private VatsTablePanel vatsTablePanel;
	
	@Autowired
	private VatsFormPanel vatsFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		vatsTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(vatsTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	public void onAddEventClick() {
		vatsFormPanel.reset();
		getTabSheet().addFormPanel(vatsFormPanel);
		getTabSheet().setSelectedTab(vatsFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(vatsFormPanel);
		initSelectedTab(vatsFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == vatsFormPanel) {
			vatsFormPanel.assignValues(vatsTablePanel.getItemSelectedId());
		} else if (selectedTab == vatsTablePanel && getTabSheet().isNeedRefresh()) {
			vatsTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}

}

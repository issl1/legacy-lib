package com.nokor.efinance.ra.ui.panel.dealer.ladder;

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
@VaadinView(LadderTypesPanel.NAME)
public class LadderTypesPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -2983055467054135680L;
	
	public static final String NAME = "ladder.types";
	
	@Autowired
	private LadderTypeTablePanel ladderTypeTablePanel;
	@Autowired
	private LadderTypeFormPanel ladderTypeFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		ladderTypeTablePanel.setMainPanel(this);
		ladderTypeFormPanel.setCaption(I18N.message("ladder.type"));
		ladderTypeFormPanel.setMainPanel(this);
		getTabSheet().setTablePanel(ladderTypeTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		ladderTypeFormPanel.reset();
		getTabSheet().addFormPanel(ladderTypeFormPanel);
		getTabSheet().setSelectedTab(ladderTypeFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(ladderTypeFormPanel);
		initSelectedTab(ladderTypeFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == ladderTypeFormPanel) {
			ladderTypeFormPanel.assignValues(ladderTypeTablePanel.getItemSelectedId());
		} else if (selectedTab == ladderTypeTablePanel && getTabSheet().isNeedRefresh()) {
			ladderTypeTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}

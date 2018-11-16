package com.nokor.efinance.gui.ui.panel.collection.field.supervisor.result;

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
@VaadinView(ResultsPanel.NAME)
public class ResultsPanel extends AbstractTabsheetPanel implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3554787070786374024L;
	
	public static final String NAME = "col.results";
	
	@Autowired
	private ResultsTablePanel resultsTablePanel;
	
	@Autowired
	private ResultsFormPanel resultsFormPanel;

	@PostConstruct
	public void PostConstruct() {
		super.init();
		resultsTablePanel.setMainPanel(this);
		resultsFormPanel.setCaption(I18N.message("result"));
		getTabSheet().setTablePanel(resultsTablePanel);
	}

	@Override
	public void onAddEventClick() {
		resultsFormPanel.reset();
		getTabSheet().addFormPanel(resultsFormPanel);
		getTabSheet().setSelectedTab(resultsFormPanel);
	}

	@Override
	public void onEditEventClick() {
		resultsFormPanel.reset();
		getTabSheet().addFormPanel(resultsFormPanel);
		initSelectedTab(resultsFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == resultsFormPanel) {
			resultsFormPanel.assignValue(resultsTablePanel.getItemSelectedId());
		} else if (selectedTab == resultsFormPanel && getTabSheet().isNeedRefresh()) {
			resultsTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);	
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}

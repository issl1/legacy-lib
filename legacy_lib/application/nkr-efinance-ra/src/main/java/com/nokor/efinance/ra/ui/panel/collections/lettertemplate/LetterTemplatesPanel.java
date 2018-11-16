package com.nokor.efinance.ra.ui.panel.collections.lettertemplate;

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
@VaadinView(LetterTemplatesPanel.NAME)
public class LetterTemplatesPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = -2149707107396177780L;

	public static final String NAME = "letter.templates";
		
	@Autowired
	private LetterTemplateTablePanel letterTamplateTablePanel;
	@Autowired
	private LetterTemplateFormPanel letterTamplateFormPanel;
		
	@PostConstruct
	public void PostConstruct() {
		super.init();
		letterTamplateTablePanel.setMainPanel(this);
		letterTamplateFormPanel.setCaption( I18N.message("letter.template"));
		getTabSheet().setTablePanel(letterTamplateTablePanel);		
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		letterTamplateFormPanel.reset();
		getTabSheet().addFormPanel(letterTamplateFormPanel);
		getTabSheet().setSelectedTab(letterTamplateFormPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		letterTamplateFormPanel.reset();
		getTabSheet().addFormPanel(letterTamplateFormPanel);
		initSelectedTab(letterTamplateFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == letterTamplateFormPanel) {
			letterTamplateFormPanel.assignValues(letterTamplateTablePanel.getItemSelectedId());
		} else if (selectedTab == letterTamplateTablePanel && getTabSheet().isNeedRefresh()) {
			letterTamplateTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}

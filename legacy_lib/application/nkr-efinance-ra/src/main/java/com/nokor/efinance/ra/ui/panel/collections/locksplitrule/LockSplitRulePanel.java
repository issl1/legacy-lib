package com.nokor.efinance.ra.ui.panel.collections.locksplitrule;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.ra.ui.panel.collections.locksplitrule.detail.LockSplitRuleFormPanel;
import com.nokor.efinance.ra.ui.panel.collections.locksplitrule.ruleitem.LockSplitRuleItemFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Lock Split Rule Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(LockSplitRulePanel.NAME)
public class LockSplitRulePanel extends AbstractTabsheetPanel implements View {

	/** */
	private static final long serialVersionUID = -6673798285053308685L;
	
	public static final String NAME = "lock.split.rules";
	
	@Autowired
	private LockSplitRuleTablePanel tablePanel;
	
	@Autowired
	private LockSplitRuleFormPanel formPanel;
	
	@Autowired
	private LockSplitRuleItemFormPanel itemFormPanel;
	
	/**
	 * Post Constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		tablePanel.setMainPanel(this);
		formPanel.setCaption(I18N.message("lock.split.rule"));
		formPanel.setMainPanel(this);
		getTabSheet().setTablePanel(tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		formPanel.reset();
		getTabSheet().addFormPanel(formPanel);
		getTabSheet().setSelectedTab(formPanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(formPanel);
		addSubTab(tablePanel.getItemSelectedId());
		initSelectedTab(formPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == formPanel) {
			formPanel.assignValues(tablePanel.getItemSelectedId());
		} else if (selectedTab == tablePanel && getTabSheet().isNeedRefresh()) {
			tablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	/**
	 * Add Sub Tab
	 * @param lockSplitRuleId
	 */
	public void addSubTab(Long lockSplitRuleId) {
		itemFormPanel.assignValues(lockSplitRuleId);
		getTabSheet().addFormPanel(itemFormPanel);
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

}

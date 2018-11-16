package com.nokor.efinance.gui.ui.panel.collection.callcenter;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class InboxWelcomeCallPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper, SelectedTabChangeListener {

	/** */
	private static final long serialVersionUID = 58588138725723232L;

	private TabSheet rightLayout;
	
	private Button btnWelcomeCall;
	
	private WelcomeCallPanel welcomeCallAllPanel;
	private WelcomeCallPanel welcomeCallFollowUpPanel;
	private WelcomeCallPanel welcomeCallUnprocessedPanel;
	private WelcomeCallPanel welcomeCallCompletedPanel;
	
	/**
	 * 	
	 */
	public InboxWelcomeCallPanel() {
		welcomeCallAllPanel = new WelcomeCallPanel(null);
		ECallCenterResult ko = ENTITY_SRV.getByCode(ECallCenterResult.class, ECallCenterResult.KO);
		ECallCenterResult ok = ENTITY_SRV.getByCode(ECallCenterResult.class, ECallCenterResult.OK);
		ECallCenterResult other = ENTITY_SRV.getByCode(ECallCenterResult.class, ECallCenterResult.OTHER);
		welcomeCallFollowUpPanel = new WelcomeCallPanel(ko);
		welcomeCallUnprocessedPanel = new WelcomeCallPanel(other);
		welcomeCallCompletedPanel = new WelcomeCallPanel(ok);
		
		rightLayout = new TabSheet();
		rightLayout.addSelectedTabChangeListener(this);
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.setMargin(new MarginInfo(false, false, true, true));
		vLayout.addComponent(rightLayout);
		
		Panel leftPanel = new Panel(getButtonPanel());
		leftPanel.setStyleName(Reindeer.PANEL_LIGHT);
		leftPanel.setSizeUndefined();
		
		HorizontalLayout horSplitPanel = new HorizontalLayout();
		horSplitPanel.setSizeFull();
		horSplitPanel.addComponent(leftPanel);
		horSplitPanel.addComponent(vLayout);
		horSplitPanel.setExpandRatio(vLayout, 1.0f);
		
		assignValues();
		
		setSpacing(true);
		addComponent(horSplitPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getButtonPanel() {
		btnWelcomeCall = getButtonMenu("welcome.call");
		VerticalLayout vLayout = new VerticalLayout(btnWelcomeCall);
		vLayout.setMargin(new MarginInfo(true, false, true, false));
		return vLayout;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Button getButtonMenu(String caption) {
		Button button = new NativeButton("<h3 style=\"text-align: left; margin:2px 0\">" + I18N.message(caption) + "</h3>");
		button.setStyleName("btn btn-success button-small");
		button.setWidth(110, Unit.PIXELS);
		button.setCaptionAsHtml(true);
		button.addClickListener(this);
		return button;
	}
	
	/**
	 * 
	 */
	private void assignValues() {
		this.reset();
		tabAction();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		rightLayout.removeAllComponents();
		setEnabledButton();
		if (event.getButton().equals(btnWelcomeCall)) {
			tabAction();
		}
	}
	
	/**
	 * 
	 */
	private void tabAction() {
		btnWelcomeCall.setEnabled(false);
		welcomeCallFollowUpPanel.assignValues();
		this.setDisplayTab(welcomeCallAllPanel, "all");
		this.setDisplayTab(welcomeCallFollowUpPanel, "follow.up");
		this.setDisplayTab(welcomeCallUnprocessedPanel, "unprocessed");
		this.setDisplayTab(welcomeCallCompletedPanel, "completed");
		rightLayout.setSelectedTab(welcomeCallFollowUpPanel);
	}
	
	/**
	 * 
	 * @param component
	 * @param caption
	 */
	private void setDisplayTab(Component component, String caption) {
		rightLayout.addTab(component, I18N.message(caption));
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		rightLayout.removeAllComponents();
		setEnabledButton();
	}
	
	/**
	 * 
	 */
	private void setEnabledButton() {
		btnWelcomeCall.setEnabled(true);
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (rightLayout.getSelectedTab().equals(welcomeCallAllPanel)) {
			welcomeCallAllPanel.assignValues();
		} else if (rightLayout.getSelectedTab().equals(welcomeCallUnprocessedPanel)) {
			welcomeCallUnprocessedPanel.assignValues();
		} else if (rightLayout.getSelectedTab().equals(welcomeCallCompletedPanel)) {
			welcomeCallCompletedPanel.assignValues();
		}
	}
}


package com.nokor.efinance.gui.ui.panel.contract.notes.sms;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * SMS form panel
 * @author uhout.cheng
 */
public class SMSFormPanel extends Panel implements ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 2980285971110784364L;
	
	private SMSTablePanel smsTablePanel;
	private Button btnAdd;
	
	/**
	 * 
	 * @param caption
	 */
	public SMSFormPanel(String caption) {
		setCaption(I18N.message(caption));
		init();
	}
	
	/**
	 * 
	 * @param icon
	 * @return
	 */
	private Button getButton(String caption, Resource icon) {
		Button button = new NativeButton(I18N.message(caption));
		button.setIcon(icon);
		button.addClickListener(this);
		return button;
	}
	
	/**
	 * 
	 */
	private void init() {
		btnAdd = getButton("new", FontAwesome.PLUS);
		
		smsTablePanel = new SMSTablePanel();
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		
		VerticalLayout contentLayout = new VerticalLayout();
		
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(smsTablePanel);
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(contentLayout);
	}

	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		smsTablePanel.assignValues(contract);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			smsTablePanel.displayPopup();
		} 
	}

}

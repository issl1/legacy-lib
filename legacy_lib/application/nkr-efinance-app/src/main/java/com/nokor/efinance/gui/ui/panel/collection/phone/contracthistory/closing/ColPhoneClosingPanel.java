package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.closing;

import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneClosingPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 8254682977661802477L;
	
	private ColPhoneClosingAssetPanel colPhoneClosingAssetPanel;
	private ColPhoneClosingLoanPanel colPhoneClosingLoanPanel;
	
	/**
	 * 
	 */
	public ColPhoneClosingPanel() {
		colPhoneClosingAssetPanel = new ColPhoneClosingAssetPanel();
		colPhoneClosingLoanPanel = new ColPhoneClosingLoanPanel();
		
		Panel assetPanel = getPanelCaptionColor("asset", colPhoneClosingAssetPanel, true);
		Panel loanPanel = getPanelCaptionColor("loan", colPhoneClosingLoanPanel, true);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(loanPanel);
		mainLayout.addComponent(assetPanel);
		
		addComponent(mainLayout);
		
	}
	
	/**
	 * 
	 * @param caption
	 * @param component
	 * @param isBorderPanel
	 * @return
	 */
	public Panel getPanelCaptionColor(String caption, Component component, boolean isBorderPanel) {
		Panel panel = new Panel(component);
		panel.setCaptionAsHtml(true);
		panel.setCaption("<h2 style=\"margin: 0; color: #398439\" >" + I18N.message(caption) + "</h2>");
		if (!isBorderPanel) {
			panel.setStyleName(Reindeer.PANEL_LIGHT);
		}
		return panel;
	}

}

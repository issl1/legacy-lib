package com.nokor.efinance.gui.ui.panel.contract.loan.summary.asset;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.gui.ui.panel.contract.insurance.InsurancesTablePanel;
import com.nokor.efinance.gui.ui.panel.contract.loan.LoanSummaryPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Reindeer;


/**
 * Main asset summary in Loan
 * @author uhout.cheng
 */
public class LoanAssetMainPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 5843754775973687872L;

	private LoanSummaryPanel loanSummaryPanel;
	
	private LoanAssetPanel loanAssetPanel;
	private LoanRegistrationPanel loanRegistrationPanel;
	private InsurancesTablePanel loanInsurancePanel;
	
	/**
	 * 
	 * @param loanSummaryPanel
	 */
	public LoanAssetMainPanel(LoanSummaryPanel loanSummaryPanel) {
		this.loanSummaryPanel = loanSummaryPanel;
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		
		loanAssetPanel = new LoanAssetPanel(loanSummaryPanel);
		loanRegistrationPanel = new LoanRegistrationPanel(loanSummaryPanel);
		loanInsurancePanel = new InsurancesTablePanel(null);
		Panel isrPanel = getPanelCaptionColor("insurance", loanInsurancePanel, false);
		
		setSpacing(true);
		addComponent(loanAssetPanel);
		addComponent(loanRegistrationPanel);
//		addComponent(isrPanel);
	}
	
	/**
	 * 
	 * @param caption
	 * @param component
	 * @param isBorderPanel
	 * @return
	 */
	private Panel getPanelCaptionColor(String caption, Component component, boolean isBorderPanel) {
		Panel panel = new Panel(component);
		panel.setCaptionAsHtml(true);
		panel.setCaption("<h2 style=\"margin: 0; color: #398439\" >" + I18N.message(caption) + "</h2>");
		if (!isBorderPanel) {
			panel.setStyleName(Reindeer.PANEL_LIGHT);
		}
		return panel;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assigValues(Contract contract) {
		reset();
		loanAssetPanel.assigValues(contract);
		loanRegistrationPanel.assigValues(contract);
		loanInsurancePanel.assignValues(contract);
	}
	
	/**
	 * 
	 */
	protected void reset() {
		loanAssetPanel.reset();
		loanRegistrationPanel.reset();
	}
	
}

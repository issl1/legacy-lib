package com.nokor.efinance.core.quotation.panel;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.quotation.QuotationProfileUtils;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * Underwriter panel
 * @author ly.youhort
 */
public class UnderwriterPanel extends AbstractTabPanel implements QuotationEntityField {
	
	private static final long serialVersionUID = -5682940523541469251L;
	
//	private ContractFormPanel quotationFormPanel;
	
	private VerticalLayout contentLayout;
	private TabSheet tabSheetUw;
	private ScoringPanel scoringPanel;
	private CreditBureauPanel applicantCreditBureauPanel;
	private CreditBureauPanel guarantorCreditBureauPanel;
	private VerticalLayout navigationPanel;
	
	private Quotation quotation;
	
	public UnderwriterPanel(/*ContractFormPanel quotationFormPanel*/) {
		super();
		setSizeFull();
//		this.quotationFormPanel = quotationFormPanel;
	}
	
	/**
	 * @return
	 */
	/*public ContractFormPanel getQuotationFormPanel() {
		return quotationFormPanel;
	}*/
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		contentLayout = new VerticalLayout();		
		tabSheetUw = new TabSheet();
		scoringPanel = new ScoringPanel(this);		
		applicantCreditBureauPanel = new CreditBureauPanel(EApplicantType.C);
		guarantorCreditBureauPanel = new CreditBureauPanel(EApplicantType.G);
		tabSheetUw.addTab(applicantCreditBureauPanel, I18N.message("applicant.credit"));
		tabSheetUw.addTab(guarantorCreditBureauPanel, I18N.message("guarantor.credit"));
		tabSheetUw.addTab(scoringPanel, I18N.message("underwriter"));		
		contentLayout.addComponent(tabSheetUw);
		
		tabSheetUw.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -382492569665690482L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				updateNavigationPanel(navigationPanel, null);
			}
		});
		
		return contentLayout;
	}
	
	/**
	 * Set quotation
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {
		this.quotation = quotation;
		scoringPanel.assignValues(quotation);
		applicantCreditBureauPanel.assignValues(quotation, EApplicantType.C);
		guarantorCreditBureauPanel.assignValues(quotation, EApplicantType.G);
	}
		
	/**
	 * Reset panel
	 */
	public void reset() {
	}
	
	@Override
	public void updateNavigationPanel(VerticalLayout navigationPanel, NavigationPanel defaultNavigationPanel) {
		this.navigationPanel = navigationPanel;
		navigationPanel.removeAllComponents();
		Component selectedTab = tabSheetUw.getSelectedTab();
		if (selectedTab == applicantCreditBureauPanel) {
			if (QuotationProfileUtils.isNavigationUnderwriterAvailable(quotation)) {
				navigationPanel.addComponent(applicantCreditBureauPanel.getNavigationPanel());
			}
			applicantCreditBureauPanel.assignValues(quotation, EApplicantType.C);
		} else if (selectedTab == guarantorCreditBureauPanel) {
			if (QuotationProfileUtils.isNavigationUnderwriterAvailable(quotation)) {
				navigationPanel.addComponent(guarantorCreditBureauPanel.getNavigationPanel());
			}
			guarantorCreditBureauPanel.assignValues(quotation, EApplicantType.G);
		} else if (selectedTab == scoringPanel) {
			if (QuotationProfileUtils.isNavigationUnderwriterAvailable(quotation)) {
				navigationPanel.addComponent(scoringPanel.getNavigationPanel());
			}
			scoringPanel.assignValues(quotation);
		}
	}
}

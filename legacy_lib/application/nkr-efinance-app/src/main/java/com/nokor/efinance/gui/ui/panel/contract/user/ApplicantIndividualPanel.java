package com.nokor.efinance.gui.ui.panel.contract.user;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.contact.panel.UserContactAddressPanel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.user.credit.history.UserCreditHistoryPanel;
import com.nokor.efinance.gui.ui.panel.contract.user.financial.UserFinancialPanel;
import com.nokor.efinance.gui.ui.panel.contract.user.phone.UserContactPhonePanel;
import com.nokor.efinance.gui.ui.panel.contract.user.reference.UserReferenceTable;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;


/**
 * User Panel in CM last version 
 * @author uhout.cheng
 */
public class ApplicantIndividualPanel extends AbstractTabPanel implements FinServicesHelper, SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = 3693113984688805350L;

	private TabSheet accordingTab;
	
	private VerticalLayout userContactLayout;
	private UserContactAddressPanel userContactAddressPanel;
	private UserContactPhonePanel userContactPhonePanel;
	
	private VerticalLayout userReferenceLayout;
	private UserReferenceTable userReferenceTable;
	
	private UserFinancialPanel userFinancialPanel;
	private UserGeneralPanel userGeneralPanel;
	private Applicant applicant;
	private UserCreditHistoryPanel creditHistoryPanel;
	
	private VerticalLayout contentLayout;
	private RefreshListener refreshListener;
	
	/**
	 * @return the userFinancialPanel
	 */
	public UserFinancialPanel getUserFinancialPanel() {
		return userFinancialPanel;
	}
	
	/**
	 * @return the accordingTab
	 */
	public TabSheet getAccordingTab() {
		return accordingTab;
	}
		
	/**
	 * @return the userReferenceTable
	 */
	public UserReferenceTable getUserReferenceTable() {
		return userReferenceTable;
	}

	/**
	 * @return the userContactLayout
	 */
	public VerticalLayout getUserContactLayout() {
		return userContactLayout;
	}

	/**
	 * @return the userReferenceLayout
	 */
	public VerticalLayout getUserReferenceLayout() {
		return userReferenceLayout;
	}

	/**
	 * @return the applicant
	 */
	public Applicant getApplicant() {
		return applicant;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		super.setMargin(false);
		accordingTab = new TabSheet();
		accordingTab.addSelectedTabChangeListener(this);
		userGeneralPanel = new UserGeneralPanel(this);
		userContactAddressPanel = new UserContactAddressPanel();
		userContactPhonePanel = new UserContactPhonePanel();
		userReferenceTable = new UserReferenceTable(this);
		userFinancialPanel = new UserFinancialPanel();
		creditHistoryPanel = new UserCreditHistoryPanel();
				
		userContactLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		userContactLayout.addComponent(userContactAddressPanel);
		userContactLayout.addComponent(userContactPhonePanel);
		userReferenceLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		userReferenceLayout.addComponent(userReferenceTable);
		accordingTab.addTab(userGeneralPanel, I18N.message("general"));
		accordingTab.addTab(userContactLayout, I18N.message("contact"));
		accordingTab.addTab(userReferenceLayout, I18N.message("references"));
		accordingTab.addTab(userFinancialPanel, I18N.message("financial"));
		accordingTab.addTab(creditHistoryPanel, I18N.message("credit.history"));
		
		contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
	
		contentLayout.addComponent(accordingTab);
		return contentLayout;
	}	
	
	
	/**
	 * 
	 * @param isVisible
	 */
	public void setVisibleContactTable(boolean isVisible) {
		userContactAddressPanel.setVisible(isVisible);
		//userContactPhoneTable.setVisible(isVisible);
	}
	
	/**
	 * 
	 * @param isVisible
	 */
	public void setVisibleControls(boolean isVisible) {
		contentLayout.setVisible(isVisible);
	}
	
	/** 
	 * @param contract
	 * @param summaryPanel
	 */
	public void assignValues(Applicant applicant) {
		super.removeErrorsPanel();
		userGeneralPanel.removeErrorsPanel();
		userFinancialPanel.removeErrorsPanel();
		this.applicant = applicant;
		if (this.applicant != null) {
			assignValuesSeletedTab();
		} else {
			userGeneralPanel.assignValues(applicant);
			accordingTab.setSelectedTab(userGeneralPanel);
		}
	}
		
	/**
	 * 
	 */
	public void refresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		userGeneralPanel.reset();
		userFinancialPanel.reset();
	}
	
	/**
	 * 
	 */
	public void createNewGuarantor() {
		userGeneralPanel.createNewGuarantor();
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		userGeneralPanel.removeErrorsPanel();
		userFinancialPanel.removeErrorsPanel();
		assignValuesSeletedTab();
	}
	
	/**
	 * 
	 */
	private void assignValuesSeletedTab() {
		if (this.applicant != null) {
			Individual individual = this.applicant.getIndividual();		
			if (accordingTab.getSelectedTab().equals(userGeneralPanel)) {
				userGeneralPanel.assignValues(applicant);				
			} else if (accordingTab.getSelectedTab().equals(userReferenceLayout)) {
				userReferenceTable.assignValues(individual);
			} else if (accordingTab.getSelectedTab().equals(userContactLayout)) {
				userContactAddressPanel.setIndividual(individual);
				userContactAddressPanel.assignValue();
				userContactPhonePanel.assignValue(individual);
			} else if (accordingTab.getSelectedTab().equals(userFinancialPanel)) {
				userFinancialPanel.assignValues(individual);
			} else if (accordingTab.getSelectedTab().equals(creditHistoryPanel)) {
				creditHistoryPanel.assignValues(applicant);
			}			
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void setContract(Contract contract) {
		userGeneralPanel.setContract(contract);
	}
	
	/**
	 * @return the refreshListener
	 */
	public RefreshListener getRefreshListener() {
		return refreshListener;
	}

	/**
	 * @param refreshListener the refreshListener to set
	 */
	public void setRefreshListener(RefreshListener refreshListener) {
		this.refreshListener = refreshListener;
	}

	/**
	 * @author bunlong.taing
	 */
	public interface RefreshListener {
		/** */
		void onRefresh();
	}
	
}

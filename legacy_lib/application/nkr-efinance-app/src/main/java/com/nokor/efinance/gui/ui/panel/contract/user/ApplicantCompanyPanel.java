package com.nokor.efinance.gui.ui.panel.contract.user;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;


/**
 * User Panel in CM last version 
 * @author buntha.chea
 */
public class ApplicantCompanyPanel extends AbstractTabPanel implements FinServicesHelper, SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = 3693113984688805350L;

	private TabSheet accordingTab;
	
	private VerticalLayout userContactLayout;
	private VerticalLayout userReferenceLayout;
	private UserCompanyGeneralPanel userCompanyGeneralPanel;
	private Applicant applicant;
	
	private UsersPanel usersPanel;
	
	private VerticalLayout contentLayout;
	
	/**
	 * @param usersPanel
	 */
	public ApplicantCompanyPanel(UsersPanel usersPanel) {
		this.usersPanel = usersPanel;
	}
	
	/**
	 * @return the accordingTab
	 */
	public TabSheet getAccordingTab() {
		return accordingTab;
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
		userCompanyGeneralPanel = new UserCompanyGeneralPanel(this);	
		userContactLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		accordingTab.addTab(userCompanyGeneralPanel, I18N.message("general"));
		contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
	
		contentLayout.addComponent(accordingTab);
		return contentLayout;
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
		userCompanyGeneralPanel.removeErrorsPanel();
		this.applicant = applicant;
		if (accordingTab.getSelectedTab() == userCompanyGeneralPanel) {
			userCompanyGeneralPanel.assignValues(applicant);
		} else {
			accordingTab.setSelectedTab(userCompanyGeneralPanel);
		}
	}
		
	public void refresh() {
		if (usersPanel != null) {
			usersPanel.refresh();
		}
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		userCompanyGeneralPanel.reset();
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		userCompanyGeneralPanel.removeErrorsPanel();
		if (applicant != null) {
			if (accordingTab.getSelectedTab().equals(userCompanyGeneralPanel)) {
				userCompanyGeneralPanel.assignValues(applicant);				
			} 
		}
	}	
}

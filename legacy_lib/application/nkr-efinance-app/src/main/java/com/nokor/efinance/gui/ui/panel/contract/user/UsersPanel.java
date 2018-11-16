package com.nokor.efinance.gui.ui.panel.contract.user;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.summary.SummaryPanel;
import com.nokor.efinance.gui.ui.panel.contract.user.ApplicantIndividualPanel.RefreshListener;
import com.nokor.efinance.gui.ui.panel.contract.user.driver.DriverInformationTab;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;


/**
 * User Panel in CM last version 
 * @author uhout.cheng
 */
public class UsersPanel extends AbstractTabPanel implements FinServicesHelper, RefreshListener, ClickListener {
	
	/** */
	private static final long serialVersionUID = 3693113984688805350L;

	private TabSheet tabUsers;
	
	private UserDetailTable userDetailTable;
	private ApplicantIndividualPanel applicantIndividualPanel;
	private ApplicantCompanyPanel applicantCompanyPanel;
	private DriverInformationTab driverInfoTab;
	private SummaryPanel summaryPanel;
	
	private VerticalLayout usersTab;
	private VerticalLayout driversTab;
	
	private Contract contract;
	
	private Button btnNewGuarantor;
	
	/**
	 * @param summaryPanel
	 */
	public UsersPanel(SummaryPanel summaryPanel) {
		this.summaryPanel = summaryPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		super.setMargin(false);
		btnNewGuarantor = ComponentLayoutFactory.getDefaultButton("new.guarantor", FontAwesome.PLUS_CIRCLE, 100);
		btnNewGuarantor.addClickListener(this);
		userDetailTable = new UserDetailTable(this);
		applicantIndividualPanel = new ApplicantIndividualPanel();
		applicantIndividualPanel.setRefreshListener(this);
		applicantCompanyPanel = new ApplicantCompanyPanel(this);
		driverInfoTab = new DriverInformationTab();
		
		VerticalLayout applicantPanel = new VerticalLayout();
		applicantPanel.addComponent(applicantIndividualPanel);
		applicantPanel.addComponent(applicantCompanyPanel);
		
		usersTab = ComponentLayoutFactory.getVerticalLayout(true, true);
		usersTab.addComponent(btnNewGuarantor);
		usersTab.addComponent(userDetailTable);
		usersTab.addComponent(applicantPanel);
		
		driversTab = ComponentLayoutFactory.getVerticalLayout(true, true);
		driversTab.addComponent(driverInfoTab);
		
		tabUsers = new TabSheet();
		tabUsers.addTab(usersTab, I18N.message("users"));
		tabUsers.addTab(driversTab, I18N.message("driver.info"));
		
		return tabUsers;
	}	
	
	/**
	 */
	public void refresh() {
		CONT_SRV.refresh(this.contract);
		summaryPanel.assignValues(contract);
		userDetailTable.assignValues(contract);
	}
	
	
	/**
	 * @param contract
	 * @param summaryPanel
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		userDetailTable.assignValues(contract);
		applicantIndividualPanel.setContract(this.contract);
		driverInfoTab.assignValues(this.contract);
		displayApplicant(contract.getApplicant());
	}

	
	/**
	 * @param applicant
	 */
	public void displayApplicant(Applicant applicant) {
		if (EApplicantCategory.COMPANY.equals(applicant.getApplicantCategory())) {
			applicantCompanyPanel.assignValues(applicant);
			applicantCompanyPanel.setVisibleControls(true);
			applicantIndividualPanel.setVisibleControls(false);
		} else if (EApplicantCategory.INDIVIDUAL.equals(applicant.getApplicantCategory())) {
			applicantIndividualPanel.assignValues(applicant);	
			applicantIndividualPanel.setVisibleControls(true);
			applicantCompanyPanel.setVisibleControls(false);
		}
		btnNewGuarantor.setVisible(!EApplicantCategory.COMPANY.equals(applicant.getApplicantCategory()));
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.contract.user.ApplicantIndividualPanel.RefreshListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		refresh();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnNewGuarantor)) {
			applicantIndividualPanel.createNewGuarantor();
			applicantIndividualPanel.reset();
			applicantIndividualPanel.assignValues(null);
		}
	}
}

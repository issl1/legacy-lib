package com.nokor.efinance.gui.ui.panel.applicant;
import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.user.ApplicantCompanyPanel;
import com.nokor.efinance.gui.ui.panel.contract.user.ApplicantIndividualPanel;
import com.nokor.efinance.gui.ui.panel.contract.user.ApplicantIndividualPanel.RefreshListener;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * Relationship panel
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ApplicantsPanel.NAME)
public class ApplicantsPanel extends AbstractTabsheetPanel implements View, FinServicesHelper, RefreshListener {

	private static final long serialVersionUID = -6175296765478766254L;

	public static final String NAME = "applicants";
	
	@Autowired
	private ApplicantTablePanel applicantTablePanel;
	private ApplicantIndividualPanel formPanel;
	private ApplicantCompanyPanel companyFormPanel;
	private VerticalLayout companyLayout;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		applicantTablePanel.setMainPanel(this);
		formPanel = new ApplicantIndividualPanel();
		formPanel.setSizeFull();
		formPanel.setMargin(true);
		formPanel.setCaption(I18N.message("applicant"));
		formPanel.setRefreshListener(this);
		
		companyFormPanel = new ApplicantCompanyPanel(null);
		
		companyLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		companyLayout.setCaption(I18N.message("applicant"));
		companyLayout.addComponent(companyFormPanel);
		
		getTabSheet().setTablePanel(applicantTablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		Applicant applicant = APP_SRV.getById(Applicant.class, applicantTablePanel.getItemSelectedId());
		if (EApplicantCategory.INDIVIDUAL.equals(applicant.getApplicantCategory())) {
			getTabSheet().addFormPanel(formPanel);
			initSelectedTab(formPanel);
		} else if (EApplicantCategory.COMPANY.equals(applicant.getApplicantCategory())) {
			getTabSheet().addFormPanel(companyLayout);
			initSelectedTab(companyLayout);
		}
		
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == formPanel) {
			formPanel.assignValues(APP_SRV.getById(Applicant.class, applicantTablePanel.getItemSelectedId()));
		} else if (selectedTab == companyLayout) {
			companyFormPanel.assignValues(APP_SRV.getById(Applicant.class, applicantTablePanel.getItemSelectedId()));
		} else if (selectedTab == applicantTablePanel && getTabSheet().isNeedRefresh()) {
			applicantTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		String appId = event.getParameters();
		if (StringUtils.isNotEmpty(appId)) {
			Applicant app = APP_SRV.getById(Applicant.class, new Long(appId));
			getTabSheet().setForceSelected(true);
			if (EApplicantCategory.INDIVIDUAL.equals(app.getApplicantCategory())) {
				getTabSheet().addFormPanel(formPanel);
				formPanel.assignValues(app);
				getTabSheet().setSelectedTab(formPanel);
			} else if (EApplicantCategory.COMPANY.equals(app.getApplicantCategory())) {
				getTabSheet().addFormPanel(companyLayout);
				companyFormPanel.assignValues(app);
				getTabSheet().setSelectedTab(companyLayout);
			}
		} else {
			formPanel.reset();
			companyFormPanel.reset();
		}
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.contract.user.ApplicantIndividualPanel.RefreshListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		getTabSheet().setNeedRefresh(true);
	}
	
}

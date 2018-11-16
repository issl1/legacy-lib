package com.nokor.efinance.gui.ui.panel.dashboard;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.panel.ApplicantPanel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.panel.comment.CommentsPanel;
import com.nokor.efinance.gui.ui.panel.report.contract.overdue.ContractDocumentsPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * DashboardFormPanel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DashboardMainFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 8671040639198651779L;
	
	private ApplicantPanel applicantPanel;
	private CommentsPanel commentsPanel;
	private ContractDocumentsPanel contractDocumentsPanel;
	
	private TabSheet accordionPanel;
	private Contract contract;
	
	/**
	 * Post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
        super.init();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		accordionPanel = new TabSheet();
		applicantPanel = new ApplicantPanel();
		commentsPanel = new CommentsPanel();
		contractDocumentsPanel = new ContractDocumentsPanel();
		
		accordionPanel.addTab(applicantPanel, I18N.message("applicant"));
		accordionPanel.addTab(contractDocumentsPanel, I18N.message("collection.documents"));
		accordionPanel.addTab(commentsPanel, I18N.message("comments"));
		
		contentLayout.addComponent(accordionPanel);
		
		return contentLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		return null;
	}
	
	/**
	 * Assign value
	 * @param id
	 */
	public void assignValues(Long id) {
		accordionPanel.setSelectedTab(applicantPanel);
		contract = ENTITY_SRV.getById(Contract.class, id);
		Quotation quotation = contract.getQuotation();
		applicantPanel.assignValues(quotation.getApplicant());
		applicantPanel.setApplicantEnabled(false, true);
		
		commentsPanel.assignValues(quotation);
		contractDocumentsPanel.assignValues(contract);
	}

}

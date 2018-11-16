package com.nokor.efinance.gui.ui.panel.contract.loan.summary.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.gl.finwiz.share.domain.registration.RegistrationBook;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.asset.model.MAsset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.gui.ui.panel.contract.loan.LoanSummaryPanel;
import com.nokor.efinance.third.finwiz.client.reg.ClientRegistration;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;


/**
 * Asset registration summary in Loan
 * @author uhout.cheng
 */
public class LoanRegistrationPanel extends AbstractControlPanel implements MAsset {

	/** */
	private static final long serialVersionUID = -6681006120650958617L;
	
	private static final String TEMPLATE = "loan/summary/summaryRegistrationLayout";
	
	private LoanSummaryPanel loanSummaryPanel;
	
	private Label lblBorrowerStatusValue;
	private Label lblBorrowerFullNameValue;
	private Label lblNumberOfReferencesValue;
	private Label lblNumberOfGuarantorsValue;
	
//	private Label lblAOMTaxValidationDateValue;
//	private Label lblAOMTaxExpiryDateValue;
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public LoanRegistrationPanel(LoanSummaryPanel loanSummaryPanel) {
		this.loanSummaryPanel = loanSummaryPanel;
		init();
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(getDefaultString(value));
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabel() {
		Label label = ComponentFactory.getLabel(null, ContentMode.HTML);
		label.setSizeUndefined();
		return label;	
	}
	
	/**
	 * 
	 */
	private void init() {
//		lblAOMTaxValidationDateValue = getLabel();
//		lblAOMTaxExpiryDateValue = getLabel();
		
		lblBorrowerStatusValue = getLabel();
		lblBorrowerFullNameValue = getLabel();
		lblNumberOfReferencesValue = getLabel();
		lblNumberOfGuarantorsValue = getLabel();
		
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		verLayout.addComponent(getCustomLayout());
//		verLayout.addComponent(simpleTable);
		
		Panel panel = loanSummaryPanel.getPanelCaptionColor("borrower", verLayout, true);
		addComponent(panel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assigValues(Contract contract) {
		reset();
		RegistrationBook regBook = ClientRegistration.getRegBookByContractReference(contract.getReference());
		if (regBook != null) {
//			lblAOMTaxValidationDateValue.setValue(getDescription(getDateFormat(regBook.getTaxValidatedDate())));
//			lblAOMTaxExpiryDateValue.setValue(getDescription(getDateFormat(regBook.getTaxExpirationDate())));
//			if (regBook.getTaxStatus() != null) {
//				lblAOMTaxStatusValue.setValue(getDescription(regBook.getTaxStatus().name()));
//			}
		}
		
		Applicant applicant = null;
		ContractSimulation contractSimulation = ContractUtils.getLastContractSimulation(contract.getId());
		if (contractSimulation != null) {
			applicant = contractSimulation.getApplicant();
		} else {
			applicant = contract.getApplicant();
		}
		
		int nbOfReference = 0;
		if (applicant != null) {
			lblBorrowerFullNameValue.setValue(getDescription(applicant.getNameLocale()));
			Individual individual = applicant.getIndividual();
			lblBorrowerStatusValue.setValue(getDescription(individual == null ? "" : individual.getWkfStatus().getDescLocale()));
			if (individual != null) {
				List<IndividualReferenceInfo> infos = individual.getIndividualReferenceInfos();
				if (infos != null) {
					nbOfReference = infos.size();
				}
			}
		}
		lblNumberOfReferencesValue.setValue(getDescription(getDefaultString(nbOfReference)));
		lblNumberOfGuarantorsValue.setValue(getDescription(getDefaultString(MyNumberUtils.getInteger(contract.getNumberGuarantors()))));
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(OPERATION, I18N.message("operation"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("creation.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEADLINE, I18N.message("deadline"), Date.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getCustomLayout() {
		CustomLayout customLayout = loanSummaryPanel.getCustomLayout(TEMPLATE);
		customLayout.addComponent(ComponentFactory.getLabel("borrower.status"), "lblBorrowerStatus");
		customLayout.addComponent(lblBorrowerStatusValue, "lblBorrowerStatusValue");
		customLayout.addComponent(ComponentFactory.getLabel("borrower.fullname"), "lblBorrowerFullName");
		customLayout.addComponent(lblBorrowerFullNameValue, "lblBorrowerFullNameValue");
		customLayout.addComponent(ComponentFactory.getLabel("number.of.references"), "lblNumberOfReferences");
		customLayout.addComponent(lblNumberOfReferencesValue, "lblNumberOfReferencesValue");
		customLayout.addComponent(ComponentFactory.getLabel("number.of.guarantors"), "lblNumberOfGuarantors");
		customLayout.addComponent(lblNumberOfGuarantorsValue, "lblNumberOfGuarantorsValue");
		return customLayout;
	}
	
	/**
	 * 
	 */
	protected void reset() {
//		lblAOMTaxValidationDateValue.setValue("");
//		lblAOMTaxExpiryDateValue.setValue("");
		lblBorrowerStatusValue.setValue("");
		lblBorrowerFullNameValue.setValue("");
		lblNumberOfReferencesValue.setValue("");
		lblNumberOfGuarantorsValue.setValue("");
	}
	
}

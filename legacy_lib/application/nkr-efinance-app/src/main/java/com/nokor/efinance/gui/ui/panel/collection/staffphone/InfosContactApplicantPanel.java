package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.ContractFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * Contact information for applicant panel in collection phone staff
 * @author uhout.cheng
 */
public class InfosContactApplicantPanel extends AbstractControlPanel implements ClickListener {

	/** */
	private static final long serialVersionUID = 7070330962102801886L;
	
	private Label lblLesseeFullName;
	private Label lblLesseePrimaryPhone;
	private Label lblLesseeOtherPhone;
	private Label lblGuarantorFullName;
	private Label lblGuarantorPrimaryPhone;
	private Label lblGuarantorOtherPhone;
	private Label lblDPD;
	private Label lblAPD;
	private Label lblODM;
	private Label lblNPD;
	private Label lblLPD;
	private Label lblDueDate;
	
	private Button btnOpenContract;
	
	private Contract contract;
	
	private ContractFormPanel contractFormPanel;
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = ComponentFactory.getHtmlLabel(null);
		label.setSizeUndefined();
		return label;
	}
	
	/**
	 * 
	 */
	public InfosContactApplicantPanel() {
		setWidth(550, Unit.PIXELS);

		lblLesseeFullName = getLabelValue();
		lblLesseePrimaryPhone = getLabelValue();
		lblLesseeOtherPhone = getLabelValue();
		lblGuarantorFullName = getLabelValue();
		lblGuarantorPrimaryPhone = getLabelValue();
		lblGuarantorOtherPhone = getLabelValue();
		lblDPD = getLabelValue();
		lblAPD = getLabelValue();
		lblODM = getLabelValue();
		lblNPD = getLabelValue();
		lblLPD = getLabelValue();
		lblDueDate = getLabelValue();
		
		btnOpenContract =  ComponentLayoutFactory.getButtonStyle("open.contract", FontAwesome.EXTERNAL_LINK, 100, "btn btn-success button-small");
		btnOpenContract.addClickListener(this);
		
		GridLayout leftGrid = getLeftLayout();
		GridLayout rightGrid = getRightLayout();
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, false);
		horLayout.setWidth(530, Unit.PIXELS);
		horLayout.addComponent(leftGrid);
		horLayout.addComponent(rightGrid);
		horLayout.setComponentAlignment(rightGrid, Alignment.TOP_RIGHT);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(btnOpenContract);
		verticalLayout.addComponent(horLayout);
		verticalLayout.setComponentAlignment(btnOpenContract, Alignment.TOP_RIGHT);
		
		Panel mainPanel = new Panel(verticalLayout);
		addComponent(mainPanel);
	}
	
	/**
	 * Right grid layout
	 * @return
	 */
	private GridLayout getRightLayout() {
		GridLayout gridLayout = new GridLayout(10, 6);
		gridLayout.setMargin(new MarginInfo(true, false, false, false));
		gridLayout.setSpacing(true);
		
		Label lblDPDTitle = getLabelCaption("dpd");
		Label lblAPDTitle = getLabelCaption("apd");
		Label lblODMTitle = getLabelCaption("odm");
		Label lblNPDTitle = getLabelCaption("npd");
		Label lblLPDTitle = getLabelCaption("lpd");
		Label lblDueDateTitle = getLabelCaption("due.date");
		
		int iCol = 0;
		gridLayout.addComponent(lblDPDTitle, iCol++, 0);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 0);
		gridLayout.addComponent(lblDPD, iCol++, 0);
		iCol = 0;
		gridLayout.addComponent(lblAPDTitle, iCol++, 1);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 1);
		gridLayout.addComponent(lblAPD, iCol++, 1);
		iCol = 0;
		gridLayout.addComponent(lblODMTitle, iCol++, 2);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 2);
		gridLayout.addComponent(lblODM, iCol++, 2);
		iCol = 0;
		gridLayout.addComponent(lblLPDTitle, iCol++, 3);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 3);
		gridLayout.addComponent(lblLPD, iCol++, 3);
		iCol = 0;
		gridLayout.addComponent(lblNPDTitle, iCol++, 4);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 4);
		gridLayout.addComponent(lblNPD, iCol++, 4);
		iCol = 0;
		gridLayout.addComponent(lblDueDateTitle, iCol++, 5);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 5);
		gridLayout.addComponent(lblDueDate, iCol++, 5);
		
		gridLayout.setComponentAlignment(lblDPDTitle, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblAPDTitle, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblODMTitle, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblLPDTitle, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblNPDTitle, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblDueDateTitle, Alignment.MIDDLE_RIGHT);
		return gridLayout;
	}
	
	/**
	 * Left grid layout
	 * @return
	 */
	private GridLayout getLeftLayout() {
		GridLayout gridLayout = new GridLayout(10, 6);
		gridLayout.setMargin(new MarginInfo(true, false, false, true));
		gridLayout.setSpacing(true);
		
		Label lblLesseeFullNameTitle = getLabelCaption("lessee.fullname");
		Label lblGuarantorFullNameTitle = getLabelCaption("guarantor.fullname");
		Label lblLesseePrimaryPhoneTitle = getLabelCaption("primary.phone.no");
		Label lblGuarantorPrimaryPhoneTitle = getLabelCaption("primary.phone.no");
		Label lblLesseeOtherPhoneTitle = getLabelCaption("other.phone.no");
		Label lblGuarantorOtherPhoneTitle = getLabelCaption("other.phone.no");
		
		int iCol = 0;
		gridLayout.addComponent(lblLesseeFullNameTitle, iCol++, 0);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 0);
		gridLayout.addComponent(lblLesseeFullName, iCol++, 0);
		iCol = 0;
		gridLayout.addComponent(lblLesseePrimaryPhoneTitle, iCol++, 1);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 1);
		gridLayout.addComponent(lblLesseePrimaryPhone, iCol++, 1);
		iCol = 0;
		gridLayout.addComponent(lblLesseeOtherPhoneTitle, iCol++, 2);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 2);
		gridLayout.addComponent(lblLesseeOtherPhone, iCol++, 2);
		iCol = 0;
		gridLayout.addComponent(lblGuarantorFullNameTitle, iCol++, 3);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 3);
		gridLayout.addComponent(lblGuarantorFullName, iCol++, 3);
		iCol = 0;
		gridLayout.addComponent(lblGuarantorPrimaryPhoneTitle, iCol++, 4);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 4);
		gridLayout.addComponent(lblGuarantorPrimaryPhone, iCol++, 4);
		iCol = 0;
		gridLayout.addComponent(lblGuarantorOtherPhoneTitle, iCol++, 5);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 5);
		gridLayout.addComponent(lblGuarantorOtherPhone, iCol++, 5);
		
		gridLayout.setComponentAlignment(lblLesseeFullNameTitle, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblGuarantorFullNameTitle, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblLesseePrimaryPhoneTitle, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblGuarantorPrimaryPhoneTitle, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblLesseeOtherPhoneTitle, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblGuarantorOtherPhoneTitle, Alignment.MIDDLE_RIGHT);
		return gridLayout;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value == null ? "" : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		resetControls();
		Map<EApplicantType, Applicant> applicants = new LinkedHashMap<>();
		applicants.put(EApplicantType.C, contract.getApplicant());
		List<ContractApplicant> contractApplicants = contract.getContractApplicants();
		if (contractApplicants != null && !contractApplicants.isEmpty()) {
			for (ContractApplicant contractApplicant : contractApplicants) {
				if (EApplicantType.G.equals(contractApplicant.getApplicantType())) {
					applicants.put(EApplicantType.G, contractApplicant.getApplicant());
				}
			}
		}
		if (!applicants.isEmpty()) {
			for (EApplicantType key : applicants.keySet()) {
				Applicant applicant = applicants.get(key);
				Individual individual = applicant.getIndividual();
				Company company = applicant.getCompany();
				if (EApplicantType.C.equals(key)) {
					lblLesseeFullName.setValue(getDescription(applicant.getNameEn()));
					if (individual != null) {
						lblLesseePrimaryPhone.setValue(getDescription(individual.getIndividualPrimaryContactInfo()));
						lblLesseeOtherPhone.setValue(getDescription(individual.getIndividualSecondaryContactInfo()));
					} else {
						lblLesseePrimaryPhone.setValue(getDescription(company.getTel()));
						lblLesseeOtherPhone.setValue(getDescription(company.getMobile()));
					}
				} else if (EApplicantType.G.equals(key)) {
					lblGuarantorFullName.setValue(getDescription(applicant.getNameEn()));
					if (individual != null) {
						lblGuarantorPrimaryPhone.setValue(getDescription(individual.getIndividualPrimaryContactInfo()));
						lblGuarantorOtherPhone.setValue(getDescription(individual.getIndividualSecondaryContactInfo()));
					} else {
						lblGuarantorPrimaryPhone.setValue(getDescription(company.getTel()));
						lblGuarantorOtherPhone.setValue(getDescription(company.getMobile()));
					}
				}
			}
		}
		Collection collection = contract.getCollection();
		if (collection != null) {
			String dpd = getDefaultString(MyNumberUtils.getInteger(collection.getNbOverdueInDays()));
			String apd = getDefaultString(MyNumberUtils.getDouble(collection.getTiTotalAmountInOverdue()));
			String odm = getDefaultString(MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue()));
			
			lblDPD.setValue(getDescription(dpd));
			lblAPD.setValue(getDescription(apd));
			lblODM.setValue(getDescription(odm));
			lblLPD.setValue(getDescription(getDateFormat(collection.getLastPaymentDate())));
			lblNPD.setValue(getDescription(getDateFormat(collection.getNextDueDate())));
		}
		lblDueDate.setValue(getDescription(getDateFormat(contract.getFirstDueDate())));
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : "N/A";
	}
	
	/**
	 * 
	 */
	private void resetControls() {
		lblLesseeFullName.setValue("");
		lblLesseePrimaryPhone.setValue("");
		lblLesseeOtherPhone.setValue("");
		lblGuarantorFullName.setValue("");
		lblGuarantorPrimaryPhone.setValue("");
		lblGuarantorOtherPhone.setValue("");
		lblDPD.setValue("");
		lblAPD.setValue("");
		lblODM.setValue("");
		lblLPD.setValue("");
		lblNPD.setValue("");
		lblDueDate.setValue("");
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabelCaption(String caption) {
		Label label = ComponentFactory.getLabel(caption);
		label.setSizeUndefined();
		return label;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnOpenContract) {
			Window window = new Window(I18N.message("contract"));
			window.setModal(true);
			window.setResizable(true);
			window.setWidth(90, Unit.PERCENTAGE);
			window.setHeight(100, Unit.PERCENTAGE);
			
			contractFormPanel = new ContractFormPanel();
			contractFormPanel.assignValues(contract.getId(), true);
			
			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.addComponent(contractFormPanel);
			
			Panel panel = new Panel(verticalLayout);
			panel.setStyleName(Reindeer.PANEL_LIGHT);
			
			window.setContent(panel);
			UI.getCurrent().addWindow(window);
		}
		
	}
}

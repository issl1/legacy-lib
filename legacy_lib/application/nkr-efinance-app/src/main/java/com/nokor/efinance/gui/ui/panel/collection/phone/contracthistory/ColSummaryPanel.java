package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.service.ContractFlagRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.contract.ContractsPanel;
import com.nokor.efinance.gui.ui.panel.contract.asset.registration.popup.AssetRegistrationDetailPopupPanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * Collection phone summary panel
 * @author uhout.cheng
 */
public class ColSummaryPanel extends AbstractTabPanel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 2069539745947050324L;

	private Button btnContractId;
	private Label lblTransferInfo;
	private Label lblTitle;
	private Label lblDOB;
	private Label lblOccupation;
	private Label lblDPD;
	private Label lblAPD;
	private Label lblODIODM;
	private Label lblNextDueDate;
	private Label lblBrand;
	private Label lblModel;
	
	private Label lblInstallment;
	private Label lblTerms;
	private Label lblLastDueDate;
	private Label lblFirstDueDate;
	private Label lblPrefix;
	private Label lblFullName;
	private Label lblNickName;
	private Label lblPIRI;
	private Label lblPFIRFI;
	private Label lblLastPaymentDateLastPaidAmount;
	private Button btnSerie;
	private Label lblColor;
	private Label lblRegistrationNo;
	private Label lblStatus;
	private Button btnRegistration;
	private Label lblSerie;
	private Label lblLoanAmount;
	private Label lblColStaff;
	
	private Contract contract;
	
	/*private Label lblPaidTerms;
	private Label lblARBalance;
	private Label lblLastPaymentAmount;
	private Label lblBalancePenalty;
	private Label lblBalanceColFee;
	private Label lblBalanceOperation;
	private Label lblAOMTaxStatus;
	private Label lblPrimaryPhoneNO;
	private Label lblRemainingTerms;*/
	
	/**
	 * 
	 */
	public ColSummaryPanel() {
		super.setMargin(false);
		super.setSpacing(false);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		return getSummaryDetailPanel();
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
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		return DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH);
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getSummaryDetailPanel() {
		btnContractId = new Button();
		btnContractId.setStyleName(Reindeer.BUTTON_LINK);
		btnContractId.addClickListener(this);
		lblTransferInfo = getLabelValue();
		lblTitle = getLabelValue();
		lblDOB = getLabelValue();
		lblOccupation = getLabelValue();
		lblDPD = getLabelValue();
		lblAPD = getLabelValue();
		lblNextDueDate = getLabelValue();
		lblBrand = getLabelValue();
		lblModel = getLabelValue();
		lblModel.setWidthUndefined();
		lblODIODM = getLabelValue();
		lblLastPaymentDateLastPaidAmount = getLabelValue();
		
		lblInstallment = getLabelValue();
		lblTerms = getLabelValue();
		lblLastDueDate = getLabelValue();
		lblFirstDueDate = getLabelValue();
		lblPrefix = getLabelValue();
		lblFullName = getLabelValue();
		lblNickName = getLabelValue();
		lblPIRI = getLabelValue();
		lblPFIRFI = getLabelValue();
		lblSerie = getLabelValue();
		
		btnSerie = new Button();
		btnSerie.setStyleName(Reindeer.BUTTON_LINK);
		btnSerie.addClickListener(this);
		
		btnRegistration = new Button();
		btnRegistration.setStyleName(Reindeer.BUTTON_LINK);
		btnRegistration.addClickListener(this);
		
		lblColor = getLabelValue();
		lblRegistrationNo = getLabelValue();
		lblStatus = getLabelValue();
		
		lblLoanAmount = getLabelValue();
		
		lblColStaff = getLabelValue();
		
/*		lblARBalance = getLabelValue();	
		lblRemainingTerms = getLabelValue();
		lblPaidTerms = getLabelValue();
		lblPrimaryPhoneNO = getLabelValue();
		lblLastPaymentAmount = getLabelValue();
		lblBalancePenalty = getLabelValue();
		lblBalanceColFee = getLabelValue();
		lblBalanceOperation = getLabelValue();*/
		
		Label lblContractIDTitle = getLabel("contract.id");
		Label lblBrandTitle = getLabel("asset.make");
		Label lblLesseeTitle = getLabel("title");
		Label lblDPDTitle = getLabel("dpd");
		Label lblModelTitle = getLabel("asset.range");
		Label lblDOBTitle = getLabel("dob");
		Label lblAPDTitle = getLabel("apd");
		Label lblOccupationTitle = getLabel("occupation");
		Label lblNextDueDateTitle = getLabel("next.due.date");
		Label lblODIODMTitle = getLabel("odi.odm");
		Label lblLoanAmountTitle = getLabel("loan.amount");
		
		Label lblInstallmentTitle = getLabel("installment");
		Label lblTermsTitle = getLabel("terms");
		Label lblLastDueDateTitle = getLabel("last.due.date");
		Label lblFirstDueDateTitle = getLabel("first.due.date");
		Label lblPrefixTitle = getLabel("prefix");
		Label lblFullNameTitle = getLabel("fullname");
		Label lblNickNameTitle = getLabel("nickname");
		Label lblPIRITitle = getLabel("pi.ri");
		Label lblPFIRFITitle = getLabel("pfi.rfi");
		Label lblLastPaymentDateLastPaidAmountTitle = getLabel("lpd.lpa");
		Label lblSerieTitle = getLabel("serie");
		Label lblColorTitle = getLabel("color");
		Label lblRegistrationNoTitle = getLabel("registration.no");
		Label lblStatusTitle = getLabel("status");
		
		Label lblColStaffTitle = getLabel("staff.in.charge");
		
/*		Label lblODMTitle = getLabel("odm");
		Label lblLastPaymentDateTitle = getLabel("last.payment.date");
		Label lblARBalanceTitle = getLabel("ar.balance");
		Label lblRemainingTermsTitle = getLabel("remaining.terms");
		Label lblPaidTermsTitle = getLabel("paid.terms");
		Label lblBalancePenaltyTitle = getLabel("balance.penalty");
		Label lblAOMTaxStatusTitle = getLabel("aom.tax.status");
		Label lblPrimaryPhoneNOTitle = getLabel("primary.phone.no");
		Label lblODITitle = getLabel("odi");
		Label lblBalanceColFeeTitle = getLabel("balance.collection.fee");
		Label lblBalanceOperationTitle = getLabel("balance.operation");
		Label lblLastPaymentAmountTitle = getLabel("last.payment.amount");*/
		
		GridLayout gridLayout = new GridLayout(14, 7);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		int iCol = 0;
		gridLayout.addComponent(lblContractIDTitle, iCol++, 0);
		gridLayout.addComponent(btnContractId, iCol++, 0);
		gridLayout.addComponent(lblTransferInfo, iCol++, 0);
		gridLayout.addComponent(lblLesseeTitle, iCol++, 0);
		gridLayout.addComponent(lblTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblODIODMTitle, iCol++, 0);
		gridLayout.addComponent(lblODIODM, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblBrandTitle, iCol++, 0);
		gridLayout.addComponent(lblBrand, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblColStaffTitle, iCol++, 0);
		gridLayout.addComponent(lblColStaff, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblTermsTitle, iCol++, 1);
		gridLayout.addComponent(lblTerms, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblPrefixTitle, iCol++, 1);
		gridLayout.addComponent(lblPrefix, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblPIRITitle, iCol++, 1);
		gridLayout.addComponent(lblPIRI, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblModelTitle, iCol++, 1);
		gridLayout.addComponent(lblModel, iCol++, 1);
		
		iCol = 0;
		gridLayout.addComponent(lblInstallmentTitle, iCol++, 2);
		gridLayout.addComponent(lblInstallment, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(lblDOBTitle, iCol++, 2);
		gridLayout.addComponent(lblDOB, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(lblPFIRFITitle, iCol++, 2);
		gridLayout.addComponent(lblPFIRFI, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(lblSerieTitle, iCol++, 2);
		gridLayout.addComponent(lblSerie, iCol++, 2);
		
		iCol = 0;
		gridLayout.addComponent(lblFirstDueDateTitle, iCol++, 3);
		gridLayout.addComponent(lblFirstDueDate, iCol++, 3);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 3);
		gridLayout.addComponent(lblFullNameTitle, iCol++, 3);
		gridLayout.addComponent(lblFullName, iCol++, 3);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 3);
		gridLayout.addComponent(lblDPDTitle, iCol++, 3);
		gridLayout.addComponent(lblDPD, iCol++, 3);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 3);
		gridLayout.addComponent(lblColorTitle, iCol++, 3);
		gridLayout.addComponent(lblColor, iCol++, 3);
		
		iCol = 0;
		gridLayout.addComponent(lblLastDueDateTitle, iCol++, 4);
		gridLayout.addComponent(lblLastDueDate, iCol++, 4);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 4);
		gridLayout.addComponent(lblNickNameTitle, iCol++, 4);
		gridLayout.addComponent(lblNickName, iCol++, 4);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 4);
		gridLayout.addComponent(lblAPDTitle, iCol++, 4);
		gridLayout.addComponent(lblAPD, iCol++, 4);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 4);
		gridLayout.addComponent(lblRegistrationNoTitle, iCol++, 4);
		gridLayout.addComponent(btnRegistration, iCol++, 4);
		
		iCol = 0;
		gridLayout.addComponent(lblLoanAmountTitle, iCol++, 5);
		gridLayout.addComponent(lblLoanAmount, iCol++, 5);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 5);
		gridLayout.addComponent(lblOccupationTitle, iCol++, 5);
		gridLayout.addComponent(lblOccupation, iCol++, 5);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 5);
		gridLayout.addComponent(lblLastPaymentDateLastPaidAmountTitle, iCol++, 5);
		gridLayout.addComponent(lblLastPaymentDateLastPaidAmount, iCol++, 5);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 5);
		gridLayout.addComponent(lblStatusTitle, iCol++, 5);
		gridLayout.addComponent(lblStatus, iCol++, 5);
		
		iCol = 6;
		gridLayout.addComponent(lblNextDueDateTitle, iCol++, 6);
		gridLayout.addComponent(lblNextDueDate, iCol++, 6);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 6);
		
		/*iCol = 0;
		gridLayout.addComponent(lblOccupationTitle, iCol++, 4);
		gridLayout.addComponent(lblOccupation, iCol++, 4);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 4);
		gridLayout.addComponent(lblNextDueDateTitle, iCol++, 4);
		gridLayout.addComponent(lblNextDueDate, iCol++, 4);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 4);
		gridLayout.addComponent(lblBalanceOperationTitle, iCol++, 4);
		gridLayout.addComponent(lblBalanceOperation, iCol++, 4);
		
		iCol = 0;
		gridLayout.addComponent(lblInstallmentTitle, iCol++, 5);
		gridLayout.addComponent(lblInstallment, iCol++, 5);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 5);
		gridLayout.addComponent(lblTermsTitle, iCol++, 5);
		gridLayout.addComponent(lblTerms, iCol++, 5);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 5);
		gridLayout.addComponent(lblARBalanceTitle, iCol++, 5);
		gridLayout.addComponent(lblARBalance, iCol++, 5);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 5);
		gridLayout.addComponent(lblRemainingTermsTitle, iCol++, 5);
		gridLayout.addComponent(lblRemainingTerms, iCol++, 5);
		
		iCol = 0;
		gridLayout.addComponent(lblPaidTermsTitle, iCol++, 6);
		gridLayout.addComponent(lblPaidTerms, iCol++, 6);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 6);
		gridLayout.addComponent(lblLastDueDateTitle, iCol++, 6);
		gridLayout.addComponent(lblLastDueDate, iCol++, 6);
		
		gridLayout.setComponentAlignment(lblPrimaryPhoneNOTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblODMTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblODITitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblLastPaymentDateTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblLastPaymentAmountTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblBalancePenaltyTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblBalanceColFeeTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblBalanceOperationTitle, Alignment.TOP_RIGHT);
		
		gridLayout.setComponentAlignment(lblAOMTaxStatusTitle, Alignment.TOP_RIGHT);
		
		gridLayout.setComponentAlignment(lblInstallmentTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblARBalanceTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblRemainingTermsTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblPaidTermsTitle, Alignment.TOP_RIGHT);*/
	
		gridLayout.setComponentAlignment(lblLoanAmountTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblOccupationTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblDPDTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblAPDTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblBrandTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblModelTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblLastDueDateTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblTermsTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblContractIDTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblLesseeTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblDOBTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblStatusTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblRegistrationNoTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblSerieTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblColorTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblLastPaymentDateLastPaidAmountTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblPIRITitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblPFIRFITitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblODIODMTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblFullNameTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblNickNameTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblPrefixTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblFirstDueDateTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblNextDueDateTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblInstallmentTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblColStaffTitle, Alignment.TOP_RIGHT);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(gridLayout);
	
		Panel panel = new Panel(verticalLayout);
		return panel;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDOBFormat(Date date) {
		StringBuffer buffer = new StringBuffer();
		if (date != null) {
			int year = DateUtils.getNumberYearOfTwoDates(DateUtils.today(), date);
			buffer.append(getDateFormat(date));
			buffer.append(StringUtils.SPACE);
			buffer.append("(");
			buffer.append(getDefaultString(year));
			buffer.append(StringUtils.SPACE);
			buffer.append(I18N.message("year.old"));
			buffer.append(")");
		}
		return buffer.toString();
	}
		
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		this.contract = contract;
		btnContractId.setCaption(contract.getReference());
		String transferDesc = StringUtils.EMPTY;
		if (ContractUtils.isTranfered(contract)) {
			transferDesc = "(T)";
		}
		lblTransferInfo.setValue(getDescription(transferDesc));
		
		Applicant applicant = null;
		if (ContractUtils.isPendingTransfer(contract)) {
			ContractSimulation contractSimulation = ContractUtils.getLastContractSimulation(contract.getId());
			if (contractSimulation != null) {
				applicant = contractSimulation.getApplicant();
			}
		} else {
			applicant = contract.getApplicant();
		}
		
		if (applicant != null) {
			Individual individual = applicant.getIndividual();
			Company company = applicant.getCompany();
			
			lblFullName.setValue(getDescription(applicant.getNameLocale()));
			
			if (individual != null) {
				lblDOB.setValue(getDescription(getDOBFormat(individual.getBirthDate())));
//				lblPrimaryPhoneNO.setValue(getDescription(individual.getIndividualPrimaryContactInfo()));
				lblTitle.setValue(getDescription(individual.getTitle() != null ? individual.getTitle().getDescLocale() : ""));
				lblPrefix.setValue(getDescription(individual.getCivility() != null ? individual.getCivility().getDescLocale() : ""));
				lblNickName.setValue(getDescription(individual.getNickName() != null ? individual.getNickName() : ""));
				
				Employment employment = individual.getCurrentEmployment();
				if (employment != null) {
					String occupation = employment.getEmploymentIndustry() != null ? employment.getEmploymentIndustry().getDescLocale() : "";
					lblOccupation.setValue(getDescription(occupation));
				}
			} else {
				lblDOB.setValue(getDescription(getDOBFormat(company.getStartDate())));
//				lblPrimaryPhoneNO.setValue(getDescription(company.getTel()));
			}
		}
		
		Collection collection = contract.getCollection();
		if (collection != null) {
			String dpd = getDefaultString(MyNumberUtils.getInteger(collection.getNbOverdueInDays()));
			String apd = getDefaultString(MyNumberUtils.formatDoubleToString(
					MyNumberUtils.getDouble(collection.getTiTotalAmountInOverdue()), "###,##0.00"));
			String odm = getDefaultString(MyNumberUtils.getInteger(collection.getDebtLevel()));
//			String balancePenalty = getDefaultString(MyNumberUtils.getDouble(collection.getTiPenaltyAmount()));
			String odi = getDefaultString(MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue()));
			
			double pi = 0;
			double ri = 0;
			pi = MyNumberUtils.getDouble(collection.getPartialPaidInstallment());
			ri = contract.getTerm() - pi;
			
			String paidInstallment = AmountUtils.format(pi);
			String remainingInstallment = AmountUtils.format(ri);
			
			int pfi = 0;
			int rfi = 0;
			pfi = MyNumberUtils.getInteger(collection.getNbInstallmentsPaid());
			rfi = contract.getTerm() - pfi;
			
			lblDPD.setValue(getDescription(dpd));
			lblAPD.setValue(getDescription(apd));
			lblNextDueDate.setValue(getDescription(getDateFormat(collection.getNextDueDate())));
			lblODIODM.setValue(getDescription(odi) + "/" + getDescription(odm));
			lblPIRI.setValue(getDescription(paidInstallment) + "/" + getDescription(remainingInstallment));
			lblPFIRFI.setValue(getDescription(getDefaultString(pfi)) + "/" + getDescription(getDefaultString(rfi)));
			if (collection.getLastPaymentDate() != null && collection.getTiLastPaidAmount() != null) {
				lblLastPaymentDateLastPaidAmount.setValue(getDescription(getDateFormat(collection.getLastPaymentDate()))
						+ "/" + getDescription(MyNumberUtils.formatDoubleToString(
								MyNumberUtils.getDouble(collection.getTiLastPaidAmount()), "###,##0.00")));
			}
			
			/*lblLastPaymentDate.setValue(getDescription(getDateFormat(collection.getLastPaymentDate())));
			lblBalancePenalty.setValue(getDescription(balancePenalty));
			lblLastPaymentAmount.setValue(StringUtils.EMPTY);
			lblBalanceColFee.setValue(StringUtils.EMPTY);
			lblBalanceOperation.setValue(StringUtils.EMPTY);
			s
			lblPaidTerms.setValue(getDescription(getDefaultString(collection.getNbInstallmentsPaid())));
		
			double arBalance = collection.getTiBalanceCapital() + collection.getTiBalanceInterest();
			lblARBalance.setValue(getDescription(getDefaultString(AmountUtils.format(arBalance))));*/
			
			SecUser collectionUser = COL_SRV.getCollectionUser(contract.getId());
			if (collectionUser != null) {
				lblColStaff.setValue(getDescription(collectionUser.getDesc()));
			}
		}
		
		Asset asset = contract.getAsset();
		if (asset != null) {
			lblBrand.setValue(getDescription(asset.getBrandDescLocale()));
			lblModel.setValue(getDescription(asset.getModelDescLocale()));
			AssetModel assModel = asset.getModel();
			if (assModel != null) {
//				btnSerie.setCaption(assModel.getDescLocale());
				lblSerie.setValue(getDescription(assModel.getSerie()));
			}
			lblColor.setValue(getDescription(asset.getColor() != null ? asset.getColor().getDescLocale() : ""));
			btnRegistration.setCaption((getDefaultString(asset.getPlateNumber())));
		}
		
		lblTerms.setValue(getDescription(getDefaultString(contract.getTerm())));
		lblLastDueDate.setValue(getDescription(getDateFormat(contract.getLastDueDate())));
		lblFirstDueDate.setValue(getDescription(getDateFormat(contract.getFirstDueDate())));
		lblInstallment.setValue(getDescription(MyNumberUtils.formatDoubleToString(
				MyNumberUtils.getDouble(contract.getTiInstallmentAmount()), "###,##0.00")));
		
		double loanAmount = 0d;
		loanAmount = MyNumberUtils.getDouble(contract.getLoanAmount() != null ? contract.getLoanAmount().getTiAmount() : null);
		
		lblLoanAmount.setValue(getDescription(MyNumberUtils.formatDoubleToString(loanAmount, "###,##0.00")));
		
		getAssetStatus(contract);
		
//		int nbbInstallmentsPaid = MyNumberUtils.getInteger(collection.getNbInstallmentsPaid());
//		lblRemainingTerms.setValue(getDescription(getDefaultString((contract.getTerm() - nbbInstallmentsPaid))));
		/*RegistrationBook regBook = ClientRegistration.getRegBookByContractReference(contract.getReference());
		 * 
		if (regBook != null) {
			if (regBook.getTaxStatus() != null) {
				lblAOMTaxStatus.setValue(getDefaultString(regBook.getTaxStatus().name()));
			}
		}*/
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	public String getAssetStatus(Contract contract) {
		ContractFlagRestriction restrictions = new ContractFlagRestriction();
		restrictions.setCompleted(true);
		restrictions.setConId(contract.getId());
		List<ContractFlag> contractFlags = ENTITY_SRV.list(restrictions);
		if (contractFlags != null && !contractFlags.isEmpty()) {
			ContractFlag contractFlag = contractFlags.get(0);
			if (contractFlag != null) {
				lblStatus.setValue(getDescription(contractFlag.getFlag() != null ? contractFlag.getFlag().getDesc() : StringUtils.EMPTY));
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		btnContractId.setCaption(StringUtils.EMPTY);
		lblTransferInfo.setValue(StringUtils.EMPTY);
		lblTitle.setValue(StringUtils.EMPTY);
		lblDOB.setValue(StringUtils.EMPTY);
		lblOccupation.setValue(StringUtils.EMPTY);
		lblDPD.setValue(StringUtils.EMPTY);
		lblAPD.setValue(StringUtils.EMPTY);
		lblNextDueDate.setValue(StringUtils.EMPTY);
		lblBrand.setValue(StringUtils.EMPTY);
		lblModel.setValue(StringUtils.EMPTY);

		lblInstallment.setValue(StringUtils.EMPTY);
		lblTerms.setValue(StringUtils.EMPTY);
		lblLastDueDate.setValue(StringUtils.EMPTY);
		lblFirstDueDate.setValue(StringUtils.EMPTY);
		btnSerie.setCaption(StringUtils.EMPTY);
		lblStatus.setValue(StringUtils.EMPTY);
		lblODIODM.setValue(StringUtils.EMPTY);
		lblLastPaymentDateLastPaidAmount.setValue(StringUtils.EMPTY);
		lblPrefix.setValue(StringUtils.EMPTY);
		lblPIRI.setValue(StringUtils.EMPTY);
		lblPFIRFI.setValue(StringUtils.EMPTY);
		lblColor.setValue(StringUtils.EMPTY);
		lblRegistrationNo.setValue(StringUtils.EMPTY);
		lblFullName.setValue(StringUtils.EMPTY);
		lblNickName.setValue(StringUtils.EMPTY);
		lblLoanAmount.setValue(StringUtils.EMPTY);
		lblColStaff.setValue(StringUtils.EMPTY);
		
		/*lblARBalance.setValue(StringUtils.EMPTY);
		lblRemainingTerms.setValue(StringUtils.EMPTY);
		lblPaidTerms.setValue(StringUtils.EMPTY);
		lblPrimaryPhoneNO.setValue(StringUtils.EMPTY);
		lblBalancePenalty.setValue(StringUtils.EMPTY);
		lblBalanceColFee.setValue(StringUtils.EMPTY);
		lblBalanceOperation.setValue(StringUtils.EMPTY);
		lblAOMTaxStatus.setValue(StringUtils.EMPTY);*/
	}
	
	/**
	 * POPUP Message
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnContractId) {
			Contract contra = CONT_SRV.getByReference(btnContractId.getCaption());
			Page.getCurrent().setUriFragment("!" + ContractsPanel.NAME + "/" + contra.getId());
		} else if (event.getButton() == btnRegistration) {
			AssetRegistrationDetailPopupPanel assetRegistrationDetailPopupPanel = new AssetRegistrationDetailPopupPanel();
			assetRegistrationDetailPopupPanel.assignValues(contract);
			UI.getCurrent().addWindow(assetRegistrationDetailPopupPanel);
		}
	}
}

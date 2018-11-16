package com.nokor.efinance.core.quotation.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jibx.runtime.JiBXException;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.DocumentConfirmEvidence;
import com.nokor.efinance.core.document.model.DocumentScoring;
import com.nokor.efinance.core.document.model.DocumentUwGroup;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.model.QuotationExtModule;
import com.nokor.efinance.core.quotation.model.QuotationSupportDecision;
import com.nokor.efinance.core.quotation.panel.comment.CommentFormPanel;
import com.nokor.efinance.core.shared.conf.UWScoreConfig;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.quotation.QuotationProfileUtils;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.third.creditbureau.cbc.XmlBinder;
import com.nokor.efinance.third.creditbureau.cbc.model.response.AccDetail;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Consumer;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Employer;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Response;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Underwriter panel
 * @author ly.youhort
 */
public class ManagementPanel extends AbstractTabPanel implements QuotationEntityField, ClickListener, FrmkServicesHelper {
		
	private static final long serialVersionUID = 8148756640029148875L;
	
	private static final ThemeResource greenIcon = new ThemeResource("icons/32/green.png");
	private static final ThemeResource grayIcon = new ThemeResource("icons/32/gray.png");
	
	private QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
	
//	private ContractManagementFormPanel quotationFormPanel;
	
	private Quotation quotation;
	
	private VerticalLayout managementLayout;
	private List<FinService> services;
		
	private Button btnBackUw;
	private Button btnApprove;
	private Button btnReject;
	
	public ManagementPanel(/*ContractManagementFormPanel quotationFormPanel*/) {
		super();
		setSizeFull();
//		this.quotationFormPanel = quotationFormPanel;
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		managementLayout = new VerticalLayout();
		managementLayout.setSizeFull();
		managementLayout.setMargin(true);
		managementLayout.setSpacing(true);
				
		btnBackUw = new NativeButton(I18N.message("back.underwriter"));
		btnBackUw.addClickListener(this);
		btnBackUw.setIcon(new ThemeResource("../nkr-default/icons/16/previous.png"));
		
		btnApprove = new NativeButton(I18N.message("approve"));
		btnApprove.addClickListener(this);
		btnApprove.setIcon(new ThemeResource("../nkr-default/icons/16/tick.png"));
				
		btnReject = new NativeButton(I18N.message("reject"));
		btnReject.setIcon(new ThemeResource("../nkr-default/icons/16/error.png"));
		btnReject.addClickListener(this);
		
		return managementLayout;
	}
		
	
	/**
	 * Set quotation
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {
		
		managementLayout.removeAllComponents();
		
		this.quotation = quotation;
		
		Applicant applicant = quotation.getApplicant();
		Individual individual = applicant.getIndividual();
		Address applicantAddress = individual.getMainAddress();
		Applicant guarantorApplicant = quotation.getGuarantor();
		boolean guarantorRequired = quotationService.isGuarantorRequired(quotation);
		Employment applicantEmployment = individual.getCurrentEmployment();
		Employment applicantSecondEmployment = null;
		List<Employment> secondEmployments = individual.getEmployments(EEmploymentType.SECO);
		if (secondEmployments != null && !secondEmployments.isEmpty()) {
			applicantSecondEmployment = secondEmployments.get(0);
		}
						
		double applicantInstallment = calTotalInstallmentAmount(quotation);//quotation.getTiInstallmentUsd();
		double applicantRevenus = 0d;
		double applicantAllowance = 0d;
		double applicantBusinessExpenses = 0d;
		double applicantPersonalExpenses = MyNumberUtils.getDouble(individual.getMonthlyPersonalExpenses());
		double applicantFamilyExpenses = MyNumberUtils.getDouble(individual.getMonthlyFamilyExpenses());
		double applicantDebtInstallment = MyNumberUtils.getDouble(individual.getTotalDebtInstallment());
		List<Employment> employments = individual.getEmployments();
		for (Employment employment : employments) {
			applicantRevenus += MyNumberUtils.getDouble(employment.getRevenue()) ;
			applicantAllowance += MyNumberUtils.getDouble(employment.getAllowance()) ;
			applicantBusinessExpenses += MyNumberUtils.getDouble(employment.getBusinessExpense());
		}
		double applicantNetIncome = applicantRevenus + applicantAllowance - applicantBusinessExpenses;
		double applicantDisposableIncome = applicantNetIncome - applicantPersonalExpenses - applicantFamilyExpenses - applicantDebtInstallment ;
		double applicantRatio = applicantDisposableIncome / applicantInstallment;
		
		double applicantFcRevenus = MyNumberUtils.getDouble(quotation.getCoRevenuEstimation());
		double applicantFcAllowance = MyNumberUtils.getDouble(quotation.getCoAllowanceEstimation());
		double applicantFcNetIncome = MyNumberUtils.getDouble(quotation.getCoNetIncomeEstimation());
		double applicantFcBusinessExpenses = MyNumberUtils.getDouble(quotation.getCoBusinessExpensesEstimation());
		double applicantFcPersonalExpenses = MyNumberUtils.getDouble(quotation.getCoPersonalExpensesEstimation());
		double applicantFcFamilyExpenses = MyNumberUtils.getDouble(quotation.getCoFamilyExpensesEstimation());
		double applicantFcDebtInstallment = MyNumberUtils.getDouble(quotation.getCoLiabilityEstimation());
		double applicantFcDisposableIncome = applicantFcNetIncome - applicantFcPersonalExpenses - applicantFcFamilyExpenses - applicantFcDebtInstallment;
		double applicantFcRatio = applicantFcDisposableIncome / applicantInstallment;
		
		
		double applicantUwRevenus = MyNumberUtils.getDouble(quotation.getUwRevenuEstimation());
		double applicantUwAllowance = MyNumberUtils.getDouble(quotation.getUwAllowanceEstimation());
		double applicantUwNetIncome = MyNumberUtils.getDouble(quotation.getUwNetIncomeEstimation());
		double applicantUwBusinessExpenses = MyNumberUtils.getDouble(quotation.getUwBusinessExpensesEstimation());
		double applicantUwPersonalExpenses = MyNumberUtils.getDouble(quotation.getUwPersonalExpensesEstimation());
		double applicantUwFamilyExpenses = MyNumberUtils.getDouble(quotation.getUwFamilyExpensesEstimation());
		double applicantUwDebtInstallment = MyNumberUtils.getDouble(quotation.getUwLiabilityEstimation());
		double applicantUwDisposableIncome = applicantUwNetIncome - applicantUwPersonalExpenses - applicantUwFamilyExpenses - applicantUwDebtInstallment;
		double applicantUwRatio = applicantUwDisposableIncome / applicantInstallment;
		
		QuotationExtModule quotationExtModule = null;
		if (quotation.getQuotationExtModules() != null && !quotation.getQuotationExtModules().isEmpty()) {
			quotationExtModule = quotation.getQuotationExtModules().get(0);
		}
		
		Double applicantCbDebtInstallment = 0.0;
		Double applicantCbRevenus = 0.0;
		
		boolean existingCb = false;
		if (quotationExtModule != null && StringUtils.isNotEmpty(quotationExtModule.getResult())) {
			try {
				Response response = XmlBinder.unmarshal(quotationExtModule.getResult());
				Consumer consumer = response.getMessage().getItems().get(0).getRspReport().getConsumer();
				List<AccDetail> accDetails = consumer.getAccDetails();
				if (accDetails != null) {
					for (AccDetail accDetail : accDetails) {
						if (!"C".equals(accDetail.getAccstatus())) {
							if ("KHR".equals(accDetail.getAcccurr())) {
								applicantCbDebtInstallment += accDetail.getAccinstl() / 4000;
							} else {
								applicantCbDebtInstallment += accDetail.getAccinstl();
							}
						}
					}
				}
				
				List<Employer> employers = consumer.getEmployers();
				if (employers != null) {
					for (Employer employer : employers) {
						if ("KHR".equals(employer.getEcurr())) {
							applicantCbRevenus += MyNumberUtils.getDouble(employer.getEtms()) / 4000;
						} else {
							applicantCbRevenus += MyNumberUtils.getDouble(employer.getEtms());
						}
					}
				}
//					existingCb = true;

			} catch (JiBXException e) {
			}
		}
		if (applicantCbDebtInstallment > 0) {
			existingCb = true;
		}
		Double totalInstallmentAmount = calTotalInstallmentAmount(quotation);
		Double applicantCbNetIncome = applicantCbRevenus;
		double applicantCbDisposableIncome = applicantCbNetIncome - applicantCbDebtInstallment - applicantPersonalExpenses - applicantFamilyExpenses;
		double applicantCbRatio = applicantCbDisposableIncome / applicantInstallment;
		
		int nbHousehold = 1;
		double netIncomeHousehold = applicantNetIncome;
		//String householdValue = "Applicant is " + applicant.getMaritalStatus().getDesc() + " and the household is composed of " + nbHousehold + " people totalizing a net income of " + netIncomeHousehold;
		
		String symbol = "$";
//		Currency currency = DataReference.getInstance().getCurrencyByDefault();
//		if (currency != null && StringUtils.isNotEmpty(currency.getSymbol())) {
//			symbol = currency.getSymbol();
//		} 
		
		Map<Long, Long> timing = getTiming(quotation);
				
		String template = guarantorRequired ? "management" : "management_no_guarantor";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout inputFieldLayout = null;
		try {
			inputFieldLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		inputFieldLayout.setSizeFull();
		inputFieldLayout.addComponent(new Label(I18N.message("purchased.at")), "lblPurchase");
		inputFieldLayout.addComponent(new Label(I18N.message(quotation.getDealer().getNameEn() + " on " + quotation.getQuotationDate())), "lblPurchaseValue");
		//table1
		inputFieldLayout.addComponent(new Label(I18N.message("lease.type")), "lblLeaseType");
		inputFieldLayout.addComponent(new Label(I18N.message("advance.payment.percentage")), "lblAdvancePaymentPercentage");
		inputFieldLayout.addComponent(new Label(AmountUtils.format(quotation.getAdvancePaymentPercentage()) + "%"), "lblAdvancePaymentPercentageValue");
		inputFieldLayout.addComponent(new Label(I18N.message("lease.amount")), "lblLeaseAmount");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(MyNumberUtils.getDouble(quotation.getTiFinanceAmount()))), "lblLeaseAmountValue");
		inputFieldLayout.addComponent(new Label(I18N.message("term.month")), "lblTermMonth");
		inputFieldLayout.addComponent(new Label(I18N.message(quotation.getTerm().toString())), "lblTermMonthValue");
		inputFieldLayout.addComponent(new Label(I18N.message("installment.amount")), "lblInstallmentAmount");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(totalInstallmentAmount)), "lblInstallmentAmountValue");
		//table2
		inputFieldLayout.addComponent(new Label(I18N.message("people.charge")), "lblPeopleCharge");
		inputFieldLayout.addComponent(new Label(I18N.message("co")), "lblCo");
		inputFieldLayout.addComponent(new Label(I18N.message(quotation.getCreditOfficer().getDesc())), "lblCoValue");
		inputFieldLayout.addComponent(new Label(I18N.message("po")), "lblPo");
		inputFieldLayout.addComponent(new Label(I18N.message(quotation.getProductionOfficer() != null ? quotation.getProductionOfficer().getDesc() : "")), "lblPoValue");
		inputFieldLayout.addComponent(new Label(I18N.message("uw")), "lblUw");
		inputFieldLayout.addComponent(new Label(I18N.message(quotation.getUnderwriter() != null ? quotation.getUnderwriter().getDesc() : "")), "lblUwValue");
		inputFieldLayout.addComponent(new Label(I18N.message("uw.supervisor")), "lblUwSupervisor");
		inputFieldLayout.addComponent(new Label(I18N.message(quotation.getUnderwriterSupervisor() != null ? quotation.getUnderwriterSupervisor().getDesc() : "")), "lblUwSupervisorValue");
		inputFieldLayout.addComponent(new Label(I18N.message("fc")), "lblFc");
		inputFieldLayout.addComponent(new Label(I18N.message(quotation.getFieldCheck() != null ? quotation.getFieldCheck().getDesc() : "")), "lblFcValue");
		
		//table3
		inputFieldLayout.addComponent(new Label(I18N.message("timing")), "lblTiming");
		inputFieldLayout.addComponent(new Label(getTime(getTotalTiming(timing))), "lblTotalTimingValie");
		inputFieldLayout.addComponent(new Label(I18N.message("co")), "lblTimingCo");
		inputFieldLayout.addComponent(new Label(getTime(timing.get(FMProfile.CO))), "lblTimingCoValue");
		inputFieldLayout.addComponent(new Label(I18N.message("uw")), "lblTimingUw");
		inputFieldLayout.addComponent(new Label(getTime(timing.get(FMProfile.UW))), "lblTimingUwValue");
		inputFieldLayout.addComponent(new Label(I18N.message("uw.supervisor")), "lblTimingUwSupervisor");
		inputFieldLayout.addComponent(new Label(getTime(timing.get(FMProfile.US))), "lblTimingUwSupervisorValue");
		inputFieldLayout.addComponent(new Label(I18N.message("ma")), "lblTimingMa");
		inputFieldLayout.addComponent(new Label(getTime(timing.get(FMProfile.MA))), "lblTimingMaValue");
		//block2
		inputFieldLayout.addComponent(new Label(I18N.message("name.en")), "lblName");
		inputFieldLayout.addComponent(new Label(individual.getLastNameEn() + " " + individual.getFirstNameEn()), "lblNameValue");
		inputFieldLayout.addComponent(new Label(I18N.message("gender")), "lblGender");
		inputFieldLayout.addComponent(new Label(I18N.message(individual.getGender().getDesc())), "lblGenderValue");
		inputFieldLayout.addComponent(new Label(I18N.message("age")), "lblApplicantAge");
		inputFieldLayout.addComponent(new Label("" + DateUtils.getAge(individual.getBirthDate()) + " " + I18N.message("years")), "lblApplicantAgeValue");
		inputFieldLayout.addComponent(new Label(I18N.message("housing")), "lblpropertyAddress");
		inputFieldLayout.addComponent(new Label(applicantAddress.getProperty() != null ? applicantAddress.getProperty().getDesc() : ""), "lblHouseholdValue");
		inputFieldLayout.addComponent(new Label(individual.getMaritalStatus().getDesc()), "lblMaritalStatus");
		inputFieldLayout.addComponent(new Label(I18N.message("number.household")), "lblNumberOfHousehold");
		inputFieldLayout.addComponent(new Label(""+ nbHousehold), "lblNumberOfHouseholdValue");
		inputFieldLayout.addComponent(new Label(I18N.message("total.net.income")), "lblTotalNetIncome");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(netIncomeHousehold)), "lblTotalNetIncomeValue");
		inputFieldLayout.addComponent(new Label(I18N.message("occupation")), "lblOccupation");
		inputFieldLayout.addComponent(new Label(applicantEmployment.getPosition()), "lblOccupationValue1");
		inputFieldLayout.addComponent(new Label(I18N.message("status")), "lblStatus");
		inputFieldLayout.addComponent(new Label(applicantEmployment.getEmploymentStatus().getDescEn()), "lblStatusValue1");
		inputFieldLayout.addComponent(new Label(I18N.message("employment.industry")), "lblEmploymentIndustryApplicant");
		inputFieldLayout.addComponent(new Label(applicantEmployment.getEmploymentIndustry() != null ? applicantEmployment.getEmploymentIndustry().getDescEn() : ""), "lblEmploymentIndustryApplicantValue1");
				
		if (applicantSecondEmployment != null) {
			inputFieldLayout.addComponent(new Label(applicantSecondEmployment.getEmploymentIndustry() != null ? applicantSecondEmployment.getEmploymentIndustry().getDescEn() : ""), "lblEmploymentIndustryApplicantValue2");
			inputFieldLayout.addComponent(new Label(applicantSecondEmployment.getPosition()), "lblOccupationValue2");
			inputFieldLayout.addComponent(new Label(applicantSecondEmployment.getEmploymentStatus() != null ? applicantSecondEmployment.getEmploymentStatus().getDescEn() : ""), "lblStatusValue2");
		} else {
			inputFieldLayout.addComponent(new Label(I18N.message("")), "lblEmploymentIndustryApplicantValue2");
			inputFieldLayout.addComponent(new Label(I18N.message("")), "lblOccupationValue2");
			inputFieldLayout.addComponent(new Label(I18N.message("")), "lblStatusValue2");
		}
		inputFieldLayout.addComponent(new Label(I18N.message("cbc.report.available")), "lblCBCReportAvailable");
		CheckBox cbCBCReportAvailable = new CheckBox();
		cbCBCReportAvailable.setValue(existingCb);
		cbCBCReportAvailable.setStyleName("checkbox_unchange_disabled_color");
		inputFieldLayout.addComponent(cbCBCReportAvailable, "cbCBCReportAvailable");
		inputFieldLayout.addComponent(new Label(I18N.message("existing.installments")), "lblExistingInstallments");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantCbDebtInstallment)), "lblExistingInstallmentsValue");
		
		double errorMarginCbInstallment = MyNumberUtils.getDouble(applicantCbDebtInstallment 
				* UWScoreConfig.getInstance().getConfiguration().getDouble("score.consistence.installment[@factor]"));
		if ((applicantCbDebtInstallment - errorMarginCbInstallment) <= applicantDebtInstallment
				&& applicantDebtInstallment <= (applicantCbDebtInstallment + errorMarginCbInstallment)) {
			inputFieldLayout.addComponent(new Label(I18N.message("corresponding to the amount stated by the customer")), "lblExistingInstallmentsValue1");
		} else {
			inputFieldLayout.addComponent(new Label(I18N.message("different from the amount stated by the customer")), "lblExistingInstallmentsValue1");
		}
		
		inputFieldLayout.addComponent(new Label(I18N.message("field.check")), "lblFieldCheck");
		CheckBox cbFieldCheck = new CheckBox();
		cbFieldCheck.setValue(quotation.isFieldCheckPerformed());
		cbFieldCheck.setStyleName("checkbox_unchange_disabled_color");
		inputFieldLayout.addComponent(cbFieldCheck, "cbFieldCheck");
		
		inputFieldLayout.addComponent(new Label(I18N.message("field.check.request.by.uw")), "lblFieldCheckRequestedByUw");
		inputFieldLayout.addComponent(getRequestedFieldCheck(quotation), "fieldCheckRequestedByUwGrid");
		
		inputFieldLayout.addComponent(new Label(I18N.message("base.salary.total.sales")), "lblRevenue");
		inputFieldLayout.addComponent(new Label(I18N.message("allowance")), "lblAllowance");
		inputFieldLayout.addComponent(new Label(I18N.message("business.expenses")), "lblBusinessExpenses");
		inputFieldLayout.addComponent(new Label(I18N.message("net.income")), "lblNetIncome1");
		inputFieldLayout.addComponent(new Label(I18N.message("personal.expenses")), "lblPersonalExpenses");
		inputFieldLayout.addComponent(new Label(I18N.message("family.expenses")), "lblFamilyExpenses");
		inputFieldLayout.addComponent(new Label(I18N.message("liability")), "lblLiability");
		inputFieldLayout.addComponent(new Label(I18N.message("disposable.income")), "lblDisposableIncome");
		inputFieldLayout.addComponent(new Label(I18N.message("installment")), "lblInstallment");
		inputFieldLayout.addComponent(new Label(I18N.message("ratio")), "lblRatio");
		
		inputFieldLayout.addComponent(new Label(I18N.message("co.interview.info")), "lblCoInterviewInfo");
		inputFieldLayout.addComponent(new Label(I18N.message("field.check.estimation")), "lblFieldCheckEstimation");
		inputFieldLayout.addComponent(new Label(I18N.message("uw.estimation")), "lblUwEstimation");
		inputFieldLayout.addComponent(new Label(I18N.message("credit.bureau.report")), "lblCbcInfo");
		
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantRevenus)), "lblApplicantRevenu");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantAllowance)), "lblApplicantAllowance");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantBusinessExpenses)), "lblApplicantBusinessExpenses");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantNetIncome)), "lblApplicantNetIncome");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantPersonalExpenses)), "lblApplicantPersonalExpenses");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantFamilyExpenses)), "lblApplicantFamilyExpenses");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantDebtInstallment)), "lblApplicantDebInstallment");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantDisposableIncome)), "lblApplicantDisposableIncome");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantInstallment)), "lblApplicantInstallment1");
		inputFieldLayout.addComponent(new Label(AmountUtils.format(applicantRatio)), "lblApplicantRatio");
		
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantFcRevenus)), "lblApplicantFcRevenus");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantFcAllowance)), "lblApplicantFcAllowance");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantFcBusinessExpenses)), "lblApplicantFcBusinessExpenses");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantFcNetIncome)), "lblApplicantFcNetIncome");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantFcPersonalExpenses)), "lblApplicantFcPersonalExpenses");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantFcFamilyExpenses)), "lblApplicantFcFamilyExpenses");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantFcDebtInstallment)), "lblApplicantFcDebtInstallment");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantFcDisposableIncome)), "lblApplicantFcDisposableIncome");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantInstallment)), "lblApplicantInstallment2");
		inputFieldLayout.addComponent(new Label(AmountUtils.format(applicantFcRatio)), "lblApplicantFcRatio");
		
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantUwRevenus)), "lblApplicantUwRevenus");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantUwAllowance)), "lblApplicantUwAllowance");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantUwBusinessExpenses)), "lblApplicantUwBusinessExpenses");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantUwNetIncome)), "lblApplicantUwNetIncome");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantUwPersonalExpenses)), "lblApplicantUwPersonalExpenses");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantUwFamilyExpenses)), "lblApplicantUwFamilyExpenses");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantUwDebtInstallment)), "lblApplicantUwDebtInstallment");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantUwDisposableIncome)), "lblApplicantUwDisposableIncome");
		inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantInstallment)), "lblApplicantInstallment3");
		inputFieldLayout.addComponent(new Label(AmountUtils.format(applicantUwRatio)), "lblApplicantUwRatio");
		
		if (existingCb) {
			inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantCbRevenus)), "lblApplicantCbRevenus");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbAllowance");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbBusinessExpenses");
			inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantCbRevenus)), "lblApplicantCbNetIncome");
			inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantPersonalExpenses)), "lblApplicantCbPersonalExpenses");
			inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantFamilyExpenses)), "lblApplicantCbFamilyExpenses");
			inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantCbDebtInstallment)), "lblApplicantCbDebtInstallment");
			inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantCbDisposableIncome)), "lblApplicantCbDisposableIncome");
			inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(applicantInstallment)), "lblApplicantInstallment4");
			inputFieldLayout.addComponent(new Label(AmountUtils.format(applicantCbRatio)), "lblApplicantCbRatio");
		} else {
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbRevenus");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbAllowance");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbBusinessExpenses");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbNetIncome");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbPersonalExpenses");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbFamilyExpenses");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbDebtInstallment");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbDisposableIncome");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantInstallment4");
			inputFieldLayout.addComponent(new Label("NA"), "lblApplicantCbRatio");
		}
				
		inputFieldLayout.addComponent(new Label(I18N.message("uw.comment.estimation")), "lblUWCommentEstimation");
		inputFieldLayout.addComponent(new Label(I18N.message("*Write down the reason of estimation")), "lblUWCommentEstimationValue");
		inputFieldLayout.addComponent(new Label(I18N.message("evidences")), "lblEvidences");
		List<DocumentUwGroup> documentUwGroups = quotationService.list(DocumentUwGroup.class);
		if (documentUwGroups != null && !documentUwGroups.isEmpty()) {
			VerticalLayout documentsGridLayout = getDocumentsGridLayout(quotation, documentUwGroups, EApplicantType.C);
			inputFieldLayout.addComponent(documentsGridLayout, "applicantDocumentsGridLayout");
		} else {
			inputFieldLayout.addComponent(new VerticalLayout(), "applicantDocumentsGridLayout");
		}
		
		if (guarantorRequired) {
			Individual guarantor = guarantorApplicant.getIndividual();
			Employment guarantorSecondEmployment = null;
			List<Employment> secondEmploymentsGuarantors = guarantor.getEmployments(EEmploymentType.SECO);
			if (secondEmploymentsGuarantors != null && !secondEmploymentsGuarantors.isEmpty()) {
				guarantorSecondEmployment = secondEmploymentsGuarantors.get(0);
			}
			
			Address guarantorAddress = guarantor.getMainAddress();
			Employment guarantorEmployment = guarantor.getCurrentEmployment();
			inputFieldLayout.addComponent(new Label(I18N.message("guarantor")), "lblGuarantor");
			inputFieldLayout.addComponent(new Label(I18N.message("relationship")), "lblRelationship");
			inputFieldLayout.addComponent(new Label(quotation.getQuotationApplicant(EApplicantType.G).getRelationship().getDescEn()), "lblGuarantorRelationship");
			inputFieldLayout.addComponent(new Label(I18N.message("live.with.applicant")), "lblLiveWithApplicant");
			CheckBox cbLiveWithApplicant = new CheckBox();
			cbLiveWithApplicant.setValue(quotation.getQuotationApplicants().get(1).isSameApplicantAddress());
			cbLiveWithApplicant.setStyleName("checkbox_unchange_disabled_color");
			
			inputFieldLayout.addComponent(cbLiveWithApplicant, "cbLiveWithApplicant");
			inputFieldLayout.addComponent(new Label(I18N.message("gender")), "lblGender");
			inputFieldLayout.addComponent(new Label(guarantor.getGender().getDesc()), "lblGuarantorGender");
			inputFieldLayout.addComponent(new Label(I18N.message("Age")), "lblGuarantorAge");
			inputFieldLayout.addComponent(new Label("" + DateUtils.getAge(guarantor.getBirthDate()) + " " + I18N.message("years")), "lblGuarantorAgeValue");
			inputFieldLayout.addComponent(new Label(I18N.message("housing")), "lblGuarantorpropertyAddress");
			inputFieldLayout.addComponent(new Label(guarantorAddress.getProperty() != null ? guarantorAddress.getProperty().getDesc() : ""), "lblGuarantorHouseholdValue");
			inputFieldLayout.addComponent(new Label(I18N.message("main.occupation")), "lblMainOccupation");
			inputFieldLayout.addComponent(new Label(guarantorEmployment.getPosition()), "lblGuarantorMainOccupation");
			
			inputFieldLayout.addComponent(new Label(I18N.message("employment.industry")), "lblEmploymentIndustryGuarantor");
			inputFieldLayout.addComponent(new Label(guarantorEmployment.getEmploymentIndustry() != null ? guarantorEmployment.getEmploymentIndustry().getDescEn() : ""), "lblEmploymentIndustryGuarantorValue1");
			if (guarantorSecondEmployment != null) {
				inputFieldLayout.addComponent(new Label(guarantorSecondEmployment.getEmploymentIndustry() != null ? guarantorSecondEmployment.getEmploymentIndustry().getDescEn() : ""), "lblEmploymentIndustryGuarantorValue2");
			} else {
				inputFieldLayout.addComponent(new Label(I18N.message("")), "lblEmploymentIndustryGuarantorValue2");
			}
			inputFieldLayout.addComponent(new Label(I18N.message("net.income")), "lblNetIncome");
			inputFieldLayout.addComponent(new Label(symbol + AmountUtils.format(guarantorEmployment.getRevenue())), "lblGuarantorNetIncome");
			if (documentUwGroups != null && !documentUwGroups.isEmpty()) {
				VerticalLayout documentsGridLayout = getDocumentsGridLayout(quotation, documentUwGroups, EApplicantType.G);
				inputFieldLayout.addComponent(documentsGridLayout, "guarantorDocumentsGridLayout");
			} else {
				inputFieldLayout.addComponent(new VerticalLayout(), "guarantorDocumentsGridLayout");
			}
		}
		
		inputFieldLayout.addComponent(new Label(I18N.message("cov.recommendation")), "lblCovRecommendation");
		inputFieldLayout.addComponent(new Label(I18N.message("applicant.reputation")), "lblApplicantReputation");
		inputFieldLayout.addComponent(new Label(I18N.message("guarantor.reputation")), "lblGuarantorReputation");
		
		inputFieldLayout.addComponent(new Label(I18N.message("uw.quote")), "lblUWQuote");
		inputFieldLayout.addComponent(getProfileQuote(quotation, FMProfile.UW), "lblUWQuoteValue");
		inputFieldLayout.addComponent(new Label(I18N.message("us.quote")), "lblUSQuote");
		inputFieldLayout.addComponent(getProfileQuote(quotation, FMProfile.US), "lblUSQuoteValue");
		
		managementLayout.addComponent(inputFieldLayout);
		managementLayout.addComponent(inputFieldLayout);
	}
	
	/**
	 * @param quotation
	 * @param profile
	 * @return
	 */
	private Label getProfileQuote(Quotation quotation, Long profile) {
		List<Comment> comments = quotation.getComments();
		Label lblComment = new Label();
		lblComment.setWidth(620, Unit.PIXELS);
		if (comments != null && !comments.isEmpty()) {
			for (Comment comment : comments) {
				if (comment.isForManager() && profile.equals(comment.getUser().getDefaultProfile().getId())) {
					lblComment.setValue(comment.getDesc());
					break;
				}
			}
		}
		return lblComment;
	}
	
	/**
	 * @param document
	 * @param quotationDocuments
	 * @return QuotationDocument
	 */
	private QuotationDocument getDocumentSelected(Document document, List<QuotationDocument> quotationDocuments) {
		if (quotationDocuments != null && !quotationDocuments.isEmpty()) {
			for (QuotationDocument quotationDocument : quotationDocuments) {
				if (document.getId().equals(quotationDocument.getDocument().getId())) {
					return quotationDocument;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param quotation
	 * @param documentUwGroups
	 * @return
	 */
	private VerticalLayout getDocumentsGridLayout(Quotation quotation, List<DocumentUwGroup> documentUwGroups, EApplicantType applicantType) {
		CustomLayout documentsGridLayout = new CustomLayout("xxx");
		int i = 0;
		String documentsGridTemplate = "<table cellspacing='0' cellpadding='5' style='border:1px solid black; border-collapse:collapse;'>";
		if(documentUwGroups !=null && documentUwGroups.size()>1){
			Collections.sort(documentUwGroups, new DocumentUwGroupsComparatorByIdex());	
		}
		
		for (DocumentUwGroup documentUwGroup : documentUwGroups) {
			if (documentUwGroup.getApplicantType() == applicantType) {
				List<DocumentScoring> documentsScoring = documentUwGroup.getDocumentsScoring();
				if (documentsScoring != null && !documentsScoring.isEmpty()) {					
					int totalScore = 0;
					List<QuotationDocument> selectedDocuments = new ArrayList<QuotationDocument>();
					for (DocumentScoring documentScoring : documentsScoring) {
						Document document = documentScoring.getDocument();
						QuotationDocument quotationDocument = getDocumentSelected(document, quotation.getQuotationDocuments());
						if (quotationDocument != null) {
			        		totalScore += documentScoring.getScore();
			        		selectedDocuments.add(quotationDocument);
						}
					}
					if (!selectedDocuments.isEmpty()) {
						
						documentsGridTemplate += "<tr>";
						
						Label lbldocumentUwGroup = new Label(documentUwGroup.getDescEn());
						if (totalScore >= 5) {
							lbldocumentUwGroup.setIcon(greenIcon);
						} else {
							lbldocumentUwGroup.setIcon(grayIcon);
						}							
												
						String documentScoringTemplate = "<table cellspacing='0' cellpadding='5'>";
						documentScoringTemplate += "<tr><td>";
						
						documentScoringTemplate = "<table cellspacing='3' cellpadding='5' style='border-collapse:collapse; display:inline-block;'>";						
						documentScoringTemplate += "<tr>";
						documentScoringTemplate += "<td class='blackborder' width='250px'>" + I18N.message("document") + "</td>";
		        		documentScoringTemplate += "<td class='blackborder' width='90px'>" + I18N.message("issue.date") + "</td>";
		        		documentScoringTemplate += "</tr>";
		        		
						for (QuotationDocument quotationDocument : selectedDocuments) {
							documentScoringTemplate += "<tr>";
							documentScoringTemplate += "<td class='blackborder'>";
							documentScoringTemplate += "<input type='checkbox' checked class='checkbox_unchange_disabled_color'/>";
							documentScoringTemplate += quotationDocument.getDocument().getApplicantType() + " - " + quotationDocument.getDocument().getDescEn();
			        		documentScoringTemplate += "</td>";
			        		documentScoringTemplate += "<td class='blackborder'>";
			        		if (quotationDocument.getIssueDate() != null) {
			        			documentScoringTemplate += DateUtils.date2StringDDMMYYYY_SLASH(quotationDocument.getIssueDate());
			        		}
			        		documentScoringTemplate += "</td>";
			        		documentScoringTemplate += "</tr>";
						}
						
						documentScoringTemplate += "</table>";
		        		
						documentScoringTemplate += "</td><td>";
						List<DocumentConfirmEvidence>  documentsConfirmEvidence = documentUwGroup.getDocumentsConfirmEvidence();
						if (documentsConfirmEvidence != null && !documentsConfirmEvidence.isEmpty()) {
							documentScoringTemplate += "<table cellspacing='3' cellpadding='0' style='display:inline-block; vertical-align:top;'>";
							for (DocumentConfirmEvidence documentConfirmEvidence : documentsConfirmEvidence) {
								String checkConfirmEvidence = "";
								documentScoringTemplate += "<tr><td><input type='checkbox' " + checkConfirmEvidence + " class='checkbox_unchange_disabled_color'>" + documentConfirmEvidence.getConfirmEvidence().getDescEn() +"</td></tr>";
							}
							documentScoringTemplate += "</table>";
						}
						
						documentScoringTemplate += "</td></tr>";
						documentScoringTemplate += "</table>";
						
						Comment comment = getComment(quotation, documentUwGroup);
						
						if (comment != null) {
							documentScoringTemplate += "<br/>";
							
							documentScoringTemplate += "<table cellspacing='3' cellpadding='5' style='border:1px solid black; border-collapse:collapse;'><tr>";
							documentScoringTemplate += "<td><div location='lblCommentUwGroup'/></td>";
							documentScoringTemplate += "</tr></table>";
						}
												
						CustomLayout documentScoringLayout = new CustomLayout("xxxx");
						documentScoringLayout.setTemplateContents(documentScoringTemplate);
						documentScoringLayout.setSizeFull();
						
						if (comment != null) {
							Label lblCommentUwGroup = new Label();
							lblCommentUwGroup.setWidth(700, Unit.PIXELS);
							lblCommentUwGroup.setValue(comment.getDesc());
							documentScoringLayout.addComponent(lblCommentUwGroup, "lblCommentUwGroup");
						}
						
						documentsGridTemplate += "<td class='blackborder' width='120px'><div location='lbldocumentUwGroup" + i +"'/></td>";
						documentsGridTemplate += "<td class='blackborder'><div location='documentScoringLayout" + i +"'/></td>";
						documentsGridLayout.addComponent(lbldocumentUwGroup, "lbldocumentUwGroup" + i);
						documentsGridLayout.addComponent(documentScoringLayout, "documentScoringLayout" + i);
						++i;
						documentsGridTemplate += "</tr>";
					}
				}
			}
			
		}
		documentsGridTemplate += "</table>";
		documentsGridLayout.setTemplateContents(documentsGridTemplate);
		return new VerticalLayout(documentsGridLayout);
	}
	
	/**
	 * @author ly.youhort
	 */
	protected static class DocumentUwGroupsComparatorByIdex implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			DocumentUwGroup c1 = (DocumentUwGroup) o1;
			DocumentUwGroup c2 = (DocumentUwGroup) o2;
			if (c1 == null || c1.getSortIndex() == null) {
				if (c2 == null || c2.getSortIndex() == null) return 0;
				return -1;
			}
			if (c2 == null || c2.getSortIndex() == null) return 1;
			return c1.getSortIndex().compareTo(c2.getSortIndex());
		}
	}
	
	/**
	 * 
	 * @param quotation
	 */
	private Map<Long, Long> getTiming(Quotation quotation) {
		Map<Long, Long> timing = new HashMap<Long, Long>();
		// TODO PYI
//		List<QuotationStatusHistory> quotationStatusHistories = quotationService.getWkfStatusHistories(quotation.getId(), Order.asc("updateDate"));
//		Date start = quotation.getStartCreationDate() != null ? quotation.getStartCreationDate() : DateUtils.today();
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			SecUser secUser = quotationStatusHistory.getUser();
//			Date end = quotationStatusHistory.getCreateDate();
//			long time = 0;
//			if (ProfileUtil.isPOS(secUser)) {
//				time = MyNumberUtils.getLong(timing.get(FMProfile.CO));
//				long diff = end.getTime() - start.getTime();
//				if (diff > 0) {
//					time += diff;
//				}
//				timing.put(FMProfile.CO, time);
//			} else {
//				time = MyNumberUtils.getLong(timing.get(secUser.getDefaultProfile().getId()));
//				time += end.getTime() - start.getTime();
//				timing.put(secUser.getDefaultProfile().getId(), time);
//			}
//			start = end;
//		}
		return timing;
	}
	
	/**
	 * @param timing
	 * @return
	 */
	private long getTotalTiming(Map<Long, Long> timing) {
		long total = 0;
		for (Iterator<Long> iter = timing.keySet().iterator(); iter.hasNext(); ) {
			total += MyNumberUtils.getLong(timing.get(iter.next()));
		}
		return total;
	}
	
	/**
	 * @param time
	 * @return
	 */
	private String getTime(Long millis) {
		if (millis != null) {
			String s = "" + (millis / 1000) % 60;
			String m = "" + (millis / (1000 * 60)) % 60;
			String h = "" + (millis / (1000 * 60 * 60)) % 24;
			String d = "" + (millis / (1000 * 60 * 60 * 24));
			return d + "d " + h + "h:" + m + "m:" + s + "s";
		}
		return "N/A";
	}
	
	@Override
	public void updateNavigationPanel(VerticalLayout navigationLayout, NavigationPanel defaultNavigationPanel) {
		navigationLayout.removeAllComponents();
		if (QuotationProfileUtils.isNavigationManagerAvailable(quotation)) {
			NavigationPanel navigationPanel = new NavigationPanel();
			if (quotation.getWkfStatus() != QuotationWkfStatus.RCG) {
				navigationPanel.addButton(btnBackUw);
			}
			navigationPanel.addButton(btnApprove);
			navigationPanel.addButton(btnReject);
			navigationLayout.addComponent(navigationPanel);
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		EWkfStatus newQuotationStatus = null;
		boolean forManager = false;
		if (event.getButton() == btnBackUw) {
			newQuotationStatus = QuotationWkfStatus.PRO;
		} else if (event.getButton() == btnApprove) {
			if (quotation.getWkfStatus().equals(QuotationWkfStatus.RCG)) {
				newQuotationStatus = QuotationWkfStatus.LCG;
			} else if (quotation.getWkfStatus().equals(QuotationWkfStatus.AWS)) {
				newQuotationStatus = QuotationWkfStatus.AWT;
			} else {
				newQuotationStatus = QuotationWkfStatus.APV;
			}
			forManager = true;
		} else if (event.getButton() == btnReject) {
			if (quotation.getWkfStatus().equals(QuotationWkfStatus.RCG)) {
				newQuotationStatus = QuotationWkfStatus.ACT;
			} else {
				newQuotationStatus = QuotationWkfStatus.REJ;
			}
		} 
		showCommentFormPanel(newQuotationStatus, forManager,event.getButton().getCaption());
	}
	
	/**
	 * @param newStatus
	 */
	public void showCommentFormPanel(final EWkfStatus newStatus, final boolean forManager,String caption) {
		CommentFormPanel commentFormPanel = new CommentFormPanel(quotation, newStatus, forManager, new ClickListener() {
			private static final long serialVersionUID = -8159169476150724593L;
			@Override
			public void buttonClick(ClickEvent event) {
				quotationService.saveManagementDecision(quotation, newStatus);
				Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
				if (newStatus == QuotationWkfStatus.PRO) {
					notification.setDescription(I18N.message("back.message.underwriter", String.valueOf(quotation.getId())));
				} else if (newStatus == QuotationWkfStatus.APV || newStatus == QuotationWkfStatus.AWT || newStatus == QuotationWkfStatus.LCG) {
					notification.setDescription(I18N.message("accept.proposal", String.valueOf(quotation.getId())));
				} else {
					notification.setDescription(I18N.message("reject.proposal", String.valueOf(quotation.getId())));
				}
				notification.setDelayMsec(5000);
				notification.show(Page.getCurrent());
//				quotationFormPanel.getQuotationsPanel().displayQuotationTablePanel();
			}
		});
		commentFormPanel.setWidth(700, Unit.PIXELS);
		commentFormPanel.setHeight(350, Unit.PIXELS);
		commentFormPanel.setCaption(caption);
		UI.getCurrent().addWindow(commentFormPanel);
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	private HorizontalLayout getRequestedFieldCheck(Quotation quotation) {
		HorizontalLayout supportDecisionsLayout = new HorizontalLayout(); 
		List<QuotationSupportDecision> quotationSupportDecisions = quotation.getQuotationSupportDecisions(QuotationWkfStatus.RFC);
		if (quotationSupportDecisions != null && !quotationSupportDecisions.isEmpty()) {
			for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisions) {
				CheckBox cbSupportDecision = new CheckBox(quotationSupportDecision.getSupportDecision().getDescEn());
				cbSupportDecision.setValue(true);
				cbSupportDecision.setStyleName("checkbox_unchange_disabled_color");
				supportDecisionsLayout.addComponent(cbSupportDecision);
				supportDecisionsLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
			}
		}
		if (supportDecisionsLayout.getComponentCount() == 0) {
			supportDecisionsLayout.addComponent(new Label("N/A"));
		}
		return supportDecisionsLayout;
	}
		
	/**
	 * @param quotation
	 * @param ocumentUwGroup
	 * @return
	 */
	private Comment getComment(Quotation quotation, DocumentUwGroup documentUwGroup) {
		for (Comment comment : quotation.getComments()) {
			if (comment.getDocumentUwGroup() != null && comment.getDocumentUwGroup().getId().equals(documentUwGroup.getId())) {
				return comment;
			}
		}
		return null;
	}
	public Double calTotalInstallmentAmount(Quotation quotation){
		Double insuranceFee = 0d;
		Double servicingFee = 0d;
		Double totalInsallmentAmount = 0d;
		int nbPaidPerYear = 0;
		int i=0;
		if (quotation != null) {
			//Query Service 
				services = new ArrayList<FinService>();
				BaseRestrictions<FinService> restrictions = new BaseRestrictions<FinService>(FinService.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				services = ENTITY_SRV.list(restrictions);
				double serviceAmount[] = new double[services.size()];
				for (FinService service : services) {
					com.nokor.efinance.core.quotation.model.QuotationService quotationService =  quotation.getQuotationService(service.getId());
					if(quotationService != null){
						serviceAmount[i] = quotationService.getTiPrice();
					}
					i++;
				}
				insuranceFee = serviceAmount[0];
				servicingFee = serviceAmount[1];
				
				if(quotation.getFrequency().getCode() == "annually"){
					nbPaidPerYear = 1;
				}else if(quotation.getFrequency().getCode() == "half.year"){
					nbPaidPerYear = 2;
				}else if(quotation.getFrequency().getCode() == "monthly"){
					nbPaidPerYear = 12;
				}else{
					nbPaidPerYear = 4;
				}
				
				insuranceFee = MyMathUtils.roundAmountTo((quotation.getTerm() / nbPaidPerYear) * insuranceFee) / quotation.getTerm();
				servicingFee = MyMathUtils.roundAmountTo(servicingFee / quotation.getTerm());	
				
				totalInsallmentAmount = MyMathUtils.roundAmountTo(quotation.getTiInstallmentAmount() + insuranceFee + servicingFee);

		}
		return totalInsallmentAmount;
	}

}


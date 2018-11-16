package com.nokor.efinance.gui.ui.panel.quotation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.finance.services.tools.LoanUtils;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Simulation re payment schedule panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SimulationRepaymentScheduleFormPanel extends AbstractFormPanel implements QuotationEntityField, ValueChangeListener {

	private static final long serialVersionUID = -3948949199018017119L;

	@Autowired
	private QuotationService quotationService;

	@Autowired
	protected ApplicantService applicantService;
	
	private FinanceCalculationService financeCalculationService = (FinanceCalculationService) SecApplicationContextHolder.getContext().getBean("financeCalculationService");
	//private ReportService reportService = SpringUtils.getBean(ReportService.class);
	
	private TextField txtAssetPrice;
	private TextField txtTermInMonth;
	private TextField txtLeaseAmount;
	private TextField txtOtherLeaseAmount;
	private TextField txtDealerLeaseAmount;
	private TextField txtPeriodicInterestRate;
	private TextField txtAdvancePaymentPecentage;
	private TextField txtAdvancePayment;
	private TextField txtInsuranceFee;
	private TextField txtServiceFee;
	private TextField txtInstallmentAmont;
	private TextField txtTotalInstallmentAmount;
	
	private EntityRefComboBox<FinProduct> cbxFinancialProduct;
	private ERefDataComboBox<EFrequency> cbxFrequency;
	private List<TextField> txtServices;
	private List<CheckBox> cbIncludeInInstallmentServices;
	private List<CheckBox> cbSplitWithInstallmentServices;
	
	private HorizontalLayout invalidMessageLayout;
	private VerticalLayout servicesLayout;

	@SuppressWarnings("unused")
	private boolean invalidQuotation;

	@PostConstruct
	public void PostConstruct() {
		super.init();
		setCaption(I18N.message("simulation.repayment.schedule"));
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@SuppressWarnings("serial")
	@Override
	protected com.vaadin.ui.Component createForm() {
		invalidQuotation = true;
		
		txtServices = new ArrayList<TextField>();
		cbIncludeInInstallmentServices = new ArrayList<CheckBox>();
		cbSplitWithInstallmentServices = new ArrayList<CheckBox>();
		
		servicesLayout = new VerticalLayout(); 
		
		Label iconInvalidMessage = new Label();
		iconInvalidMessage.setIcon(new ThemeResource("../nkr-default/icons/16/danger.png"));
		Label lblInvalidMessage = new Label(I18N.message("quotation.invalid"));
		lblInvalidMessage.setStyleName("error");
		invalidMessageLayout = new HorizontalLayout(iconInvalidMessage, lblInvalidMessage);
		invalidMessageLayout.setVisible(true);
		
		cbxFinancialProduct = new EntityRefComboBox<FinProduct>();
		BaseRestrictions<FinProduct> restrictions = new BaseRestrictions<>(FinProduct.class);
		restrictions.addCriterion(Restrictions.le(START_DATE, DateUtils.getDateAtEndOfDay(DateUtils.todayH00M00S00())));
		restrictions.addCriterion(Restrictions.ge(END_DATE, DateUtils.todayH00M00S00()));
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		cbxFinancialProduct.setRestrictions(restrictions);		
		cbxFinancialProduct.setImmediate(true);
		cbxFinancialProduct.renderer();
		cbxFinancialProduct.setSelectedEntity(null);
		
		txtAssetPrice = ComponentFactory.getTextField(false, 30, 170);
		txtAssetPrice.setStyleName("blackdisabled");
		txtAssetPrice.setEnabled(true);
				
		txtTermInMonth = ComponentFactory.getTextField(false, 10, 100);
		txtTermInMonth.setImmediate(true);
		txtTermInMonth.addValueChangeListener(this);
		
		txtLeaseAmount = ComponentFactory.getTextField(false, 50, 170);
		txtLeaseAmount.setStyleName("blackdisabled");
		txtLeaseAmount.setEnabled(false);
		
		txtDealerLeaseAmount = ComponentFactory.getTextField(false, 50, 170);
		txtDealerLeaseAmount.setVisible(false);
		
		txtOtherLeaseAmount = ComponentFactory.getTextField(false, 50, 170);
		txtOtherLeaseAmount.setEnabled(false);
		
		txtPeriodicInterestRate = ComponentFactory.getTextField(false, 50, 100);
		txtPeriodicInterestRate.setImmediate(true);
		txtPeriodicInterestRate.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				calculateInstallmentAmount();
			}
		});
		
		txtAdvancePaymentPecentage = ComponentFactory.getTextField(false, 50, 170);
		txtAdvancePaymentPecentage.setImmediate(true);
		txtAdvancePaymentPecentage.addValueChangeListener(this);
		
		txtAdvancePayment = ComponentFactory.getTextField(false, 50, 170);
		txtAdvancePayment.setImmediate(true);
		txtAdvancePayment.setStyleName("blackdisabled");
		txtAdvancePayment.setEnabled(false);
		
		cbxFrequency = new ERefDataComboBox<EFrequency>(EFrequency.class);
		cbxFrequency.setImmediate(true);
		cbxFrequency.setSelectedEntity(EFrequency.M);
		cbxFrequency.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				calculateInstallmentAmount();
			}
		});
		
		txtInstallmentAmont = ComponentFactory.getTextField(false, 50, 150);
		txtInstallmentAmont.setStyleName("blackdisabled");
		txtInstallmentAmont.setEnabled(false);
		
		txtTotalInstallmentAmount = ComponentFactory.getTextField(false, 50, 150);
		txtTotalInstallmentAmount.setStyleName("blackdisabled");
		txtTotalInstallmentAmount.setEnabled(false);
		
		txtInsuranceFee = ComponentFactory.getTextField(false, 50, 150);
		txtInsuranceFee.setImmediate(true);
		txtInsuranceFee.setEnabled(true);
		txtInsuranceFee.setValue("0.00");
		
		txtServiceFee = ComponentFactory.getTextField(false, 50, 150);
		txtServiceFee.setImmediate(true);
		txtServiceFee.setEnabled(true);
		txtServiceFee.setValue("0.00");
		
		cbxFinancialProduct.addValueChangeListener(this);
				
		Button btnCalcul = new Button("");
		btnCalcul.setIcon(new ThemeResource("icons/32/calculatrice.png"));
		btnCalcul.setStyleName(Reindeer.BUTTON_LINK);
		btnCalcul.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6516775560049407997L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(checkValidityFiels()){
					calculateInstallmentAmount();
					//generate Report Excel
					//generateReport(GLFSimulationRepaymentSchedule.class);
				}	
			}
		});
		
		String template = "quotationFinanceCalculation";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout inputFieldLayout = null;
		try {
			inputFieldLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		inputFieldLayout.setSizeFull();
		inputFieldLayout.addComponent(new Label(I18N.message("financial.product")), "lblFinancialProduct");
		inputFieldLayout.addComponent(cbxFinancialProduct, "cbxFinancialProduct");
		inputFieldLayout.addComponent(new Label(I18N.message("asset.price")), "lblAssetPrice");
		inputFieldLayout.addComponent(txtAssetPrice, "txtAssetPrice");
		inputFieldLayout.addComponent(new Label(I18N.message("advance.payment.percentage")), "lblAdvancePaymentPecentage");
		inputFieldLayout.addComponent(txtAdvancePaymentPecentage, "txtAdvancePaymentPecentage");
		inputFieldLayout.addComponent(new Label(I18N.message("advance.payment")), "lblAdvancePayment");
		inputFieldLayout.addComponent(txtAdvancePayment, "txtAdvancePayment");
		inputFieldLayout.addComponent(new Label(I18N.message("lease.amount")), "lblLeaseAmount");
		inputFieldLayout.addComponent(txtLeaseAmount, "txtLeaseAmount");
		inputFieldLayout.addComponent(new Label(I18N.message("frequency")), "lblFrequency");
		inputFieldLayout.addComponent(cbxFrequency, "cbxFrequency");
		inputFieldLayout.addComponent(new Label(I18N.message("term.month")), "lblTermInMonth");
		inputFieldLayout.addComponent(txtTermInMonth, "txtTermInMonth");
		inputFieldLayout.addComponent(new Label(I18N.message("periodic.interest.rate")), "lblPeriodicInterestRate");
		inputFieldLayout.addComponent(txtPeriodicInterestRate, "txtPeriodicInterestRate");
		inputFieldLayout.addComponent(new Label(I18N.message("installment.amount")), "lblInstallmentAmont");
		inputFieldLayout.addComponent(new HorizontalLayout(txtInstallmentAmont, btnCalcul), "txtInstallmentAmont");
		//inputFieldLayout.addComponent(invalidMessageLayout, "lblInvalidMessage");
		
		txtAdvancePaymentPecentage.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4508483753757807372L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				recalculateAdvancePaymentAmont();
			}
		});
		
		final Panel financialProductPanel = new Panel(I18N.message("repayment.schedule"));
		this.ServicePanel();
		VerticalLayout contentLayout = new VerticalLayout(inputFieldLayout, servicesLayout);
		contentLayout.setMargin(true);
		financialProductPanel.setContent(contentLayout);
		
		setInvalidQuotationFlag(false);
		return financialProductPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		return null;
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
	}
	
	/**
	 * 
	 */
	public void ServicePanel(){
		Panel servicePanel = new Panel(I18N.message("services"));
		final GridLayout gridServiceLayout = new GridLayout(10, 10);
		gridServiceLayout.setSpacing(true);
		gridServiceLayout.setMargin(true);
		gridServiceLayout.addComponent(ComponentFactory.getLabel("", 150), 0, 0);
		gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 1, 0);
		gridServiceLayout.addComponent(ComponentFactory.getLabel(I18N.message("amount"), 150), 2, 0);
		gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 3, 0);
		gridServiceLayout.addComponent(ComponentFactory.getLabel(I18N.message("split.with.installment"), 150), 4, 0);
	
		CheckBox cbIncludeInInstallment = new CheckBox();
		
 		CheckBox cbSplitWithInstallment = new CheckBox();
 		cbSplitWithInstallment.setEnabled(false);
 		cbSplitWithInstallment.setValue(true);
 		
		cbIncludeInInstallment.setEnabled(false);
		cbIncludeInInstallment.setImmediate(true);
		cbIncludeInInstallment.setValue(true);
		cbIncludeInInstallment.addValueChangeListener(this);
		
		gridServiceLayout.addComponent(new Label(I18N.message("insurance.fee")), 0, 1);
		gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 1, 1);
		gridServiceLayout.addComponent(txtInsuranceFee, 2, 1);
		gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 3, 1);
		gridServiceLayout.addComponent(cbSplitWithInstallment, 4, 1);
		
		gridServiceLayout.addComponent(new Label(I18N.message("service.fee")), 0, 2);
		gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 1, 2);
		gridServiceLayout.addComponent(txtServiceFee, 2, 2);
		gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 3, 2);
		gridServiceLayout.addComponent(cbIncludeInInstallment, 4, 2);
		
		gridServiceLayout.addComponent(new Label(I18N.message("total.installment.amount")), 5,1 );
		gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 6, 1);
		gridServiceLayout.addComponent(txtTotalInstallmentAmount, 7, 1);
		
		cbIncludeInInstallmentServices.add(cbIncludeInInstallment);
		cbSplitWithInstallmentServices.add(cbSplitWithInstallment);
		
		servicePanel.setContent(gridServiceLayout);
    	servicesLayout.removeAllComponents();
    	servicesLayout.addComponent(servicePanel);
	}
	
	/**
	 * 
	 */
	public void calculateInstallmentAmount(){
		if(checkValidityFiels()){
			CalculationParameter calculationParameter = new CalculationParameter();
			calculationParameter.setInitialPrincipal(getDouble(txtLeaseAmount, 0d));
			calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(getInteger(txtTermInMonth), cbxFrequency.getSelectedEntity()));
			calculationParameter.setPeriodicInterestRate(getDouble(txtPeriodicInterestRate, 0d) / 100);
			calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(cbxFinancialProduct.getSelectedEntity().getNumberOfPrincipalGracePeriods()));
			double installmentAmount = MyMathUtils.roundAmountTo(financeCalculationService.getInstallmentPayment(calculationParameter));
			txtInstallmentAmont.setValue(AmountUtils.format(installmentAmount));
			calTotalInstallmentAmount(installmentAmount);
			recalculateAdvancePaymentAmont();
			setInvalidQuotationFlag(false);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean checkValidityFiels() {
		//super.removeErrorsPanel();
		checkMandatorySelectField(cbxFinancialProduct, "financial.product");
		checkMandatoryField(txtAssetPrice, "asset.price");
		checkDoubleField(txtAssetPrice, "asset.price");
		checkMandatoryField(txtTermInMonth, "term.month");
		checkIntegerField(txtTermInMonth, "term.month");
		checkMandatoryField(txtPeriodicInterestRate, "periodic.interest.rate");
		checkDoubleField(txtPeriodicInterestRate, "periodic.interest.rate");
		checkMandatoryField(txtAdvancePaymentPecentage, "advance.payment.percentage");
		checkDoubleField(txtAdvancePaymentPecentage, "advance.payment.percentage");
		checkDoubleField(txtInsuranceFee, "insurance.fee");
		checkDoubleField(txtServiceFee, "service.fee");
		checkMandatorySelectField(cbxFrequency, "frequency");
		return errors.isEmpty();
		
	}
	
	/**
	 *
	 */
	private void recalculateAdvancePaymentAmont() {
		double cashPrice = getDouble(txtAssetPrice, 0d);
		double advancePaymentAmontPc = getDouble(txtAdvancePaymentPecentage, 0d);
		double advancePaymentAmont = advancePaymentAmontPc *  cashPrice / 100; 
		txtAdvancePayment.setValue(AmountUtils.format(advancePaymentAmont));
		recalculateLeaseAmont();
	}
	
	/**
	 * 
	 */
	private void recalculateLeaseAmont() {
		double cashPrice = getDouble(txtAssetPrice, 0d);
		double advancePaymentAmont = getDouble(txtAdvancePayment, 0d);
		txtDealerLeaseAmount.setValue(AmountUtils.format(cashPrice - advancePaymentAmont));
		txtOtherLeaseAmount.setValue(AmountUtils.format(getAmountOfServicesIncludedInInstallment()));
		txtLeaseAmount.setValue(AmountUtils.format(getDouble(txtDealerLeaseAmount, 0d) + getDouble(txtOtherLeaseAmount, 0d)));
	}
	
	/**
	 * 
	 * @param invalid
	 */
	public void setInvalidQuotationFlag(boolean invalid) {
		invalidQuotation = invalid;
		invalidMessageLayout.setVisible(invalid);
	}
	
	/**
	 * 
	 * @return
	 */
	private double getAmountOfServicesIncludedInInstallment() {
		double servicesIncludedInInstallment = 0d;
		if (txtServices != null && !txtServices.isEmpty()) {
			for (int i = 0; i < txtServices.size(); i++) {
				if (cbIncludeInInstallmentServices.get(i).getValue()) {
					servicesIncludedInInstallment += getDouble(txtServices.get(i));
				}
			}
		}
		return servicesIncludedInInstallment;
	}
	public void calTotalInstallmentAmount(Double installmentAmounts){
		Double insuranceFee = 0d;
		Double servicingFee = 0d;
		Double TotalInstallmentAmount = 0d;
		installmentAmounts = installmentAmounts == null ? 0d : installmentAmounts;
		int numPaidPerYear = 1;
		int term = 0;
		if (txtTermInMonth.getValue() != ""
				&& txtServiceFee.getValue() != ""
				&& txtInsuranceFee.getValue() != ""
				&& cbxFrequency.getSelectedEntity() != null) {
				
			term = Integer.valueOf(txtTermInMonth.getValue());
			insuranceFee = Double.valueOf(txtInsuranceFee.getValue());
			servicingFee = Double.valueOf(txtServiceFee.getValue());
			
			if(cbxFrequency.getSelectedEntity().getCode() == "annually"){
				numPaidPerYear =1;
			}else if(cbxFrequency.getSelectedEntity().getCode() == "half.year"){
				numPaidPerYear = 2;
			}else if(cbxFrequency.getSelectedEntity().getCode() == "monthly"){
				numPaidPerYear = 12;
			}else {
				numPaidPerYear = 4;
			}
			
		}
			insuranceFee = MyMathUtils.roundAmountTo(((term / numPaidPerYear) * insuranceFee) / term);
			servicingFee = MyMathUtils.roundAmountTo(servicingFee/term); 
			TotalInstallmentAmount = insuranceFee + servicingFee + installmentAmounts;
			txtTotalInstallmentAmount.setValue(AmountUtils.format(TotalInstallmentAmount) + "");
		}
	/*private void generateReport (Class<? extends Report> reportClass) {
		try {
			ReportParameter reportParameter = new ReportParameter();
			reportParameter.addParameter("assetPrice", txtAssetPrice.getValue());
			reportParameter.addParameter("advancePaymentPercentage", txtAdvancePaymentPecentage.getValue());
			reportParameter.addParameter("advancePayment", txtAdvancePayment.getValue());
			reportParameter.addParameter("lesseeAmount", txtLeaseAmount.getValue());
			reportParameter.addParameter("termInMonth", txtTermInMonth.getValue());
			reportParameter.addParameter("periodicInterestRate",txtPeriodicInterestRate.getValue());
			reportParameter.addParameter("installmentAmont", txtInstallmentAmont.getValue());
			reportParameter.addParameter("frequency", cbxFrequency.getSelectedEntity());
			reportParameter.addParameter("insuranceFee", txtInsuranceFee.getValue());
			reportParameter.addParameter("serviceFee", txtServiceFee.getValue());
			
			reportClass = GLFSimulationRepaymentSchedule.class;
				
			String fileName = reportService.extract(reportClass, reportParameter);
			String fileDir = "";
			String tmpFileName = "";
				fileDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
				tmpFileName =fileDir + "/" + fileName;
			DocumentViewver documentViewver = new DocumentViewver(I18N.message(""), tmpFileName); 
			UI.getCurrent().addWindow(documentViewver);
		} catch (Exception e) {
			logger.error("", e);
		}
	}*/
}

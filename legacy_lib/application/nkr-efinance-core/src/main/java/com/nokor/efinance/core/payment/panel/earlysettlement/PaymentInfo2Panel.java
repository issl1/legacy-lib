package com.nokor.efinance.core.payment.panel.earlysettlement;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.document.panel.DocumentViewver;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.service.ReportService;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author heng.mao
 *
 */
public class PaymentInfo2Panel extends VerticalLayout implements FMEntityField {

	private static final long serialVersionUID = 8593151485544267183L;
	private ReportService reportService = (ReportService) SecApplicationContextHolder.getContext().getBean("reportService");
	private TextField txtFirstName;
	private TextField txtLastName;
	private AutoDateField dfPaymentDate;
	private TextField txtCode;
	private TextField txtExternalCode;
	private TextField txtPaymentAmount;
	private TextField txtInstallmentAmount;
	private TextField txtOtherAmount;
	private TextField txtNumberInstallment;
	private TextField txtPenaltyAmount;
	private TextField txtServicingFee;
	private TextField txtInsuranceFee;
//	private ERefDataComboBox<EPaymentMethod> cbxPaymentMethod; 
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private ToolbarButtonsPanel toolbarButtons;
	private Button btnPrintInstallmentReceipt;
	private VerticalLayout contentLayout;
	private Payment payment;
	
	public PaymentInfo2Panel() {
		setMargin(true);
		addComponent(createForm());
	}

	/**
	 * @return
	 */
	protected Component createForm() {
	
		contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		final GridLayout gridLayout = new GridLayout(12, 4);
		gridLayout.setSpacing(true);
		
		txtFirstName = ComponentFactory.getTextField(false, 60, 150);
		txtFirstName.setEnabled(false);
		txtFirstName.setStyleName("v-disabled");

		txtLastName = ComponentFactory.getTextField(60, 150);
		txtLastName.setEnabled(false);
		txtLastName.setStyleName("v-disabled");
				
		dfPaymentDate = ComponentFactory.getAutoDateField("", true);
		
		cbxPaymentMethod = new EntityRefComboBox<>();
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setRequired(true);
		cbxPaymentMethod.setWidth("150px");
		
		txtCode = ComponentFactory.getTextField(60, 150);
		txtCode.setEnabled(false);
		txtExternalCode = ComponentFactory.getTextField(60, 150);
		
		txtPaymentAmount = ComponentFactory.getTextField(60, 120);
		txtPaymentAmount.setEnabled(false);
		
		txtInstallmentAmount = ComponentFactory.getTextField(60, 120);
		txtInstallmentAmount.setEnabled(false);
		
		txtPenaltyAmount = ComponentFactory.getTextField(60, 120);
		txtPenaltyAmount.setEnabled(false);
		
		txtServicingFee = ComponentFactory.getTextField(30, 150);
		txtServicingFee.setEnabled(false);
		txtInsuranceFee = ComponentFactory.getTextField(30, 150);
		txtInsuranceFee.setEnabled(false);
		
		txtOtherAmount = ComponentFactory.getTextField(60, 120);
		txtOtherAmount.setEnabled(false);
		
		txtNumberInstallment = ComponentFactory.getTextField(60, 150);
		txtNumberInstallment.setEnabled(false);
		
        btnPrintInstallmentReceipt = new NativeButton(I18N.message("print.installment"));
        btnPrintInstallmentReceipt.setIcon(new ThemeResource("../nkr-default/icons/16/print.png"));
        btnPrintInstallmentReceipt.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1044087379420693632L;
			@Override
			public void buttonClick(ClickEvent event) {
				Class<? extends Report> reportClass = GLFInstallmentReceipt.class;
				ReportParameter reportParameter = new ReportParameter();
				reportParameter.addParameter("paymnId", payment.getId());	
				String	fileName = "";
				try {
					fileName = reportService.extract(reportClass, reportParameter);
				} catch (Exception e) {
				}
				 String fileDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
				DocumentViewver documentViewver = new DocumentViewver(I18N.message(""), fileDir + "/" + fileName); 
				UI.getCurrent().addWindow(documentViewver);
			}
		});
        toolbarButtons = new ToolbarButtonsPanel();
        toolbarButtons.addButton(btnPrintInstallmentReceipt);
        
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("installment.payment.no")), iCol++, 0);
        gridLayout.addComponent(txtCode, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("num.installment")), iCol++, 0);
        gridLayout.addComponent(txtNumberInstallment, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("installment.amount")), iCol++, 0);
        gridLayout.addComponent(txtInstallmentAmount, iCol++, 0);
        
        iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("lastname.en")), iCol++, 1);
        gridLayout.addComponent(txtLastName, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(new Label(I18N.message("firstname.en")), iCol++, 1);
        gridLayout.addComponent(txtFirstName, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("other.amount")), iCol++, 1);
        gridLayout.addComponent(txtOtherAmount, iCol++, 1);
        
		iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("servicing.fee")), iCol++, 2);
        gridLayout.addComponent(txtServicingFee, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
        gridLayout.addComponent(new Label(I18N.message("insurance.fee")), iCol++, 2);
        gridLayout.addComponent(txtInsuranceFee, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
        gridLayout.addComponent(new Label(I18N.message("penalty.amount")), iCol++, 2);
        gridLayout.addComponent(txtPenaltyAmount, iCol++, 2);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("payment.date")), iCol++, 3);
        gridLayout.addComponent(dfPaymentDate, iCol++, 3);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 3);        
		gridLayout.addComponent(new Label(I18N.message("payment.method")), iCol++, 3);
        gridLayout.addComponent(cbxPaymentMethod, iCol++, 3);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 3);
        gridLayout.addComponent(new Label(I18N.message("total.amount")), iCol++, 3);
        gridLayout.addComponent(txtPaymentAmount, iCol++, 3);
        contentLayout.addComponent(toolbarButtons);
        contentLayout.addComponent(gridLayout);
		return contentLayout;
	}
	
	public void removeButtonPrintInstallment() {
		contentLayout.removeComponent(toolbarButtons);
	}
	/**
	 * @param txtPaymentAmount the txtPaymentAmount to set
	 */
	public void setTxtPaymentAmount(String value) {
		txtPaymentAmount.setValue(value);
	}
	
	public void setTxtInstallmentAmount(String value) {
		txtInstallmentAmount.setValue(value);
	}
	public void setTxtOtherAmount(String value) {
		txtOtherAmount.setValue(value);
	}
	
	/** 
	 * @return value of cbxPaymentMethod
	 */
	public EPaymentMethod getPaymentMethod() {		
		return cbxPaymentMethod.getSelectedEntity();
	}
	
	/**
	 * @return value of paymentDate
	 */
	public Date getPayementDate() {
		return dfPaymentDate.getValue();
	}
	
	/**
	 * @return
	 */
	public String getExternalCode() {
		return txtExternalCode.getValue();
	}
	
	/**
	 * @param contractId
	 */
	public void assignValues(Payment payment) {
		reset();
		this.payment = payment;
		Applicant applicant = payment.getApplicant();
		if (applicant != null) {
			txtFirstName.setValue(applicant.getIndividual().getFirstNameEn());
			txtLastName.setValue(applicant.getIndividual().getLastNameEn());
		}
		cbxPaymentMethod.setSelectedEntity(payment.getPaymentMethod());
		dfPaymentDate.setValue(payment.getPaymentDate());
		txtExternalCode.setValue(StringUtils.defaultString(payment.getExternalReference()));
		txtCode.setValue(StringUtils.defaultString(payment.getReference()));
		double installment = 0d ;
		double installmentOther = 0d;
		double penaltyAmount = 0d;
		double tiInsuranceFeeUsd = 0d;
		double tiServicingFeeUsd = 0d;
		int numInstallment = 0;
		String no = "";
		boolean isNumInstallment = false;
		if (payment.getCashflows() != null) {
			for (Cashflow cashflow : payment.getCashflows()) {
				if(cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					installment += cashflow.getTiInstallmentAmount();
				} else if(cashflow.getCashflowType().equals(ECashflowType.FEE)){
					if (EServiceType.INSFEE.equals(cashflow.getService().getServiceType())) {
						tiInsuranceFeeUsd += cashflow.getTiInstallmentAmount();
					} else if (EServiceType.SRVFEE.equals(cashflow.getService().getServiceType())) {
						tiServicingFeeUsd += cashflow.getTiInstallmentAmount();
					} else {
						installmentOther += cashflow.getTiInstallmentAmount();
					}
				
				} else if(!cashflow.getCashflowType().equals(ECashflowType.FIN) && !cashflow.getCashflowType().equals(ECashflowType.PEN)) {
					installmentOther += cashflow.getTiInstallmentAmount();
				}
				if (cashflow.getCashflowType().equals(ECashflowType.PEN)) {
					penaltyAmount += cashflow.getTiInstallmentAmount();
				}
				if(numInstallment != 0 && numInstallment != cashflow.getNumInstallment()){
					isNumInstallment = true;
				}
				numInstallment = cashflow.getNumInstallment();
				no = cashflow.getNumInstallment().toString();
			}
		}

		txtNumberInstallment.setValue(no);
		if (isNumInstallment) {
			txtNumberInstallment.setValue("");
		}
		txtServicingFee.setValue(AmountUtils.format(tiServicingFeeUsd));
		txtInsuranceFee.setValue(AmountUtils.format(tiInsuranceFeeUsd));
		txtPenaltyAmount.setValue(AmountUtils.format(penaltyAmount));
		txtPaymentAmount.setValue(AmountUtils.format(installment
				+ installmentOther + penaltyAmount + tiServicingFeeUsd
				+ tiInsuranceFeeUsd));
		txtInstallmentAmount.setValue(AmountUtils.format(installment));
		txtOtherAmount.setValue(AmountUtils.format(installmentOther));
	}

	/**
	 */
	public void reset() {
		txtFirstName.setValue("");
		txtLastName.setValue("");
		dfPaymentDate.setValue(null);
		txtCode.setValue("");
		txtExternalCode.setValue("");
		txtNumberInstallment.setValue("");
	}
}

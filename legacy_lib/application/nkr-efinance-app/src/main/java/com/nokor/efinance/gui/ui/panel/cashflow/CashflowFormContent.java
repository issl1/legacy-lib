package com.nokor.efinance.gui.ui.panel.cashflow;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.financial.model.CreditLine;
import com.nokor.efinance.core.financial.model.EProductLineType;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

/**
 * @author ky.nora
 */
public class CashflowFormContent extends AbstractControlPanel {
	
	/** */
	private static final long serialVersionUID = 8987122830426594603L;
	
	private ERefDataComboBox<ETreasuryType> cbxTreasuryType;
	private ERefDataComboBox<ECashflowType> cbxType;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private EntityRefComboBox<ProductLine> cbxProductLine;
	private ERefDataComboBox<EProductLineType> cbxProductLineType;
	private TextField txtContract;
	private TextField txtnumInstallment;
	private TextField txtTiInstallmentUsd;
	private AutoDateField dfinstallmentDate;
	
	private EntityService entityService;
	private Contract contract;
	private Cashflow cashflow;
	
	/**
	 * 
	 * @param entityService
	 */
	public CashflowFormContent(EntityService entityService) {
		this.entityService = entityService;
	}
	
	/**
	 * 
	 * @return
	 */
	public Contract getContract() {
		return contract;
	}
	
	/**
	 * 
	 * @param visible
	 */
	public void setCreditLineVisible(boolean visible) {
		txtContract.setVisible(visible);
	}
	
	/**
	 * 
	 * @return
	 */
	public Cashflow setCashFlowValue() {
		cashflow.setPaymentMethod(cbxPaymentMethod.getSelectedEntity());
		cashflow.setTreasuryType(cbxTreasuryType.getSelectedEntity());
		cashflow.setCashflowType(cbxType.getSelectedEntity());
		cashflow.setProductLine(cbxProductLine.getSelectedEntity());
		cashflow.setProductLineType(cbxProductLineType.getSelectedEntity());
		cashflow.setContract(contract);
		cashflow.setNumInstallment(getInteger(txtnumInstallment));
		cashflow.setTiInstallmentAmount(getDouble(txtTiInstallmentUsd, 0d));
		cashflow.setInstallmentDate(dfinstallmentDate.getValue());
		return cashflow;
	}
	
	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(false);
		formLayout.setStyleName("myform-align-left");
		formLayout.setSizeUndefined();
		return formLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	public com.vaadin.ui.Component createForm() {
		
		cbxTreasuryType = new ERefDataComboBox<ETreasuryType>(I18N.message("treasury"), ETreasuryType.class);
		cbxTreasuryType.setRequired(true);
		cbxTreasuryType.setWidth(180, Unit.PIXELS);
		cbxType = new ERefDataComboBox<ECashflowType>(I18N.message("cashflow.type"),ECashflowType.class);
		cbxType.setRequired(true);
		cbxType.setWidth(180, Unit.PIXELS);
		cbxPaymentMethod = new EntityRefComboBox<>();
		cbxPaymentMethod.setCaption(I18N.message("payment.method"));
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.setRequired(true);
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setWidth(180, Unit.PIXELS);
		cbxProductLine = new EntityRefComboBox<ProductLine>(I18N.message("product.line"));
		cbxProductLine.setRestrictions(new BaseRestrictions<ProductLine>(ProductLine.class));
		cbxProductLine.setRequired(true);
		cbxProductLine.renderer();
		cbxProductLine.setWidth(180, Unit.PIXELS);
		cbxProductLineType = new ERefDataComboBox<EProductLineType>(I18N.message("product.line.type"),EProductLineType.class);
		cbxProductLineType.setRequired(true);
		cbxProductLineType.setWidth(180, Unit.PIXELS);
		
		txtContract = ComponentFactory.getTextField("contract", true, 60, 150);
		txtContract.setImmediate(true);
		txtContract.setRequired(true);
		txtnumInstallment = ComponentFactory.getTextField("num.installment", true, 10, 150);
		txtnumInstallment.setRequired(true);
		txtTiInstallmentUsd = ComponentFactory.getTextField("ti.installment.usd", true, 10, 150);
		txtTiInstallmentUsd.setImmediate(true);
		txtTiInstallmentUsd.setRequired(true);
		
		dfinstallmentDate = ComponentFactory.getAutoDateField("installment.date", true);
		dfinstallmentDate.setValue(new Date());
		txtContract.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8554047040350185811L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(StringUtils.isNotEmpty(txtContract.getValue())){
					Contract contractItem = entityService.getById(Contract.class, Long.parseLong(txtContract.getValue()));
					if(contractItem != null){
						contract = contractItem;
					} else {
						txtContract.setValue("");
						setAlertMessageInvalidate("cant.find.this.item");
					}
				}
				
			}
		});

		HorizontalLayout form = new HorizontalLayout();
        FormLayout formPanel1 = getFormLayout();
        FormLayout formPanel2 = getFormLayout();
        form.setSpacing(true);
        formPanel1.addComponent(cbxTreasuryType);
        formPanel1.addComponent(cbxType);
        formPanel1.addComponent(cbxPaymentMethod);
        formPanel1.addComponent(cbxProductLineType);
        formPanel1.addComponent(cbxProductLine);
        formPanel2.addComponent(txtContract);
        formPanel2.addComponent(txtnumInstallment);
        formPanel2.addComponent(txtTiInstallmentUsd);
        formPanel2.addComponent(dfinstallmentDate);
        form.addComponent(formPanel1);
        form.addComponent(formPanel2);
        
		return form;
	}

	/**
	 * @param id
	 */
	public void assignValues(Long id) {
		if (id != null) {
			cashflow = entityService.getById(Cashflow.class, id);
			cbxTreasuryType.setSelectedEntity(cashflow.getTreasuryType());
			cbxPaymentMethod.setSelectedEntity(cashflow.getPaymentMethod());
			cbxType.setSelectedEntity(cashflow.getCashflowType());
			cbxProductLine.setSelectedEntity(cashflow.getProductLine());
			cbxProductLineType.setSelectedEntity(cashflow.getProductLineType());
			txtContract.setValue(getDefaultString(cashflow.getContract().getId()));
			txtnumInstallment.setValue(getDefaultString(cashflow.getNumInstallment()));
			txtTiInstallmentUsd.setValue(getDefaultString(cashflow.getTiInstallmentAmount()));
			dfinstallmentDate.setValue(cashflow.getInstallmentDate());
		}
	}
	
	/**
	 * Reset
	 */
	public void newCashFlow() {
		cashflow = new Cashflow();
		contract = new Contract();
		cbxTreasuryType.setSelectedEntity(null);
		cbxPaymentMethod.setSelectedEntity(null);
		cbxProductLine.setSelectedEntity(null);
		cbxType.setSelectedEntity(null);
		cbxProductLineType.setSelectedEntity(null);
		txtContract.setValue("");
		txtnumInstallment.setValue("");
		txtTiInstallmentUsd.setValue("");	
	}
	
	/**
	 * 
	 * @param message
	 */
	private void setAlertMessageInvalidate(String message) {
		Notification notification = new Notification("", Type.WARNING_MESSAGE);
		notification.setDescription(I18N.message(message));
		notification.setDelayMsec(3000);
		notification.show(Page.getCurrent());
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getValidate() {
		checkMandatorySelectField(cbxTreasuryType, "treasury");
		checkMandatorySelectField(cbxType, "cashflow.type");
		checkMandatorySelectField(cbxPaymentMethod, "payment.method");
		checkMandatorySelectField(cbxProductLine, "product.line");
		checkMandatorySelectField(cbxProductLineType, "product.line.type");
		checkMandatoryField(txtContract, "contract");
		checkMandatoryField(txtnumInstallment, "num.installment");
		checkMandatoryField(txtTiInstallmentUsd, "ti.installment.usd");
		return errors;
	}
	
	/**
	 * clear Errors List
	 */
	public void clearErrors() {
		errors.clear();
	}
}

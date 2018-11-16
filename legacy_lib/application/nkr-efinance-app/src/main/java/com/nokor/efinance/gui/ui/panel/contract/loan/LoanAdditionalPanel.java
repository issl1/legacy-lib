package com.nokor.efinance.gui.ui.panel.contract.loan;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class LoanAdditionalPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 525964912375590964L;
	
//	private ComboBox cbxDay;
//	private ComboBox cbxMonth;
//	private ComboBox cbxYear;
	private AutoDateField dfFirstDueDate;
	private TextField txtTaxInvoiceId;
	private TextField txtEngineNo;
	private TextField txtChassisNo;
	private ERefDataComboBox<EColor> cbxColor;
	private EntityComboBox<OrgStructure> cbxBranchInCharge;
	private TextField txtMileage;
	
	private ComboBox cbxPrepaidTerm;
	private Button btnEdit;
	private Button btnSave;
	private Button btnCancel;
	
	private TextField txtDealerTaxId;
	private TextField txtDealerBranch;
	
	private TextField txtTeInvoiceAmount;
	private TextField txtVatInvoiceAmount;
	private TextField txtTiInvoiceAmount;
	private DateField dfTaxInvoice;
	
	private Contract contract;
	
	private VerticalLayout messagePanel;
	
	public LoanAdditionalPanel() {
		init();
		
		FormLayout editLeftFormLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		editLeftFormLayout.addComponent(dfFirstDueDate);
		editLeftFormLayout.addComponent(cbxBranchInCharge);
		editLeftFormLayout.addComponent(txtTeInvoiceAmount);		
		editLeftFormLayout.addComponent(cbxColor);
		editLeftFormLayout.addComponent(cbxPrepaidTerm);

		
		FormLayout editCenterFormLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		editCenterFormLayout.addComponent(txtDealerTaxId);
		editCenterFormLayout.addComponent(txtTaxInvoiceId);
		editCenterFormLayout.addComponent(txtVatInvoiceAmount);		
		editCenterFormLayout.addComponent(txtEngineNo);
		editCenterFormLayout.addComponent(txtMileage);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		
		buttonLayout.addComponent(btnEdit);
		buttonLayout.addComponent(btnSave);
		buttonLayout.addComponent(ComponentFactory.getSpaceHeight(1, Unit.PIXELS));
		buttonLayout.addComponent(btnCancel);
		
		FormLayout editRightFormLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		editRightFormLayout.addComponent(txtDealerBranch);
		editRightFormLayout.addComponent(dfTaxInvoice);
		editRightFormLayout.addComponent(txtTiInvoiceAmount);
		editRightFormLayout.addComponent(txtChassisNo);
		editRightFormLayout.addComponent(buttonLayout);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		HorizontalLayout editLayout = new HorizontalLayout();
		editLayout.setMargin(new MarginInfo(false, true, false, true));	
		editLayout.setSpacing(true);
		editLayout.addComponent(editLeftFormLayout);
		editLayout.addComponent(editCenterFormLayout);
		editLayout.addComponent(editRightFormLayout);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(editLayout);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("additional.information"));
		fieldSet.setContent(contentLayout);
		
		Panel mainPanel = new Panel();
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		mainPanel.setContent(fieldSet);
		
		addComponent(mainPanel);
	}

	/**
	 * Display error message panel
	 */
	protected void displayAllErrorsPanel() {
		messagePanel.removeAllComponents();
		Label messageLabel = ComponentFactory.getHtmlLabel(ValidateUtil.getErrorMessages());
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}
	
	/**
	 * Removed error message panel
	 */
	protected void removedAllErrorsPanel() {
		ValidateUtil.clearErrors();
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
	}
	
	/**
	 * 
	 */
	private void init() {
		
		dfFirstDueDate = ComponentFactory.getAutoDateField("first.due.date", true);
		
		txtDealerTaxId = ComponentFactory.getTextField("dealer.tax.id", false, 60, 175);
		txtDealerTaxId.setEnabled(false);
		txtDealerBranch = ComponentFactory.getTextField("dealer.branch", false, 60, 175);
		txtDealerBranch.setEnabled(false);
		
		cbxColor = new ERefDataComboBox<>(I18N.message("color"), EColor.class);
		cbxColor.setWidth(175, Unit.PIXELS);
		cbxColor.setRequired(true);
		txtChassisNo = ComponentFactory.getTextField("chassis.no", true, 30, 175);
		txtEngineNo = ComponentFactory.getTextField("engine.no", true, 30, 175);
				
		cbxBranchInCharge = new EntityComboBox<>(OrgStructure.class, I18N.message("branch.in.charge"), "nameEn", "");
		cbxBranchInCharge.setImmediate(true);
		cbxBranchInCharge.renderer();
		cbxBranchInCharge.setRequired(true);
		cbxBranchInCharge.setWidth(175, Unit.PIXELS);
		txtMileage = ComponentFactory.getTextField("mileage", false, 60, 175);
		
		cbxPrepaidTerm = ComponentFactory.getComboBox();
		cbxPrepaidTerm.setCaption(I18N.message("prepaid.term"));
		cbxPrepaidTerm.setWidth(175, Unit.PIXELS);
		cbxPrepaidTerm.setNullSelectionAllowed(false);
		
		cbxPrepaidTerm.addItem(0);
		cbxPrepaidTerm.addItem(1);
		cbxPrepaidTerm.addItem(2);
		cbxPrepaidTerm.addItem(3);
		
		btnEdit = ComponentLayoutFactory.getDefaultButton("edit", FontAwesome.EDIT, 55);
		btnEdit.addClickListener(this);
		btnEdit.setVisible(true);
		
		btnSave = ComponentLayoutFactory.getDefaultButton("save", FontAwesome.SAVE, 55);
		btnSave.addClickListener(this);
		btnSave.setVisible(false);
		
		btnCancel = ComponentLayoutFactory.getButtonCancel();
		btnCancel.setWidth(55, Unit.PIXELS);
		btnCancel.addClickListener(this);
		
		dfTaxInvoice = ComponentFactory.getAutoDateField("tax.invoice.date", true);

		txtTaxInvoiceId = ComponentFactory.getTextField("tax.invoice.no", true, 30, 175);
		txtVatInvoiceAmount = ComponentFactory.getTextField("invoice.amount.vat", true, 30, 175);
		txtVatInvoiceAmount.setEnabled(false);
		txtTiInvoiceAmount = ComponentFactory.getTextField("invoice.amount.inc", true, 30, 175);
		txtTiInvoiceAmount.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 317485357239378352L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				double tiInvoiceAmount = getDouble(txtTiInvoiceAmount, 0d);
				Amount invoiceAmount = MyMathUtils.calculateFromAmountIncl(tiInvoiceAmount, contract.getVatValue());
				txtTeInvoiceAmount.setValue(AmountUtils.format(invoiceAmount.getTeAmount()));
				txtVatInvoiceAmount.setValue(AmountUtils.format(invoiceAmount.getVatAmount()));
				txtTiInvoiceAmount.setValue(AmountUtils.format(tiInvoiceAmount));
			}
		});
		
		txtTeInvoiceAmount = ComponentFactory.getTextField("invoice.amount.ex", true, 30, 175);
		txtTeInvoiceAmount.setEnabled(false);
	}
	
	/**
	 * AssignValue
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		reset();
		
		txtDealerTaxId.setValue(StringUtils.defaultString(contract.getDealer().getVatRegistrationNo()));
		txtDealerBranch.setValue(StringUtils.defaultString(contract.getDealer().getBranchNo()));
		
		txtTeInvoiceAmount.setValue(AmountUtils.format(MyNumberUtils.getDouble(contract.getTeInvoiceAmount())));
		txtVatInvoiceAmount.setValue(AmountUtils.format(MyNumberUtils.getDouble(contract.getVatInvoiceAmount())));
		txtTiInvoiceAmount.setValue(AmountUtils.format(MyNumberUtils.getDouble(contract.getTiInvoiceAmount())));
		dfTaxInvoice.setValue(contract.getTaxInvoiceDate());
		cbxBranchInCharge.setSelectedEntity(contract.getBranchInCharge());
		cbxPrepaidTerm.setValue(contract.getNumberPrepaidTerm() != null ? contract.getNumberPrepaidTerm() : 0);
		dfFirstDueDate.setValue(contract.getFirstDueDate());
		Asset asset = contract.getAsset();
		if (asset != null) {
			cbxColor.setSelectedEntity(asset.getColor());
			txtChassisNo.setValue(asset.getChassisNumber());
			txtEngineNo.setValue(asset.getEngineNumber());
			txtTaxInvoiceId.setValue(asset.getTaxInvoiceNumber());
			txtMileage.setValue(getDefaultString(asset.getMileage() != null ? asset.getMileage() : 0));
		}
		setControlEnable(false);
	}
	
	/**
	 * 
	 * @param isEnable
	 */
	private void setControlEnable(boolean isEnable) {
		cbxBranchInCharge.setEnabled(isEnable);
		cbxPrepaidTerm.setEnabled(isEnable);
		cbxColor.setEnabled(isEnable);
		txtChassisNo.setEnabled(isEnable);
		txtEngineNo.setEnabled(isEnable);
		txtTaxInvoiceId.setEnabled(isEnable);
		txtMileage.setEnabled(isEnable);
//		txtTeInvoiceAmount.setEnabled(isEnable);
		txtTiInvoiceAmount.setEnabled(isEnable);
		//txtVatInvoiceAmount.setEnabled(isEnable);
		//txtTiInvoiceAmount.setEnabled(isEnable);
		dfTaxInvoice.setEnabled(isEnable);
		dfFirstDueDate.setEnabled(isEnable);
		btnEdit.setVisible(ContractUtils.isBeforeActive(contract));
		btnCancel.setVisible(ContractUtils.isBeforeActive(contract));
	}
	
	/**
	 * reset
	 */
	public void reset() {
		cbxColor.setSelectedEntity(null);
		txtChassisNo.setValue("");
		txtEngineNo.setValue("");
		txtTaxInvoiceId.setValue("");
		txtMileage.setValue("0");
		cbxBranchInCharge.setSelectedEntity(null);
		
		txtDealerTaxId.setValue("");
		txtDealerBranch.setValue("");
		txtTeInvoiceAmount.setValue("");
		txtTiInvoiceAmount.setValue("");
		txtVatInvoiceAmount.setValue("");
		dfTaxInvoice.setValue(null);
		cbxPrepaidTerm.select(0);
		dfFirstDueDate.setValue(DateUtils.today());
	}
	
	/**
	 * Save 
	 */
	private void save() {
		if (validation()) {
			String prepaidTerm = cbxPrepaidTerm.getValue() != null ? String.valueOf(cbxPrepaidTerm.getValue()) : "0";
			
			contract.setFirstDueDate(dfFirstDueDate.getValue());
			contract.setLastDueDate(getLastDueDate(dfFirstDueDate.getValue()));
			contract.setNumberPrepaidTerm(MyNumberUtils.getInteger(prepaidTerm, 0));
			contract.setBranchInCharge(cbxBranchInCharge.getSelectedEntity());
			
			contract.setTeInvoiceAmount(getDouble(txtTeInvoiceAmount));
			contract.setVatInvoiceAmount(getDouble(txtVatInvoiceAmount));
			contract.setTiInvoiceAmount(getDouble(txtTiInvoiceAmount));
			contract.setTaxInvoiceDate(dfTaxInvoice.getValue());
			
			Asset asset = contract.getAsset();
			asset.setChassisNumber(txtChassisNo.getValue());
			asset.setEngineNumber(txtEngineNo.getValue());
			asset.setTaxInvoiceNumber(txtTaxInvoiceId.getValue());
			asset.setColor(cbxColor.getSelectedEntity());
			asset.setMileage(MyNumberUtils.getInteger(txtMileage.getValue(), 0));
			//asset.setTaxInvoiceAmount(getDouble(txtTeInvoiceAmount));
			try {
				CONT_SRV.updateContractAndAsset(contract);
				
				btnEdit.setCaption(I18N.message("edit"));
				btnEdit.setIcon(FontAwesome.EDIT);
				setControlEnable(false);
				ComponentLayoutFactory.displaySuccessfullyMsg();
				removedAllErrorsPanel();
			} catch (Exception e) {
				e.printStackTrace();
				ComponentLayoutFactory.displayErrorMsg(e.getMessage());
			}
			btnSave.setVisible(false);
			btnEdit.setVisible(true);
		} else {
			displayAllErrorsPanel();
		}
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	public Integer getInteger(ComboBox field) {
		try {
			return Integer.valueOf(Integer.parseInt((String) field.getValue()));
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validation() {
		ValidateUtil.clearErrors();
		ValidateUtil.checkMandatoryDateField(dfFirstDueDate, "first.due.date");
		ValidateUtil.checkMandatoryField(txtTaxInvoiceId, "tax.invoice.no");
		ValidateUtil.checkMandatorySelectField(cbxColor, "color");
		ValidateUtil.checkMandatorySelectField(cbxBranchInCharge, "branch.in.charge");
		ValidateUtil.checkIntegerField(txtMileage, "mileage");
		ValidateUtil.checkMandatoryDateField(dfTaxInvoice, "tax.invoice.date");
		ValidateUtil.checkMandatoryField(txtTeInvoiceAmount, "invoice.amount.ex");
		ValidateUtil.checkMandatoryField(txtTiInvoiceAmount, "invoic.amount.inc");
		ValidateUtil.checkMandatoryField(txtVatInvoiceAmount, "invoice.amount.vat");
		
		double financeAmount = contract.getTiFinancedAmount();
		double tiTaxInvoiceAmount = getDouble(txtTiInvoiceAmount);
		if (tiTaxInvoiceAmount < financeAmount) {
			ValidateUtil.addError(I18N.message("invoice.amount.inc.could.not.less.than.finance.amuont") + "(" + AmountUtils.format(financeAmount) + ")");
		}
		return StringUtils.isEmpty(ValidateUtil.getErrorMessages());
	}
	
	/**
	 * 
	 * @return
	 */
	protected boolean validationConfirmActive() {
		ValidateUtil.clearErrors();
		if (btnSave.isVisible()) {
			save();
		}
		Date firstDueDate = dfFirstDueDate.getValue();
		errors = CONT_ACTIVATION_SRV.validation(contract, firstDueDate, txtChassisNo.getValue(), txtEngineNo.getValue(), txtTaxInvoiceId.getValue());
		if (!errors.isEmpty()) {
			for (String error: errors) {
				ValidateUtil.addError(error);
			}
		}
		return StringUtils.isEmpty(ValidateUtil.getErrorMessages());
	}
	
	/**
	 * 
	 * @param firstDueDate
	 */
	private Date getLastDueDate(Date firstDueDate) {
		Integer term = MyNumberUtils.getInteger(contract.getTerm());
		Date lastDueDate = null;
		if (firstDueDate != null) {
			lastDueDate = DateUtils.addMonthsDate(DateUtils.addMonthsDate(firstDueDate, term), -1);
		} 
		return lastDueDate;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnEdit) {
			setControlEnable(true);
			btnSave.setVisible(true);
			btnEdit.setVisible(false);
		} else if (event.getButton() == btnSave) {
			save();
		} else if (event.getButton() == btnCancel) {
			assignValues(contract);
			btnEdit.setCaption(I18N.message("edit"));
			btnEdit.setIcon(FontAwesome.EDIT);
			removedAllErrorsPanel();
		}
	}

}

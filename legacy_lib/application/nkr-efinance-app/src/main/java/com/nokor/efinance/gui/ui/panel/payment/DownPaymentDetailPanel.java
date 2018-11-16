package com.nokor.efinance.gui.ui.panel.payment;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.widget.ManualPaymentMethodComboBox;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 */
public class DownPaymentDetailPanel extends AbstractTabPanel implements QuotationEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;
	
	private AutoDateField dfPaymentDate;
	private ManualPaymentMethodComboBox cbxPaymentMethod;
	private TextField txtPaymentAmount;
	private TextField txtDealerType;
	private TextField txtDealer;
	private TextField txtApplicant;
	private TextField txtRegistrationFee;
	private TextField txtAdvancePayment;
	private TextField txtServicingFee;
	private TextField txtInsuranceFee;
	private TextField txtOthers;
	private NativeButton btnReceive;
	
	private Quotation quotation;
	private ClickListener allocateListener;

	public DownPaymentDetailPanel() {
		super();
		setMargin(false);
		setImmediate(true);
		btnReceive = new NativeButton(I18N.message("receive"));
		btnReceive.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		btnReceive.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -94612598626779199L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (isValid()) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.receive.down.payment", txtApplicant.getValue()),
					        new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 2380193173874927880L;
								public void onClose(ConfirmDialog dialog) {
					                if (dialog.isConfirmed()) {
					                	dialog.close();
										allocateListener.buttonClick(null);
				                	}
				            	}
				    	});
						confirmDialog.setWidth("400px");
						confirmDialog.setHeight("150px");
						confirmDialog.setImmediate(true);
				}
			}
		});

		ToolbarButtonsPanel tblButtonsPanel = new ToolbarButtonsPanel();
		tblButtonsPanel.addButton(btnReceive);
		addComponent(tblButtonsPanel, 0);
	}
	
	/**
	 * @param cotraId
	 */
	public void assignValues(Long quotaId) {
		if (quotaId != null) {
			this.quotation = ENTITY_SRV.getById(Quotation.class, quotaId);
			Double tiRegistrationFeeUsd = getService("REGFEE", quotation.getQuotationServices());
			Double tiInsuranceFeeUsd = getService("INSFEE", quotation.getQuotationServices());
			Double tiServicingFeeUsd = getService("SERFEE", quotation.getQuotationServices());
			double tiTotalPaymentUsd = MyNumberUtils.getDouble(quotation.getTiAdvancePaymentAmount()) 
					+ MyNumberUtils.getDouble(tiInsuranceFeeUsd)
					+ MyNumberUtils.getDouble(tiServicingFeeUsd) 
					+ MyNumberUtils.getDouble(tiRegistrationFeeUsd);
			
			txtDealerType.setValue(quotation.getDealer() != null ? quotation.getDealer().getDealerType().getDesc() : "");
			txtDealer.setValue(quotation.getDealer().getNameEn());
			txtApplicant.setValue(quotation.getApplicant().getIndividual().getLastNameEn() + "  " + quotation.getApplicant().getIndividual().getFirstNameEn());
			txtRegistrationFee.setValue(AmountUtils.format(tiRegistrationFeeUsd));
			txtAdvancePayment.setValue(AmountUtils.format(quotation.getTiAdvancePaymentAmount()));
			txtServicingFee.setValue(AmountUtils.format(tiServicingFeeUsd));
			txtInsuranceFee.setValue(AmountUtils.format(tiInsuranceFeeUsd));
			txtOthers.setValue(AmountUtils.format(0d));
			txtPaymentAmount.setValue(AmountUtils.format(tiTotalPaymentUsd));
		}
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		
		final GridLayout gridLayout = new GridLayout(8, 6);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		dfPaymentDate = ComponentFactory.getAutoDateField("", false);
		dfPaymentDate.setValue(new Date());
		dfPaymentDate.setRequired(true);
				
		cbxPaymentMethod = new ManualPaymentMethodComboBox();
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setRequired(true);
		cbxPaymentMethod.setWidth("150px");
        
		txtPaymentAmount = ComponentFactory.getTextField(30, 150);
		txtPaymentAmount.setEnabled(false);
		
		txtDealerType = ComponentFactory.getTextField(100, 200);
		txtDealerType.setEnabled(false);
		txtDealer = ComponentFactory.getTextField(100, 200);
		txtDealer.setEnabled(false);
		txtApplicant = ComponentFactory.getTextField(100, 150);
		txtApplicant.setEnabled(false);
		
		txtRegistrationFee = ComponentFactory.getTextField(30, 150);
		txtRegistrationFee.setEnabled(false);
		
		txtAdvancePayment = ComponentFactory.getTextField(30, 150);
		txtAdvancePayment.setEnabled(false);
		txtServicingFee = ComponentFactory.getTextField(30, 150);
		txtServicingFee.setEnabled(false);
		txtInsuranceFee = ComponentFactory.getTextField(30, 150);
		txtInsuranceFee.setEnabled(false);
		
		txtOthers = ComponentFactory.getTextField(30, 150);
		txtOthers.setEnabled(false);
		
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(txtDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(txtDealer, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("applicant")), iCol++, 1);
        gridLayout.addComponent(txtApplicant, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("servicing.fee")), iCol++, 1);
        gridLayout.addComponent(txtServicingFee, iCol++, 1);
		
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("advance.payment")), iCol++, 2);
        gridLayout.addComponent(txtAdvancePayment, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
        gridLayout.addComponent(new Label(I18N.message("insurance.fee")), iCol++, 2);
        gridLayout.addComponent(txtInsuranceFee, iCol++, 2);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("registration.fee")), iCol++, 3);
        gridLayout.addComponent(txtRegistrationFee, iCol++, 3);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 3);
        gridLayout.addComponent(new Label(I18N.message("total.amount")), iCol++, 3);
        gridLayout.addComponent(txtPaymentAmount, iCol++, 3);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("other.amount")), iCol++, 4);
        gridLayout.addComponent(txtOthers, iCol++, 4);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 4);
        gridLayout.addComponent(new Label(I18N.message("payment.method")), iCol++, 4);
        gridLayout.addComponent(cbxPaymentMethod, iCol++, 4);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("payment.date")), iCol++, 5);
        gridLayout.addComponent(dfPaymentDate, iCol++, 5);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 5);
		
        contentLayout.setSpacing(true);
        contentLayout.addComponent(gridLayout);

        return contentLayout;
	}

	/**
	 * @return
	 */
	public Date getPaymentDate() {
		return dfPaymentDate.getValue();
	}
	
	/**
	 * @return
	 */
	public EPaymentMethod getPaymentMethod() {
		return cbxPaymentMethod.getSelectedEntity();
	}
	
	/**
	 * @param allocateListener the allocateListener to set
	 */
	public void setAllocateListener(ClickListener allocateListener) {
		this.allocateListener = allocateListener;
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		reset();
		checkMandatoryDateField(dfPaymentDate, "payment.date");
		checkMandatorySelectField(cbxPaymentMethod, "payment.method");
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * @param code
	 * @param quotationServices
	 * @return
	 */
	private Double getService(String code, List<QuotationService> quotationServices) {
		for (QuotationService quotationService : quotationServices) {
			if (code.equals(quotationService.getService().getCode()) && !quotationService.isSplitWithInstallment()) {
				return quotationService.getTiPrice();
			}
		}
		return null;
	}
}

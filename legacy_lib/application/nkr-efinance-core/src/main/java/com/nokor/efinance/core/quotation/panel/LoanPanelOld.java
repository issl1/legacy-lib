package com.nokor.efinance.core.quotation.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.service.DocumentService;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Loan panel
 * @author ly.youhort
 */
public class LoanPanelOld extends AbstractControlPanel implements FinServicesHelper {
	
	private static final long serialVersionUID = 8341162309347917428L;
		
	private final DocumentService documentService = SpringUtils.getBean(DocumentService.class);
	
	private EntityRefComboBox<FinProduct> cbxFinancialProduct;
	
	private AutoDateField dfFirstDueDate;
	private AutoDateField dfLastDueDate;
	
	private TextField txtContractID;
	private TextField txtProductReference;
	private TextField txtProductName;
	private TextField txtProductVat;
	private TextField txtFinancialProduct;
	private TextField txtDefaultFinanceAmount;
	private TextField txtFinanceAmount;
	private TextField txtDownPayment;
	private TextField txtDownPaymentPercentage;
	private TextField txtEffRate;
	private TextField txtFlatRate;
	private TextField txtTerms;
	private TextField txtInstallmentAmount;
	private TextField txtVatInstallmentAmount;
	private TextField txtNetInstallmentAmount;
	private TextField txtApplicationID;
	// Right part
	private AutoDateField dfApplicationDate;
	private AutoDateField dfApprovalDate;
	private AutoDateField dfBookingDate;
	
	private TextField txtTotalAR;
	private TextField txtTotalUE;
	private TextField txtTotalVat;
	private TextField txtNbGuarantor;
	private TextField txtPrepaidInstallmentAtShop;
	private TextField txtNbPrepaidTerm;
	private TextField txtServiceFee;
	private TextField txtInsuranceFee;
	
	private CheckBox cbGuarantor;
	
	// Document part
	private List<CheckBox> cbDocuments;
	private Panel documentGroupPanel;
		
	/**
	 * 
	 * @param delegate
	 */
	public LoanPanelOld(/*ContractFormPanel delegate*/) {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		
		cbxFinancialProduct = new EntityRefComboBox<>();
		cbxFinancialProduct.setRestrictions(new BaseRestrictions<>(FinProduct.class));
		cbxFinancialProduct.renderer();
		cbxFinancialProduct.setImmediate(true);
		cbxFinancialProduct.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 269148483398382863L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxFinancialProduct.getSelectedEntity() != null) {
					txtFinancialProduct.setValue(cbxFinancialProduct.getSelectedEntity().getDescEn());
				} else {
					txtFinancialProduct.setValue("");
				}
			}
		});
		
		txtContractID = ComponentFactory.getTextField(false, 60, 220);
		txtApplicationID = ComponentFactory.getTextField(false, 60, 220);
		txtProductReference = ComponentFactory.getTextField(false, 60, 220);		
		txtProductName = ComponentFactory.getTextField(false, 60, 220);	
		txtProductVat = ComponentFactory.getTextField(false, 60, 220);
		txtFinancialProduct = ComponentFactory.getTextField(false, 60, 150);
		txtDefaultFinanceAmount = ComponentFactory.getTextField(false, 60, 220);
		txtFinanceAmount = ComponentFactory.getTextField(false, 60, 220);
		txtDownPayment = ComponentFactory.getTextField(false, 60, 220);
		txtDownPaymentPercentage = ComponentFactory.getTextField(false, 60, 220);
		txtEffRate = ComponentFactory.getTextField(false, 60, 220);
		txtFlatRate = ComponentFactory.getTextField(false, 60, 220);
		txtTerms = ComponentFactory.getTextField(false, 60, 220);
		txtInstallmentAmount = ComponentFactory.getTextField(false, 60, 220);
		txtVatInstallmentAmount = ComponentFactory.getTextField(false, 60, 220);
		txtNetInstallmentAmount = ComponentFactory.getTextField(false, 60, 220);
		dfFirstDueDate = ComponentFactory.getAutoDateField();
		dfLastDueDate = ComponentFactory.getAutoDateField();

		// Right
		txtTotalAR = ComponentFactory.getTextField(false, 60, 220);
		txtTotalUE = ComponentFactory.getTextField(false, 60, 220);
		txtTotalVat = ComponentFactory.getTextField(false, 60, 220);
		txtNbGuarantor = ComponentFactory.getTextField(false, 60, 220);
		txtPrepaidInstallmentAtShop = ComponentFactory.getTextField(false, 60, 220);
		txtNbPrepaidTerm = ComponentFactory.getTextField(false, 60, 220);
		txtServiceFee = ComponentFactory.getTextField(false, 60, 220);
		txtInsuranceFee = ComponentFactory.getTextField(false, 60, 220);
		dfApplicationDate = ComponentFactory.getAutoDateField();
		dfApprovalDate = ComponentFactory.getAutoDateField();
		dfBookingDate = ComponentFactory.getAutoDateField();
		cbGuarantor = new CheckBox();
		
		// Document 
		documentGroupPanel = new Panel();
		documentGroupPanel.setWidth("370px");
		cbDocuments = new ArrayList<>();
		
		List<Document> documents = documentService.getDocuments();
				
        if (documents != null && !documents.isEmpty()) {
        	VerticalLayout panelGroupLayout = new VerticalLayout();
        	int i = 1;
        	GridLayout gridLayout = new GridLayout(3, documents.size() + 1);
        	gridLayout.setSpacing(true);
        	gridLayout.setMargin(true);
        	for (Document document : documents) {
        		CheckBox cbDocument = new CheckBox();
        		cbDocument.setCaption(document.getDescEn());
        		cbDocument.setData(document);
        		cbDocuments.add(cbDocument);
        		gridLayout.addComponent(cbDocument, 0, i);
        		i++;
        	}
        	panelGroupLayout.addComponent(gridLayout);
        	documentGroupPanel.setContent(panelGroupLayout);
        }
				
		setEnabledLoanControls(false);
		//Call method Load form 
		createLoanForm();
	}
	
	
	/**
	 * Method Create Loan Form in QuotationForm Panel
	 */
	private void createLoanForm(){
		String template = "quotationLoanLayout";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		/**
		 * set Item to Customer Layout
		 */
		// Left
		customLayout.addComponent(new Label(I18N.message("contract.id")), "lblContractID");
		customLayout.addComponent(txtContractID, "txtContractID");
		customLayout.addComponent(new Label(I18N.message("application.id")), "lblApplicationID");
		customLayout.addComponent(txtApplicationID, "txtApplicationID");
		customLayout.addComponent(new Label(I18N.message("product.reference")), "lblProductReference");
		customLayout.addComponent(txtProductReference, "txtProductReference");
		customLayout.addComponent(new Label(I18N.message("name.product")), "lblProductName");
		customLayout.addComponent(txtProductName, "txtProductName");
		customLayout.addComponent(new Label(I18N.message("product.vat")), "lblProductVat");
		customLayout.addComponent(txtProductVat, "txtProductVat");
		customLayout.addComponent(new Label(I18N.message("marketing.campaign")), "lblMarketingCampaign");
		customLayout.addComponent(cbxFinancialProduct, "cbxMarketingCampaign");
		customLayout.addComponent(txtFinancialProduct, "txtMarketingCampaign");
		customLayout.addComponent(new Label(I18N.message("default.finance.amount")), "lblDefaultFinanceAmount");
		customLayout.addComponent(txtDefaultFinanceAmount, "txtDefaultFinanceAmount");
		customLayout.addComponent(new Label(I18N.message("finance.amount")), "lblFinanceAmount");
		customLayout.addComponent(txtFinanceAmount, "txtFinanceAmount");
		customLayout.addComponent(new Label(I18N.message("down.payment")), "lblDownPayment");
		customLayout.addComponent(txtDownPayment, "txtDownPayment");
		customLayout.addComponent(new Label(I18N.message("down.payment.percentage")), "lblDownPaymentPercentage");
		customLayout.addComponent(txtDownPaymentPercentage, "txtDownPaymentPercentage");
		customLayout.addComponent(new Label(I18N.message("eff.rate")), "lblEffRate");
		customLayout.addComponent(txtEffRate, "txtEffRate");
		customLayout.addComponent(new Label(I18N.message("flat.rate")), "lblFlatRate");
		customLayout.addComponent(txtFlatRate, "txtFlatRate");
		customLayout.addComponent(new Label(I18N.message("terms")), "lblTerms");
		customLayout.addComponent(txtTerms, "txtTerms");
		customLayout.addComponent(new Label(I18N.message("first.due.date")), "lblFirstDueDate");
		customLayout.addComponent(dfFirstDueDate, "dfFirstDueDate");
		customLayout.addComponent(new Label(I18N.message("last.due.date")), "lblLastDueDate");
		customLayout.addComponent(dfLastDueDate, "dfLastDueDate");
		customLayout.addComponent(new Label(I18N.message("installment")), "lblInstallmentAmount");
		customLayout.addComponent(txtInstallmentAmount, "txtInstallmentAmount");
		customLayout.addComponent(new Label(I18N.message("vat.installment")), "lblVatInstallmentAmount");
		customLayout.addComponent(txtVatInstallmentAmount, "txtVatInstallmentAmount");
		customLayout.addComponent(new Label(I18N.message("net.installment")), "lblNetInstallmentAmount");
		customLayout.addComponent(txtNetInstallmentAmount, "txtNetInstallmentAmount");
		// Right
		customLayout.addComponent(new Label(I18N.message("application.date")), "lblApplicationDate");
		customLayout.addComponent(dfApplicationDate, "dfApplicationDate");
		customLayout.addComponent(new Label(I18N.message("approval.date")), "lblApprovalDate");
		customLayout.addComponent(dfApprovalDate, "dfApprovalDate");
		customLayout.addComponent(new Label(I18N.message("booking.date")), "lblBookingDate");
		customLayout.addComponent(dfBookingDate, "dfBookingDate");
		customLayout.addComponent(new Label(I18N.message("total.ar")), "lblTotalAR");
		customLayout.addComponent(txtTotalAR, "txtTotalAR");
		customLayout.addComponent(new Label(I18N.message("total.ue")), "lblTotalUE");
		customLayout.addComponent(txtTotalUE, "txtTotalUE");
		customLayout.addComponent(new Label(I18N.message("total.vat")), "lblTotalVat");
		customLayout.addComponent(txtTotalVat, "txtTotalVat");
		customLayout.addComponent(new Label(I18N.message("guarantor")), "lblGuarantor");
		customLayout.addComponent(cbGuarantor, "cbGuarantor");
		customLayout.addComponent(new Label(I18N.message("number.guarantor")), "lblNbGuarantor");
		customLayout.addComponent(txtNbGuarantor, "txtNbGuarantor");
		customLayout.addComponent(new Label(I18N.message("prepaid.installment.at.shop")), "lblPrepaidInstallmentAtShop");
		customLayout.addComponent(txtPrepaidInstallmentAtShop, "txtPrepaidInstallmentAtShop");
		customLayout.addComponent(new Label(I18N.message("number.prepaid.term")), "lblNbPrepaidTerm");
		customLayout.addComponent(txtNbPrepaidTerm, "txtNbPrepaidTerm");
		customLayout.addComponent(new Label(I18N.message("service.fee")), "lblServiceFee");
		customLayout.addComponent(txtServiceFee, "txtServiceFee");
		customLayout.addComponent(new Label(I18N.message("insurance.fee")), "lblInsuranceFee");
		customLayout.addComponent(txtInsuranceFee, "txtInsuranceFee");
		// Document
		if (ProfileUtil.isCMProfile()) {
			customLayout.addComponent(documentGroupPanel, "documentPanel");
		}
		addComponent(customLayout);
	}
	
	/**
	 * 
	 * @param quotation
	 * @return
	 */
	public Quotation getQuotation(Quotation quotation) {
		quotation.setFinancialProduct(cbxFinancialProduct.getSelectedEntity());
		quotation.setTiDefaultFinanceAmount(getDouble(txtDefaultFinanceAmount));
		quotation.setTeDefaultFinanceAmount(getDouble(txtDefaultFinanceAmount));
		quotation.setVatDefaultFinanceAmount(0.0);
		quotation.setTiFinanceAmount(getDouble(txtFinanceAmount));
		quotation.setTeFinanceAmount(getDouble(txtFinanceAmount));
		quotation.setVatFinanceAmount(0.0);
		quotation.setTiAdvancePaymentAmount(getDouble(txtDownPayment));
		quotation.setTeAdvancePaymentAmount(getDouble(txtDownPayment));
		quotation.setVatAdvancePaymentAmount(0.0);
		quotation.setAdvancePaymentPercentage(getDouble(txtDownPaymentPercentage));
		quotation.setInterestRate(getDouble(txtEffRate));
		quotation.setIrrRate(getDouble(txtFlatRate));
		quotation.setTerm(getInteger(txtTerms));
		quotation.setTiInstallmentAmount(getDouble(txtInstallmentAmount));
		quotation.setTeInstallmentAmount(getDouble(txtNetInstallmentAmount));
		quotation.setVatInstallmentAmount(getDouble(txtVatInstallmentAmount));
		quotation.setAcceptationDate(dfApprovalDate.getValue());
		quotation.setBookingDate(dfBookingDate.getValue());
		quotation.setTotalAR(getDouble(txtTotalAR));
		quotation.setTotalUE(getDouble(txtTotalUE));
		quotation.setTotalVAT(getDouble(txtTotalVat));
		// quotation.setTiPrepaidInstallment(getDouble(txtPrepaidInstallmentAtShop));
		// quotation.setNumberPrepaidTerm(getInteger(txtNbPrepaidTerm));
		
		Double servicingFee = getDouble(txtServiceFee);
		Double insuranceFee = getDouble(txtInsuranceFee);
		
		saveQuotationService(quotation, EServiceType.SRVFEE, servicingFee);
		saveQuotationService(quotation, EServiceType.INSFEE, insuranceFee);
		quotation.setQuotationDocuments(getDocuments(quotation));
		
		return quotation;
	}
	
	/**
	 * 
	 * @param quotation
	 * @param serviceType
	 * @param value
	 */
	private void saveQuotationService(Quotation quotation, EServiceType serviceType, Double value) {
		List<QuotationService> quotationServices = quotation.getQuotationServices();
		if (quotationServices == null) {
			quotationServices = new ArrayList<>();
		}
		QuotationService servicingService = quotation.getQuotationService(serviceType.getCode());
		if (servicingService == null) {
			servicingService = new QuotationService();
			servicingService.setCrudAction(CrudAction.CREATE);
			servicingService.setService(QUO_SRV.getByCode(FinService.class, serviceType.getCode()));
			quotationServices.add(servicingService);
		} else {
			servicingService.setCrudAction(CrudAction.UPDATE);
		}
		servicingService.setQuotation(quotation);
		servicingService.setTePrice(value);
		servicingService.setVatPrice(0d);
		servicingService.setTiPrice(value);
		quotation.setQuotationServices(quotationServices);
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public List<QuotationDocument> getDocuments(Quotation quotation) {
		
		List<QuotationDocument> quotationDocuments = quotation.getQuotationDocuments();
		if (quotationDocuments == null) {
			quotationDocuments = new ArrayList<QuotationDocument>();
		} else {
			// By default delete all existing QuotationService
			// The update the flag UPDATE or CREATE will be done later.
			for (QuotationDocument quotationDocument : quotationDocuments) {
				quotationDocument.setCrudAction(CrudAction.DELETE);
			}
		}
		
		if (cbDocuments != null && !cbDocuments.isEmpty()) {
			for (int i = 0; i < cbDocuments.size(); i++) {
				CheckBox cbDocument = cbDocuments.get(i);
				if (cbDocument.getValue()) {
					Document document = (Document) cbDocument.getData();
					
					QuotationDocument quotationDocument = quotation.getQuotationDocument(document.getId());
					if (quotationDocument == null) {
						quotationDocument = new QuotationDocument();
						quotationDocument.setCrudAction(CrudAction.CREATE);
						quotationDocuments.add(quotationDocument);
					} else {
						quotationDocument.setCrudAction(CrudAction.UPDATE);
					}
					quotationDocument.setDocument(document);
					quotationDocument.setQuotation(quotation);
				}
			}
		}
		
		quotation.setQuotationDocuments(quotationDocuments);
		return quotationDocuments;
	}
	
	/**
	 * 
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {		
		txtContractID.setValue(getDefaultString(quotation.getReference()));
	    dfFirstDueDate.setValue(quotation.getFirstDueDate());
	    dfLastDueDate.setValue(quotation.getLastDueDate());
	    
		txtApplicationID.setValue(getDefaultString(quotation.getExternalReference()));
						
		if (quotation.getFinancialProduct() != null) {
			txtProductReference.setValue(getDefaultString(quotation.getFinancialProduct().getCode()));
			txtProductName.setValue(getDefaultString(quotation.getFinancialProduct().getDescEn()));
			txtProductVat.setValue(AmountUtils.format(0d));
		}
		if (quotation.getFinancialProduct() != null) {
			cbxFinancialProduct.setSelectedEntity(quotation.getFinancialProduct());
			txtFinancialProduct.setValue(getDefaultString(quotation.getFinancialProduct().getDescEn()));
		}
		txtDefaultFinanceAmount.setValue(AmountUtils.format(quotation.getTiDefaultFinanceAmount()));
		txtFinanceAmount.setValue(AmountUtils.format(quotation.getTiFinanceAmount()));
		txtDownPayment.setValue(AmountUtils.format(quotation.getTiAdvancePaymentAmount()));
		txtDownPaymentPercentage.setValue(getDefaultString(quotation.getAdvancePaymentPercentage()));
		txtEffRate.setValue(AmountUtils.format(quotation.getInterestRate()));
		txtFlatRate.setValue(AmountUtils.format(quotation.getIrrRate()));
		txtTerms.setValue(getDefaultString(quotation.getTerm()));
		
		txtInstallmentAmount.setValue(AmountUtils.format(quotation.getTiInstallmentAmount()));
		txtVatInstallmentAmount.setValue(AmountUtils.format(quotation.getVatInstallmentAmount()));
		txtNetInstallmentAmount.setValue(AmountUtils.format(quotation.getTeInstallmentAmount()));
		dfApplicationDate.setValue(quotation.getQuotationDate());
		dfApprovalDate.setValue(quotation.getAcceptationDate());
		dfBookingDate.setValue(quotation.getBookingDate());
		txtTotalAR.setValue(AmountUtils.format(quotation.getTotalAR()));
		txtTotalUE.setValue(AmountUtils.format(quotation.getTotalUE()));
		txtTotalVat.setValue(AmountUtils.format(quotation.getTotalVAT()));
		cbGuarantor.setValue(quotation.getQuotationApplicant(EApplicantType.G) != null);
		cbGuarantor.setStyleName("checkbox_unchange_disabled_color");
		if (cbGuarantor.getValue()) {
			txtNbGuarantor.setValue(getDefaultString(1));
		} else {
			txtNbGuarantor.setValue(getDefaultString(0));
		}
		// txtPrepaidInstallmentAtShop.setValue(AmountUtils.format(quotation.getTiPrepaidInstallment()));
		// txtNbPrepaidTerm.setValue(getDefaultString(quotation.getNumberPrepaidTerm()));
		List<QuotationService> quotationServices = quotation.getQuotationServices();
		if (quotationServices != null && !quotationServices.isEmpty()) {
			for (QuotationService quotationService : quotationServices) {
				if (quotationService.getService() != null) {
					if (EServiceType.INSFEE.getCode().equals(quotationService.getService().getCode())) {
						txtInsuranceFee.setValue(getDefaultString(quotationService.getTiPrice()));	
					} else if (EServiceType.SRVFEE.getCode().equals(quotationService.getService().getCode())) {
						txtServiceFee.setValue(getDefaultString(quotationService.getTiPrice()));
					}
				}
			}
		}
		
		List<QuotationDocument> documents = quotation.getQuotationDocuments();
		if (cbDocuments != null && !cbDocuments.isEmpty()) {
			for (int i = 0; i < cbDocuments.size(); i++) {
				CheckBox cbDocument = cbDocuments.get(i);
				Document document = (Document) cbDocument.getData();
				boolean found = false;
				if (documents != null && !documents.isEmpty()) {
					for (QuotationDocument quotationDocument : documents) {
						if (quotationDocument != null && quotationDocument.getDocument() != null) {
							if (document.getId().equals(quotationDocument.getDocument().getId())) {
								cbDocument.setValue(true);
								found = true;
								break;
							}
						}
					}
				}
				if (!found) {
					cbDocument.setValue(false);	
				}
			}
		}
	}
	
	/**
	 * @param enabled
	 */
	public void setEnabledLoanControls(boolean enabled) {
		cbxFinancialProduct.setEnabled(enabled);
		txtApplicationID.setEnabled(enabled);
		txtProductReference.setEnabled(enabled);
		txtProductName.setEnabled(enabled);
		txtProductVat.setEnabled(enabled);
		txtFinancialProduct.setEnabled(enabled);
		txtDefaultFinanceAmount.setEnabled(enabled);
		txtFinanceAmount.setEnabled(enabled);
		txtDownPayment.setEnabled(enabled);
		txtDownPaymentPercentage.setEnabled(enabled);
		txtEffRate.setEnabled(enabled);
		txtFlatRate.setEnabled(enabled);
		txtTerms.setEnabled(enabled);
		dfLastDueDate.setEnabled(enabled);
		txtInstallmentAmount.setEnabled(enabled);
		txtVatInstallmentAmount.setEnabled(enabled);
		txtNetInstallmentAmount.setEnabled(enabled);
		dfApplicationDate.setEnabled(enabled);
		dfApprovalDate.setEnabled(enabled);
		cbGuarantor.setEnabled(enabled);
		txtNbGuarantor.setEnabled(enabled);
		txtInsuranceFee.setEnabled(enabled);
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		cbxFinancialProduct.setSelectedEntity(null);
		dfFirstDueDate.setValue(null);
		dfLastDueDate.setValue(null);
		txtContractID.setValue("");
		txtProductReference.setValue("");
		txtProductName.setValue("");
		txtProductVat.setValue("");
		txtFinancialProduct.setValue("");
		txtDefaultFinanceAmount.setValue("");
		txtFinanceAmount.setValue("");
		txtDownPayment.setValue("");
		txtDownPaymentPercentage.setValue("");
		txtEffRate.setValue("");
		txtFlatRate.setValue("");
		txtTerms.setValue("");
		txtInstallmentAmount.setValue("");
		txtVatInstallmentAmount.setValue("");
		txtNetInstallmentAmount.setValue("");
		txtApplicationID.setValue("");
		dfApplicationDate.setValue(null);
		dfApprovalDate.setValue(null);
		dfBookingDate.setValue(null);
		txtTotalAR.setValue("");
		txtTotalUE.setValue("");
		txtTotalVat.setValue("");
		txtNbGuarantor.setValue("");
		txtPrepaidInstallmentAtShop.setValue("");
		txtNbPrepaidTerm.setValue("");
		txtServiceFee.setValue("");
		txtInsuranceFee.setValue("");
		cbGuarantor.setValue(false);
		if (cbDocuments != null && !cbDocuments.isEmpty()) {
			for (int i = 0; i < cbDocuments.size(); i++) {
				cbDocuments.get(i).setValue(false);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> isValid() {	
		super.reset();
		for (int i = 0; i < cbDocuments.size(); i++) {
			CheckBox cbDocument = cbDocuments.get(i);
			Document document = (Document) cbDocument.getData();
			if (document.isMandatory() && !cbDocument.getValue()) {
				errors.add(I18N.message("document.required.1", new String[] 
						{document.getApplicantType() + "-" + document.getDescEn()}));
			}
		}
		return errors;
	}
}
package com.nokor.efinance.gui.ui.panel.report.contract.overdue;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.panel.ApplicantPanel;
import com.nokor.efinance.core.applicant.panel.address.AddressesPanel;
import com.nokor.efinance.core.applicant.panel.guarantor.GuarantorPanel;
import com.nokor.efinance.core.asset.panel.AssetPanelOld;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.document.panel.DocumentsPanel;
import com.nokor.efinance.core.financial.panel.product.FinProductPanel;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.quotation.panel.comment.CommentsPanel;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Form panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OverdueContractFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 4128202456541713088L;
	
	private ContractService contractService = SpringUtils.getBean(ContractService.class);
	
	private NavigationPanel navigationPanel;
	private Button btnRepossessed;
	
	private Panel contractInfoPanel;
	private TabSheet accordionPanel;
	private ApplicantPanel applicantPanel;
	private GuarantorPanel guarantorPanel;
	private AssetPanelOld assetPanel;
	private DocumentsPanel documentsPanel;
	private CommentsPanel commentsPanel;
	private CollectionHistoryPanel collectionHistoryPanel;
	private FinProductPanel financialProductPanel;
	private PaymentSchedulePanel paymentSchedulePanel;
	private UnpaidInstallmentsPanel unpaidInstallmentsPanel;
	private ContractDocumentsPanel contractDocumentsPanel;
	private ChangeCollectionOfficerPanel changeCollectionOfficerPanel;
	private PaidOffPanel paidOffPanel;
	private AddressesPanel addressesPanel;
	
	private Contract contract;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
        super.init();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		contractInfoPanel = new Panel();
		navigationPanel = new NavigationPanel();
		btnRepossessed = new NativeButton(I18N.message("repossess"));
 		btnRepossessed.setIcon(new ThemeResource("../nkr-default/icons/16/tick.png"));
 		navigationPanel.addButton(btnRepossessed);
 		btnRepossessed.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 3637046509666055529L;
			@Override
			public void buttonClick(ClickEvent event) {
				/*
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("request.repossess.contract"),
			        new ConfirmDialog.Listener() {
						private static final long serialVersionUID = 2380193173874927880L;
						public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
			                	contract = contractService.requestRepossess(contract.getId());
			                }
			            }
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");
				*/
				
				UI.getCurrent().addWindow(new RepossessedPopupPanel(ContractWkfStatus.REP, contract));
			}
 		});
		
		accordionPanel = new TabSheet();
		applicantPanel = new ApplicantPanel();
		guarantorPanel = new GuarantorPanel();
		assetPanel = new AssetPanelOld();
		documentsPanel = new DocumentsPanel();
		commentsPanel = new CommentsPanel();
		collectionHistoryPanel = new CollectionHistoryPanel();
		financialProductPanel = new FinProductPanel();
		paymentSchedulePanel = new PaymentSchedulePanel();
		unpaidInstallmentsPanel = new UnpaidInstallmentsPanel();
		contractDocumentsPanel = new ContractDocumentsPanel();
		changeCollectionOfficerPanel = new ChangeCollectionOfficerPanel();
		paidOffPanel = new PaidOffPanel();
		addressesPanel = new AddressesPanel();
		
		accordionPanel.addTab(collectionHistoryPanel, I18N.message("collection.history"));
		accordionPanel.addTab(commentsPanel, I18N.message("comments"));
		if (ProfileUtil.isCollectionSupervisor()) {
			accordionPanel.addTab(changeCollectionOfficerPanel, I18N.message("change.officer"));
		}
		accordionPanel.addTab(paymentSchedulePanel, I18N.message("payment.schedule"));
		accordionPanel.addTab(unpaidInstallmentsPanel, I18N.message("unpaid.installments"));
		accordionPanel.addTab(applicantPanel, I18N.message("applicant"));
		accordionPanel.addTab(guarantorPanel, I18N.message("guarantor"));
		accordionPanel.addTab(assetPanel, I18N.message("asset"));
		accordionPanel.addTab(financialProductPanel, I18N.message("financial.product"));
		accordionPanel.addTab(documentsPanel, I18N.message("documents"));
		accordionPanel.addTab(contractDocumentsPanel, I18N.message("collection.documents"));
		accordionPanel.addTab(paidOffPanel, I18N.message("paid.off"));
		accordionPanel.addTab(addressesPanel, I18N.message("addresses"));
		
		if (ProfileUtil.isCollectionSupervisor()) {
			contentLayout.addComponent(navigationPanel);
		}
		
		contentLayout.addComponent(contractInfoPanel);
		contentLayout.addComponent(accordionPanel);

		
		
		return contentLayout;
	}

	/**
	 * @param contract
	 */
	private void displayContractInfo(final Contract contract) {
		String template = "contractInfo";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout inputFieldLayout = null;
		try {
			inputFieldLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		inputFieldLayout.setSizeFull();
		inputFieldLayout.addComponent(new Label(I18N.message("applicant")), "lblApplicant");
		inputFieldLayout.addComponent(new Label("<b>" + contract.getApplicant().getIndividual().getLastNameEn() + " " + 
										  contract.getApplicant().getIndividual().getFirstNameEn() + "</b>", ContentMode.HTML), "lnkApplicant");
		inputFieldLayout.addComponent(new Label(I18N.message("reference")), "lblReference");
		inputFieldLayout.addComponent(new Label("<b>" + contract.getReference() + "</b>", ContentMode.HTML), "lblReferenceValue");
		inputFieldLayout.addComponent(new Label(I18N.message("startdate") ), "lblStartDate");
		inputFieldLayout.addComponent(new Label("<b>" + DateUtils.date2StringDDMMYYYY_SLASH(contract.getStartDate()) + "</b>", ContentMode.HTML), "lblStartDateValue");
		inputFieldLayout.addComponent(new Label(I18N.message("enddate")), "lblEndDate");
		inputFieldLayout.addComponent(new Label("<b>" + DateUtils.date2StringDDMMYYYY_SLASH(contract.getEndDate()) + "</b>", ContentMode.HTML), "lblEndDateValue");
		inputFieldLayout.addComponent(new Label(I18N.message("penalty.rule")), "lblPenaltyRule");
		inputFieldLayout.addComponent(new Label(contract.getPenaltyRule() != null ? contract.getPenaltyRule().getDescEn() : ""), "txtPenaltyRule");
        
		inputFieldLayout.addComponent(new Label(I18N.message("guarantor")), "lblGuarantor");
		if (contract.getGuarantor() != null) {
			inputFieldLayout.addComponent(new Label("<b>" + contract.getGuarantor().getIndividual().getLastNameEn() + " " + 
											  contract.getGuarantor().getIndividual().getFirstNameEn() + "</b>", ContentMode.HTML), "lnkGuarantor");
		} else {
			inputFieldLayout.addComponent(new Label("N/A"), "lnkGuarantor");
		}
		inputFieldLayout.addComponent(new Label(I18N.message("outstanding")), "lblOutstanding");
		inputFieldLayout.addComponent(new Label(I18N.message("status")), "lblStatus");
		inputFieldLayout.addComponent(new Label(contract.getWkfStatus().getDesc()), "txtStatus");
		
		Amount outstanding = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId());
		inputFieldLayout.addComponent(new Label(AmountUtils.format(outstanding.getTiAmount())), "txtOutstanding");
		
        VerticalLayout vertical = new VerticalLayout();
        vertical.addComponent(inputFieldLayout);
        
        contractInfoPanel.setContent(vertical);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		return null;
	}
	
	/**
	 * @param id
	 */
	public void assignValues(Long id) {
		accordionPanel.setSelectedTab(collectionHistoryPanel);
		contract = ENTITY_SRV.getById(Contract.class, id);
		Quotation quotation = contract.getQuotation();
		applicantPanel.assignValues(quotation.getApplicant());
		applicantPanel.setApplicantEnabled(false, true);
		guarantorPanel.setMainApplicant(quotation.getApplicant());
		QuotationApplicant guarantor = quotation.getQuotationApplicant(EApplicantType.G);
		if (guarantor != null) {
			guarantorPanel.assignValues(guarantor);
		} else {
			accordionPanel.removeComponent(guarantorPanel);
		}
		paymentSchedulePanel.assignValues(quotation);
		unpaidInstallmentsPanel.assignValues(contract);
		assetPanel.assignValues(contract);
		documentsPanel.assignValues(quotation);
		commentsPanel.assignValues(quotation);
		collectionHistoryPanel.assignValues(contract);
		financialProductPanel.assignValues(quotation);
		contractDocumentsPanel.assignValues(contract);
		changeCollectionOfficerPanel.assignValues(contract);
		
		displayContractInfo(contract);
		paidOffPanel.assignValues(contract);
		
		addressesPanel.assignValues(contract.getApplicant().getIndividual().getIndividualAddresses());		
	}

}

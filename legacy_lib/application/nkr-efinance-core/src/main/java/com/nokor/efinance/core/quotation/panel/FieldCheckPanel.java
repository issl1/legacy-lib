package com.nokor.efinance.core.quotation.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.DocumentUwGroup;
import com.nokor.efinance.core.document.panel.DisplayDocumentPanel;
import com.nokor.efinance.core.document.panel.DocumentUploader;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.model.QuotationSupportDecision;
import com.nokor.efinance.core.quotation.model.SupportDecision;
import com.nokor.efinance.core.quotation.panel.include.DocumentUwGroupPanel;
import com.nokor.efinance.core.shared.comparator.SortIndexComparator;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 * Field check panel
 * @author ly.youhort
 */
public class FieldCheckPanel extends AbstractTabPanel implements QuotationEntityField, ValueChangeListener, FrmkServicesHelper {
	
	private static final long serialVersionUID = 1163676646989549493L;
	
	private QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
	
	private SecUserComboBox cbxCreditOfficer;
	
	private TextField txtApplicantFcRevenu;
	private TextField txtApplicantFcAllowance;
	private TextField txtApplicantFcBusinessExpenses;
	private TextField txtApplicantFcNetIncome;
	private TextField txtApplicantFcPersonalExpenses;
	private TextField txtApplicantFcFamilyExpenses;
	private TextField txtApplicantFcLiability;
	
	private TextField txtGuarantorFcRevenu;
	private TextField txtGuarantorFcAllowance;
	private TextField txtGuarantorFcBusinessExpenses;
	private TextField txtGuarantorFcNetIncome;
	private TextField txtGuarantorFcPersonalExpenses;
	private TextField txtGuarantorFcFamilyExpenses;
	private TextField txtGuarantorFcLiability;

	private VerticalLayout documentsLayout;
	private List<DocumentUwGroupPanel> documentUwGroupsPanel;
	
	private List<CheckBox> cbFieldCheckDocuments;
	private List<Button> btnPaths;

	private HorizontalLayout fieldCheckRequestLayout;
	private Panel fieldCheckRequestPanel;
	private List<CheckBox> cbSupportDecisions;
	private List<QuotationSupportDecision> quotationSupportDecisions;
	private Quotation quotation;
	
	
	@SuppressWarnings("unchecked")
	public FieldCheckPanel() {
		super();
		setSizeFull();

		documentUwGroupsPanel = new ArrayList<DocumentUwGroupPanel>();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout = new VerticalLayout();
		contentLayout.setSizeFull();
		contentLayout.setSpacing(true);	
		
		fieldCheckRequestLayout = new HorizontalLayout();
		fieldCheckRequestLayout.setMargin(true);
		fieldCheckRequestPanel = new Panel(I18N.message("field.check.request"));
		cbSupportDecisions = new ArrayList<CheckBox>();
		
		List<SupportDecision> supportDecisions = DataReference.getInstance().getSupportDecisions(QuotationWkfStatus.RFC);
		if (supportDecisions != null && !supportDecisions.isEmpty()) {

			for (SupportDecision supportDecision : supportDecisions) {
				CheckBox cbSupportDecision = new CheckBox(supportDecision.getDescEn());
				cbSupportDecision.setData(supportDecision);
				cbSupportDecisions.add(cbSupportDecision);
				fieldCheckRequestLayout.addComponent(cbSupportDecision);
				fieldCheckRequestLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
			}
			fieldCheckRequestPanel.setContent(fieldCheckRequestLayout);
		}
		
		FormLayout fieldCheckUserLayout = new FormLayout();
		fieldCheckUserLayout.setMargin(true);
		Panel fieldCheckUserPanel = new Panel(I18N.message("staff.in.charge"));
		cbxCreditOfficer = new SecUserComboBox(I18N.message("field.check.performed.by"), DataReference.getInstance().getUsers(FMProfile.CO));
		if (!ProfileUtil.isPOS() && !ProfileUtil.isAdmin()) {
			cbxCreditOfficer.setEnabled(false);
		}
		fieldCheckUserLayout.addComponent(cbxCreditOfficer);
		fieldCheckUserPanel.setContent(fieldCheckUserLayout);
		
		Panel fieldCheckEstimationPanel = new Panel(I18N.message("field.check.estimation"));
		txtApplicantFcRevenu = ComponentFactory.getTextField(false, 80, 70);
		txtApplicantFcRevenu.setImmediate(true);
		txtApplicantFcRevenu.addStyleName("amount");
		txtApplicantFcRevenu.addValueChangeListener(this);
		
		txtGuarantorFcRevenu = ComponentFactory.getTextField(false, 80, 70);
		txtGuarantorFcRevenu.setImmediate(true);
		txtGuarantorFcRevenu.addStyleName("amount");
		txtGuarantorFcRevenu.addValueChangeListener(this);
		
		
		txtApplicantFcAllowance = ComponentFactory.getTextField(false, 80, 70);
		txtApplicantFcAllowance.setImmediate(true);
		txtApplicantFcAllowance.addStyleName("amount");
		txtApplicantFcAllowance.addValueChangeListener(this);
		
		txtGuarantorFcAllowance = ComponentFactory.getTextField(false, 80, 70);
		txtGuarantorFcAllowance.setImmediate(true);
		txtGuarantorFcAllowance.addStyleName("amount");
		txtGuarantorFcAllowance.addValueChangeListener(this);
		
		txtApplicantFcBusinessExpenses = ComponentFactory.getTextField(false, 80, 70);
		txtApplicantFcBusinessExpenses.setImmediate(true);
		txtApplicantFcBusinessExpenses.addStyleName("amount");
		txtApplicantFcBusinessExpenses.addValueChangeListener(this);
		
		txtGuarantorFcBusinessExpenses = ComponentFactory.getTextField(false, 80, 70);
		txtGuarantorFcBusinessExpenses.setImmediate(true);
		txtGuarantorFcBusinessExpenses.addStyleName("amount");
		txtGuarantorFcBusinessExpenses.addValueChangeListener(this);
		
		txtApplicantFcNetIncome = ComponentFactory.getTextField(false, 80, 70);
		txtApplicantFcNetIncome.setReadOnly(true);
		txtApplicantFcNetIncome.addStyleName("amount");
		
		txtGuarantorFcNetIncome = ComponentFactory.getTextField(false, 80, 70);
		txtGuarantorFcNetIncome.setReadOnly(true);
		txtGuarantorFcNetIncome.addStyleName("amount");
		
		txtApplicantFcPersonalExpenses = ComponentFactory.getTextField(false, 80, 70);
		txtApplicantFcPersonalExpenses.addStyleName("amount");
		
		txtGuarantorFcPersonalExpenses = ComponentFactory.getTextField(false, 80, 70);
		txtGuarantorFcPersonalExpenses.addStyleName("amount");
		
		txtApplicantFcFamilyExpenses = ComponentFactory.getTextField(false, 80, 70);
		txtApplicantFcFamilyExpenses.addStyleName("amount");
		
		txtGuarantorFcFamilyExpenses = ComponentFactory.getTextField(false, 80, 70);
		txtGuarantorFcFamilyExpenses.addStyleName("amount");
		
		txtApplicantFcLiability = ComponentFactory.getTextField(false, 80, 70);
		txtApplicantFcLiability.addStyleName("amount");
		
		txtGuarantorFcLiability = ComponentFactory.getTextField(false, 80, 70);
		txtGuarantorFcLiability.addStyleName("amount");
		
		try {
		
			InputStream fieldCheckEstimationLayoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/co_fc_estimation.html");
			CustomLayout fieldCheckEstimationGridLayout = new CustomLayout(fieldCheckEstimationLayoutFile);
			fieldCheckEstimationGridLayout.setSizeFull();
			
			fieldCheckEstimationGridLayout.addComponent(new Label(I18N.message("base.salary.total.sales")), "lblRevenu");
			fieldCheckEstimationGridLayout.addComponent(new Label(I18N.message("allowance")), "lblAllowance");
			fieldCheckEstimationGridLayout.addComponent(new Label(I18N.message("business.expenses")), "lblBusinessExpenses");
			fieldCheckEstimationGridLayout.addComponent(new Label(I18N.message("net.income")), "lblNetIncome");
			fieldCheckEstimationGridLayout.addComponent(new Label(I18N.message("personal.expenses")), "lblPersonalExpenses");
			fieldCheckEstimationGridLayout.addComponent(new Label(I18N.message("family.expenses")), "lblFamilyExpenses");
			fieldCheckEstimationGridLayout.addComponent(new Label(I18N.message("liability")), "lblLiability");
			
			fieldCheckEstimationGridLayout.addComponent(new Label(I18N.message("field.check")), "lblFcEstimation");
			
			fieldCheckEstimationGridLayout.addComponent(new Label(I18N.message("applicant")), "lblApplicant");
			fieldCheckEstimationGridLayout.addComponent(txtApplicantFcRevenu, "txtApplicantFcRevenu");
			fieldCheckEstimationGridLayout.addComponent(txtApplicantFcAllowance, "txtApplicantFcAllowance");
			fieldCheckEstimationGridLayout.addComponent(txtApplicantFcBusinessExpenses, "txtApplicantFcBusinessExpenses");
			fieldCheckEstimationGridLayout.addComponent(txtApplicantFcNetIncome, "txtApplicantFcNetIncome");
			fieldCheckEstimationGridLayout.addComponent(txtApplicantFcPersonalExpenses, "txtApplicantFcPersonalExpenses");
			fieldCheckEstimationGridLayout.addComponent(txtApplicantFcFamilyExpenses, "txtApplicantFcFamilyExpenses");
			fieldCheckEstimationGridLayout.addComponent(txtApplicantFcLiability, "txtApplicantFcLiability");
			
			fieldCheckEstimationGridLayout.addComponent(new Label(I18N.message("guarantor")), "lblGuarantor");
			fieldCheckEstimationGridLayout.addComponent(txtGuarantorFcRevenu, "txtGuarantorFcRevenu");
			fieldCheckEstimationGridLayout.addComponent(txtGuarantorFcAllowance, "txtGuarantorFcAllowance");
			fieldCheckEstimationGridLayout.addComponent(txtGuarantorFcBusinessExpenses, "txtGuarantorFcBusinessExpenses");
			fieldCheckEstimationGridLayout.addComponent(txtGuarantorFcNetIncome, "txtGuarantorFcNetIncome");
			fieldCheckEstimationGridLayout.addComponent(txtGuarantorFcPersonalExpenses, "txtGuarantorFcPersonalExpenses");
			fieldCheckEstimationGridLayout.addComponent(txtGuarantorFcFamilyExpenses, "txtGuarantorFcFamilyExpenses");
			fieldCheckEstimationGridLayout.addComponent(txtGuarantorFcLiability, "txtGuarantorFcLiability");
			
			VerticalLayout fieldCheckEstimationLayout = new VerticalLayout();
			fieldCheckEstimationLayout.setMargin(true);
			fieldCheckEstimationLayout.addComponent(fieldCheckEstimationGridLayout);
			fieldCheckEstimationPanel.setContent(fieldCheckEstimationLayout);
			
			
		} catch (IOException e) {
			Notification.show(e.toString());
		}
		
		documentsLayout = new VerticalLayout();
		Panel documentsPanel = new Panel();
		documentsPanel.setCaption(I18N.message("third.party.confirmation"));
		documentsPanel.setContent(documentsLayout);		
			
		
		if (ProfileUtil.isUnderwriter() || ProfileUtil.isUnderwriterSupervisor() || ProfileUtil.isManager()) {
			contentLayout.addComponent(fieldCheckRequestPanel);
		}
		contentLayout.addComponent(fieldCheckUserPanel);
		contentLayout.addComponent(fieldCheckEstimationPanel);
		contentLayout.addComponent(documentsPanel);
		
		cbFieldCheckDocuments = new ArrayList<CheckBox>();
		btnPaths = new ArrayList<Button>();
		
		List<Document> documents = DataReference.getInstance().getDocuments();
		List<Document> fcDocuments = new ArrayList<Document>();
		if (documents != null && !documents.isEmpty()) {
			for (Document document : documents) {
				if (document.isFieldCheck()) {
					fcDocuments.add(document);
				}
			}
		}
		
		if (!fcDocuments.isEmpty()) {
			Panel fieldCheckDocumentsPanel = new Panel(I18N.message("field.check.documents"));
			GridLayout fieldCheckDocumentGridLayout = new GridLayout(3, fcDocuments.size() + 1);
        	fieldCheckDocumentGridLayout.setSpacing(true);
        	fieldCheckDocumentGridLayout.setMargin(true);
        	fieldCheckDocumentGridLayout.addComponent(new Label(), 0, 0);
        	fieldCheckDocumentGridLayout.addComponent(new Label("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", ContentMode.HTML), 1, 0);
        	fieldCheckDocumentGridLayout.addComponent(new Label(I18N.message("upload")), 2, 0);
			
        	int i = 1;
        	Collections.sort(fcDocuments, new DocumentComparatorByIdex());
        	for (Document document : fcDocuments) {
        		CheckBox cbDocument = new CheckBox();
        		cbDocument.setCaption(document.getApplicantType() + " - " + document.getDescEn());
        		cbDocument.setData(document);
        		cbFieldCheckDocuments.add(cbDocument);
        		
        		Button btnPath = new Button();
        		btnPath.setIcon(new ThemeResource("../nkr-default/icons/16/pdf.png"));
        		btnPath.setVisible(false);
        		btnPath.setStyleName(Runo.BUTTON_LINK);
        		btnPaths.add(btnPath);
        		btnPath.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 1674938731111758211L;
					@Override
					public void buttonClick(ClickEvent event) {
						new DisplayDocumentPanel((String) ((Button) event.getSource()).getData()).display();
					}
				});
        		
        		Upload uploadDocument = new Upload();
        		uploadDocument.setButtonCaption(I18N.message("upload"));
        		
        		final DocumentUploader uploader = new DocumentUploader(cbDocument, btnPath, uploadDocument); 
        		uploadDocument.setReceiver(uploader);
        		uploadDocument.addSucceededListener(uploader);
        		
        		fieldCheckDocumentGridLayout.addComponent(cbDocument, 0, i);
        		fieldCheckDocumentGridLayout.addComponent(btnPath, 1, i);
        		fieldCheckDocumentGridLayout.addComponent(uploadDocument, 2, i);
        		i++;
    		}
    		
    		fieldCheckDocumentsPanel.setContent(fieldCheckDocumentGridLayout);
    		contentLayout.addComponent(fieldCheckDocumentsPanel);
		}
		
		addComponent(contentLayout);
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		return new VerticalLayout();
	}
	
	/**
	 * 
	 */
	public void updateSupportDecision() {
		if (cbSupportDecisions != null && !cbSupportDecisions.isEmpty()) {
			for (CheckBox cbSupportDecision : cbSupportDecisions) {
				boolean isAlreadyExist = false;
				SupportDecision supportDecisionInCheckbox = (SupportDecision) cbSupportDecision.getData();

				if (quotationSupportDecisions != null && !quotationSupportDecisions.isEmpty()) {
					for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisions) {
						SupportDecision supportDecision = quotationSupportDecision.getSupportDecision();
						if (supportDecision.getId() == supportDecisionInCheckbox.getId()) {
							isAlreadyExist = true;
						}
						if (supportDecision.getId() == supportDecisionInCheckbox.getId() && !cbSupportDecision.getValue()) {
							ENTITY_SRV.delete(quotationSupportDecision);
							break;
						}
					}
				}
				
				if (cbSupportDecision.getValue() && !isAlreadyExist) {
					QuotationSupportDecision quotationSupportDecision = new QuotationSupportDecision();
					quotationSupportDecision.setQuotation(quotation);
					quotationSupportDecision.setSupportDecision(supportDecisionInCheckbox);
					quotationSupportDecision.setWkfStatus(quotation.getWkfStatus());
					quotationSupportDecision.setProcessed(true);
					ENTITY_SRV.saveOrUpdate(quotationSupportDecision);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param quotation
	 * @return
	 */
	private List<QuotationSupportDecision> getListQuotationSupportDecisions(Quotation quotation) {
		BaseRestrictions<QuotationSupportDecision> restrictions = new BaseRestrictions<QuotationSupportDecision>(
				QuotationSupportDecision.class);		
		restrictions.addCriterion(Restrictions.eq("quotation.id", quotation.getId()));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * Set quotation
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {
		this.quotation = quotation;
		this.quotationSupportDecisions = getListQuotationSupportDecisions(quotation);
		if (cbSupportDecisions != null && !cbSupportDecisions.isEmpty()) {
			for (CheckBox cbSupportDecision : cbSupportDecisions) {
				SupportDecision supportDecision = (SupportDecision) cbSupportDecision.getData();
				if (quotationSupportDecisions != null && !quotationSupportDecisions.isEmpty()) {
					for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisions) {
						if (supportDecision.getId() == quotationSupportDecision.getSupportDecision().getId()) {
							cbSupportDecision.setValue(true);
							break;
						}
					}
				}
			}
		}
		if (quotation.getFieldCheck() == null) {
			cbxCreditOfficer.setSelectedEntity(quotation.getCreditOfficer());
		} else {
			cbxCreditOfficer.setSelectedEntity(quotation.getFieldCheck());
		}
		
		txtApplicantFcRevenu.setValue(AmountUtils.format(quotation.getCoRevenuEstimation()));
		txtApplicantFcAllowance.setValue(AmountUtils.format(quotation.getCoAllowanceEstimation()));
		txtApplicantFcBusinessExpenses.setValue(AmountUtils.format(quotation.getCoBusinessExpensesEstimation()));
		txtApplicantFcNetIncome.setReadOnly(false);
		txtApplicantFcNetIncome.setValue(AmountUtils.format(quotation.getCoNetIncomeEstimation()));
		txtApplicantFcNetIncome.setReadOnly(true);
		txtApplicantFcPersonalExpenses.setValue(AmountUtils.format(quotation.getCoPersonalExpensesEstimation()));
		txtApplicantFcFamilyExpenses.setValue(AmountUtils.format(quotation.getCoFamilyExpensesEstimation()));
		txtApplicantFcLiability.setValue(AmountUtils.format(quotation.getCoLiabilityEstimation()));
		
		txtGuarantorFcRevenu.setValue(AmountUtils.format(quotation.getCoGuarantorRevenuEstimation()));
		txtGuarantorFcAllowance.setValue(AmountUtils.format(quotation.getCoGuarantorAllowanceEstimation()));
		txtGuarantorFcBusinessExpenses.setValue(AmountUtils.format(quotation.getCoGuarantorBusinessExpensesEstimation()));
		txtGuarantorFcNetIncome.setReadOnly(false);
		txtGuarantorFcNetIncome.setValue(AmountUtils.format(quotation.getCoGuarantorNetIncomeEstimation()));
		txtGuarantorFcNetIncome.setReadOnly(true);
		txtGuarantorFcPersonalExpenses.setValue(AmountUtils.format(quotation.getCoGuarantorPersonalExpensesEstimation()));
		txtGuarantorFcFamilyExpenses.setValue(AmountUtils.format(quotation.getCoGuarantorFamilyExpensesEstimation()));
		txtGuarantorFcLiability.setValue(AmountUtils.format(quotation.getCoGuarantorLiabilityEstimation()));		
						
		TabSheet documentsTabSheet = new TabSheet(); 
		documentsLayout.removeAllComponents();
		documentUwGroupsPanel.clear();
		documentsLayout.addComponent(documentsTabSheet);
		List<DocumentUwGroup> documentUwGroups = quotationService.list(DocumentUwGroup.class);
		Collections.sort(documentUwGroups, new SortIndexComparator());
		if (documentUwGroups != null && !documentUwGroups.isEmpty()) {
			for (DocumentUwGroup documentUwGroup : documentUwGroups) {
				if (!quotationService.isGuarantorRequired(quotation) && documentUwGroup.getApplicantType().equals(EApplicantType.G)) {
					// do nothing
				} else { 	
					DocumentUwGroupPanel documentUwGroupPanel = new DocumentUwGroupPanel(documentUwGroup, quotation, true); 
					documentsTabSheet.addTab(documentUwGroupPanel, documentUwGroup.getDescEn());
					documentUwGroupsPanel.add(documentUwGroupPanel);
				}
			}
		}
				
		List<QuotationDocument> documents = quotation.getQuotationDocuments();
		if (cbFieldCheckDocuments != null && !cbFieldCheckDocuments.isEmpty()) {
			for (int i = 0; i < cbFieldCheckDocuments.size(); i++) {
				CheckBox cbDocument = cbFieldCheckDocuments.get(i);
				Document document = (Document) cbDocument.getData();
				boolean found = false;
				if (documents != null) {
					for (QuotationDocument quotationDocument : documents) {
						if (document.isFieldCheck() && document.getId().equals(quotationDocument.getDocument().getId())) {
							cbDocument.setValue(true);
							if (StringUtils.isNotEmpty(quotationDocument.getPath())) {
								btnPaths.get(i).setVisible(true);
								btnPaths.get(i).setData(quotationDocument.getPath());
							} else {
								btnPaths.get(i).setVisible(false);
								btnPaths.get(i).setData(null);
							}
							found = true;
							break;
						}
					}
				}
				if (!found) {
					cbDocument.setValue(false);
					btnPaths.get(i).setVisible(false);
					btnPaths.get(i).setData(null);
				}
			}
		}
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public Quotation getQuotation(Quotation quotation) {
		quotation.setFieldCheck(cbxCreditOfficer.getSelectedEntity());
		quotation.setCoRevenuEstimation(getDouble(txtApplicantFcRevenu));
		quotation.setCoAllowanceEstimation(getDouble(txtApplicantFcAllowance));
		quotation.setCoBusinessExpensesEstimation(getDouble(txtApplicantFcBusinessExpenses));
		quotation.setCoNetIncomeEstimation(getDouble(txtApplicantFcNetIncome));
		quotation.setCoPersonalExpensesEstimation(getDouble(txtApplicantFcPersonalExpenses));
		quotation.setCoFamilyExpensesEstimation(getDouble(txtApplicantFcFamilyExpenses));
		quotation.setCoLiabilityEstimation(getDouble(txtApplicantFcLiability));
		
		quotation.setCoGuarantorRevenuEstimation(getDouble(txtGuarantorFcRevenu));
		quotation.setCoGuarantorAllowanceEstimation(getDouble(txtGuarantorFcAllowance));
		quotation.setCoGuarantorBusinessExpensesEstimation(getDouble(txtGuarantorFcBusinessExpenses));
		quotation.setCoGuarantorNetIncomeEstimation(getDouble(txtGuarantorFcNetIncome));
		quotation.setCoGuarantorPersonalExpensesEstimation(getDouble(txtGuarantorFcPersonalExpenses));
		quotation.setCoGuarantorFamilyExpensesEstimation(getDouble(txtGuarantorFcFamilyExpenses));
		quotation.setCoGuarantorLiabilityEstimation(getDouble(txtGuarantorFcLiability));
		
		List<QuotationDocument> quotationDocuments = quotation.getQuotationDocuments();
		if (quotationDocuments == null) {
			quotationDocuments = new ArrayList<QuotationDocument>();
		}
		
		if (cbFieldCheckDocuments != null && !cbFieldCheckDocuments.isEmpty()) {
			for (int i = 0; i < cbFieldCheckDocuments.size(); i++) {
				CheckBox cbDocument = cbFieldCheckDocuments.get(i);
				Document document = (Document) cbDocument.getData();
				QuotationDocument quotationDocument = quotation.getQuotationDocument(document.getId());
				if (cbDocument.getValue()) {
					if (quotationDocument == null) {
						quotationDocument = new QuotationDocument();
						quotationDocument.setCrudAction(CrudAction.CREATE);
						quotationDocuments.add(quotationDocument);
					} else {
						quotationDocument.setCrudAction(CrudAction.UPDATE);
					}
					quotationDocument.setDocument(document);
					quotationDocument.setQuotation(quotation);
					if (btnPaths.get(i).getData() != null) {
						quotationDocument.setPath((String) btnPaths.get(i).getData());
					}
				} else if (quotationDocument != null) {
					quotationDocument.setCrudAction(CrudAction.DELETE);
				}
			}
		}
		
		quotation.setQuotationDocuments(quotationDocuments);
		return quotation;
	}
	
	/**
	 * @return
	 */
	public boolean checkValidityFiels() {
		super.removeErrorsPanel();
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		
		txtApplicantFcRevenu.setValue("");
		txtApplicantFcAllowance.setValue("");
		txtApplicantFcBusinessExpenses.setValue("");
		txtApplicantFcPersonalExpenses.setValue("");
		txtApplicantFcFamilyExpenses.setValue("");
		txtApplicantFcLiability.setValue("");
		
		txtGuarantorFcRevenu.setValue("");
		txtGuarantorFcAllowance.setValue("");
		txtGuarantorFcBusinessExpenses.setValue("");
		txtGuarantorFcPersonalExpenses.setValue("");
		txtGuarantorFcFamilyExpenses.setValue("");
		txtGuarantorFcLiability.setValue("");
		
		if (cbFieldCheckDocuments != null && !cbFieldCheckDocuments.isEmpty()) {
			for (int i = 0; i < cbFieldCheckDocuments.size(); i++) {
				cbFieldCheckDocuments.get(i).setValue(false);
				btnPaths.get(i).setVisible(false);
				btnPaths.get(i).setData(null);
			}
		}
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		txtApplicantFcNetIncome.setReadOnly(false);
		txtApplicantFcNetIncome.setValue(AmountUtils.format(getDouble(txtApplicantFcRevenu, 0.d) + getDouble(txtApplicantFcAllowance, 0.d) - getDouble(txtApplicantFcBusinessExpenses, 0.d)));
		txtApplicantFcNetIncome.setReadOnly(true);
		
		txtGuarantorFcNetIncome.setReadOnly(false);
		txtGuarantorFcNetIncome.setValue(AmountUtils.format(getDouble(txtGuarantorFcRevenu, 0.d) + getDouble(txtGuarantorFcAllowance, 0.d) - getDouble(txtGuarantorFcBusinessExpenses, 0.d)));
		txtGuarantorFcNetIncome.setReadOnly(true);
	}
	
	/**
	 * sort index
	 * @author uhout.cheng
	 *
	 */
	@SuppressWarnings("rawtypes")
	private static class DocumentComparatorByIdex implements Comparator {
		public int compare(Object o1, Object o2) {
			Document c1 = (Document) o1;
			Document c2 = (Document) o2;
			if (c1 == null || c1.getSortIndex() == null) {
				if (c2 == null || c2.getSortIndex() == null)
					return 0;
				return -1;
			}
			if (c2 == null || c2.getSortIndex() == null)
				return 1;
			return c1.getSortIndex().compareTo(c2.getSortIndex());
		}
	}
}

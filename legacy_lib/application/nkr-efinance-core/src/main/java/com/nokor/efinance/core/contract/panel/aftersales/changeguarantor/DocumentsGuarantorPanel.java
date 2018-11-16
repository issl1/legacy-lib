package com.nokor.efinance.core.contract.panel.aftersales.changeguarantor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.DocumentGroup;
import com.nokor.efinance.core.document.panel.DisplayDocumentPanel;
import com.nokor.efinance.core.document.panel.DocumentUploader;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 * Documents panel
 * @author ly.youhort
 */
public class DocumentsGuarantorPanel extends AbstractTabPanel {
	
	private static final long serialVersionUID = -8778667935206669712L;
	
	private List<CheckBox> cbDocuments;
	private List<TextField> txtReferenceDocuments;
	private List<AutoDateField> dfIssueDateDocuments;
	private List<AutoDateField> dfExpireDateDocuments;
	private List<Button> btnPaths;
	private List<CheckBox> cbOriginals;
	
	private Map<Long, List<Document>> documentsGroupMap;
		
	public DocumentsGuarantorPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected Component createForm() {		
		cbDocuments = new ArrayList<CheckBox>();
		txtReferenceDocuments = new ArrayList<TextField>();
		dfIssueDateDocuments = new ArrayList<AutoDateField>();
		dfExpireDateDocuments = new ArrayList<AutoDateField>();
		btnPaths = new ArrayList<Button>();
		cbOriginals = new ArrayList<CheckBox>();
		documentsGroupMap = new LinkedHashMap<Long, List<Document>>();
		List<Document> documents = DataReference.getInstance().getDocuments();
		if (documents != null && !documents.isEmpty()) {
			for (Document document : documents) {
				if (document.getApplicantType().equals(EApplicantType.G)) {
					List<Document> documentsByGroup = documentsGroupMap.get(document.getDocumentGroup().getId());
					if (documentsByGroup == null) {
						documentsByGroup = new ArrayList<Document>();
						documentsGroupMap.put(document.getDocumentGroup().getId(), documentsByGroup);
					}
					documentsByGroup.add(document);
				}
			}
		}
		
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        
        for (DocumentGroup  documentGroup :  DataReference.getInstance().getDocumentGroups()) {
        	
	        List<Document> documentsByGroup = documentsGroupMap.get(documentGroup.getId());
	        
	        if (documentsByGroup != null && !documentsByGroup.isEmpty()) {
	        
		        Panel documentGroupPanel = new Panel(documentGroup.getDescEn());
		        	        
	        	GridLayout gridLayout = new GridLayout(7, documentsByGroup.size() + 1);
	        	gridLayout.setSpacing(true);
	        	gridLayout.setMargin(true);
	        	gridLayout.addComponent(new Label(), 0, 0);
	        	gridLayout.addComponent(new Label(I18N.message("reference")), 1, 0);
	        	gridLayout.addComponent(new Label(I18N.message("issue.date")), 2, 0);
	        	gridLayout.addComponent(new Label(I18N.message("expire.date")), 3, 0);
	        	Label lblOriginal = new Label(I18N.message("original"));
	        	lblOriginal.setWidth(60, Unit.PIXELS);
	        	gridLayout.addComponent(lblOriginal, 4, 0);
	        	Label lblPath = new Label();
	        	lblPath.setWidth(60, Unit.PIXELS);
	        	gridLayout.addComponent(lblPath, 5, 0);
	        	gridLayout.addComponent(new Label(I18N.message("upload")), 6, 0);
		        	
	        	int i = 1;
	        	for (Document document : documentsByGroup) {
	        		CheckBox cbDocument = new CheckBox();
	        		cbDocument.setCaption(document.getApplicantType() + " - " + document.getDescEn());
	        		cbDocument.setData(document);
	        		cbDocuments.add(cbDocument);
	        		
	        		TextField txtReferenceDocument = ComponentFactory.getTextField(false, 100, 200);
	        		txtReferenceDocuments.add(txtReferenceDocument);
	        		txtReferenceDocument.setEnabled(document.isReferenceRequired());
	        		
	        		AutoDateField dfIssueDateDocument = ComponentFactory.getAutoDateField();
	        		dfIssueDateDocuments.add(dfIssueDateDocument);
	        		dfIssueDateDocument.setEnabled(document.isIssueDateRequired());
	        		
	        		AutoDateField dfExpireDateDocument = ComponentFactory.getAutoDateField();
	        		dfExpireDateDocuments.add(dfExpireDateDocument);
	        		dfExpireDateDocument.setEnabled(document.isExpireDateRequired());
	        		
	        		CheckBox cbOriginal = new CheckBox();
	        		cbOriginal.setCaption("                      ");
	        		cbOriginals.add(cbOriginal);
	        		
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
	        		
	        		gridLayout.addComponent(cbDocument, 0, i);
	        		gridLayout.addComponent(txtReferenceDocument, 1, i);
	        		gridLayout.addComponent(dfIssueDateDocument, 2, i);
	        		gridLayout.addComponent(dfExpireDateDocument, 3, i);
	        		gridLayout.addComponent(cbOriginal, 4, i);
	        		gridLayout.addComponent(btnPath, 5, i);
	        		gridLayout.addComponent(uploadDocument, 6, i);
	        		i++;
	        	}
	        	documentGroupPanel.setContent(gridLayout);
	        	verticalLayout.addComponent(documentGroupPanel);
	        }
        }
        return verticalLayout;
    }
	
	/**
	 * Assign value
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {
		List<QuotationDocument> documents = quotation.getQuotationDocuments(EApplicantType.G);
		if (cbDocuments != null && !cbDocuments.isEmpty()) {
			for (int i = 0; i < cbDocuments.size(); i++) {
				CheckBox cbDocument = cbDocuments.get(i);
				Document document = (Document) cbDocument.getData();
				boolean found = false;
				if (documents != null) {
					for (QuotationDocument quotationDocument : documents) {
						if (document.getId().equals(quotationDocument.getDocument().getId())) {
							cbDocument.setValue(true);
							txtReferenceDocuments.get(i).setValue(getDefaultString(quotationDocument.getReference()));
							dfIssueDateDocuments.get(i).setValue(quotationDocument.getIssueDate());
							dfExpireDateDocuments.get(i).setValue(quotationDocument.getExpireDate());
							cbOriginals.get(i).setValue(quotationDocument.isOriginal());
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
					txtReferenceDocuments.get(i).setValue("");
					dfIssueDateDocuments.get(i).setValue(null);
					dfExpireDateDocuments.get(i).setValue(null);
					cbOriginals.get(i).setValue(false);
					btnPaths.get(i).setVisible(false);
				}
			}
		}
			
		setEnabled(!(quotation.getWkfStatus().equals(QuotationWkfStatus.QUO) && quotation.getPreviousWkfStatus().equals(QuotationWkfStatus.APV)));
	}

	/**
	 * @param quotation
	 * @return
	 */
	public List<QuotationDocument> getDocuments(Quotation quotation) {
		
		List<QuotationDocument> quotationDocuments = quotation.getQuotationDocuments(EApplicantType.G);
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
					quotationDocument.setReference(txtReferenceDocuments.get(i).getValue());
					quotationDocument.setIssueDate(dfIssueDateDocuments.get(i).getValue());
					quotationDocument.setExpireDate(dfExpireDateDocuments.get(i).getValue());
					quotationDocument.setOriginal(BooleanUtils.toBoolean(cbOriginals.get(i).getValue()));
					if (btnPaths.get(i).getData() != null) {
						quotationDocument.setPath((String) btnPaths.get(i).getData());
					}
				}
			}
		}
		
		quotation.setQuotationDocuments(quotationDocuments);
		return quotationDocuments;
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		return true;
	}
	
	public List<String> fullValidate() {
		super.removeErrorsPanel();
		for (int i = 0; i < cbDocuments.size(); i++) {
			CheckBox cbDocument = cbDocuments.get(i);
			Document document = (Document) cbDocument.getData();
			
			if (document.isMandatory() && !cbDocument.getValue()) {
				// Check in the same group if we have one document is selected  
				Integer numGroup = document.getNumGroup();
				boolean madatory = true;
				if (numGroup != null) {
					for (int j = 0; j < cbDocuments.size(); j++) {
						Document document2 = (Document) cbDocuments.get(j).getData();
						if (numGroup.equals(document2.getNumGroup()) && cbDocuments.get(j).getValue()) {
							madatory = false;
							break;
						}
					}
				}
				if (madatory) {
					errors.add(I18N.message("document.required.1", document.getApplicantType() + "-" + document.getDescEn()));
				}
			}
			
			if (cbDocument.getValue() && document.isReferenceRequired() && (StringUtils.isEmpty(txtReferenceDocuments.get(i).getValue()))) {
				errors.add(I18N.message("document.reference.required.1", document.getApplicantType() + "-" + document.getDescEn()));
			}

			if (cbDocument.getValue() && document.isIssueDateRequired() && (dfIssueDateDocuments.get(i).getValue() == null)) {
				errors.add(I18N.message("document.issue.date.required.1", document.getApplicantType() + "-" + document.getDescEn()));
			}
			
			if (cbDocument.getValue() && document.isExpireDateRequired() && (dfExpireDateDocuments.get(i).getValue() == null)) {
				errors.add(I18N.message("document.expire.date.required.1", document.getApplicantType() + "-" + document.getDescEn()));
			}
		}
		
		return errors;
	}

	/**
	 * Reset panel
	 */
	public void reset() {
		if (cbDocuments != null && !cbDocuments.isEmpty()) {
			for (int i = 0; i < cbDocuments.size(); i++) {
				cbDocuments.get(i).setValue(false);
				txtReferenceDocuments.get(i).setValue("");
				dfIssueDateDocuments.get(i).setValue(null);
				dfExpireDateDocuments.get(i).setValue(null);
				btnPaths.get(i).setData(null);
				btnPaths.get(i).setVisible(false);
				cbOriginals.get(i).setValue(false);
			}
		}
	}
	
}

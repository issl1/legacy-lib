package com.nokor.efinance.core.document.panel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;

import com.itextpdf.text.DocumentException;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.DocumentGroup;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.quotation.QuotationProfileUtils;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
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
public class DocumentsPanel extends AbstractTabPanel implements SaveClickListener, FrmkServicesHelper {
	
	private static final long serialVersionUID = -8778667935206669712L;

	private QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
	
//	private ContractFormPanel quotationFormPanel;
	private Quotation quotation;
	
	private List<CheckBox> cbDocuments;
	private List<TextField> txtReferenceDocuments;
	private List<AutoDateField> dfIssueDateDocuments;
	private List<AutoDateField> dfExpireDateDocuments;
	private List<Button> btnPaths;
	private List<CheckBox> cbOriginals;
	private List<Upload> uploadDocuments;
	private Map<Long, List<Document>> documentsGroupMap;
		
	public DocumentsPanel(/*ContractFormPanel quotationFormPanel*/) {
		super();
		setSizeFull();
//		this.quotationFormPanel = quotationFormPanel;
	}
	
	@Override
	protected Component createForm() {		
		cbDocuments = new ArrayList<CheckBox>();
		uploadDocuments = new ArrayList<Upload>();
		txtReferenceDocuments = new ArrayList<TextField>();
		dfIssueDateDocuments = new ArrayList<AutoDateField>();
		dfExpireDateDocuments = new ArrayList<AutoDateField>();
		btnPaths = new ArrayList<Button>();
		cbOriginals = new ArrayList<CheckBox>();
		documentsGroupMap = new LinkedHashMap<Long, List<Document>>();
		List<Document> documents = DataReference.getInstance().getDocuments();
		if (documents != null && !documents.isEmpty()) {
			for (Document document : documents) {
				List<Document> documentsByGroup = documentsGroupMap.get(document.getDocumentGroup().getId());
				if (documentsByGroup == null) {
					documentsByGroup = new ArrayList<Document>();
					documentsGroupMap.put(document.getDocumentGroup().getId(), documentsByGroup);
				}
				documentsByGroup.add(document);
			}
		}
		
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        
        for (final DocumentGroup  documentGroup :  DataReference.getInstance().getDocumentGroups()) {
        	
	        List<Document> documentsByGroup = documentsGroupMap.get(documentGroup.getId());
	        
	        if (documentsByGroup != null && !documentsByGroup.isEmpty()) {
	        
		        //Panel documentGroupPanel = new Panel(documentGroup.getDescEn());
	        	Panel documentGroupPanel = new Panel();
		        	        
	        	VerticalLayout panelGroupLayout = new VerticalLayout();
		        HorizontalLayout hLayout = new HorizontalLayout();
		        hLayout.setSpacing(true);
		        hLayout.setMargin(true);
		        hLayout.setSizeFull();
		        		        
		        hLayout.addComponent(new Label(documentGroup.getDescEn()));
		        Button mergedPDFBtn = new Button("Download All Documents");
		        hLayout.addComponent(mergedPDFBtn);
		       // hLayout.setEnabled(false);
		        panelGroupLayout.addComponent(hLayout);
		        
		        mergedPDFBtn.setData(documentGroup);
		        mergedPDFBtn.setEnabled(false);
		        mergedPDFBtn.setVisible(false);
		        
		        mergedPDFBtn.addClickListener(new ClickListener(){
		        	private static final long serialVersionUID = 1674938731111758211L;
					@Override
					public void buttonClick(ClickEvent event) {
						
						List<File> customerFiles = new ArrayList<File>();
						List<File> guarantorFiles = new ArrayList<File>();
					 try {
							List<QuotationDocument> documents = quotation.getQuotationDocuments();
							int size = documents.size();
							String documentDir = AppConfig.getInstance().getConfiguration().getString("document.path");
							
							for(int i=0; i< size; i++){
								
								QuotationDocument qdoc = ENTITY_SRV.getById(QuotationDocument.class, documents.get(i).getId());
								Document doc = qdoc.getDocument();
								System.out.println("doc dir: "+documentDir+", doc file name: "+ qdoc.getPath());
								
								if(qdoc != null){
									String pathFileName = qdoc.getPath();
									if (pathFileName != null ) {
										//System.out.println("==> quotation id: "+ quotation.getId());	
										String fileName =  pathFileName.substring(pathFileName.indexOf("/") + 1);
																
										File f = new File(documentDir + "/" + quotation.getId() + "/" + fileName);
										
										if (f.exists()) {
											if(doc.getApplicantType().equals(EApplicantType.C))
												customerFiles.add(f);
											if(doc.getApplicantType().equals(EApplicantType.G))
												guarantorFiles.add(f);
										}									
									}
								}
							}
						
						
							File documentFileDir = new File(documentDir + "/" + quotation.getId());
							if (!documentFileDir.exists()) {
								documentFileDir.mkdirs();
							}
							File f = new File(documentDir + "/" + quotation.getId() + "/" + "C_"+quotation.getId()+"_all.pdf");
							if (f.exists()) {
								f.delete();
							}
							File gf = new File(documentDir + "/" + quotation.getId() + "/" + "G_"+quotation.getId()+"_all.pdf");
							if (gf.exists()) {
								gf.delete();
							}
							
							DocumentGroup docGroup = (DocumentGroup)((Button)event.getSource()).getData();
							
							if("CE".equalsIgnoreCase(docGroup.getCode()))
								MergePDF.doMerge(customerFiles, documentDir + "/" + quotation.getId() + "/" + "C_"+quotation.getId()+"_all.pdf");
							
							if("GE".equalsIgnoreCase(docGroup.getCode()))
								MergePDF.doMerge(guarantorFiles, documentDir + "/" + quotation.getId() + "/" + "G_"+quotation.getId()+"_all.pdf");
							
							String cAllFileName = documentDir + "/" + quotation.getId() + "/" + "C_"+quotation.getId()+"_all.pdf";
							String gAllFileName = documentDir + "/" + quotation.getId() + "/" + "G_"+quotation.getId()+"_all.pdf";
							File cAllFile = new File(cAllFileName);
							File gAllFile = new File(gAllFileName);
							
							
							
							if(cAllFile.exists() && "CE".equalsIgnoreCase(docGroup.getCode()) )
								new DisplayDocumentPanel(quotation.getId() + "/" + "C_"+quotation.getId()+"_all.pdf").display();
														
							if(gAllFile.exists() && "GE".equalsIgnoreCase(docGroup.getCode()))
								new DisplayDocumentPanel(quotation.getId() + "/" + "G_"+quotation.getId()+"_all.pdf").display();
							
							
						} catch (DocumentException e) {							
							System.out.println(e.getMessage());
						} catch (IOException e) {							
							System.out.println(e.getMessage());
						}catch(Exception e){
							System.out.println(e.getMessage());
						}
					}
		        	
		        });
		        
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
							DisplayDocumentPanel displayDocumentPanel = 
									new DisplayDocumentPanel((String) ((Button) event.getSource()).getData());
							displayDocumentPanel.display();
							if (ProfileUtil.isPOS() || ProfileUtil.isUW() || ProfileUtil.isManager()) {
								displayDocumentPanel.setModal(false);
							}
						}
					});
	        		
	        		Upload uploadDocument = new Upload();
	        		uploadDocument.setButtonCaption(I18N.message("upload"));
	        		uploadDocuments.add(uploadDocument);
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
	        	//documentGroupPanel.setContent(gridLayout);
	        	panelGroupLayout.addComponent(gridLayout);
	        	documentGroupPanel.setContent(panelGroupLayout);
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
		this.quotation = quotation;		
		List<QuotationDocument> documents = quotation.getQuotationDocuments();
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
		setEnableDocument();
		//setEnabled(!(quotation.getWkfStatus() == QuotationStatus.QUO && quotation.getPreviousWkfStatus() == QuotationStatus.APV));
	}
	/**
	 * 
	 * @param i
	 * @param isEnableDocument
	 */
	public void setEnableDocument() {
		for  (int i = 0; i < cbDocuments.size(); i++) {
			Document document = (Document) cbDocuments.get(i).getData();
			boolean allowUpdateChangeAsset = true;
			if (quotation.getWkfStatus().equals(QuotationWkfStatus.QUO)
					&& quotation.getPreviousWkfStatus().equals(QuotationWkfStatus.APV)) {
				allowUpdateChangeAsset = document.isAllowUpdateChangeAsset();
			}
			cbDocuments.get(i).setEnabled(allowUpdateChangeAsset || ProfileUtil.isAdmin());
			txtReferenceDocuments.get(i).setEnabled((allowUpdateChangeAsset && document.isReferenceRequired()) || 
													 ProfileUtil.isAdmin());
			dfIssueDateDocuments.get(i).setEnabled((allowUpdateChangeAsset && document.isIssueDateRequired()) ||
													ProfileUtil.isAdmin());
			dfExpireDateDocuments.get(i).setEnabled((allowUpdateChangeAsset && document.isExpireDateRequired()) || 
													 ProfileUtil.isAdmin());
			cbOriginals.get(i).setEnabled(allowUpdateChangeAsset || ProfileUtil.isAdmin());
			btnPaths.get(i).setEnabled(allowUpdateChangeAsset || ProfileUtil.isAdmin());
			uploadDocuments.get(i).setEnabled(allowUpdateChangeAsset || ProfileUtil.isAdmin());
		}
		
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
		boolean guarantorRequired = quotationService.isGuarantorRequired(quotation);
		for (int i = 0; i < cbDocuments.size(); i++) {
			CheckBox cbDocument = cbDocuments.get(i);
			Document document = (Document) cbDocument.getData();
			if (!guarantorRequired && document.getApplicantType().equals(EApplicantType.G)) {
				// do not need to control documents of guarantor
			} else {
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
	
	@Override
	public void updateNavigationPanel(VerticalLayout navigationLayout, NavigationPanel defaultNavigationPanel) {
		if (QuotationProfileUtils.isSaveDocumentAvailable(quotation)) {
			navigationLayout.removeAllComponents();
			NavigationPanel navigationPanel = new NavigationPanel();
			navigationPanel.addSaveClickListener(this);
			navigationLayout.addComponent(navigationPanel);
		} else {
			super.updateNavigationPanel(navigationLayout, defaultNavigationPanel);
		}
	}
	
	@Override
	public void saveButtonClick(ClickEvent event) {
		quotation.setQuotationDocuments(getDocuments(quotation));
		quotationService.saveOrUpdateQuotationDocuments(quotation);
//		quotationFormPanel.displaySuccess();
		assignValues(quotation);
	};
	
	
	
}

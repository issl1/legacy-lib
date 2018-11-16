package com.nokor.efinance.core.contract.panel;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.DaoException;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractDocument;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.quotation.panel.document.popup.ContractDocumentUploader;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Contract Document Panel
 * @author bunlong.taing
 */
public class ContractDocumentPanel extends AbstractTabPanel implements FMEntityField, ClickListener {

	/** */
	private static final long serialVersionUID = -4834916900930850742L;

	private static final String DOCUMENT_TYPE = "document.type";
	private static final String ADD_BY = "add.by";
	private static final String COMMENT = "comment";
	
	private Button btnUploadDocument;
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<ContractDocument> pagedTable;

	private Contract contract;
	
	private NativeButton btnUpload;
	private NativeButton btnCancel;
	private Upload uploadDocument;
	private TextField txtDocumentReference;
	private EntityRefComboBox<Document> cbxDocumentType;
	private TextArea txtComment;
	private ContractDocumentUploader contractDocumentUploader;
	private Window contractDocumentUploadwindow;
	private Label lblUploadDesc;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		btnUploadDocument = ComponentFactory.getButton("upload.document");
		btnUploadDocument.addClickListener(this);
		
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<ContractDocument>(this.columnDefinitions);
		
		lblUploadDesc = new Label();
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(pagedTable);
		verticalLayout.addComponent(pagedTable.createControls());
		verticalLayout.addComponent(btnUploadDocument);
		verticalLayout.setComponentAlignment(btnUploadDocument, Alignment.TOP_RIGHT);
		
		return verticalLayout;
	}
	
	/**
	 * 
	 * @param contractDocuments
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer setIndexedContainer(List<ContractDocument> contractDocuments) {
		IndexedContainer indexedContainer = new IndexedContainer();
		try {	
			for (ColumnDefinition column : this.columnDefinitions) {
				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			}
			for (ContractDocument contractDocument : contractDocuments) {
				Item item = indexedContainer.addItem(contractDocument.getId());
				item.getItemProperty(ID).setValue(contractDocument.getId());
				item.getItemProperty(DOCUMENT_TYPE).setValue(contractDocument.getDocument() != null ? contractDocument.getDocument().getDescEn() : "");
				item.getItemProperty(REFERENCE).setValue(contractDocument.getReference());
				item.getItemProperty(ADD_BY).setValue(contractDocument.getCreateUser());
				item.getItemProperty(COMMENT).setValue(contractDocument.getComment());
			}		
		} catch (DaoException e) {
			logger.error("DaoException", e);
		}
		return indexedContainer;
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DOCUMENT_TYPE, I18N.message("document.type"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(ADD_BY, I18N.message("add.by"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(COMMENT, I18N.message("comment"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	
	/**
	 * assing values
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		if (contract != null) {
			pagedTable.setContainerDataSource(setIndexedContainer(getContractDocuments(contract)));
		}
	}
	
	/**
	 * get list of contract documents
	 * @param contract
	 * @return
	 */
	private List<ContractDocument> getContractDocuments(Contract contract) {
		BaseRestrictions<ContractDocument> restrictions = new BaseRestrictions<>(ContractDocument.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * contract document upload popup window
	 */
	private void contractDocumentPopupPanel() {
		contractDocumentUploadwindow = new Window(I18N.message("upload"));
		contractDocumentUploadwindow.setModal(true);
		contractDocumentUploadwindow.setResizable(false);
		contractDocumentUploadwindow.setWidth(480, Unit.PIXELS);
		contractDocumentUploadwindow.setHeight(300, Unit.PIXELS);
		
		btnUpload = new NativeButton(I18N.message("upload"));
		btnUpload.setIcon(FontAwesome.UPLOAD);
		btnUpload.addClickListener(this);
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.addClickListener(this);
		
		contractDocumentUploader = new ContractDocumentUploader(this);
		uploadDocument = new Upload();
		//uploadDocument.setCaption(I18N.message("upload"));
		uploadDocument.setButtonCaption(I18N.message("browse"));
		uploadDocument.setImmediate(true);
		uploadDocument.setReceiver(contractDocumentUploader);
		
		txtDocumentReference = ComponentFactory.getTextField("reference", false, 100, 200);
		
		cbxDocumentType = new EntityRefComboBox<Document>(I18N.message("document.type"));
		cbxDocumentType.setImmediate(true);
		cbxDocumentType.setRestrictions(new BaseRestrictions<Document>(Document.class));
		cbxDocumentType.renderer();
		
		txtComment = new TextArea(I18N.message("comment"));
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnUpload);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtDocumentReference);
		formLayout.addComponent(cbxDocumentType);
		formLayout.addComponent(txtComment);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(getUploadLayout());
		verticalLayout.addComponent(formLayout);
		
		contractDocumentUploadwindow.setContent(verticalLayout);
		UI.getCurrent().addWindow(contractDocumentUploadwindow);
	}
	
	/**
	 * Save 
	 */
	private void doSave() {
		ContractDocument contractDocument = new ContractDocument();
		contractDocument.setReference(txtDocumentReference.getValue());
		contractDocument.setDocument(cbxDocumentType.getSelectedEntity());
		contractDocument.setComment(txtComment.getValue());
		contractDocument.setContract(contract);
		
		if (contractDocumentUploader.getFile() != null) {
			contractDocument.setPath(contractDocumentUploader.getFile().getPath());
		}
		ENTITY_SRV.saveOrUpdate(contractDocument);
		pagedTable.setContainerDataSource(setIndexedContainer(getContractDocuments(contract)));
		contractDocumentUploadwindow.close();
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getUploadLayout() {
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = "<table cellspacing=\"2\" cellpadding=\"2\" border=\"0\" style=\"margin-left:15px;\">";
		template += "<tr>";
		template += "<td align=\"right\" width=\"118\"><div location=\"lblUpload\" class=\"inline-block\"></div></td>";
		template += "<td><div location =\"uploadDocument\"/></td>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblUploadDesc\"></td>";
		template += "</tr>";
		template += "</table>";
		
		cusLayout.addComponent(new Label(I18N.message("upload")), "lblUpload");
		cusLayout.addComponent(uploadDocument, "uploadDocument");
		cusLayout.addComponent(lblUploadDesc, "lblUploadDesc");
		cusLayout.setTemplateContents(template);
		return cusLayout;
	}
	
	/**
	 * @return the lblUploadDesc
	 */
	public Label getLblUploadDesc() {
		return lblUploadDesc;
	}


	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnUploadDocument) {
			lblUploadDesc.setValue(null);
			this.contractDocumentPopupPanel();
		} else if (event.getButton() == btnUpload) {
			this.doSave();
		} else if (event.getButton() == btnCancel) {
			contractDocumentUploadwindow.close();
		}
	}
	
	
}

package com.nokor.efinance.gui.ui.panel.report.contract.overdue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractDocument;
import com.nokor.efinance.core.document.panel.DisplayDocumentPanel;
import com.nokor.efinance.core.document.panel.DocumentFileUploader;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 * Contract Document panel
 * @author bunlong.taing
 */
public class ContractDocumentsPanel extends AbstractTabPanel implements SaveClickListener, DeleteClickListener, FrmkServicesHelper {

	/** */
	private static final long serialVersionUID = -9110947820488165481L;
	
	//private TextField txtDesc;
	//private TextField txtDescEn;
	private TextField txtReference;
	private Upload upload;
	private DocumentFileUploader uploader;
	private NavigationPanel navigationPanel;
	private VerticalLayout messagePanel;
	private Item selectedItem;
	private SimplePagedTable<ContractDocument> contractDocumentTable;
	
	private Contract contract;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		//txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		//txtDesc = ComponentFactory.getTextField("desc", false, 60, 200);
		txtReference = ComponentFactory.getTextField("reference", true, 60, 200);
		
		DocumentFileUploader documentFileUploader = new DocumentFileUploader("ContractDocuments");
		uploader = documentFileUploader;
		upload = new Upload();
		upload.setCaption(I18N.message("contract.document"));
		upload.setReceiver(uploader);
		upload.addSucceededListener(uploader);
		
		navigationPanel = new NavigationPanel();
		navigationPanel.addSaveClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		
		this.messagePanel = new VerticalLayout();
		this.messagePanel.setMargin(true);
		this.messagePanel.setVisible(false);
		this.messagePanel.addStyleName("message");
		
		FormLayout formLayout = new FormLayout();
		formLayout.setSpacing(true);
		formLayout.setMargin(true);
		
		formLayout.addComponent(txtReference);
		//formLayout.addComponent(txtDescEn);
		//formLayout.addComponent(txtDesc);
		formLayout.addComponent(upload);
		
		contractDocumentTable = new SimplePagedTable<ContractDocument>(createColumnDefinitions());
		contractDocumentTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			/** */
			private static final long serialVersionUID = 5827412946453700690L;
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
			}
		});
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(this.messagePanel);
		verticalLayout.addComponent(formLayout);
		
		VerticalLayout contLayoutLayout = new VerticalLayout();
		contLayoutLayout.setSpacing(true);
		
		contLayoutLayout.addComponent(new Panel(verticalLayout));
		contLayoutLayout.addComponent(contractDocumentTable);
		contLayoutLayout.addComponent(contractDocumentTable.createControls());
		
		return contLayoutLayout;
	}
	
	/**
	 * @param contract
	 */
	public void assignValues (Contract contract) {
		reset();
		this.contract = contract;
		this.messagePanel.setVisible(false);
		setIndexedContainer();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent paramClickEvent) {
		this.errors.clear();
		this.messagePanel.setVisible(false);
		if (validate()) {
			saveEntity();
			displaySuccess();
			setIndexedContainer();
			reset();
			if (getParent() instanceof TabSheet)
				((TabSheet) getParent()).setNeedRefresh(true);
		} else {
			displayErrors();
		}
	}
	
	/**
	 * Display error message
	 */
	private void displayErrors() {
		this.messagePanel.removeAllComponents();
		if (!(this.errors.isEmpty())) {
			for (String error : this.errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				this.messagePanel.addComponent(messageLabel);
			}
			this.messagePanel.setVisible(true);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#displaySuccess()
	 */
	@Override
	protected void displaySuccess () {
		Notification notification = new Notification("");
		notification.setDescription(I18N.message("save.successfully"));
		notification.show(UI.getCurrent().getPage());
	}
	
	/**
	 */
	private void saveEntity() {
		if (this.contract != null) {
			ContractDocument contractDocument = new ContractDocument();
			contractDocument.setReference(txtReference.getValue());
			//contractDocument.setDesc(txtDesc.getValue());
			//contractDocument.setDescEn(txtDescEn.getValue());
			contractDocument.setPath(uploader.getFileNameDoc());
			contractDocument.setContract(this.contract);
			ENTITY_SRV.saveOrUpdate(contractDocument);
			
			if (this.contract.getContractDocuments() == null || this.contract.getContractDocuments().isEmpty()) {
				this.contract.setContractDocuments(new ArrayList<ContractDocument>());
			}
			this.contract.getContractDocuments().add(contractDocument);
		}
	}
	
	/**
	 * @return
	 */
	private boolean validate () {
		//checkMandatoryField(txtDescEn, "desc.en");
		checkMandatoryField(txtReference, "reference");
		checkMandatoryUploadField(uploader, "contract.document");
		return this.errors.isEmpty();
	}
	
	/**
	 * @param uploader
	 * @param messageKey
	 * @return
	 */
	protected boolean checkMandatoryUploadField(DocumentFileUploader uploader, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isEmpty(uploader.getFileNameDoc())) {
			this.errors.add(I18N.message("field.required.1",
					new String[] { I18N.message(messageKey) }));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 */
	public void reset() {
		this.messagePanel.setVisible(false);
		this.errors.clear();
		txtReference.setValue("");
		//txtDesc.setValue("");
		//txtDescEn.setValue("");
		uploader.setFileNameDoc("");
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.RIGHT, 60));
		columnDefinitions.add(new ColumnDefinition("download", I18N.message("download"), Button.class, Align.CENTER, 60));
		columnDefinitions.add(new ColumnDefinition("reference", I18N.message("reference"), String.class, Align.LEFT, 250));
		columnDefinitions.add(new ColumnDefinition("desc.en", I18N.message("desc.en"), String.class, Align.LEFT, 250));
		columnDefinitions.add(new ColumnDefinition("desc", I18N.message("desc"), String.class, Align.LEFT, 250));
		
		return columnDefinitions;
	}
	
	/**
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer() {
		Indexed indexedContainer = contractDocumentTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (contract.getContractDocuments() == null || contract.getContractDocuments().isEmpty()) {
			return;
		}
		
		for (ContractDocument document : contract.getContractDocuments()) {
			Item item = indexedContainer.addItem(document.getId());
			item.getItemProperty("id").setValue(document.getId());
			item.getItemProperty("reference").setValue(document.getReference());
			//item.getItemProperty("desc.en").setValue(document.getDescEn());
			//item.getItemProperty("desc").setValue(document.getDesc());
			
			Button btnDocument = new Button();
			btnDocument.setIcon(new ThemeResource("../nkr-default/icons/16/pdf.png"));
			btnDocument.setData(document.getPath());
			btnDocument.setStyleName(Runo.BUTTON_LINK);
			btnDocument.addClickListener(new ClickListener() {
				/** */
				private static final long serialVersionUID = -4354233214566367620L;
				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					new DisplayDocumentPanel((String) ((Button) event.getSource()).getData()).display();
				}
			});
			
			item.getItemProperty("download").setValue(btnDocument);
		}
		contractDocumentTable.refreshContainerDataSource();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener#deleteButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void deleteButtonClick(ClickEvent paramClickEvent) {
		if (selectedItem != null) {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single", String.valueOf(selectedItem.getItemProperty("id").getValue())),
					new ConfirmDialog.Listener() {
						/** */
						private static final long serialVersionUID = 1638433477273353678L;
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								ContractDocument document = ENTITY_SRV.getById(ContractDocument.class, (Long) selectedItem.getItemProperty("id").getValue());
								if (document != null) {
									contract.getContractDocuments().remove(document);
									ENTITY_SRV.delete(document);
								}
								setIndexedContainer();
								selectedItem = null;
							}
						}
					});
		} else {
			MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"),
					new MessageBox.ButtonConfig[] { new MessageBox.ButtonConfig(
							MessageBox.ButtonType.OK, I18N.message("ok")) });
			mb.setWidth("300px");
			mb.setHeight("150px");
			mb.show();
		}
	}

}

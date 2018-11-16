package com.nokor.efinance.core.contract.panel;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractDocument;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.EDocumentStatus;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class ValidationTabPanel extends AbstractTabPanel implements FinServicesHelper, AddClickListener, SaveClickListener {

	/** */
	private static final long serialVersionUID = 4655171918946640200L;
	
	private SimpleTable<ContractDocument> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
			
	private List<ContractDocument> contractDocuments;
	private List<EntityRefComboBox<Document>> cbxDocuments;
	private List<ERefDataComboBox<EDocumentStatus>> cbxDocumentStatuses;
	private List<TextArea> txtComments;
	
	private Contract contract;
	
	/**
	 * 
	 */
	public ValidationTabPanel() {
		setMargin(true);
		setSpacing(true);
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addSaveClickListener(this);
		addComponent(navigationPanel, 0);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxDocuments = new ArrayList<>();
		cbxDocumentStatuses = new ArrayList<>();
		txtComments = new ArrayList<>();
		columnDefinitions = createColumnDefinitions();
	    pagedTable = new SimpleTable<ContractDocument>(columnDefinitions);
		return pagedTable;
	}
	
	/**
	 * 
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("document", I18N.message("document"), EntityRefComboBox.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition("issue", I18N.message("issue"), ERefDataComboBox.class, Align.LEFT, 240));
		columnDefinitions.add(new ColumnDefinition("comment", I18N.message("comment"), TextArea.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition("by", I18N.message("by"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("button", I18N.message("event"), Button.class, Align.LEFT, 80));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contractDocuments
	 */
	@SuppressWarnings({ "unchecked" })
	private void setIndexedContainer(List<ContractDocument> contractDocuments) {
		pagedTable.removeAllItems();
		Container indexedContainer = pagedTable.getContainerDataSource();
		if (contractDocuments != null) {
			int index = 0;
			clear();
			pagedTable.setPageLength(contractDocuments.size());
			for (ContractDocument contractDocument : contractDocuments) {
				Item item = indexedContainer.addItem(index);
				Button btnDelete = new Button(I18N.message("delete"), FontAwesome.TIMES);
				btnDelete.setStyleName(Reindeer.BUTTON_SMALL);
				btnDelete.addClickListener(new DeleteContractDocument(contractDocument, index));
				
				EntityRefComboBox<Document> cbxDocument = new EntityRefComboBox<Document>();
				cbxDocument.setRestrictions(new BaseRestrictions<>(Document.class));
			    cbxDocument.setRequired(true);
			    cbxDocument.renderer();
			    cbxDocument.setWidth("180px");
				cbxDocument.setSelectedEntity(contractDocument.getDocument());
				
				ERefDataComboBox<EDocumentStatus> cbxDocumentStatus = new ERefDataComboBox<EDocumentStatus>(EDocumentStatus.class);
				cbxDocumentStatus.setWidth("200px");
				cbxDocumentStatus.setSelectedEntity(contractDocument.getStatus());
				
				TextArea txtComment = ComponentFactory.getTextArea(false, 180, 40);
				txtComment.setValue(contractDocument.getComment() != null ? contractDocument.getComment() : "");
				
				item.getItemProperty("document").setValue(cbxDocument);
				item.getItemProperty("issue").setValue(cbxDocumentStatus);
				item.getItemProperty("comment").setValue(txtComment);
				item.getItemProperty("button").setValue(btnDelete);
				
				cbxDocuments.add(cbxDocument);
				cbxDocumentStatuses.add(cbxDocumentStatus);
				txtComments.add(txtComment);	
				
				index++;
			}
		}
	}
	
	/**
	 * save or update ContractDocument
	 */
	private void save(List<ContractDocument> contractDocuments) {
		if (contractDocuments != null && !contractDocuments.isEmpty()) {
			int index = 0;
			for (ContractDocument contractDocument : contractDocuments) {
				contractDocument.setContract(contract);
				contractDocument.setDocument(cbxDocuments.get(index).getSelectedEntity());
				contractDocument.setStatus(cbxDocumentStatuses.get(index).getSelectedEntity());
				contractDocument.setComment(txtComments.get(index).getValue());
				
				CONT_SRV.saveOrUpdate(contractDocument);
				
				index++;
			}
			displaySuccess();
			setIndexedContainer(contractDocuments);
		} else {
			errors.add(I18N.message("field.required.1", I18N.message("data.is.empty")));
			displayErrorsPanel();
		}
	}
	
	/**
	 * assign value
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		super.removeErrorsPanel();
		if (contract != null) {
			this.contract = contract;
			contractDocuments = DOC_SRV.getDocumentsByContract(contract.getId());
			setIndexedContainer(contractDocuments);
		}
	}
	
	/**
	 * clear list
	 */
	private void clear() {
		cbxDocuments.clear();
		cbxDocumentStatuses.clear();
		txtComments.clear();
	}
	
	/**
	 * Confirm Delete ContractDocument
	 * @param
	 */
	private void confirmDeleteContractDocument(ContractDocument contractDocument) {
		final DeleteInfoContractDocument deleteInfoContractDocument = new DeleteInfoContractDocument();
		deleteInfoContractDocument.setContractDocument(contractDocument);
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"),
				new ConfirmDialog.Listener() {
					/** */
					private static final long serialVersionUID = 2380193173874927880L;
					
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							ContractDocument contractDocument = deleteInfoContractDocument.getContractDocument();
							CONT_SRV.delete(contractDocument);
							contractDocuments.remove(contractDocument);
							setIndexedContainer(contractDocuments);
							Notification.show("",I18N.message("delete.successfully"),Notification.Type.HUMANIZED_MESSAGE);
						}
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class DeleteContractDocument implements ClickListener {
		
		/** */
		private static final long serialVersionUID = -4298038912389634537L;
		
		private ContractDocument contractDocument;
		private int index;
		
		public DeleteContractDocument(ContractDocument contractDocument, int index) {
			this.contractDocument = contractDocument;
			this.index = index;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			for (int i = 0; i < contractDocuments.size(); i++) {
				contractDocuments.get(i).setDocument(cbxDocuments.get(i).getSelectedEntity());
				contractDocuments.get(i).setStatus(cbxDocumentStatuses.get(i).getSelectedEntity());
				contractDocuments.get(i).setComment(txtComments.get(i).getValue());
			}
			if (this.contractDocument.getId() != null) {
				confirmDeleteContractDocument(this.contractDocument);
			} else {
				contractDocuments.remove(this.index);
				setIndexedContainer(contractDocuments);
			}
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class DeleteInfoContractDocument {
		
		private ContractDocument contractDocument;

		/**
		 * @return the contractDocument
		 */
		public ContractDocument getContractDocument() {
			return contractDocument;
		}

		/**
		 * @param contractDocument the contractDocument to set
		 */
		public void setContractDocument(ContractDocument contractDocument) {
			this.contractDocument = contractDocument;
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		save(contractDocuments);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		removeErrorsPanel();
		if (contractDocuments != null) {
			if (!contractDocuments.isEmpty()) {
				for (int i = 0; i < contractDocuments.size(); i++) {
					contractDocuments.get(i).setDocument(cbxDocuments.get(i).getSelectedEntity());
					contractDocuments.get(i).setStatus(cbxDocumentStatuses.get(i).getSelectedEntity());
					contractDocuments.get(i).setComment(txtComments.get(i).getValue());
				}
			}
			ContractDocument contractDocument = EntityFactory.createInstance(ContractDocument.class);
			contractDocuments.add(contractDocument);
			pagedTable.setPageLength(contractDocuments.size());
			setIndexedContainer(contractDocuments);
		}
	}
	
}

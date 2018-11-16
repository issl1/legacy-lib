package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.collection.model.ContractOperation;
import com.nokor.efinance.core.collection.model.EOperationType;
import com.nokor.efinance.core.collection.service.ContractOperationRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author buntha.chea
 *
 */
public class ContractOperationsPanel extends AbstractFormPanel implements FinServicesHelper, ClickListener {

	/** */
	private static final long serialVersionUID = -2336223266190300646L;
	
	private ContractOperation contractOperation;
	private Contract contract;
	
	private SimpleTable<ContractOperation> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private TextField txtAttribute;
	private TextField txtPrice;
	
	private Button btnNew;
	private Button btnSubmit;
	private Button btnCancel;
	
	private CheckBox cbDocumentDelivery;
	
	private ERefDataComboBox<EOperationType> cbxOperation;
	private ERefDataComboBox<EApplicantCategory> cbxMethod;
	private ComboBox cbxAddress;
	
	private VerticalLayout verLayout;
	
	/**
	 * 
	 */
	public ContractOperationsPanel() {
		super.init();
		this.columnDefinitions = createColumnDefinition();
		simpleTable = new SimpleTable<>(this.columnDefinitions);
		simpleTable.setPageLength(5);
		simpleTable.addItemClickListener(new ItemClickListener() {
			
			/** */
			private static final long serialVersionUID = -2706632553217860768L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				Long id = (Long) event.getItem().getItemProperty(ContractOperation.ID).getValue();
				if (event.isDoubleClick()) {
					setVisibledControls(true);
					contractOperation = ENTITY_SRV.getById(ContractOperation.class, id);
					assignValueToControls(contractOperation);
				}
			}
		});
		
		setMargin(true);
		setSpacing(true);
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @return
	 */
	protected Component createForm() {
		btnNew = new NativeButton(I18N.message("new"), this);
		btnNew.setStyleName("btn btn-success button-small");
		btnNew.setWidth("60px");
		
		btnSubmit = new NativeButton(I18N.message("submit"), this);
		btnSubmit.setStyleName("btn btn-success button-small");
		btnSubmit.setWidth("60px");
		
		btnCancel = ComponentLayoutFactory.getButtonCancel();
		btnCancel.setStyleName("btn btn-success button-small");
		btnCancel.setWidth("60px");
		btnCancel.addClickListener(this);
		
		cbDocumentDelivery = new CheckBox(I18N.message("document.delivery"));
		
		cbxMethod = new ERefDataComboBox<>(EApplicantCategory.values());
		cbxMethod.setWidth(120, Unit.PIXELS);
		
		cbxOperation = new ERefDataComboBox<>(EOperationType.values());
		cbxOperation.setWidth(130, Unit.PIXELS);
		
		cbxAddress = new ComboBox();
		cbxAddress.setWidth(130, Unit.PIXELS);
		
		txtAttribute = ComponentFactory.getTextField(false, 100, 120);
		txtPrice = ComponentFactory.getTextField(false, 100, 120);
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("operation"));
		horLayout.addComponent(cbxOperation);
		horLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("attribute"));
		horLayout.addComponent(txtAttribute);
		horLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("method"));
		horLayout.addComponent(cbxMethod);
		horLayout.addComponent(cbDocumentDelivery);
		horLayout.addComponent(cbxAddress);
		
		horLayout.setComponentAlignment(cbDocumentDelivery, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout priceLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		priceLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("price"));
		priceLayout.addComponent(txtPrice);
		priceLayout.addComponent(btnCancel);
		priceLayout.addComponent(btnSubmit);
		
		verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		verLayout.addComponent(btnNew);
		verLayout.addComponent(horLayout);
		verLayout.addComponent(priceLayout);
		verLayout.setComponentAlignment(priceLayout, Alignment.TOP_RIGHT);
		
		setVisibledControls(false);
		
		Panel mainPanel = new Panel(verLayout);
		
		return mainPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		this.contractOperation.setContract(this.contract);
		this.contractOperation.setOperationType(cbxOperation.getSelectedEntity());
		this.contractOperation.setTiPrice(getDouble(txtPrice));
		return this.contractOperation;
	}
	
	/**
	 * AssignValues
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		reset();
		this.contract = contract;
		
		setIndexContainer(contract);
	}
	
	/**
	 * createColumnDefinition
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(ContractOperation.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(ContractOperation.OPERATIONTYPE, I18N.message("operation"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(ContractOperation.CREATEDATE, I18N.message("request.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(ContractOperation.TIPRICE, I18N.message("price"), String.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(ContractOperation.BALANCE, I18N.message("balance"), String.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(ContractOperation.DEADLINE, I18N.message("deadline"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(ContractOperation.ACTIONS, I18N.message("actions"), Button.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * SetIndexContainer
	 * @param contract
	 */
	@SuppressWarnings("unchecked")
	private void setIndexContainer(Contract contract) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		List<ContractOperation> contractOperations = this.getContractOperations(contract);
		for (ContractOperation contractOperation : contractOperations) {
			Item item = indexedContainer.addItem(contractOperation.getId());
			item.getItemProperty(ContractOperation.ID).setValue(contractOperation.getId());
			item.getItemProperty(ContractOperation.OPERATIONTYPE).setValue(contractOperation.getOperationType() != null ? contractOperation.getOperationType().getDescEn() : "");
			item.getItemProperty(ContractOperation.CREATEDATE).setValue(contractOperation.getCreateDate());
			item.getItemProperty(ContractOperation.TIPRICE).setValue(MyNumberUtils.formatDoubleToString(
					MyNumberUtils.getDouble(contractOperation.getTiPrice()), "###,##0.00"));
			item.getItemProperty(ContractOperation.BALANCE).setValue("");
			item.getItemProperty(ContractOperation.DEADLINE).setValue(null);
			Button btnDelete = ComponentLayoutFactory.getButtonIcon(FontAwesome.TRASH_O);
			item.getItemProperty(ContractOperation.ACTIONS).setValue(btnDelete);
			
			btnDelete.addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = 2028754480106760309L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"), new ConfirmDialog.Listener() {
						
						/** */
						private static final long serialVersionUID = 7523390811089522343L;

						/**
						 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
						 */
						@Override
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								try {
									ENTITY_SRV.delete(contractOperation);
									ComponentLayoutFactory.displaySuccessMsg("delete.successfully");
									setIndexContainer(contract);
								} catch (Exception e) {
									logger.error(e.getMessage());
									if (e instanceof DataIntegrityViolationException) {
				    					ComponentLayoutFactory.displayErrorMsg("msg.warning.delete.selected.item.is.used");
				    				} else {
				    					ComponentLayoutFactory.displayErrorMsg("msg.error.technical");
				    				}
								}
							}
						}
					});
				}
			});
		}
	}
	
	/**
	 * get Contract Operation By Contract
	 * @param contract
	 * @return
	 */
	private List<ContractOperation> getContractOperations(Contract contract) {
		ContractOperationRestriction restrictions = new ContractOperationRestriction();
		restrictions.setConId(contract.getId());
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param visible
	 */
	private void setVisibledControls(boolean visible) {
		verLayout.getComponent(1).setVisible(visible);
		verLayout.getComponent(2).setVisible(visible);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		setVisibledControls(false);
		resetControls();
	}
	
	/**
	 * 
	 */
	private void resetControls() {
		super.reset();
		this.contractOperation = ContractOperation.createInstance();
		txtAttribute.setValue(StringUtils.EMPTY);	
		txtPrice.setValue(StringUtils.EMPTY);		
		cbxAddress.setValue(null);
		cbxMethod.setSelectedEntity(null);
		cbxOperation.setSelectedEntity(null);
		cbDocumentDelivery.setValue(false);
	}
	
	/**
	 * 
	 * @param operation
	 */
	private void assignValueToControls(ContractOperation operation) {
		txtPrice.setValue(MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(operation.getTiPrice()), "###,##0.00"));
		txtAttribute.setValue(""); 	  	   // TODO
		cbxMethod.setSelectedEntity(null); // TODO
		cbxAddress.setValue(null); 		   // TODO
		cbxOperation.setSelectedEntity(operation.getOperationType());
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		super.reset();
		checkMandatorySelectField(cbxOperation, "operation");
//		checkMandatorySelectField(cbxMethod, "method");
//		checkMandatoryField(txtAttribute, "attribute");
		checkMandatoryField(txtPrice, "price");
		checkDoubleField(txtPrice, "price");
		return errors.isEmpty();
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnNew)) {
			setVisibledControls(!verLayout.getComponent(1).isVisible());
		} else if (event.getButton() == btnCancel) {
			this.reset();
			setIndexContainer(this.contract);
		} else if (event.getButton() == btnSubmit) {
			if (validate()) {
				ENTITY_SRV.saveOrUpdate(getEntity());
				displaySuccess();
				resetControls();
				setIndexContainer(this.contract);
			} else {
				displayErrors();
			}
		}
	}

}

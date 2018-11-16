package com.nokor.efinance.ra.ui.panel.finproduct.financialproduct;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinProductService;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.shared.financialproduct.FinancialProductEntityField;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class FinancialProductServiceTablePanel extends VerticalLayout implements FinancialProductEntityField, SeuksaServicesHelper {
	
	private static final long serialVersionUID = -7538697565112892189L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private FinProduct finProduct;
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<FinProductService> pagedTable;
	private Item selectedItem = null;
	private EntityRefComboBox<FinService> cbxService;
	private List<String> errors;
	private VerticalLayout messagePanel;
	private List<EServiceType> serviceTypes;
	private Window winAddService;
	
	/**
	 * 
	 * @param caption
	 * @param serviceTypes
	 */
	public FinancialProductServiceTablePanel(String caption, List<EServiceType> serviceTypes) {
		this.serviceTypes = serviceTypes;
		setSizeFull();
		setMargin(true);
		setSpacing(true);	
		
		errors = new ArrayList<String>();
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAddService = new NativeButton(I18N.message("add"));
		btnAddService.setIcon(FontAwesome.PLUS);
		btnAddService.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = -4058398610792221873L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				selectedItem = null;
				createServiceForm(caption);
			}
		});
		
		NativeButton btnDeleteService = new NativeButton(I18N.message("delete"));
		btnDeleteService.setIcon(FontAwesome.TRASH_O);
		btnDeleteService.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = 2910252608135870153L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (selectedItem == null) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
							MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				} else {
					final Long id = (Long) selectedItem.getItemProperty("id").getValue();
					ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single", String.valueOf(id)),
							new ConfirmDialog.Listener() {

						/** */
						private static final long serialVersionUID = -5719696946393183890L;
						
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								ENTITY_SRV.delete(FinProductService.class, id);
								assignValues(finProduct.getId());
								selectedItem = null;
							}
						}
					});
				}
			}
		});
		
		navigationPanel.addButton(btnAddService);
		navigationPanel.addButton(btnDeleteService);
		
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<>(this.columnDefinitions);
		
		pagedTable.addItemClickListener(new ItemClickListener() {
			
			/** */
			private static final long serialVersionUID = -6676228064499031341L;
			
			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				if (selectedItem != null && event.isDoubleClick()) {
					createServiceForm(caption);
				}
			}
		});
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * 
	 * @param caption
	 */
	private void createServiceForm(String caption) {
		winAddService = new Window(I18N.message(caption));
		winAddService.setModal(true);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
						
        FormLayout frmLayout = new FormLayout();
        frmLayout.setMargin(true);
        frmLayout.setSpacing(true);
        frmLayout.addStyleName("myform-align-left");
        
        BaseRestrictions<FinService> restrictions = new BaseRestrictions<>(FinService.class);
        restrictions.addCriterion(Restrictions.in("serviceType", serviceTypes));
        
        cbxService = new EntityRefComboBox<>(I18N.message("service")); 
        cbxService.setRestrictions(restrictions);
        cbxService.setRequired(true);
        cbxService.renderer();
        
        reset();
        		        
        frmLayout.addComponent(cbxService);
        
        FinProductService finProductService;
        if (selectedItem == null) {
			finProductService = EntityFactory.createInstance(FinProductService.class);
		} else {
			Long id = (Long) selectedItem.getItemProperty(ID).getValue();
			finProductService = ENTITY_SRV.getById(FinProductService.class, id);
			cbxService.setSelectedEntity(finProductService.getService());
		}
        
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

        	/** */
			private static final long serialVersionUID = -4024064977917270885L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (validate()) {
					finProductService.setService(cbxService.getSelectedEntity());
					finProductService.setFinancialProduct(finProduct);
					finProductService.setStatusRecord(EStatusRecord.ACTIV);
					ENTITY_SRV.saveOrUpdate(finProductService);
					winAddService.close();
					assignValues(finProduct.getId());
					selectedItem = null;
				} else {
					displayErrors();
				}
			}
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 3975121141565713259L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
            	winAddService.close();
            }
        });
		btnCancel.setIcon(FontAwesome.TIMES);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(frmLayout);
        
        winAddService.setContent(contentLayout);
        UI.getCurrent().addWindow(winAddService);
	}
	
	/**
	 * @param fiprdId
	 */
	public void assignValues(Long fiprdId) {
		if (fiprdId != null) {
			finProduct = ENTITY_SRV.getById(FinProduct.class, new Long(fiprdId));
			pagedTable.setContainerDataSource(getIndexedContainer(finProduct));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(FinProduct financialProduct) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (financialProduct.getFinancialProductServices() != null && !financialProduct.getFinancialProductServices().isEmpty()) {
			for (FinProductService financialProductService : financialProduct.getFinancialProductServices()) {
				if (serviceTypes.contains(financialProductService.getService().getServiceType())) {
					Item item = indexedContainer.addItem(financialProductService.getId());
					item.getItemProperty(ID).setValue(financialProductService.getId());
					item.getItemProperty(SERVICE).setValue(financialProductService.getService().getDescEn());
				}
			}
		}
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(SERVICE, I18N.message("code"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}	
	
	/**
	 * Validate the service form
	 * @return
	 */
	private boolean validate() {
		removeErrorComponent();
		if (cbxService.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("service") }));
		}
		return errors.isEmpty();
	}
	
	/**
	 * Reset
	 */
	private void reset() {
		removeErrorComponent();
		cbxService.setSelectedEntity(null);
	}
	
	/**
	 * Display Errors
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
	 * 
	 */
	private void removeErrorComponent() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		errors.clear();
	}
}

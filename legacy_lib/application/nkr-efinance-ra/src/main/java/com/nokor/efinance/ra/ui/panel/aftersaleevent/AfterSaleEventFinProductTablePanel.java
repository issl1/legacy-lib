package com.nokor.efinance.ra.ui.panel.aftersaleevent;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.aftersale.AfterSaleEvent;
import com.nokor.efinance.core.aftersale.AfterSaleEventFinProduct;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.shared.FMEntityField;
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

/**
 * Add Financial Product In After Sale Event
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AfterSaleEventFinProductTablePanel extends VerticalLayout implements FMEntityField {
	
	private static final long serialVersionUID = -7538697565112892189L;
	
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	private Logger LOG = LoggerFactory.getLogger(AfterSaleEventFinProductTablePanel.class);
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<AfterSaleEventFinProduct> pagedTable;
	private Item selectedItem = null;
	private Long afterSaleEventFinProductId;
	private VerticalLayout messagePanel;
	private AfterSaleEventFinProduct afterSaleEventFinProduct;
	private AfterSaleEvent afterSaleEvent;
	
	private EntityRefComboBox<FinProduct> cbxFinProduct;
	
	
	
	@PostConstruct
	public void PostConstruct() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);	
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAddFinProduct = new NativeButton(I18N.message("add"));
		btnAddFinProduct.setIcon(FontAwesome.PLUS_CIRCLE);
		btnAddFinProduct.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4058398610792221873L;

			@Override
			public void buttonClick(ClickEvent event) {
				messagePanel.removeAllComponents();
				messagePanel.setVisible(false);
				afterSaleEventFinProductId = null;
				getFinProductForm(afterSaleEventFinProductId);
			}
		});	
		NativeButton btnDeleteFinProduct = new NativeButton(I18N.message("delete"));
		btnDeleteFinProduct.setIcon(FontAwesome.TRASH_O);
		btnDeleteFinProduct.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7179741868226690073L;

			@Override
			public void buttonClick(ClickEvent event) {
				delete();
			}
		});
			
		
		navigationPanel.addButton(btnAddFinProduct);
		navigationPanel.addButton(btnDeleteFinProduct);
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<AfterSaleEventFinProduct>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				afterSaleEventFinProductId = (Long) selectedItem.getItemProperty("id").getValue();
			}
		});
		
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * @param dealerId
	 */
	public void assignValues(Long afterSaleEventId) {
		if (afterSaleEventId != null) {
			this.afterSaleEvent = entityService.getById(AfterSaleEvent.class, afterSaleEventId);
			List<AfterSaleEventFinProduct> afterSaleEventFinProducts = this.getAfterSaleEventFinProductById(this.afterSaleEvent); 
			pagedTable.setContainerDataSource(getIndexedContainer(afterSaleEventFinProducts));
		} else {
			pagedTable.removeAllItems();
		}
	}
	/**
	 * 
	 * @param afterSaleEventId
	 * @return
	 */
	private List<AfterSaleEventFinProduct> getAfterSaleEventFinProductById(AfterSaleEvent afterSaleEvent) {
		BaseRestrictions<AfterSaleEventFinProduct> restrictions = new BaseRestrictions<>(AfterSaleEventFinProduct.class);
		restrictions.addCriterion(Restrictions.eq("afterSaleEvent", afterSaleEvent));
		List<AfterSaleEventFinProduct> afterSaleEventFinProducts = entityService.list(restrictions);
		return afterSaleEventFinProducts;
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<AfterSaleEventFinProduct> afterSaleEventFinProducts) {
		IndexedContainer indexedContainer = new IndexedContainer();		
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (AfterSaleEventFinProduct afterSaleEventFinProduct : afterSaleEventFinProducts) {
			Item item = indexedContainer.addItem(afterSaleEventFinProduct.getId());
			item.getItemProperty(ID).setValue(afterSaleEventFinProduct.getId());
			item.getItemProperty(FINANCIAL_PRODUCT).setValue(afterSaleEventFinProduct.getFinProduct().getDescEn());
		}
		
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(FINANCIAL_PRODUCT, I18N.message("financial.product"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	
	/**
	 * Display FinProduct in Windown Popup (Save or Update)
	 * @param afterSaleEventFinProductId
	 */
	public void getFinProductForm(final Long afterSaleEventFinProductId) {
		this.afterSaleEventFinProductId = afterSaleEventFinProductId;
		final Window winAfterSaleFinProduct = new Window(I18N.message("financial.product"));
		winAfterSaleFinProduct.setModal(true);
		winAfterSaleFinProduct.setResizable(false);
		winAfterSaleFinProduct.setWidth(390, Unit.PIXELS);
		winAfterSaleFinProduct.setHeight(163, Unit.PIXELS);
	    
		VerticalLayout contentLayout = new VerticalLayout();
		cbxFinProduct = new EntityRefComboBox<>(I18N.message("financial.product"));
		cbxFinProduct.setRestrictions(new BaseRestrictions<>(FinProduct.class));
		cbxFinProduct.renderer();
		cbxFinProduct.setImmediate(true);
		cbxFinProduct.setRequired(true);
						
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setSpacing(true);
        
        if (afterSaleEventFinProductId != null) {
			afterSaleEventFinProduct = entityService.getById(AfterSaleEventFinProduct.class, afterSaleEventFinProductId);
			cbxFinProduct.setSelectedEntity(afterSaleEventFinProduct.getFinProduct());
		}
        
        formLayout.addComponent(cbxFinProduct);
               
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			private static final long serialVersionUID = -4024064977917270885L;

			public void buttonClick(ClickEvent event) {
				if (validate()) {
					if (afterSaleEventFinProductId == null) {
							afterSaleEventFinProduct = new AfterSaleEventFinProduct();
							afterSaleEventFinProduct.setFinProduct(cbxFinProduct.getSelectedEntity());
							afterSaleEventFinProduct.setAfterSaleEvent(afterSaleEvent);
							entityService.saveOrUpdate(afterSaleEventFinProduct);
						} 
					winAfterSaleFinProduct.close();
					assignValues(afterSaleEvent.getId());	
				}
				
            }
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			private static final long serialVersionUID = 3975121141565713259L;

			public void buttonClick(ClickEvent event) {
            	winAfterSaleFinProduct.close();
            }
        });
		btnCancel.setIcon(FontAwesome.TIMES);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
        contentLayout.addComponent(formLayout);
        
        winAfterSaleFinProduct.setContent(contentLayout);
        UI.getCurrent().addWindow(winAfterSaleFinProduct);
	
	}	
	/**
	 * Validate Add FinProduct Field Required 
	 * @return
	 */
	public boolean validate() {
		messagePanel.removeAllComponents();
		List<String> errors = new ArrayList<>();
		errors.clear();
		if (cbxFinProduct.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("financial.product") }));
		}
		if (!errors.isEmpty()) {
			for (String error : errors) {
				Label messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		return errors.isEmpty();
	}
	
	/**
	 * Delete Financial Products
	 */
	private void delete() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			//final Long id = (Long) selectedItem.getItemProperty("id").getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {afterSaleEventFinProductId.toString()}),
					new ConfirmDialog.Listener() {

				/** */
				private static final long serialVersionUID = -1278300263633872114L;

				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
					    LOG.debug("[>> deleteAfterSaleFinProduct]");
						entityService.delete(AfterSaleEventFinProduct.class, afterSaleEventFinProductId);
						LOG.debug("This item " + afterSaleEventFinProductId + "deleted successfully !");
						LOG.debug("[<< deleteAfterSaleFinProduct]");
					    dialog.close();
						assignValues(afterSaleEvent.getId());
						selectedItem = null;
					}
				}
			});
		}
	}
}

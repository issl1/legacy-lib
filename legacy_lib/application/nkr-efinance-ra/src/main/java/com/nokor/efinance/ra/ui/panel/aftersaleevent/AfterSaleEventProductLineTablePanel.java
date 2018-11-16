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
import com.nokor.efinance.core.aftersale.AfterSaleEventProductLine;
import com.nokor.efinance.core.dealer.model.DealerAssetModel;
import com.nokor.efinance.core.financial.model.ProductLine;
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
 * Add ProductLine In After Sale Event
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AfterSaleEventProductLineTablePanel extends VerticalLayout implements FMEntityField {
	
	private static final long serialVersionUID = -7538697565112892189L;
	
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	private Logger LOG = LoggerFactory.getLogger(AfterSaleEventFinProductTablePanel.class);
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<DealerAssetModel> pagedTable;
	private Item selectedItem = null;
	private Long afterSaleEventProductLineId;
	private VerticalLayout messagePanel;
	private AfterSaleEventProductLine afterSaleEventProductLine;
	private AfterSaleEvent afterSaleEvent;
	
	private EntityRefComboBox<ProductLine> cbxProductLine;
	
	
	
	@PostConstruct
	public void PostConstruct() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);	
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAddProductLine = new NativeButton(I18N.message("add"));
		btnAddProductLine.setIcon(FontAwesome.PLUS_CIRCLE);
		btnAddProductLine.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4058398610792221873L;

			@Override
			public void buttonClick(ClickEvent event) {
				messagePanel.removeAllComponents();
				messagePanel.setVisible(false);
				afterSaleEventProductLineId = null;
				getProductLineForm(afterSaleEventProductLineId);
			}
		});		
		NativeButton btnDeleteProductLine = new NativeButton(I18N.message("delete"));
		btnDeleteProductLine.setIcon(FontAwesome.TRASH_O);
		btnDeleteProductLine.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -682442556367422293L;

			@Override
			public void buttonClick(ClickEvent event) {
				delete();
			}
		});
		
		navigationPanel.addButton(btnAddProductLine);	
		navigationPanel.addButton(btnDeleteProductLine);	
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<DealerAssetModel>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				afterSaleEventProductLineId = (Long) selectedItem.getItemProperty(ID).getValue();
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
			List<AfterSaleEventProductLine> afterSaleEventProductLines = this.getAfterSaleEventProductLineById(this.afterSaleEvent); 
			pagedTable.setContainerDataSource(getIndexedContainer(afterSaleEventProductLines));
		} else {
			pagedTable.removeAllItems();
		}
	}
	/**
	 * 
	 * @param afterSaleEventId
	 * @return
	 */
	private List<AfterSaleEventProductLine> getAfterSaleEventProductLineById(AfterSaleEvent afterSaleEvent) {
		BaseRestrictions<AfterSaleEventProductLine> restrictions = new BaseRestrictions<>(AfterSaleEventProductLine.class);
		restrictions.addCriterion(Restrictions.eq("afterSaleEvent", afterSaleEvent));
		List<AfterSaleEventProductLine> afterSaleEventProductLines = entityService.list(restrictions);
		return afterSaleEventProductLines;
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<AfterSaleEventProductLine> afterSaleEventProductLines) {
		IndexedContainer indexedContainer = new IndexedContainer();		
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (AfterSaleEventProductLine afterSaleEventProductLine : afterSaleEventProductLines) {
			Item item = indexedContainer.addItem(afterSaleEventProductLine.getId());
			item.getItemProperty(ID).setValue(afterSaleEventProductLine.getId());
			item.getItemProperty(PRODUCT_LINE).setValue(afterSaleEventProductLine.getProductLine().getDescEn());
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
		columnDefinitions.add(new ColumnDefinition(PRODUCT_LINE, I18N.message("product.line"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	
	/**
	 * Display ProductLine in Windown Popup (Save or Update)
	 * @param afterSaleEventProductLineId
	 */
	public void getProductLineForm(final Long afterSaleEventProductLineId) {
		this.afterSaleEventProductLineId = afterSaleEventProductLineId;
		final Window winAfterSaleProductLine = new Window(I18N.message("product.line"));
		winAfterSaleProductLine.setModal(true);
		winAfterSaleProductLine.setResizable(false);
		winAfterSaleProductLine.setWidth(390, Unit.PIXELS);
		winAfterSaleProductLine.setHeight(163, Unit.PIXELS);
	    
		VerticalLayout contentLayout = new VerticalLayout();
		cbxProductLine = new EntityRefComboBox<>(I18N.message("product.line"));
		cbxProductLine.setRestrictions(new BaseRestrictions<>(ProductLine.class));
		cbxProductLine.renderer();
		cbxProductLine.setImmediate(true);
		cbxProductLine.setRequired(true);
						
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setSpacing(true);
        
        if (afterSaleEventProductLineId != null) {
			afterSaleEventProductLine = entityService.getById(AfterSaleEventProductLine.class, afterSaleEventProductLineId);
			cbxProductLine.setSelectedEntity(afterSaleEventProductLine.getProductLine());
		}
        
        formLayout.addComponent(cbxProductLine);
               
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			private static final long serialVersionUID = -4024064977917270885L;

			public void buttonClick(ClickEvent event) {
				if (validate()) {
					if (afterSaleEventProductLineId == null) {
						afterSaleEventProductLine = new AfterSaleEventProductLine();
						afterSaleEventProductLine.setProductLine(cbxProductLine.getSelectedEntity());
						afterSaleEventProductLine.setAfterSaleEvent(afterSaleEvent);
						entityService.saveOrUpdate(afterSaleEventProductLine);
						} 
					winAfterSaleProductLine.close();
					assignValues(afterSaleEvent.getId());	
				}
				
            }
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			private static final long serialVersionUID = 3975121141565713259L;

			public void buttonClick(ClickEvent event) {
            	winAfterSaleProductLine.close();
            }
        });
		btnCancel.setIcon(FontAwesome.TIMES);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
        contentLayout.addComponent(formLayout);
        
        winAfterSaleProductLine.setContent(contentLayout);
        UI.getCurrent().addWindow(winAfterSaleProductLine);
	
	}	
	/**
	 * Validate Add ProductLine Field Required 
	 * @return
	 */
	public boolean validate() {
		messagePanel.removeAllComponents();
		List<String> errors = new ArrayList<>();
		errors.clear();
		if (cbxProductLine.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("product.line") }));
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
	 * Delete Products Line in AfterSaleEvent
	 */
	private void delete() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {afterSaleEventProductLineId.toString()}),
					new ConfirmDialog.Listener() {

				/** */
				private static final long serialVersionUID = -1278300263633872114L;

				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
					    LOG.debug("[>> deleteAfterSaleProductLine]");
						entityService.delete(AfterSaleEventProductLine.class, afterSaleEventProductLineId);
						LOG.debug("This item " + afterSaleEventProductLineId + "deleted successfully !");
						LOG.debug("[<< deleteAfterSaleProductLine]");
					    dialog.close();
						assignValues(afterSaleEvent.getId());
						selectedItem = null;
					}
				}
			});
		}
	}
}

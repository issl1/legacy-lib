package com.nokor.efinance.ra.ui.panel.dealer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAssetModel;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
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
 * Add Asset model in dealer
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DealerAssetModelTablePanel extends VerticalLayout implements FMEntityField {
	
	private static final long serialVersionUID = -7538697565112892189L;
	
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<DealerAssetModel> pagedTable;
	private Item selectedItem = null;
	private long dealerId;
	private Dealer dealer;
	private Long dealerAssetModelId;
	private VerticalLayout messagePanel;
	
	private EntityRefComboBox<AssetRange> cbxAssetRange;
	private EntityRefComboBox<AssetModel> cbxAssetModel;
	
    private DealerAssetModel dealerAssetModel;
	
	@PostConstruct
	public void PostConstruct() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);	
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAddAssetModel = new NativeButton(I18N.message("add.asset.model"));
		btnAddAssetModel.setIcon(FontAwesome.PLUS_CIRCLE);
		btnAddAssetModel.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4058398610792221873L;

			@Override
			public void buttonClick(ClickEvent event) {
				messagePanel.removeAllComponents();
				messagePanel.setVisible(false);
				dealerAssetModelId = null;
				getAssetModelForm(dealerAssetModelId);
			}
		});		
		
		navigationPanel.addButton(btnAddAssetModel);	
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<DealerAssetModel>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				messagePanel.removeAllComponents();
				messagePanel.setVisible(false);
				dealerAssetModelId = (Long) selectedItem.getItemProperty("id").getValue();
				getAssetModelForm(dealerAssetModelId);
			}
		});
		
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * @param dealerId
	 */
	public void assignValues(Long dealerId) {
		if (dealerId != null) {
			this.dealerId = dealerId;
			this.dealer =  entityService.getById(Dealer.class, new Long(dealerId));
			pagedTable.setContainerDataSource(getIndexedContainer(null));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<DealerAssetModel> dealerAssetModels) {
		IndexedContainer indexedContainer = new IndexedContainer();		
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (DealerAssetModel dealerAssetModel : dealerAssetModels) {
			AssetModel assetModel = dealerAssetModel.getAssetModel();
			Item item = indexedContainer.addItem(dealerAssetModel.getId());
			item.getItemProperty(ID).setValue(dealerAssetModel.getId());
			item.getItemProperty(CODE).setValue(assetModel.getCode());
			item.getItemProperty(DESC_EN).setValue(assetModel.getDescEn());
			item.getItemProperty(DESC).setValue(assetModel.getDesc());
			item.getItemProperty(ASSET_RANGE).setValue(assetModel.getAssetRange().getDescEn());
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
		columnDefinitions.add(new ColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(ASSET_RANGE, I18N.message("asset.range"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * Display assetModelForm in Windown Popup (Save or Update)
	 * @param dealerAssetModelId
	 */
	public void getAssetModelForm(final Long dealerAssetModelId) {
		this.dealerAssetModelId = dealerAssetModelId;
		final Window winAssetModel = new Window(I18N.message("asset.model"));
		winAssetModel.setModal(true);
		winAssetModel.setResizable(false);
		winAssetModel.setWidth(430, Unit.PIXELS);
		winAssetModel.setHeight(250, Unit.PIXELS);
	    
		VerticalLayout contentLayout = new VerticalLayout(); 
						
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setSpacing(true);
              
        cbxAssetRange = new EntityRefComboBox<>(I18N.message("asset.range"));
        cbxAssetRange.setRestrictions(new BaseRestrictions<>(AssetRange.class));
        cbxAssetRange.renderer();
        cbxAssetRange.setImmediate(true);	
        
        cbxAssetModel = new EntityRefComboBox<>(I18N.message("asset.model"));
		cbxAssetModel.setRestrictions(new BaseRestrictions<>(AssetModel.class));
		cbxAssetModel.setRequired(true);
		cbxAssetModel.setSelectedEntity(null);
		cbxAssetModel.setImmediate(true);
		
		cbxAssetRange.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1812853593330316633L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxAssetRange.getSelectedEntity() != null) {
					reloadAssetModel(cbxAssetRange.getSelectedEntity().getId());
				}						
			}
		});
    	        
        if (dealerAssetModelId != null) {
			dealerAssetModel = entityService.getById(DealerAssetModel.class, dealerAssetModelId);
			AssetModel assetModel = dealerAssetModel.getAssetModel();
			cbxAssetRange.setSelectedEntity(assetModel.getAssetRange());
			cbxAssetModel.setSelectedEntity(dealerAssetModel.getAssetModel());		
		}
   
        formLayout.addComponent(cbxAssetRange);
        formLayout.addComponent(cbxAssetModel);
        
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			private static final long serialVersionUID = -4024064977917270885L;

			public void buttonClick(ClickEvent event) {
				if (validate()) {
					if (dealerAssetModelId == null) {
						dealerAssetModel = new DealerAssetModel();
						dealerAssetModel.setDealer(dealer);
						dealerAssetModel.setAssetModel(cbxAssetModel.getSelectedEntity());
						entityService.saveOrUpdate(dealerAssetModel);
					}
					winAssetModel.close();
					assignValues(dealerId);	
				}
            }
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			private static final long serialVersionUID = 3975121141565713259L;

			public void buttonClick(ClickEvent event) {
            	winAssetModel.close();
            }
        });
		btnCancel.setIcon(FontAwesome.TIMES);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
        contentLayout.addComponent(formLayout);
        
        winAssetModel.setContent(contentLayout);
        UI.getCurrent().addWindow(winAssetModel);
	
	}
	
	/**
	 * @param asranId
	 */
	private void reloadAssetModel(Long asranId) {
		List<Criterion> criterions = new ArrayList<>();
		if (asranId != null) {
			criterions.add(Restrictions.eq("assetRange.id", asranId));
			cbxAssetModel.getRestrictions().setCriterions(criterions);
			cbxAssetModel.renderer();			
		}
	}
	
	/**
	 * Validate Add AssetModel Field Required 
	 * @return
	 */
	public boolean validate() {
		messagePanel.removeAllComponents();
		List<String> errors = new ArrayList<>();
		errors.clear();
		if (cbxAssetModel.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("asset.model") }));
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

}

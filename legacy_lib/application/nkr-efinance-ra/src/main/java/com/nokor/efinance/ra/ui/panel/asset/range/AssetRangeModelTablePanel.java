package com.nokor.efinance.ra.ui.panel.asset.range;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * List all series by model
 * @author uhout.cheng
 */
public class AssetRangeModelTablePanel extends AbstractTabPanel implements FMEntityField, FinServicesHelper, ItemClickListener, SelectedItem, ClickListener {

	/** */
	private static final long serialVersionUID = -2444553135347765451L;

	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<AssetRange> pagedTable;
	
	private Item selectedItem;
	
	private Button btnAdd;
	private Button btnDelete;
	
	private AssetRangeFormPanel delegate;
	
	private Long assetRangeId;
	
	/**
	 * 
	 * @param delegate
	 */
	public AssetRangeModelTablePanel(AssetRangeFormPanel delegate) {
		super();
		this.delegate = delegate;
		setSizeFull();
		setMargin(true);
		setSpacing(true);		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<>(this.columnDefinitions);
		pagedTable.addItemClickListener(this);
		
		btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(this);
		btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(this);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnDelete);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		return contentLayout;
	}
	
	/**
	 * @param assetRangeId
	 */
	public void assignValues(Long assetRangeId) {
		this.selectedItem = null;
		this.assetRangeId = assetRangeId;
		if (assetRangeId != null) {
			AssetRange assetRange = ASS_MODEL_SRV.getById(AssetRange.class, assetRangeId);
			pagedTable.setContainerDataSource(getIndexedContainer(ASS_MODEL_SRV.getAssetModelsByAssetRange(assetRange)));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			this.delegate.addForm(this.assetRangeId, null);
		} else if (event.getButton() == btnDelete) {
			if (this.selectedItem == null) {
				ComponentLayoutFactory.displayErrorMsg("msg.info.delete.item.not.selected");
			} else {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"), new ConfirmDialog.Listener() {
							
						/** */
						private static final long serialVersionUID = 4605195684545338632L;

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								try {
									ASS_MODEL_SRV.delete(ASS_MODEL_SRV.getById(AssetModel.class, getItemSelectedId()));
									dialog.close();
									assignValues(assetRangeId);
								} catch (Exception e) {
									logger.error(e.getMessage());
									ComponentLayoutFactory.displayErrorMsg("msg.warning.delete.selected.item.is.used");
								}
				            }
						}
					});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");
			}
		} 
	}
	
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ID).getValue());
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}

	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			this.delegate.addForm(this.assetRangeId, getItemSelectedId());
		}
	}
	
	/**
	 * Get indexed container
	 * @param assetModels
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<AssetModel> assetModels) {
		IndexedContainer indexedContainer = new IndexedContainer();
		
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (assetModels != null && !assetModels.isEmpty()) {
			for (AssetModel assetModel : assetModels) {
				Item item = indexedContainer.addItem(assetModel.getId());
				item.getItemProperty(ID).setValue(assetModel.getId());
				item.getItemProperty(ASSET_MODEL).setValue(assetModel.getDescLocale());
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
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(ASSET_MODEL, I18N.message("serie.name"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	
}

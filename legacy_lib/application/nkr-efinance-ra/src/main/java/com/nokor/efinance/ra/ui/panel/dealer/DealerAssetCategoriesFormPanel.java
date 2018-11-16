package com.nokor.efinance.ra.ui.panel.dealer;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAssetCategory;
import com.nokor.efinance.core.dealer.service.DealerAssetCategoryRestriction;
import com.nokor.efinance.core.financial.model.CampaignAssetCategory;
import com.nokor.efinance.ra.ui.panel.asset.category.AssetCategorySelectPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Asset category Form Panel
 * @author bunlong.taing
 */
public class DealerAssetCategoriesFormPanel extends AbstractTabPanel implements ClickListener {
	/** */
	private static final long serialVersionUID = -7291687783845766287L;
	
	private Button btnAdd;
	private Button btnDelete;
	
	private SimplePagedTable<AssetCategory> pagedTable;

	private Dealer dealer;
	private Item selectedItem = null;
	
	public DealerAssetCategoriesFormPanel() {
		super();
	}
	
	@Override
	protected Component createForm() {
		btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(this);
		btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(this);
		createPagedTable();
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnDelete);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		return contentLayout;
	}
	
	/**
	 */
	private void createPagedTable() {
		pagedTable = new SimplePagedTable<AssetCategory>(createColumnDefinition());
		pagedTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = 5717232829461304660L;
			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
			}
		});
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(DealerAssetCategory.ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("assetCategory." + AssetCategory.DESCEN, I18N.message("name"), String.class, Align.LEFT, 250));
		return columnDefinitions;
	}
	
	/**
	 * @param dealerAssetCategories
	 */
	@SuppressWarnings("unchecked")
	private void setTableDataSource(List<DealerAssetCategory> dealerAssetCategories) {
		Container container = pagedTable.getContainerDataSource();
		if (dealerAssetCategories != null) {
			for (DealerAssetCategory dealerAssetCategory : dealerAssetCategories) {
				Item item = container.addItem(dealerAssetCategory.getId());
				item.getItemProperty(DealerAssetCategory.ID).setValue(dealerAssetCategory.getId());
				AssetCategory assetCategory = dealerAssetCategory.getAssetCategory();
				if (assetCategory != null) {
					item.getItemProperty("assetCategory." + AssetCategory.DESCEN).setValue(assetCategory.getDescLocale());
				}
			}
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * @param deaId
	 */
	public void assignValues(Dealer dealer) {
		reset();
		if (dealer != null) {
			this.dealer = dealer;
			DealerAssetCategoryRestriction restriction = new DealerAssetCategoryRestriction();
			restriction.setDealerId(dealer.getId());
			setTableDataSource(ENTITY_SRV.list(restriction));
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		dealer = null;
		selectedItem = null;
		pagedTable.removeAllItems();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			selectedItem = null;
			AssetCategorySelectPanel dialog = new AssetCategorySelectPanel();
			dialog.setDealerId(dealer.getId());
			
			dialog.show(new AssetCategorySelectPanel.Listener() {
				/** */
				private static final long serialVersionUID = -5890999882421853005L;				
				@Override
				public void onClose(AssetCategorySelectPanel dialog) {
					List<Long> ids = dialog.getSelectedIds();
					if (ids != null && !ids.isEmpty()) {
						List<DealerAssetCategory> dealerAssetCategories = new ArrayList<>();
						for (Long id : ids) {
							DealerAssetCategory dealerAssetCategory = new DealerAssetCategory();
							dealerAssetCategory.setAssetCategory(ENTITY_SRV.getById(AssetCategory.class, id));
							dealerAssetCategory.setDealer(dealer);
							dealerAssetCategories.add(dealerAssetCategory);
						}
						ENTITY_SRV.saveOrUpdateBulk(dealerAssetCategories);
						assignValues(dealer);						
					}
				}
			});
			dialog.setWidth("780px");
			dialog.setHeight("520px");
		} else if (event.getButton() == btnDelete) {
			delete();
		}
	}
	
	/**
	 * Delete
	 */
	private void delete() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long id = (Long) selectedItem.getItemProperty(CampaignAssetCategory.ID).getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {id.toString()}),
					new ConfirmDialog.Listener() {
				
				/** */
				private static final long serialVersionUID = 8854617190459011252L;
				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
					    logger.debug("[ >> Delete CampaignAssetmake ]");
						ENTITY_SRV.delete(CampaignAssetCategory.class, id);
						logger.debug("CampaignAssetMake id: " + id + " deleted successfully!");
						logger.debug("[ << Delete CampaignAssetmake ]");
					    dialog.close();
						MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
								MessageBox.Icon.INFO, I18N.message("item.deleted.successfully", 
								new String[]{id.toString()}), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
						assignValues(dealer);
						selectedItem = null;
					}
				}
			});
		}
	}

}

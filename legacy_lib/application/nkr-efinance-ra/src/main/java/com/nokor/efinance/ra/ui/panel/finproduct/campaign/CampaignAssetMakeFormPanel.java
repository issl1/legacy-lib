package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.CampaignAssetMake;
import com.nokor.efinance.core.financial.service.CampaignAssetMakeRestriction;
import com.nokor.efinance.ra.ui.panel.asset.make.popup.AssetMakeSelectPanel;
import com.nokor.efinance.ra.ui.panel.asset.make.popup.AssetMakeSelectPanel.SelectListener;
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
 * Campaign Asset Make Form Panel
 * @author bunlong.taing
 */
public class CampaignAssetMakeFormPanel extends AbstractTabPanel implements ClickListener, SelectListener {
	/** */
	private static final long serialVersionUID = -7291687783845766287L;
	
	private Button btnAdd;
	private Button btnDelete;
	
	private SimplePagedTable<CampaignAssetMake> pagedTable;
	private AssetMakeSelectPanel selectPopup;
	private Long campaignId;
	private Item selectedItem = null;
	
	public CampaignAssetMakeFormPanel() {
		super();
	}
	
	@Override
	protected Component createForm() {
		selectPopup = new AssetMakeSelectPanel();
		selectPopup.setSelectListener(this);
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
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		return contentLayout;
	}
	
	/**
	 */
	private void createPagedTable() {
		pagedTable = new SimplePagedTable<CampaignAssetMake>(createColumnDefinition());
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
		columnDefinitions.add(new ColumnDefinition(CampaignAssetMake.ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CampaignAssetMake.ASSETMAKE + "." + AssetMake.ID, I18N.message("brand.id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CampaignAssetMake.ASSETMAKE + "." + AssetMake.DESCEN, I18N.message("brand.name"), String.class, Align.LEFT, 250));
		return columnDefinitions;
	}
	
	/**
	 * @param campaignAssetMakes
	 */
	@SuppressWarnings("unchecked")
	private void setTableDataSource(List<CampaignAssetMake> campaignAssetMakes) {
		Container container = pagedTable.getContainerDataSource();
		if (campaignAssetMakes != null) {
			for (CampaignAssetMake campaignAssetMake : campaignAssetMakes) {
				Item item = container.addItem(campaignAssetMake.getId());
				item.getItemProperty(CampaignAssetMake.ID).setValue(campaignAssetMake.getId());
				AssetMake brand = campaignAssetMake.getAssetMake();
				if (brand != null) {
					item.getItemProperty(CampaignAssetMake.ASSETMAKE + "." + AssetMake.ID).setValue(brand.getId());
					item.getItemProperty(CampaignAssetMake.ASSETMAKE + "." + AssetMake.DESCEN).setValue(brand.getDescLocale());
				}
			}
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * @param campaignId
	 */
	public void assignValues(Long campaignId) {
		reset();
		if (campaignId != null) {
			this.campaignId = campaignId;
			CampaignAssetMakeRestriction restriction = new CampaignAssetMakeRestriction();
			restriction.setCampaignId(campaignId);
			setTableDataSource(ENTITY_SRV.list(restriction));
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		campaignId = null;
		selectedItem = null;
		pagedTable.removeAllItems();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			selectPopup.reset();
			selectPopup.setEntityId(campaignId);
			selectPopup.show();
		} else if (event.getButton() == btnDelete) {
			delete();
		}
	}

	/**
	 * @see com.nokor.efinance.ra.ui.panel.asset.make.popup.AssetMakeSelectPanel.SelectListener#onSelected(java.util.List)
	 */
	@Override
	public void onSelected(List<Long> selectedIds) {
		if (selectedIds != null && !selectedIds.isEmpty()) {
			List<CampaignAssetMake> campaignAssetMakes = new ArrayList<>();
			Campaign campaign = ENTITY_SRV.getById(Campaign.class, campaignId);
			for (Long id : selectedIds) {
				CampaignAssetMake campaignAssetMake = new CampaignAssetMake();
				campaignAssetMake.setAssetMake(ENTITY_SRV.getById(AssetMake.class, id));
				campaignAssetMake.setCampaign(campaign);
				campaignAssetMakes.add(campaignAssetMake);
			}
			ENTITY_SRV.saveOrUpdateBulk(campaignAssetMakes);
			assignValues(campaignId);
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
			final Long id = (Long) selectedItem.getItemProperty(CampaignAssetMake.ID).getValue();
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
						ENTITY_SRV.delete(CampaignAssetMake.class, id);
						logger.debug("CampaignAssetMake id: " + id + " deleted successfully!");
						logger.debug("[ << Delete CampaignAssetmake ]");
					    dialog.close();
						MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
								MessageBox.Icon.INFO, I18N.message("item.deleted.successfully", 
								new String[]{id.toString()}), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
						assignValues(campaignId);
						selectedItem = null;
					}
				}
			});
		}
	}

}

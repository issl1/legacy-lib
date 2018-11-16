package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.CampaignAssetModel;
import com.nokor.efinance.core.financial.service.CampaignAssetModelRestriction;
import com.nokor.efinance.ra.ui.panel.asset.model.popup.AssetModelPopupSelectPanel;
import com.nokor.efinance.ra.ui.panel.asset.model.popup.AssetModelPopupSelectPanel.SelectListener;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class CampaignAssetFormPanel extends AbstractTabPanel implements SelectListener {
	/** */
	private static final long serialVersionUID = -6979887462512677403L;
	
	private Long campaignId;
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<CampaignAssetModel> pagedTable;
	private AssetModelPopupSelectPanel selectPopup;
	private Item selectedItem = null;

	public CampaignAssetFormPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected Component createForm() {
		
		selectPopup = new AssetModelPopupSelectPanel();
		selectPopup.setSelectListener(this);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = -3852951932212784561L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				selectedItem = null;
				selectPopup.reset();
				selectPopup.setEntityId(campaignId);
				selectPopup.show();
			}
		});
			
		NativeButton btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = 2076636025858343601L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				delete();
			}
		});
					
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnDelete);
			
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<CampaignAssetModel>(this.columnDefinitions);

		pagedTable.addItemClickListener(new ItemClickListener() {
	
			/** */
			private static final long serialVersionUID = -7275069449968067446L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
			}
		});
			
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		return contentLayout;
	}
	
	/**
	 * 
	 * @param campaignId
	 */
	public void assignValues(Long campaignId) {
		reset();
		if (campaignId != null) {
			this.campaignId = campaignId;
			CampaignAssetModelRestriction restriction = new CampaignAssetModelRestriction();
			restriction.setCampaignId(campaignId);
			pagedTable.setContainerDataSource(getCampaignAssetIndexedContainer(ENTITY_SRV.list(restriction)));
		}
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(AssetModel.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(AssetModel.ASSETRANGE + "." + AssetRange.ASSETMAKE, I18N.message("asset.make"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(AssetModel.ASSETRANGE, I18N.message("asset.range"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(AssetModel.SERIE, I18N.message("asset.model"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(AssetModel.CC, I18N.message("cc"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CampaignAssetModel.STANDARDFINANCEAMOUNT, I18N.message("standard.finance.amount"), String.class, Align.RIGHT, 130));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param campaign
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getCampaignAssetIndexedContainer(List<CampaignAssetModel> campaignAssetModels) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (!campaignAssetModels.isEmpty()) {
			for (CampaignAssetModel campaignAssetModel : campaignAssetModels) {
				Item item = indexedContainer.addItem(campaignAssetModel.getId());
				
				AssetModel assetModel = campaignAssetModel.getAssetModel();
				if (assetModel != null) {
					
					AssetRange assetRange = assetModel.getAssetRange();
					AssetMake assetMake = null;
					if (assetRange != null) {
						assetMake = assetRange.getAssetMake();
					}
					item.getItemProperty(AssetModel.ID).setValue(campaignAssetModel.getId());
					item.getItemProperty(AssetModel.ASSETRANGE + "." + AssetRange.ASSETMAKE).setValue(assetMake != null ? assetMake.getDescLocale() : "");
					item.getItemProperty(AssetModel.ASSETRANGE).setValue(assetRange != null ? assetRange.getDescLocale() : "");
					item.getItemProperty(AssetModel.SERIE).setValue(assetModel.getSerie());
					item.getItemProperty(AssetModel.CC).setValue(getDefaultString(assetModel.getEngine() != null ? 
							assetModel.getEngine().getDescLocale() : null));
					item.getItemProperty(CampaignAssetModel.STANDARDFINANCEAMOUNT).setValue(MyNumberUtils.formatDoubleToString(
							MyNumberUtils.getDouble(campaignAssetModel.getStandardFinanceAmount()), "###,##0.00"));
				}
			}	
		}
		return indexedContainer;
	}
	
	/**
	 * Reset
	 */
	protected void reset() {
		campaignId = null;
		selectedItem = null;
		pagedTable.removeAllItems();
	}
	
	/**
	 * @see com.nokor.efinance.ra.ui.panel.asset.model.popup.AssetModelPopupSelectPanel.SelectListener#onSelected(java.util.List)
	 */
	@Override
	public void onSelected(Map<Long, Double[]> selectedIds) {
		if (selectedIds != null && !selectedIds.isEmpty()) {
			List<CampaignAssetModel> campaignAssetModels = new ArrayList<>();
			Campaign campaign = ENTITY_SRV.getById(Campaign.class, campaignId);
			for (Iterator<Long> iter = selectedIds.keySet().iterator(); iter.hasNext(); ) {
				long assetModelId = iter.next();
				Double[] amounts = selectedIds.get(assetModelId);
				CampaignAssetModel campaignAssetModel = new CampaignAssetModel();
				campaignAssetModel.setAssetModel(ENTITY_SRV.getById(AssetModel.class, assetModelId));
				campaignAssetModel.setCampaign(campaign);
				campaignAssetModel.setStandardFinanceAmount(amounts[0]);
				campaignAssetModels.add(campaignAssetModel);
			}
			ENTITY_SRV.saveOrUpdateBulk(campaignAssetModels);
			assignValues(campaignId);
		}
	}
	
	/**
	 * 
	 */
	private void delete() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long id = (Long) selectedItem.getItemProperty("id").getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {id.toString()}),
					new ConfirmDialog.Listener() {

				/** */
				private static final long serialVersionUID = -1278300263633872114L;

				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
					    logger.debug("[>> deleteCampaignAssetModel]");
						ENTITY_SRV.delete(CampaignAssetModel.class, id);
						logger.debug("This item " + id + "deleted successfully !");
						logger.debug("[<< deleteCampaignAssetModel]");
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

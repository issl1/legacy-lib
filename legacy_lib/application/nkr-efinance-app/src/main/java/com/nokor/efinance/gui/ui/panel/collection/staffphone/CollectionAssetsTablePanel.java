package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.model.MAsset;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Asset detail table in collection right panel
 * @author uhout.cheng
 */
public class CollectionAssetsTablePanel extends Panel implements FinServicesHelper, MAsset {

	/** */
	private static final long serialVersionUID = 7926497280205349173L;

	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public CollectionAssetsTablePanel() {
		init();
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setWidth(528, Unit.PIXELS);
		simpleTable.setPageLength(1);
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = getSimpleTable(getColumnDefinitions());
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(new VerticalLayout(simpleTable));
		setContent(horLayout);
		setStyleName(Reindeer.PANEL_LIGHT);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(CODE, I18N.message("asset.id"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(ASSETMAKE, I18N.message("asset.make"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(ASSETRANGE, I18N.message("asset.range"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DESCEN, I18N.message("asset.model"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(COLOR, I18N.message("color"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(YEAR, I18N.message("year"), Integer.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TIASSETPRICE, I18N.message("manufacturing.price"), Amount.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param asset
	 */
	@SuppressWarnings("unchecked")
	private void setTableIndexedContainer(Asset asset) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		AssetModel assetModel = null;
		AssetRange assetRange = null;
		AssetMake assetMake = null;
		if (asset != null) {
			Item item = indexedContainer.addItem(asset.getId());
			item.getItemProperty(CODE).setValue(asset.getCode());
			assetModel = asset.getModel();
			if (assetModel != null) {
				assetRange = assetModel.getAssetRange();
				if (assetRange != null) {
					assetMake = assetRange.getAssetMake();
				}
			}
			item.getItemProperty(ASSETMAKE).setValue(assetMake == null ? "" : assetMake.getDescLocale());
			item.getItemProperty(ASSETRANGE).setValue(assetRange == null ? "" : assetRange.getDescLocale());
			item.getItemProperty(DESCEN).setValue(assetModel == null ? "" : assetModel.getDescLocale());
			item.getItemProperty(COLOR).setValue(asset.getColor() != null ? asset.getColor().getDescLocale() : "");
			item.getItemProperty(YEAR).setValue(asset.getYear());
			item.getItemProperty(TIASSETPRICE).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(asset.getTiAssetPrice())));
		}
	}
	
	/**
	 * 
	 * @param asset
	 */
	public void assignValues(Asset asset) {
		setTableIndexedContainer(asset);
	}

}

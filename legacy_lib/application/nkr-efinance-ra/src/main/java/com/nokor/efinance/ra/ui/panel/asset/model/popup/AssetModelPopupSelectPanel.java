package com.nokor.efinance.ra.ui.panel.asset.model.popup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.service.AssetModelRestriction;
import com.nokor.efinance.core.asset.service.AssetRangeRestriction;
import com.nokor.efinance.core.financial.model.CampaignAssetModel;
import com.nokor.efinance.core.financial.model.MCampaignAssetModel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Asset Model Pop up Select Panel
 * @author bunlong.taing
 */
public class AssetModelPopupSelectPanel extends Window implements ClickListener, FinServicesHelper, MCampaignAssetModel {
	/** */
	private static final long serialVersionUID = 6644768325705600497L;
	
	private static final String SELECT = "select";
	
	private Button btnSearch;
	private Button btnReset;
	private Button btnSelect;
	private Button btnApplyAll;
	
	private TextField txtSerie;
	private TextField txtDefaultStandardFinanceAmount;
	
	private EntityComboBox<AssetMake> cbxAssetMake;
	private EntityComboBox<AssetRange> cbxAssetRange;
	private EntityComboBox<AssetCategory> cbxAssetCategory;
	
	private SimpleTable<AssetMake> simpleTable;
	private SelectListener selectListener;
	
	private Long entityId;
	
	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
	/**
	 */
	public AssetModelPopupSelectPanel() {
		setCaption(I18N.message("asset.models"));
		setModal(true);
		setReadOnly(false);
		setWidth(750, Unit.PIXELS);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponent(createSearchPanel());
		content.addComponent(createForm());
		setContent(content);
	}
	
	/**
	 * @return
	 */
	private Component createSearchPanel() {
		Label lblAssetMake = ComponentFactory.getLabel("brand");
		Label lblAssetRange = ComponentFactory.getLabel("asset.range");
		Label lblAssetCategory = ComponentFactory.getLabel("category");
		Label lblSerie = ComponentFactory.getLabel("serie");
		
		txtSerie = ComponentFactory.getTextField(100, 160);
		cbxAssetMake = new EntityComboBox<AssetMake>(AssetMake.class, AssetMake.DESCLOCALE);
		cbxAssetMake.renderer();
		cbxAssetMake.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 1094555666038934180L;
			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxAssetMake.getValue() != null) {
					AssetRangeRestriction restriction = new AssetRangeRestriction();
					restriction.setAssetMake(cbxAssetMake.getSelectedEntity());
					cbxAssetRange.renderer(restriction);
				} else {
					cbxAssetRange.renderer();
				}
			}
		});
		cbxAssetRange = new EntityComboBox<AssetRange>(AssetRange.class, AssetRange.DESCLOCAL);
		cbxAssetRange.renderer();

		cbxAssetCategory = new EntityComboBox<AssetCategory>(AssetCategory.class, AssetCategory.DESCLOCALE);
		cbxAssetCategory.renderer();
		
		btnSearch = ComponentFactory.getButton("search");
		btnSearch.setIcon(FontAwesome.SEARCH);
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setImmediate(true);
		btnSearch.addClickListener(this);
		btnReset = ComponentFactory.getButton("reset");
		btnReset.setIcon(FontAwesome.ERASER);
		btnReset.addClickListener(this);
		
		GridLayout gridLayout = new GridLayout(6, 2);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(lblAssetCategory, 0, 0);
		gridLayout.addComponent(cbxAssetCategory, 1, 0);
		gridLayout.addComponent(lblAssetMake, 2, 0);
		gridLayout.addComponent(cbxAssetMake, 3, 0);
		gridLayout.addComponent(lblAssetRange, 4, 0);
		gridLayout.addComponent(cbxAssetRange, 5, 0);
		gridLayout.addComponent(lblSerie, 0, 1);
		gridLayout.addComponent(txtSerie, 1, 1);
		
		gridLayout.setComponentAlignment(lblAssetCategory, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblAssetMake, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblAssetRange, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblSerie, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(btnSearch);
		horizontalLayout.addComponent(btnReset);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponent(gridLayout);
		content.addComponent(horizontalLayout);
		content.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		
		Panel panel = new Panel(I18N.message("search"));
		panel.setContent(content);
		return panel;
	}
	
	/**
	 * @return
	 */
	private Component createForm() {
		btnSelect = ComponentFactory.getButton("select");
		btnSelect.setIcon(FontAwesome.CHECK_SQUARE_O);
		btnSelect.addClickListener(this);
		
		btnApplyAll = new NativeButton(I18N.message("apply.all"));
		btnApplyAll.addClickListener(this);
		btnApplyAll.setStyleName("btn btn-success button-small");
		btnApplyAll.setWidth("60px");
		
		txtDefaultStandardFinanceAmount = ComponentFactory.getTextField(100, 170);
		Label lblDefaultOffsetFinanceAmount = ComponentFactory.getLabel("default.standard.finance.amount");
		createTable();
		
		GridLayout gridLayout = new GridLayout(4, 1);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(btnSelect, 0, 0);
		gridLayout.addComponent(lblDefaultOffsetFinanceAmount, 1, 0);
		gridLayout.addComponent(txtDefaultStandardFinanceAmount, 2, 0);
		gridLayout.addComponent(btnApplyAll, 3, 0);
		
		gridLayout.setComponentAlignment(lblDefaultOffsetFinanceAmount, Alignment.MIDDLE_LEFT);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(gridLayout);
		content.addComponent(simpleTable);
		return content;
	}
	
	/**
	 */
	private void createTable() {
		simpleTable = new SimpleTable<AssetMake>(createColumnDefinition());
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(SELECT, I18N.message("check"), CheckBox.class, Align.CENTER, 50));
		columnDefinitions.add(new ColumnDefinition(AssetModel.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(AssetModel.ASSETRANGE + "." + AssetRange.ASSETMAKE, I18N.message("asset.make"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(AssetModel.SERIE, I18N.message("asset.model"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(CampaignAssetModel.STANDARDFINANCEAMOUNT, I18N.message("standard.finance.amount"), NumberField.class, Align.RIGHT, 130));
		return columnDefinitions;
	}
	
	/**
	 * @param assets
	 */
	@SuppressWarnings("unchecked")
	private void setTableDataSource(List<AssetModel> assets) {
		if (assets != null) {
			for (AssetModel asset : assets) {
				Item item = simpleTable.addItem(asset.getId());
				AssetRange range = asset.getAssetRange();
				AssetMake make = range != null ? range.getAssetMake() : null;
				CheckBox cbSelect = new CheckBox();
				cbSelect.setValue(true);
				item.getItemProperty(SELECT).setValue(cbSelect);
				item.getItemProperty(AssetModel.ID).setValue(asset.getId());
				item.getItemProperty(AssetModel.ASSETRANGE + "." + AssetRange.ASSETMAKE).setValue(make != null ? make.getDescLocale() : "");
				item.getItemProperty(AssetModel.SERIE).setValue(asset.getSerie());
				NumberField txtStandardFinAmt = ComponentFactory.getNumberField(20, 120);
				txtStandardFinAmt.setValue(txtDefaultStandardFinanceAmount.getValue());
				item.getItemProperty(CampaignAssetModel.STANDARDFINANCEAMOUNT).setValue(txtStandardFinAmt);
			}
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		txtSerie.setValue("");
		cbxAssetMake.setSelectedEntity(null);
		cbxAssetRange.setSelectedEntity(null);
		cbxAssetCategory.setSelectedEntity(null);
		txtDefaultStandardFinanceAmount.setValue("");
		simpleTable.removeAllItems();
		Map<String, AssetMake> assetMakes = cbxAssetMake.getValueMap();
		if (!assetMakes.isEmpty()) {
			cbxAssetMake.setSelectedEntity(assetMakes.values().iterator().next());
		}
	}
	
	/**
	 * Search
	 */
	private void search() {
		simpleTable.removeAllItems();
		setTableDataSource(ENTITY_SRV.list(getRestrictions()));
		txtDefaultStandardFinanceAmount.setValue("");
	}

	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<AssetModel> getRestrictions() {
		String DOT = ".";
		String CAM_ASS_MODEL = "camassmodel";
		
		AssetModelRestriction restrictions = new AssetModelRestriction();
		restrictions.setAssetRange(cbxAssetRange.getSelectedEntity());
		restrictions.setAssetCategory(cbxAssetCategory.getSelectedEntity());
		AssetMake brand = cbxAssetMake.getSelectedEntity();
		restrictions.setBraId(brand != null ? brand.getId() : null);
		restrictions.setSerie(txtSerie.getValue());
		
		if (entityId != null) {
			DetachedCriteria subCriteria = DetachedCriteria.forClass(CampaignAssetModel.class, CAM_ASS_MODEL);
			subCriteria.add(Restrictions.eq(CAM_ASS_MODEL + DOT + CAMPAIGN + DOT + ID, entityId));
			subCriteria.setProjection(Projections.projectionList().add(Projections.property(CAM_ASS_MODEL + DOT + ASSETMODEL + DOT + ID)));
			restrictions.addCriterion(Property.forName(ID).notIn(subCriteria));
		}
		return restrictions;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSearch) {
			search();
		} else if (event.getButton() == btnReset) {
			txtSerie.setValue("");
			cbxAssetMake.setSelectedEntity(null);
		} else if (event.getButton() == btnSelect) {
			select();
		} else if (event.getButton() == btnApplyAll) {
			search();
		}
	}
	
	/**
	 * Show the popup
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
	}
	
	/**
	 */
	private void select() {
		if (selectListener != null) {
			selectListener.onSelected(getSelectedIds());
			close();
		}
	}
	
	private Map<Long, Double[]> getSelectedIds() {
		Map<Long, Double[]> ids = new HashMap<>();
		for (Object i : simpleTable.getItemIds()) {
		    Long id = (Long) i;
		    Item item = simpleTable.getItem(id);
		    CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
		    NumberField txtStandardFinAmt = (NumberField) item.getItemProperty(CampaignAssetModel.STANDARDFINANCEAMOUNT).getValue();
		    double standardFinAmt = MyNumberUtils.getDouble(txtStandardFinAmt.getValue(), 0d);
		    if (cbSelect.getValue()) {
		    	if (!ids.containsKey(id)) {
		    		ids.put(id, new Double[] { standardFinAmt });
		    	}
		    }
		}
		return ids;
	}
	
	/**
	 * @param selectListener
	 */
	public void setSelectListener(SelectListener selectListener) {
		this.selectListener = selectListener;
	}
	
	/**
	 * @author bunlong.taing
	 */
	public interface SelectListener {
		/** */
		void onSelected(Map<Long, Double[]> selectedIds);
	}

}

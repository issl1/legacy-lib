package com.nokor.efinance.ra.ui.panel.insurance.company.detail.price;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.service.AssetModelRestriction;
import com.nokor.efinance.core.asset.service.AssetRangeRestriction;
import com.nokor.efinance.core.financial.model.MCampaignAssetModel;
import com.nokor.efinance.core.financial.model.MInsuranceFinService;
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
public class InsuracePriceLOSPopupSelectPanel extends Window implements ClickListener, FinServicesHelper, MCampaignAssetModel {
	/** */
	private static final long serialVersionUID = 6644768325705600497L;
	
	private static final String SELECT = "select";
	
	private Button btnSearch;
	private Button btnReset;
	private Button btnSelect;
	
	
	private EntityComboBox<AssetMake> cbxAssetMake;
	private EntityComboBox<AssetRange> cbxAssetRange;
	
	private TextField txtAssetSerial;
	
	private SimpleTable<AssetMake> simpleTable;
	private SelectListener selectListener;
	
	/**
	 */
	public InsuracePriceLOSPopupSelectPanel() {
		setCaption(I18N.message("lost"));
		setModal(true);
		setReadOnly(false);
		setWidth(1125, Unit.PIXELS);
		
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
		Label lblSerie = ComponentFactory.getLabel("serie");
		
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
		
		btnSearch = ComponentFactory.getButton("search");
		btnSearch.setIcon(FontAwesome.SEARCH);
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setImmediate(true);
		btnSearch.addClickListener(this);
		btnReset = ComponentFactory.getButton("reset");
		btnReset.setIcon(FontAwesome.ERASER);
		btnReset.addClickListener(this);
		
		txtAssetSerial = ComponentFactory.getTextField(40, 120);
		
		GridLayout gridLayout = new GridLayout(6, 2);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(lblAssetMake, 0, 0);
		gridLayout.addComponent(cbxAssetMake, 1, 0);
		gridLayout.addComponent(lblAssetRange, 2, 0);
		gridLayout.addComponent(cbxAssetRange, 3, 0);
		gridLayout.addComponent(lblSerie, 4, 0);
		gridLayout.addComponent(txtAssetSerial, 5, 0);
		
		gridLayout.setComponentAlignment(lblAssetMake, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblAssetRange, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblSerie, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(btnSearch);
		buttonLayout.addComponent(btnReset);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponent(gridLayout);
		content.addComponent(buttonLayout);
		content.setComponentAlignment(gridLayout, Alignment.TOP_CENTER);
		content.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		
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
		
		createTable();
		
		GridLayout gridLayout = new GridLayout(4, 1);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(btnSelect, 0, 0);
		
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
		columnDefinitions.add(new ColumnDefinition(AssetModel.MODEL, I18N.message("asset.range"), String.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition(AssetModel.SERIE, I18N.message("asset.model"), String.class, Align.LEFT, 120));
		
		columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.PREMIUM_FIRST_YEAR, I18N.message("premium.1y"), TextField.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.CLAIM_AMOUNT_FIRST_YEAR, I18N.message("sum.insured.1y"), TextField.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.PREMIUM_SECOND_YEAR, I18N.message("premium.2y"), TextField.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.CLAIM_AMOUNT_2Y_FIRST_YEAR, I18N.message("sum.insured.2y.first.year"), TextField.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.CLAIM_AMOUNT_2Y_SECOND_YEAR, I18N.message("sum.insured.2y.second.year"), TextField.class, Align.LEFT, 150));
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
				item.getItemProperty(AssetModel.MODEL).setValue(asset.getAssetRange()!= null ? asset.getAssetRange().getDescLocale() : "");
				item.getItemProperty(AssetModel.SERIE).setValue(asset.getSerie());
				
				NumberField txtPremiumFirstYear = ComponentFactory.getNumberField(20, 60);
				NumberField txtClaimAmountFirstYear = ComponentFactory.getNumberField(20, 60);
				NumberField txtPremiumSecondYear = ComponentFactory.getNumberField(20, 60);
				NumberField txtClaimAmount2YFirstYear = ComponentFactory.getNumberField(20, 60);
				NumberField txtClaimAmount2YSecondYear = ComponentFactory.getNumberField(20, 60);
				
				item.getItemProperty(MInsuranceFinService.PREMIUM_FIRST_YEAR).setValue(txtPremiumFirstYear);
				item.getItemProperty(MInsuranceFinService.CLAIM_AMOUNT_FIRST_YEAR).setValue(txtClaimAmountFirstYear);
				item.getItemProperty(MInsuranceFinService.PREMIUM_SECOND_YEAR).setValue(txtPremiumSecondYear);
				item.getItemProperty(MInsuranceFinService.CLAIM_AMOUNT_2Y_FIRST_YEAR).setValue(txtClaimAmount2YFirstYear);
				item.getItemProperty(MInsuranceFinService.CLAIM_AMOUNT_2Y_SECOND_YEAR).setValue(txtClaimAmount2YSecondYear);
			}
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		cbxAssetMake.setSelectedEntity(null);
		cbxAssetRange.setSelectedEntity(null);
		txtAssetSerial.setValue("");
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
	}

	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<AssetModel> getRestrictions() {
		AssetModelRestriction restrictions = new AssetModelRestriction();
		restrictions.setAssetRange(cbxAssetRange.getSelectedEntity());
		AssetMake brand = cbxAssetMake.getSelectedEntity();
		restrictions.setBraId(brand != null ? brand.getId() : null);
		
		if (!StringUtils.isEmpty(txtAssetSerial.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("serie", txtAssetSerial.getValue(), MatchMode.ANYWHERE));
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
			cbxAssetMake.setSelectedEntity(null);
			txtAssetSerial.setValue("");
		} else if (event.getButton() == btnSelect) {
			select();
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
		
		    NumberField txtPremiumFirstYear = (NumberField) item.getItemProperty(MInsuranceFinService.PREMIUM_FIRST_YEAR).getValue();
		    NumberField txtClaimAmountFirstYear = (NumberField) item.getItemProperty(MInsuranceFinService.CLAIM_AMOUNT_FIRST_YEAR).getValue();
		    NumberField txtPremiumSecondYear = (NumberField) item.getItemProperty(MInsuranceFinService.PREMIUM_SECOND_YEAR).getValue();
			NumberField txtClaimAmount2YFirstYear = (NumberField) item.getItemProperty(MInsuranceFinService.CLAIM_AMOUNT_2Y_FIRST_YEAR).getValue();
			NumberField txtClaimAmount2YSecondYear = (NumberField) item.getItemProperty(MInsuranceFinService.CLAIM_AMOUNT_2Y_SECOND_YEAR).getValue();
		   
			double premiumFirstYear = MyNumberUtils.getDouble(txtPremiumFirstYear.getValue(), 0d);
			double claimAmountFirstYear = MyNumberUtils.getDouble(txtClaimAmountFirstYear.getValue(), 0d);
			double premiumSecondYear = MyNumberUtils.getDouble(txtPremiumSecondYear.getValue(), 0d);	
			double claimAmount2YFirstYear = MyNumberUtils.getDouble(txtClaimAmount2YFirstYear.getValue(), 0d);
			double claimAmount2YSecondYear = MyNumberUtils.getDouble(txtClaimAmount2YSecondYear.getValue(), 0d);
		    
			if (cbSelect.getValue()) {
		    	if (!ids.containsKey(id)) {
		    		ids.put(id, new Double[] {premiumFirstYear, claimAmountFirstYear, premiumSecondYear, claimAmount2YFirstYear, claimAmount2YSecondYear});
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

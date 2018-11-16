package com.nokor.efinance.ra.ui.panel.asset.model;

import java.util.Map;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.service.AssetModelRestriction;
import com.nokor.efinance.core.asset.service.AssetRangeRestriction;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;


/**
 * 
 * @author uhout.cheng
 */
public class AssetModelSearchPanel extends AbstractSearchPanel<AssetModel> implements AssetEntityField {

	/** */
	private static final long serialVersionUID = -4478589367053917211L;
	
	private TextField txtCode;
	private TextField txtDesc;
	private EntityComboBox<AssetMake> cbxAssetMake;
	private EntityComboBox<AssetRange> cbxAssetRange;
	private EntityComboBox<AssetCategory> cbxAssetCategory;
	private StatusRecordField statusRecordField;
	
	private Map<String, AssetMake> assetMakes;
	
	/**
	 * 
	 * @param assetModelTablePanel
	 */
	public AssetModelSearchPanel(AssetModelTablePanel assetModelTablePanel) {
		super(I18N.message("search"), assetModelTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDesc.setValue("");
		cbxAssetCategory.setSelectedEntity(null);
		cbxAssetRange.setSelectedEntity(null);
		statusRecordField.clearValues();
		if (assetMakes != null && !assetMakes.isEmpty()) {
			cbxAssetMake.setSelectedEntity(assetMakes.values().iterator().next());
		}
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	private FormLayout getFormLayout(Component component) {
		FormLayout formLayout = new FormLayout();
		formLayout.setStyleName("myform-align-left");
		formLayout.addComponent(component);
		return formLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(5, 2);
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 170);        
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 170);
		
		cbxAssetCategory = new EntityComboBox<AssetCategory>(AssetCategory.class, AssetCategory.DESCLOCALE);
		cbxAssetCategory.setCaption(I18N.message("asset.category"));
		cbxAssetCategory.renderer();
		cbxAssetCategory.setWidth(170, Unit.PIXELS);
		
		cbxAssetMake = new EntityComboBox<AssetMake>(AssetMake.class, AssetMake.DESCLOCALE);
		cbxAssetMake.setCaption(I18N.message("asset.make"));
		cbxAssetMake.renderer();
		cbxAssetMake.setWidth(170, Unit.PIXELS);
		
		assetMakes = cbxAssetMake.getValueMap();
		if (assetMakes != null && !assetMakes.isEmpty()) {
			cbxAssetMake.setSelectedEntity(assetMakes.values().iterator().next());
		}
		
		cbxAssetMake.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -5975281317462648880L;

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
		cbxAssetRange.setCaption(I18N.message("asset.range"));
		cbxAssetRange.renderer();
		cbxAssetRange.setWidth(170, Unit.PIXELS);
		
		statusRecordField = new StatusRecordField();
        
        gridLayout.setSpacing(true);
        gridLayout.addComponent(getFormLayout(txtDesc), 0, 0);
        gridLayout.addComponent(getFormLayout(cbxAssetMake), 1, 0);
        gridLayout.addComponent(getFormLayout(cbxAssetRange), 3, 0);
        gridLayout.addComponent(getFormLayout(txtCode), 0, 1);
        gridLayout.addComponent(getFormLayout(cbxAssetCategory), 1, 1);
        gridLayout.addComponent(getFormLayout(statusRecordField), 3, 1);
        
		return gridLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<AssetModel> getRestrictions() {
		
		AssetModelRestriction restrictions = new AssetModelRestriction();
		restrictions.setAssetRange(cbxAssetRange.getSelectedEntity());
		AssetMake brand = cbxAssetMake.getSelectedEntity();
		restrictions.setBraId(brand != null ? brand.getId() : null);
		restrictions.setCode(txtCode.getValue());
		restrictions.setDesc(txtDesc.getValue());
		restrictions.setAssetCategory(cbxAssetCategory.getSelectedEntity());
			
		if (statusRecordField.isInactiveAllValues()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		} 
		if (statusRecordField.getActiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		}
		if (statusRecordField.getInactiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		}
		
		restrictions.addOrder(Order.asc(DESC));
		
		return restrictions;		
		
	}

}

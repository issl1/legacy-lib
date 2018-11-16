package com.nokor.efinance.ra.ui.panel.asset.range;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author youhort.ly
 *
 */
public class AssetRangeSearchPanel extends AbstractSearchPanel<AssetRange> implements AssetEntityField {

	/** */
	private static final long serialVersionUID = -237220972034486757L;
	
	private EntityRefComboBox<AssetMake> cbxAssetMake;
	private TextField txtCode;
	private TextField txtDescEn;
	private StatusRecordField statusRecordField;
	
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
	 * 
	 * @param assetRangeTablePanel
	 */
	public AssetRangeSearchPanel(AssetRangeTablePanel assetRangeTablePanel) {
		super(I18N.message("search"), assetRangeTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		cbxAssetMake.setSelectedEntity(null);
		statusRecordField.clearValues();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(5, 1);
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 170);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 170);
        
        cbxAssetMake = new EntityRefComboBox<AssetMake>(I18N.message("asset.make"));
        cbxAssetMake.setRestrictions(new BaseRestrictions<AssetMake>(AssetMake.class));
        cbxAssetMake.renderer();
        cbxAssetMake.setWidth("170px");
        
        statusRecordField = new StatusRecordField();
        
        gridLayout.setSpacing(true);
        gridLayout.addComponent(getFormLayout(txtCode), 0, 0);
        gridLayout.addComponent(getFormLayout(txtDescEn), 1, 0);
        gridLayout.addComponent(getFormLayout(cbxAssetMake), 2, 0);
        gridLayout.addComponent(getFormLayout(statusRecordField), 3, 0);
        
		return gridLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<AssetRange> getRestrictions() {
		
		BaseRestrictions<AssetRange> restrictions = new BaseRestrictions<>(AssetRange.class);
		
		if (cbxAssetMake.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(ASSET_MAKE + "." + ID, cbxAssetMake.getSelectedEntity().getId()));
		}
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
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
		
		return restrictions;	
	}

}

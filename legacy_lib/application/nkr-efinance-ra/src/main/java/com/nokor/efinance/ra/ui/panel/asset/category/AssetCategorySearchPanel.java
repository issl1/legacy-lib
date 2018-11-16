package com.nokor.efinance.ra.ui.panel.asset.category;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.service.AssetCategoryRestriction;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author uhout.cheng
 */
public class AssetCategorySearchPanel extends AbstractSearchPanel<AssetCategory> implements AssetEntityField {

	/** */
	private static final long serialVersionUID = -4160384210454030290L;

	private TextField txtDescEn;
	private StatusRecordField statusRecordField;
	
	/**
	 * 
	 * @param assetCategoryTablePanel
	 */
	public AssetCategorySearchPanel(AssetCategoryTablePanel assetCategoryTablePanel) {
		super(I18N.message("search"), assetCategoryTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtDescEn.setValue("");
		statusRecordField.clearValues();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		GridLayout gridLayout = new GridLayout(8, 1);
		gridLayout.setSpacing(true);
		
		txtDescEn = ComponentFactory.getTextField(null, false, 60, 180);
		
		statusRecordField = new StatusRecordField();   
        
        
        int iCol = 0;
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("name"), iCol++, 0);
        gridLayout.addComponent(txtDescEn, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(statusRecordField, iCol++, 0);
		return gridLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<AssetCategory> getRestrictions() {
		AssetCategoryRestriction restrictions = new AssetCategoryRestriction();
		restrictions.setSearchText(txtDescEn.getValue());
		
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

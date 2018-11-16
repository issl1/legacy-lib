package com.nokor.efinance.ra.ui.panel.asset.partstatus;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.asset.model.AssetPartsStatus;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * Asset part status search panel
 * @author uhout.cheng
 */
public class AssetPartStatusSearchPanel extends AbstractSearchPanel<AssetPartsStatus> implements AssetEntityField {
	
	/** */
	private static final long serialVersionUID = -4289757675791161932L;
	
	private CheckBox cbActive;
	private CheckBox cbInactive;
	private TextField txtDescEn;
	
	/**
	 * 
	 * @param assetPartsStatusTablePanel
	 */
	public AssetPartStatusSearchPanel(AssetPartStatusTablePanel assetPartsStatusTablePanel) {
		super(I18N.message("search"), assetPartsStatusTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtDescEn.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(3, 1);
		      
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
		
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        cbInactive = new CheckBox(I18N.message("inactive"));
        cbInactive.setValue(false);
        
        int iCol = 0;
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtDescEn), iCol++, 0);
        gridLayout.addComponent(new FormLayout(cbActive), iCol++, 0);
        gridLayout.addComponent(new FormLayout(cbInactive), iCol++, 0);
        
		return gridLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<AssetPartsStatus> getRestrictions() {
		
		BaseRestrictions<AssetPartsStatus> restrictions = new BaseRestrictions<>(AssetPartsStatus.class);
		
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		if (!cbActive.getValue() && !cbInactive.getValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		} 
		if (cbActive.getValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		}
		if (cbInactive.getValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		}
		
		return restrictions;	
	}

}

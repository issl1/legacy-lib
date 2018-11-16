package com.nokor.efinance.ra.ui.panel.insurance.subsidy;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.financial.model.ManufacturerSubsidy;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class SubsidySearchPanel extends AbstractSearchPanel<ManufacturerSubsidy> implements FMEntityField {
	
	/** */
	private static final long serialVersionUID = 4499980661244122424L;
	
	private EntityComboBox<AssetMake> cbxAssetBrand;
	
	/**
	 * 
	 * @param subsidyTablePanel
	 */
	public SubsidySearchPanel(SubsidyTablePanel subsidyTablePanel) {
		super(I18N.message("search"), subsidyTablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxAssetBrand.setSelectedEntity(null);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxAssetBrand = new EntityComboBox<>(AssetMake.class, AssetMake.DESCLOCALE);
		cbxAssetBrand.setCaption(I18N.message("brand"));
		cbxAssetBrand.setWidth(170, Unit.PIXELS);
		cbxAssetBrand.renderer();
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxAssetBrand);
		
		return formLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<ManufacturerSubsidy> getRestrictions() {
		BaseRestrictions<ManufacturerSubsidy> restrictions = new BaseRestrictions<>(ManufacturerSubsidy.class);	
		if (cbxAssetBrand.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(ASSET_MAKE + "." + ID, cbxAssetBrand.getSelectedEntity().getId()));
		}
		return restrictions;
	}

}

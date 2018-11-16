package com.nokor.efinance.ra.ui.panel.insurance.compensation.reposession;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.financial.model.ManufacturerCompensation;
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
public class CompensationReposessionSearchPanel extends AbstractSearchPanel<ManufacturerCompensation> implements FMEntityField{

	/** */
	private static final long serialVersionUID = 9102523689884435905L;
	
	private EntityComboBox<AssetMake> cbxAssetBrand;
	
	public CompensationReposessionSearchPanel(CompensationReposessionTablePanel compensationReposessionTablePanel) {
			super(I18N.message("search"), compensationReposessionTablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxAssetBrand.setValue(null);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxAssetBrand = new EntityComboBox<>(AssetMake.class, AssetMake.DESCEN);
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
	public BaseRestrictions<ManufacturerCompensation> getRestrictions() {
		BaseRestrictions<ManufacturerCompensation> restrictions = new BaseRestrictions<>(ManufacturerCompensation.class);	
		if (cbxAssetBrand.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(ASSET_MAKE + "." + ID, cbxAssetBrand.getSelectedEntity().getId()));
		}
		return restrictions;
	}
			

}

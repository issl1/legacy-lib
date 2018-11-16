package com.nokor.efinance.ra.ui.panel.finproduct.minimum.interest.list;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.financial.model.MinimumInterest;
import com.nokor.efinance.core.financial.model.Term;
import com.nokor.efinance.core.financial.service.MinimumInterestRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

/**
 * Minimum Interest Search Panel
 * @author bunlong.taing
 */
public class MinimumInterestSearchPanel extends AbstractSearchPanel<MinimumInterest> {
	/** */
	private static final long serialVersionUID = 1529774334842305923L;
	
	private EntityComboBox<AssetCategory> cbxAssetCategory;
	private EntityComboBox<Term> cbxTerm;

	/**
	 * @param tablePanel
	 */
	public MinimumInterestSearchPanel(MinimumInterestTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		Label lblAssetCategory = ComponentFactory.getLabel("asset.category");
		Label lblTerm = ComponentFactory.getLabel("term");
		cbxAssetCategory = new EntityComboBox<AssetCategory>(AssetCategory.class, AssetCategory.DESCLOCALE);
		cbxAssetCategory.renderer();
		cbxTerm = new EntityComboBox<Term>(Term.class, Term.DESCLOCALE);
		cbxTerm.renderer();
		
		GridLayout content = new GridLayout(4, 1);
		content.setSpacing(true);
		content.addComponent(lblAssetCategory);
		content.addComponent(cbxAssetCategory);
		content.addComponent(lblTerm);
		content.addComponent(cbxTerm);
		
		content.setComponentAlignment(lblAssetCategory, Alignment.MIDDLE_LEFT);
		content.setComponentAlignment(lblTerm, Alignment.MIDDLE_LEFT);
		
		return content;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<MinimumInterest> getRestrictions() {
		MinimumInterestRestriction restriction = new MinimumInterestRestriction();
		restriction.setAssetCategory(cbxAssetCategory.getSelectedEntity());
		restriction.setTerm(cbxTerm.getSelectedEntity());
		return restriction;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxAssetCategory.setSelectedEntity(null);
		cbxTerm.setSelectedEntity(null);
	}
	
}

package com.nokor.efinance.ra.ui.panel.finproduct.minimum.interest.detail;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.financial.model.MinimumInterest;
import com.nokor.efinance.core.financial.model.Term;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Minimum Interest Form Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MinimumInterestFormPanel extends AbstractFormPanel {
	/** */
	private static final long serialVersionUID = -1840740373068151218L;
	
	private EntityComboBox<AssetCategory> cbxAssetCategory;
	private EntityComboBox<Term> cbxTerm;
	private TextField txtMinimumInterestAmount;
	
	private MinimumInterest minInterest;
	
	/**
	 * Post Construct
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init();
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxAssetCategory = new EntityComboBox<AssetCategory>(AssetCategory.class, AssetCategory.DESCLOCALE);
		cbxAssetCategory.setCaption(I18N.message("asset.category"));
		cbxAssetCategory.renderer();
		cbxTerm = new EntityComboBox<Term>(Term.class, Term.DESCLOCALE);
		cbxTerm.setCaption(I18N.message("term"));
		cbxTerm.renderer();
		txtMinimumInterestAmount = ComponentFactory.getTextField("minimum.interest.amount", false, 100, 200);
		
		FormLayout formLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		formLayout.addComponent(cbxAssetCategory);
		formLayout.addComponent(cbxTerm);
		formLayout.addComponent(txtMinimumInterestAmount);
		
		return formLayout;
	}
	
	/**
	 * Assign Values to controls
	 * @param minimumInterestId
	 */
	public void assignValues(Long minimumInterestId) {
		reset();
		if (minimumInterestId != null) {
			minInterest = ENTITY_SRV.getById(MinimumInterest.class, minimumInterestId);
			cbxAssetCategory.setSelectedEntity(minInterest.getAssetCategory());
			cbxTerm.setSelectedEntity(minInterest.getTerm());
			txtMinimumInterestAmount.setValue(AmountUtils.format(minInterest.getMinimumInterestAmount()));
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		minInterest = new MinimumInterest();
		cbxAssetCategory.setSelectedEntity(null);
		cbxTerm.setSelectedEntity(null);
		txtMinimumInterestAmount.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkDoubleField(txtMinimumInterestAmount, "minimum.interest.amount");
		if (cbxAssetCategory.getSelectedEntity() == null && cbxTerm.getSelectedEntity() == null && StringUtils.isEmpty(txtMinimumInterestAmount.getValue())) {
			this.errors.add(I18N.message("please.fill.data"));
		}
		return errors.isEmpty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		minInterest.setAssetCategory(cbxAssetCategory.getSelectedEntity());
		minInterest.setTerm(cbxTerm.getSelectedEntity());
		minInterest.setMinimumInterestAmount(getDouble(txtMinimumInterestAmount));
		return minInterest;
	}

}

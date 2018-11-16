package com.nokor.efinance.ra.ui.panel.referential.matrixPrice.service;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.financial.model.AssetMatrixPrice;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceMatrixPriceFormPanel extends AbstractFormPanel {
	
	private static final long serialVersionUID = -5315874705514118919L;
	
	private AssetMatrixPrice matrixPrice;
	private EntityRefComboBox<AssetModel> cbxAssetModel;
	private EntityRefComboBox<FinService> cbxService;
	private TextField txtTiPriceUsd;
	private AutoDateField dfDate;
	  
    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("matrix.price.create"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		matrixPrice.setAssetModel(cbxAssetModel.getSelectedEntity());
		matrixPrice.setService(cbxService.getSelectedEntity());
		matrixPrice.setTiPriceUsd(getDouble(txtTiPriceUsd));
		matrixPrice.setTePriceUsd(matrixPrice.getTiPriceUsd());
		matrixPrice.setVatPriceUsd(0d);
		matrixPrice.setDate(dfDate.getValue());
		
		return matrixPrice;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		
		final FormLayout formPanel = new FormLayout();	
		
		cbxAssetModel = new EntityRefComboBox<AssetModel>(I18N.message("asset.model"));
		cbxAssetModel.setRestrictions(new BaseRestrictions<AssetModel>(AssetModel.class));
		cbxAssetModel.renderer();
		
		cbxService = new EntityRefComboBox<FinService>(I18N.message("service"));
		cbxService.setRestrictions(new BaseRestrictions<FinService>(FinService.class));
		cbxService.renderer();
		
		txtTiPriceUsd = ComponentFactory.getTextField(I18N.message("asset.price"), true, 60, 200);	
		txtTiPriceUsd.setRequired(true);
		
		dfDate = ComponentFactory.getAutoDateField(I18N.message("date"), false);
		dfDate.setRequired(true);
		             
        formPanel.addComponent(cbxAssetModel);
        formPanel.addComponent(cbxService);
        formPanel.addComponent(txtTiPriceUsd);
        formPanel.addComponent(dfDate);        
		return formPanel;
	}

	/**
	 * @param assetModelPriceId
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			matrixPrice = ENTITY_SRV.getById(AssetMatrixPrice.class, id);
			cbxAssetModel.setSelectedEntity(matrixPrice.getAssetModel());
			cbxService.setSelectedEntity(matrixPrice.getService());
			txtTiPriceUsd.setValue(AmountUtils.format(matrixPrice.getTiPriceUsd()));
			dfDate.setValue(matrixPrice.getDate());
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		matrixPrice = new AssetMatrixPrice();
		cbxAssetModel.setSelectedEntity(null);
		cbxService.setSelectedEntity(null);
		txtTiPriceUsd.setValue("");
		dfDate.setValue(null);
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatorySelectField(cbxAssetModel, "asset.model");
		checkMandatorySelectField(cbxService, "service");
		checkMandatoryField(txtTiPriceUsd, "price.usd");
		checkMandatoryDateField(dfDate, "date");
		return errors.isEmpty();
	}
}

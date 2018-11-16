package com.nokor.efinance.ra.ui.panel.referential.matrixPrice.assetmodel;

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
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetModelMatrixPriceFormPanel extends AbstractFormPanel {

	private static final long serialVersionUID = -2041340000460817816L;

	public static final String NAME = "2041340000460817816L";
	
	private AssetMatrixPrice matrixPrice;
	private DealerComboBox cbxDealer;
	private EntityRefComboBox<AssetModel> cbxAssetModel;
	private ERefDataComboBox<EColor> cbxColor;
	private TextField txtAssetYear;
	private TextField txtTiPriceUsd;
	private AutoDateField dfDate;
	  
    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("asset.model.create"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		matrixPrice.setAssetModel(cbxAssetModel.getSelectedEntity());
		matrixPrice.setDealer(cbxDealer.getSelectedEntity());
		matrixPrice.setColor(cbxColor.getSelectedEntity());
		matrixPrice.setYear(getInteger(txtAssetYear));
		matrixPrice.setTiPriceUsd(getDouble(txtTiPriceUsd));
		matrixPrice.setTePriceUsd(matrixPrice.getTiPriceUsd());
		matrixPrice.setVatPriceUsd(0d);
		matrixPrice.setDate(dfDate.getValue());
		
		return matrixPrice;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		
		final FormLayout formPanel = new FormLayout();	
		
		cbxDealer = new DealerComboBox(I18N.message("dealer"),DataReference.getInstance().getDealers());
		
		cbxAssetModel = new EntityRefComboBox<AssetModel>(I18N.message("asset.model"));
		cbxAssetModel.setRestrictions(new BaseRestrictions<AssetModel>(AssetModel.class));
		cbxAssetModel.renderer();
		
		cbxColor = new ERefDataComboBox<EColor>(I18N.message("color"), EColor.class);
		
		txtAssetYear = ComponentFactory.getTextField("year", true, 4, 50);
		
		txtTiPriceUsd = ComponentFactory.getTextField(I18N.message("asset.price"), true, 60, 200);	
		txtTiPriceUsd.setRequired(true);
		
		dfDate = ComponentFactory.getAutoDateField(I18N.message("date"), false);
		dfDate.setRequired(true);
		             
        formPanel.addComponent(cbxDealer);
        formPanel.addComponent(cbxAssetModel);
        formPanel.addComponent(cbxColor);
        formPanel.addComponent(txtAssetYear);
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
			cbxDealer.setSelectedEntity(matrixPrice.getDealer());
			cbxAssetModel.setSelectedEntity(matrixPrice.getAssetModel());
			cbxColor.setSelectedEntity(matrixPrice.getColor());
			txtAssetYear.setValue(getDefaultString(matrixPrice.getYear()));
			txtTiPriceUsd.setValue(AmountUtils.format(matrixPrice.getTiPriceUsd()));
			dfDate.setValue(matrixPrice.getDate());
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		matrixPrice = new AssetMatrixPrice();
		cbxDealer.setSelectedEntity(null);
		cbxAssetModel.setSelectedEntity(null);
		cbxColor.setSelectedEntity(null);
		txtAssetYear.setValue("");
		txtTiPriceUsd.setValue("");
		dfDate.setValue(null);
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatorySelectField(cbxAssetModel, "asset.model");
		checkMandatorySelectField(cbxDealer, "dealer");
		checkMandatoryField(txtTiPriceUsd, "price.usd");
		checkMandatoryDateField(dfDate, "date");
		return errors.isEmpty();
	}
}

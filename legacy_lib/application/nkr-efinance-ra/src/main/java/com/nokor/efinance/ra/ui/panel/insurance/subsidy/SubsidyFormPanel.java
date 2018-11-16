package com.nokor.efinance.ra.ui.panel.insurance.subsidy;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.service.AssetModelRestriction;
import com.nokor.efinance.core.financial.model.ManufacturerSubsidy;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubsidyFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 4085001844573980291L;

	private EntityComboBox<AssetMake> cbxAssetBrand;
	private EntityComboBox<AssetModel> cbxAssetSerie;
	private AutoDateField dfEndDate;
	private AutoDateField dfStartDate;
	private TextField txtSubsidyAmount;
	private TextField txtMonthFrom;
	private TextField txtMonthTo;
	
	private ManufacturerSubsidy manufacturerSubsidy;
	
	public SubsidyFormPanel() {
		super.init();
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	/** 
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		cbxAssetBrand = new EntityComboBox<>(AssetMake.class, AssetMake.DESCLOCALE);
		cbxAssetBrand.setCaption(I18N.message("brand"));
		cbxAssetBrand.setRequired(true);
		cbxAssetBrand.setWidth(170, Unit.PIXELS);
		cbxAssetBrand.renderer();
		
		cbxAssetSerie = new EntityComboBox<>(AssetModel.class, AssetMake.DESCLOCALE);
		cbxAssetSerie.setCaption(I18N.message("serie"));
		cbxAssetSerie.setRequired(true);
		cbxAssetSerie.setWidth(170, Unit.PIXELS);
		
		cbxAssetBrand.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 606211773743909893L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxAssetBrand.getSelectedEntity() != null) {
					AssetModelRestriction restrictions = new AssetModelRestriction();
					restrictions.setBraId(cbxAssetBrand.getSelectedEntity().getId());
					cbxAssetSerie.renderer(restrictions);
				}
			}
		});
		
		dfEndDate = ComponentFactory.getAutoDateField(I18N.message("end.date"), false);
		dfEndDate.setWidth(170, Unit.PIXELS);
		dfStartDate = ComponentFactory.getAutoDateField(I18N.message("start.date"), false);
		dfStartDate.setWidth(170, Unit.PIXELS);
		
		txtSubsidyAmount = ComponentFactory.getTextField(I18N.message("subsidy.amount"), false, 100, 170);
		txtMonthFrom = ComponentFactory.getTextField(I18N.message("from.month"), false, 100, 170);
		txtMonthTo = ComponentFactory.getTextField(I18N.message("to.month"), false, 100, 170);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxAssetBrand);
		formLayout.addComponent(cbxAssetSerie);
		formLayout.addComponent(txtMonthFrom);
		formLayout.addComponent(txtMonthTo);
		formLayout.addComponent(txtSubsidyAmount);
		formLayout.addComponent(dfStartDate);
		formLayout.addComponent(dfEndDate);
		
		return formLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		if (validate()) {
			manufacturerSubsidy.setAssetMake(cbxAssetBrand.getSelectedEntity());
			manufacturerSubsidy.setAssetModel(cbxAssetSerie.getSelectedEntity());
			manufacturerSubsidy.setStartDate(dfStartDate.getValue());
			manufacturerSubsidy.setEndDate(dfEndDate.getValue());
			manufacturerSubsidy.setSubsidyAmount(getDouble(txtSubsidyAmount));
			manufacturerSubsidy.setMonthFrom(getInteger(txtMonthFrom));
			manufacturerSubsidy.setMonthTo(getInteger(txtMonthTo));
		}
		return manufacturerSubsidy;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		manufacturerSubsidy = ManufacturerSubsidy.createInstance();
		cbxAssetBrand.setValue(null);
		cbxAssetSerie.setValue(null);
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		txtSubsidyAmount.setValue("");
		txtMonthFrom.setValue("");
		txtMonthTo.setValue("");
	}
	
	/**
	 * 
	 * @param subId
	 */
	public void assignValues(Long subId) {
		super.reset();
		if (subId != null) {
			manufacturerSubsidy = ENTITY_SRV.getById(ManufacturerSubsidy.class, subId);
			cbxAssetBrand.setSelectedEntity(manufacturerSubsidy.getAssetMake());
			cbxAssetSerie.setSelectedEntity(manufacturerSubsidy.getAssetModel());
			dfStartDate.setValue(manufacturerSubsidy.getStartDate());
			dfEndDate.setValue(manufacturerSubsidy.getEndDate());
			txtSubsidyAmount.setValue(MyNumberUtils.formatDoubleToString(
					MyNumberUtils.getDouble(manufacturerSubsidy.getSubsidyAmount()), "##0.00"));
			txtMonthFrom.setValue(getDefaultString(manufacturerSubsidy.getMonthFrom()));
			txtMonthTo.setValue(getDefaultString(manufacturerSubsidy.getMonthTo()));
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		super.reset();
		checkMandatorySelectField(cbxAssetBrand, "brand");
		checkMandatorySelectField(cbxAssetSerie, "serie");
		checkDoubleField(txtSubsidyAmount, "subsidy.amount");
		checkIntegerField(txtMonthFrom, "from.month");
		checkIntegerField(txtMonthTo, "to.month");
		return errors.isEmpty();
	}
	
}

package com.nokor.efinance.ra.ui.panel.insurance.compensation.reposession;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.service.AssetModelRestriction;
import com.nokor.efinance.core.financial.model.ManufacturerCompensation;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
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
public class CompensationReposessionFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 4085001844573980291L;

	private EntityComboBox<AssetMake> cbxAssetBrand;
	private EntityComboBox<AssetModel> cbxAssetSerie;
	private AutoDateField dfEndDate;
	private AutoDateField dfStartDate;
	private TextField txtRefundPercentage;
	private TextField txtFromMonth;
	private TextField txtToMonth;
	
	private ManufacturerCompensation manufacturerCompensation;
	
	public CompensationReposessionFormPanel() {
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
		cbxAssetBrand.setWidth(170, Unit.PIXELS);
		cbxAssetBrand.setRequired(true);
		cbxAssetBrand.renderer();
		
		cbxAssetSerie = new EntityComboBox<>(AssetModel.class, AssetMake.DESCLOCALE);
		cbxAssetSerie.setCaption(I18N.message("serie"));
		cbxAssetSerie.setRequired(true);
		cbxAssetSerie.setWidth(170, Unit.PIXELS);
		
		cbxAssetBrand.addValueChangeListener(new ValueChangeListener() {
		
			/** */
			private static final long serialVersionUID = 9067665525210872596L;

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
		
		txtRefundPercentage = ComponentFactory.getTextField(I18N.message("refund.percentage"), false, 100, 170);
		txtFromMonth = ComponentFactory.getTextField(I18N.message("from.month"), false, 100, 170);
		txtToMonth = ComponentFactory.getTextField(I18N.message("to.month"), false, 100, 170);
		
		FormLayout formLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		formLayout.addComponent(cbxAssetBrand);
		formLayout.addComponent(cbxAssetSerie);
		formLayout.addComponent(txtRefundPercentage);
		formLayout.addComponent(txtFromMonth);
		formLayout.addComponent(txtToMonth);
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
			if (manufacturerCompensation == null) {
				manufacturerCompensation = ManufacturerCompensation.createInstance();
			}
		
			manufacturerCompensation.setAssetMake(cbxAssetBrand.getSelectedEntity());
			manufacturerCompensation.setAssetModel(cbxAssetSerie.getSelectedEntity());
			manufacturerCompensation.setRefundPercentage(getDouble(txtRefundPercentage));
			manufacturerCompensation.setFromMonth(getInteger(txtFromMonth));
			manufacturerCompensation.setToMonth(getInteger(txtToMonth));
			manufacturerCompensation.setStartDate(dfStartDate.getValue());
			manufacturerCompensation.setEndDate(dfEndDate.getValue());
			
		}
		return manufacturerCompensation;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		manufacturerCompensation = ManufacturerCompensation.createInstance();
		cbxAssetBrand.setValue(null);
		cbxAssetSerie.setValue(null);
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		txtRefundPercentage.setValue("");
		txtFromMonth.setValue("");
		txtToMonth.setValue("");
	}
	
	/**
	 * 
	 * @param compenId
	 */
	public void assignValues(Long compenId) {
		super.reset();
		if (compenId != null) {
			manufacturerCompensation = ENTITY_SRV.getById(ManufacturerCompensation.class, compenId);
			cbxAssetBrand.setSelectedEntity(manufacturerCompensation.getAssetMake());
			cbxAssetSerie.setSelectedEntity(manufacturerCompensation.getAssetModel());
			txtRefundPercentage.setValue(MyNumberUtils.formatDoubleToString(
					MyNumberUtils.getDouble(manufacturerCompensation.getRefundPercentage()), "###,##0.00"));
			txtFromMonth.setValue(getDefaultString(manufacturerCompensation.getFromMonth()));
			txtToMonth.setValue(getDefaultString(manufacturerCompensation.getToMonth()));
			dfStartDate.setValue(manufacturerCompensation.getStartDate());
			dfEndDate.setValue(manufacturerCompensation.getEndDate());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatorySelectField(cbxAssetBrand, "brand");
		checkMandatorySelectField(cbxAssetSerie, "serie");
		checkDoubleField(txtRefundPercentage, "refund.percentage");	
		checkIntegerField(txtFromMonth, "from.month");
		checkIntegerField(txtToMonth, "to.month");
		return errors.isEmpty();
	}

}

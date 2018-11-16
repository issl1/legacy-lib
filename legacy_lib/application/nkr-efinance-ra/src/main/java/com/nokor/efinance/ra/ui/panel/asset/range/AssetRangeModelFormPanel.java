package com.nokor.efinance.ra.ui.panel.asset.range;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.asset.model.EEngine;
import com.nokor.efinance.core.asset.model.EFinAssetType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetRangeModelFormPanel extends AbstractFormPanel implements FinServicesHelper, ClickListener, FMEntityField {

	/** */
	private static final long serialVersionUID = -56201354314565590L;

	private AssetModel assetModel;
	
	private TextField txtCode;
	private TextField txtSerie;
	private TextField txtCharacteristic;
	private TextField txtYear;
	private ERefDataComboBox<EEngine> cbxCC;
    private TextField txtSerieName;
    private TextField txtPrice;
    private EntityRefComboBox<AssetMake> cbxAssetMake;
    private EntityRefComboBox<AssetRange> cbxAssetRange;
    private EntityComboBox<AssetCategory> cbxAssetCategory;
    private CheckBox cbActive;
    
    private Button btnSaveAdd;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("asset.make.create"));
        btnSaveAdd = new NativeButton(I18N.message("save.add"), this);
        btnSaveAdd.setIcon(FontAwesome.SAVE);
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
		navigationPanel.addButton(btnSaveAdd);
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		assetModel.setCode(txtCode.getValue());
		assetModel.setDesc(txtSerieName.getValue());
		assetModel.setDescEn(txtSerieName.getValue());
		assetModel.setSerie(txtSerie.getValue());
		assetModel.setCharacteristic(txtCharacteristic.getValue());
		assetModel.setEngine(cbxCC.getSelectedEntity());
		assetModel.setYear(getInteger(txtYear));
		assetModel.setAssetCategory(cbxAssetCategory.getSelectedEntity());
		assetModel.setAssetType(EFinAssetType.MOTO);
		assetModel.setAssetRange(cbxAssetRange.getSelectedEntity());
		assetModel.setCalculMethod(ECalculMethod.FIX);
		assetModel.setTiPrice(Double.parseDouble((txtPrice.getValue().equals("") ? "0" : txtPrice.getValue())));
		assetModel.setVatPrice(0d);
		assetModel.setTePrice(assetModel.getTePrice());
		assetModel.setAllowChangePrice(true);
		assetModel.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return assetModel;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		AssetModel assetModel = (AssetModel) getEntity();
		ASS_MODEL_SRV.saveOrUpdateAssetModel(assetModel);
		assignValues(cbxAssetRange.getSelectedEntity().getId(), assetModel.getId());
		displaySuccess();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();	
		formPanel.setStyleName("myform-align-left");
		txtCode = ComponentFactory.getTextField("asset.id", false, 60, 170);		
		txtCode.setEnabled(false);
		txtSerieName = ComponentFactory.getTextField("serie.name", true, 60, 170);		
		txtSerie = ComponentFactory.getTextField("serie", true, 60, 170);
		txtYear = ComponentFactory.getTextField("year", true, 4, 80);

		cbxCC = new ERefDataComboBox<EEngine>(EEngine.values());
		cbxCC.setWidth(170, Unit.PIXELS);
		cbxCC.setCaption(I18N.message("cc"));
		cbxCC.setRequired(true);
		
		txtCharacteristic = ComponentFactory.getTextField("characteristic", false, 100, 170);
		
        txtPrice = ComponentFactory.getTextField("manufacturing.price", false, 60, 170);
        
        cbxAssetMake = new EntityRefComboBox<AssetMake>(I18N.message("brand"));
		cbxAssetMake.setWidth(170, Unit.PIXELS);
		cbxAssetMake.setEnabled(false);
		
		cbxAssetRange = new EntityRefComboBox<AssetRange>(I18N.message("model"));
		cbxAssetRange.setWidth(170, Unit.PIXELS);
		cbxAssetRange.setRequired(true);
		cbxAssetRange.setEnabled(false);
                    
        cbxAssetCategory = new EntityComboBox<>(AssetCategory.class, AssetCategory.DESCEN);
        cbxAssetCategory.setCaption(I18N.message("asset.category"));
        cbxAssetCategory.setWidth(170, Unit.PIXELS);
        cbxAssetCategory.renderer();
        cbxAssetCategory.setRequired(true);
        
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        formPanel.addComponent(cbxAssetMake);
        formPanel.addComponent(cbxAssetRange);
        formPanel.addComponent(cbxAssetCategory);
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtSerie);
        formPanel.addComponent(txtYear);
        formPanel.addComponent(cbxCC);
        formPanel.addComponent(txtSerieName);
        formPanel.addComponent(txtCharacteristic);
        formPanel.addComponent(txtPrice);
        formPanel.addComponent(cbActive);
		return formPanel;
	}
	
	/**
	 * 
	 * @param assRangeId
	 * @param assModelId
	 */
	public void assignValues(Long assRangeId, Long assModelId) {
		this.reset();
		if (assRangeId != null) {
			BaseRestrictions<AssetRange> assRangeRestrictions = new BaseRestrictions<AssetRange>(AssetRange.class);
			assRangeRestrictions.addCriterion(Restrictions.eq(ID, assRangeId));
			cbxAssetRange.setRestrictions(assRangeRestrictions);
			cbxAssetRange.renderer(); 
			cbxAssetRange.setSelectedEntity(ASS_MODEL_SRV.getById(AssetRange.class, assRangeId));
			
			cbxAssetMake.setRestrictions(new BaseRestrictions<AssetMake>(AssetMake.class));
			cbxAssetMake.renderer();
			cbxAssetMake.setSelectedEntity(cbxAssetRange.getSelectedEntity().getAssetMake());
		}
		if (assModelId != null) {
			assetModel = ENTITY_SRV.getById(AssetModel.class, assModelId);
			if (assetModel.getAssetRange() != null) {
				cbxAssetMake.setSelectedEntity(assetModel.getAssetRange().getAssetMake());
			}
			cbxAssetRange.setSelectedEntity(assetModel.getAssetRange());
			txtCode.setValue(assetModel.getCode());
			txtSerie.setValue(assetModel.getSerie());
			txtYear.setValue(getDefaultString(assetModel.getYear()));
			cbxCC.setSelectedEntity(assetModel.getEngine() != null ? assetModel.getEngine() : null);
			txtCharacteristic.setValue(assetModel.getCharacteristic());
			txtSerieName.setValue(assetModel.getDescEn());
			cbActive.setValue(assetModel.getStatusRecord().equals(EStatusRecord.ACTIV));
			cbxAssetCategory.setSelectedEntity(assetModel.getAssetCategory());
			txtPrice.setValue(AmountUtils.format(assetModel.getTiPrice()));
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		assetModel = AssetModel.createInstance();
		txtCode.setValue("");
		txtSerieName.setValue("");
		txtSerie.setValue("");
		txtYear.setValue("");
		cbxCC.setSelectedEntity(null);
		txtCharacteristic.setValue("");
		cbActive.setValue(true);
		cbxAssetCategory.setSelectedEntity(null);
		txtPrice.setValue("");
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		errors.clear();
		checkMandatorySelectField(cbxAssetRange, "asset.range");
		checkMandatorySelectField(cbxAssetCategory, "asset.category");
		checkMandatoryField(txtSerie, "serie");
		checkMandatoryField(txtYear, "year");
		checkMandatorySelectField(cbxCC, "cc");
		checkMandatoryField(txtSerieName, "serie.name");
		checkIntegerField(txtYear, "year");
		checkDoubleField(txtPrice, "asset.price");
		return errors.isEmpty();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSaveAdd) {
			if (validate()) {
				saveEntity();
				reset();
			} else {
				displayErrors();
			}
		}
	}
}

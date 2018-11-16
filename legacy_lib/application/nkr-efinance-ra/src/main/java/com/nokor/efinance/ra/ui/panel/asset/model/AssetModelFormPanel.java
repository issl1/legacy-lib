package com.nokor.efinance.ra.ui.panel.asset.model;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
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
import com.nokor.efinance.core.asset.service.AssetModelRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
public class AssetModelFormPanel extends AbstractFormPanel implements FinServicesHelper {

	private static final long serialVersionUID = -2041340000460817816L;

	/** */
	public static final String NAME = "2041340000460817816L";
	
	private AssetModel assetModel;
	private TextField txtCode;
	private TextField txtSerie;
	// private TextField txtCharacteristic;
	private TextField txtYear;
	private ERefDataComboBox<EEngine> cbxCC;
    private TextField txtDesc;
    private TextField txtPrice;
//    private TextField txtStandardFinanceAmount;
    private EntityComboBox<AssetMake> cbxAssetBrand;
    private EntityComboBox<AssetRange> cbxAssetRange;
    private EntityComboBox<AssetCategory> cbxAssetCategory;
    private CheckBox cbActive;
    
    private Button btnSaveAdd;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("asset.model.create"));
        btnSaveAdd = new NativeButton(I18N.message("save.add"));
        btnSaveAdd.setIcon(FontAwesome.SAVE);
        btnSaveAdd.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 8424178915363564075L;

			@Override
			public void buttonClick(ClickEvent event) {
				saveAdd();
			}
		});
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
		navigationPanel.addButton(btnSaveAdd);
	}
	
    /**
     * 
     * @return
     */
	protected Entity getEntity() {
		assetModel.setCode(txtCode.getValue());
		assetModel.setDesc(txtDesc.getValue());
		assetModel.setDescEn(txtDesc.getValue());
		assetModel.setSerie(txtSerie.getValue());
		// assetModel.setCharacteristic(txtCharacteristic.getValue());
		assetModel.setEngine(cbxCC.getSelectedEntity());
		assetModel.setYear(getInteger(txtYear));
		assetModel.setAssetCategory(cbxAssetCategory.getSelectedEntity());
		assetModel.setAssetType(EFinAssetType.MOTO);
		assetModel.setAssetRange((AssetRange) cbxAssetRange.getSelectedEntity());
		assetModel.setCalculMethod(ECalculMethod.FIX);
		assetModel.setTiPrice(Double.parseDouble((txtPrice.getValue().equals("") ? "0" : txtPrice.getValue())));
		assetModel.setVatPrice(0d);
		assetModel.setTePrice(assetModel.getTePrice());
//		assetModel.setStandardFinanceAmount(MyNumberUtils.getDouble(txtStandardFinanceAmount.getValue(), 0d));
		assetModel.setAllowChangePrice(true);
		assetModel.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return assetModel;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkSerieExist() {
		AssetModelRestriction assetModelRestriction = new AssetModelRestriction();
		assetModelRestriction.setAssetRange(cbxAssetRange.getSelectedEntity());
		assetModelRestriction.setSerie(txtSerie.getValue());
		List<AssetModel> listAssetModel = ENTITY_SRV.list(assetModelRestriction);
		if (listAssetModel != null && !listAssetModel.isEmpty()) {
			errors.add(I18N.message("serie.exist", new String[] { I18N.message("serie") }));
		}
		return errors.isEmpty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		AssetModel assetModel = (AssetModel) getEntity();
		ASS_MODEL_SRV.saveOrUpdateAssetModel(assetModel);
		assignValues(assetModel.getId());
	}

	/**
	 * 
	 * @return
	 */
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();	
		formPanel.setStyleName("myform-align-left");
		txtCode = ComponentFactory.getTextField("asset.id", false, 60, 170);		
		txtCode.setEnabled(false);
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 170);		
		txtSerie = ComponentFactory.getTextField("serie", true, 60, 170);
		txtYear = ComponentFactory.getTextField("year", true, 4, 80);
		
		cbxCC = new ERefDataComboBox<EEngine>(EEngine.values());
		cbxCC.setWidth(170, Unit.PIXELS);
		cbxCC.setCaption(I18N.message("cc"));
		cbxCC.setRequired(true);
		
		// txtCharacteristic = ComponentFactory.getTextField("characteristic", false, 100, 170);
		
        txtPrice = ComponentFactory.getTextField("manufacturing.price", false, 60, 170);
//        txtStandardFinanceAmount = ComponentFactory.getTextField("standard.finance.amount", true, 60, 170);
        
        cbxAssetBrand = new EntityComboBox<>(AssetMake.class, AssetMake.DESCEN);
		cbxAssetBrand.setCaption(I18N.message("brand"));
		cbxAssetBrand.setWidth(170, Unit.PIXELS);
		cbxAssetBrand.renderer();
		
		cbxAssetRange = new EntityComboBox<>(AssetRange.class, AssetRange.DESCEN);
		cbxAssetRange.setCaption(I18N.message("model"));
		cbxAssetRange.setWidth(170, Unit.PIXELS);
		cbxAssetRange.setRequired(true);
		cbxAssetRange.renderer();
        
        cbxAssetBrand.addValueChangeListener(new ValueChangeListener() {
			/**
			 */
			private static final long serialVersionUID = 2132004230189208270L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxAssetBrand.getSelectedEntity() != null) {
					BaseRestrictions<AssetRange> restrictions = new BaseRestrictions<>(AssetRange.class);
					restrictions.addCriterion(Restrictions.eq("assetMake.id", cbxAssetBrand.getSelectedEntity().getId()));
					cbxAssetRange.renderer(restrictions);
				}
			}
		});
                    
        cbxAssetCategory = new EntityComboBox<>(AssetCategory.class, AssetCategory.DESCEN);
        cbxAssetCategory.setCaption(I18N.message("asset.category"));
        cbxAssetCategory.setWidth(170, Unit.PIXELS);
        cbxAssetCategory.renderer();
        cbxAssetCategory.setRequired(true);
        
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        formPanel.addComponent(cbxAssetBrand);
        formPanel.addComponent(cbxAssetRange);
        formPanel.addComponent(cbxAssetCategory);
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtSerie);
        formPanel.addComponent(txtYear);
        formPanel.addComponent(cbxCC);
        formPanel.addComponent(txtDesc);
        // formPanel.addComponent(txtCharacteristic);
        formPanel.addComponent(txtPrice);
//        formPanel.addComponent(txtStandardFinanceAmount);
        formPanel.addComponent(cbActive);
		return formPanel;
	}

	/**
	 * @param asmodId
	 */
	public void assignValues(Long asmodId) {
		super.reset();
		if (asmodId != null) {
			assetModel = ENTITY_SRV.getById(AssetModel.class, asmodId);
			if (assetModel.getAssetRange() != null) {
				cbxAssetBrand.setSelectedEntity(assetModel.getAssetRange().getAssetMake());
			}
			cbxAssetRange.setSelectedEntity(assetModel.getAssetRange());
			txtCode.setValue(assetModel.getCode());
			txtSerie.setValue(assetModel.getSerie());
			txtYear.setValue(getDefaultString(assetModel.getYear()));
			cbxCC.setSelectedEntity(assetModel.getEngine() != null ? assetModel.getEngine() : null);
			// txtCharacteristic.setValue(assetModel.getCharacteristic());
			txtDesc.setValue(assetModel.getDescEn());
			cbActive.setValue(assetModel.getStatusRecord().equals(EStatusRecord.ACTIV));
			cbxAssetCategory.setSelectedEntity(assetModel.getAssetCategory());
			txtPrice.setValue(AmountUtils.format(assetModel.getTiPrice()));
//			txtStandardFinanceAmount.setValue(AmountUtils.format(assetModel.getStandardFinanceAmount()));
		}
	}
	
	/**
	 * save / add
	 */
	private void saveAdd() {
		if (validate()) {
			ASS_MODEL_SRV.saveOrUpdateAssetModel((AssetModel) this.getEntity());
			displaySuccess();
			reset();
		} else {
			displayErrors();
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		super.reset();
		assetModel = new AssetModel();
		txtCode.setValue("");
		txtDesc.setValue("");
		txtSerie.setValue("");
		txtYear.setValue("");
		cbxCC.setSelectedEntity(null);;
		// txtCharacteristic.setValue("");
		cbActive.setValue(true);
		cbxAssetRange.setSelectedEntity(null);
		cbxAssetCategory.setSelectedEntity(null);
		cbxAssetBrand.setSelectedEntity(null);
		txtPrice.setValue("");
//		txtStandardFinanceAmount.setValue("");
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		super.reset();
		checkMandatorySelectField(cbxAssetRange, "asset.range");
		checkMandatorySelectField(cbxAssetCategory, "asset.category");
		checkMandatoryField(txtSerie, "serie");
		checkMandatoryField(txtYear, "year");
		checkMandatorySelectField(cbxCC, "cc");
//		checkMandatoryField(txtStandardFinanceAmount, "standard.finance.amount");
		checkIntegerField(txtYear, "year");
		checkDoubleField(txtPrice, "asset.price");
//		checkDoubleField(txtStandardFinanceAmount, "standard.finance.amount");
		if (assetModel != null) {
			if (!txtSerie.getValue().equals(assetModel.getSerie())) {
				checkSerieExist();
			}
		}
		return errors.isEmpty();
	}
}

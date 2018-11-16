package com.nokor.efinance.gui.ui.panel.contract.asset;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.model.EEngine;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.service.ContractFlagRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.asset.registration.AssetRegistrationPanel;
import com.nokor.efinance.gui.ui.panel.contract.asset.supplier.AssetSupplierPanel;
import com.nokor.efinance.gui.ui.panel.contract.insurance.AssetInsurancePanel;
import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Asset panel latest version
 * @author uhout.cheng
 */
public class AssetPanel extends AbstractTabPanel implements FinServicesHelper, SelectedTabChangeListener, ClickListener {

	/** */
	private static final long serialVersionUID = -5361123128002356763L;
	
	private static final String TEMPLATE = "asset/assetSummaryPageLayout";
	
	private Contract contract;
	private TabSheet accordingTab;
	
	private AssetSupplierPanel supplierPanel;
	private AssetRegistrationPanel registrationPanel;
	private AssetInsurancePanel insurancePanel;
	
	private TextField txtAssetID;
	private TextField txtBrand;
	private TextField txtModel;
	private TextField txtSeries;
	private TextField txtYear;
	private TextField txtCharacteristic;
	private ERefDataComboBox<EEngine> cbxCC;
	private ERefDataComboBox<EColor> cbxColor;
	private EntityComboBox<AssetCategory> cbxAssetCategory;
	private TextField txtEngineNO;
	private TextField txtChassisNO;
	private TextField txtRegistrationNO;
	private TextField txtManufacturingPrice;
	private TextField txtMarketValue;
	private TextField txtStatus;
	private TextField txtMileage;
	
	private Label lblMileage;
	private Button btnEdit;
	private Button btnSave;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		accordingTab = new TabSheet();
		supplierPanel = new AssetSupplierPanel();
		registrationPanel = new AssetRegistrationPanel();
		insurancePanel = new AssetInsurancePanel();
		
		txtAssetID = ComponentFactory.getTextField(60, 130);
		txtBrand = ComponentFactory.getTextField(60, 130);
		txtModel= ComponentFactory.getTextField(60, 130);
		txtSeries= ComponentFactory.getTextField(60, 130);
		txtYear= ComponentFactory.getTextField(60, 130);
		txtCharacteristic= ComponentFactory.getTextField(60, 130);
		
		cbxCC = new ERefDataComboBox<EEngine>(EEngine.values());
		cbxCC.setWidth(130, Unit.PIXELS);
		
		cbxColor = new ERefDataComboBox<>(EColor.values());
		cbxColor.setWidth(130, Unit.PIXELS);
		
		cbxAssetCategory = new EntityComboBox<>(AssetCategory.class, AssetCategory.DESCLOCALE);
		cbxAssetCategory.renderer();
		cbxAssetCategory.setImmediate(true);
        cbxAssetCategory.setWidth(130, Unit.PIXELS);
        
		txtEngineNO= ComponentFactory.getTextField(60, 130);
		txtChassisNO= ComponentFactory.getTextField(60, 130);
		txtRegistrationNO= ComponentFactory.getTextField(60, 130);
		txtManufacturingPrice= ComponentFactory.getTextField(60, 130);
		txtMarketValue= ComponentFactory.getTextField(60, 130);
		txtStatus= ComponentFactory.getTextField(60, 130);
		
		txtMileage = ComponentFactory.getTextField(60, 130);
		lblMileage = ComponentFactory.getLabel(I18N.message("mileage"));
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 60, "btn btn-success button-small");
		btnEdit.addClickListener(this);
		setDisableControls(false);
		
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
		btnSave.setEnabled(false);
		
		accordingTab.addTab(supplierPanel, I18N.message("supplier"));
		accordingTab.addTab(registrationPanel, I18N.message("registration"));
		accordingTab.addTab(insurancePanel, I18N.message("insurance"));
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.addComponent(getContractLayout());
		contentLayout.addComponent(accordingTab);
		return contentLayout;
	}
	
	/**
	 * 
	 * @param contract
	 * @param loanSummaryPanel
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		
		if (contract != null) {
			Asset asset = contract.getAsset();
			assignValuesToAsset(asset);
			supplierPanel.assignValues(contract);
			registrationPanel.assignValues(contract, this);
			insurancePanel.assignValues(contract);
		}
	}
	
	/**
	 * 
	 * @param asset
	 */
	public void assignValuesToAsset(Asset asset) {
		if (asset != null) {
			AssetModel assetModel = asset.getModel();
			AssetRange assetRange = null;
			AssetMake assetMake = null;
			if (assetModel != null) {
				assetRange = assetModel.getAssetRange();
				if (assetRange != null) {
					assetMake = assetRange.getAssetMake();
				}
			}
			txtAssetID.setValue(asset.getCode());
			txtBrand.setValue(getDefaultString(assetMake == null ? "" : assetMake.getDescLocale()));
			txtModel.setValue(getDefaultString(assetRange == null ? "" : assetRange.getDescLocale()));
			txtSeries.setValue(getDefaultString(assetModel == null ? "" : assetModel.getSerie()));
			txtYear.setValue(getDefaultString(asset.getYear()));
			txtCharacteristic.setValue(getDefaultString(assetModel == null ? "" : assetModel.getCharacteristic()));
			cbxCC.setSelectedEntity(assetModel.getEngine() != null ? assetModel.getEngine() : null);
			cbxAssetCategory.setSelectedEntity(assetModel != null ? assetModel.getAssetCategory() : null);
			txtRegistrationNO.setValue(getDefaultString(asset.getPlateNumber()));
			txtMileage.setValue(getDefaultString(asset.getMileage()));
			getAssetStatus(contract);
			
			refreshAssetInfos(asset);
		}
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private String getAssetStatus(Contract contract) {
		ContractFlagRestriction restrictions = new ContractFlagRestriction();
		restrictions.setConId(contract.getId());
		List<ContractFlag> contractFlags = ENTITY_SRV.list(restrictions);
		if (contractFlags != null && !contractFlags.isEmpty()) {
			ContractFlag contractFlag = contractFlags.get(0);
			if (contractFlag != null) {
				txtStatus.setValue(contractFlag.getFlag() != null ? contractFlag.getFlag().getDesc() : StringUtils.EMPTY);
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @param asset
	 */
	public void refreshAssetInfos(Asset asset) {
		cbxColor.setSelectedEntity(asset.getColor());
		txtEngineNO.setValue(getDefaultString(asset.getEngineNumber()));
		txtChassisNO.setValue(getDefaultString(asset.getChassisNumber()));
		txtManufacturingPrice.setValue(AmountUtils.format(MyNumberUtils.getDouble(asset.getTiAssetPrice())));
		txtMarketValue.setValue(AmountUtils.format(0d));
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getContractLayout() {
		CustomLayout customLayout = LayoutHelper.createCustomLayout(TEMPLATE);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(TEMPLATE), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(new Label(I18N.message("asset.id")), "lblAssetID");
		customLayout.addComponent(txtAssetID, "txtAssetID");
		customLayout.addComponent(new Label(I18N.message("brand")), "lblBrand");
		customLayout.addComponent(txtBrand, "txtBrand");
		customLayout.addComponent(new Label(I18N.message("model")), "lblModel");
		customLayout.addComponent(txtModel, "txtModel");
		customLayout.addComponent(new Label(I18N.message("series")), "lblSeries");
		customLayout.addComponent(txtSeries, "txtSeries");
		customLayout.addComponent(new Label(I18N.message("year")), "lblYear");
		customLayout.addComponent(txtYear, "txtYear");
		customLayout.addComponent(new Label(I18N.message("characteristics")), "lblCharacteristic");
		customLayout.addComponent(txtCharacteristic, "txtCharacteristic");
		customLayout.addComponent(new Label(I18N.message("cc")), "lblCC");
		customLayout.addComponent(cbxCC, "cbxCC");
		customLayout.addComponent(new Label(I18N.message("color")), "lblColor");
		customLayout.addComponent(cbxColor, "cbxColor");
		customLayout.addComponent(new Label(I18N.message("asset.category")), "lblAssetCategory");
		customLayout.addComponent(cbxAssetCategory, "cbxAssetCategory");
		customLayout.addComponent(new Label(I18N.message("engine.no")), "lblEngineNO");
		customLayout.addComponent(txtEngineNO, "txtEngineNO");
		customLayout.addComponent(new Label(I18N.message("chassis.no")), "lblChassisNO");
		customLayout.addComponent(txtChassisNO, "txtChassisNO");
		customLayout.addComponent(new Label(I18N.message("registration.no")), "lblRegistrationNO");
		customLayout.addComponent(txtRegistrationNO, "txtRegistrationNO");
		customLayout.addComponent(new Label(I18N.message("manufacturing.price")), "lblManufacturingPrice");
		customLayout.addComponent(txtManufacturingPrice, "txtManufacturingPrice");
		customLayout.addComponent(new Label(I18N.message("market.value")), "lblMarketValue");
		customLayout.addComponent(txtMarketValue, "txtMarketValue");
		customLayout.addComponent(new Label(I18N.message("status")), "lblStatus");
		customLayout.addComponent(txtStatus, "txtStatus");
		
		customLayout.addComponent(lblMileage, "lblMileage");
		customLayout.addComponent(txtMileage, "txtMileage");
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setMargin(true);
		horLayout.addComponent(customLayout);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(btnEdit);
		buttonLayout.addComponent(btnSave);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(new MarginInfo(false, true, true, false));
		verLayout.addComponent(horLayout);
		verLayout.addComponent(buttonLayout);
		verLayout.setComponentAlignment(buttonLayout, Alignment.TOP_RIGHT);
		
		Panel panel = new Panel(verLayout);

		return panel;
	}
	
	
	
	/**
	 * Validate for activated
	 * @return
	 */
	public List<String> validateForActivated() {
		super.removeErrorsPanel();
		if (contract != null) {
			Asset asset = contract.getAsset();
			if (asset.getColor() == null) {
				errors.add(I18N.message("field.required.1", I18N.message("marketing.color")));
			}
			if (StringUtils.isEmpty(asset.getChassisNumber())) {
				errors.add(I18N.message("field.required.1", I18N.message("chassis.no")));
			}
			if (StringUtils.isEmpty(asset.getEngineNumber())) {
				errors.add(I18N.message("field.required.1", I18N.message("engine.no")));
			}
			if (StringUtils.isEmpty(asset.getTaxInvoiceNumber())) {
				errors.add(I18N.message("field.required.1", I18N.message("tax.invoice.no")));
			}
			if (MyNumberUtils.getDouble(asset.getTiAssetPrice()) == 0) {
				errors.add(I18N.message("field.required.1", I18N.message("wholesale.price")));
			}
			checkIntegerField(txtMileage, "mileage");
		}
		return errors;
	}
	
	/**
	 * 
	 */
	public void reset() {
		contract = Contract.createInstance();
		txtAssetID.setValue("");
		txtBrand.setValue("");
		txtModel.setValue("");
		txtSeries.setValue("");
		txtYear.setValue("");
		txtCharacteristic.setValue("");
		cbxCC.setSelectedEntity(null);
		cbxColor.setSelectedEntity(null);
		cbxAssetCategory.setSelectedEntity(null);
		txtEngineNO.setValue("");
		txtChassisNO.setValue("");
		txtRegistrationNO.setValue("");
		txtManufacturingPrice.setValue("");
		txtMarketValue.setValue("");
		txtStatus.setValue("");
		txtMileage.setValue("");
		supplierPanel.reset();
		registrationPanel.reset();
	}

	/**
	 * 
	 * @param isEnable
	 */
	private void setDisableControls(boolean isEnable) {
		txtAssetID.setEnabled(isEnable);
		txtBrand.setEnabled(isEnable);
		txtModel.setEnabled(isEnable);
		txtSeries.setEnabled(isEnable);
		txtYear.setEnabled(isEnable);
		txtCharacteristic.setEnabled(isEnable);
		cbxCC.setEnabled(isEnable);
		cbxColor.setEnabled(isEnable);
		cbxAssetCategory.setEnabled(isEnable);
		txtEngineNO.setEnabled(isEnable);
		txtChassisNO.setEnabled(isEnable);
		txtRegistrationNO.setEnabled(isEnable);
		txtManufacturingPrice.setEnabled(isEnable);
		txtMarketValue.setEnabled(isEnable);
		txtStatus.setEnabled(isEnable);
		txtMileage.setEnabled(isEnable);
	}
	
	private boolean validate() {
		errors.clear();
		checkIntegerField(txtMileage, "mileage");
		if (!errors.isEmpty()) {
			ComponentLayoutFactory.displayErrorMsg(errors.get(0));
		}
		return errors.isEmpty();
	}
	
	/**
	 * Save Mileage
	 */
	private void save() {
		Asset asset = contract.getAsset();
		asset.setMileage(MyNumberUtils.getInteger(txtMileage.getValue(), 0));
		try {
			ENTITY_SRV.saveOrUpdate(asset);
			ComponentLayoutFactory.displaySuccessfullyMsg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (event.getTabSheet().equals(supplierPanel)) {
			
		} else if (event.getTabSheet().equals(registrationPanel)) {
			
		} else if (event.getTabSheet().equals(insurancePanel)) {
			
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnEdit) {
			txtMileage.setEnabled(true);
			btnSave.setEnabled(true);
			btnEdit.setEnabled(false);
		} else if (event.getButton() == btnSave) {
			if (validate()) {
				save();
				txtMileage.setEnabled(false);
				btnSave.setEnabled(false);
				btnEdit.setEnabled(true);
			}
		}
		
	}
	
}

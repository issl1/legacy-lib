package com.nokor.efinance.core.asset.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.model.MAsset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Asset detail panel
 * @author uhout.cheng
 */
public class AssetPanelOld extends AbstractTabPanel implements MAsset, MContract, ClickListener, FinServicesHelper, SaveClickListener {
	
	/** */
	private static final long serialVersionUID = -6228658016714960425L;
		
	private TextField txtBrand;
	private TextField txtSerie;
	private TextField txtModel;
	private TextField txtAssetPrice;
	private TextField txtChassisNo;
	private TextField txtEngineNo;
	private ERefDataComboBox<EColor> cbxColor;
	private TextField txtDealerName;
	private TextField txtTaxInvoiceNumber;
	
	private Button btnMoreDetailLink;
	private AssetDetailLayout assetDetailLayout;
	private VerticalLayout mainVerLayout;
	private VerticalLayout mainLayout;
	private Contract contract;
	private NavigationPanel navigationPanel;
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Button getButton(String caption) {
		Button btnButton = ComponentFactory.getButton(caption);
		btnButton.setStyleName(Reindeer.BUTTON_LINK);
		btnButton.addClickListener(this);
		return btnButton;
	} 
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(values);
		comboBox.setWidth(200, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnMoreDetailLink) {
			assetDetailLayout.getBtnBack().addClickListener(new ClickListener() {
				/** */
				private static final long serialVersionUID = 2742389704939811876L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					navigationPanel.setVisible(true);
					assetDetailLayout.reset();
					mainLayout.removeAllComponents();
					mainLayout.addComponent(mainVerLayout);
				}
			});
			navigationPanel.setVisible(false);
			super.removeErrorsPanel();
			mainLayout.removeAllComponents();
			mainLayout.addComponent(assetDetailLayout);
		} 
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		btnMoreDetailLink = getButton("more.detail");
		btnMoreDetailLink.addClickListener(this);
		
		assetDetailLayout = new AssetDetailLayout();
		
		txtBrand = ComponentFactory.getTextField(60, 200);
		txtSerie = ComponentFactory.getTextField(60, 200);
		txtModel = ComponentFactory.getTextField(60, 200);
		
		txtAssetPrice = ComponentFactory.getTextField(60, 200);
		txtChassisNo = ComponentFactory.getTextField(60, 200);
		txtEngineNo = ComponentFactory.getTextField(60, 200);
		cbxColor = getERefDataComboBox(EColor.values()); 
		txtDealerName = ComponentFactory.getTextField(60, 200);
		txtTaxInvoiceNumber = ComponentFactory.getTextField(50, 200);
		
		setEnabledAssetControls(false);
		
		String template = "assetTabLayout";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(new Label(I18N.message("asset.make")), "lblBrand");
		customLayout.addComponent(txtBrand, "cbxBrand");
		customLayout.addComponent(new Label(I18N.message("series")), "lblSeries");
		customLayout.addComponent(txtSerie, "cbxSerie");
		customLayout.addComponent(new Label(I18N.message("marketing.name")), "lblMarketingName");
		customLayout.addComponent(txtModel, "cbxModel");
		customLayout.addComponent(new Label(I18N.message("asset.price")), "lblAssetPrice");
		customLayout.addComponent(txtAssetPrice, "txtAssetPrice");
		customLayout.addComponent(new Label(I18N.message("marketing.color")), "lblMarketingColor");
		customLayout.addComponent(cbxColor, "cbxMarketingColor");
		customLayout.addComponent(new Label(I18N.message("chassis.no")), "lblChassisNo");
		customLayout.addComponent(txtChassisNo, "txtChassisNo");
		customLayout.addComponent(new Label(I18N.message("engine.no")), "lblEngineNo");
		customLayout.addComponent(txtEngineNo, "txtEngineNo");
		customLayout.addComponent(new Label(I18N.message("name.en")), "lblName");
		customLayout.addComponent(txtDealerName, "txtName");
		customLayout.addComponent(new Label(I18N.message("tax.invoice.no")), "lblTaxInvoiceNumber");
		customLayout.addComponent(txtTaxInvoiceNumber, "txtTaxInvoiceNumber");
		
		Panel generalPanel = new Panel();
		generalPanel.setCaption("<h2 style=\"border:1px solid #E3E3E3;padding:9px;border-radius:3px;background-color:#F5F5F5;margin:0;\" "
				+ "align=\"center\" >" + I18N.message("general") + "</h2>");
		generalPanel.setCaptionAsHtml(true);
		generalPanel.setStyleName(Reindeer.PANEL_LIGHT);
		generalPanel.setContent(customLayout);
		
		navigationPanel = new NavigationPanel();
		navigationPanel.addSaveClickListener(this);
		addComponent(navigationPanel, 0);
		
		mainVerLayout = new VerticalLayout();
		mainVerLayout.setSpacing(true);
		mainVerLayout.addComponent(generalPanel);
		mainVerLayout.addComponent(btnMoreDetailLink);
		mainVerLayout.setComponentAlignment(btnMoreDetailLink, Alignment.BOTTOM_RIGHT);
		
		mainLayout = new VerticalLayout();
		mainLayout.addComponent(mainVerLayout);
		
		return mainLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		if (contract != null) {
			this.contract = contract;
			navigationPanel.setVisible(true);
			assetDetailLayout.assignValues(contract);
			Asset asset = contract.getAsset();
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
				
				txtBrand.setValue(assetMake.getDescEn());
				txtSerie.setValue(assetRange.getDescEn());
				txtModel.setValue(assetModel.getDescEn());
				txtAssetPrice.setValue(getDefaultString(asset.getTiAssetPrice()));
				cbxColor.setSelectedEntity(asset.getColor());
				txtChassisNo.setValue(getDefaultString(asset.getChassisNumber()));
				txtEngineNo.setValue(getDefaultString(asset.getEngineNumber()));
				txtTaxInvoiceNumber.setValue(getDefaultString(asset.getTaxInvoiceNumber()));
			}
			Dealer dealer = contract.getDealer();
			if (dealer != null) {
				txtDealerName.setValue(getDefaultString(dealer.getNameEn()));
			}
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		removeErrorsPanel();
		contract = Contract.createInstance();
		mainLayout.removeAllComponents();
		mainLayout.addComponent(mainVerLayout);
		txtBrand.setValue("");
		txtSerie.setValue("");
		txtModel.setValue("");
		txtAssetPrice.setValue("");
		cbxColor.setSelectedEntity(null);
		txtChassisNo.setValue("");
		txtEngineNo.setValue("");
		txtDealerName.setValue("");
		txtTaxInvoiceNumber.setValue("");
	}
	
	/**
	 * @param enabled
	 */
	public void setEnabledAssetControls(boolean enabled) {
		txtBrand.setEnabled(enabled);
		txtSerie.setEnabled(enabled);
		txtModel.setEnabled(enabled);
		txtDealerName.setEnabled(enabled);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		removeErrorsPanel();
		if (validate()) {
			try {
				saveEntity();
				displaySuccess();
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				errors.add(I18N.message("msg.error.technical"));
				errors.add(ex.getMessage());
			}
			if (!errors.isEmpty()) {
				displayErrorsPanel();
			}
			
		} else {
			displayErrorsPanel();
		}
	}
	
	/**
	 * Save Entity
	 */
	public void saveEntity() {
		// Update asset price 
		Asset asset = contract.getAsset();
		double assetPriceUsd = MyNumberUtils.getDouble(getDouble(txtAssetPrice));
		asset.setTiAssetPrice(assetPriceUsd);
		asset.setTeAssetPrice(assetPriceUsd);
		asset.setVatAssetPrice(0d);
		asset.setChassisNumber(txtChassisNo.getValue());
		asset.setEngineNumber(txtEngineNo.getValue());
		asset.setTaxInvoiceNumber(txtTaxInvoiceNumber.getValue());
		asset.setColor(cbxColor.getSelectedEntity());
		// Update advance payment amount & advance payment percentage
		double advancePaymentAmount = assetPriceUsd - MyNumberUtils.getDouble(contract.getTiFinancedAmount());
		double advancePaymentPercentage = getAdvancePaymentPercentage(advancePaymentAmount, assetPriceUsd);
		contract.setTiAdvancePaymentAmount(advancePaymentAmount);
		contract.setTeAdvancePaymentAmount(advancePaymentAmount);
		contract.setVatAdvancePaymentAmount(0d);
		contract.setAdvancePaymentPercentage(advancePaymentPercentage);
		// saveOrUpdate asset info & advance payment amount
		CONT_SRV.updateContractAndAsset(contract);
	}
	
	/**
	 * 
	 * @param advancePaymentAmt
	 * @param assetPrice
	 * @return
	 */
	private Double getAdvancePaymentPercentage(double advancePaymentAmt, double assetPrice) {
		if (advancePaymentAmt > 0) {
			double total = (advancePaymentAmt / assetPrice);
			return MyMathUtils.roundTo(total * 100, 2);
		}
		return 0.00d;
	}
	
	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		checkMandatoryField(txtChassisNo, "chassis.no");
		checkMandatoryField(txtEngineNo, "engine.no");
		checkMandatoryField(txtAssetPrice, "wholesale.price");
		checkDoubleField(txtAssetPrice, "wholesale.price");
		checkMandatorySelectField(cbxColor, "marketing.color");
		checkMandatoryField(txtTaxInvoiceNumber, "tax.invoice.no");
		
		if (errors.isEmpty()) {
			if (ASS_SRV.isChassisNumberExist(txtChassisNo.getValue(), contract.getAsset())) {
				errors.add(I18N.message("chassis.already.existed", new String[] {txtChassisNo.getValue()}));
			}
			if (ASS_SRV.isEnginNumberExist(txtEngineNo.getValue(), contract.getAsset())) {
				errors.add(I18N.message("engine.already.existed", new String[] {txtEngineNo.getValue()}));
			}
		}		
		return errors.isEmpty();
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
		}
		return errors;
	}
}

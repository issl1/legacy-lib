package com.nokor.efinance.gui.ui.panel.contract.asset.registration;

import org.seuksa.frmk.i18n.I18N;

import com.gl.finwiz.share.domain.registration.RegistrationBook;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.asset.AssetPanel;
import com.nokor.efinance.gui.ui.panel.contract.asset.registration.popup.RegistrationDetailPopup;
import com.nokor.efinance.third.finwiz.client.reg.ClientRegistration;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * Registration detail in registration tab
 * @author uhout.cheng
 */
public class RegistrationDetailPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 646243339839720463L;

	private static final String TEMPLATE = "asset/assetSupplierRegistrationLayout";

	private AutoDateField dfArrivalDate;
	private AutoDateField dfRegistrationDate;
	private AutoDateField dfAOMTaxValidationDate;
	private AutoDateField dfAOMTaxExpirationDate;
	private AutoDateField dfAOMTagValidationDate;
	private AutoDateField dfAOMTagExpirationDate;
	private TextField txtRegistrationNO;
	private TextField txtMotorbikeType;
	private TextField txtBookLocation;
	private TextField txtManufacturingYear;
	private TextField txtAOMStatus;
	private TextField txtAOMTaxBalance;
	private TextField txtAOMTaxDPD;
	private TextField txtAOMTaxPenaltyBalance;
	private TextField txtRegistrationType;
	private TextField txtProvince;
	private Button btnEdit;
	private RegistrationDetailPopup registrationDetailPopup;
	
	/**
	 * 
	 * @param assetPanel
	 * @param loanSummaryPanel
	 */
	public RegistrationDetailPanel() {
		dfArrivalDate = ComponentFactory.getAutoDateField();
		dfRegistrationDate = ComponentFactory.getAutoDateField();
		dfAOMTaxValidationDate = ComponentFactory.getAutoDateField();
		dfAOMTaxExpirationDate = ComponentFactory.getAutoDateField();
		dfAOMTagValidationDate = ComponentFactory.getAutoDateField();
		dfAOMTagExpirationDate = ComponentFactory.getAutoDateField();
		txtRegistrationNO = ComponentFactory.getTextField(60, 150);
		txtBookLocation = ComponentFactory.getTextField(60, 150);
		txtManufacturingYear = ComponentFactory.getTextField(60, 150);
		txtAOMStatus = ComponentFactory.getTextField(60, 150);
		txtAOMTaxBalance = ComponentFactory.getTextField(60, 150);
		txtAOMTaxDPD = ComponentFactory.getTextField(60, 150);
		txtAOMTaxPenaltyBalance = ComponentFactory.getTextField(60, 150);
		txtMotorbikeType = ComponentFactory.getTextField(60, 150);
		txtRegistrationType = ComponentFactory.getTextField(60, 150);
		txtProvince = ComponentFactory.getTextField(60, 150);
		setDisableControls(false);
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnEdit.setVisible(false);
		btnEdit.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -2846314067745553713L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(registrationDetailPopup);
			}
		});
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.addComponent(getLayout());
		Panel mainPanel = new Panel();
		mainPanel.setCaption(I18N.message("registration.details"));
		mainPanel.setContent(verLayout);
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 * @param contract
	 * @param assetPanel
	 */
	public void assignValues(Contract contract, AssetPanel assetPanel) {
		RegistrationBook regBook = ClientRegistration.getRegBookByContractReference(contract.getReference());
		if (regBook != null) {
//			registrationDetailPopup = new RegistrationDetailPopup();
//			registrationDetailPopup.assignValues(asset, this, assetPanel);
		
			dfArrivalDate.setValue(regBook.getArrivalDate());
			dfRegistrationDate.setValue(regBook.getRegistrationdate());
			dfAOMTaxValidationDate.setValue(regBook.getTaxValidatedDate());
			dfAOMTaxExpirationDate.setValue(regBook.getTaxExpirationDate());
			dfAOMTagValidationDate.setValue(regBook.getTagValidationDate());
			dfAOMTagExpirationDate.setValue(regBook.getTagExpirationDate());
			txtRegistrationNO.setValue(getDefaultString(regBook.getRegistrationNumber()));
			if (regBook.getRegistrationType() != null) {
				txtRegistrationType.setValue(getDefaultString(regBook.getRegistrationType().getValue()));
			}
			if (regBook.getMotorcycleType() != null) {
				txtMotorbikeType.setValue(getDefaultString(regBook.getMotorcycleType().getDescription()));
			}
			if (regBook.getLocation() != null) {
				txtBookLocation.setValue(getDefaultString(regBook.getLocation().name()));
			}
			if (regBook.getTaxStatus() != null) {
				txtAOMStatus.setValue(getDefaultString(regBook.getTaxStatus().name()));
			}
			txtManufacturingYear.setValue(getDefaultString(regBook.getManufacturingYear()));
			txtAOMTaxBalance.setValue(regBook.getAomTaxBalance() != null ? regBook.getAomTaxBalance().toString() : "0.00");
			txtAOMTaxDPD.setValue(getDefaultString(regBook.getTaxDPD()));
			txtAOMTaxPenaltyBalance.setValue(regBook.getTaxPenaltyBalance() != null ? regBook.getTaxPenaltyBalance().toString() : "0.00");
			if (regBook.getAsset() != null) {
				Asset asset = ASS_SRV.getById(Asset.class, regBook.getAsset().getId());
				if (asset != null) {
					txtProvince.setValue(asset.getRegistrationProvince() != null ? asset.getRegistrationProvince().getDescLocale() : "");
				}
			}
		}
	}
	
	/**
	 * 
	 * @param title
	 * @return
	 */
	private Component getLayout() {
		CustomLayout customLayout = LayoutHelper.createCustomLayout(TEMPLATE);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(TEMPLATE), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(ComponentFactory.getLabel("arrival.date"), "lblArrivalDate");
		customLayout.addComponent(dfArrivalDate, "dfArrivalDate");
		customLayout.addComponent(ComponentFactory.getLabel("registration.date"), "lblRegistrationDate");
		customLayout.addComponent(dfRegistrationDate, "dfRegistrationDate");
		customLayout.addComponent(ComponentFactory.getLabel("registration.no"), "lblRegistrationNO");
		customLayout.addComponent(txtRegistrationNO, "txtRegistrationNO");
		customLayout.addComponent(ComponentFactory.getLabel("registration.type"), "lblRegistrationType");
		customLayout.addComponent(txtRegistrationType, "txtRegistrationType");
		customLayout.addComponent(ComponentFactory.getLabel("motorbike.type"), "lblMotorbikeType");
		customLayout.addComponent(txtMotorbikeType, "txtMotorbikeType");
		customLayout.addComponent(ComponentFactory.getLabel("book.location"), "lblBookLocation");
		customLayout.addComponent(txtBookLocation, "txtBookLocation");
		customLayout.addComponent(ComponentFactory.getLabel("province"), "lblProvince");
		customLayout.addComponent(txtProvince, "txtProvince");
		customLayout.addComponent(ComponentFactory.getLabel("manufacturing.year"), "lblManufacturingYear");
		customLayout.addComponent(txtManufacturingYear, "txtManufacturingYear");
		customLayout.addComponent(ComponentFactory.getLabel("aom.tax.validation.date"), "lblAOMTaxValidationDate");
		customLayout.addComponent(dfAOMTaxValidationDate, "dfAOMTaxValidationDate");
		customLayout.addComponent(ComponentFactory.getLabel("aom.tax.expiration.date"), "lblAOMTaxExpirationDate");
		customLayout.addComponent(dfAOMTaxExpirationDate, "dfAOMTaxExpirationDate");
		customLayout.addComponent(ComponentFactory.getLabel("aom.tax.status"), "lblAOMStatus");
		customLayout.addComponent(txtAOMStatus, "txtAOMStatus");
		customLayout.addComponent(ComponentFactory.getLabel("aom.tax.balance"), "lblAOMTaxBalance");
		customLayout.addComponent(txtAOMTaxBalance, "txtAOMTaxBalance");
		customLayout.addComponent(ComponentFactory.getLabel("aom.tax.dpd"), "lblAOMTaxDPD");
		customLayout.addComponent(txtAOMTaxDPD, "txtAOMTaxDPD");
		customLayout.addComponent(ComponentFactory.getLabel("aom.tax.penalty.balance"), "lblAOMTaxPenaltyBalance");
		customLayout.addComponent(txtAOMTaxPenaltyBalance, "txtAOMTaxPenaltyBalance");
		customLayout.addComponent(ComponentFactory.getLabel("aom.tax.tag.validation.date"), "lblAOMTagValidationDate");
		customLayout.addComponent(dfAOMTagValidationDate, "dfAOMTagValidationDate");
		customLayout.addComponent(ComponentFactory.getLabel("aom.tax.tag.expiration.date"), "lblAOMTagExpirationDate");
		customLayout.addComponent(dfAOMTagExpirationDate, "dfAOMTagExpirationDate");
		Panel panelLayout = new Panel();
		panelLayout.setStyleName(Reindeer.PANEL_LIGHT);
		panelLayout.setContent(customLayout);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		verLayout.addComponent(panelLayout);
		verLayout.addComponent(btnEdit);
		verLayout.setComponentAlignment(btnEdit, Alignment.BOTTOM_RIGHT);
		return verLayout;
	}
	
	/**
	 * 
	 */
	public void reset() {
		dfArrivalDate.setValue(null);
		dfRegistrationDate.setValue(null);
		dfAOMTaxValidationDate.setValue(null);
		dfAOMTaxExpirationDate.setValue(null);
		dfAOMTagValidationDate.setValue(null);
		dfAOMTagExpirationDate.setValue(null);
		txtRegistrationNO.setValue("");
		txtRegistrationType.setValue("");
		txtMotorbikeType.setValue("");
		txtBookLocation.setValue("");
		txtManufacturingYear.setValue("");
		txtAOMStatus.setValue("");
		txtAOMTaxBalance.setValue("");
		txtAOMTaxDPD.setValue("");
		txtAOMTaxPenaltyBalance.setValue("");
		txtProvince.setValue("");
	}
	
	/**
	 * 
	 * @param isEnable
	 */
	private void setDisableControls(boolean isEnable) {
		dfArrivalDate.setEnabled(isEnable);
		dfRegistrationDate.setEnabled(isEnable);
		dfAOMTaxValidationDate.setEnabled(isEnable);
		dfAOMTaxExpirationDate.setEnabled(isEnable);
		dfAOMTagValidationDate.setEnabled(isEnable);
		dfAOMTagExpirationDate.setEnabled(isEnable);
		txtRegistrationNO.setEnabled(isEnable);
		txtRegistrationType.setEnabled(isEnable);
		txtMotorbikeType.setEnabled(isEnable);
		txtBookLocation.setEnabled(isEnable);
		txtManufacturingYear.setEnabled(isEnable);
		txtAOMStatus.setEnabled(isEnable);
		txtAOMTaxBalance.setEnabled(isEnable);
		txtAOMTaxDPD.setEnabled(isEnable);
		txtAOMTaxPenaltyBalance.setEnabled(isEnable);
		txtProvince.setEnabled(isEnable);
	}
	
}

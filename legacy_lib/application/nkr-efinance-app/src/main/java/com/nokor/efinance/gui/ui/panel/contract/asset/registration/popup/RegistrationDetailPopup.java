package com.nokor.efinance.gui.ui.panel.contract.asset.registration.popup;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.ERegBookStatus;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.asset.AssetPanel;
import com.nokor.efinance.gui.ui.panel.contract.asset.registration.RegistrationDetailPanel;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Registration detail pop up panel
 * @author uhout.cheng
 */
public class RegistrationDetailPopup extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 8827334630421389731L;

	private RegistrationDetailPanel registrationDetailPanel;
	private AutoDateField dfArrivalDate;
	private AutoDateField dfRegistrationDate;
	private ERefDataComboBox<ERegBookStatus> cbxRegistrationType;
	private TextField txtRegistrationNO;
	private TextField txtManufacturingYear;
	private EntityRefComboBox<Province> cbxProvince;
	private Asset asset;
	private AssetPanel assetPanel;
	
	/**
	 * 
	 * @param registrationDetailPanel
	 * @param assetPanel
	 * @param loanSummaryPanel
	 */
	public RegistrationDetailPopup() {
		setCaption(I18N.message("registration.details"));
		setModal(true);
		setResizable(false);
		dfArrivalDate = ComponentFactory.getAutoDateField("arrival.date", false);
		dfRegistrationDate = ComponentFactory.getAutoDateField("registration.date", false);
		txtRegistrationNO = ComponentFactory.getTextField("registration.no", false, 60, 170);
		txtManufacturingYear = ComponentFactory.getTextField("manufacturing.year", false, 60, 170);
		cbxRegistrationType = new ERefDataComboBox<>(I18N.message("registration.type"), ERegBookStatus.values());
		cbxRegistrationType.setWidth(170, Unit.PIXELS);
		cbxRegistrationType.setImmediate(true);
		cbxProvince = new EntityRefComboBox<Province>(I18N.message("province"));
		cbxProvince.setWidth(170, Unit.PIXELS);
		cbxProvince.setRestrictions(new BaseRestrictions<>(Province.class));
		cbxProvince.renderer();
		init();
	}
	
	/**
	 * 
	 * @param asset
	 * @param registrationDetailPanel
	 * @param assetPanel
	 * @param loanSummaryPanel
	 */
	public void assignValues(Asset asset, RegistrationDetailPanel registrationDetailPanel, AssetPanel assetPanel) {
		this.registrationDetailPanel = registrationDetailPanel;
		this.assetPanel = assetPanel;
		if (asset != null) {
			this.asset = asset;
			dfArrivalDate.setValue(null);
			dfRegistrationDate.setValue(asset.getRegistrationDate());
			cbxRegistrationType.setSelectedEntity(asset.getRegistrationBookStatus());
			txtRegistrationNO.setValue(asset.getPlateNumber());
			txtManufacturingYear.setValue(String.valueOf(MyNumberUtils.getInteger(asset.getYear())));
			cbxProvince.setSelectedEntity(asset.getRegistrationProvince());
		}
	}
	
	/**
	 * 
	 */
	private void init() {
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -6843639557959431904L;

			public void buttonClick(ClickEvent event) {
				asset.setRegistrationDate(dfRegistrationDate.getValue());
				asset.setRegistrationBookStatus(cbxRegistrationType.getSelectedEntity());
				asset.setPlateNumber(txtRegistrationNO.getValue());
				asset.setYear(MyNumberUtils.getInteger(txtManufacturingYear.getValue(), 0));
				asset.setRegistrationProvince(cbxProvince.getSelectedEntity());
				ENTITY_SRV.saveOrUpdate(asset);
//				registrationDetailPanel.assignValues(asset, assetPanel);
				assetPanel.assignValuesToAsset(asset);
				close();
			}
		});
		btnSave.setIcon(FontAwesome.SAVE);
	     
		Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -2587413676820805788L;

			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		btnCancel.setIcon(FontAwesome.BAN);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(getVerLayout());
	    
		setContent(contentLayout);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getVerLayout() {
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		frmLayout.addComponent(dfArrivalDate);
		frmLayout.addComponent(dfRegistrationDate);
		frmLayout.addComponent(cbxRegistrationType);
		frmLayout.addComponent(txtRegistrationNO);
		frmLayout.addComponent(txtManufacturingYear);
		frmLayout.addComponent(cbxProvince);
		verLayout.addComponent(frmLayout);
		return verLayout;
	}
	
}

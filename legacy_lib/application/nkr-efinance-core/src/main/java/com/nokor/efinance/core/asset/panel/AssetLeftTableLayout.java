package com.nokor.efinance.core.asset.panel;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Asset left table layout more detail
 * @author uhout.cheng
 */
public class AssetLeftTableLayout extends CustomLayout implements ValueChangeListener {

	/** */
	private static final long serialVersionUID = 6707802783181696832L;

	private static String OPEN_TABLE_HAS_BORDER = "<table cellspacing=\"3\" cellpadding=\"3\" style=\"border:1px solid black; border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"120px\" bgcolor=\"#e1e1e1\" "
			+ "style=\"border:1px solid black;\" >";
	private static String OPEN_TD = "<td align=\"left\" style=\"border:1px solid black;\" >";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	private Label lblBranchValue;
	private Label lblEngineNoValue;
	private Label lblChassisNoValue;
	private Label lblColorValue;
	private TextField txtMileageValue;
	private CheckBox cbRegYes;
	private CheckBox cbRegNo;
	private CheckBox cbRegB;
	private CheckBox cbAOMYes;
	private CheckBox cbAOMNo;
	private CheckBox cbEngineYes;
	private CheckBox cbEngineNo;
	private CheckBox cbMobilYes;
	private CheckBox cbMobilNo;
	private CheckBox cbCDIYes;
	private CheckBox cbCDINo;
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(cbRegYes)) {
			if (cbRegYes.getValue()) {
				cbRegNo.setValue(false);
				cbRegB.setValue(false);
			}
		} else if (event.getProperty().equals(cbRegNo)) {
			if (cbRegNo.getValue()) {
				cbRegYes.setValue(false);
				cbRegB.setValue(false);
			}
		} else if (event.getProperty().equals(cbRegB)) {
			if (cbRegB.getValue()) {
				cbRegNo.setValue(false);
				cbRegYes.setValue(false);
			}
		}
		if (event.getProperty().equals(cbAOMYes)) {
			if (cbAOMYes.getValue()) {
				cbAOMNo.setValue(false);
			}
		}  else if (event.getProperty().equals(cbAOMNo)) {
			if (cbAOMNo.getValue()) {
				cbAOMYes.setValue(false);
			}
		}
		if (event.getProperty().equals(cbEngineYes)) {
			if (cbEngineYes.getValue()) {
				cbEngineNo.setValue(false);
			}
		}  else if (event.getProperty().equals(cbEngineNo)) {
			if (cbEngineNo.getValue()) {
				cbEngineYes.setValue(false);
			}
		}
		if (event.getProperty().equals(cbMobilYes)) {
			if (cbMobilYes.getValue()) {
				cbMobilNo.setValue(false);
			}
		}  else if (event.getProperty().equals(cbMobilNo)) {
			if (cbMobilNo.getValue()) {
				cbMobilYes.setValue(false);
			}
		}
		if (event.getProperty().equals(cbCDIYes)) {
			if (cbCDIYes.getValue()) {
				cbCDINo.setValue(false);
			}
		}  else if (event.getProperty().equals(cbCDINo)) {
			if (cbCDINo.getValue()) {
				cbCDIYes.setValue(false);
			}
		}
	}
	
	/** */
	public AssetLeftTableLayout() {
		List<String> locations = new ArrayList<String>();
		locations.add("<div location =\"lblYes\" />");
		locations.add("<div location =\"lblNo\" />");
		locations.add("<div location =\"lblB\" />");
		String template = OPEN_TABLE_HAS_BORDER;
		template += OPEN_TR;
		template += OPEN_TH + CLOSE_TH;
		template += OPEN_TH + CLOSE_TH;
		for (String string : locations) {
			template += "<th class=\"align-center\" bgcolor=\"#e1e1e1\" style=\"border:1px solid black;\" >";
			template += string;
			template += CLOSE_TH;
		}
		template += CLOSE_TR;
		
		locations = new ArrayList<String>();
		locations.add("<div location =\"lblBrandTitle\" />");
		locations.add("<div location =\"lblBrandValue\" />");
		
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		template += CLOSE_TR;
		
		locations = new ArrayList<String>();
		locations.add("<div location =\"lblEngineNoTitle\" />");
		locations.add("<div location =\"lblEngineNoValue\" />");
		
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		template += CLOSE_TR;

		locations = new ArrayList<String>();
		locations.add("<div location =\"lblChassisNoTitle\" />");
		locations.add("<div location =\"lblChassisNoValue\" />");
		
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		template += CLOSE_TR;

		locations = new ArrayList<String>();
		locations.add("<div location =\"lblColorTitle\" />");
		locations.add("<div location =\"lblColorValue\" />");
		
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		template += CLOSE_TR;
		
		locations = new ArrayList<String>();
		locations.add("<div location =\"lblMileageTitle\" />");
		locations.add("<div location =\"txtMileageValue\" />");
		
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		template += CLOSE_TR;

		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblRegistrationPlateTitle\" />";
		template += CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		
		locations = new ArrayList<String>();
		locations.add("<div location =\"cbRegYes\" />");
		locations.add("<div location =\"cbRegNo\" />");
		locations.add("<div location =\"cbRegB\" />");
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += CLOSE_TR;
		
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblAOMTaxNoticeTitle\" />";
		template += CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		locations = new ArrayList<String>();
		locations.add("<div location =\"cbAOMYes\" />");
		locations.add("<div location =\"cbAOMNo\" />");
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += OPEN_TD + CLOSE_TD;
		template += CLOSE_TR;
		
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblEngineStartableTitle\" />";
		template += CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		locations = new ArrayList<String>();
		locations.add("<div location =\"cbEngineYes\" />");
		locations.add("<div location =\"cbEngineNo\" />");
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += OPEN_TD + CLOSE_TD;
		template += CLOSE_TR;
		
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblMobilableTitle\" />";
		template += CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		locations = new ArrayList<String>();
		locations.add("<div location =\"cbMobilYes\" />");
		locations.add("<div location =\"cbMobilNo\" />");
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += OPEN_TD + CLOSE_TD;
		template += CLOSE_TR;
		
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblCDIBoxTitle\" />";
		template += CLOSE_TD;
		template += OPEN_TD + CLOSE_TD;
		locations = new ArrayList<String>();
		locations.add("<div location =\"cbCDIYes\" />");
		locations.add("<div location =\"cbCDINo\" />");
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += OPEN_TD + CLOSE_TD;
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		
		template += "<table border=\"0\" cellspacing=\"2\" cellpadding=\"0\" >";
		template += "<tr>";
		template += "<td>";
		template += "<div location =\"conditionLayout\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		
		lblBranchValue = new Label();
		lblEngineNoValue = new Label();
		lblChassisNoValue = new Label();
		lblColorValue = new Label();
		txtMileageValue = ComponentFactory.getTextField(60, 120);
		cbRegYes = new CheckBox();
		cbRegNo = new CheckBox();
		cbRegB = new CheckBox();
		cbAOMYes = new CheckBox();
		cbAOMNo = new CheckBox();
		cbEngineYes = new CheckBox();
		cbEngineNo = new CheckBox();
		cbMobilYes = new CheckBox();
		cbMobilNo = new CheckBox();
		cbCDIYes = new CheckBox();
		cbCDINo = new CheckBox();
		cbRegYes.addValueChangeListener(this);
		cbRegNo.addValueChangeListener(this);
		cbRegB.addValueChangeListener(this);
		cbAOMYes.addValueChangeListener(this);
		cbAOMNo.addValueChangeListener(this);
		cbEngineYes.addValueChangeListener(this);
		cbEngineNo.addValueChangeListener(this);
		cbMobilYes.addValueChangeListener(this);
		cbMobilNo.addValueChangeListener(this);
		cbCDIYes.addValueChangeListener(this);
		cbCDINo.addValueChangeListener(this);
		
		addComponent(new Label(I18N.message("yes")), "lblYes");
		addComponent(new Label(I18N.message("no")), "lblNo");
		addComponent(new Label(I18N.message("b")), "lblB");
		addComponent(new Label(I18N.message("brand")), "lblBrandTitle");
		addComponent(new Label(I18N.message("engine.no")), "lblEngineNoTitle");
		addComponent(new Label(I18N.message("chassis.no")), "lblChassisNoTitle");
		addComponent(new Label(I18N.message("color")), "lblColorTitle");
		addComponent(new Label(I18N.message("mileage")), "lblMileageTitle");
		addComponent(new Label(I18N.message("registration.plate")), "lblRegistrationPlateTitle");
		addComponent(new Label(I18N.message("aom.tax.notice")), "lblAOMTaxNoticeTitle");
		addComponent(new Label(I18N.message("engine.startable")), "lblEngineStartableTitle");
		addComponent(new Label(I18N.message("mobilable")), "lblMobilableTitle");
		addComponent(new Label(I18N.message("cdi.box")), "lblCDIBoxTitle");
		addComponent(lblBranchValue, "lblBrandValue");
		addComponent(lblEngineNoValue, "lblEngineNoValue");
		addComponent(lblChassisNoValue, "lblChassisNoValue");
		addComponent(lblColorValue, "lblColorValue");
		addComponent(txtMileageValue, "txtMileageValue");
		addComponent(cbRegYes, "cbRegYes");
		addComponent(cbRegNo, "cbRegNo");
		addComponent(cbRegB, "cbRegB");
		addComponent(cbAOMYes, "cbAOMYes");
		addComponent(cbAOMNo, "cbAOMNo");
		addComponent(cbEngineYes, "cbEngineYes");
		addComponent(cbEngineNo, "cbEngineNo");
		addComponent(cbMobilYes, "cbMobilYes");
		addComponent(cbMobilNo, "cbMobilNo");
		addComponent(cbCDIYes, "cbCDIYes");
		addComponent(cbCDINo, "cbCDINo");
		addComponent(getConditionStatusPanel(), "conditionLayout");
		
		setTemplateContents(template);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDefaultString(String value) {
		return ((value == null) ? "" : value);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		if (contract != null) {
			Asset asset = contract.getAsset();
			if (asset != null) {
				lblChassisNoValue.setValue(getDefaultString(asset.getChassisNumber()));
				lblEngineNoValue.setValue(getDefaultString(asset.getEngineNumber()));
				lblColorValue.setValue(getDefaultString(asset.getColor() != null ? 
						getDefaultString(asset.getColor().getDescEn()) : ""));
				AssetModel assetModel = asset.getModel();
				
				if (assetModel != null) {
					AssetRange assetRange = assetModel.getAssetRange();
					if (assetRange != null) {
						lblBranchValue.setValue(assetRange.getAssetMake() == null ? null : 
							getDefaultString(assetRange.getAssetMake().getDescEn()));
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getConditionStatusPanel() {
		VerticalLayout verLayout = ComponentFactory.getVerticalLayout("condition.status");
		verLayout.setSpacing(true);
		CheckBox cbFalled = new CheckBox(I18N.message("falled"));
		CheckBox cbChangedSpare = new CheckBox(I18N.message("changed.spare"));
		CheckBox cbBroken = new CheckBox(I18N.message("broken"));
		CheckBox cbCrashed = new CheckBox(I18N.message("scraped.crashed"));
		CheckBox cbAccidented = new CheckBox(I18N.message("accidented"));
		CheckBox cbTotalLoss = new CheckBox(I18N.message("total.loss"));
		CheckBox cbNoEquipment = new CheckBox(I18N.message("no.equipment"));
		TextArea txtComment = ComponentFactory.getTextArea("comment", false, 335, 70);
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponent(cbFalled);
		layout.addComponent(cbChangedSpare);
		layout.addComponent(cbBroken);
		layout.addComponent(cbCrashed);
		layout.addComponent(cbAccidented);
		layout.addComponent(cbTotalLoss);
		layout.addComponent(cbNoEquipment);
		layout.addComponent(txtComment);
		HorizontalLayout horlLayout = new HorizontalLayout();
		horlLayout.addComponent(layout);
		
		verLayout.addComponent(horlLayout);
		return verLayout;
	}
}

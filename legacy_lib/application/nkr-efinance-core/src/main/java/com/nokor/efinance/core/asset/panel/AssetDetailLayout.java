package com.nokor.efinance.core.asset.panel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Full detail reference in contact tab
 * @author uhout.cheng
 */
public class AssetDetailLayout extends AbstractTabPanel {
	
	/** */
	private static final long serialVersionUID = 1024192686672581262L;
	
	private static String OPEN_TABLE_NONE_BORDER = "<table cellspacing=\"1\" cellpadding=\"1\" style=\"border:0\" >";
	private static String OPEN_TABLE_HAS_BORDER = "<table cellspacing=\"1\" cellpadding=\"1\" style=\"border:1px solid black; border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TD = "</td>";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	private TextField txtContractNo;
	private TextField txtContractStatus;
	private TextField txtName;
	private TextField txtSurName;
	private TextField txtWarehouseLocation;
	
	private Label lblBookingDateValue;
	private Label lblBookingStaffValue;
	private Label lblWarehouseValue;
	private Label lblGradingDateValue;
	private Label lblGradingStaffValue;
	private Label lblTagNoValue;
	private Label lblGradeValue;
	private Label lblCustomerNameValue;
	private Label lblRegistrationNoValue;
	private Label lblChassisNoValue;
	private Label lblEngineNoValue;
	private Label lblBranchValue;
	private Label lblYearValue;
	private Label lblSeriesValue;
	private Label lblModelValue;
	private Label lblCharacteristicsValue;
	private Label lblColorValue;
	private CheckBox cbInternalValue;
	private CheckBox cbExternalValue;
	private CheckBox cbRepoValue;
	private CheckBox cbReturnedValue;
	
	private Label lblDepartmentValue;
	private Label lblRepoDateValue;
	private Label lblRepoStaffCodeValue;
	private Label lblRepoStaffNameValue;
	private Label lblRepoPhoneNumberValue;
	private Label lblDaysSinceRepossessionValue;
	private Label lblDeliveryDateValue;
	private Label lblDeliveryStaffCodeValue;
	private Label lblDeliveryStaffNameValue;
	private Label lblDeliveryPhoneNumberValue;
	private Label lblStageValue;
	
	private AssetLeftTableLayout assetLeftTableLayout;
	private AssetRightTableLayout assetRightTableLayout;

	private Button btnBack;
	
	/**
	 * @return the btnBack
	 */
	public Button getBtnBack() {
		return btnBack;
	}

	/**
	 * @param btnBack the btnBack to set
	 */
	public void setBtnBack(Button btnBack) {
		this.btnBack = btnBack;
	}
	
	/** */
	public AssetDetailLayout() {
		super();
		setSizeFull();
		btnBack = new NativeButton(I18N.message("back"));
		btnBack.setIcon(FontAwesome.STEP_BACKWARD);
		setBtnBack(btnBack);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeFull();
		navigationPanel.addButton(btnBack);
		addComponent(navigationPanel, 0);
	}
	
	/**
	 * 
	 * @return
	 */
	private TextField getTextField() {
		TextField textField = ComponentFactory.getTextField(60, 120);
		return textField;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getPanel(HorizontalLayout layout) {
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		panel.setContent(layout);
		return panel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		assetRightTableLayout = new AssetRightTableLayout();
		HorizontalLayout topHorLayout = new HorizontalLayout();
		topHorLayout.addComponent(getTopInfoLayout());
		Panel topPanel = getPanel(topHorLayout);
		HorizontalLayout middleHorLayout = new HorizontalLayout();
		middleHorLayout.addComponent(getMiddlePanel());
		Panel middlePanel = getPanel(middleHorLayout);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.addComponent(topPanel);
		mainLayout.addComponent(middlePanel);
		
		return mainLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		if (contract != null) {
			if (contract != null) {
				txtContractNo.setValue(getDefaultString(contract.getReference()));
				txtContractStatus.setValue(contract.getWkfStatus() != null ? getDefaultString(contract.getWkfStatus().getDescEn()) : "");
			}
	
			lblBookingDateValue.setValue(DateUtils.getDateLabel(contract.getBookingDate(), DateUtils.FORMAT_DDMMYYYY_SLASH));
			lblBookingStaffValue.setValue(contract.getCreateUser());
			lblWarehouseValue.setValue("");
			lblGradingDateValue.setValue("");
			lblGradingStaffValue.setValue("");
			lblTagNoValue.setValue("");
			Asset asset = contract.getAsset();
			if (asset != null) {
				lblGradeValue.setValue(getDefaultString(asset.getGrade()));
				lblRegistrationNoValue.setValue(getDefaultString(asset.getPlateNumber()));
				lblChassisNoValue.setValue(getDefaultString(asset.getChassisNumber()));
				lblEngineNoValue.setValue(getDefaultString(asset.getEngineNumber()));
				lblYearValue.setValue(getDefaultString(asset.getYear()));
				lblColorValue.setValue(getDefaultString(asset.getColor() != null ? 
						getDefaultString(asset.getColor().getDescEn()) : ""));
				AssetModel assetModel = asset.getModel();
				
				if (assetModel != null) {
					AssetRange assetRange = assetModel.getAssetRange();
					if (assetRange != null) {
						lblBranchValue.setValue(assetRange.getAssetMake() == null ? null : 
							getDefaultString(assetRange.getAssetMake().getDescEn()));
					}
					lblSeriesValue.setValue(assetModel.getAssetRange() == null ? null : 
						getDefaultString(assetModel.getAssetRange().getDescEn()));
					lblModelValue.setValue(getDefaultString(assetModel.getDescEn()));
				}
			}
			Applicant applicant = contract.getApplicant();
			if (applicant != null && applicant.getIndividual() != null) {
				lblCustomerNameValue.setValue(getFullName(applicant.getIndividual().getLastNameEn(), applicant.getIndividual().getFirstNameEn()));
			}
			lblCharacteristicsValue.setValue("");
			cbInternalValue.setValue(true);
			cbExternalValue.setValue(false);
			cbRepoValue.setValue(true);
			cbReturnedValue.setValue(false);
			assetLeftTableLayout.assignValues(contract);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getMiddlePanel() {
		CustomLayout customLayout = new CustomLayout("xxx");
		List<String> locations = new ArrayList<String>();
		locations.add("<div location =\"assetLeftTableLayout\" />");
		locations.add("<div location =\"assetRightTableLayout\" />");
		locations.add("<div location =\"uplodadImagePanel\" />");
		String template = OPEN_TABLE_NONE_BORDER;
		template += OPEN_TR;
		for (String string : locations) {
			template += "<td valign=\"top\" >";
			template += string;
			template += CLOSE_TD;
		}
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		customLayout.addComponent(getAssetLeft(), "assetLeftTableLayout");
		customLayout.addComponent(getAssetRight(), "assetRightTableLayout");
		customLayout.addComponent(getUploadImage(), "uplodadImagePanel");
		customLayout.setTemplateContents(template);

		VerticalLayout verLayout = new VerticalLayout();
		verLayout.addComponent(customLayout);
		return verLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getAssetLeft() {
		assetLeftTableLayout = new AssetLeftTableLayout();
		
		CustomLayout customLayout = new CustomLayout("xxx");
		List<String> locations = new ArrayList<String>();
		locations.add("<div location =\"assetLeftTableLayout\" />");
		String template = OPEN_TABLE_NONE_BORDER;
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		customLayout.addComponent(assetLeftTableLayout, "assetLeftTableLayout");
		customLayout.setTemplateContents(template);
		return customLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getAssetRight() {
		final String OPEN_TABLE = "<table width=\"800px\" cellspacing=\"1\" cellpadding=\"1\" style=\"border:0\" >";
		
		assetRightTableLayout = new AssetRightTableLayout();
		
		CustomLayout customLayout = new CustomLayout("xxx");
		List<String> locations = new ArrayList<String>();
		locations.add("<div location =\"assetRightTableLayout\" />");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		customLayout.addComponent(assetRightTableLayout, "assetRightTableLayout");
		customLayout.setTemplateContents(template);
		return customLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getUploadImage() {
		CustomLayout customLayout = new CustomLayout("xxx");
		List<String> locations = new ArrayList<String>();
		locations.add("<div location =\"uplodadImagePanel\" />");
		String template = OPEN_TABLE_HAS_BORDER;
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		customLayout.addComponent(uploadImagePanel(), "uplodadImagePanel");
		customLayout.setTemplateContents(template);
		return customLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getTopInfoLayout() {
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		verLayout.addComponent(getContractLayoutDetail());
		verLayout.addComponent(getBookingAssetTableDetail());
		verLayout.addComponent(getDepartmentTableDetail());
		return verLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getContractLayoutDetail() {
		String OPEN_TD = "<td align=\"left\" >";
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE_NONE_BORDER;
		template += OPEN_TR;
		template += "<td align=\"left\" width=\"150\" >";
		template += "<div location =\"lblContractNo\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtContractNo\" />";
		template += CLOSE_TD;
		template += "<td align=\"left\" width=\"290\" >";
		template += "<div location =\"lblContractStatus\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtContractStatus\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblName\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += "<td align=\"left\" width=\"80\" >";
		template += "<div location =\"txtName\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblSurName\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtSurName\" />";
		template += CLOSE_TD;
		template += "<td align=\"left\" width=\"380\" >";
		template += "<div location =\"lblWarehouseLocation\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtWarehouseLocation\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		
		txtContractNo = getTextField();
		txtContractStatus = getTextField();
		txtName = getTextField();
		txtSurName = getTextField();
		txtWarehouseLocation = getTextField();
		
		cusLayout.addComponent(new Label(I18N.message("contract.no")), "lblContractNo");
		cusLayout.addComponent(new Label(I18N.message("contract.status")), "lblContractStatus");
		cusLayout.addComponent(new Label(I18N.message("name.en")), "lblName");
		cusLayout.addComponent(new Label(I18N.message("surname")), "lblSurName");
		cusLayout.addComponent(new Label(I18N.message("warehouse.location")), "lblWarehouseLocation");
		cusLayout.addComponent(txtContractNo, "txtContractNo");
		cusLayout.addComponent(txtContractStatus, "txtContractStatus");
		cusLayout.addComponent(txtName, "txtName");
		cusLayout.addComponent(txtSurName, "txtSurName");
		cusLayout.addComponent(txtWarehouseLocation, "txtWarehouseLocation");
		
		template += CLOSE_TABLE;
		cusLayout.setTemplateContents(template);
		return cusLayout;
	}
	
	/**
	 * 
	 * @param lastName
	 * @param firstName
	 */
	private String getFullName(String lastName, String firstName) {
		StringBuffer referenceName = new StringBuffer(); 
		referenceName.append(lastName);
		referenceName.append(" ");
		referenceName.append(firstName);
		return referenceName.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getBookingAssetTableDetail() {
		String OPEN_TH = "<th rowspan=\"2\" class=\"align-center\" width=\"130px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black\" >";
		String OPEN_TD = "<td class=\"align-center\" style=\"border:1px solid black;\" >";
		
		CustomLayout cusLayout = new CustomLayout("xxx");
		
		List<String> locations = new ArrayList<String>();
		locations.add("<div location =\"lblBookingDate\" />");
		locations.add("<div location =\"lblBookingStaff\" />");
		locations.add("<div location =\"lblWarehouse\" />");
		locations.add("<div location =\"lblGradingDate\" />");
		locations.add("<div location =\"lblGradingStaff\" />");
		locations.add("<div location =\"lblTagNo\" />");
		locations.add("<div location =\"lblGrade\" />");
		locations.add("<div location =\"lblCustomerName\" />");
		locations.add("<div location =\"lblRegistrationNo\" />");
		locations.add("<div location =\"lblChassisNo\" />");
		locations.add("<div location =\"lblEngineNo\" />");
		locations.add("<div location =\"lblBranch\" />");
		locations.add("<div location =\"lblYear\" />");
		locations.add("<div location =\"lblSeries\" />");
		locations.add("<div location =\"lblModel\" />");
		locations.add("<div location =\"lblCharacteristics\" />");
		locations.add("<div location =\"lblColor\" />");
		
		String template = OPEN_TABLE_HAS_BORDER;
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TH;
			template += string;
			template += CLOSE_TH;
		}
		template += CLOSE_TH;
		
		template += "<th colspan=\"2\" class=\"align-center\" bgcolor=\"#e1e1e1\" style=\"border:1px solid black;\" >";
		template += "<div location =\"lblSource\" />";
		template += CLOSE_TH;
		template += "<th colspan=\"2\" class=\"align-center\" bgcolor=\"#e1e1e1\" style=\"border:1px solid black;\" >";
		template += "<div location =\"lblChannel\" />";
		template += CLOSE_TH;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += "<th class=\"align-center\" bgcolor=\"#e1e1e1\" style=\"border:1px solid black;\" >";
		template += "<div location =\"lblInternal\" />";
		template += CLOSE_TH;
		template += "<th class=\"align-center\" bgcolor=\"#e1e1e1\" style=\"border:1px solid black;\" >";
		template += "<div location =\"lblExternal\" />";
		template += CLOSE_TH;
		template += "<th class=\"align-center\" bgcolor=\"#e1e1e1\" style=\"border:1px solid black;\" >";
		template += "<div location =\"lblRepo\" />";
		template += CLOSE_TH;
		template += "<th class=\"align-center\" bgcolor=\"#e1e1e1\" style=\"border:1px solid black;\" >";
		template += "<div location =\"lblReturned\" />";
		template += CLOSE_TH;
		template += CLOSE_TR;
		
		cusLayout.addComponent(new Label(I18N.message("booking.date")), "lblBookingDate");
		cusLayout.addComponent(new Label(I18N.message("booking.staff")), "lblBookingStaff");
		cusLayout.addComponent(new Label(I18N.message("warehouse")), "lblWarehouse");
		cusLayout.addComponent(new Label(I18N.message("grading.date")), "lblGradingDate");
		cusLayout.addComponent(new Label(I18N.message("grading.staff")), "lblGradingStaff");
		cusLayout.addComponent(new Label(I18N.message("tag.no")), "lblTagNo");
		cusLayout.addComponent(new Label(I18N.message("grade")), "lblGrade");
		cusLayout.addComponent(new Label(I18N.message("customer.name")), "lblCustomerName");
		cusLayout.addComponent(new Label(I18N.message("registration.no")), "lblRegistrationNo");
		cusLayout.addComponent(new Label(I18N.message("chassis.no")), "lblChassisNo");
		cusLayout.addComponent(new Label(I18N.message("engine.no")), "lblEngineNo");
		cusLayout.addComponent(new Label(I18N.message("branch")), "lblBranch");
		cusLayout.addComponent(new Label(I18N.message("year")), "lblYear");
		cusLayout.addComponent(new Label(I18N.message("series")), "lblSeries");
		cusLayout.addComponent(new Label(I18N.message("model")), "lblModel");
		cusLayout.addComponent(new Label(I18N.message("characteristics")), "lblCharacteristics");
		cusLayout.addComponent(new Label(I18N.message("color")), "lblColor");
		cusLayout.addComponent(new Label(I18N.message("source")), "lblSource");
		cusLayout.addComponent(new Label(I18N.message("channel")), "lblChannel");
		cusLayout.addComponent(new Label(I18N.message("internal")), "lblInternal");
		cusLayout.addComponent(new Label(I18N.message("external")), "lblExternal");
		cusLayout.addComponent(new Label(I18N.message("repo")), "lblRepo");
		cusLayout.addComponent(new Label(I18N.message("returned")), "lblReturned");
		
		locations = new ArrayList<String>();
		locations.add("<div location =\"lblBookingDateValue\" />");
		locations.add("<div location =\"lblBookingStaffValue\" />");
		locations.add("<div location =\"lblWarehouseValue\" />");
		locations.add("<div location =\"lblGradingDateValue\" />");
		locations.add("<div location =\"lblGradingStaffValue\" />");
		locations.add("<div location =\"lblTagNoValue\" />");
		locations.add("<div location =\"lblGradeValue\" />");
		locations.add("<div location =\"lblCustomerNameValue\" />");
		locations.add("<div location =\"lblRegistrationNoValue\" />");
		locations.add("<div location =\"lblChassisNoValue\" />");
		locations.add("<div location =\"lblEngineNoValue\" />");
		locations.add("<div location =\"lblBranchValue\" />");
		locations.add("<div location =\"lblYearValue\" />");
		locations.add("<div location =\"lblSeriesValue\" />");
		locations.add("<div location =\"lblModelValue\" />");
		locations.add("<div location =\"lblCharacteristicsValue\" />");
		locations.add("<div location =\"lblColorValue\" />");
		locations.add("<div location =\"cbInternalValue\" />");
		locations.add("<div location =\"cbExternalValue\" />");
		locations.add("<div location =\"cbRepoValue\" />");
		locations.add("<div location =\"cbReturnedValue\" />");
		
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		
		lblBookingDateValue = new Label();
		lblBookingStaffValue = new Label();
		lblWarehouseValue = new Label();
		lblGradingDateValue = new Label();
		lblGradingStaffValue = new Label();
		lblTagNoValue = new Label();
		lblGradeValue = new Label();
		lblCustomerNameValue = new Label();
		lblRegistrationNoValue = new Label();
		lblChassisNoValue = new Label();
		lblEngineNoValue = new Label();
		lblBranchValue = new Label();
		lblYearValue = new Label();
		lblSeriesValue = new Label();
		lblModelValue = new Label();
		lblCharacteristicsValue = new Label();
		lblColorValue = new Label();
		cbInternalValue = new CheckBox();
		cbExternalValue = new CheckBox();
		cbRepoValue = new CheckBox();
		cbReturnedValue = new CheckBox();
		
		cusLayout.addComponent(lblBookingDateValue, "lblBookingDateValue");
		cusLayout.addComponent(lblBookingStaffValue, "lblBookingStaffValue");
		cusLayout.addComponent(lblWarehouseValue, "lblWarehouseValue");
		cusLayout.addComponent(lblGradingDateValue, "lblGradingDateValue");
		cusLayout.addComponent(lblGradingStaffValue, "lblGradingStaffValue");
		cusLayout.addComponent(lblTagNoValue, "lblTagNoValue");
		cusLayout.addComponent(lblGradeValue, "lblGradeValue");
		cusLayout.addComponent(lblCustomerNameValue, "lblCustomerNameValue");
		cusLayout.addComponent(lblRegistrationNoValue, "lblRegistrationNoValue");
		cusLayout.addComponent(lblChassisNoValue, "lblChassisNoValue");
		cusLayout.addComponent(lblEngineNoValue, "lblEngineNoValue");
		cusLayout.addComponent(lblBranchValue, "lblBranchValue");
		cusLayout.addComponent(lblYearValue, "lblYearValue");
		cusLayout.addComponent(lblSeriesValue, "lblSeriesValue");
		cusLayout.addComponent(lblModelValue, "lblModelValue");
		cusLayout.addComponent(lblCharacteristicsValue, "lblCharacteristicsValue");
		cusLayout.addComponent(lblColorValue, "lblColorValue");
		cusLayout.addComponent(cbInternalValue, "cbInternalValue");
		cusLayout.addComponent(cbExternalValue, "cbExternalValue");
		cusLayout.addComponent(cbRepoValue, "cbRepoValue");
		cusLayout.addComponent(cbReturnedValue, "cbReturnedValue");
		
		cusLayout.setTemplateContents(template);
		
		return cusLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getDepartmentTableDetail() {
		String OPEN_TH = "<th class=\"align-center\" width=\"150px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		String OPEN_TD = "<td class=\"align-center\" style=\"border:1px solid black;\" >";
		
		CustomLayout cusLayout = new CustomLayout("xxx");
		
		List<String> locations = new ArrayList<String>();
		locations.add("<div location =\"lblDepartment\" />");
		locations.add("<div location =\"lblRepoDate\" />");
		locations.add("<div location =\"lblRepoStaffCode\" />");
		locations.add("<div location =\"lblRepoStaffName\" />");
		locations.add("<div location =\"lblRepoPhoneNumber\" />");
		locations.add("<div location =\"lblDaysSinceRepossession\" />");
		locations.add("<div location =\"lblDeliveryDate\" />");
		locations.add("<div location =\"lblDeliveryStaffCode\" />");
		locations.add("<div location =\"lblDeliveryStaffName\" />");
		locations.add("<div location =\"lblDeliveryPhoneNumber\" />");
		locations.add("<div location =\"lblStage\" />");
		
		String template = OPEN_TABLE_HAS_BORDER;
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TH;
			template += string;
			template += CLOSE_TH;
		}
		template += CLOSE_TR;
		
		cusLayout.addComponent(new Label(I18N.message("department")), "lblDepartment");
		cusLayout.addComponent(new Label(I18N.message("repo.date")), "lblRepoDate");
		cusLayout.addComponent(new Label(I18N.message("repo.staff.code")), "lblRepoStaffCode");
		cusLayout.addComponent(new Label(I18N.message("repo.staff.name")), "lblRepoStaffName");
		cusLayout.addComponent(new Label(I18N.message("repo.phone.number")), "lblRepoPhoneNumber");
		cusLayout.addComponent(new Label(I18N.message("day.since.repossession")), "lblDaysSinceRepossession");
		cusLayout.addComponent(new Label(I18N.message("delivery.date")), "lblDeliveryDate");
		cusLayout.addComponent(new Label(I18N.message("delivery.staff.code")), "lblDeliveryStaffCode");
		cusLayout.addComponent(new Label(I18N.message("delivery.staff.name")), "lblDeliveryStaffName");
		cusLayout.addComponent(new Label(I18N.message("delivery.phone.number")), "lblDeliveryPhoneNumber");
		cusLayout.addComponent(new Label(I18N.message("stage")), "lblStage");
		
		locations = new ArrayList<String>();
		locations.add("<div location =\"lblDepartmentValue\" />");
		locations.add("<div location =\"lblRepoDateValue\" />");
		locations.add("<div location =\"lblRepoStaffCodeValue\" />");
		locations.add("<div location =\"lblRepoStaffNameValue\" />");
		locations.add("<div location =\"lblRepoPhoneNumberValue\" />");
		locations.add("<div location =\"lblDaysSinceRepossessionValue\" />");
		locations.add("<div location =\"lblDeliveryDateValue\" />");
		locations.add("<div location =\"lblDeliveryStaffCodeValue\" />");
		locations.add("<div location =\"lblDeliveryStaffNameValue\" />");
		locations.add("<div location =\"lblDeliveryPhoneNumberValue\" />");
		locations.add("<div location =\"lblStageValue\" />");
		
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
		}
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		
		lblDepartmentValue = new Label();
		lblRepoDateValue = new Label();
		lblRepoStaffCodeValue = new Label();
		lblRepoStaffNameValue = new Label();
		lblRepoPhoneNumberValue = new Label();
		lblDaysSinceRepossessionValue = new Label();
		lblDeliveryDateValue = new Label();
		lblDeliveryStaffCodeValue = new Label();
		lblDeliveryStaffNameValue = new Label();
		lblDeliveryPhoneNumberValue = new Label();
		lblStageValue = new Label();
		
		lblDepartmentValue.setValue("");
		lblRepoDateValue.setValue("");
		lblRepoStaffCodeValue.setValue("");
		lblRepoStaffNameValue.setValue("");
		lblRepoPhoneNumberValue.setValue("");
		lblDaysSinceRepossessionValue.setValue("");
		lblDeliveryDateValue.setValue("");
		lblDeliveryStaffCodeValue.setValue("");
		lblDeliveryStaffNameValue.setValue("");
		lblDeliveryPhoneNumberValue.setValue("");
		lblStageValue.setValue("");
		
		cusLayout.addComponent(lblDepartmentValue, "lblDepartmentValue");
		cusLayout.addComponent(lblRepoDateValue, "lblRepoDateValue");
		cusLayout.addComponent(lblRepoStaffCodeValue, "lblRepoStaffCodeValue");
		cusLayout.addComponent(lblRepoStaffNameValue, "lblRepoStaffNameValue");
		cusLayout.addComponent(lblRepoPhoneNumberValue, "lblRepoPhoneNumberValue");
		cusLayout.addComponent(lblDaysSinceRepossessionValue, "lblDaysSinceRepossessionValue");
		cusLayout.addComponent(lblDeliveryDateValue, "lblDeliveryDateValue");
		cusLayout.addComponent(lblDeliveryStaffCodeValue, "lblDeliveryStaffCodeValue");
		cusLayout.addComponent(lblDeliveryStaffNameValue, "lblDeliveryStaffNameValue");
		cusLayout.addComponent(lblDeliveryPhoneNumberValue, "lblDeliveryPhoneNumberValue");
		cusLayout.addComponent(lblStageValue, "lblStageValue");
		
		cusLayout.setTemplateContents(template);
		
		return cusLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel uploadImagePanel(){
		final Embedded image = new Embedded(I18N.message("uploaded.image"));
		image.setVisible(false);

		// Implement both receiver that saves upload in a file and
		// listener for successful upload
		class ImageUploader implements Receiver, SucceededListener {
			
			/** */
			private static final long serialVersionUID = 2293515205235931213L;
			
			public File file;
		    
		    public OutputStream receiveUpload(String filename, String mimeType) {
		        // Create upload stream
		        FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		            file = new File(AppConfig.getInstance().getConfiguration().getString("specific.tmpdir") + filename);
		            fos = new FileOutputStream(file);
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("Could not open file<br/>",e.getMessage(),Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
		            return null;
		        }
		        return fos; // Return the output stream to write to
		    }
		    
		    /**
		     * @see com.vaadin.ui.Upload.SucceededListener#uploadSucceeded(com.vaadin.ui.Upload.SucceededEvent)
		     */
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				// Show the uploaded file in the image viewer
		        image.setVisible(true);
		        image.setWidth("300px");
		        image.setHeight("190px");
		        image.setSource(new FileResource(file));
			}
		};
		ImageUploader receiver = new ImageUploader(); 

		// Create the upload with a caption and set receiver later
		Upload upload = new Upload(I18N.message("upload.image.here"), receiver);
		upload.setButtonCaption(I18N.message("start.upload"));
		upload.addSucceededListener(receiver);
		        
		// Put the components in a panel
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.addComponents(upload, image);
		panel.setContent(panelContent);
		return panel;
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		txtContractNo.setValue("");
		txtContractStatus.setValue("");
		txtName.setValue("");
		txtSurName.setValue("");
		txtWarehouseLocation.setValue("");
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {		
		return errors.isEmpty();
	}
}

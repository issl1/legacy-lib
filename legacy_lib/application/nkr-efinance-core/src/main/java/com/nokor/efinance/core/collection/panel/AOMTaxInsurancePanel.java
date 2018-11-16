package com.nokor.efinance.core.collection.panel;


import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.service.DocumentService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

/**
 * AOM tax/insurance tab panel in CM & collection
 * @author uhout.cheng
 */
public class AOMTaxInsurancePanel extends VerticalLayout {

	/** */
	private static final long serialVersionUID = 7218099012556662195L;
	
	private static final String EVIDENCE = "EVIDENCE";

	private static String OPEN_TABLE = "<table cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; "
			+ "border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"100px\" bgcolor=\"#e1e1e1\" "
			+ "style=\"border:1px solid black;\" >";
	private static String OPEN_TD = "<td class=\"align-left\" style=\"border:1px solid black;\" >";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	// AOM tax part
	private AutoDateField dfTaxCalculationDate;
	private AutoDateField dfTaxExpirationDate;
	private AutoDateField dfTaxValidateDate;
	private TextField txtTaxFee;
	private ComboBox txtTaxStatus;
	// AOM insurance part
	private AutoDateField dfInsuranceCalculationDate;
	private AutoDateField dfPeriodInsuranceFrom;
	private AutoDateField dfPeriodInsuranceTo;
	private TextField txtInsurancePolicyNumber;
	private TextField txtInsurancePremium;
	private ComboBox txtTagStatus;
	// Lost insurance part
	private TextField txtCompanyName;
	private AutoDateField dfPeriodLostInsuranceFrom;
	private AutoDateField dfPeriodLostInsuranceTo;
	private TextField txtLostInsurancePolicyNumber;
	private TextField txtLostInsurancePremium;
	private TextField txtSumOfLostInsurance;
	private CheckBox cbLostMotorcycle;
	private CheckBox cbLostBikeCompletely;
	private GridLayout mainLayout;
	
	private boolean isRegistrationTab;
	
	private List<CheckBox> cbDocuments;
	
	/**
	 * 
	 * @param isRegistrationTab
	 */
	public AOMTaxInsurancePanel(boolean isRegistrationTab) {
		this.isRegistrationTab = isRegistrationTab;
		init();
	}
	
	/** */
	public AOMTaxInsurancePanel() {
		init();
	}

	/** */
	private void init() {
		setMargin(true);
		setSpacing(true);
		addComponent(createForm());
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	private Component createForm() {	
		VerticalLayout aomTaxInsuranceLayout = new VerticalLayout();
		aomTaxInsuranceLayout.setSpacing(true);
		aomTaxInsuranceLayout.addComponent(getAOMInsurancePanel());
		aomTaxInsuranceLayout.addComponent(getLostInsurancePanel());
		mainLayout = getGridLayout(3, 2);
		
		if (ProfileUtil.isCollection()) {
			mainLayout.addComponent(getAOMTaxPanel(), 0, 0);
			mainLayout.addComponent(aomTaxInsuranceLayout, 1, 0);
		} else if (ProfileUtil.isCMProfile()) {
			if (isRegistrationTab) {
				mainLayout.addComponent(getAOMTaxPanel(), 0, 0);
			} else {
				HorizontalLayout horLayout = new HorizontalLayout();
				horLayout.setSpacing(true);
				horLayout.addComponent(getAOMInsurancePanel());
				horLayout.addComponent(getLostInsurancePanel());
				
				VerticalLayout verLayout = new VerticalLayout();
				verLayout.setSpacing(true);
				verLayout.setHeight(480, Unit.PIXELS);
				verLayout.addComponent(horLayout);
				verLayout.addComponent(getDailyReportLayout());
				return verLayout;
			} 
		}
		return mainLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabel() {
		Label label = new Label();
		return label;
	}
	
	/** */
	private AutoDateField getAutoDateField() {
		AutoDateField autoDateField = ComponentFactory.getAutoDateField();
		autoDateField.setValue(DateUtils.today());
		return autoDateField;
	}
	
	/**
	 * 
	 * @param col
	 * @param row
	 * @return
	 */
	private GridLayout getGridLayout(int col, int row) {
		GridLayout gridLayout = new GridLayout(col, row);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		return gridLayout;
	}
	
	/**
	 * 
	 * @param caption
	 * @param component
	 * @return
	 */
	private Panel getPanel(String caption, Component component) {
		Panel panel = new Panel(I18N.message(caption));
		panel.setContent(component);
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		return panel;
	}

	/**
	 * 
	 * @return
	 */
	private VerticalLayout getDailyReportLayout() {
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		horLayout.setCaptionAsHtml(true);
		horLayout.setCaption("<B>" + I18N.message("daily.reports") + "</B>");
		horLayout.addComponent(getDailyReportTable());
		horLayout.addComponent(getDocumentCheckListLayout());
		VerticalLayout verLayout = new VerticalLayout();
		Button btnPrint = new Button(I18N.message("print"));
		btnPrint.setIcon(FontAwesome.PRINT);
		verLayout.addComponent(horLayout);
		verLayout.addComponent(btnPrint);
		verLayout.setComponentAlignment(btnPrint, Alignment.BOTTOM_RIGHT);
		return verLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getDocumentCheckListLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setSizeUndefined();
		
		DocumentService documentService = SpringUtils.getBean(DocumentService.class);
		cbDocuments = new ArrayList<CheckBox>();
		List<Document> documentEvidences = documentService.getDocumentByGroups(EVIDENCE);
		if (documentEvidences != null && !documentEvidences.isEmpty()) {
			int i = 1;
			GridLayout gridLayout = new GridLayout(2, documentEvidences.size() + 1);
        	gridLayout.setSpacing(true);
        	gridLayout.addComponent(new Label("<h3>" + documentEvidences.get(0).getDocumentGroup().getDescEn() + "</h3>", ContentMode.HTML), 0, 0);
			for (Document document : documentEvidences) {
				CheckBox cbDocument = new CheckBox();
	    		cbDocument.setCaption(document.getDescEn());
	    		cbDocument.setData(document);
	    		cbDocuments.add(cbDocument);
	    		gridLayout.addComponent(cbDocument, 0, i);
	    		i++;
			}
			layout.addComponent(gridLayout);
		}
		return layout;
	}
	
	/**
	 * 
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {			
		List<QuotationDocument> documents = quotation.getQuotationDocuments();
		if (cbDocuments != null && !cbDocuments.isEmpty()) {
			for (int i = 0; i < cbDocuments.size(); i++) {
				CheckBox cbDocument = cbDocuments.get(i);
				Document document = (Document) cbDocument.getData();
				boolean found = false;
				if (documents != null) {
					for (QuotationDocument quotationDocument : documents) {
						if (document.getId().equals(quotationDocument.getDocument().getId())) {
							cbDocument.setValue(true);
							found = true;
							break;
						}
					}
				}
				if (!found) {
					cbDocument.setValue(false);	
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getDailyReportTable() {
		List<String> locations = new ArrayList<String>();
		locations.add("<div location =\"lblDailyReportCodeTitle\" />");
		locations.add("<div location =\"lblCrimeReportCodeTitle\" />");
		locations.add("<div location =\"lblReportDateTitle\" />");
		locations.add("<div location =\"btnAdd\" />");
		
		CustomLayout customLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TH;
			template += string;
			template += CLOSE_TH;
		}
		template += CLOSE_TR;
	
		Button btnAdd = getButton("add", FontAwesome.PLUS);
		
		customLayout.addComponent(new Label(I18N.message("daily.report.code")), "lblDailyReportCodeTitle");
		customLayout.addComponent(new Label(I18N.message("crime.report.code")), "lblCrimeReportCodeTitle");
		customLayout.addComponent(new Label(I18N.message("date")), "lblReportDateTitle");
		customLayout.addComponent(btnAdd, "btnAdd");
	
		setDailyReportTableData(customLayout, template, locations);
		
		return customLayout;
	}
	
	/**
	 * 
	 * @param customLayout
	 * @param template
	 * @param locations
	 */
	private void setDailyReportTableData(CustomLayout customLayout, String template, List<String> locations) {
		for (int i = 0; i < 5; i++) {
			locations = new ArrayList<String>();
			locations.add("<div location =\"lblDailyReportCodeValue" + i + "\" />");
			locations.add("<div location =\"lblCrimeReportCodeValue" + i + "\" />");
			locations.add("<div location =\"lblReportDateValue" + i + "\" />");
			locations.add("<div location =\"btnLayout" + i + "\" />");
			template += OPEN_TR;
			for (String string : locations) {
				template += OPEN_TD;
				template += string;
				template += CLOSE_TD;
			}
			template += CLOSE_TR;
			
			Label lblDailyReportCodeValue = getLabel();
			Label lblCrimeReportCodeValue = getLabel();
			Label lblReportDateValue = getLabel();
			Button btnEdit = getButton("edit", FontAwesome.EDIT);
			Button btnDelete = getButton("delete", FontAwesome.TRASH_O);
			btnEdit.addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = -8983258521798277908L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					UI.getCurrent().addWindow(null);
				}
			});
			btnDelete.addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = 5350628319500475627L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
							new String[] {""}), new ConfirmDialog.Listener() {
								
							/** */
							private static final long serialVersionUID = -5465722593314229245L;

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									dialog.close();
									MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
											MessageBox.Icon.INFO, I18N.message("item.deleted.successfully", 
											new String[]{"1"}), Alignment.MIDDLE_RIGHT,
											new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
									mb.show();
					            }
							}
						});
						confirmDialog.setWidth("400px");
						confirmDialog.setHeight("150px");
				}
			});
			HorizontalLayout buttonLayout = new HorizontalLayout();
			buttonLayout.setSpacing(true);
			buttonLayout.addComponent(btnEdit);
			buttonLayout.addComponent(btnDelete);
			
			lblDailyReportCodeValue.setValue("1223-223454");
			lblCrimeReportCodeValue.setValue("234534");
			lblReportDateValue.setValue("21/02/2015");
			customLayout.addComponent(lblDailyReportCodeValue, "lblDailyReportCodeValue" + i);
			customLayout.addComponent(lblCrimeReportCodeValue, "lblCrimeReportCodeValue" + i);
			customLayout.addComponent(lblReportDateValue, "lblReportDateValue" + i);
			customLayout.addComponent(buttonLayout, "btnLayout" + i);
		}
	
		template += CLOSE_TABLE;
		customLayout.setTemplateContents(template);
	}
	
	/**
	 * 
	 * @param caption
	 * @param themeResource
	 * @return
	 */
	/*private Button getButton(String caption, ThemeResource themeResource) {
		Button button = ComponentFactory.getButton(caption);
		button.setIcon(themeResource);
		button.setStyleName(Runo.BUTTON_SMALL);
		return button;
	}*/
	
	/**
	 * 
	 * @param caption
	 * @param themeResource
	 * @return
	 */
	private Button getButton(String caption, Resource style) {
		Button button = ComponentFactory.getButton(caption);
		button.setIcon(style);
		button.setStyleName(Runo.BUTTON_SMALL);
		return button;
	}
	
	/**
	 * AOM tax panel
	 * @return
	 */
	private Panel getAOMTaxPanel() {
		txtTaxFee = ComponentFactory.getTextField(60, 150);
		txtTaxStatus = ComponentFactory.getComboBox(null, null);
		txtTaxStatus.setWidth(150, Unit.PIXELS);
		dfTaxCalculationDate = getAutoDateField();
		dfTaxExpirationDate = getAutoDateField();
		dfTaxValidateDate = getAutoDateField();
		Button btnTaxCalculate = ComponentFactory.getButton("calculate");
		btnTaxCalculate.setStyleName(Runo.BUTTON_SMALL);
		
		GridLayout aomTaxGridLayout = getGridLayout(4, 5);
		int iCol = 0;
		aomTaxGridLayout.addComponent(new Label(I18N.message("tax.calculation.date")), iCol++, 0);
		aomTaxGridLayout.addComponent(dfTaxCalculationDate, iCol++, 0);
		aomTaxGridLayout.addComponent(btnTaxCalculate, iCol++, 0);
		aomTaxGridLayout.addComponent(new Label(" = 4 months 0 day"), iCol++, 0);
		iCol = 0;
		aomTaxGridLayout.addComponent(new Label(I18N.message("tax.expiration.date")), iCol++, 1);
		aomTaxGridLayout.addComponent(dfTaxExpirationDate, iCol++, 1);
		iCol = 0;
		aomTaxGridLayout.addComponent(new Label(I18N.message("tax.validate.date")), iCol++, 2);
		aomTaxGridLayout.addComponent(dfTaxValidateDate, iCol++, 2);
		iCol = 0;
		aomTaxGridLayout.addComponent(new Label(I18N.message("tax.fee")), iCol++, 3);
		aomTaxGridLayout.addComponent(txtTaxFee, iCol++, 3);
		iCol = 0;
		aomTaxGridLayout.addComponent(new Label(I18N.message("tax.status")), iCol++, 4);
		aomTaxGridLayout.addComponent(txtTaxStatus, iCol++, 4);
		
		HorizontalLayout labelLayout = new HorizontalLayout();
		labelLayout.setSpacing(true);
		labelLayout.setMargin(true);
		labelLayout.addComponent(new Label("Total amount of payment = 68859"));
		
		VerticalLayout aomTaxVerticalLayout = new VerticalLayout();
		aomTaxVerticalLayout.setSpacing(true);
		aomTaxVerticalLayout.addComponent(aomTaxGridLayout);
		aomTaxVerticalLayout.addComponent(labelLayout);
		
		Panel taxPanel = getPanel("aom.tax", aomTaxVerticalLayout);
		return taxPanel;
	}
	
	/**
	 * AOM insurance panel
	 * @return
	 */
	private Panel getAOMInsurancePanel() {
		txtInsurancePolicyNumber = ComponentFactory.getTextField(60, 150);
		txtInsurancePremium = ComponentFactory.getTextField(60, 150);
		txtTagStatus = ComponentFactory.getComboBox(null, null);
		txtTagStatus.setWidth(150, Unit.PIXELS);
		dfInsuranceCalculationDate = getAutoDateField();
		dfPeriodInsuranceFrom = getAutoDateField();
		dfPeriodInsuranceTo = getAutoDateField();
		Button btnInsuranceCalculate = ComponentFactory.getButton("calculate");
		btnInsuranceCalculate.setStyleName(Runo.BUTTON_SMALL);
		
		GridLayout aomInsuranceGridLayout = getGridLayout(4, 6);
		int iCol = 0;
		aomInsuranceGridLayout.addComponent(new Label(I18N.message("calculation.date")), iCol++, 0);
		aomInsuranceGridLayout.addComponent(dfInsuranceCalculationDate, iCol++, 0);
		aomInsuranceGridLayout.addComponent(btnInsuranceCalculate, iCol++, 0);
		aomInsuranceGridLayout.addComponent(new Label(" = 4 months 0 day"), iCol++, 0);
		iCol = 0;
		aomInsuranceGridLayout.addComponent(new Label(I18N.message("insurance.policy.no")), iCol++, 1);
		aomInsuranceGridLayout.addComponent(txtInsurancePolicyNumber, iCol++, 1);
		iCol = 0;
		aomInsuranceGridLayout.addComponent(new Label(I18N.message("period.insurance.from")), iCol++, 2);
		aomInsuranceGridLayout.addComponent(dfPeriodInsuranceFrom, iCol++, 2);
		iCol = 0;
		aomInsuranceGridLayout.addComponent(new Label(I18N.message("to")), iCol++, 3);
		aomInsuranceGridLayout.addComponent(dfPeriodInsuranceTo, iCol++, 3);
		iCol = 0;
		aomInsuranceGridLayout.addComponent(new Label(I18N.message("insurance.premium")), iCol++, 4);
		aomInsuranceGridLayout.addComponent(txtInsurancePremium, iCol++, 4);
		iCol = 0;
		aomInsuranceGridLayout.addComponent(new Label(I18N.message("tag.status")), iCol++, 5);
		aomInsuranceGridLayout.addComponent(txtTagStatus, iCol++, 5);
		
		Panel insurancePanel = getPanel("aom.insurance", aomInsuranceGridLayout);
		return insurancePanel;
	}
	
	/**
	 * Lost insurance panel
	 * @return
	 */
	private Panel getLostInsurancePanel() {
		txtCompanyName = ComponentFactory.getTextField(60, 150);
		txtLostInsurancePolicyNumber = ComponentFactory.getTextField(60, 150);
		txtLostInsurancePremium = ComponentFactory.getTextField(60, 150);
		txtSumOfLostInsurance = ComponentFactory.getTextField(60, 150);
		dfPeriodLostInsuranceFrom = ComponentFactory.getAutoDateField();
		dfPeriodLostInsuranceTo = ComponentFactory.getAutoDateField();
		cbLostMotorcycle = new CheckBox(I18N.message("lost.motorcycle"));
		cbLostBikeCompletely = new CheckBox(I18N.message("lost.bike.completely"));	
		GridLayout lostInsuranceLayout = getGridLayout(2, 8);
		int iCol = 0;
		lostInsuranceLayout.addComponent(new Label(I18N.message("company.name")), iCol++, 0);
		lostInsuranceLayout.addComponent(txtCompanyName, iCol++, 0);
		iCol = 0;
		lostInsuranceLayout.addComponent(new Label(I18N.message("insurance.policy.no")), iCol++, 1);
		lostInsuranceLayout.addComponent(txtLostInsurancePolicyNumber, iCol++, 1);
		iCol = 0;
		lostInsuranceLayout.addComponent(new Label(I18N.message("period.insurance.from")), iCol++, 2);
		lostInsuranceLayout.addComponent(dfPeriodLostInsuranceFrom, iCol++, 2);
		iCol = 0;
		lostInsuranceLayout.addComponent(new Label(I18N.message("to")), iCol++, 3);
		lostInsuranceLayout.addComponent(dfPeriodLostInsuranceTo, iCol++, 3);
		iCol = 0;
		lostInsuranceLayout.addComponent(new Label(I18N.message("insurance.premium")), iCol++, 4);
		lostInsuranceLayout.addComponent(txtLostInsurancePremium, iCol++, 4);
		iCol = 0;
		lostInsuranceLayout.addComponent(new Label(I18N.message("sum.insurance")), iCol++, 5);
		lostInsuranceLayout.addComponent(txtSumOfLostInsurance, iCol++, 5);
		iCol = 1;
		lostInsuranceLayout.addComponent(cbLostMotorcycle, iCol++, 6);
		iCol = 1;
		lostInsuranceLayout.addComponent(cbLostBikeCompletely, iCol++, 7);
		
		Panel lostInsurancePanel = getPanel("lost.insurance", lostInsuranceLayout);
		return lostInsurancePanel;
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		if (cbDocuments != null && !cbDocuments.isEmpty()) {
			for (int i = 0; i < cbDocuments.size(); i++) {
				cbDocuments.get(i).setValue(false);
			}
		}
	}
}
package com.nokor.efinance.core.quotation.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.jibx.runtime.JiBXException;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.document.model.DocumentUwGroup;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationExtModule;
import com.nokor.efinance.core.quotation.panel.comment.CommentFormPanel;
import com.nokor.efinance.core.quotation.panel.include.DocumentUwGroupPanel;
import com.nokor.efinance.core.shared.comparator.SortIndexComparator;
import com.nokor.efinance.core.shared.conf.UWScoreConfig;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.third.creditbureau.cbc.XmlBinder;
import com.nokor.efinance.third.creditbureau.cbc.model.response.AccDetail;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Consumer;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Employer;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Response;
import com.nokor.efinance.tools.ui.TooltipUtil;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Credit bureau panel
 * 
 * @author ly.youhort
 */
public class ScoringPanel extends AbstractTabPanel implements QuotationEntityField, ValueChangeListener, ClickListener, FrmkServicesHelper {

	private static final long serialVersionUID = -8911799573866466911L;

	private static final ThemeResource redIcon = new ThemeResource("icons/32/red.png");
	private static final ThemeResource orangeIcon = new ThemeResource("icons/32/orange.png");
	private static final ThemeResource greenIcon = new ThemeResource("icons/32/green.png");
	private static final ThemeResource grayIcon = new ThemeResource("icons/32/gray.png");

	private QuotationService quotationService = SpringUtils.getBean(QuotationService.class);

	private Quotation quotation;

	private double monthlyInstallment = 0;
	private double monthlyInstallmentGuarantor = 0;

	private VerticalLayout liabilityLayout;
	private VerticalLayout documentsLayout;
	private VerticalLayout contactsUwLayout;
	private VerticalLayout contactFieldCheckLayout;

	private Label lblExistingInstallmentScore;
	private Label lblCurrentAddressScore;
	private Label lblMonthlyInstallmentScore;
	private Label lblLengthServiceScore;
	private Label lblEmployerAddressScore;

	private Label lblApplicantRatio;
	private Label lblUnderwriterRatio;
	private Label lblCbInfoRatio;

	private TextField txtUwRevenu;
	private TextField txtUwAllowance;
	private TextField txtUwBusinessExpenses;
	private TextField txtUwNetIncome;
	private TextField txtUwPersonalExpenses;
	private TextField txtUwFamilyExpenses;
	private TextField txtUwLiability;
	private TextField txtUwDisposableIncome;

	private Label lblExistingInstallmentScoreGuarantor;
	private Label lblCurrentAddressScoreGuarantor;
	private Label lblMonthlyInstallmentScoreGuarantor;
	private Label lblLengthServiceScoreGuarantor;
	private Label lblEmployerAddressScoreGuarantor;

	private Label lblApplicantRatioGuarantor;
	private Label lblUnderwriterRatioGuarantor;
	private Label lblCbInfoRatioGuarantor;

	private TextField txtUwRevenuGuarantor;
	private TextField txtUwAllowanceGuarantor;
	private TextField txtUwBusinessExpensesGuarantor;
	private TextField txtUwNetIncomeGuarantor;
	private TextField txtUwPersonalExpensesGuarantor;
	private TextField txtUwFamilyExpensesGuarantor;
	private TextField txtUwLiabilityGuarantor;
	private TextField txtUwDisposableIncomeGuarantor;

	private Label lbltotalTiming;
	private Label lblfmProfileCo;
	private Label lblfmProfileUw;
	private Label lblfmProfileUs;
	private Label lblfmProfileMa;

	private Configuration config;
	private Button btnBackPos;
	private Button btnBackUw;
	private Button btnSave;
	private Button btnApprove;
	private Button btnReject;
	private Button btnRequestFieldCheck;
	private Button btnApproveWithCondition;

	private List<DocumentUwGroupPanel> documentUwGroupsPanel;

	private TabSheet liabilityTabSheet;
	private TabSheet contactsTabSheet;
	private NavigationPanel navigationPanel;
	private NavigationPanel navigationPanelResquestFieldCheck;
	
	private FieldCheckPanel fieldCheckPanel;
	private List<FinService> services;

	public ScoringPanel(UnderwriterPanel underwriterPanel) {
		super();
		setSizeFull();
		config = UWScoreConfig.getInstance().getConfiguration();
		navigationPanel = new NavigationPanel();
		navigationPanelResquestFieldCheck = new NavigationPanel();

		btnBackPos = new NativeButton(I18N.message("back.pos"));
		btnBackPos.addClickListener(this);
		btnBackPos.setIcon(new ThemeResource(
				"../nkr-default/icons/16/previous.png"));
		navigationPanel.addButton(btnBackPos);

		btnBackUw = new NativeButton(I18N.message("back.underwriter"));
		btnBackUw.addClickListener(this);
		btnBackUw.setIcon(new ThemeResource(
				"../nkr-default/icons/16/previous.png"));
		navigationPanel.addButton(btnBackUw);

		btnSave = new NativeButton(I18N.message("save"));
		btnSave.addClickListener(this);
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		navigationPanel.addButton(btnSave);

		btnApprove = new NativeButton(I18N.message("approve"));
		btnApprove.addClickListener(this);
		btnApprove
				.setIcon(new ThemeResource("../nkr-default/icons/16/tick.png"));
		navigationPanel.addButton(btnApprove);

		btnApproveWithCondition = new NativeButton(
				I18N.message("approve.with.condition"));
		btnApproveWithCondition.addClickListener(this);
		btnApproveWithCondition.setIcon(new ThemeResource(
				"../nkr-default/icons/16/tick.png"));
		navigationPanel.addButton(btnApproveWithCondition);

		btnReject = new NativeButton(I18N.message("reject"));
		btnReject
				.setIcon(new ThemeResource("../nkr-default/icons/16/error.png"));
		btnReject.addClickListener(this);
		navigationPanel.addButton(btnReject);

		btnRequestFieldCheck = new NativeButton(
				I18N.message("request.field.check"));
		btnRequestFieldCheck.setIcon(new ThemeResource(
				"../nkr-default/icons/16/back.png"));
		btnRequestFieldCheck.addClickListener(this);
		navigationPanel.addButton(btnRequestFieldCheck);
	}

	/**
	 * @param newStatus
	 */
	public void showCommentFormPanel(String caption,
			final EWkfStatus newStatus, boolean forManager) {
		CommentFormPanel commentFormPanel = new CommentFormPanel(quotation,
				newStatus, forManager, new ClickListener() {
					private static final long serialVersionUID = -8159169476150724593L;

					@Override
					public void buttonClick(ClickEvent event) {
						quotationService.saveUnderwriterDecision(
								getQuotation(quotation), newStatus,
								getComments());
						Notification notification = new Notification("",
								Type.HUMANIZED_MESSAGE);
						if (newStatus == QuotationWkfStatus.APS
								|| newStatus == QuotationWkfStatus.AWS) {
							notification.setDescription(I18N.message(
									"message.to.management",
									String.valueOf(quotation.getId())));
						} else if (newStatus == QuotationWkfStatus.APU
								|| newStatus == QuotationWkfStatus.AWU) {
							notification.setDescription(I18N.message(
									"message.to.underwriter.supervisor",
									String.valueOf(quotation.getId())));
						} else if (newStatus == QuotationWkfStatus.PRO) {
							notification.setDescription(I18N.message(
									"back.message.underwriter",
									String.valueOf(quotation.getId())));
						} else if (newStatus == QuotationWkfStatus.QUO
								|| newStatus == QuotationWkfStatus.RAD
								|| newStatus == QuotationWkfStatus.RFC) {
							notification
									.setDescription(I18N.message(
											"back.message.pos",
											String.valueOf(quotation.getId())));
						} else {
							notification
									.setDescription(I18N.message(
											"reject.proposal",
											String.valueOf(quotation.getId())));
						}
						notification.setDelayMsec(5000);
						notification.show(Page.getCurrent());
						/*underwriterPanel.getQuotationFormPanel()
								.getQuotationsPanel()
								.displayQuotationTablePanel();*/
					}
				});
		commentFormPanel.setCaption(caption);
		commentFormPanel.setWidth(700, Unit.PIXELS);
		commentFormPanel.setHeight(450, Unit.PIXELS);
		UI.getCurrent().addWindow(commentFormPanel);
	}

	/**
	 * @return
	 */
	private List<Comment> getComments() {
		List<Comment> comments = new ArrayList<Comment>();
		if (documentUwGroupsPanel != null && !documentUwGroupsPanel.isEmpty()) {
			for (DocumentUwGroupPanel documentUwGroupPanel : documentUwGroupsPanel) {
				Comment comment = documentUwGroupPanel.getComment();
				if (comment != null) {
					comments.add(comment);
				}
			}
		}
		return comments;
	}

	/**
	 * @param quotation
	 * @return
	 */
	private Quotation getQuotation(Quotation quotation) {

		quotation.setUwRevenuEstimation(getDouble(txtUwRevenu));
		quotation.setUwAllowanceEstimation(getDouble(txtUwAllowance));
		quotation
				.setUwBusinessExpensesEstimation(getDouble(txtUwBusinessExpenses));
		quotation.setUwNetIncomeEstimation(getDouble(txtUwNetIncome));
		quotation
				.setUwPersonalExpensesEstimation(getDouble(txtUwPersonalExpenses));
		quotation.setUwFamilyExpensesEstimation(getDouble(txtUwFamilyExpenses));
		quotation.setUwLiabilityEstimation(getDouble(txtUwLiability));

		// Guarantor
		quotation
				.setUwGuarantorRevenuEstimation(getDouble(txtUwRevenuGuarantor));
		quotation
				.setUwGuarantorAllowanceEstimation(getDouble(txtUwAllowanceGuarantor));
		quotation
				.setUwGuarantorBusinessExpensesEstimation(getDouble(txtUwBusinessExpensesGuarantor));
		quotation
				.setUwGuarantorNetIncomeEstimation(getDouble(txtUwNetIncomeGuarantor));
		quotation
				.setUwGuarantorPersonalExpensesEstimation(getDouble(txtUwPersonalExpensesGuarantor));
		quotation
				.setUwGuarantorFamilyExpensesEstimation(getDouble(txtUwFamilyExpensesGuarantor));
		quotation.setUwGuarantorLiabilityEstimation(getDouble(txtUwLiabilityGuarantor));
		
		return quotation;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {

		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout = new VerticalLayout();
		contentLayout.setSizeFull();
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);

		liabilityLayout = new VerticalLayout();
		liabilityLayout.setMargin(true);

		documentsLayout = new VerticalLayout();
		documentsLayout.setSizeFull();
		documentsLayout.setMargin(true);

		Panel documentsPanel = new Panel(I18N.message("evidences"));
		documentsPanel.setSizeFull();
		documentsPanel.setContent(documentsLayout);
		
		documentUwGroupsPanel = new ArrayList<DocumentUwGroupPanel>();

		contactsUwLayout = new VerticalLayout();
		contactsUwLayout.setHeight("100%");
		contactsUwLayout.setMargin(true);
		contactsUwLayout.setSpacing(true);

		contactFieldCheckLayout = new VerticalLayout();
		contactFieldCheckLayout.setHeight("100%");
		contactFieldCheckLayout.setMargin(true);
		contactFieldCheckLayout.setSpacing(true);

		Panel liabilityContentPanel = new Panel();
		liabilityContentPanel.setContent(liabilityLayout);

		liabilityTabSheet = new TabSheet();
		liabilityTabSheet.setSizeFull();
		liabilityTabSheet.addTab(liabilityContentPanel,
				I18N.message("consistence.and.liability"));
		liabilityTabSheet.addTab(addTiming(), I18N.message("timing"));
		
		contactsTabSheet = new TabSheet();
		contactsTabSheet.addTab(contactsUwLayout, I18N.message("underwriter"));

		Panel contactsPanel = new Panel(I18N.message("contacts"));
		contactsPanel.setSizeFull();
		contactsPanel.setContent(contactsTabSheet);

		contentLayout.addComponent(liabilityTabSheet);
		contentLayout.addComponent(documentsPanel);
		contentLayout.addComponent(contactsPanel);

		return contentLayout;
	}

	/**
	 * Set quotation
	 * 
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {
		this.quotation = quotation;
		btnBackUw.setVisible(ProfileUtil.isUnderwriterSupervisor());

		Map<Long, Long> timing = getTiming(quotation);
		lbltotalTiming.setValue(getTime(getTotalTiming(timing)));
		lblfmProfileCo.setValue(getTime(timing.get(FMProfile.CO)));
		lblfmProfileUw.setValue(getTime(timing.get(FMProfile.UW)));
		lblfmProfileUs.setValue(getTime(timing.get(FMProfile.US)));
		lblfmProfileMa.setValue(getTime(timing.get(FMProfile.MA)));

		addConsistenceAndLiabilityLayout(quotation);
		addDocumentsLayout(quotation);
		addContactsLayout(quotation);
		addFieldCheckLayout(quotation);
	}

	/**
	 * 
	 * @param quotation
	 */
	private Map<Long, Long> getTiming(Quotation quotation) {
		Map<Long, Long> timing = new HashMap<Long, Long>();
		// TODO PYI
//		List<QuotationStatusHistory> quotationStatusHistories = quotationService
//				.getWkfStatusHistories(quotation.getId(),
//						Order.asc("updateDate"));
//		Date start = quotation.getStartCreationDate() != null ? quotation
//				.getStartCreationDate() : DateUtils.today();
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			SecUser secUser = quotationStatusHistory.getUser();
//			Date end = quotationStatusHistory.getCreateDate();
//			long time = 0;
//			if (ProfileUtil.isPOS(secUser)) {
//				time = MyNumberUtils.getLong(timing.get(FMProfile.CO));
//				long diff = end.getTime() - start.getTime();
//				if (diff > 0) {
//					time += diff;
//				}
//				timing.put(FMProfile.CO, time);
//			} else {
//				time = MyNumberUtils.getLong(timing.get(secUser.getDefaultProfile()
//						.getId()));
//				time += end.getTime() - start.getTime();
//				timing.put(secUser.getDefaultProfile().getId(), time);
//			}
//			start = end;
//		}
		return timing;
	}

	/**
	 * @param timing
	 * @return
	 */
	private long getTotalTiming(Map<Long, Long> timing) {
		long total = 0;
		for (Iterator<Long> iter = timing.keySet().iterator(); iter.hasNext();) {
			total += MyNumberUtils.getLong(timing.get(iter.next()));
		}
		return total;
	}

	/**
	 * @param time
	 * @return
	 */
	private String getTime(Long millis) {
		if (millis != null) {
			String s = "" + (millis / 1000) % 60;
			String m = "" + (millis / (1000 * 60)) % 60;
			String h = "" + (millis / (1000 * 60 * 60)) % 24;
			String d = "" + (millis / (1000 * 60 * 60 * 24));
			return d + "d " + h + "h:" + m + "m:" + s + "s";
		}
		return "N/A";
	}

	/**
	 * @return navigationPanel
	 */
	public NavigationPanel getNavigationPanel() {
		return this.navigationPanel;
	}

	public NavigationPanel getButRequestFieldCheck() {
		return this.navigationPanelResquestFieldCheck;

	}

	/**
	 * @return
	 */
	private VerticalLayout addTiming() {

		lbltotalTiming = new Label();
		lblfmProfileCo = new Label();
		lblfmProfileUw = new Label();
		lblfmProfileUs = new Label();
		lblfmProfileMa = new Label();

		String template = "timingTable";
		InputStream layoutFile = getClass().getResourceAsStream(
				"/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout inputFieldLayout = null;
		try {
			inputFieldLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template,
					e.getMessage(), Type.ERROR_MESSAGE);
		}

		inputFieldLayout.addComponent(new Label(I18N.message("timing")),
				"lblTiming");
		inputFieldLayout.addComponent(lbltotalTiming, "lblTotalTimingValie");
		inputFieldLayout.addComponent(new Label(I18N.message("co")),
				"lblTimingCo");
		inputFieldLayout.addComponent(lblfmProfileCo, "lblTimingCoValue");
		inputFieldLayout.addComponent(new Label(I18N.message("uw")),
				"lblTimingUw");
		inputFieldLayout.addComponent(lblfmProfileUw, "lblTimingUwValue");
		inputFieldLayout.addComponent(new Label(I18N.message("uw.supervisor")),
				"lblTimingUwSupervisor");
		inputFieldLayout.addComponent(lblfmProfileUs,
				"lblTimingUwSupervisorValue");
		inputFieldLayout.addComponent(new Label(I18N.message("ma")),
				"lblTimingMa");
		inputFieldLayout.addComponent(lblfmProfileMa, "lblTimingMaValue");

		VerticalLayout timingLayout = new VerticalLayout();
		timingLayout.setMargin(true);
		timingLayout.addComponent(inputFieldLayout);

		return timingLayout;
	}

	/**
	 * Generate Liability Layout
	 * 
	 * @param quotation
	 */
	private void addConsistenceAndLiabilityLayout(Quotation quotation) {

		String amountStyle = "style=\"border:1px solid black;\" align=\"right\"";

		lblExistingInstallmentScore = new Label();
		lblCurrentAddressScore = new Label();
		lblMonthlyInstallmentScore = new Label();
		lblLengthServiceScore = new Label();
		lblEmployerAddressScore = new Label();

		lblApplicantRatio = new Label();
		lblUnderwriterRatio = new Label();
		lblCbInfoRatio = new Label();

		txtUwRevenu = ComponentFactory.getTextField(false, 30, 70);
		txtUwRevenu.setImmediate(true);
		txtUwRevenu.addStyleName("amount");
		txtUwRevenu.addValueChangeListener(this);

		txtUwAllowance = ComponentFactory.getTextField(false, 30, 70);
		txtUwAllowance.setImmediate(true);
		txtUwAllowance.addStyleName("amount");
		txtUwAllowance.addValueChangeListener(this);

		txtUwBusinessExpenses = ComponentFactory.getTextField(false, 30, 70);
		txtUwBusinessExpenses.setImmediate(true);
		txtUwBusinessExpenses.addStyleName("amount");
		txtUwBusinessExpenses.addValueChangeListener(this);

		txtUwNetIncome = ComponentFactory.getTextField(false, 30, 70);
		txtUwNetIncome.setReadOnly(true);
		txtUwNetIncome.addStyleName("amount");

		txtUwPersonalExpenses = ComponentFactory.getTextField(false, 30, 70);
		txtUwPersonalExpenses.setImmediate(true);
		txtUwPersonalExpenses.addStyleName("amount");
		txtUwPersonalExpenses.addValueChangeListener(this);

		txtUwFamilyExpenses = ComponentFactory.getTextField(false, 30, 70);
		txtUwFamilyExpenses.setImmediate(true);
		txtUwFamilyExpenses.addStyleName("amount");
		txtUwFamilyExpenses.addValueChangeListener(this);

		txtUwLiability = ComponentFactory.getTextField(false, 30, 70);
		txtUwLiability.setImmediate(true);
		txtUwLiability.addStyleName("amount");
		txtUwLiability.addValueChangeListener(this);

		txtUwDisposableIncome = ComponentFactory.getTextField(false, 30, 70);
		txtUwDisposableIncome.setReadOnly(true);
		txtUwDisposableIncome.addStyleName("amount");

		// Guarantor
		lblExistingInstallmentScoreGuarantor = new Label();
		lblCurrentAddressScoreGuarantor = new Label();
		lblMonthlyInstallmentScoreGuarantor = new Label();
		lblLengthServiceScoreGuarantor = new Label();
		lblEmployerAddressScoreGuarantor = new Label();

		lblApplicantRatioGuarantor = new Label();
		lblUnderwriterRatioGuarantor = new Label();
		lblCbInfoRatioGuarantor = new Label();

		txtUwRevenuGuarantor = ComponentFactory.getTextField(false, 30, 70);
		txtUwRevenuGuarantor.setImmediate(true);
		txtUwRevenuGuarantor.addStyleName("amount");
		txtUwRevenuGuarantor.addValueChangeListener(this);

		txtUwAllowanceGuarantor = ComponentFactory.getTextField(false, 30, 70);
		txtUwAllowanceGuarantor.setImmediate(true);
		txtUwAllowanceGuarantor.addStyleName("amount");
		txtUwAllowanceGuarantor.addValueChangeListener(this);

		txtUwBusinessExpensesGuarantor = ComponentFactory.getTextField(false,
				30, 70);
		txtUwBusinessExpensesGuarantor.setImmediate(true);
		txtUwBusinessExpensesGuarantor.addStyleName("amount");
		txtUwBusinessExpensesGuarantor.addValueChangeListener(this);

		txtUwNetIncomeGuarantor = ComponentFactory.getTextField(false, 30, 70);
		txtUwNetIncomeGuarantor.setReadOnly(true);
		txtUwNetIncomeGuarantor.addStyleName("amount");

		txtUwPersonalExpensesGuarantor = ComponentFactory.getTextField(false,
				30, 70);
		txtUwPersonalExpensesGuarantor.setImmediate(true);
		txtUwPersonalExpensesGuarantor.addStyleName("amount");
		txtUwPersonalExpensesGuarantor.addValueChangeListener(this);

		txtUwFamilyExpensesGuarantor = ComponentFactory.getTextField(false, 30,
				70);
		txtUwFamilyExpensesGuarantor.setImmediate(true);
		txtUwFamilyExpensesGuarantor.addStyleName("amount");
		txtUwFamilyExpensesGuarantor.addValueChangeListener(this);

		txtUwLiabilityGuarantor = ComponentFactory.getTextField(false, 30, 70);
		txtUwLiabilityGuarantor.setImmediate(true);
		txtUwLiabilityGuarantor.addStyleName("amount");
		txtUwLiabilityGuarantor.addValueChangeListener(this);

		txtUwDisposableIncomeGuarantor = ComponentFactory.getTextField(false,
				30, 70);
		txtUwDisposableIncomeGuarantor.setReadOnly(true);
		txtUwDisposableIncomeGuarantor.addStyleName("amount");
		// ---------------------

		liabilityLayout.removeAllComponents();
		List<QuotationExtModule> quotationExtModules = quotation
				.getQuotationExtModules();
		Response cbResponse = null;
		if (quotationExtModules != null && !quotationExtModules.isEmpty()) {
			try {
				cbResponse = XmlBinder.unmarshal(quotationExtModules.get(0)
						.getResult());
			} catch (JiBXException e) {
				Notification.show(e.toString());
				cbResponse = null;
			}
		}

		Applicant applicant = quotation.getApplicant();
		Individual customer = applicant.getIndividual();
		
		double totalNetIncome = 0d;
		double totalRevenus = 0d;
		double totalAllowance = 0d;
		double totalBusinessExpenses = 0d;
		double totalDebtInstallment = MyNumberUtils.getDouble(customer
				.getTotalDebtInstallment());
		List<Employment> employments = customer.getEmployments();
		for (Employment employment : employments) {
			totalRevenus += MyNumberUtils.getDouble(employment.getRevenue());
			totalAllowance += MyNumberUtils.getDouble(employment.getAllowance());
			totalBusinessExpenses += MyNumberUtils.getDouble(employment
					.getBusinessExpense());
		}
		totalNetIncome = totalRevenus + totalAllowance - totalBusinessExpenses;

		double totalExpenses = MyNumberUtils.getDouble(customer
				.getMonthlyPersonalExpenses())
				+ MyNumberUtils.getDouble(customer.getMonthlyFamilyExpenses())
				+ totalDebtInstallment;

		double disposableIncome = totalNetIncome - totalExpenses;
		monthlyInstallment = calTotalInstallmentAmount(quotation); //quotation.getTiInstallmentUsd();

		double totalCbNetIncome = 0;
		double totalCbInstallment = 0;
		int cbLengthOfService = 0;
		if (cbResponse != null) {
			Consumer consumer = cbResponse.getMessage().getItems().get(0)
					.getRspReport().getConsumer();
			List<AccDetail> accDetails = consumer.getAccDetails();
			if (accDetails != null) {
				for (AccDetail accDetail : accDetails) {
					if (!"C".equals(accDetail.getAccstatus())) {
						if ("USD".equals(accDetail.getAcccurr())) {
							totalCbInstallment += accDetail.getAccinstl();
						} else if ("KHM".equals(accDetail.getAcccurr())) {
							totalCbInstallment += accDetail.getAccinstl() / 4000;
						}
					}
				}
			}
			List<Employer> cbEmployers = consumer.getEmployers();
			if (cbEmployers != null && cbEmployers.size() > 1) {
				for (int i = 1; i < cbEmployers.size(); i++) {
					Employer cbEmployer = cbEmployers.get(i);
					if ("USD".equals(cbEmployer.getEcurr())) {
						totalCbNetIncome += cbEmployer.getEtms();
					} else if ("KHM".equals(cbEmployer.getEcurr())) {
						totalCbNetIncome += cbEmployer.getEtms() / 4000;
					}
					if ("C".equals(cbEmployer.getEtyp())
							&& cbLengthOfService <= 0) {
						cbLengthOfService = cbEmployer.getElen();
					}
				}
			}
		}

		double totalCbExpenses = totalCbInstallment
				+ MyNumberUtils.getDouble(customer.getMonthlyPersonalExpenses())
				+ MyNumberUtils.getDouble(customer.getMonthlyFamilyExpenses());

		double disposableCbIncome = totalCbNetIncome - totalCbExpenses;
		double customerRatio = disposableIncome / monthlyInstallment;
		Configuration config = UWScoreConfig.getInstance().getConfiguration();

		double maxRedGrossIncomeRatio = config
				.getDouble("score.liability.gross_income_ratio[@maxred]");
		if (customerRatio < maxRedGrossIncomeRatio) {
			lblApplicantRatio.setIcon(redIcon);
		} else {
			double maxOrangeGrossIncomeRatio = config
					.getDouble("score.liability.gross_income_ratio[@maxorange]");
			if (customerRatio < maxOrangeGrossIncomeRatio) {
				lblApplicantRatio.setIcon(orangeIcon);
			} else {
				lblApplicantRatio.setIcon(greenIcon);
			}
		}

		txtUwRevenu.setValue(AmountUtils.format(quotation
				.getUwRevenuEstimation()));
		txtUwAllowance.setValue(AmountUtils.format(quotation
				.getUwAllowanceEstimation()));
		txtUwBusinessExpenses.setValue(AmountUtils.format(quotation
				.getUwBusinessExpensesEstimation()));
		txtUwPersonalExpenses.setValue(AmountUtils.format(quotation
				.getUwPersonalExpensesEstimation()));
		txtUwFamilyExpenses.setValue(AmountUtils.format(quotation
				.getUwFamilyExpensesEstimation()));
		txtUwLiability.setValue(AmountUtils.format(quotation
				.getUwLiabilityEstimation()));

		if (cbResponse == null) {
			lblExistingInstallmentScore.setIcon(grayIcon);
			lblCurrentAddressScore.setIcon(grayIcon);
			lblMonthlyInstallmentScore.setIcon(grayIcon);
			lblLengthServiceScore.setIcon(grayIcon);
			lblEmployerAddressScore.setIcon(grayIcon);
		} else {
			double errorMarginCbInstallment = MyNumberUtils
					.getDouble(totalCbInstallment
							* config.getDouble("score.consistence.installment[@factor]"));
			double errorMarginCbMonthlyIncome = MyNumberUtils
					.getDouble(totalCbNetIncome
							* config.getDouble("score.consistence.monthly_income[@factor]"));
			int errorMarginCbLengthService = (int) (cbLengthOfService * config
					.getDouble("score.consistence.length_of_service[@factor]"));

			if ((totalCbInstallment - errorMarginCbInstallment) <= totalDebtInstallment
					&& totalDebtInstallment <= (totalCbInstallment + errorMarginCbInstallment)) {
				lblExistingInstallmentScore.setIcon(greenIcon);
			} else {
				lblExistingInstallmentScore.setIcon(orangeIcon);
			}

			String existingInstallmentDesc = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
			existingInstallmentDesc += "<tr><td " + amountStyle
					+ ">Total Debt Installment (Applicant)</td><td "
					+ amountStyle + ">" + totalDebtInstallment + "</td></tr>";
			existingInstallmentDesc += "<tr><td " + amountStyle
					+ ">Total Debt Installment (CBC)</td><td " + amountStyle
					+ ">" + totalCbInstallment + "</td></tr>";
			existingInstallmentDesc += "<tr><td " + amountStyle
					+ ">Margin : +/-</td><td " + amountStyle + ">"
					+ errorMarginCbInstallment + "</td></tr>";
			existingInstallmentDesc += "</table>";
			lblExistingInstallmentScore.setDescription(existingInstallmentDesc);

			if ((totalCbNetIncome - errorMarginCbMonthlyIncome) <= totalNetIncome
					&& totalNetIncome <= (totalCbNetIncome + errorMarginCbMonthlyIncome)) {
				lblMonthlyInstallmentScore.setIcon(greenIcon);
			} else {
				lblMonthlyInstallmentScore.setIcon(orangeIcon);
			}

			String monthlyInstallmentDesc = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
			monthlyInstallmentDesc += "<tr><td " + amountStyle
					+ ">Gross Income (Applicant)</td><td " + amountStyle + ">"
					+ totalNetIncome + "</td></tr>";
			monthlyInstallmentDesc += "<tr><td " + amountStyle
					+ ">Gross Income (CBC)</td><td " + amountStyle + ">"
					+ totalCbNetIncome + "</td></tr>";
			monthlyInstallmentDesc += "<tr><td " + amountStyle
					+ ">Margin : +/-</td><td " + amountStyle + ">"
					+ errorMarginCbMonthlyIncome + "</td></tr>";
			monthlyInstallmentDesc += "</table>";
			lblMonthlyInstallmentScore.setDescription(monthlyInstallmentDesc);

			int lengthOfService = MyNumberUtils.getInteger(customer
					.getCurrentEmployment().getTimeWithEmployerInMonth())
					+ MyNumberUtils.getInteger(customer.getCurrentEmployment()
							.getTimeWithEmployerInYear()) * 12;

			if ((cbLengthOfService - errorMarginCbLengthService) <= lengthOfService
					&& lengthOfService <= (cbLengthOfService + errorMarginCbLengthService)) {
				lblLengthServiceScore.setIcon(greenIcon);
			} else {
				lblLengthServiceScore.setIcon(orangeIcon);
			}

			String lengthOfServiceDesc = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
			lengthOfServiceDesc += "<tr><td " + amountStyle
					+ ">Length of Service (Applicant)</td><td " + amountStyle
					+ ">" + lengthOfService + "</td></tr>";
			lengthOfServiceDesc += "<tr><td " + amountStyle
					+ ">Length of Service (CBC)</td><td " + amountStyle + ">"
					+ cbLengthOfService + "</td></tr>";
			lengthOfServiceDesc += "<tr><td " + amountStyle
					+ ">Margin : +/-</td><td " + amountStyle + ">"
					+ errorMarginCbLengthService + "</td></tr>";
			lengthOfServiceDesc += "</table>";
			lblLengthServiceScore.setDescription(lengthOfServiceDesc);

			Consumer consumer = cbResponse.getMessage().getItems().get(0)
					.getRspReport().getConsumer();
			List<com.nokor.efinance.third.creditbureau.cbc.model.response.Address> cbAddresses = consumer
					.getAddresses();
			Address currentAddress = customer.getMainAddress();
			boolean matchCurrentAddress = false;
			if (cbAddresses != null && cbAddresses.size() > 1) {
				for (int i = 1; i < cbAddresses.size(); i++) {
					com.nokor.efinance.third.creditbureau.cbc.model.response.Address cbAddress = cbAddresses
							.get(i);
					if (!matchCurrentAddress
							&& currentAddress.getProvince().getCode()
									.equals(cbAddress.getCaprov())
							&& currentAddress.getDistrict().getCode()
									.equals(cbAddress.getCadist())
							&& currentAddress.getCommune().getCode()
									.equals(cbAddress.getCacomm())
							&& currentAddress.getVillage().getCode()
									.equals(cbAddress.getCavill())) {
						matchCurrentAddress = true;
						break;
					}
				}
			} else {
				matchCurrentAddress = true;
			}

			if (matchCurrentAddress) {
				lblCurrentAddressScore.setIcon(greenIcon);
			} else {
				lblCurrentAddressScore.setIcon(redIcon);
			}

			String currentAddressDesc = TooltipUtil.getToolTip(currentAddress,
					cbAddresses);
			lblCurrentAddressScore.setDescription(currentAddressDesc);

			Address employerAddress = customer.getCurrentEmployment()
					.getAddress();
			boolean matchEmploymentAddress = false;

			List<Employer> cbEmployers = consumer.getEmployers();
			if (cbEmployers != null && cbEmployers.size() > 1) {
				for (int i = 1; i < cbEmployers.size(); i++) {
					Employer cbEmployer = cbEmployers.get(i);
					if (cbEmployer.getEadrs() != null
							&& !cbEmployer.getEadrs().isEmpty()) {
						com.nokor.efinance.third.creditbureau.cbc.model.response.Eadr cbEmployerAddress = cbEmployer
								.getEadrs().get(0);
						if (!matchEmploymentAddress
								&& employerAddress.getProvince().getCode()
										.equals(cbEmployerAddress.getEaprov())
								&& employerAddress.getDistrict().getCode()
										.equals(cbEmployerAddress.getEadist())
								&& employerAddress.getCommune().getCode()
										.equals(cbEmployerAddress.getEacomm())
								&& employerAddress.getVillage().getCode()
										.equals(cbEmployerAddress.getEavill())) {
							matchEmploymentAddress = true;
						}
					}
				}
			} else {
				matchEmploymentAddress = true;
			}

			if (matchEmploymentAddress) {
				lblEmployerAddressScore.setIcon(greenIcon);
			} else {
				lblEmployerAddressScore.setIcon(redIcon);
			}

			String employerAddressDesc = TooltipUtil.getToolTip(
					customer.getCurrentEmployment(), cbEmployers);
			lblEmployerAddressScore.setDescription(employerAddressDesc);
		}

		String cbGrossIncomeDesc = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
		cbGrossIncomeDesc += "<tr><td " + amountStyle + ">Revenue</td><td " + amountStyle + ">" + AmountUtils.format(totalRevenus)
				+ "</td></tr>";
		cbGrossIncomeDesc += "<tr><td " + amountStyle + ">Allowance</td><td " + amountStyle + ">" + AmountUtils.format(totalAllowance)
				+ "</td></tr>";
		cbGrossIncomeDesc += "<tr><td " + amountStyle + ">Business Expenses</td><td " + amountStyle + ">"
				+ AmountUtils.format(totalBusinessExpenses) + "</td></tr>";
		cbGrossIncomeDesc += "<tr><td " + amountStyle + "><b>Net Income</b></td><td " + amountStyle + "><b>"
				+ AmountUtils.format(totalNetIncome) + "</b></td></tr>";
		cbGrossIncomeDesc += "<tr><td " + amountStyle + ">Personal Expenses</td><td " + amountStyle + ">"
				+ AmountUtils.format(customer.getMonthlyPersonalExpenses()) + "</td></tr>";
		cbGrossIncomeDesc += "<tr><td " + amountStyle + ">Family Expenses</td><td " + amountStyle + ">"
				+ AmountUtils.format(customer.getMonthlyFamilyExpenses()) + "</td></tr>";
		cbGrossIncomeDesc += "<tr><td " + amountStyle + ">Debt Installment</td><td " + amountStyle + ">"
				+ AmountUtils.format(totalDebtInstallment) + "</td></tr>";
		cbGrossIncomeDesc += "<tr><td " + amountStyle + "><b>Expenses</b></td><td " + amountStyle + "><b>"
				+ AmountUtils.format(totalExpenses) + "</b></td></tr>";
		cbGrossIncomeDesc += "<tr><td " + amountStyle + "><b>Ratio</b></td><td " + amountStyle + "><b>"
				+ AmountUtils.format(customerRatio) + "</b</td></tr>";
		cbGrossIncomeDesc += "</table>";
		lblApplicantRatio.setDescription(cbGrossIncomeDesc);

		if (cbResponse == null) {

		} else {

			double creditbureauRatio = disposableCbIncome / monthlyInstallment;
			double maxCbInfoRedRatio = config.getDouble("score.liability.cbc_info_ratio[@maxred]");
			if (creditbureauRatio < maxCbInfoRedRatio) {
				lblCbInfoRatio.setIcon(redIcon);
			} else {
				double maxCbInfoOrangeRatio = config.getDouble("score.liability.cbc_info_ratio[@maxorange]");
				if (creditbureauRatio < maxCbInfoOrangeRatio) {
					lblCbInfoRatio.setIcon(orangeIcon);
				} else {
					lblCbInfoRatio.setIcon(greenIcon);
				}
			}

			String cbInfoDesc = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
			cbInfoDesc += "<tr><td " + amountStyle + ">Net Income</td><td " + amountStyle + ">" + AmountUtils.format(totalCbNetIncome)
					+ "</td></tr>";
			cbInfoDesc += "<tr><td " + amountStyle + ">Expenses</td><td " + amountStyle + ">" + AmountUtils.format(totalCbExpenses)
					+ "</td></tr>";
			cbInfoDesc += "<tr><td " + amountStyle + "><b>Ratio</b></td><td " + amountStyle + "><b>"
					+ AmountUtils.format(creditbureauRatio) + "</b</td></tr>";
			cbInfoDesc += "</table>";

			lblCbInfoRatio.setDescription(cbInfoDesc);

		}

		// Guarantor
		List<QuotationExtModule> quotationExtModulesGuarantor = quotation
				.getQuotationExtModules();
		Response cbResponseGuarantor = null;
		if (quotationExtModulesGuarantor != null
				&& !quotationExtModulesGuarantor.isEmpty()) {
			try {
				cbResponseGuarantor = XmlBinder
						.unmarshal(quotationExtModulesGuarantor.get(0)
								.getResult());
			} catch (JiBXException e) {
				Notification.show(e.toString());
				cbResponseGuarantor = null;
			}
		}

		Applicant applicantGuarantor = quotation.getGuarantor();
		Individual guarantor = null;
		if (applicantGuarantor != null) {
			guarantor = applicantGuarantor.getIndividual();
		}
		
		double totalNetIncomeGuarantor = 0d;
		double totalRevenusGuarantor = 0d;
		double totalAllowanceGuarantor = 0d;
		double totalBusinessExpensesGuarantor = 0d;
		double totalDebtInstallmentGuarantor = 0d;
		double disposableIncomeGuarantor = 0d;
		double totalExpensesGuarantor = 0d;

		if (guarantor != null && quotationService.isGuarantorRequired(quotation)) {
			totalDebtInstallmentGuarantor = MyNumberUtils.getDouble(guarantor
					.getTotalDebtInstallment());

			List<Employment> employmentGuarantors = guarantor.getEmployments();
			for (Employment employmentGuarantor : employmentGuarantors) {
				totalRevenusGuarantor += MyNumberUtils
						.getDouble(employmentGuarantor.getRevenue());
				totalAllowanceGuarantor += MyNumberUtils
						.getDouble(employmentGuarantor.getAllowance());
				totalBusinessExpensesGuarantor += MyNumberUtils
						.getDouble(employmentGuarantor.getBusinessExpense());
			}
			totalNetIncomeGuarantor = totalRevenusGuarantor
					+ totalAllowanceGuarantor - totalBusinessExpensesGuarantor;

			totalExpensesGuarantor = MyNumberUtils.getDouble(guarantor
					.getMonthlyPersonalExpenses())
					+ MyNumberUtils.getDouble(guarantor
							.getMonthlyFamilyExpenses())
					+ totalDebtInstallmentGuarantor;

			disposableIncomeGuarantor = totalNetIncomeGuarantor
					- totalExpensesGuarantor;
			monthlyInstallmentGuarantor = calTotalInstallmentAmount(quotation);
		}
		double totalCbNetIncomeGuarantor = 0;
		double totalCbInstallmentGuarantor = 0;
		int cbLengthOfServiceGuarantor = 0;
		if (cbResponseGuarantor != null) {
			Consumer consumer = cbResponseGuarantor.getMessage().getItems()
					.get(0).getRspReport().getConsumer();
			List<AccDetail> accDetails = consumer.getAccDetails();
			if (accDetails != null) {
				for (AccDetail accDetail : accDetails) {
					if (!"C".equals(accDetail.getAccstatus())) {
						if ("USD".equals(accDetail.getAcccurr())) {
							totalCbInstallmentGuarantor += accDetail
									.getAccinstl();
						} else if ("KHM".equals(accDetail.getAcccurr())) {
							totalCbInstallmentGuarantor += accDetail
									.getAccinstl() / 4000;
						}
					}
				}
			}
			List<Employer> cbEmployers = consumer.getEmployers();
			if (cbEmployers != null && cbEmployers.size() > 1) {
				for (int i = 1; i < cbEmployers.size(); i++) {
					Employer cbEmployer = cbEmployers.get(i);
					if ("USD".equals(cbEmployer.getEcurr())) {
						totalCbNetIncomeGuarantor += cbEmployer.getEtms();
					} else if ("KHM".equals(cbEmployer.getEcurr())) {
						totalCbNetIncomeGuarantor += cbEmployer.getEtms() / 4000;
					}
					if ("C".equals(cbEmployer.getEtyp())
							&& cbLengthOfService <= 0) {
						cbLengthOfServiceGuarantor = cbEmployer.getElen();
					}
				}
			}
		}
		double totalCbExpensesGuarantor = 0d;
		if (guarantor != null && quotationService.isGuarantorRequired(quotation)) {
			totalCbExpensesGuarantor = totalCbInstallment
					+ MyNumberUtils.getDouble(guarantor
							.getMonthlyPersonalExpenses())
					+ MyNumberUtils.getDouble(guarantor
							.getMonthlyFamilyExpenses());
		}

		double disposableCbIncomeGuarantor = totalCbNetIncome - totalCbExpenses;
		double customerRatioGuarantor = disposableIncomeGuarantor
				/ monthlyInstallmentGuarantor;
		Configuration configGuarantor = UWScoreConfig.getInstance()
				.getConfiguration();

		double maxRedGrossIncomeRatioGuarantor = configGuarantor
				.getDouble("score.liability.gross_income_ratio[@maxred]");
		if (customerRatioGuarantor < maxRedGrossIncomeRatioGuarantor) {
			lblApplicantRatioGuarantor.setIcon(redIcon);
		} else {
			double maxOrangeGrossIncomeRatioGuarantor = configGuarantor
					.getDouble("score.liability.gross_income_ratio[@maxorange]");
			if (customerRatioGuarantor < maxOrangeGrossIncomeRatioGuarantor) {
				lblApplicantRatioGuarantor.setIcon(orangeIcon);
			} else {
				lblApplicantRatioGuarantor.setIcon(greenIcon);
			}
		}

		txtUwRevenuGuarantor.setValue(AmountUtils.format(quotation
				.getUwGuarantorRevenuEstimation()));
		txtUwAllowanceGuarantor.setValue(AmountUtils.format(quotation
				.getUwGuarantorAllowanceEstimation()));
		txtUwBusinessExpensesGuarantor.setValue(AmountUtils.format(quotation
				.getUwGuarantorBusinessExpensesEstimation()));
		txtUwPersonalExpensesGuarantor.setValue(AmountUtils.format(quotation
				.getUwGuarantorPersonalExpensesEstimation()));
		txtUwFamilyExpensesGuarantor.setValue(AmountUtils.format(quotation
				.getUwGuarantorFamilyExpensesEstimation()));
		txtUwLiabilityGuarantor.setValue(AmountUtils.format(quotation
				.getUwGuarantorLiabilityEstimation()));

		if (cbResponseGuarantor == null) {
			lblExistingInstallmentScoreGuarantor.setIcon(grayIcon);
			lblCurrentAddressScoreGuarantor.setIcon(grayIcon);
			lblMonthlyInstallmentScoreGuarantor.setIcon(grayIcon);
			lblLengthServiceScoreGuarantor.setIcon(grayIcon);
			lblEmployerAddressScoreGuarantor.setIcon(grayIcon);
		} else {
			double errorMarginCbInstallmentGuarantor = MyNumberUtils
					.getDouble(totalCbInstallmentGuarantor
							* config.getDouble("score.consistence.installment[@factor]"));
			double errorMarginCbMonthlyIncomeGuarantor = MyNumberUtils
					.getDouble(totalCbNetIncomeGuarantor
							* config.getDouble("score.consistence.monthly_income[@factor]"));
			int errorMarginCbLengthServiceGuarantor = (int) (cbLengthOfServiceGuarantor * config
					.getDouble("score.consistence.length_of_service[@factor]"));

			if ((totalCbInstallmentGuarantor - errorMarginCbInstallmentGuarantor) <= totalDebtInstallmentGuarantor
					&& totalDebtInstallmentGuarantor <= (totalCbInstallmentGuarantor + errorMarginCbInstallmentGuarantor)) {
				lblExistingInstallmentScoreGuarantor.setIcon(greenIcon);
			} else {
				lblExistingInstallmentScoreGuarantor.setIcon(orangeIcon);
			}

			String existingInstallmentDescGuarantor = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
			existingInstallmentDescGuarantor += "<tr><td " + amountStyle + ">Total Debt Installment (Applicant)</td><td "
					+ amountStyle + ">" + totalDebtInstallmentGuarantor + "</td></tr>";
			existingInstallmentDescGuarantor += "<tr><td " + amountStyle + ">Total Debt Installment (CBC)</td><td " + amountStyle
					+ ">" + totalCbInstallmentGuarantor + "</td></tr>";
			existingInstallmentDescGuarantor += "<tr><td " + amountStyle + ">Margin : +/-</td><td " + amountStyle + ">"
					+ errorMarginCbInstallmentGuarantor + "</td></tr>";
			existingInstallmentDescGuarantor += "</table>";
			lblExistingInstallmentScoreGuarantor.setDescription(existingInstallmentDescGuarantor);

			if ((totalCbNetIncomeGuarantor - errorMarginCbMonthlyIncomeGuarantor) <= totalNetIncomeGuarantor
					&& totalNetIncomeGuarantor <= (totalCbNetIncomeGuarantor + errorMarginCbMonthlyIncomeGuarantor)) {
				lblMonthlyInstallmentScoreGuarantor.setIcon(greenIcon);
			} else {
				lblMonthlyInstallmentScoreGuarantor.setIcon(orangeIcon);
			}

			String monthlyInstallmentDescGuarantor = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
			monthlyInstallmentDescGuarantor += "<tr><td " + amountStyle + ">Gross Income (Applicant)</td><td " + amountStyle + ">"
					+ totalNetIncomeGuarantor + "</td></tr>";
			monthlyInstallmentDescGuarantor += "<tr><td " + amountStyle + ">Gross Income (CBC)</td><td " + amountStyle + ">"
					+ totalCbNetIncomeGuarantor + "</td></tr>";
			monthlyInstallmentDescGuarantor += "<tr><td " + amountStyle + ">Margin : +/-</td><td " + amountStyle + ">"
					+ errorMarginCbMonthlyIncomeGuarantor + "</td></tr>";
			monthlyInstallmentDescGuarantor += "</table>";
			lblMonthlyInstallmentScoreGuarantor.setDescription(monthlyInstallmentDescGuarantor);

			int lengthOfServiceGuarantor = 0;
			if (guarantor != null && quotationService.isGuarantorRequired(quotation)) {
				lengthOfServiceGuarantor = MyNumberUtils.getInteger(guarantor
						.getCurrentEmployment().getTimeWithEmployerInMonth())
						+ MyNumberUtils.getInteger(guarantor
								.getCurrentEmployment()
								.getTimeWithEmployerInYear()) * 12;
			}
			if ((cbLengthOfServiceGuarantor - errorMarginCbLengthServiceGuarantor) <= lengthOfServiceGuarantor
					&& lengthOfServiceGuarantor <= (cbLengthOfServiceGuarantor + errorMarginCbLengthServiceGuarantor)) {
				lblLengthServiceScoreGuarantor.setIcon(greenIcon);
			} else {
				lblLengthServiceScoreGuarantor.setIcon(orangeIcon);
			}

			String lengthOfServiceDescGuarantor = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
			lengthOfServiceDescGuarantor += "<tr><td " + amountStyle + ">Length of Service (Applicant)</td><td " + amountStyle
					+ ">" + lengthOfServiceGuarantor + "</td></tr>";
			lengthOfServiceDescGuarantor += "<tr><td " + amountStyle + ">Length of Service (CBC)</td><td " + amountStyle + ">"
					+ cbLengthOfServiceGuarantor + "</td></tr>";
			lengthOfServiceDescGuarantor += "<tr><td " + amountStyle + ">Margin : +/-</td><td " + amountStyle + ">"
					+ errorMarginCbLengthServiceGuarantor + "</td></tr>";
			lengthOfServiceDescGuarantor += "</table>";
			lblLengthServiceScoreGuarantor.setDescription(lengthOfServiceDescGuarantor);

			Consumer consumer = cbResponseGuarantor.getMessage().getItems()
					.get(0).getRspReport().getConsumer();
			List<com.nokor.efinance.third.creditbureau.cbc.model.response.Address> cbAddressesGuarantor = consumer
					.getAddresses();
			if (guarantor != null && quotationService.isGuarantorRequired(quotation)) {
				Address currentAddressGuarantor = guarantor.getMainAddress();
				boolean matchCurrentAddressGuarantor = false;
				if (cbAddressesGuarantor != null
						&& cbAddressesGuarantor.size() > 1) {
					for (int i = 1; i < cbAddressesGuarantor.size(); i++) {
						com.nokor.efinance.third.creditbureau.cbc.model.response.Address cbAddressGuarantor = cbAddressesGuarantor
								.get(i);
						if (!matchCurrentAddressGuarantor
								&& currentAddressGuarantor.getProvince()
										.getCode()
										.equals(cbAddressGuarantor.getCaprov())
								&& currentAddressGuarantor.getDistrict()
										.getCode()
										.equals(cbAddressGuarantor.getCadist())
								&& currentAddressGuarantor.getCommune()
										.getCode()
										.equals(cbAddressGuarantor.getCacomm())
								&& currentAddressGuarantor.getVillage()
										.getCode()
										.equals(cbAddressGuarantor.getCavill())) {
							matchCurrentAddressGuarantor = true;
							break;
						}
					}
				} else {
					matchCurrentAddressGuarantor = true;
				}

				if (matchCurrentAddressGuarantor) {
					lblCurrentAddressScoreGuarantor.setIcon(greenIcon);
				} else {
					lblCurrentAddressScoreGuarantor.setIcon(redIcon);
				}

				String currentAddressDescGuarantor = TooltipUtil.getToolTip(
						currentAddressGuarantor, cbAddressesGuarantor);
				lblCurrentAddressScoreGuarantor
						.setDescription(currentAddressDescGuarantor);
			}
			if (guarantor != null && quotationService.isGuarantorRequired(quotation)) {
				Address employerAddressGuarantor = guarantor
						.getCurrentEmployment().getAddress();
				boolean matchEmploymentAddressGuarantor = false;

				List<Employer> cbEmployersGuarantor = consumer.getEmployers();
				if (cbEmployersGuarantor != null
						&& cbEmployersGuarantor.size() > 1) {
					for (int i = 1; i < cbEmployersGuarantor.size(); i++) {
						Employer cbEmployerGuarantor = cbEmployersGuarantor
								.get(i);
						if (cbEmployerGuarantor.getEadrs() != null
								&& !cbEmployerGuarantor.getEadrs().isEmpty()) {
							com.nokor.efinance.third.creditbureau.cbc.model.response.Eadr cbEmployerAddressGuarantor = cbEmployerGuarantor
									.getEadrs().get(0);
							if (!matchEmploymentAddressGuarantor
									&& employerAddressGuarantor
											.getProvince()
											.getCode()
											.equals(cbEmployerAddressGuarantor
													.getEaprov())
									&& employerAddressGuarantor
											.getDistrict()
											.getCode()
											.equals(cbEmployerAddressGuarantor
													.getEadist())
									&& employerAddressGuarantor
											.getCommune()
											.getCode()
											.equals(cbEmployerAddressGuarantor
													.getEacomm())
									&& employerAddressGuarantor
											.getVillage()
											.getCode()
											.equals(cbEmployerAddressGuarantor
													.getEavill())) {
								matchEmploymentAddressGuarantor = true;
							}
						}
					}
				} else {
					matchEmploymentAddressGuarantor = true;
				}

				if (matchEmploymentAddressGuarantor) {
					lblEmployerAddressScoreGuarantor.setIcon(greenIcon);
				} else {
					lblEmployerAddressScoreGuarantor.setIcon(redIcon);
				}

				String employerAddressDescGuarantor = TooltipUtil.getToolTip(
						guarantor.getCurrentEmployment(), cbEmployersGuarantor);
				lblEmployerAddressScoreGuarantor
						.setDescription(employerAddressDescGuarantor);
			}
		}

		if (guarantor != null && quotationService.isGuarantorRequired(quotation)) {
			String cbGrossIncomeDescGuarantor = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
			cbGrossIncomeDescGuarantor += "<tr><td " + amountStyle + ">Revenue</td><td " + amountStyle + ">"
					+ AmountUtils.format(totalRevenusGuarantor) + "</td></tr>";
			cbGrossIncomeDescGuarantor += "<tr><td " + amountStyle + ">Allowance</td><td " + amountStyle + ">"
					+ AmountUtils.format(totalAllowanceGuarantor) + "</td></tr>";
			cbGrossIncomeDescGuarantor += "<tr><td " + amountStyle + ">Business Expenses</td><td " + amountStyle + ">"
					+ AmountUtils.format(totalBusinessExpensesGuarantor) + "</td></tr>";
			cbGrossIncomeDescGuarantor += "<tr><td " + amountStyle + "><b>Net Income</b></td><td " + amountStyle + "><b>"
					+ AmountUtils.format(totalNetIncomeGuarantor) + "</b></td></tr>";
			cbGrossIncomeDescGuarantor += "<tr><td " + amountStyle + ">Personal Expenses</td><td " + amountStyle + ">"
					+ AmountUtils.format(guarantor.getMonthlyPersonalExpenses()) + "</td></tr>";
			cbGrossIncomeDescGuarantor += "<tr><td " + amountStyle + ">Family Expenses</td><td " + amountStyle + ">"
					+ AmountUtils.format(guarantor.getMonthlyFamilyExpenses()) + "</td></tr>";
			cbGrossIncomeDescGuarantor += "<tr><td " + amountStyle + ">Debt Installment</td><td " + amountStyle + ">"
					+ AmountUtils.format(totalDebtInstallmentGuarantor) + "</td></tr>";
			cbGrossIncomeDescGuarantor += "<tr><td " + amountStyle + "><b>Expenses</b></td><td " + amountStyle + "><b>"
					+ AmountUtils.format(totalExpensesGuarantor) + "</b></td></tr>";
			cbGrossIncomeDescGuarantor += "<tr><td " + amountStyle + "><b>Ratio</b></td><td " + amountStyle + "><b>"
					+ AmountUtils.format(customerRatioGuarantor) + "</b</td></tr>";
			cbGrossIncomeDescGuarantor += "</table>";
			lblApplicantRatioGuarantor.setDescription(cbGrossIncomeDescGuarantor);
		}
		if (cbResponseGuarantor == null) {

		} else {

			double creditbureauRatioGuarantor = disposableCbIncomeGuarantor / monthlyInstallmentGuarantor;
			double maxCbInfoRedRatioGuarantor = config.getDouble("score.liability.cbc_info_ratio[@maxred]");
			if (creditbureauRatioGuarantor < maxCbInfoRedRatioGuarantor) {
				lblCbInfoRatioGuarantor.setIcon(redIcon);
			} else {
				double maxCbInfoOrangeRatioGuarantor = config.getDouble("score.liability.cbc_info_ratio[@maxorange]");
				if (creditbureauRatioGuarantor < maxCbInfoOrangeRatioGuarantor) {
					lblCbInfoRatioGuarantor.setIcon(orangeIcon);
				} else {
					lblCbInfoRatioGuarantor.setIcon(greenIcon);
				}
			}

			String cbInfoDescGuarantor = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
			cbInfoDescGuarantor += "<tr><td " + amountStyle + ">Net Income</td><td " + amountStyle + ">"
					+ AmountUtils.format(totalCbNetIncomeGuarantor) + "</td></tr>";
			cbInfoDescGuarantor += "<tr><td " + amountStyle + ">Expenses</td><td " + amountStyle + ">"
					+ AmountUtils.format(totalCbExpensesGuarantor) + "</td></tr>";
			cbInfoDescGuarantor += "<tr><td " + amountStyle + "><b>Ratio</b></td><td " + amountStyle + "><b>"
					+ AmountUtils.format(creditbureauRatioGuarantor) + "</b</td></tr>";
			cbInfoDescGuarantor += "</table>";

			lblCbInfoRatio.setDescription(cbInfoDescGuarantor);

		}
		// --------------------------

		try {
			// Applicant liability
			InputStream customLiabilityLayoutFile = getClass().getResourceAsStream(	"/VAADIN/themes/efinance/layouts/uw_liability.html");
			CustomLayout customLiabilityLayout = new CustomLayout(customLiabilityLayoutFile);
			
			customLiabilityLayout.addComponent(new Label(I18N.message("applicant")), "lblApplicant");
			customLiabilityLayout.addComponent(new Label(I18N.message("base.salary.total.sales")), "lblRevenu");
			customLiabilityLayout.addComponent(new Label(I18N.message("allowance")), "lblAllowance");
			customLiabilityLayout.addComponent(new Label(I18N.message("business.expenses")), "lblBusinessExpenses");
			customLiabilityLayout.addComponent(new Label(I18N.message("net.income")), "lblNetIncome");
			customLiabilityLayout.addComponent(new Label(I18N.message("personal.expenses")), "lblPersonalExpenses");
			customLiabilityLayout.addComponent(new Label(I18N.message("family.expenses")), "lblFamilyExpenses");
			customLiabilityLayout.addComponent(new Label(I18N.message("liability")), "lblLiability");
			customLiabilityLayout.addComponent(new Label(I18N.message("disposable.income")), "lblDisposableIncome");
			customLiabilityLayout.addComponent(new Label(I18N.message("installment")), "lblInstallment");
			customLiabilityLayout.addComponent(new Label(I18N.message("ratio")), "lblRatio");

			customLiabilityLayout.addComponent(new Label(I18N.message("co.interview.info")), "lblCoInterviewInfo");
			customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalRevenus)), "lblTotalRevenus");
			customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalAllowance)), "lblTotalAllowance");
			customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalBusinessExpenses)), "lblTotalBusinessExpenses");
			customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalNetIncome)), "lblTotalNetIncome");
			customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalExpenses)), "lblTotalExpenses");
			customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(customer.getMonthlyPersonalExpenses())), "lblTotalPersonalExpenses");
			customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(customer.getMonthlyFamilyExpenses())), "lblTotalFamilyExpenses");
			customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(customer.getTotalDebtInstallment())), "lblLiabilityValue");
			customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(disposableIncome)), "lblDisposableIncomeValue");
			customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(monthlyInstallment)), "lblMonthlyInstallmentValue1");
			customLiabilityLayout.addComponent(lblApplicantRatio, "lblApplicantRatio");

			customLiabilityLayout.addComponent(new Label(I18N.message("underwriter.estimation")), "lblUwEstimation");
			customLiabilityLayout.addComponent(txtUwRevenu, "txtUwRevenu");
			customLiabilityLayout.addComponent(txtUwAllowance, "txtUwAllowance");
			customLiabilityLayout.addComponent(txtUwBusinessExpenses, "txtUwBusinessExpenses");
			customLiabilityLayout.addComponent(txtUwNetIncome, "txtUwNetIncome");
			customLiabilityLayout.addComponent(txtUwPersonalExpenses, "txtUwPersonalExpenses");
			customLiabilityLayout.addComponent(txtUwFamilyExpenses, "txtUwFamilyExpenses");
			customLiabilityLayout.addComponent(txtUwLiability, "lblUwLiabilityValue");
			customLiabilityLayout.addComponent(txtUwDisposableIncome, "txtUwDisposableIncome");
			customLiabilityLayout.addComponent(new Label("" + monthlyInstallment), "lblMonthlyInstallmentValue2");
			customLiabilityLayout.addComponent(lblUnderwriterRatio, "lblUnderwriterRatio");

			if (cbResponse != null) {
				customLiabilityLayout.addComponent(new Label(I18N.message("credit.bureau.report")), "lblCreditBureauReport");
				customLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbRevenus");
				customLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbAllowance");
				customLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbBusinessExpenses");
				customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalCbNetIncome)), "lblTotalCbNetIncome");
				customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(customer.getMonthlyPersonalExpenses())),
						"lblTotalCbPersonalExpenses");
				customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(customer.getMonthlyFamilyExpenses())),
						"lblTotalCbFamilyExpenses");
				customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalCbInstallment)), "lblCbLiabilityValue");
				customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(disposableCbIncome)), "lblDisposableCbIncome");
				customLiabilityLayout.addComponent(new Label("" + AmountUtils.format(monthlyInstallment)), "lblMonthlyInstallmentValue3");
				customLiabilityLayout.addComponent(lblCbInfoRatio, "lblCbInfoRatio");
			} else {
				customLiabilityLayout.addComponent(new Label(I18N.message("no.credit.bureau.report")), "lblCreditBureauReport");
				customLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbRevenus");
				customLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbAllowance");
				customLiabilityLayout.addComponent(new Label("NA"),	"lblTotalCbBusinessExpenses");
				customLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbNetIncome");
				customLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbPersonalExpenses");
				customLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbFamilyExpenses");
				customLiabilityLayout.addComponent(new Label("NA"), "lblCbLiabilityValue");
				customLiabilityLayout.addComponent(new Label("NA"), "lblDisposableCbIncome");
				customLiabilityLayout.addComponent(new Label("NA"), "lblMonthlyInstallmentValue3");
				customLiabilityLayout.addComponent(lblCbInfoRatio, "lblCbInfoRatio");
			}

			// Guarantor liability
			InputStream customGuarantorLiabilityLayoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/uw_liability_guarantor.html");
			CustomLayout customGuarantorLiabilityLayout = new CustomLayout(customGuarantorLiabilityLayoutFile);
			if (guarantor != null && quotationService.isGuarantorRequired(quotation)) {customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("guarantor")), "lblGuarantor");
			
				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("base.salary.total.sales")), "lblRevenuGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("allowance")), "lblAllowanceGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("business.expenses")), "lblBusinessExpensesGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("net.income")), "lblNetIncomeGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("personal.expenses")), "lblPersonalExpensesGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("family.expenses")), "lblFamilyExpensesGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("liability")), "lblLiabilityGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("disposable.income")), "lblDisposableIncomeGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("installment")), "lblInstallmentGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("ratio")), "lblRatioGuarantor");

				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("co.interview.info")), "lblCoInterviewInfoGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalRevenusGuarantor)), "lblTotalRevenusGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalAllowanceGuarantor)), "lblTotalAllowanceGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalBusinessExpensesGuarantor)), "lblTotalBusinessExpensesGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalNetIncomeGuarantor)), "lblTotalNetIncomeGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalExpensesGuarantor)), "lblTotalExpensesGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(guarantor != null ? "" + AmountUtils.format(guarantor.getMonthlyPersonalExpenses()) : ""),
						"lblTotalPersonalExpensesGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(guarantor != null ? "" + AmountUtils.format(guarantor.getMonthlyFamilyExpenses()) : ""),
						"lblTotalFamilyExpensesGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label(guarantor != null ? "" + AmountUtils.format(guarantor.getTotalDebtInstallment()) : ""),
						"lblLiabilityValueGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(disposableIncomeGuarantor)),
						"lblDisposableIncomeValueGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(monthlyInstallmentGuarantor)),
						"lblMonthlyInstallmentValue1Guarantor");
				customGuarantorLiabilityLayout.addComponent(lblApplicantRatioGuarantor, "lblApplicantRatioGuarantor");

				customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("underwriter.estimation")), "lblUwEstimationGuarantor");
				customGuarantorLiabilityLayout.addComponent(txtUwRevenuGuarantor, "txtUwRevenuGuarantor");
				customGuarantorLiabilityLayout.addComponent(txtUwAllowanceGuarantor, "txtUwAllowanceGuarantor");
				customGuarantorLiabilityLayout.addComponent(txtUwBusinessExpensesGuarantor, "txtUwBusinessExpensesGuarantor");
				customGuarantorLiabilityLayout.addComponent(txtUwNetIncomeGuarantor, "txtUwNetIncomeGuarantor");
				customGuarantorLiabilityLayout.addComponent(txtUwPersonalExpensesGuarantor, "txtUwPersonalExpensesGuarantor");
				customGuarantorLiabilityLayout.addComponent(txtUwFamilyExpensesGuarantor, "txtUwFamilyExpensesGuarantor");
				customGuarantorLiabilityLayout.addComponent(txtUwLiabilityGuarantor, "lblUwLiabilityValueGuarantor");
				customGuarantorLiabilityLayout.addComponent(txtUwDisposableIncomeGuarantor, "txtUwDisposableIncomeGuarantor");
				customGuarantorLiabilityLayout.addComponent(new Label("" + monthlyInstallmentGuarantor), "lblMonthlyInstallmentValue2Guarantor");
				customGuarantorLiabilityLayout.addComponent(lblUnderwriterRatioGuarantor, "lblUnderwriterRatioGuarantor");

				if (cbResponseGuarantor != null) {
					customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("credit.bureau.report")), "lblCreditBureauReportGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbRevenusGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbAllowanceGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbBusinessExpensesGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalCbNetIncomeGuarantor)),
							"lblTotalCbNetIncomeGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label(guarantor != null ? "" + AmountUtils.format(guarantor.getMonthlyPersonalExpenses()) : ""),
							"lblTotalCbPersonalExpensesGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label(guarantor != null ? "" + AmountUtils.format(guarantor.getMonthlyFamilyExpenses()) : ""),
							"lblTotalCbFamilyExpensesGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(totalCbInstallmentGuarantor)),
							"lblCbLiabilityValueGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(disposableCbIncomeGuarantor)),
							"lblDisposableCbIncomeGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("" + AmountUtils.format(monthlyInstallmentGuarantor)),
							"lblMonthlyInstallmentValue3Guarantor");
					customGuarantorLiabilityLayout.addComponent(lblCbInfoRatioGuarantor, "lblCbInfoRatioGuarantor");
				} else {
					customGuarantorLiabilityLayout.addComponent(new Label(I18N.message("no.credit.bureau.report")), "lblCreditBureauReportGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbRevenusGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbAllowanceGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbBusinessExpensesGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbNetIncomeGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbPersonalExpensesGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblTotalCbFamilyExpensesGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblCbLiabilityValueGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblDisposableCbIncomeGuarantor");
					customGuarantorLiabilityLayout.addComponent(new Label("NA"), "lblMonthlyInstallmentValue3Guarantor");
					customGuarantorLiabilityLayout.addComponent(lblCbInfoRatioGuarantor, "lblCbInfoRatioGuarantor");
				}
			}

			/*
			 * InputStream customConsistenceLayoutFile =
			 * getClass().getResourceAsStream
			 * ("/VAADIN/themes/efinance/layouts/uw_consistence.html");
			 * CustomLayout customConsistenceLayout = new
			 * CustomLayout(customConsistenceLayoutFile);
			 * customConsistenceLayout.addComponent(new
			 * Label(I18N.message("existing.installment")),
			 * "lblExistingInstallment");
			 * customConsistenceLayout.addComponent(lblExistingInstallmentScore,
			 * "lblExistingInstallmentScore");
			 * customConsistenceLayout.addComponent(new
			 * Label(I18N.message("current.address")), "lblCurrentAddress");
			 * customConsistenceLayout.addComponent(lblCurrentAddressScore,
			 * "lblCurrentAddressScore");
			 * customConsistenceLayout.addComponent(new
			 * Label(I18N.message("monthly.income")), "lblMonthlyInstallment");
			 * customConsistenceLayout.addComponent(lblMonthlyInstallmentScore,
			 * "lblMonthlyInstallmentScore");
			 * customConsistenceLayout.addComponent(new
			 * Label(I18N.message("length.of.service")), "lblLengthService");
			 * customConsistenceLayout.addComponent(lblLengthServiceScore,
			 * "lblLengthServiceScore");
			 * customConsistenceLayout.addComponent(new
			 * Label(I18N.message("employer.address")), "lblEmployerAddress");
			 * customConsistenceLayout.addComponent(lblEmployerAddressScore,
			 * "lblEmployerAddressScore");
			 * 
			 * liabilityLayout.addComponent(customConsistenceLayout);
			 * liabilityLayout.addComponent(ComponentFactory.getSpaceLayout(15,
			 * Unit.PIXELS));
			 */
			liabilityLayout.addComponent(customLiabilityLayout);
			liabilityLayout.addComponent(getSpaceHeight(10, Unit.PIXELS));
			if (guarantor != null) {
				liabilityLayout.addComponent(customGuarantorLiabilityLayout);
			}
		} catch (IOException e) {
			Notification.show(e.toString());
			return;
		}
	}

	/**
	 * @param height
	 * @param unit
	 * @return
	 */
	public VerticalLayout getSpaceHeight(int height, Unit unit) {
		VerticalLayout spaceHeight = new VerticalLayout();
		spaceHeight.setHeight(height, unit);
		return spaceHeight;
	}

	/**
	 * Add documents Layout
	 * 
	 * @param quotation
	 */
	private void addDocumentsLayout(Quotation quotation) {
		TabSheet documentsTabSheet = new TabSheet();
		documentsLayout.removeAllComponents();
		documentUwGroupsPanel.clear();
		documentsLayout.addComponent(documentsTabSheet);
		documentsLayout.setWidth("90%");
		List<DocumentUwGroup> documentUwGroups = quotationService.list(DocumentUwGroup.class);
		Collections.sort(documentUwGroups, new SortIndexComparator());
		if (documentUwGroups != null && !documentUwGroups.isEmpty()) {
			for (DocumentUwGroup documentUwGroup : documentUwGroups) {
				if (!quotationService.isGuarantorRequired(quotation)
						&& documentUwGroup.getApplicantType().equals(EApplicantType.G)) {
					// do nothing
				} else {
					DocumentUwGroupPanel documentUwGroupPanel = new DocumentUwGroupPanel(
							documentUwGroup, quotation, false);
					int totalScore = documentUwGroupPanel.getTotalScore();
					ThemeResource iconDocumentUwGroup = totalScore >= 5 ? greenIcon
							: grayIcon;
					documentsTabSheet.addTab(documentUwGroupPanel, " ("
							+ totalScore + ") " + documentUwGroup.getDescEn()
							+ " ", iconDocumentUwGroup);
					documentUwGroupsPanel.add(documentUwGroupPanel);
				}
			}
		}
	}

	/**
	 * Add documents Layout
	 * 
	 * @param quotation
	 */
	private void addContactsLayout(Quotation quotation) {
		contactsUwLayout.removeAllComponents();

		Panel covRecommendationPanel = new Panel(I18N.message("cov.recommendation"));
		HorizontalLayout covRecommendationsLayout = new HorizontalLayout();
		covRecommendationsLayout.setMargin(true);
		covRecommendationsLayout.addComponent(new Label(I18N.message("applicant.reputation")));
		covRecommendationsLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));

		covRecommendationsLayout.addComponent(new Label(I18N
				.message("guarantor.reputation")));
		covRecommendationsLayout.addComponent(ComponentFactory.getSpaceLayout(
				10, Unit.PIXELS));
		
		covRecommendationPanel.setContent(covRecommendationsLayout);
		
		contactsUwLayout.addComponent(covRecommendationPanel);
	}

	/**
	 * Add documents Layout
	 * 
	 * @param quotation
	 */
	private void addFieldCheckLayout(Quotation quotation) {
		contactFieldCheckLayout.removeAllComponents();
		contactFieldCheckLayout.setVisible(quotation.isFieldCheckPerformed());
		if (quotation.isFieldCheckPerformed()) {
			fieldCheckPanel = new FieldCheckPanel();
			contactFieldCheckLayout.addComponent(fieldCheckPanel);
			fieldCheckPanel.assignValues(quotation);
			if (contactsTabSheet.getTab(contactFieldCheckLayout) == null) {
				contactsTabSheet.addTab(contactFieldCheckLayout,
						I18N.message("field.check"));
			}
		} else {
			contactsTabSheet.removeComponent(contactFieldCheckLayout);
		}
	}

	@Override
	public void valueChange(ValueChangeEvent event) {

		txtUwNetIncome.setReadOnly(false);
		txtUwNetIncome.setValue(AmountUtils.format(getDouble(txtUwRevenu, 0.d)
				+ getDouble(txtUwAllowance, 0.d)
				- getDouble(txtUwBusinessExpenses, 0.d)));
		txtUwNetIncome.setReadOnly(true);

		txtUwDisposableIncome.setReadOnly(false);
		txtUwDisposableIncome.setValue(AmountUtils.format(getDouble(
				txtUwNetIncome, 0.d)
				- getDouble(txtUwPersonalExpenses, 0.d)
				- getDouble(txtUwFamilyExpenses, 0.d)
				- getDouble(txtUwLiability, 0.d)));
		txtUwDisposableIncome.setReadOnly(true);

		double underwriterRatio = getDouble(txtUwDisposableIncome, 0.d)
				/ monthlyInstallment;

		double maxUnderwriterRedRatio = config
				.getDouble("score.liability.uw_estimation_ratio[@maxred]");
		if (underwriterRatio < maxUnderwriterRedRatio) {
			lblUnderwriterRatio.setIcon(redIcon);
		} else {
			double maxUnderwriterOrangeRatio = config
					.getDouble("score.liability.uw_estimation_ratio[@maxorange]");
			if (underwriterRatio < maxUnderwriterOrangeRatio) {
				lblUnderwriterRatio.setIcon(orangeIcon);
			} else {
				lblUnderwriterRatio.setIcon(greenIcon);
			}
		}

		String amountStyle = "style=\"border:1px solid black;\" align=\"right\"";
		String underwriterDesc = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
		underwriterDesc += "<tr><td " + amountStyle + ">Net Income</td><td " + amountStyle + ">" + AmountUtils.format(getDouble(txtUwNetIncome, 0.d))
				+ "</td></tr>";
		underwriterDesc += "<tr><td " + amountStyle + ">Expenses</td><td " + amountStyle + ">"
				+ AmountUtils.format(getDouble(txtUwPersonalExpenses, 0.d) + getDouble(txtUwFamilyExpenses, 0.d)) + "</td></tr>";
		underwriterDesc += "<tr><td " + amountStyle + "><b>Ratio</b></td><td " + amountStyle + "><b>" + AmountUtils.format(underwriterRatio)
				+ "</b</td></tr>";
		underwriterDesc += "</table>";
		lblUnderwriterRatio.setDescription(underwriterDesc);

		// Guarantor
		txtUwNetIncomeGuarantor.setReadOnly(false);
		txtUwNetIncomeGuarantor.setValue(AmountUtils.format(getDouble(txtUwRevenuGuarantor, 0.d)
				+ getDouble(txtUwAllowanceGuarantor, 0.d)
				- getDouble(txtUwBusinessExpensesGuarantor, 0.d)));
		txtUwNetIncomeGuarantor.setReadOnly(true);

		txtUwDisposableIncomeGuarantor.setReadOnly(false);
		txtUwDisposableIncomeGuarantor.setValue(AmountUtils.format(getDouble(txtUwNetIncomeGuarantor, 0.d)
				- getDouble(txtUwPersonalExpensesGuarantor, 0.d)
				- getDouble(txtUwFamilyExpensesGuarantor, 0.d)
				- getDouble(txtUwLiabilityGuarantor, 0.d)));
		txtUwDisposableIncomeGuarantor.setReadOnly(true);

		double underwriterRatioGuarantor = getDouble(txtUwDisposableIncomeGuarantor, 0.d) / monthlyInstallmentGuarantor;

		double maxUnderwriterRedRatioGuarantor = config.getDouble("score.liability.uw_estimation_ratio[@maxred]");
		if (underwriterRatioGuarantor < maxUnderwriterRedRatioGuarantor) {
			lblUnderwriterRatioGuarantor.setIcon(redIcon);
		} else {
			double maxUnderwriterOrangeRatioGuarantor = config.getDouble("score.liability.uw_estimation_ratio[@maxorange]");
			if (underwriterRatioGuarantor < maxUnderwriterOrangeRatioGuarantor) {
				lblUnderwriterRatioGuarantor.setIcon(orangeIcon);
			} else {
				lblUnderwriterRatioGuarantor.setIcon(greenIcon);
			}
		}

		String amountStyleGuarantor = "style=\"border:1px solid black;\" align=\"right\"";
		String underwriterDescGuarantor = "<table height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; border-collapse:collapse;\">";
		underwriterDescGuarantor += "<tr><td " + amountStyleGuarantor + ">Net Income</td><td " + amountStyleGuarantor + ">"
				+ AmountUtils.format(getDouble(txtUwNetIncomeGuarantor, 0.d)) + "</td></tr>";
		underwriterDescGuarantor += "<tr><td " + amountStyleGuarantor + ">Expenses</td><td " + amountStyleGuarantor + ">"
				+ AmountUtils.format(getDouble(txtUwPersonalExpensesGuarantor, 0.d) + getDouble(txtUwFamilyExpensesGuarantor, 0.d))
				+ "</td></tr>";
		underwriterDescGuarantor += "<tr><td " + amountStyleGuarantor + "><b>Ratio</b></td><td " + amountStyleGuarantor + "><b>"
				+ AmountUtils.format(underwriterRatioGuarantor) + "</b</td></tr>";
		underwriterDescGuarantor += "</table>";
		lblUnderwriterRatioGuarantor.setDescription(underwriterDescGuarantor);
		// --------------------
	}

	@Override
	public void buttonClick(ClickEvent event) {
		EWkfStatus newQuotationStatus = null;
		boolean forManager = false;
		if (event.getButton() == btnApprove) {
			if (ProfileUtil.isUnderwriter()) {
				newQuotationStatus = QuotationWkfStatus.APU;
			} else {
				newQuotationStatus = QuotationWkfStatus.APS;
			}
			forManager = true;
		} else if (event.getButton() == btnReject) {
			if (ProfileUtil.isUnderwriter()) {
				newQuotationStatus = QuotationWkfStatus.REU;
			} else {
				newQuotationStatus = QuotationWkfStatus.REJ;
			}
		} else if (event.getButton() == btnRequestFieldCheck) {
			newQuotationStatus = QuotationWkfStatus.RFC;
		} else if (event.getButton() == btnApproveWithCondition) {
			if (ProfileUtil.isUnderwriter()) {
				newQuotationStatus = QuotationWkfStatus.AWU;
			} else {
				newQuotationStatus = QuotationWkfStatus.AWS;
			}
			forManager = true;
		} else if (event.getButton() == btnBackPos) {
			newQuotationStatus = QuotationWkfStatus.QUO;
		} else if (event.getButton() == btnBackUw) {
			newQuotationStatus = QuotationWkfStatus.PRO;
		}

		if (event.getButton() == btnSave) {
			quotationService.saveUnderwriter(getQuotation(quotation),
					getComments());
//			underwriterPanel.getQuotationFormPanel().displaySuccess();
			if (fieldCheckPanel != null) {
				fieldCheckPanel.updateSupportDecision();
				fieldCheckPanel.assignValues(quotation);
			}
		} else {
			showCommentFormPanel(event.getButton().getCaption(),
					newQuotationStatus, forManager);
		}
	}

	public NavigationPanel getNavigationPanel(
			NavigationPanel defaultNavigationPanel) {
		return navigationPanel;
	}
	/**
	 * for calculate TotalInsallAmount
	 * @param quotation
	 * @return
	 */
	public Double calTotalInstallmentAmount(Quotation quotation){
		Double insuranceFee = 0d;
		Double servicingFee = 0d;
		Double totalInsallmentAmount = 0d;
		int nbPaidPerYear = 0;
		int i=0;
		if (quotation != null) {
			//Query Service 
				services = new ArrayList<FinService>();
				BaseRestrictions<FinService> restrictions = new BaseRestrictions<FinService>(FinService.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				services = ENTITY_SRV.list(restrictions);
				double serviceAmount[] = new double[services.size()];
				for (FinService service : services) {
					com.nokor.efinance.core.quotation.model.QuotationService quotationService =  quotation.getQuotationService(service.getId());
					if(quotationService != null){
						serviceAmount[i] = quotationService.getTiPrice();
					}
					i++;
				}
				insuranceFee = serviceAmount[0];
				servicingFee = serviceAmount[1];
				
				if(quotation.getFrequency().getCode() == "annually"){
					nbPaidPerYear = 1;
				}else if(quotation.getFrequency().getCode() == "half.year"){
					nbPaidPerYear = 2;
				}else if(quotation.getFrequency().getCode() == "monthly"){
					nbPaidPerYear = 12;
				}else{
					nbPaidPerYear = 4;
				}
				Integer term = quotation.getTerm();
				
				if(term != null){
					insuranceFee = MyMathUtils.roundAmountTo((quotation.getTerm() / nbPaidPerYear) * insuranceFee) / quotation.getTerm();
					servicingFee = MyMathUtils.roundAmountTo(servicingFee / quotation.getTerm());	
				}
				
				totalInsallmentAmount = MyMathUtils.roundAmountTo(quotation.getTiInstallmentAmount() + insuranceFee + servicingFee);

		}
		return totalInsallmentAmount;
	}

}
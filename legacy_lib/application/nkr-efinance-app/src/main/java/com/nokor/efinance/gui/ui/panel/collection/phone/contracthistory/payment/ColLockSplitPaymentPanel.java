package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.EPromiseStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractPromise;
import com.nokor.efinance.core.contract.model.ContractSms;
import com.nokor.efinance.core.contract.model.Email;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.third.finwiz.client.sms.ClientSms;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColLockSplitPaymentPanel extends AbstractControlPanel implements FinServicesHelper, ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1967162223031841581L;
	
	private String USD_FORMAT = "###,##0.00";
	
	private static final String ITEM = I18N.message("item");
	private static final String DUE = I18N.message("due");
	
	private Contract contract;
	
	private LockSplit newLocksplit;
	private LockSplit oldLockSplit;
	
	private VerticalLayout allocatToLayout;
	private Button btnAdd;
	private Label lblTotalAmountValue;
	private Label lblEmailValue;
	private Label lblPhoneSmsValue;
	
	private SimpleTable<Entity> simpleTable;
	private AutoDateField dfDueDate;
	private Button btnCalculate;
	
	private CheckBox cbEmail;
	private CheckBox cbPhoneSMS;
	private CheckBox cbPromise;
	private AutoDateField dfValidFrom;
	private AutoDateField dfValidTo;
	
	private double totalAmount;
	private String sendEmail;
	private String phoneNumber;
	
	private Button btnSubmit;
	private Button btnCancel;
	private Map<Integer, LockSplitItem> mapLockSplitItems;
	
	private boolean needRefresh = false;
	
	private Window locksplitPopup;
	private ColPhoneLockSplitTabPanel colPhoneLockSplitTabPanel;
	
	private Map<JournalEvent, Double> mapItems;
	
	private Map<Integer, LockSplitItemLayout> mapLockSplitItemLayouts;
	private LockSplitItemLayout lockSplitItemLayout;
	private Integer index;
	
	private VerticalLayout messagePanel;

	/**
	 * 
	 * @param locksplitPopup
	 * @param colPhoneLockSplitTabPanel
	 */
	public ColLockSplitPaymentPanel(Window locksplitPopup, ColPhoneLockSplitTabPanel colPhoneLockSplitTabPanel) {
		this.locksplitPopup = locksplitPopup;
		this.colPhoneLockSplitTabPanel = colPhoneLockSplitTabPanel;
		init();
	}

	/**
	 * 
	 */
	public ColLockSplitPaymentPanel() {
		locksplitPopup = null;
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		allocatToLayout = new VerticalLayout();
		index = 0;
		
		btnAdd = ComponentLayoutFactory.getDefaultButton("add", FontAwesome.PLUS, 70);
		btnAdd.addClickListener(this);
		
		dfDueDate = ComponentFactory.getAutoDateField();
		btnCalculate = ComponentLayoutFactory.getDefaultButton("calculate", null, 70);
		btnCalculate.addClickListener(this);
		
		simpleTable = new SimpleTable<>(createColumnDefinition());
		simpleTable.setWidthUndefined();
		simpleTable.setPageLength(5);
		
		btnSubmit = ComponentLayoutFactory.getButtonStyle("submit", FontAwesome.CHECK, 70, "btn btn-success");
		btnSubmit.addClickListener(this);
		
		btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.TIMES, 70, "btn btn-danger");
		btnCancel.addClickListener(this);
		
		cbEmail = new CheckBox(I18N.message("send.email"));
		cbPhoneSMS = new CheckBox(I18N.message("send.sms"));
		cbPromise = new CheckBox(I18N.message("promise"));
		dfValidFrom = ComponentFactory.getAutoDateField();
		dfValidTo = ComponentFactory.getAutoDateField();
		dfValidFrom.setValue(DateUtils.today());
		dfValidTo.setValue(DateUtils.addMonthsDate(dfValidFrom.getValue(), 1));
		
		dfValidFrom.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 6151501738938117707L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (dfValidFrom.getValue() != null) {
					dfValidTo.setValue(DateUtils.addMonthsDate(dfValidFrom.getValue(), 1));
				} else {
					dfValidTo.setValue(null);
				}
			}
		});
		
		lblEmailValue = getLabelValue();
		lblPhoneSmsValue = getLabelValue();
		
		HorizontalLayout validLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		validLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("valid.from"));
		validLayout.addComponent(dfValidFrom);
		validLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("to"));
		validLayout.addComponent(dfValidTo);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnSubmit);
		buttonLayout.addComponent(btnCancel);
		
		Label lblTotalAmount = ComponentLayoutFactory.getLabelCaption(I18N.message("total.amount") + " : " );
		lblTotalAmountValue = getLabelValue();
		lblTotalAmountValue.setValue(getDescription(MyNumberUtils.formatDoubleToString(0d, USD_FORMAT)));
		
		HorizontalLayout totalAmountLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		totalAmountLayout.addComponent(lblTotalAmount);
		totalAmountLayout.addComponent(lblTotalAmountValue);
		totalAmountLayout.setComponentAlignment(lblTotalAmountValue, Alignment.MIDDLE_LEFT);
		
		VerticalLayout mainAllocatToLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		mainAllocatToLayout.setWidth(420, Unit.PIXELS);
		mainAllocatToLayout.addComponent(allocatToLayout);
		mainAllocatToLayout.addComponent(btnAdd);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("allocat.to"));
		fieldSet.setContent(mainAllocatToLayout);
		
		HorizontalLayout itemFormLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		itemFormLayout.addComponent(fieldSet);
		itemFormLayout.addComponent(createDuePanel());
		
		HorizontalLayout emailLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		emailLayout.addComponent(cbEmail);
		emailLayout.addComponent(lblEmailValue);
		emailLayout.setComponentAlignment(lblEmailValue, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout smsLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		smsLayout.addComponent(cbPhoneSMS);
		smsLayout.addComponent(lblPhoneSmsValue);
		smsLayout.setComponentAlignment(lblPhoneSmsValue, Alignment.MIDDLE_LEFT);
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		HorizontalLayout cbLayout = new HorizontalLayout();
		cbLayout.setSpacing(true);
		cbLayout.addComponent(emailLayout);
		cbLayout.addComponent(smsLayout);
		cbLayout.addComponent(cbPromise);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(itemFormLayout);
		mainLayout.addComponent(totalAmountLayout);
		mainLayout.addComponent(cbLayout);
		mainLayout.addComponent(validLayout);
		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
		
		FieldSet lockSplitFieldSet = new FieldSet();
		lockSplitFieldSet.setLegend(I18N.message("new.locksplit"));
		lockSplitFieldSet.setContent(mainLayout);
		
		Panel mainPanel = new Panel();
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		mainPanel.setContent(lockSplitFieldSet);
		
		addComponent(mainPanel);
	}

	/**
	 * 
	 * @param cashflows
	 */
	private Map<JournalEvent, Double> getMapItems(List<Cashflow> cashflows) {
		Map<JournalEvent, Double> mapItems = new HashMap<JournalEvent, Double>();
		if (cashflows != null && !cashflows.isEmpty()) {
			double totalInstallment = 0d;
			mapItems = new HashMap<JournalEvent, Double>();
			for (Cashflow cashflow : cashflows) {
				JournalEvent journalEvent = cashflow.getJournalEvent();
				if (journalEvent != null) {
					if (!mapItems.containsKey(journalEvent)) {
						totalInstallment += MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
						mapItems.put(journalEvent, totalInstallment);
					} else {
						totalInstallment = mapItems.get(journalEvent);
						totalInstallment += MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
						mapItems.replace(journalEvent, totalInstallment);
					}
				}
			}
		}
		return mapItems;
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	public void assignValues(LockSplit lockSplit) {
		mapLockSplitItems = new HashMap<Integer, LockSplitItem>();
		mapLockSplitItemLayouts = new HashMap<Integer, LockSplitItemLayout>();
		allocatToLayout.removeAllComponents();
		this.reset();
		totalAmount = 0d;
		phoneNumber = "";
		sendEmail = "";
		this.contract = lockSplit.getContract();
		
		if (contract != null) {
			Applicant applicant = contract.getApplicant();
			if (applicant != null && (EApplicantCategory.INDIVIDUAL.equals(applicant.getApplicantCategory())
					|| EApplicantCategory.GLSTAFF.equals(applicant.getApplicantCategory()))) {
				Individual individual = applicant.getIndividual();
				if (individual != null) {
					phoneNumber = individual.getIndividualPrimaryContactInfo();
					lblPhoneSmsValue.setValue(getDescription(phoneNumber));
					List<IndividualContactInfo> individualContactInfos = INDIVI_SRV.getIndividualContactInfos(individual.getId());
					for (IndividualContactInfo individualContactInfo : individualContactInfos) {
						ContactInfo contactInfo = individualContactInfo.getContactInfo();
						if (ETypeContactInfo.EMAIL.equals(contactInfo.getTypeInfo())) {
							sendEmail = contactInfo.getValue();
							lblEmailValue.setValue(getDescription(sendEmail));
						}
					}
				}
			}
		}
		
		cbEmail.setValue(lockSplit.isSendEmail());
 		cbPhoneSMS.setValue(lockSplit.isSendSms());
		cbPromise.setValue(lockSplit.isPromise());
		setTableIndexedContainer();
	
		if (lockSplit.getId() != null) {
			this.oldLockSplit = lockSplit;
			dfValidFrom.setValue(lockSplit.getFrom());
			dfValidTo.setValue(lockSplit.getTo());
			List<LockSplitItem> items = lockSplit.getItems();
			if (items != null && !items.isEmpty()) {
				for (LockSplitItem item : items) {
					lockSplitItemLayout = new LockSplitItemLayout(this, 
							LCK_SPL_SRV.copyLocksplitItem(LockSplitItem.createInstance(), item), mapItems, index);
					addItemLayoutToMap(index, lockSplitItemLayout);
					addItemToMap(index, lockSplitItemLayout.getLockSplitItem());
					index = index + 1;
				}
			}
			addNewItemLayout();
			displayTotalAmount();
			
			newLocksplit = LockSplit.createInstance();
			newLocksplit.setContract(this.contract);
		} else {
			newLocksplit = lockSplit;
		}
	}
	
	/**
	 * 
	 * @param index
	 * @param item
	 */
	private void addItemToMap(int index, LockSplitItem item) {
		if (!mapLockSplitItems.containsKey(index)) {
			mapLockSplitItems.put(index, item);
		} else {
			mapLockSplitItems.replace(index, item);
		}
	}
	
	/**
	 * 
	 * @param index
	 * @param itemLayout
	 */
	private void addItemLayoutToMap(int index, LockSplitItemLayout itemLayout) {
		if (!mapLockSplitItemLayouts.containsKey(index)) {
			mapLockSplitItemLayouts.put(index, itemLayout);
		} else {
			mapLockSplitItemLayouts.replace(index, itemLayout);
		}
	}
	
	/**
	 * 
	 */
	private void addNewItemLayout() {
		if (mapLockSplitItemLayouts != null && !mapLockSplitItemLayouts.isEmpty()) {
			for (Integer key : mapLockSplitItemLayouts.keySet()) {
				allocatToLayout.addComponent(mapLockSplitItemLayouts.get(key));
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	protected void displayTotalAmount() {
		totalAmount = 0d;
		if (mapLockSplitItemLayouts != null && !mapLockSplitItemLayouts.isEmpty()) {
			for (Integer key : mapLockSplitItemLayouts.keySet()) {
				totalAmount += mapLockSplitItemLayouts.get(key).getTotalAmount();
			}
		}
		lblTotalAmountValue.setValue(getDescription(MyNumberUtils.formatDoubleToString(totalAmount, USD_FORMAT)));
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(ITEM, I18N.message("item"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(DUE, I18N.message("due"), String.class, Align.RIGHT, 80));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return
	 */
	private FieldSet createDuePanel() {
		HorizontalLayout dueLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		dueLayout.addComponent(dfDueDate);
		dueLayout.addComponent(btnCalculate);
		
		VerticalLayout dueMainLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		dueMainLayout.addComponent(dueLayout);
		dueMainLayout.addComponent(simpleTable);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("dues"));
		fieldSet.setContent(dueMainLayout);
		
		return fieldSet;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer() {
		mapItems = new HashMap<JournalEvent, Double>();
		if (contract != null) {
			if (dfDueDate.getValue() != null) {
				mapItems = getMapItems(CASHFLOW_SRV.getCashflowsToPaidLessThanToday(contract.getId(), dfDueDate.getValue()));
			} else {
				mapItems = getMapItems(CASHFLOW_SRV.getCashflowsToPaid(contract.getId(), contract.getStartDate(), contract.getEndDate()));
			}
			Collection collection = contract.getCollection();
			if (collection != null) {
				double penaltyAmount = MyNumberUtils.getDouble(collection.getTiPenaltyAmount());
				if (penaltyAmount > 0) {
					JournalEvent penalty = ENTITY_SRV.getByCode(JournalEvent.class, "02");
					if (!mapItems.containsKey(penalty)) {
						mapItems.put(penalty, penaltyAmount);
					}
				}
			}
		}
		if (mapLockSplitItemLayouts != null && !mapLockSplitItemLayouts.isEmpty()) {
			for (Integer key : mapLockSplitItemLayouts.keySet()) {
				mapLockSplitItemLayouts.get(key).setMapItems(mapItems);
				
				List<JournalEvent> events = new ArrayList<JournalEvent>();
				if (mapItems != null && !mapItems.isEmpty()) {
					for(JournalEvent journalEvent : mapItems.keySet()) {
						events.add(journalEvent);
					}
				}
				
				if (mapLockSplitItemLayouts.get(key).getCbxJournalEvent().getSelectedEntity() == null) {
					mapLockSplitItemLayouts.get(key).getCbxJournalEvent().setValues(events);
				}
			}
		}
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (mapItems != null && !mapItems.isEmpty()) {
			int index = 0;
			for(JournalEvent journalEvent : mapItems.keySet()) {	
				Item item = indexedContainer.addItem(index);
				item.getItemProperty(ITEM).setValue(journalEvent.getCode() + " - " + journalEvent.getDescLocale());
				double totalInstallment = MyNumberUtils.getDouble(mapItems.get(journalEvent));
				item.getItemProperty(DUE).setValue(MyNumberUtils.formatDoubleToString(totalInstallment, USD_FORMAT));
				index++;
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(getDefaultString(value));
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 */
	private double calculateVatAmount() {
		Amount vatAmount = MyMathUtils.calculateFromAmountIncl(totalAmount, contract.getVatValue());
		return vatAmount.getVatAmount();
	}
	
	/**
	 * Submit
	 */
	private void submit() {
		if (validation()) {
			SecUser user = UserSessionManager.getCurrentUser();
			double totalVatAmount = calculateVatAmount();
			newLocksplit.setCreatedBy(user.getLogin());
			newLocksplit.setFrom(dfValidFrom.getValue());
			newLocksplit.setTo(dfValidTo.getValue());
			newLocksplit.setTotalAmount(totalAmount);
			newLocksplit.setTotalVatAmount(totalVatAmount);
			newLocksplit.setSendEmail(cbEmail.getValue());
			newLocksplit.setSendSms(cbPhoneSMS.getValue());
			newLocksplit.setPromise(cbPromise.getValue());
			
			List<LockSplitItem> items = new ArrayList<LockSplitItem>();
			if (mapLockSplitItems != null && !mapLockSplitItems.isEmpty()) {
				for (Integer key : mapLockSplitItems.keySet()) {
					items.add(mapLockSplitItems.get(key));
				}
			}
			newLocksplit.setItems(items);
			
			if (cbPhoneSMS.getValue()) {
				newLocksplit.setSendSms(true);
				createContractSMS();
			}
			if (cbEmail.getValue()) {
				createContractEmail();
			}
			if (cbPromise.getValue()) {
				createPromise();
			}
			
			try {
				if (oldLockSplit != null) {
					LCK_SPL_SRV.updateLockSplit(oldLockSplit, newLocksplit);
				} else {
					LCK_SPL_SRV.saveLockSplit(newLocksplit);
				}
				
				ComponentLayoutFactory.displaySuccessfullyMsg();
				LockSplit lckSplit = LockSplit.createInstance();
				lckSplit.setContract(contract);
				assignValues(lckSplit);
				needRefresh = true;
				if (locksplitPopup != null) {
					locksplitPopup.close();
					colPhoneLockSplitTabPanel.refereshValues(contract);
				}
			} catch (Exception e) {
				e.printStackTrace();
				ComponentLayoutFactory.displayErrorMsg(e.getMessage());
			}
		} else {
			displayAllErrorsPanel();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validation() {
		ValidateUtil.clearErrors();
		ValidateUtil.checkMandatoryDateField(dfValidFrom, "valid.from");
		ValidateUtil.checkMandatoryDateField(dfValidTo, "to");
		
		if (this.contract == null) {
			ValidateUtil.addError(I18N.message("contract.cannot.be.null"));
		}
		
		if (mapLockSplitItemLayouts != null && !mapLockSplitItemLayouts.isEmpty()) {
			for (Integer key : mapLockSplitItemLayouts.keySet()) {
				mapLockSplitItemLayouts.get(key).validation();
			}
		}
		
		Date paymentDateFromSelected = DateUtils.getDateAtBeginningOfDay(dfValidFrom.getValue());
		Date paymentDateToSelected = DateUtils.getDateAtBeginningOfDay(dfValidTo.getValue());
		if (paymentDateFromSelected != null && paymentDateToSelected != null) {
			if (paymentDateFromSelected.after(paymentDateToSelected)) {
				ValidateUtil.addError(I18N.message("payment.date.from.less.than.payment.date.to"));
			} else {
				if (paymentDateFromSelected.before(DateUtils.getDateAtBeginningOfDay(DateUtils.today()))) {
					if (this.newLocksplit.getId() == null) {
						ValidateUtil.addError(I18N.message("payment.date.from.less.than.today", 
								new String[]{ DateUtils.getDateLabel(DateUtils.today(), DateUtils.FORMAT_DDMMYYYY_SLASH) }));
					}
				} else if (paymentDateToSelected.before(DateUtils.getDateAtBeginningOfDay(DateUtils.today()))) {
					if (this.newLocksplit.getId() == null) {
						ValidateUtil.addError(I18N.message("payment.date.to.less.than.today", 
								new String[]{ DateUtils.getDateLabel(DateUtils.today(), DateUtils.FORMAT_DDMMYYYY_SLASH) }));
					}
				}
			} 
		} 
		if (paymentDateFromSelected != null) {
			Date maxPaymentDateFrom = DateUtils.getDateAtBeginningOfDay(DateUtils.addMonthsDate(DateUtils.today(), 1));
			if (paymentDateFromSelected.after(maxPaymentDateFrom)) {
				ValidateUtil.addError(I18N.message("valid.from.max.1month", 
						new String[]{ DateUtils.getDateLabel(maxPaymentDateFrom, DateUtils.FORMAT_DDMMYYYY_SLASH) }));
			}
		}
		if (paymentDateToSelected != null) {
			Date maxPaymentDateTo = DateUtils.getDateAtBeginningOfDay(DateUtils.addMonthsDate(DateUtils.today(), 2));
			if (paymentDateToSelected.after(maxPaymentDateTo)) {
				ValidateUtil.addError(I18N.message("valid.to.max.2month", 
						new String[]{ DateUtils.getDateLabel(maxPaymentDateTo, DateUtils.FORMAT_DDMMYYYY_SLASH) }));
			}
		}
		
		return StringUtils.isEmpty(ValidateUtil.getErrorMessages());
	}

	/**
	 * create locksplit contract sms
	 */
	private void createContractSMS() {
		SecUser user = UserSessionManager.getCurrentUser();
		ContractSms contractSms = ContractSms.createInstance();
		contractSms.setSendTo(EApplicantType.C.getDesc());
		contractSms.setPhoneNumber(phoneNumber);
		contractSms.setContract(contract);
		contractSms.setUserLogin(user.getLogin());
		contractSms.setMessage("Locksplit Create");
		try {
			ClientSms.sendSms(contractSms.getPhoneNumber(), contractSms.getMessage(), user.getDesc());
			NOTE_SRV.saveOrUpdateSMS(contractSms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * create email for contract locksplit
	 */
	private void createContractEmail() {
		SecUser user = UserSessionManager.getCurrentUser();
		Email email = new Email();
		email.setContract(contract);
		email.setUserLogin(user.getLogin());
		email.setSendEmail(sendEmail);
		email.setSubject("Locksplit");
		email.setSendDate(DateUtils.today());
		email.setMessage("Locksplit");
		try {
			ENTITY_SRV.saveOrUpdate(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * create promise from locksplit
	 */
	private void createPromise() {
		SecUser user = UserSessionManager.getCurrentUser();
		ContractPromise promise = ContractPromise.createInstance();
		promise.setPromiseAmount(totalAmount);
		promise.setContract(contract);
		promise.setPromiseStatus(EPromiseStatus.PENDING);
		promise.setPromiseDate(dfValidTo.getValue());
		promise.setCreatedBy(user.getLogin());
		promise.setRemark("Create From Locksplit");
		try {
			ENTITY_SRV.saveOrUpdate(promise);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		removedAllErrorsPanel();
		lblPhoneSmsValue.setValue(StringUtils.EMPTY);
		lblEmailValue.setValue(StringUtils.EMPTY);
		cbEmail.setValue(false);
		cbPhoneSMS.setValue(false);
		cbPromise.setValue(false);
		dfValidFrom.setValue(DateUtils.today());
		dfValidTo.setValue(DateUtils.addMonthsDate(dfValidFrom.getValue(), 1));
		dfDueDate.setValue(null);
	}
	
	/** 
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			lockSplitItemLayout = new LockSplitItemLayout(this, LockSplitItem.createInstance(), mapItems, index);
			addItemLayoutToMap(index, lockSplitItemLayout);
			addItemToMap(index, lockSplitItemLayout.getLockSplitItem());
			addNewItemLayout();
			index = index + 1;
		} else if (event.getButton() == btnCalculate) {
			setTableIndexedContainer();
		} else if (event.getButton() == btnCancel) {
			if (locksplitPopup != null) {
				locksplitPopup.close();
			} else {
				this.reset();
			}
		} else if (event.getButton() == btnSubmit) {
			submit();
		}
	}
	
	/**
	 * 
	 * @param index
	 */
	protected void removeItemByIndexed(int index) {
		mapLockSplitItemLayouts.remove(index);
		mapLockSplitItems.remove(index);
		displayTotalAmount();
	}

	/**
	 * Display error message panel
	 */
	protected void displayAllErrorsPanel() {
		messagePanel.removeAllComponents();
		Label messageLabel = ComponentFactory.getHtmlLabel(ValidateUtil.getErrorMessages());
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}
	
	/**
	 * Removed error message panel
	 */
	protected void removedAllErrorsPanel() {
		ValidateUtil.clearErrors();
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
	}

	/**
	 * @return the needRefresh
	 */
	public boolean isNeedRefresh() {
		return needRefresh;
	}

	/**
	 * @param needRefresh the needRefresh to set
	 */
	public void setNeedRefresh(boolean needRefresh) {
		this.needRefresh = needRefresh;
	}
}

package com.nokor.efinance.gui.ui.panel.contract;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.gui.ui.panel.collection.delinquencies.DelinquenciesPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.closing.ColPhoneClosingPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.ColPhoneContactPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.ColPhoneContactResultPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.isr.ISRTabPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal.ColPhoneLegalFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.letters.ColPhoneLettersFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment.ColPhoneLockSplitPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.redumption.RedemptionPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.transactions.TransactionPanel;
import com.nokor.efinance.gui.ui.panel.contract.accounting.AccountingPanel;
import com.nokor.efinance.gui.ui.panel.contract.asset.AssetTabPanel;
import com.nokor.efinance.gui.ui.panel.contract.asset.registration.AssetRegistrationPanel;
import com.nokor.efinance.gui.ui.panel.contract.document.DocumentPanel;
import com.nokor.efinance.gui.ui.panel.contract.dues.DuesPanel;
import com.nokor.efinance.gui.ui.panel.contract.expenses.ExpensesPanel;
import com.nokor.efinance.gui.ui.panel.contract.loan.LoanFormPanel;
import com.nokor.efinance.gui.ui.panel.contract.loan.LoanSummaryPanel;
import com.nokor.efinance.gui.ui.panel.contract.notes.NotePanel;
import com.nokor.efinance.gui.ui.panel.contract.notes.appointment.AppointmentFormPanel;
import com.nokor.efinance.gui.ui.panel.contract.promises.PromisesPanel;
import com.nokor.efinance.gui.ui.panel.contract.queue.QueuePanel;
import com.nokor.efinance.gui.ui.panel.contract.summary.SummaryPanel;
import com.nokor.efinance.gui.ui.panel.contract.task.ContractTaskPanel;
import com.nokor.efinance.gui.ui.panel.contract.user.UsersPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class ContractFormPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 85731513526887135L;

	private SummaryPanel summaryPanel;
	
	private ContractTaskPanel taskPanel;
	
	private VerticalLayout rightLayout;
	private VerticalLayout contentLayout;
	
	private Button btnSummary;
	private Button btnLoan;
	private Button btnUser;
	private Button btnTask;
	private Button btnDues;
	private Button btnAsset;
	private Button btnRegistration;
	private Button btnInsurance;
	private Button btnPayment;
	private Button btnTransaction;
	private Button btnForeClosure;
	private Button btnLegal;
	private Button btnNote;
	private Button btnPromise;
	private Button btnCommunication;
	private Button btnDocument;
	private Button btnAccounting;
	private Button btnDelinquencies;
	private Button btnQueue;
	private Button btnExpenses;
	private Button btnClosing;
	private Button btnRedemption;
	
	private LoanSummaryPanel loanSummaryPanel;
	private LoanFormPanel loanFormPanel;
	
	private NotePanel notesPanel;
	
	private AccountingPanel accountingPanel;
	
	private ColPhoneLockSplitPanel colPhoneLocksplitPanel;
	
	private DuesPanel duesPanel;

	private Contract contract;
	
	/**
	 * 	
	 */
	public ContractFormPanel() {
		summaryPanel = new SummaryPanel(this);
		taskPanel = new ContractTaskPanel();
		if (ProfileUtil.isCMProfile()) {
			taskPanel.setVisible(false);
		}
		loanFormPanel = new LoanFormPanel(this);
		
		FieldSet summaryFieldSet = new FieldSet();
		summaryFieldSet.setLegend(I18N.message("recap"));
		summaryFieldSet.setContent(summaryPanel);
		
		Panel summaryFieldSetPanel = new Panel(summaryFieldSet);
		summaryFieldSetPanel.setStyleName(Reindeer.PANEL_LIGHT);

		notesPanel = new NotePanel();
		
		accountingPanel = new AccountingPanel();
		
		colPhoneLocksplitPanel = new ColPhoneLockSplitPanel();
		
		duesPanel = new DuesPanel();
		
		rightLayout = new VerticalLayout();
		contentLayout = new VerticalLayout();
		contentLayout.setMargin(new MarginInfo(false, false, true, true));
		contentLayout.addComponent(rightLayout);
		
		loanSummaryPanel = new LoanSummaryPanel();
		
		Panel leftPanel = new Panel(getButtonPanel());
		leftPanel.setStyleName(Reindeer.PANEL_LIGHT);
		leftPanel.setSizeUndefined();
		
		HorizontalLayout horSplitPanel = new HorizontalLayout();
		horSplitPanel.setSizeFull();
		horSplitPanel.addComponent(leftPanel);
		horSplitPanel.addComponent(contentLayout);
		horSplitPanel.setExpandRatio(contentLayout, 1);
		
		setMargin(true);
		setSpacing(true);
		addComponent(summaryFieldSetPanel);
		addComponent(taskPanel);
		addComponent(horSplitPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getButtonPanel() {
		btnSummary = getButtonMenu("summary");
		btnLoan = getButtonMenu("loan");
		btnUser = getButtonMenu("users");
		btnTask = getButtonMenu("tasks");
		btnDues = getButtonMenu("dues");
		btnAsset = getButtonMenu("asset");
		btnRegistration = getButtonMenu("registration");
		btnInsurance = getButtonMenu("insurance");
		btnPayment = getButtonMenu("payment");
		btnTransaction = getButtonMenu("transactions");
		btnForeClosure = getButtonMenu("foreclosure");
		btnForeClosure.setVisible(false);
		btnLegal = getButtonMenu("legal");
		btnNote = getButtonMenu("note");
		btnPromise = getButtonMenu("promise");
		btnCommunication = getButtonMenu("communication");
		btnDocument = getButtonMenu("document");
		btnAccounting = getButtonMenu("accounting");
//		btnAccounting.setVisible(false);
		btnDelinquencies = getButtonMenu("delinquencies");
		btnQueue = getButtonMenu("queues");
		btnExpenses = getButtonMenu("expenses");
		btnExpenses.setVisible(false);
		btnClosing = getButtonMenu("closing");
		btnRedemption = getButtonMenu("redemption");
		VerticalLayout vLayout = new VerticalLayout(btnSummary, btnLoan, btnUser, btnTask, btnAsset, btnRegistration,
				btnInsurance, btnDues, btnPayment, btnTransaction, btnForeClosure, btnLegal, btnNote, btnPromise, btnCommunication,
				btnDocument, btnAccounting, btnDelinquencies, btnQueue, btnExpenses, btnClosing, btnRedemption);
		vLayout.setMargin(new MarginInfo(true, false, true, false));
		return vLayout;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Button getButtonMenu(String caption) {
		Button button = new NativeButton("<h3 style=\"text-align: left; margin:2px 0\">" + I18N.message(caption) + "</h3>");
		button.setStyleName("btn btn-success button-small");
		button.setWidth(110, Unit.PIXELS);
		button.setCaptionAsHtml(true);
		button.addClickListener(this);
		return button;
	}
	
	/**
	 * 
	 * @param conId
	 * @param isDefault
	 */
	public void assignValues(Long conId, boolean isDefault) {
		this.reset();
		if (conId != null) {
			this.contract = CONT_SRV.getById(Contract.class, conId);
			loanSummaryPanel.assignValues(contract);
			summaryPanel.assignValues(contract);
			loanFormPanel.setLoanContent(contract);
			setVisibleButtonMenu(!ContractUtils.isBeforeActive(contract) && !ContractUtils.isHoldPayment(contract));
		}
		taskPanel.assignValues(conId);
		if (isDefault) {
			btnSummary.setEnabled(false);
			this.setDisplayTab(loanSummaryPanel, "summary");
		} 
		
		if (ContractUtils.isBeforeActive(contract) || ContractUtils.isHoldPayment(contract)) {
			btnLoan.setEnabled(false);
			rightLayout.removeComponent(loanSummaryPanel);
			this.setDisplayTab(loanFormPanel, "loan");
		}
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		rightLayout.removeAllComponents();
		rightLayout.setVisible(true);
		contentLayout.setMargin(new MarginInfo(false, false, true, true));
		
		setEnabledButton();
		if (event.getButton().equals(btnSummary)) {
			btnSummary.setEnabled(false);
			loanSummaryPanel.assignValues(contract);
			this.setDisplayTab(loanSummaryPanel, "summary");
		} else if (event.getButton().equals(btnLoan)) {
			btnLoan.setEnabled(false);
			this.setDisplayTab(loanFormPanel, "loan");
		} else if (event.getButton().equals(btnUser)) {
			btnUser.setEnabled(false);
			UsersPanel usersPanel = new UsersPanel(summaryPanel);
			usersPanel.assignValues(contract);
			this.setDisplayTab(usersPanel, "users");
		} else if (event.getButton().equals(btnTask)) {
			btnTask.setEnabled(false);
			ColPhoneContactResultPanel taskPanel = new ColPhoneContactResultPanel();
			taskPanel.assignValues(contract);
			this.setDisplayTab(taskPanel, "task");
		} else if (event.getButton().equals(btnAsset)) {
			btnAsset.setEnabled(false);
			AssetTabPanel assetTabPanel = new AssetTabPanel();
			assetTabPanel.assignValues(contract);
			this.setDisplayTab(assetTabPanel, "asset");
		} else if (event.getButton().equals(btnRegistration)) {
			btnRegistration.setEnabled(false);
			AssetRegistrationPanel registrationPanel = new AssetRegistrationPanel();
			registrationPanel.assignValues(contract, null);
			this.setDisplayTab(registrationPanel, "registration");
		} else if (event.getButton().equals(btnInsurance)) {
			btnInsurance.setEnabled(false);
			ISRTabPanel isrTabPanel = new ISRTabPanel();
			isrTabPanel.assignValues(contract);
			this.setDisplayTab(isrTabPanel, "insurance");
		} else if (event.getButton().equals(btnPayment)) {
			btnPayment.setEnabled(false);
			colPhoneLocksplitPanel.assignValue(contract);
			this.setDisplayTab(colPhoneLocksplitPanel);
		} else if (event.getButton().equals(btnTransaction)) {
			btnTransaction.setEnabled(false);
			TransactionPanel transactionPanel = new TransactionPanel();
			transactionPanel.assignValues(contract);
			this.setDisplayTab(transactionPanel, "transactions");
		} else if (event.getButton().equals(btnForeClosure)) {
			btnForeClosure.setEnabled(false);
			this.setDisplayTab(new VerticalLayout(), "foreclosure");
		} else if (event.getButton().equals(btnLegal)) {
			btnLegal.setEnabled(false);
			ColPhoneLegalFormPanel legalFormPanel = new ColPhoneLegalFormPanel();
			legalFormPanel.assignValue(contract);
			this.setDisplayTab(legalFormPanel, "legal");
		} else if (event.getButton().equals(btnNote)) {
			btnNote.setEnabled(false);
			notesPanel.assignValues(contract);
			this.setDisplayTab(notesPanel);
		} else if (event.getButton().equals(btnPromise)) {
			btnPromise.setEnabled(false);
			PromisesPanel promisesPanel = new PromisesPanel();
			promisesPanel.assignValues(contract);
			this.setDisplayTab(promisesPanel, "promise");
		} else if (event.getButton().equals(btnCommunication)) {
			btnCommunication.setEnabled(false);
			ColPhoneContactPanel contactPanel = new ColPhoneContactPanel(true);
			ColPhoneLettersFormPanel colPhoneLettersFormPanel = new ColPhoneLettersFormPanel();
			AppointmentFormPanel appointmentFormPanel = new AppointmentFormPanel(null);
			contactPanel.assingValues(contract);
			colPhoneLettersFormPanel.assignValue(contract);
			appointmentFormPanel.assignValues(contract);
			TabSheet tabContact = new TabSheet();			
			tabContact.addTab(contactPanel, I18N.message("contact"));
			tabContact.addTab(colPhoneLettersFormPanel, I18N.message("letters"));
			tabContact.addTab(appointmentFormPanel, I18N.message("appointments"));
			this.setDisplayTab(tabContact);			
		} else if (event.getButton().equals(btnDocument)) {
			btnDocument.setEnabled(false);
			DocumentPanel documentPanel = new DocumentPanel();
			documentPanel.assignValues(contract);
			this.setDisplayTab(documentPanel, "document");
		} else if (event.getButton().equals(btnAccounting)) {
			btnAccounting.setEnabled(false);
			this.setDisplayTab(accountingPanel);
		} else if (event.getButton() == btnDelinquencies) {
			btnDelinquencies.setEnabled(false);
			DelinquenciesPanel delinquenciesPanel = new DelinquenciesPanel();
			delinquenciesPanel.assignValues(contract);
			this.setDisplayTab(delinquenciesPanel, I18N.message("delinquencies"));
		} else if (event.getButton() == btnDues) {
			btnDues.setEnabled(false);
			duesPanel.assignValues(contract);
			this.setDisplayTab(duesPanel);
		} else if (event.getButton() == btnQueue) {
			btnQueue.setEnabled(false);
			QueuePanel queuePanel = new QueuePanel();
			this.setDisplayTab(queuePanel, I18N.message("queue"));
		} else if (event.getButton() == btnExpenses) {
			btnExpenses.setEnabled(false);
			ExpensesPanel expensesPanel = new ExpensesPanel();
			expensesPanel.assignValues(contract);
			this.setDisplayTab(expensesPanel, I18N.message("expenses"));
		} else if (event.getButton() == btnClosing) {
			btnClosing.setEnabled(false);
			ColPhoneClosingPanel closingPanel = new ColPhoneClosingPanel();
			this.setDisplayTab(closingPanel, I18N.message("closing"));
		} else if (event.getButton() == btnRedemption) {
			btnRedemption.setEnabled(false);
			RedemptionPanel redemptionPanel = new RedemptionPanel();
			redemptionPanel.assignValue(contract);
			this.setDisplayTab(redemptionPanel, I18N.message("redemption"));
		}
	}
	
	/**
	 * 
	 * @param component
	 */
	private void setDisplayTab(Component component) {
		rightLayout.addComponent(component);
	}
	
	/**
	 * 
	 * @param component
	 * @param caption
	 */
	private void setDisplayTab(Component component, String caption) {
		rightLayout.addComponent(component);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		rightLayout.removeAllComponents();
		setEnabledButton();
		summaryPanel.reset();
		loanSummaryPanel.reset();
	}
	
	/**
	 * 
	 * @param isActivated
	 */
	public void setVisibleButtonMenu(boolean isActivated) {
		btnSummary.setVisible(isActivated);
		btnDues.setVisible(isActivated);
		btnRegistration.setVisible(isActivated);
		btnInsurance.setVisible(isActivated);
		btnPayment.setVisible(isActivated);
		btnTransaction.setVisible(isActivated);
		btnForeClosure.setVisible(false);
		btnLegal.setVisible(isActivated);
		btnPromise.setVisible(isActivated);
		btnAccounting.setVisible(isActivated);
		btnDelinquencies.setVisible(isActivated);
		btnQueue.setVisible(isActivated);
		btnExpenses.setVisible(false);
		btnClosing.setVisible(isActivated);
		btnRedemption.setVisible(isActivated);
	}
	
	/**
	 * 
	 */
	private void setEnabledButton() {
		btnSummary.setEnabled(true);
		btnLoan.setEnabled(true);
		btnUser.setEnabled(true);
		btnTask.setEnabled(true);
		btnDues.setEnabled(true);
		btnAsset.setEnabled(true);
		btnRegistration.setEnabled(true);
		btnInsurance.setEnabled(true);
		btnPayment.setEnabled(true);
		btnTransaction.setEnabled(true);
		btnForeClosure.setEnabled(true);
		btnLegal.setEnabled(true);
		btnNote.setEnabled(true);
		btnPromise.setEnabled(true);
		btnCommunication.setEnabled(true);
		btnDocument.setEnabled(true);
		btnAccounting.setEnabled(true);
		btnDelinquencies.setEnabled(true);
		btnQueue.setEnabled(true);
		btnExpenses.setEnabled(true);
		btnClosing.setEnabled(true);
		btnRedemption.setEnabled(true);
	}
	
	/**
	 * @return the btnSummary
	 */
	public Button getBtnSummary() {
		return btnSummary;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void setRefreshContractData(Contract contract) {
		this.contract = contract;
		CONT_SRV.refresh(this.contract);
		summaryPanel.refreshContractData(this.contract);
	} 
}


package com.nokor.efinance.gui.ui.panel.payment.blocked.identify.locksplit;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MLockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.payment.blocked.identify.allocation.IdentifyPaymentAllocationPanel;
import com.nokor.efinance.gui.ui.panel.payment.blocked.identify.dues.IdentifyPaymentDuesPanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class IdentifyPaymentLockSplitTab extends AbstractControlPanel implements MLockSplit, FinServicesHelper, ClickListener {
	
	/** */
	private static final long serialVersionUID = -8930324481669565593L;

	private TextField txtContractRef;

	private Label lblTermsPaid;
	private Label lblRemainingTerms;
	private Label lblNextDueDate;
	private Label lblFirstName;
	private Label lblLastName;
	private Label lblStaffIncharge;
	private Label lblPhoneNo;
	private Label lblContractStatus;
	private Label lblPenalty;
	private Label lblOverdue;
	private Label lblInstallmentAmount;
	private Label lblDifferenceOfInstallment;
	private Label lblOutstandingBalance;
	private VerticalLayout detailLayout;
	
	private Button btnView;
	private IdentifyPaymentLockSplitPanel lckSplitPanel;
	private IdentifyPaymentDuesPanel duesPanel;
	
	private PaymentFileItem paymentFileItem;
	
	private IdentifyPaymentAllocationPanel allocationPanel;
	
	/**
	 * @return the txtContractRef
	 */
	public TextField getTxtContractRef() {
		return txtContractRef;
	}
	
	/**
	 * 
	 * @param allocationPanel
	 */
	public IdentifyPaymentLockSplitTab(IdentifyPaymentAllocationPanel allocationPanel) {
		setMargin(true);
		this.allocationPanel = allocationPanel;
		txtContractRef = ComponentFactory.getTextField();
		
		lblTermsPaid = getLabelValue();
		lblRemainingTerms = getLabelValue();
		lblNextDueDate = getLabelValue();
		lblFirstName = getLabelValue();
		lblLastName = getLabelValue();
		lblStaffIncharge = getLabelValue();
		lblPhoneNo = getLabelValue();
		lblContractStatus = getLabelValue();
		lblPenalty = getLabelValue();
		lblOverdue = getLabelValue();
		lblOutstandingBalance = getLabelValue();
		lblInstallmentAmount = getLabelValue();
		lblDifferenceOfInstallment = getLabelValue();
		detailLayout = new VerticalLayout();
		
		btnView = ComponentLayoutFactory.getDefaultButton("view", FontAwesome.EYE, 80);
		btnView.addClickListener(this);
		lckSplitPanel = new IdentifyPaymentLockSplitPanel();
		duesPanel = new IdentifyPaymentDuesPanel();
		duesPanel.setHeight(300, Unit.PIXELS);
		
		HorizontalLayout searchLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		searchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("contract.id"));
		searchLayout.addComponent(txtContractRef);
		searchLayout.addComponent(btnView);
	
		TabSheet mainTab = new TabSheet();
		mainTab.addTab(allocationPanel, I18N.message("allocation"));
		mainTab.addTab(lckSplitPanel, I18N.message("lock.splits"));
		mainTab.addTab(duesPanel, I18N.message("dues"));
		
		setSpacing(true);
		addComponent(searchLayout);
		addComponent(detailLayout);
		addComponent(mainTab);
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private Component getDetailPanel() {
		Label lblTermsPaidTitle = getLabel("terms.paid");
		Label lblRemainingTermsTitle = getLabel("remaining.terms");
		Label lblNextDueDateTitle = getLabel("next.due.date");
		Label lblFirstNameTitle = getLabel("firstname");
		Label lblLastNameTitle = getLabel("lastname");
		Label lblStaffInchargeTitle = getLabel("staff.in.charge");
		Label lblPhoneNoTitle = getLabel("phone.no");
		Label lblContractStatusTitle = getLabel("contract.status");
		Label lblPenaltyTitle = getLabel("penalty");
		Label lblOverdueTitle = getLabel("overdue");
		Label lblInstallmentAmountTitle = getLabel("installment.amount");
		Label lblDifferenceOfInstallmentTitle = getLabel("difference.of.installment");
		Label lblOutstandingBalanceTitle = getLabel("outstanding.balance");
		
		GridLayout gridLayout = new GridLayout(14, 3);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		int iCol = 0;
		gridLayout.addComponent(lblTermsPaidTitle, iCol++, 0);
		gridLayout.addComponent(lblTermsPaid, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblRemainingTermsTitle, iCol++, 0);
		gridLayout.addComponent(lblRemainingTerms, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblNextDueDateTitle, iCol++, 0);
		gridLayout.addComponent(lblNextDueDate, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblFirstNameTitle, iCol++, 0);
		gridLayout.addComponent(lblFirstName, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblLastNameTitle, iCol++, 0);
		gridLayout.addComponent(lblLastName, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblStaffInchargeTitle, iCol++, 1);
		gridLayout.addComponent(lblStaffIncharge, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblPhoneNoTitle, iCol++, 1);
		gridLayout.addComponent(lblPhoneNo, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblContractStatusTitle, iCol++, 1);
		gridLayout.addComponent(lblContractStatus, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblPenaltyTitle, iCol++, 1);
		gridLayout.addComponent(lblPenalty, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblOverdueTitle, iCol++, 1);
		gridLayout.addComponent(lblOverdue, iCol++, 1);
		
		iCol = 0;
		gridLayout.addComponent(lblInstallmentAmountTitle, iCol++, 2);
		gridLayout.addComponent(lblInstallmentAmount, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(lblDifferenceOfInstallmentTitle, iCol++, 2);
		gridLayout.addComponent(lblDifferenceOfInstallment, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(lblOutstandingBalanceTitle, iCol++, 2);
		gridLayout.addComponent(lblOutstandingBalance, iCol++, 2);
		
		gridLayout.setComponentAlignment(lblTermsPaidTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblRemainingTermsTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblNextDueDateTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblFirstNameTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblLastNameTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblStaffInchargeTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblPhoneNoTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblContractStatusTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblPenaltyTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblOverdueTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblInstallmentAmountTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblDifferenceOfInstallmentTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblOutstandingBalanceTitle, Alignment.TOP_RIGHT);
	
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(gridLayout);
	
		Panel panel = new Panel(verticalLayout);
		return panel;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(PaymentFileItem paymentFileItem) {
		this.paymentFileItem = paymentFileItem;
		Contract contract = null;
		if (StringUtils.isNotEmpty(txtContractRef.getValue())) {
			contract = CONT_SRV.getByReference(StringUtils.trim(txtContractRef.getValue()));
		}
		if (contract != null) {
			lckSplitPanel.assignValue(contract);
			duesPanel.assignValues(contract);
		}
		detailLayout.removeAllComponents();
		detailLayout.addComponent(getDetailPanel());
		assignToContractDetail(paymentFileItem);
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnView)) {
			assignValues(paymentFileItem);
			assignToContractDetail(paymentFileItem);
			Contract contract = CONT_SRV.getByReference(StringUtils.trim(txtContractRef.getValue()));
			if (contract != null) {
				allocationPanel.assignValues(contract);
			}
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	private void assignToContractDetail(PaymentFileItem paymentFileItem) {
		this.reset();
		Contract contract = null;
		if (StringUtils.isNotEmpty(txtContractRef.getValue())) {
			contract = CONT_SRV.getByReference(StringUtils.trim(txtContractRef.getValue()));
		}
		if (contract != null) {
			Collection collection = contract.getCollection();
			if (collection != null) {
				int nbbInstallmentsPaid = MyNumberUtils.getInteger(collection.getNbInstallmentsPaid());
				lblTermsPaid.setValue(getDescription(getDefaultString(nbbInstallmentsPaid)));
				lblRemainingTerms.setValue(getDescription(getDefaultString((contract.getTerm() - nbbInstallmentsPaid))));
				lblOverdue.setValue(getDescription(getDefaultString(AmountUtils.format(collection.getTiTotalAmountInOverdue()))));
				double outstandingBalance = collection.getTiBalanceCapital() + collection.getTiBalanceInterest();
				lblOutstandingBalance.setValue(getDescription(getDefaultString(AmountUtils.format(outstandingBalance))));
				lblNextDueDate.setValue(getDescription(getDateFormat(collection.getNextDueDate())));
			}
			Applicant lessee = contract.getApplicant();
			if (lessee != null) {
				lblFirstName.setValue(getDescription(lessee.getFirstNameLocale()));
				lblLastName.setValue(getDescription(lessee.getLastNameLocale()));
				lblPhoneNo.setValue(getDescription(lessee.getIndividual().getIndividualPrimaryContactInfo()));
			}
			
			lblContractStatus.setValue(getDescription(contract.getWkfStatus().getDescLocale()));
			lblInstallmentAmount.setValue(getDescription(getDefaultString(AmountUtils.format(contract.getTiInstallmentAmount()))));
			lblPenalty.setValue(getDescription());
			if (paymentFileItem != null) {
				double differenceOfInstallment = 0d;
				if (contract.getReference().equals(StringUtils.trim(paymentFileItem.getCustomerRef1()))) {
					differenceOfInstallment = MyNumberUtils.getDouble(paymentFileItem.getAmount()) - 
							MyNumberUtils.getDouble(contract.getTiInstallmentAmount());
				}
				lblDifferenceOfInstallment.setValue(getDescription(getDefaultString(AmountUtils.format(differenceOfInstallment))));
			}
			
			SecUser collectionUser = COL_SRV.getCollectionUser(contract.getId());
			if (collectionUser != null) {
				lblStaffIncharge.setValue(getDescription(collectionUser.getDesc()));
			}
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		lblTermsPaid.setValue(StringUtils.EMPTY);
		lblRemainingTerms.setValue(StringUtils.EMPTY);
		lblNextDueDate.setValue(StringUtils.EMPTY);
		lblFirstName.setValue(StringUtils.EMPTY);
		lblLastName.setValue(StringUtils.EMPTY);
		lblStaffIncharge.setValue(StringUtils.EMPTY);
		lblPhoneNo.setValue(StringUtils.EMPTY);
		lblContractStatus.setValue(StringUtils.EMPTY);
		lblPenalty.setValue(StringUtils.EMPTY);
		lblInstallmentAmount.setValue(StringUtils.EMPTY);
		lblOutstandingBalance.setValue(StringUtils.EMPTY);
		lblOverdue.setValue(StringUtils.EMPTY);
		lblDifferenceOfInstallment.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value == null ? StringUtils.EMPTY : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
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
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : StringUtils.EMPTY;
	}
	
}

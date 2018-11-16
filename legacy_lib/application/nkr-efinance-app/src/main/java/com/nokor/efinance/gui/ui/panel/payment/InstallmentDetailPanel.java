package com.nokor.efinance.gui.ui.panel.payment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.panel.earlysettlement.EarlySettlementsPanel;
import com.nokor.efinance.core.payment.panel.earlysettlement.PaymentInfo2Panel;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.widget.ManualPaymentMethodComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author mao.heng
 */
public class InstallmentDetailPanel extends AbstractTabPanel implements FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	private CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	private PaymentService paymentService= SpringUtils.getBean(PaymentService.class);
	private final ContractService contractService = SpringUtils.getBean(ContractService.class);
	
	private AutoDateField dfPaymentDate;
	private AutoDateField dfInstallmentDate;
	private ManualPaymentMethodComboBox cbxPaymentMethod;
	private TextField txtPaymentAmount;
	private TextField txtExternalCode;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private CheckBox cbShiftInsurance;
	private TextField txtApplicant;
	private TextField txtPenalty;
	private TextField txtOverdueDays;
	private TextField txtInstallmentAmount;
	private TextField txtInsuranceAmount;
	private TextField txtOtherAmount;
	private NativeButton btnReceive;
	private NativeButton btnMultiInstallment;
	private NativeButton btnPaidOff;
	private List<Cashflow> cashflows;
	private ClickListener multiInstallmentListener;
	private ClickListener allocateListener;
	private ClickListener paidOffListener;
	private Contract contract;
	private Long cotraId;
	private Payment payment;
	private VerticalLayout installmentLayout;
	private TabSheet tabSheetMultiInstallment;
	private boolean isMultiInstallment = false;
	private List<Cashflow> cashflowList;
	private PaymentInfo2Panel paymentInfo2Panel;
	private Window window;
	private double insuranceAmount;
	private Date installmentDate;
	private boolean alreadyProcessed;
	
	private SecUserDetail secUserDetail;
	
	public InstallmentDetailPanel() {
		super();
		setMargin(false);
		setImmediate(true);
		btnReceive = new NativeButton(I18N.message("paid"));
		btnReceive.setDisableOnClick(true);
		btnReceive.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		btnReceive.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -94612598626779199L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (isValid() && isValidInstallmentOrder()) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.receive.installment.payment", txtApplicant.getValue()),
    				        new ConfirmDialog.Listener() {
    							private static final long serialVersionUID = 2380193173874927880L;
    							public void onClose(ConfirmDialog dialog) {
    				                if (dialog.isConfirmed() && !alreadyProcessed) {
    				                	alreadyProcessed = true;
    				                	dialog.setImmediate(true);
    				                	dialog.close();
    				                	setEnabled(false);
    				        			if (getDouble(txtPenalty) > 0d) {
    				    					Cashflow cashflowFromLst = cashflows.get(0);
    				    					Cashflow penaltyCashflow = CashflowUtils.createCashflow(cashflowFromLst.getProductLine(), null,
    				    							  cashflowFromLst.getContract(), cashflowFromLst.getVatValue(), ECashflowType.PEN, ETreasuryType.APP, cashflowFromLst.getJournalEvent(),
    				    							  cbxPaymentMethod.getSelectedEntity(), getDouble(txtPenalty), 0d, 
    				    							  getDouble(txtPenalty), dfPaymentDate.getValue(), cashflowFromLst.getPeriodStartDate(), cashflowFromLst.getPeriodEndDate(), cashflowFromLst.getNumInstallment());
    				    					cashflows.add(penaltyCashflow);
    				    				} else {
    				    					txtOverdueDays.setValue("");
    				    				}
    				        			if (cashflows != null && !cashflows.isEmpty()) {
											
    				        				List<Cashflow> insCashflows = new ArrayList<>(); 
    				        				List<Cashflow> cashflowsListNew = cashflows;
				        					for (Cashflow cashflow : cashflows) {
				        						if (cashflow.getCashflowType().equals(ECashflowType.FEE) 
				        								&& "INSFEE".equals(cashflow.getService().getCode())) {
													if (!cbShiftInsurance.getValue()) {
														cashflowsListNew.remove(cashflow);
														break;
				        							} else {
				        								insCashflows.add(cashflow);
				        							}
				        							
				        						}
				        					}
											if (insCashflows != null && !insCashflows.isEmpty()) {
												for (Cashflow cashflow : insCashflows) {
					        						double insuranceAmount = getDouble(txtInsuranceAmount) / insCashflows.size();
						        					cashflow.setTiInstallmentAmount(insuranceAmount);
				        							cashflow.setTeInstallmentAmount(cashflow.getTiInstallmentAmount());
				        							cashflow.setVatInstallmentAmount(0d);
					        					}
											}
    				        				SecUser receivedUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    				        				Contract contract = cashflowService.getById(Contract.class, new Long(cotraId));
    				        				Payment payment = new Payment();
    				        				payment.setApplicant(contract.getApplicant());
    				        				payment.setContract(contract);
    				        				payment.setExternalReference(txtExternalCode.getValue());
    				        				payment.setPaymentDate(dfPaymentDate.getValue());
    				        				payment.setPaymentMethod(cbxPaymentMethod.getSelectedEntity());
    				        				payment.setTiPaidAmount(getDouble(txtPaymentAmount));
    				        				payment.setWkfStatus(PaymentWkfStatus.PAI);
    				        				payment.setConfirm(payment.getPaymentMethod().isAutoConfirm());
    				        				payment.setReceivedUser(receivedUser);
    				        				payment.setPaymentType(EPaymentType.IRC);
    				        				payment.setNumPenaltyDays(getInteger(txtOverdueDays));
    				        				payment.setCashflows(cashflowsListNew);
    				        				payment.setDealer(cbxDealer.getSelectedEntity());
    				        				paymentService.createPayment(payment);
    				        				setPayment(payment);
    				        				
											if (isMultiInstallment) {
												installmentLayout = new VerticalLayout();
												
												paymentInfo2Panel = new PaymentInfo2Panel();
												paymentInfo2Panel.assignValues(getPayment());
												installmentLayout.addComponent(paymentInfo2Panel);			
												
												tabSheetMultiInstallment.addTab(installmentLayout, I18N.message("installment.payment.info"));
												tabSheetMultiInstallment.setSelectedTab(installmentLayout);
												window.close();
											} else {
												allocateListener.buttonClick(null);	
											}    				        				
    				        			}
    				                } else {
				        				btnReceive.setEnabled(true);
				        			}
    							}
    				    });
    				confirmDialog.setWidth("450px");
    				confirmDialog.setHeight("200px");
    				confirmDialog.setImmediate(true);
    				confirmDialog.getOkButton().setDisableOnClick(true);
				}
			}
		});
		btnMultiInstallment = new NativeButton(I18N.message("multi.installment"));
		btnMultiInstallment.setIcon(new ThemeResource("../nkr-default/icons/16/table.png"));
		btnMultiInstallment.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -94612598626779199L;
			@Override
			public void buttonClick(ClickEvent event) {
				multiInstallmentListener.buttonClick(null);
				Page.getCurrent().setUriFragment("!" + MultiInstallmentsPanel.NAME + "/" + contract.getReference());
			}
		});
		
		btnPaidOff = new NativeButton(I18N.message("paid.off"));
		btnPaidOff.setIcon(new ThemeResource("../nkr-default/icons/16/table.png"));
		btnPaidOff.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -94612598626779199L;
			@Override
			public void buttonClick(ClickEvent event) {
				paidOffListener.buttonClick(null);
				Page.getCurrent().setUriFragment("!" + EarlySettlementsPanel.NAME + "/" + contract.getId());
			}
		});
		
		ToolbarButtonsPanel tblButtonsPanel = new ToolbarButtonsPanel();
		tblButtonsPanel.addButton(btnReceive);
		tblButtonsPanel.addButton(btnMultiInstallment);
		tblButtonsPanel.addButton(btnPaidOff);
		addComponent(tblButtonsPanel, 0);
	}
	
	/**
	 * @param cotraId
	 */
	public void assignValues(Long cotraId, Date installmentDate) {
		insuranceAmount = 0d;
		this.installmentDate = installmentDate;
		if (cotraId != null) {
			alreadyProcessed = false;
			contract = ENTITY_SRV.getById(Contract.class, cotraId);
			this.cotraId = cotraId;
			this.cashflows = cashflowService.getCashflowsToPaid(cotraId, installmentDate);
			if (cashflows != null && !cashflows.isEmpty()) {
				dfPaymentDate.setValue(DateUtils.today());
				double installmentAmountUsd =  0d;
				double otherInstallmentAmountUsd =  0d;
				double insuranceAmountUsd = 0d;
				for (Cashflow cashflow : cashflows) {
					if(cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
						installmentAmountUsd += cashflow.getTiInstallmentAmount();
					} else if (cashflow.getCashflowType().equals(ECashflowType.FEE) && "INSFEE".equals(cashflow.getService().getCode())) {
						insuranceAmountUsd += cashflow.getTiInstallmentAmount();
						insuranceAmount += cashflow.getTiInstallmentAmount();
					} else {
						otherInstallmentAmountUsd += cashflow.getTiInstallmentAmount();
					}
				}
				PenaltyVO penaltyVO = contractService.calculatePenalty(contract, installmentDate, DateUtils.todayH00M00S00(), installmentAmountUsd);
				double penaltyAmountUsd = penaltyVO.getPenaltyAmount() != null ? penaltyVO.getPenaltyAmount().getTiAmount() : 0d;
				dfInstallmentDate.setValue(installmentDate);
				
				secUserDetail = getSecUserDetail(); 
				if (ProfileUtil.isPOS() && secUserDetail != null && secUserDetail.getDealer() != null) {
					cbxDealer.setSelectedEntity(secUserDetail.getDealer());
					cbxDealerType.setSelectedEntity(secUserDetail.getDealer().getDealerType() != null ? 
							secUserDetail.getDealer().getDealerType() : null);
					cbxDealer.setEnabled(false);
					cbxDealerType.setEnabled(false);
				} else {
					cbxDealer.setSelectedEntity(contract.getDealer());
					cbxDealerType.setSelectedEntity(contract.getDealer().getDealerType() != null ? contract.getDealer().getDealerType() : null);
					cbxDealer.setEnabled(true);
					cbxDealerType.setEnabled(true);
				}
				
				txtApplicant.setValue(contract.getApplicant().getIndividual().getLastNameEn() + "  " + contract.getApplicant().getIndividual().getFirstNameEn());
				txtPenalty.setValue(AmountUtils.format(penaltyAmountUsd));
				txtOverdueDays.setValue(getDefaultString(penaltyVO.getNumPenaltyDays()));
				txtInstallmentAmount.setValue(AmountUtils.format(installmentAmountUsd));
				txtInsuranceAmount.setValue(AmountUtils.format(insuranceAmountUsd));
				txtOtherAmount.setValue(AmountUtils.format(otherInstallmentAmountUsd));
				txtPaymentAmount.setValue(AmountUtils.format(installmentAmountUsd + insuranceAmountUsd + otherInstallmentAmountUsd + penaltyAmountUsd));
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ENTITY_SRV.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}
	
	/**
	 * @param selectedItemIds, installmentDate, tabSheet
	 */
	public void assignValuesMultiInstallment(Set<Long> selectedItemIds, Date installmentDate, TabSheet tabSheet) {
		if (installmentLayout != null) {
			tabSheetMultiInstallment.removeComponent(installmentLayout);
		}
		alreadyProcessed = false;
		this.installmentDate = installmentDate;
		this.tabSheetMultiInstallment = tabSheet;
		this.isMultiInstallment = true;
		btnMultiInstallment.setVisible(false);
		btnPaidOff.setVisible(false);
		cbShiftInsurance.setVisible(false);
		double multiInstallmentAmountUsd = 0d;
		double multiOtherInstallmentAmountUsd = 0d;
		double multiInsuranceAmountUsd = 0d;
		
		int numberPenaltyDays = 0;
		double penaltyAmountUsd = 0d;
		
		dfPaymentDate.setValue(DateUtils.today());
		int index = 0;
		cashflowList = new ArrayList<Cashflow>();
		for (Long cashflowId : selectedItemIds) {
			Cashflow cashflow = cashflowService.getById(Cashflow.class, cashflowId);
			if (index == 0) {
				this.contract = cashflow.getContract();
				this.cotraId = contract.getId();
			}
			Date installmentDateItem = cashflow.getInstallmentDate();
			List<Cashflow> cashflowItemList = cashflowService.getCashflowsToPaid(cotraId, cashflow.getInstallmentDate());
			double tiInstallmentAmountUsd = 0d;
			if (cashflowItemList != null && !cashflowItemList.isEmpty()) {
				for (Cashflow cashflowItem : cashflowItemList) {

					if (cashflowItem.getCashflowType().equals(ECashflowType.CAP)
							|| cashflowItem.getCashflowType().equals(ECashflowType.IAP)) {
						tiInstallmentAmountUsd += cashflowItem.getTiInstallmentAmount();
					}
					
					cashflowList.add(cashflowItem);
					if(cashflowItem.getCashflowType().equals(ECashflowType.CAP) || cashflowItem.getCashflowType().equals(ECashflowType.IAP)) {
						multiInstallmentAmountUsd += cashflowItem.getTiInstallmentAmount();
					} else if (cashflow.getCashflowType().equals(ECashflowType.FEE) && "INSFEE".equals(cashflow.getService().getCode())) {
						multiInsuranceAmountUsd += cashflowItem.getTiInstallmentAmount();
					}  else {
						multiOtherInstallmentAmountUsd += cashflowItem.getTiInstallmentAmount();
					}
				}
			}
			index++;
			PenaltyVO penaltyVO = contractService.calculatePenalty(contract, installmentDateItem, DateUtils.todayH00M00S00(), tiInstallmentAmountUsd);
			if (penaltyVO != null) {
				numberPenaltyDays += penaltyVO.getNumPenaltyDays() != null ? penaltyVO.getNumPenaltyDays() : 0;
				penaltyAmountUsd += penaltyVO.getPenaltyAmount() != null ? penaltyVO.getPenaltyAmount().getTiAmount() : 0d;	
			}
		}
		this.cashflows = cashflowList;	
		dfInstallmentDate.setValue(installmentDate);
		
		secUserDetail = getSecUserDetail(); 
		if (ProfileUtil.isPOS() && secUserDetail != null && secUserDetail.getDealer() != null) {
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());
			cbxDealerType.setSelectedEntity(secUserDetail.getDealer().getDealerType() != null ?
					secUserDetail.getDealer().getDealerType() : null);
			cbxDealer.setEnabled(false);
			cbxDealerType.setEnabled(false);
		} else {
			cbxDealer.setSelectedEntity(contract.getDealer());
			cbxDealerType.setSelectedEntity(contract.getDealer().getDealerType() != null ? contract.getDealer().getDealerType() : null);
			cbxDealer.setEnabled(true);
			cbxDealerType.setEnabled(true);
		}
		
		txtApplicant.setValue(contract.getApplicant().getIndividual().getLastNameEn() + "  " + contract.getApplicant().getIndividual().getFirstNameEn());
		txtPenalty.setValue(AmountUtils.format(penaltyAmountUsd));
		txtOverdueDays.setValue(getDefaultString(numberPenaltyDays));
		txtInstallmentAmount.setValue(AmountUtils.format(multiInstallmentAmountUsd));
		txtInsuranceAmount.setValue(AmountUtils.format(multiInsuranceAmountUsd));
		txtOtherAmount.setValue(AmountUtils.format(multiInsuranceAmountUsd));
		txtPaymentAmount.setValue(AmountUtils.format(multiInstallmentAmountUsd + multiInsuranceAmountUsd + multiOtherInstallmentAmountUsd + penaltyAmountUsd));
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		
		final GridLayout gridLayout = new GridLayout(8, 6);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		dfPaymentDate = ComponentFactory.getAutoDateField("", false);
		dfPaymentDate.setValue(new Date());
		dfPaymentDate.setRequired(true);
		dfPaymentDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 326589421387673769L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (contract != null && getDouble(txtInstallmentAmount) != null && getDouble(txtOtherAmount) != null) {
					PenaltyVO penaltyVO = contractService.calculatePenalty(contract, dfInstallmentDate.getValue(), dfPaymentDate.getValue(), getDouble(txtInstallmentAmount));
					double penaltyAmountUsd = penaltyVO.getPenaltyAmount() != null ? penaltyVO.getPenaltyAmount().getTiAmount() : 0d;
					txtPenalty.setValue(AmountUtils.format(penaltyAmountUsd));
					txtOverdueDays.setValue(getDefaultString(penaltyVO.getNumPenaltyDays()));
					txtPaymentAmount.setValue(AmountUtils.format(getDouble(txtInstallmentAmount) + getDouble(txtOtherAmount) + penaltyAmountUsd));
				}
			}
		});
		
		dfInstallmentDate = ComponentFactory.getAutoDateField("", false);
		dfInstallmentDate.setEnabled(false);
		
		cbxPaymentMethod = new ManualPaymentMethodComboBox();
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setRequired(true);
		cbxPaymentMethod.setWidth("150px");
        
		txtPaymentAmount = ComponentFactory.getTextField(30, 150);
		txtPaymentAmount.setEnabled(false);
		
		cbxDealer = new DealerComboBox(DataReference.getInstance().getDealers());
		cbxDealer.setWidth("220px");
		cbxDealer.setRequired(true);
		cbxDealer.setImmediate(true);
		
		
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.values());
		cbxDealerType.setImmediate(true);
		
		txtApplicant = ComponentFactory.getTextField(100, 150);
		txtApplicant.setEnabled(false);
		
		txtPenalty = ComponentFactory.getTextField(30, 150);
		txtPenalty.setEnabled(false);
		
		txtOverdueDays = ComponentFactory.getTextField(30, 150);
		txtOverdueDays.setEnabled(false);
		
		txtExternalCode = ComponentFactory.getTextField(30, 150);
		
		txtInstallmentAmount = ComponentFactory.getTextField(30, 150);
		txtInstallmentAmount.setEnabled(false);
		
		txtOtherAmount = ComponentFactory.getTextField(30, 150);
		txtOtherAmount.setEnabled(false);
		
		cbShiftInsurance = new CheckBox();
		cbShiftInsurance.setValue(true);
		cbShiftInsurance.setImmediate(true);
		cbShiftInsurance.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6473641522176598721L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				double insurance = getDouble(txtInsuranceAmount);
				if ((boolean) event.getProperty().getValue()) {
					txtInsuranceAmount.setValue(AmountUtils.format(insurance + insuranceAmount));
				} else {
					txtInsuranceAmount.setValue(AmountUtils.format(insurance - insuranceAmount));
				}
				
			}
		});
		
		txtInsuranceAmount = ComponentFactory.getTextField(30, 150);
		txtInsuranceAmount.setImmediate(true);
		txtInsuranceAmount.setEnabled(false);
		txtInsuranceAmount.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 3619346944998750262L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (getDouble(txtInstallmentAmount) != null) {
					txtPaymentAmount.setValue(AmountUtils.format(getDouble(txtInstallmentAmount) 
							+ (getDouble(txtInsuranceAmount) != null ? getDouble(txtInsuranceAmount) : 0d)
							+ (getDouble(txtOtherAmount) != null ? getDouble(txtOtherAmount) : 0d)
							+ (getDouble(txtPenalty) != null ?  getDouble(txtPenalty) : 0d)));
				}
			}
		});
		
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("applicant")), iCol++, 1);
        gridLayout.addComponent(txtApplicant, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("installment.amount")), iCol++, 1);
        gridLayout.addComponent(txtInstallmentAmount, iCol++, 1);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("due.date")), iCol++, 2);
        gridLayout.addComponent(dfInstallmentDate, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
        gridLayout.addComponent(new Label(I18N.message("penalty.amount")), iCol++, 2);
        gridLayout.addComponent(txtPenalty, iCol++, 2);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("no.penalty.days")), iCol++, 3);
        gridLayout.addComponent(txtOverdueDays, iCol++, 3);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 3);
        gridLayout.addComponent(new Label(I18N.message("other.amount")), iCol++, 3);
        gridLayout.addComponent(txtOtherAmount, iCol++, 3);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("insurance.amount")), iCol++, 4);
        gridLayout.addComponent(txtInsuranceAmount, iCol++, 4);
        gridLayout.addComponent(cbShiftInsurance, iCol++, 4);
        gridLayout.addComponent(new Label(I18N.message("total.amount")), iCol++, 4);
        gridLayout.addComponent(txtPaymentAmount, iCol++, 4);
        //gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 4);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("payment.date")), iCol++, 5);
        gridLayout.addComponent(dfPaymentDate, iCol++, 5);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 5);
        gridLayout.addComponent(new Label(I18N.message("payment.method")), iCol++, 5);
        gridLayout.addComponent(cbxPaymentMethod, iCol++, 5);
		
        contentLayout.setSpacing(true);
        contentLayout.addComponent(gridLayout);

        return contentLayout;
	}
	
	/**
	 * 
	 * @return date
	 */
	private Date getDateAt_01_02_2014() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(2014, 01, 00);
		return cal.getTime();
	}
	/**
	 * @param allocateListener the allocateListener to set
	 */
	public void setAllocateListener(ClickListener allocateListener) {
		this.allocateListener = allocateListener;
	}
	/**
	 * @param window the window to set
	 */
	public void setWindow(Window window) {
		this.window = window;
	}
	
	/**
	 * @param paidOffListener the paidOffListener to set
	 */
	public void setPaidOffListener(ClickListener paidOffListener) {
		this.paidOffListener = paidOffListener;
	}
	
	/**
	 * @param multiInstallmentListener the multiInstallmentListener to set
	 */
	public void setMultiInstallmentListener(ClickListener multiInstallmentListener) {
		this.multiInstallmentListener = multiInstallmentListener;
	}
	
	/**
	 * @return the payment
	 */
	public Payment getPayment() {
		return payment;
	}

	/**
	 * @param payment the payment to set
	 */
	private void setPayment(Payment payment) {
		this.payment = payment;
	}
	public void removeTabPaymentInfo() {
		tabSheetMultiInstallment.removeComponent(installmentLayout);
	} 
	/**
	 * @return
	 */
	public boolean isValid() {
		reset();
		checkMandatoryDateField(dfPaymentDate, "payment.date");
		checkMandatorySelectField(cbxPaymentMethod, "payment.method");
		checkMandatorySelectField(cbxDealer, "dealer");
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
			btnReceive.setEnabled(true);
		}
		return errors.isEmpty();
	}
	
	/**
	 * Check valid installment order. 
	 * Set a system which allow only to receive the installment in the correct order
	 * @return
	 */
	public boolean isValidInstallmentOrder() {
		reset();		
		return errors.isEmpty();
	}
}

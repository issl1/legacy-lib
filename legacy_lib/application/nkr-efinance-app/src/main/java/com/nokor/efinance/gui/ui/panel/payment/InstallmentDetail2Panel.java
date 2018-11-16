package com.nokor.efinance.gui.ui.panel.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
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
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.panel.earlysettlement.EarlySettlementsPanel;
import com.nokor.efinance.core.payment.panel.earlysettlement.PaymentInfo2Panel;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author mao.heng
 */
public class InstallmentDetail2Panel extends AbstractTabPanel implements FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	private CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	private PaymentService paymentService= SpringUtils.getBean(PaymentService.class);
	private final ContractService contractService = SpringUtils.getBean(ContractService.class);
	
	private AutoDateField dfPaymentDate;
	private AutoDateField dfInstallmentDate;
//	private ManualPaymentMethodComboBox cbxPaymentMethod;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private TextField txtPaymentAmount;
	private TextField txtExternalCode;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private TextField txtApplicant;
	private TextField txtPenalty;
	private TextField txtPenaltyDays;
	private TextField txtInstallmentAmount;
	private TextField txtOtherAmount;
	private TextField txtServicingFee;
	private TextField txtInsuranceFee;
	private Button btnReceive;
	private Button btnMultiInstallment;
	private Button btnPaidOff;
	private List<Cashflow> cashflows;
	private Long cotraId;
	private Contract contract;
	private ClickListener allocateListener;
	private ClickListener multiInstallmentListener;
	private ClickListener paidOffListener;
	private List<Cashflow> cashflowList;
	private TabSheet tabSheetMultiInstallment;
	private boolean isMultiInstallment = false;
	private VerticalLayout installmentLayout;
	private Payment payment;
	private Window window;
	private Date installmentDateItem;
	private PaymentInfoPanel paymentInfoPanel;
	private PaymentInfo2Panel paymentInfo2Panel;
	private int numberPenaltyDays;
	private double penaltyAmountUsd;	
	private boolean alreadyProcessed;
	
	private SecUserDetail secUserDetail;
	
	public InstallmentDetail2Panel() {
		super();
		setMargin(false);
		setImmediate(true);
	}
	
	/**
	 * @param cotraId
	 */
	public void assignValues(Long cotraId, Date installmentDate) {
		if (cotraId != null) {
			alreadyProcessed = false;
//			btnMultiInstallment.setVisible(true);
			contract = ENTITY_SRV.getById(Contract.class, cotraId);
			this.cotraId = cotraId;
			this.cashflows = cashflowService.getCashflowsToPaid(cotraId, installmentDate);
			if (cashflows != null && !cashflows.isEmpty()) {
				dfPaymentDate.setValue(DateUtils.today());
				double installmentAmountUsd =  0d;
				double otherInstallmentAmountUsd =  0d;
				double tiServicingFeeUsd = 0d;
				double tiInsuranceFeeUsd = 0d;
				for (Cashflow cashflow : cashflows) {
					if(cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
						installmentAmountUsd += cashflow.getTiInstallmentAmount();
					} else if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
						if (EServiceType.INSFEE.equals(cashflow.getService().getServiceType())) {
							tiInsuranceFeeUsd = cashflow.getTiInstallmentAmount();
						} else if (EServiceType.SRVFEE.equals(cashflow.getService().getServiceType())) {
							tiServicingFeeUsd = cashflow.getTiInstallmentAmount();
						} else {
							otherInstallmentAmountUsd += cashflow.getTiInstallmentAmount();
						}
					} else {
						otherInstallmentAmountUsd += cashflow.getTiInstallmentAmount();
					}
				}
				PenaltyVO penaltyVO = contractService.calculatePenalty(
						contract, installmentDate, DateUtils.todayH00M00S00(), installmentAmountUsd);
				double penaltyAmountUsd = penaltyVO.getPenaltyAmount() != null ? penaltyVO.getPenaltyAmount().getTiAmount() : 0d;
				dfInstallmentDate.setValue(installmentDate);
				
//				secUserDetail = getSecUserDetail(); 
				/*if (ProfileUtil.isPOS() && secUserDetail != null && secUserDetail.getDealer() != null) {
					cbxDealer.setSelectedEntity(secUserDetail.getDealer());
					cbxDealerType.setSelectedEntity(secUserDetail.getDealer().getDealerType() != null ? 
							secUserDetail.getDealer().getDealerType() : null);
					cbxDealer.setEnabled(false);
					cbxDealerType.setEnabled(false);
				} else {
					cbxDealer.setSelectedEntity(contract.getDealer());
					cbxDealerType.setSelectedEntity(contract.getDealer().getDealerType() != null ? 
						contract.getDealer().getDealerType() : null);
					cbxDealer.setEnabled(true);
					cbxDealerType.setEnabled(true);
				}*/
				cbxDealer.setSelectedEntity(contract.getDealer());
				cbxDealer.setEnabled(false);
				/*cbxDealerType.setEnabled(true);
				secUserDetail = getSecUserDetail(); 
				if (secUserDetail != null && secUserDetail.getDealer() != null) {
//					cbxDealerType.setSelectedEntity(secUserDetail.getDealer().getDealerType());
					cbxDealer.setSelectedEntity(secUserDetail.getDealer());
				}*/
				
				txtApplicant.setValue(contract.getApplicant().getIndividual().getLastNameEn() + "  " + contract.getApplicant().getIndividual().getFirstNameEn());
				txtPenalty.setValue(AmountUtils.format(penaltyAmountUsd));
				txtServicingFee.setValue(AmountUtils.format(tiServicingFeeUsd));
				txtInsuranceFee.setValue(AmountUtils.format(tiInsuranceFeeUsd));
				txtPenaltyDays.setValue(getDefaultString(penaltyVO.getNumPenaltyDays()));
				txtInstallmentAmount.setValue(AmountUtils.format(installmentAmountUsd));
				txtOtherAmount.setValue(AmountUtils.format(otherInstallmentAmountUsd));
				txtPaymentAmount.setValue(AmountUtils.format(installmentAmountUsd + otherInstallmentAmountUsd + penaltyAmountUsd + tiInsuranceFeeUsd + tiServicingFeeUsd));
			}
		}
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		// restrictions.addCriterion(Restrictions.ne("dealerType", DealerType.OTH));
		return restrictions;
	}
	
	/**
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
		this.tabSheetMultiInstallment = tabSheet;
		this.isMultiInstallment = true;
		btnMultiInstallment.setVisible(false);
		btnPaidOff.setVisible(false);
		double multiInstallmentAmountUsd =  0d;
		double multiOtherInstallmentAmountUsd =  0d;
		double tiMultiInsuranceFeeUsd = 0d;
		double tiMultiServicingFeeUsd = 0d;
		dfPaymentDate.setValue(DateUtils.today());
		int index = 0;
		cashflowList = new ArrayList<Cashflow>();
		for (Long cashflowId : selectedItemIds) {
			Cashflow cashflow = cashflowService.getById(Cashflow.class, cashflowId);
			if (index == 0) {
				this.contract = cashflow.getContract();
				this.cotraId = contract.getId();
			}
			installmentDateItem = cashflow.getInstallmentDate();
			List<Cashflow> cashflowItemList = cashflowService.getCashflowsToPaid(cotraId, cashflow.getInstallmentDate());
			if (cashflowItemList != null && !cashflowItemList.isEmpty()) {
				for (Cashflow cashflowItem : cashflowItemList) {

					if (cashflowItem.getCashflowType().equals(ECashflowType.CAP)
							|| cashflowItem.getCashflowType().equals(ECashflowType.IAP)) {
						multiInstallmentAmountUsd += cashflowItem.getTiInstallmentAmount();
					} else if (cashflowItem.getCashflowType().equals(ECashflowType.FEE)) {

						if ("INSFEE".equals(cashflowItem.getService().getCode())) {
							tiMultiInsuranceFeeUsd += cashflowItem.getTiInstallmentAmount();
						} else if ("SERFEE".equals(cashflowItem.getService().getCode())) {
							tiMultiServicingFeeUsd += cashflowItem.getTiInstallmentAmount();
						} else {
							multiOtherInstallmentAmountUsd += cashflowItem.getTiInstallmentAmount();	
						}
					} else {
						multiOtherInstallmentAmountUsd += cashflowItem.getTiInstallmentAmount();	
					}					
					cashflowList.add(cashflowItem);
				}
			}
			index++;
			PenaltyVO penaltyVO = contractService.calculatePenalty(contract, installmentDateItem, DateUtils.todayH00M00S00(), multiInstallmentAmountUsd);
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
			cbxDealerType.setSelectedEntity(contract.getDealer().getDealerType() != null ? 
					contract.getDealer().getDealerType() : null);
			cbxDealer.setEnabled(true);
			cbxDealerType.setEnabled(true);
		}
		
		txtApplicant.setValue(contract.getApplicant().getIndividual().getLastNameEn() + "  " + contract.getApplicant().getIndividual().getFirstNameEn());
		txtPenalty.setValue(AmountUtils.format(penaltyAmountUsd));
		txtPenaltyDays.setValue(getDefaultString(numberPenaltyDays));
		txtInstallmentAmount.setValue(AmountUtils.format(multiInstallmentAmountUsd));
		txtOtherAmount.setValue(AmountUtils.format(multiOtherInstallmentAmountUsd));
		txtServicingFee.setValue(AmountUtils.format(tiMultiServicingFeeUsd));
		txtInsuranceFee.setValue(AmountUtils.format(tiMultiInsuranceFeeUsd));
		txtPaymentAmount.setValue(AmountUtils.format(multiInstallmentAmountUsd + multiOtherInstallmentAmountUsd + penaltyAmountUsd + tiMultiServicingFeeUsd + tiMultiInsuranceFeeUsd));
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		
		final GridLayout gridLayout = new GridLayout(9, 7);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		dfPaymentDate = ComponentFactory.getAutoDateField("", false);
		dfPaymentDate.setValue(new Date());
		dfPaymentDate.setRequired(true);
		dfPaymentDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 326589421387673769L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (contract != null && getDouble(txtInstallmentAmount) != null 
						&& getDouble(txtOtherAmount) != null
						&& getDouble(txtServicingFee) != null
						&& getDouble(txtInsuranceFee) != null) {
					PenaltyVO penaltyVO = contractService.calculatePenalty(contract, dfInstallmentDate.getValue(), dfPaymentDate.getValue(), getDouble(txtInstallmentAmount));
					double penaltyAmountUsd = penaltyVO.getPenaltyAmount() != null ? penaltyVO.getPenaltyAmount().getTiAmount() : 0d;
					txtPenalty.setValue(AmountUtils.format(penaltyAmountUsd));
					txtPenaltyDays.setValue(getDefaultString(penaltyVO.getNumPenaltyDays()));
					txtPaymentAmount.setValue(AmountUtils.format(getDouble(txtInstallmentAmount) + getDouble(txtServicingFee) 
							+ getDouble(txtInsuranceFee) + getDouble(txtOtherAmount) + penaltyAmountUsd));
				}
			}
		});
		
		dfInstallmentDate = ComponentFactory.getAutoDateField("", false);
		dfInstallmentDate.setEnabled(false);
		
		/*cbxPaymentMethod = new ManualPaymentMethodComboBox();
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setWidth("150px");*/
		cbxPaymentMethod = new EntityRefComboBox<>();
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setWidth(200, Unit.PIXELS);
		cbxPaymentMethod.setRequired(true);
        
		txtPaymentAmount = ComponentFactory.getTextField(30, 150);
		txtPaymentAmount.setEnabled(false);
		
		cbxDealer = new DealerComboBox(null, ENTITY_SRV.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setWidth(200, Unit.PIXELS);
		cbxDealer.setRequired(true);
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.values());
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth(150, Unit.PIXELS);
		
		txtApplicant = ComponentFactory.getTextField(100, 150);
		txtApplicant.setEnabled(false);
		
		txtPenalty = ComponentFactory.getTextField(30, 150);
		txtPenalty.setEnabled(false);
		
		txtPenaltyDays = ComponentFactory.getTextField(30, 150);
		txtPenaltyDays.setEnabled(false);
		txtServicingFee = ComponentFactory.getTextField(30, 150);
		txtServicingFee.setEnabled(false);
		txtInsuranceFee = ComponentFactory.getTextField(30, 150);
		txtInsuranceFee.setEnabled(false);
		
		txtExternalCode = ComponentFactory.getTextField(30, 150);
		
		txtInstallmentAmount = ComponentFactory.getTextField(30, 150);
		txtInstallmentAmount.setEnabled(false);
		
		txtOtherAmount = ComponentFactory.getTextField(30, 150);
		txtOtherAmount.setEnabled(false);
		
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
        gridLayout.addComponent(txtPenaltyDays, iCol++, 3);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 3);
        gridLayout.addComponent(new Label(I18N.message("insurance.fee")), iCol++, 3);
        gridLayout.addComponent(txtInsuranceFee, iCol++, 3);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("servicing.fee")), iCol++, 4);
        gridLayout.addComponent(txtServicingFee, iCol++, 4);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 4);
        gridLayout.addComponent(new Label(I18N.message("total.amount")), iCol++, 4);
        gridLayout.addComponent(txtPaymentAmount, iCol++, 4);
                
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("other.amount")), iCol++, 5);
        gridLayout.addComponent(txtOtherAmount, iCol++, 5);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 5);
        gridLayout.addComponent(new Label(I18N.message("payment.method")), iCol++, 5);
        gridLayout.addComponent(cbxPaymentMethod, iCol++, 5);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("payment.date")), iCol++, 6);
        gridLayout.addComponent(dfPaymentDate, iCol++, 6);
		
        btnReceive = new Button(I18N.message("paid"));
        btnReceive.setDisableOnClick(true);
		btnReceive.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		btnReceive.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -94612598626779199L;
			@Override
			public void buttonClick(ClickEvent event) {
            	if (isValid() && isValidInstallmentOrder()) {
    				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.receive.installment.payment", new String[] {txtApplicant.getValue()}),
    				        new ConfirmDialog.Listener() {
    							private static final long serialVersionUID = 2380193173874927880L;
    							@SuppressWarnings("unused")
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
    				    					boolean isCashflowDuplicate = CashflowUtils.isCashflowDuplicate(cashflows, penaltyCashflow);
    				    					if (!isCashflowDuplicate) {
    				    						cashflows.add(penaltyCashflow);
    				    					}
    				    				} else {
    				    					txtPenaltyDays.setValue("");
    				    				}
    				        			if (cashflows != null && !cashflows.isEmpty()) {
    				        				SecUser receivedUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    				        				Contract contract = cashflowService.getById(Contract.class, new Long(cotraId));
    				        				Payment payment = new Payment();
    				        				payment.setDescEn("Installment" + cashflows.get(0).getNumInstallment());
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
    				        				payment.setNumPenaltyDays(getInteger(txtPenaltyDays));
    				        				payment.setDealer(cbxDealer.getSelectedEntity());
    				        				payment.setCashflows(cashflows);
    				        				paymentService.createPayment(payment);
    				        				setPayment(payment);
											if (isMultiInstallment) {
												installmentLayout = new VerticalLayout();
												// if (cashflows.get(0).getContract().getTemplateType().equals(ETemplateType.TEMPLATE_2)) {
												if (true) {
													paymentInfo2Panel = new PaymentInfo2Panel();
													paymentInfo2Panel.assignValues(getPayment());
													installmentLayout.addComponent(paymentInfo2Panel);
												} else {
													paymentInfoPanel = new PaymentInfoPanel();
													paymentInfoPanel.assignValues(getPayment());
													installmentLayout.addComponent(paymentInfoPanel);
												}
											
												
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
    				confirmDialog.getOkButton().setDisableOnClick(true);
    				confirmDialog.setImmediate(true);
            	}
			}
		});
		
		btnMultiInstallment = new Button(I18N.message("multi.installment"));
		btnMultiInstallment.setIcon(new ThemeResource("../nkr-default/icons/16/table.png"));
		btnMultiInstallment.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -94612598626779199L;
			@Override
			public void buttonClick(ClickEvent event) {
				multiInstallmentListener.buttonClick(null);
				Page.getCurrent().setUriFragment("!" + MultiInstallmentsPanel.NAME + "/" + contract.getReference());
			}
		});
		
		btnPaidOff = new Button(I18N.message("paid.off"));
		btnPaidOff.setIcon(new ThemeResource("../nkr-default/icons/16/table.png"));
		btnPaidOff.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -94612598626779199L;
			@Override
			public void buttonClick(ClickEvent event) {
				paidOffListener.buttonClick(null);
				Page.getCurrent().setUriFragment("!" + EarlySettlementsPanel.NAME + "/" + contract.getId());
			}
		});
		//ToolbarButtonsPanel tblButtonsPanel = new ToolbarButtonsPanel();
		//tblButtonsPanel.addButton(btnAllocate);
		btnPaidOff.setVisible(false);
		btnMultiInstallment.setVisible(false);
		
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);
        btnLayout.addComponent(ComponentFactory.getSpaceLayout(190, Unit.PIXELS));
        btnLayout.addComponent(btnReceive);
        btnLayout.addComponent(btnMultiInstallment);
        btnLayout.addComponent(btnPaidOff);
        contentLayout.setSpacing(true);
        contentLayout.addComponent(gridLayout);
        contentLayout.addComponent(btnLayout);

        return contentLayout;
	}
	
	/**
	 * @param allocateListener the allocateListener to set
	 */
	public void setAllocateListener(ClickListener allocateListener) {
		this.allocateListener = allocateListener;
	}
	/**
	 * @param multiInstallmentListener the multiInstallmentListener to set
	 */
	public void setMultiInstallmentListener(ClickListener multiInstallmentListener) {
		this.multiInstallmentListener = multiInstallmentListener;
	}
	/**
	 * @param paidOffListener the paidOffListener to set
	 */
	public void setPaidOffListener(ClickListener paidOffListener) {
		this.paidOffListener = paidOffListener;
	}
	/**
	 * @param window the window to set
	 */
	public void setWindow(Window window) {
		this.window = window;
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

package com.nokor.efinance.core.financial.panel.product;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinProductService;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.shared.financialproduct.FinancialProductEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.finance.services.tools.LoanUtils;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Asset detail panel
 * @author ly.youhort
 *
 */
public class FinProductPanel extends AbstractTabPanel implements FinancialProductEntityField, ValueChangeListener {
		
	private static final long serialVersionUID = -8137888259948555117L;

	private FinanceCalculationService financeCalculationService = (FinanceCalculationService) SecApplicationContextHolder.getContext().getBean("financeCalculationService");
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	
	private Quotation quotation;
	
	private EntityRefComboBox<FinProduct> cbxFinancialProduct;
	
	private TextField txtAssetPrice;
	private TextField txtTermInMonth;
	private TextField txtLeaseAmount;
	private TextField txtDealerLeaseAmount;
	private TextField txtPeriodicInterestRate;
	private TextField txtAdvancePaymentPecentage;
	private TextField txtAdvancePayment;
	private TextField txtTotalInstallAmount;
	private ERefDataComboBox<EFrequency> cbxFrequency;
	private TextField txtInstallmentAmont;
	private HorizontalLayout invalidMessageLayout;
	private VerticalLayout servicesLayout;
	
	private List<TextField> txtServices;
	private List<CheckBox> cbSplitWithInstallmentServices;
	private boolean invalidQuotation;
	private ValueChangeListener financialProductValueChangeListener;
	private List<FinService> services;
	Asset asset;
	public FinProductPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected Component createForm() {
		
		invalidQuotation = true;
		
		txtServices = new ArrayList<TextField>();
		cbSplitWithInstallmentServices = new ArrayList<CheckBox>();
		
		servicesLayout = new VerticalLayout(); 
		
		Label iconInvalidMessage = new Label();
		iconInvalidMessage.setIcon(new ThemeResource("../nkr-default/icons/16/danger.png"));
		Label lblInvalidMessage = new Label(I18N.message("quotation.invalid"));
		lblInvalidMessage.setStyleName("error");
		invalidMessageLayout = new HorizontalLayout(iconInvalidMessage, lblInvalidMessage);
		invalidMessageLayout.setVisible(false);
		
		cbxFinancialProduct = new EntityRefComboBox<FinProduct>();
		BaseRestrictions<FinProduct> restrictions = new BaseRestrictions<>(FinProduct.class);
		restrictions.addCriterion(Restrictions.le(START_DATE, DateUtils.getDateAtEndOfDay(DateUtils.todayH00M00S00())));
		restrictions.addCriterion(Restrictions.ge(END_DATE, DateUtils.todayH00M00S00()));
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		cbxFinancialProduct.setRestrictions(restrictions);		
		cbxFinancialProduct.setImmediate(true);
		cbxFinancialProduct.renderer();
		cbxFinancialProduct.setSelectedEntity(null);
		
		txtAssetPrice = ComponentFactory.getTextField(false, 30, 170);
		txtAssetPrice.setStyleName("blackdisabled");
		txtAssetPrice.setEnabled(false);
				
		txtTermInMonth = ComponentFactory.getTextField(false, 10, 100);
		txtTermInMonth.setImmediate(true);
		txtTermInMonth.addValueChangeListener(this);
		
		txtLeaseAmount = ComponentFactory.getTextField(false, 50, 170);
		txtLeaseAmount.setStyleName("blackdisabled");
		txtLeaseAmount.setEnabled(false);
		
		txtDealerLeaseAmount = ComponentFactory.getTextField(false, 50, 170);
		txtDealerLeaseAmount.setVisible(false);
		
		txtPeriodicInterestRate = ComponentFactory.getTextField(false, 50, 100);
		txtPeriodicInterestRate.setImmediate(true);
		txtPeriodicInterestRate.addValueChangeListener(this);
		
		txtAdvancePaymentPecentage = ComponentFactory.getTextField(false, 50, 170);
		txtAdvancePaymentPecentage.setImmediate(true);
		txtAdvancePaymentPecentage.addValueChangeListener(this);
		
		txtAdvancePayment = ComponentFactory.getTextField(false, 50, 170);
		txtAdvancePayment.setImmediate(true);
		txtAdvancePayment.setStyleName("blackdisabled");
		txtAdvancePayment.setEnabled(false);
		
		List<EFrequency> frequencies  = new ArrayList<EFrequency>();
		frequencies.add(EFrequency.M);
		cbxFrequency = new ERefDataComboBox<EFrequency>(frequencies);
		cbxFrequency.setImmediate(true);
		cbxFrequency.setSelectedEntity(EFrequency.M);
		cbxFrequency.addValueChangeListener(this);
		
		txtInstallmentAmont = ComponentFactory.getTextField(false, 50, 150);
		txtInstallmentAmont.setStyleName("blackdisabled");
		txtInstallmentAmont.setEnabled(false);
		
		txtTotalInstallAmount = ComponentFactory.getTextField(false, 50, 150);
		txtTotalInstallAmount.setStyleName("blackdisabled");
		txtTotalInstallAmount.setEnabled(false);
		
		if (cbxFinancialProduct.getSelectedEntity() == null) {
			resetFinanceProduct();
		}
		
		cbxFinancialProduct.addValueChangeListener(this);
		financialProductValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = 1812853593330316633L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxFinancialProduct.getSelectedEntity() != null) { 
					setInvalidQuotationFlag(true);
					Long fiprdId = cbxFinancialProduct.getSelectedEntity().getId();
					
					// Need to reload
					FinProduct financialProduct = entityService.getById(FinProduct.class, fiprdId); 
					
					cbxFrequency.setSelectedEntity(financialProduct.getFrequency());
					txtTermInMonth.setValue(getDefaultString(financialProduct.getTerm()));
					txtAdvancePaymentPecentage.setValue(getDefaultString(financialProduct.getAdvancePaymentPercentage()));
					txtPeriodicInterestRate.setValue(getDefaultString(financialProduct.getPeriodicInterestRate()));
		
					displayServices(quotation);
					if (txtServices != null && !txtServices.isEmpty()) {
						for (TextField txtTiServiceAmount : txtServices) {
							FinService service = (FinService) txtTiServiceAmount.getData();
							if (service.getCalculMethod().equals(ECalculMethod.FIX)) {
			        			txtTiServiceAmount.setValue(AmountUtils.format(service.getTiPrice()));
			        		} else if (service.getCalculMethod().equals(ECalculMethod.MAT)) {
			        			reloadServiceAmount(txtTiServiceAmount, service);
			        		}
						}
					}					
				} else {
					resetFinanceProduct();
					displayServices(quotation);
				}
			}
		};
				
		Button btnCalcul = new Button("");
		btnCalcul.setIcon(new ThemeResource("icons/32/calculatrice.png"));
		btnCalcul.setStyleName(Reindeer.BUTTON_LINK);
		btnCalcul.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6516775560049407997L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (checkValidityFiels()) {
					CalculationParameter calculationParameter = new CalculationParameter();
					calculationParameter.setInitialPrincipal(getDouble(txtLeaseAmount, 0d));
					calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(getInteger(txtTermInMonth), cbxFrequency.getSelectedEntity()));
					calculationParameter.setPeriodicInterestRate(getDouble(txtPeriodicInterestRate, 0d) / 100);
					calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(cbxFinancialProduct.getSelectedEntity().getNumberOfPrincipalGracePeriods()));
					double installmentAmount = MyMathUtils.roundAmountTo(financeCalculationService.getInstallmentPayment(calculationParameter));
					txtInstallmentAmont.setValue(AmountUtils.format(installmentAmount));
					calTotalInstallmentAmount(installmentAmount);
					recalculateAdvancePaymentAmont();
					setInvalidQuotationFlag(false);
				}
			}
		});
		
		String template = "quotationFinanceCalculation";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout inputFieldLayout = null;
		try {
			inputFieldLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		inputFieldLayout.setSizeFull();
		inputFieldLayout.addComponent(new Label(I18N.message("financial.product")), "lblFinancialProduct");
		inputFieldLayout.addComponent(cbxFinancialProduct, "cbxFinancialProduct");
		inputFieldLayout.addComponent(new Label(I18N.message("asset.price")), "lblAssetPrice");
		inputFieldLayout.addComponent(txtAssetPrice, "txtAssetPrice");
		inputFieldLayout.addComponent(new Label(I18N.message("advance.payment.percentage")), "lblAdvancePaymentPecentage");
		inputFieldLayout.addComponent(txtAdvancePaymentPecentage, "txtAdvancePaymentPecentage");
		inputFieldLayout.addComponent(new Label(I18N.message("advance.payment")), "lblAdvancePayment");
		inputFieldLayout.addComponent(txtAdvancePayment, "txtAdvancePayment");
		inputFieldLayout.addComponent(new Label(I18N.message("lease.amount")), "lblLeaseAmount");
		inputFieldLayout.addComponent(txtLeaseAmount, "txtLeaseAmount");
		inputFieldLayout.addComponent(new Label(I18N.message("frequency")), "lblFrequency");
		inputFieldLayout.addComponent(cbxFrequency, "cbxFrequency");
		inputFieldLayout.addComponent(new Label(I18N.message("term.month")), "lblTermInMonth");
		inputFieldLayout.addComponent(txtTermInMonth, "txtTermInMonth");
		inputFieldLayout.addComponent(new Label(I18N.message("periodic.interest.rate")), "lblPeriodicInterestRate");
		inputFieldLayout.addComponent(txtPeriodicInterestRate, "txtPeriodicInterestRate");
		inputFieldLayout.addComponent(new Label(I18N.message("installment.amount")), "lblInstallmentAmont");
		inputFieldLayout.addComponent(new HorizontalLayout(txtInstallmentAmont, btnCalcul), "txtInstallmentAmont");
		inputFieldLayout.addComponent(invalidMessageLayout, "lblInvalidMessage");
		
		txtAdvancePaymentPecentage.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4508483753757807372L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				recalculateAdvancePaymentAmont();
			}
		});
		
		final Panel financialProductPanel = new Panel(I18N.message("financial.product"));
		
		VerticalLayout contentLayout = new VerticalLayout(inputFieldLayout, servicesLayout);
		contentLayout.setMargin(true);
		financialProductPanel.setContent(contentLayout);
		
		setInvalidQuotationFlag(true);
		return financialProductPanel;
	}
	
	/**
	 * @param financialProduct
	 */
	private void displayServices(Quotation quotation) {
		int quotationServiceSize = 0;
		// Display services
		if (quotation != null) {
			services = new ArrayList<FinService>();
			if (quotation.getQuotationServices() != null) {
				quotationServiceSize = quotation.getQuotationServices().size();
			}
			if (cbxFinancialProduct.getSelectedEntity() != null) {
				FinProduct financialProduct = entityService.getById(FinProduct.class, cbxFinancialProduct.getSelectedEntity().getId());
				for (FinProductService financialService : financialProduct.getFinancialProductServices()) {
					if (EServiceType.list().contains(financialService.getService().getServiceType())) {
						services.add(financialService.getService());
					}
				}
			} else {
				resetFinanceProduct();
			}
		}
		
		txtServices.clear();
		cbSplitWithInstallmentServices.clear();
		if (services != null && !services.isEmpty()) {
			Panel servicePanel = new Panel(I18N.message("services"));
			final GridLayout gridServiceLayout = new GridLayout(10, services.size() + 1);
			gridServiceLayout.setSpacing(true);
			gridServiceLayout.setMargin(true);
			gridServiceLayout.addComponent(ComponentFactory.getLabel("", 150), 0, 0);
			gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 1, 0);
			gridServiceLayout.addComponent(ComponentFactory.getLabel("amount", 150), 2, 0);
			gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 3, 0);
			// gridServiceLayout.addComponent(ComponentFactory.getLabel("include.in.installment", 150), 4, 0);
			// gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 5, 0);
			gridServiceLayout.addComponent(ComponentFactory.getLabel("split.with.installment", 150), 4, 0);
			gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 5, 1);
			
			gridServiceLayout.addComponent(new Label(I18N.message("total.installment.amount")), 6, 1);
			gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 7, 1);
			gridServiceLayout.addComponent(txtTotalInstallAmount, 8, 1);
			
			int i = 1;
        	for (FinService service : services) {        		
				if (service.getStatusRecord().equals(EStatusRecord.ACTIV)
						|| services.size() == quotationServiceSize) {
	        		TextField txtTiServiceAmount = ComponentFactory.getTextField(50, 150);
	        		
	         		CheckBox cbSplitWithInstallment = new CheckBox();
	         		cbSplitWithInstallment.setEnabled(false);
	         		cbSplitWithInstallment.setValue(service.isSplitWithInstallment());
	         		
	        		txtTiServiceAmount.setData(service);
	        		txtTiServiceAmount.setEnabled(service.isAllowChangePrice());

	        		gridServiceLayout.addComponent(new Label(service.getDescEn()), 0, i);
	        		gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 1, i);
	        		gridServiceLayout.addComponent(txtTiServiceAmount, 2, i);
	        		// gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 3, i);
	        		// gridServiceLayout.addComponent(cbIncludeInInstallment, 4, i);
	        		gridServiceLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), 3, i);
	        		gridServiceLayout.addComponent(cbSplitWithInstallment, 4, i);
	        		txtServices.add(txtTiServiceAmount);
	        		cbSplitWithInstallmentServices.add(cbSplitWithInstallment);
	        		i++;
				}

        	}
        	servicePanel.setContent(gridServiceLayout);
        	servicesLayout.removeAllComponents();
        	servicesLayout.addComponent(servicePanel);
		}
	}
	
	/**
	 * 
	 */
	private void recalculateAdvancePaymentAmont() {
		double cashPrice = getDouble(txtAssetPrice, 0d);
		double advancePaymentAmontPc = getDouble(txtAdvancePaymentPecentage, 0d);
		double advancePaymentAmont = advancePaymentAmontPc *  cashPrice / 100; 
		txtAdvancePayment.setValue(AmountUtils.format(advancePaymentAmont));
		recalculateLeaseAmont();
	}
	
	/**
	 */
	private void recalculateLeaseAmont() {
		double cashPrice = getDouble(txtAssetPrice, 0d);
		double advancePaymentAmont = getDouble(txtAdvancePayment, 0d);
		txtDealerLeaseAmount.setValue(AmountUtils.format(cashPrice - advancePaymentAmont));
		txtLeaseAmount.setValue(AmountUtils.format(getDouble(txtDealerLeaseAmount, 0d)));
	}
	
	/**
	 * @param txtTiServiceAmount
	 * @param service
	 */
	private void reloadServiceAmount(TextField txtTiServiceAmount, FinService service) {
		// TODO PYT
//		if (quotation.getAsset() != null && quotation.getAsset().getAsmodIdFk() != null) {
//			AssetModel assetModel = entityService.getById(AssetModel.class, quotation.getAsset().getAsmodIdFk());
//			MatrixPrice matrixPrice = matrixPricingService.getServiceMatrixPrice(assetModel, service);
//			if (matrixPrice != null) {
//				txtTiServiceAmount.setValue(AmountUtils.format(matrixPrice.getTiPriceUsd()));
//			} else {
//				txtTiServiceAmount.setValue("");
//				MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
//						MessageBox.Icon.ERROR, I18N.message("service.price.configuration.problem"), Alignment.MIDDLE_RIGHT,
//						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
//				mb.show();
//			}
//		}
	}
	
	/**
	 * @param enabled
	 */
	public void setEnabledFinancialProduct(boolean enabled) {
		cbxFinancialProduct.setEnabled(enabled);
		txtAdvancePaymentPecentage.setEnabled(enabled);
		cbxFrequency.setEnabled(enabled);
		txtTermInMonth.setEnabled(enabled);
		txtPeriodicInterestRate.setEnabled(enabled);
		if (txtServices != null || !txtServices.isEmpty()) {
			for (int i = 0; i < txtServices.size(); i++) {
				TextField txtService = txtServices.get(i);
				txtService.setEnabled(enabled);
			}
		}
	}
	
	/**
	 * Assign value to quotation 
	 * @param asset
	 */
	public void assignValues(Quotation quotation) {
		cbxFinancialProduct.removeValueChangeListener(financialProductValueChangeListener);
		
		this.quotation = quotation;
		Asset asset = quotation.getAsset();
		if (asset != null) {
			txtAssetPrice.setValue(AmountUtils.format(asset.getTiAssetPrice()));
			cbxFinancialProduct.setSelectedEntity(quotation.getFinancialProduct());
			txtTermInMonth.setValue(getDefaultString(quotation.getTerm()));
			txtDealerLeaseAmount.setValue(AmountUtils.format(quotation.getTiFinanceAmount()));
			txtPeriodicInterestRate.setValue(AmountUtils.format(quotation.getInterestRate()));
			txtAdvancePaymentPecentage.setValue(AmountUtils.format(quotation.getAdvancePaymentPercentage()));
			txtAdvancePayment.setValue(AmountUtils.format(quotation.getTiAdvancePaymentAmount()));
	
			txtLeaseAmount.setValue(AmountUtils.format(MyNumberUtils.getDouble(quotation.getTiFinanceAmount())));
			txtPeriodicInterestRate.setValue(getDefaultString(quotation.getInterestRate()));
			txtAdvancePaymentPecentage.setValue(getDefaultString(quotation.getAdvancePaymentPercentage()));
			txtAdvancePayment.setValue(AmountUtils.format(quotation.getTiAdvancePaymentAmount()));
	
			cbxFrequency.setSelectedEntity(quotation.getFrequency());
			txtInstallmentAmont.setValue(AmountUtils.format(quotation.getTiInstallmentAmount()));
			displayServices(quotation);
		
			if (txtServices != null && !txtServices.isEmpty()) {
				List<QuotationService> quotationServices = quotation.getQuotationServices();
				for (int i = 0; i < txtServices.size(); i++) {
					TextField txtService = txtServices.get(i);
					FinService service = (FinService) txtService.getData();
					boolean found = false;
					if (quotationServices != null && !quotationServices.isEmpty()) {
						for (QuotationService quotationService : quotationServices) {
							if (service.getId().equals(quotationService.getService().getId())) {
								txtService.setValue(AmountUtils.format(quotationService.getTiPrice()));
								cbSplitWithInstallmentServices.get(i).setValue(quotationService.isSplitWithInstallment());
								found = true;
								break;
							}
						}
					}
					if (!found) {
						txtService.setValue(getDefaultString(0.00));
						cbSplitWithInstallmentServices.get(i).setValue(false);
					}
				}
			} else {
				// DO NOT Delete, force lazy loading
				if (quotation.getQuotationServices() != null) {
					quotation.getQuotationServices().size();
				}
			}
			calTotalInstallmentAmount(quotation.getTiInstallmentAmount());
			setInvalidQuotationFlag(!quotation.isValid() || (asset != null && asset.isHasChanged()));
			if (invalidQuotation) {
				recalculateAdvancePaymentAmont();
				if (asset != null) {
					if (asset.isHasChanged()) {
						cbxFinancialProduct.setSelectedEntity(null);
					}
					asset.setHasChanged(false);
				}
			}
		}
		
		if (ProfileUtil.isAdmin()) {
			setEnabledFinancialProduct(true);
		} 	
		boolean enable = false;
		if((ProfileUtil.isUnderwriter() ||  ProfileUtil.isUnderwriterSupervisor() || ProfileUtil.isManager())
			           && (quotation.getWkfStatus().equals(QuotationWkfStatus.QUO)
			    		  	|| quotation.getWkfStatus().equals(QuotationWkfStatus.PRO)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.DEC)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.PRA)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.SUB)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.RFC)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.REJ)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.REU)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.APU)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.APS)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWS)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWT)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWU))) {
					enable = true;	
			}
		
		 if(ProfileUtil.isPOS() && quotation.getWkfStatus().equals(QuotationWkfStatus.QUO))
			 enable = true;
		 
		 setEnabled(enable);
		//setEnabled(quotation.getWkfStatus() == QuotationStatus.QUO);
		 cbxFinancialProduct.addValueChangeListener(financialProductValueChangeListener);

	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		setInvalidQuotationFlag(true);
	}
	
	/**
	 * Get quotation
	 * @param quotation
	 * @return
	 */
	public Quotation getQuotation(Quotation quotation) {
		quotation.setFinancialProduct(cbxFinancialProduct.getSelectedEntity());
		quotation.setAdvancePaymentPercentage(getDouble(txtAdvancePaymentPecentage, 0d));
		quotation.setTiAdvancePaymentAmount(getDouble(txtAdvancePayment, 0d));
		quotation.setTeAdvancePaymentAmount(quotation.getTiAdvancePaymentAmount());
		quotation.setVatAdvancePaymentAmount(0d);
		quotation.setTiFinanceAmount(getDouble(txtDealerLeaseAmount, 0d));
		quotation.setVatFinanceAmount(0.00);
		quotation.setTeFinanceAmount(quotation.getTiFinanceAmount());
		quotation.setInterestRate(getDouble(txtPeriodicInterestRate, 0d));
		quotation.setTerm(getInteger(txtTermInMonth));
		quotation.setFrequency(cbxFrequency.getSelectedEntity());
		quotation.setTiInstallmentAmount(getDouble(txtInstallmentAmont, 0d));
		quotation.setTeInstallmentAmount(quotation.getTiInstallmentAmount());
		quotation.setVatInstallmentAmount(0d);
		if (cbxFinancialProduct.getSelectedEntity() != null) {
			quotation.setNumberOfPrincipalGracePeriods(cbxFinancialProduct.getSelectedEntity().getNumberOfPrincipalGracePeriods());
		}
		quotation.setValid(!invalidQuotation);
		List<QuotationService> quotationServices = quotation.getQuotationServices();
		if (quotationServices == null) {
			quotationServices = new ArrayList<QuotationService>();
		} else {
			// By default delete all existing QuotationService
			// The update the flag UPDATE or CREATE will be done later.
			for (QuotationService quotationService : quotationServices) {
				quotationService.setCrudAction(CrudAction.DELETE);
			}
		}
		if (txtServices != null && !txtServices.isEmpty()) {
			for (int i = 0; i < txtServices.size(); i++) {
				TextField txtService = txtServices.get(i);
				double serviceAmount = getDouble(txtService, 0d);
				FinService service = (FinService) txtService.getData();
				if (serviceAmount > 0) {
					QuotationService quotationService = quotation.getQuotationService(service.getId());
					if (quotationService == null) {
						quotationService = new QuotationService();
						quotationService.setCrudAction(CrudAction.CREATE);
						quotationServices.add(quotationService);
					} else {
						quotationService.setCrudAction(CrudAction.UPDATE);
					}
					quotationService.setQuotation(quotation);
					quotationService.setService(service);
					quotationService.setTiPrice(serviceAmount);
					quotationService.setTePrice(serviceAmount);
					quotationService.setVatPrice(0.0);
					quotationService.setSplitWithInstallment(cbSplitWithInstallmentServices.get(i).getValue());
				}
			}
		}
		quotation.setQuotationServices(quotationServices);
		return quotation;
	}
	
	public boolean checkValidityFiels() {
		super.removeErrorsPanel();
		checkMandatorySelectField(cbxFinancialProduct, "financial.product");
		checkMandatoryField(txtAssetPrice, "asset.price");
		checkDoubleField(txtAssetPrice, "asset.price");
		checkMandatoryField(txtTermInMonth, "term.month");
		checkIntegerField(txtTermInMonth, "term.month");
		checkMandatoryField(txtPeriodicInterestRate, "periodic.interest.rate");
		checkDoubleField(txtPeriodicInterestRate, "periodic.interest.rate");
		checkMandatoryField(txtAdvancePaymentPecentage, "advance.payment.percentage");
		checkDoubleField(txtAdvancePaymentPecentage, "advance.payment.percentage");
		checkMandatorySelectField(cbxFrequency, "frequency");
		
		if (cbxFinancialProduct.getSelectedEntity() != null) {
			double minAdvancePaymentPercentage = MyNumberUtils.getDouble(cbxFinancialProduct.getSelectedEntity().getMinAdvancePaymentPercentage());
			double advancePaymentPercentage = MyNumberUtils.getDouble(getDouble(txtAdvancePaymentPecentage));
			if (minAdvancePaymentPercentage > 0 && advancePaymentPercentage < minAdvancePaymentPercentage) {
				errors.add(I18N.message("advance.payment.less.than.value.of.financial.product", String.valueOf(minAdvancePaymentPercentage)));
			}
		}
		Double maxAdvancePaymentPercentage = null;
		// TODO PYT
//		AssetModel assetModel = entityService.getById(AssetModel.class, quotation.getAsset().getAsmodIdFk());
//		if (assetModel != null) {
//			maxAdvancePaymentPercentage = assetModel.getMaxAdvancePaymentPercentage();
//		}
		if (maxAdvancePaymentPercentage != null && maxAdvancePaymentPercentage != 0d
				&& MyNumberUtils.getDouble(getDouble(txtAdvancePaymentPecentage)) >= maxAdvancePaymentPercentage) {
			errors.add(I18N.message("advance.payment.greater.than.value.of.max.assetmodel", String.valueOf(maxAdvancePaymentPercentage)));
		}
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		
		return errors.isEmpty();
		
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {		
		return true;
	}
	
	/**
	 * @return
	 */
	public List<String> fullValidate() {		
		super.removeErrorsPanel();
		checkMandatorySelectField(cbxFinancialProduct, "financial.product");
		checkMandatoryField(txtAssetPrice, "asset.price");
		checkDoubleField(txtAssetPrice, "asset.price");
		checkMandatoryField(txtTermInMonth, "term.month");
		checkIntegerField(txtTermInMonth, "term.month");
		checkMandatoryField(txtPeriodicInterestRate, "periodic.interest.rate");
		checkDoubleField(txtPeriodicInterestRate, "periodic.interest.rate");
		checkMandatoryField(txtAdvancePaymentPecentage, "advance.payment.percentage");
		checkDoubleField(txtAdvancePaymentPecentage, "advance.payment.percentage");
		checkMandatorySelectField(cbxFrequency, "frequency");
		if (invalidQuotation) {
			errors.add(I18N.message("quotation.invalid"));
		}
		return errors;
	}
	
	/**
	 * Set invalid quotation flag
	 * @param invalid
	 */
	public void setInvalidQuotationFlag(boolean invalid) {
		invalidQuotation = invalid;
		invalidMessageLayout.setVisible(invalid);
	}

	/**
	 * Reset 
	 */
	public void reset() {
		assignValues(new Quotation());
	}
	
	private void resetFinanceProduct() {
		servicesLayout.removeAllComponents();
		cbxFrequency.setSelectedEntity(null);
		txtTermInMonth.setValue("");
		txtAdvancePaymentPecentage.setValue("0");
		txtPeriodicInterestRate.setValue("");
		txtServices.clear();
		cbSplitWithInstallmentServices.clear();
	}
	
	/**
	 * @param installmentAmounts
	 */
	private void calTotalInstallmentAmount(Double installmentAmounts) {
		Double insuranceFee = 0d;
		Double servicingFee = 0d;
		Double totalInstallmentAmount = 0d;
		installmentAmounts = installmentAmounts == null ? 0d : installmentAmounts;
		int numPaidPerYear = 1;
		int term = 0;
		
		if (txtTermInMonth.getValue() != ""
				&& txtServices.get(0).getValue() != ""
				&& txtServices.get(1).getValue() != ""
				&& cbxFrequency.getSelectedEntity() != null) {
				
			term = Integer.valueOf(txtTermInMonth.getValue());
			insuranceFee = Double.valueOf(txtServices.get(0).getValue());
			servicingFee = Double.valueOf(txtServices.get(1).getValue());
			
			if (cbxFrequency.getSelectedEntity().equals(EFrequency.A)) {
				numPaidPerYear =1;
			} else if (cbxFrequency.getSelectedEntity().equals(EFrequency.H)) {
				numPaidPerYear = 2;
			} else if (cbxFrequency.getSelectedEntity().equals(EFrequency.M)) {
				numPaidPerYear = 12;
			} else {
				numPaidPerYear = 4;
			}
			
		}
		insuranceFee = MyMathUtils.roundAmountTo(((term / numPaidPerYear) * insuranceFee) / term);
		servicingFee = MyMathUtils.roundAmountTo(servicingFee/term); 
		totalInstallmentAmount = insuranceFee + servicingFee + installmentAmounts;
		txtTotalInstallAmount.setValue(AmountUtils.format(totalInstallmentAmount) + "");
	}		
}

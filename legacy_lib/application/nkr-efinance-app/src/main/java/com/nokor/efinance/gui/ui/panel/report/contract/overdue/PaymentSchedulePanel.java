package com.nokor.efinance.gui.ui.panel.report.contract.overdue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * Payment schedule panel
 * @author uhout.cheng
 */
public class PaymentSchedulePanel extends AbstractTabPanel implements FMEntityField {

	/**	 */
	private static final long serialVersionUID = -3266945821182447130L;
	
	private FinanceCalculationService financeCalculationService = SpringUtils.getBean(FinanceCalculationService.class);
	
	private SimplePagedTable<Quotation> pagedTable;
	private Quotation quotation;
	private VerticalLayout verticalLayout;
	private HorizontalLayout tableControl;
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		
		return verticalLayout;
	}
	
	/** */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer() {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		
		CalculationParameter calculationParameter = new CalculationParameter();
		calculationParameter.setFrequency(quotation.getFrequency());
		calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
		calculationParameter.setNumberOfPeriods(quotation.getTerm());
		calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100);
		calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(quotation.getNumberOfPrincipalGracePeriods()));
		
		Date contractStartDate = quotation.getContractStartDate();
		if (contractStartDate == null) {
			contractStartDate = DateUtils.today();
		}
		
		Date firstPaymentDate = quotation.getFirstDueDate();
		if (firstPaymentDate == null) {
			firstPaymentDate = DateUtils.today();
		}
		
		AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(contractStartDate, firstPaymentDate, calculationParameter);
		
		double insuranceFeeTmp1 = getServiceFee("INSFEE", quotation.getQuotationServices());
		
		int term = quotation.getTerm();
		double insuranceFeeTmp2 = getServiceFee(term, "INSFEE", quotation.getQuotationServices()); 
		double servicingFee = getServiceFee(term, "SERFEE", quotation.getQuotationServices());
		
		int nbMonth = amortizationSchedules.getSchedules().size() ;
		
		for (int i = 0; i < nbMonth; i++) {
			Schedule schedule = amortizationSchedules.getSchedules().get(i);
			
			double installmentAmountTmp1 = schedule.getInstallmentPayment() + insuranceFeeTmp1;
			double installmentAmountTmp2 = schedule.getInstallmentPayment() + servicingFee + insuranceFeeTmp2;
			
			Item item = indexedContainer.addItem(i);
			item.getItemProperty("num.installment").setValue(schedule.getN());
			item.getItemProperty(DUE_DATE).setValue(schedule.getInstallmentDate());
			// if (quotation.getTemplateType().equals(ETemplateType.TEMPLATE_2)) {
			if (true) {
				item.getItemProperty("installment.amount").setValue(AmountUtils.convertToAmount(installmentAmountTmp2));
				item.getItemProperty("service.amount").setValue(AmountUtils.convertToAmount(servicingFee));
				item.getItemProperty("insurance.amount").setValue(AmountUtils.convertToAmount(insuranceFeeTmp2));	
			} else {
				if (i == 12 || i == 24 || i == 36 || i == 48 || i == 60 || i == 72) {
					item.getItemProperty("installment.amount").setValue(AmountUtils.convertToAmount(installmentAmountTmp1));
					item.getItemProperty("insurance.amount").setValue(AmountUtils.convertToAmount(insuranceFeeTmp1));
				} else {
					item.getItemProperty("installment.amount").setValue(AmountUtils.convertToAmount(schedule.getInstallmentPayment()));
					item.getItemProperty("insurance.amount").setValue(AmountUtils.convertToAmount(0d));
				}
			}
			item.getItemProperty("principle.payment").setValue(AmountUtils.convertToAmount(schedule.getPrincipalAmount()));
			item.getItemProperty("interest.amount").setValue(AmountUtils.convertToAmount(schedule.getInterestAmount()));
			item.getItemProperty("remaining.balance").setValue(AmountUtils.convertToAmount(schedule.getBalanceAmount()));
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * 
	 * @param term
	 * @param serviceCode
	 * @param services
	 * @return
	 */
	private Double getServiceFee(int term, String serviceCode, 
			List<com.nokor.efinance.core.quotation.model.QuotationService> services) {
		for (com.nokor.efinance.core.quotation.model.QuotationService service : services) {
			if (service.getService().getCode().equals(serviceCode)) {
				if (service.getService().getFrequency() != null) {
					int nbMonths = service.getService().getFrequency().getNbMonths();
					return MyMathUtils.roundAmountTo(((term / nbMonths) * service.getTiPrice()) / term);
				} else {
					return MyMathUtils.roundAmountTo(service.getTiPrice() / term);
				}
			}
		}
		return 0d;
	}
	
	/**
	 * @param serviceCode
	 * @param services
	 * @return
	 */
	private Double getServiceFee(String serviceCode, List<com.nokor.efinance.core.quotation.model.QuotationService> services) {
		for (com.nokor.efinance.core.quotation.model.QuotationService service : services) {
			if (service.getService().getCode().equals(serviceCode)) {
				return service.getTiPrice();
			}
		}
		return 0d;
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		
		columnDefinitions.add(new ColumnDefinition("num.installment", I18N.message("num.installment"), 
				Integer.class, Align.RIGHT, 140));
		columnDefinitions.add(new ColumnDefinition(DUE_DATE, I18N.message("due.date"), 
				Date.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("installment.amount" , I18N.message("installment.amount"), 
				Amount.class, Align.RIGHT, 140));
		// if (quotation.getTemplateType().equals(ETemplateType.TEMPLATE_2)) {
		if (true) {
			columnDefinitions.add(new ColumnDefinition("service.amount", I18N.message("service.amount"), 
					Amount.class, Align.RIGHT, 110));
			columnDefinitions.add(new ColumnDefinition("insurance.amount", I18N.message("insurance.amount"), 
					Amount.class, Align.RIGHT, 130));
		} else {
			columnDefinitions.add(new ColumnDefinition("insurance.amount", I18N.message("insurance.amount"), 
					Amount.class, Align.RIGHT, 130));
		}
		columnDefinitions.add(new ColumnDefinition("principle.payment", I18N.message("principal.amount"), 
				Amount.class, Align.RIGHT, 125));
		columnDefinitions.add(new ColumnDefinition("interest.amount", I18N.message("interest.amount"), 
				Amount.class, Align.RIGHT, 120));
		columnDefinitions.add(new ColumnDefinition("remaining.balance", I18N.message("remaining.balance"), 
				Amount.class, Align.RIGHT, 170));
		
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {
		this.quotation = quotation;
		
		if (pagedTable != null) {
			verticalLayout.removeComponent(pagedTable);
			verticalLayout.removeComponent(tableControl);
		}
		
		pagedTable = new SimplePagedTable<Quotation>(createColumnDefinitions());
		verticalLayout.addComponent(pagedTable);
		tableControl = pagedTable.createControls();
		verticalLayout.addComponent(tableControl);
		setIndexedContainer();
	}
}

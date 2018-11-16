package com.nokor.efinance.gui.ui.panel.report.uwrejectreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationSupportDecision;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UWRejectReportTablePanel extends AbstractTablePanel<Quotation> implements FMEntityField {
	
	/** */
	private static final long serialVersionUID = 5204101495689053450L;
	private static final String QUALITY_INCENTIVE = "uw.reject.report";
	@Autowired
	private ContractService contractService;
	private Quotation quotation;
	private List<FinService> services;
	private double monthlyInstallment = 0;

	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message(QUALITY_INCENTIVE));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		super.init("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Quotation> createPagedDataProvider() {
		PagedDefinition<Quotation> pagedDefinition = new PagedDefinition<Quotation>(searchPanel.getRestrictions());
		
		pagedDefinition.setRowRenderer(new QualityIncentiveRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition(CUSTOMER, I18N.message("customers"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(DEALER , I18N.message("dealer"), String.class, Align.LEFT, 230);
		pagedDefinition.addColumnDefinition("apply.date" , I18N.message("apply.date"), Date.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("reject.date" , I18N.message("reject.date"), Date.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("reject.resons" , I18N.message("reject.resons"), String.class, Align.LEFT, 230);
		pagedDefinition.addColumnDefinition("field.check" , I18N.message("field.check"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("advance.payment" , I18N.message("advance.payment"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("lease.term", I18N.message("lease.term"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("repayment.cap.by.pos", I18N.message("repayment.cap.by.pos"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("repayment.cap.by.uw", I18N.message("repayment.cap.by.uw"), String.class, Align.LEFT, 180);
		pagedDefinition.addColumnDefinition("uw", I18N.message("uw"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("us", I18N.message("us"), String.class, Align.LEFT, 100);

		EntityPagedDataProvider<Quotation> pagedDataProvider = new EntityPagedDataProvider<Quotation>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 *@author buntha.chea
	 */
	private class QualityIncentiveRowRenderer implements RowRenderer {

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.RowRenderer#renderer(com.vaadin.data.Item, org.seuksa.frmk.Entity.model.entity.Entity)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) { 
		    quotation = (Quotation) entity;
		    List<QuotationSupportDecision> quotationSupportDecisions = quotation.getQuotationSupportDecisions(QuotationWkfStatus.REJ);
			item.getItemProperty(ID).setValue(quotation.getId());
			if(quotation.getApplicant() != null){
				item.getItemProperty(CUSTOMER).setValue( quotation.getApplicant().getIndividual().getLastNameEn() +" "+ quotation.getApplicant().getIndividual().getFirstNameEn());
			}
			item.getItemProperty(DEALER).setValue(quotation.getDealer().getNameEn());
			item.getItemProperty("apply.date").setValue(quotation.getFirstSubmissionDate());
			item.getItemProperty("reject.date").setValue(getRejectDate());
			if(quotationSupportDecisions != null){
				for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisions) {
					if(quotationSupportDecision.getSupportDecision() != null){
						item.getItemProperty("reject.resons").setValue(quotationSupportDecision.getSupportDecision().getDescEn());
					}	
				}
			}
				
			item.getItemProperty("field.check").setValue(quotation.isFieldCheckPerformed() ?I18N.message("yes") : I18N.message("no"));
			item.getItemProperty("advance.payment").setValue(quotation.getAdvancePaymentPercentage().intValue() + "%");
			item.getItemProperty("lease.term").setValue(quotation.getTerm());
			item.getItemProperty("repayment.cap.by.pos").setValue(AmountUtils.format(getRationByCOInterview()));
			if(getRationByUW() != null){
				item.getItemProperty("repayment.cap.by.uw").setValue(AmountUtils.format(getRationByUW()));	
			}
			if(quotation.getUnderwriter() != null){
				item.getItemProperty("uw").setValue(quotation.getUnderwriter().getDesc());
			}	
			if(quotation.getUnderwriterSupervisor() != null){
				item.getItemProperty("us").setValue(quotation.getUnderwriterSupervisor().getDesc());
			}
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Quotation getEntity() {
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<Quotation> createSearchPanel() {
		return new UWRejectReportSearchPanel(this);
	}
	/**
	 * 
	 * @return
	 * calculate Reject Date
	 */
	private Date getRejectDate(){
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addCriterion(Restrictions.eq("quotation.id", quotation.getId()));
//		List<QuotationStatusHistory> list = entityService.list(restrictions);
//		for (QuotationStatusHistory quotationStatusHistory : list) {
//			if(quotationStatusHistory.getWkfStatus().equals(WkfQuotationStatus.REJ)) {
//				return quotationStatusHistory.getUpdateDate();
//			}
//		}
		return null;
	}
	private Double getRationByCOInterview(){
		Applicant customer = quotation.getApplicant();
		double totalNetIncome = 0d;
		double totalRevenus = 0d;
		double totalAllowance = 0d;
		double totalBusinessExpenses = 0d;
		double totalDebtInstallment = MyNumberUtils.getDouble(customer.getIndividual().getTotalDebtInstallment());
		List<Employment> employments = customer.getIndividual().getEmployments();
		for (Employment employment : employments) {
			totalRevenus += MyNumberUtils.getDouble(employment.getRevenue());
			totalAllowance += MyNumberUtils.getDouble(employment.getAllowance());
			totalBusinessExpenses += MyNumberUtils.getDouble(employment
					.getBusinessExpense());
		}
		totalNetIncome = totalRevenus + totalAllowance - totalBusinessExpenses;

		double totalExpenses = MyNumberUtils.getDouble(customer.getIndividual().getMonthlyPersonalExpenses())
				+ MyNumberUtils.getDouble(customer.getIndividual().getMonthlyFamilyExpenses())	+ totalDebtInstallment;

		double disposableIncome = totalNetIncome - totalExpenses;
		monthlyInstallment = calTotalInstallmentAmount(quotation);
		double customerRatio = disposableIncome / monthlyInstallment;
		
		return customerRatio;
	}
	private Double getRationByUW(){
		double uwNetIncome = MyNumberUtils.getDouble(quotation.getUwRevenuEstimation()) + MyNumberUtils.getDouble(quotation.getUwAllowanceEstimation()) - MyNumberUtils.getDouble(quotation.getUwBusinessExpensesEstimation());
		double uwDisposableIncome = uwNetIncome - MyNumberUtils.getDouble(quotation.getUwPersonalExpensesEstimation()) - MyNumberUtils.getDouble(quotation.getUwFamilyExpensesEstimation()) - MyNumberUtils.getDouble(quotation.getUwLiabilityEstimation());
		double underwriterRatio = uwDisposableIncome / monthlyInstallment;
		return underwriterRatio;
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
				
				insuranceFee = MyMathUtils.roundAmountTo((quotation.getTerm() / nbPaidPerYear) * insuranceFee) / quotation.getTerm();
				servicingFee = MyMathUtils.roundAmountTo(servicingFee / quotation.getTerm());	
				
				totalInsallmentAmount = MyMathUtils.roundAmountTo(quotation.getTiInstallmentAmount() + insuranceFee + servicingFee);

		}
		return totalInsallmentAmount;
	}
	

}

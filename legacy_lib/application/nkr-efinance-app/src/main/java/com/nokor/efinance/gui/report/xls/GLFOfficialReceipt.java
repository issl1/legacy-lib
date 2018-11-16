package com.nokor.efinance.gui.report.xls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.widget.NumberToWords;
import com.nokor.efinance.tools.report.Report;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * Down payment receipt for front office
 * @author sok.vina
 */
public class GLFOfficialReceipt implements Report {

	protected EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");

	/**
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		
		Long paymnId = (Long) reportParameter.getParameters().get("paymnId");
		Payment payment = entityService.getById(Payment.class, paymnId);
		List<Cashflow> cashflows = payment.getCashflows();
		Cashflow cashflowFirstIndex = payment.getCashflows().get(0);
		Contract contract = cashflowFirstIndex.getContract();
		Dealer dealer = payment.getDealer();
		Applicant applicant = payment.getApplicant();
		double advancePayment = 0d;
		double installmentOther = 0d;
		double secondPayment = 0d;
		if (payment.getCashflows() != null) {
			advancePayment = contract.getTiAdvancePaymentAmount();
			for (Cashflow cashflow : cashflows) {
				if (cashflow.getCashflowType().equals(ECashflowType.FEE) || !cashflow.getCashflowType().equals(ECashflowType.FIN)) {
					installmentOther += cashflow.getTiInstallmentAmount();
				} else if (cashflow.getCashflowType().equals(ECashflowType.FIN)) {
				    secondPayment = cashflow.getTiInstallmentAmount();
			    }
			}
		}

		// initialize list of departments in some way
		Map<String, String> beans = new HashMap<String, String>( );
		beans.put("dealer", getDefaultString(dealer.getNameEn()));
		beans.put("customerName", getDefaultString(applicant.getIndividual().getLastName() + " " + applicant.getIndividual().getFirstName()));
		beans.put("customerNameEn", getDefaultString(applicant.getIndividual().getLastNameEn() + " " + applicant.getIndividual().getFirstNameEn()));
		
		ECurrency currency = ECurrency.getDefault();
		String descEn = "USD";
		if (currency != null && StringUtils.isNotEmpty(currency.getDescEn())) {
			descEn = currency.getDescEn();
		} 
		
		beans.put("advancePayment", AmountUtils.format(advancePayment) + " " + descEn);
		
		String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
		String templateFileName = templatePath + "/Official_Receipt.xlsx";
		beans.put("isKHM", "");
		beans.put("isUSD", "X");
		beans.put("orNo", payment.getReference());
		if (installmentOther != 0d) {
			beans.put("otherAmount", AmountUtils.format(installmentOther) + " " + descEn);
		}
		beans.put("totalAmount", AmountUtils.format(advancePayment + installmentOther) + " " + descEn);
		beans.put("secondPaymentAmount", AmountUtils.format(Math.abs(secondPayment)) + " " + descEn);
		
		beans.put("totalAmountWord", NumberToWords.getDoubleToword(advancePayment + installmentOther));
		
		String outputPath = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");

		String prefixOutputName = "Official_Receipt";
		String sufixOutputName = "xlsx";
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String xlsFileName = outputPath + "/" + prefixOutputName + uuid + "." + sufixOutputName;
		XLSTransformer transformer = new XLSTransformer();	
		transformer.transformXLS(templateFileName, beans, xlsFileName);
		
		return prefixOutputName + uuid + "." + sufixOutputName;
	}
	
	/**
	 * @param value
	 * @return
	 */
	private String getDefaultString(String value) {
		return StringUtils.defaultString(value);
	}
	
}
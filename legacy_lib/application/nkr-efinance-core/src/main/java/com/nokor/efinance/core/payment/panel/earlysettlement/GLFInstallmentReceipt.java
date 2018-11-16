package com.nokor.efinance.core.payment.panel.earlysettlement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * Installment receipt for core
 * @author sok.vina
 */
public class GLFInstallmentReceipt implements Report {

	protected EntityService entityService = (EntityService) SecApplicationContextHolder
			.getContext().getBean("entityService");

	/**
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {

		Long paymnId = (Long) reportParameter.getParameters().get("paymnId");
		Payment payment = entityService.getById(Payment.class, paymnId);
		List<Cashflow> cashflows = payment.getCashflows();
		
		Applicant applicant = payment.getApplicant();
		double installment = 0d ;
		double insuranceFee = 0d;
		double servicingFee = 0d;
		double installmentOther = 0d;
		double penaltyAmount = 0d;
		Integer numInstallment = null;
		
		if (payment.getCashflows() != null) {
			for (Cashflow cashflow : cashflows) {
				if(cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					installment += cashflow.getTiInstallmentAmount();
				} else if (cashflow.getCashflowType().equals(ECashflowType.PEN)) {
					penaltyAmount = cashflow.getTiInstallmentAmount();
				} else if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
					//isInstallmentReceiptNew = true;
					if ("INSFEE".equals(cashflow.getService().getCode())) {
						insuranceFee += cashflow.getTiInstallmentAmount();
					} else if ("SERFEE".equals(cashflow.getService().getCode())) {
						servicingFee += cashflow.getTiInstallmentAmount();
					} else {
						installmentOther += cashflow.getTiInstallmentAmount();
					}
				} else if (!cashflow.getCashflowType().equals(ECashflowType.FIN)) {
					installmentOther += cashflow.getTiInstallmentAmount();
				}
				if (cashflow.getNumInstallment() != null && cashflow.getNumInstallment().intValue() > 0) {
					numInstallment = cashflow.getNumInstallment();
				}
			}
		}
	
		String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
		String templateFileName = templatePath + "/Installment_receipt.xlsx";
		
		String descEn = "USD";
		
//		ECurrency currency = DataReference.getInstance().getCurrencyByDefault();
//		if (currency != null && StringUtils.isNotEmpty(currency.getDescEn())) {
//			descEn = currency.getDescEn();
//		} 
		
		// initialize list of departments in some way
		Map<String, String> beans = new HashMap<String, String>();
		beans.put("dealer", getDefaultString(payment.getDealer().getNameEn()));
		beans.put("customerName", getDefaultString(applicant.getIndividual().getLastName() + " " + applicant.getIndividual().getFirstName()));
		beans.put("numberInstallment", (numInstallment == null ? "" : numInstallment.toString()));
		beans.put("installmentAmount", AmountUtils.format(installment) + " " + descEn);
		beans.put("isKHM", "");
		beans.put("isUSD", "X");
		if (insuranceFee != 0d) {
			beans.put("insuranceFee", AmountUtils.format(insuranceFee) + " " + descEn);
		}
		if (servicingFee != 0d) {
			beans.put("serviceFee", AmountUtils.format(servicingFee) + " " + descEn);
		}
		
		beans.put("orNo", payment.getReference());
		beans.put("contractReference", cashflows.get(0).getContract().getReference());
		
		if (penaltyAmount != 0d) {
			beans.put("penaltyFee", AmountUtils.format(penaltyAmount) + " " + descEn);
		}
		if (installmentOther != 0d) {
			beans.put("otherFee", AmountUtils.format(installmentOther) + " " + descEn);	
		}
		beans.put("totalAmount",
				AmountUtils.format(installment + insuranceFee + servicingFee
						+ penaltyAmount + installmentOther) + " " + descEn);
		

		String outputPath = AppConfig.getInstance().getConfiguration()
				.getString("specific.tmpdir");

		String prefixOutputName = "Installment_receipt";
		String sufixOutputName = "xlsx";
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String xlsFileName = outputPath + "/" + prefixOutputName + uuid + "."
				+ sufixOutputName;
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

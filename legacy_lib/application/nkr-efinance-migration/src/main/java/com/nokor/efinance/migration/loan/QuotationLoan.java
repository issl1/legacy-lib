package com.nokor.efinance.migration.loan;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nokor.efinance.migration.model.loan.Loan;

/**
 * 
 * @author buntha.chea
 *
 */
public class QuotationLoan {

	private Logger LOG = LoggerFactory.getLogger(QuotationLoan.class);
	
	private final static String DATE_FORMAT = "dd.MM.yyyy hh:mm";
	private List<Loan> loans = new ArrayList<>();
	private List<String> errors = new ArrayList<>();
	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		QuotationLoan loan = new QuotationLoan();
		//File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/312-1-201507211636.csv");
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v2/310-1-201507211636.csv");
		loan.validateData(f);
	}
	
	/**
	 * @param url
	 * @param fileName
	 */
	public List<Loan> validateData(File file) {
		
		String cvsSplitBy = "\\|";
		String s = "310|System ID|Product vat|Marketing Campaign code|Default finance amount|Finance amount|Down payment|Down payment %|Eff Rate|Flat Rate|Terms|Application date|Approval date|Activation date|First due date|Last due date|Installment|Total AR|Total UE|Total VAT|Prepaid installment at shop|Number prepaid term|Service fee|Insurance fee";
		int length = s.split(cvsSplitBy).length;
		
		try {
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] loanData = lines.get(i).split(cvsSplitBy, -1);
				if ("310".equals(loanData[0]) && !"System ID".equals(loanData[1])) {
					if (length == loanData.length) {
						//errors = ValidateDataInCSV.validateLoan("LOAN", loanData, i+1);
						Loan loan = new Loan();
						loan.setMigrationId(loanData[1]);
						loan.setProductVatCode(loanData[2]);
						loan.setMarketingCompaigneCode(loanData[3]);
						loan.setDefaultFinancialAmount(checkDouble(loanData[4]));
						loan.setFinancialAmount(checkDouble(loanData[5]));
						loan.setDownPayment(checkDouble(loanData[6]));
						loan.setDownPaymentPercentage(checkDouble(loanData[7]));
						loan.setEffRate(checkDouble(loanData[8]));
						loan.setFlatRate(checkDouble(loanData[9]));
						loan.setTerms(StringUtils.isEmpty(loanData[10]) ? 0 : Integer.valueOf(loanData[10]));
						loan.setApplicantionDate(checkDate(loanData[11]) == null ? checkDate("10.07.2006 00:00") : checkDate(loanData[11]));
						loan.setApprovalDate(checkDate(loanData[12]) == null ? checkDate("10.07.2007 00:00") : checkDate(loanData[12]));
						loan.setActivationDate(checkDate(loanData[13]));
						
						if (StringUtils.isEmpty(loanData[14])) {
							LOG.warn("Line = " + (i + 1) + ", First Due date is empty");	
						} else {
							loan.setFirstDueDate(checkDate(loanData[14]));
						}
						loan.setLastDueDate(checkDate(loanData[15]));
						loan.setInstallmentAmount(checkDouble(loanData[16]));
						loan.setTotalAR(checkDouble(loanData[17]));
						loan.setTotalUE(checkDouble(loanData[18]));
						loan.setTotalVAT(checkDouble(loanData[19]));
						loan.setPrepaidInstallment(checkDouble(loanData[20]));
						loan.setNumberPrepaidTerm(StringUtils.isEmpty(loanData[21]) ? 0 : Integer.valueOf(loanData[21]));
						
						if (!StringUtils.isEmpty(loanData[22])) {
							double servicingFee = Double.valueOf(loanData[22]);
							if (servicingFee > 0) {
								loan.setServiceFee(servicingFee);
							}
						}
						if (!StringUtils.isEmpty(loanData[23])) {
							double insuranceFee = Double.valueOf(loanData[23]);
							if (insuranceFee > 0) {
								loan.setInsuranceFee(insuranceFee);
							}
						}
						this.validate(loan, i + 1);
						loans.add(loan);
					} else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  loanData.length + ", expected length =" + length);
						errors.add("");
					}
				} 
			}
			if (errors.isEmpty()) {
				LOG.info("Validate Success");
				return loans;
			} else {
				for (String error : errors) {
					LOG.error(error);
				}
			}
		} catch (Exception e) {
			LOG.error("Exception => ", e);
		}
		return null;
	}
	/**
	 * 
	 * @param loan
	 * @param line
	 */
	private void validate(Loan loan, int line) {
		String message = "";
		if (loan.getApplicantionDate() == null) {
			message += "APPLICATION_DATE_MANDATORY|";
		}
		if (loan.getApprovalDate() == null) {
			message += "APPROVAL_DATE_MANDATORY|";
		}
		if (loan.getFirstDueDate() == null) {
			message += "FIRST_DUE_DATE_MANDATORY|";
		}
		if (loan.getFinancialAmount() == null) {
			message += "FINANCE_AMOUNT_MANDATORY|";
		}
		if (loan.getDownPayment() == null) {
			message += "DOWN_PAYMENT_AMOUNT_MANDATORY|";
		}
		if (loan.getInstallmentAmount() == null) {
			message += "INSTALLMENT_AMOUNT_MANDATORY|";
		}
		if (loan.getTerms() == null) {
			message += "TERM_MANDATORY|";
		}
		if (loan.getFlatRate() == null) {
			message += "FLAT_RATE_MANDATORY|";
		}
		if (!StringUtils.isEmpty(message)) {
			errors.add("LINE : " + line + " " + "LOAN" +" "+ message);
		}
	}
	/**
	 * 
	 * @param loanData
	 * @return
	 */
	private Double checkDouble(String loanData) {
		return StringUtils.isEmpty(loanData) ? null : Double.valueOf(loanData);
	}
	
	private Date checkDate(String loanData) {
		return StringUtils.isEmpty(loanData) ? null : DateUtils.getDate(loanData, DATE_FORMAT);
	}
}

package com.nokor.efinance.batch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.batch.report.BeanToExcel;
import com.nokor.efinance.batch.report.BeanToExcel.FormatType;
import com.nokor.efinance.batch.report.ReportColumn;
import com.nokor.efinance.core.address.model.Address;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractHistory;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.shared.service.ServiceEntityField;
import com.nokor.efinance.tools.sync.ConnectionManager;
import com.nokor.efinance.workflow.ContractHistoReason;
import com.nokor.efinance.workflow.ContractWkfStatus;



public class LeaseReport extends StepExecutionListenerSupport
		implements Tasklet, ContractEntityField {
	
	@Autowired
	private ContractService contractService;
	
	private String reference;
	private String dealerCode;
	private String familyName;
	private String firstName;
	private String email;
	
	public LeaseReport() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public void beforeStep(StepExecution stepExecution) {
		Map<String, String> pMap = BatchLauncher.extractParameters(stepExecution);
		if (!pMap.isEmpty()) {
			reference = (String) pMap.get("reference");
			dealerCode = (String) pMap.get("dealerCode");
			familyName = (String) pMap.get("familyName");
			firstName = (String) pMap.get("firstName");
			email = (String) pMap.get("email");
		} else {
			Properties prop = new Properties();
	    	try {
	    		prop.load(new FileInputStream(BatchLauncher.DEFAULT_PROPERTIES));
	    		reference = prop.getProperty("reference");
	    		dealerCode = prop.getProperty("dealerCode");
	    		familyName = prop.getProperty("familyName");
	    		firstName = prop.getProperty("firstName");
	    		email = prop.getProperty("email");
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	        }
		}
	}

	public RepeatStatus execute(StepContribution stepcontribution,
			ChunkContext chunkcontext) throws Exception {
        try {
            // Create the report object
            BeanToExcel bReport = new BeanToExcel();
 
            List<LeaseInfo> lease = getLeaseInfos();
            ReportColumn[] reportColumns = getMapColumn();
            
            bReport.addSheet(lease, reportColumns, "sheet1");
 
            // Set Column 1 text to Bold and background to Green
            //reportColumns[1].setColor(HSSFColor.GREEN.index);
 
            // Add a 2nd sheet with the same data.
            // bReport.addSheet(lease, reportColumns, "sheet2");
 

            Date fdt = new Date();
    		SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
    		String fileName = "output/LeaseReport" + "_" + ft.format(fdt) + ".xls";
            OutputStream output = new FileOutputStream(fileName);
            bReport.write(output);
            output.close();
            if (email != null && !email.isEmpty()) {
            	FileSystemResource attachment = new FileSystemResource(fileName);
                ConnectionManager.getInstance().sendMail(I18N.message("mail.subject.batch.lease.info"), I18N.message("mail.message.batch.lease.info"), email, attachment);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Done with parameter's value :");
		System.out.println("reference = " + reference);
		System.out.println("dealerCode = " + dealerCode);
		System.out.println("familyName = " + familyName);
		System.out.println("firstName = " + firstName);
		System.out.println("email = " + email);
		return RepeatStatus.FINISHED;
	}
	
	/**
	 * @return
	 */
	private ReportColumn[] getMapColumn() {
		ReportColumn[] reportColumns = new ReportColumn[] {
				new ReportColumn("poNo", I18N.message("production.officer"), FormatType.TEXT),
				new ReportColumn("poNumber", I18N.message("po.number"), FormatType.TEXT),
         		new ReportColumn("lidNo", I18N.message("contract.reference"), FormatType.TEXT),
         		new ReportColumn("fullName", I18N.message("customer"), FormatType.TEXT),
         		new ReportColumn("businessType", I18N.message("business.type"), FormatType.TEXT),
         		new ReportColumn("tel", I18N.message("telephone"), FormatType.TEXT),
         		
         		new ReportColumn("houseNo", I18N.message("house.no"), FormatType.TEXT),
         		new ReportColumn("street", I18N.message("street"), FormatType.TEXT),
         		new ReportColumn("village", I18N.message("village"), FormatType.TEXT),
         		new ReportColumn("villageKh", I18N.message("village.kh"), FormatType.TEXT),
         		new ReportColumn("commune", I18N.message("commune"), FormatType.TEXT),
         		new ReportColumn("communeKh", I18N.message("commune.kh"), FormatType.TEXT),
         		new ReportColumn("district", I18N.message("district"), FormatType.TEXT),
         		new ReportColumn("districtKh", I18N.message("district.kh"), FormatType.TEXT),
         		new ReportColumn("province", I18N.message("province"), FormatType.TEXT),
         		new ReportColumn("provinceKh", I18N.message("province.kh"), FormatType.TEXT),
         		new ReportColumn("sex", I18N.message("gender"), FormatType.TEXT),
         		new ReportColumn("dealerName", I18N.message("dealer"), FormatType.TEXT),
         		new ReportColumn("assetModel", I18N.message("asset.model"), FormatType.TEXT),
         		new ReportColumn("coName", I18N.message("credit.officer"), FormatType.TEXT),
         		new ReportColumn("dateOfContract", I18N.message("start.date"), FormatType.DATE),
         		new ReportColumn("assetPrice", I18N.message("asset.price"), FormatType.AMOUNT),
         		new ReportColumn("term", I18N.message("term"), FormatType.NUMBER),
         		new ReportColumn("rate", I18N.message("rate"), FormatType.PERCENTAGE),
         		new ReportColumn("firstInstallmentDate", I18N.message("first.installment.date"), FormatType.DATE),
         		new ReportColumn("irrYear", I18N.message("irr.year"), FormatType.AMOUNT),
         		new ReportColumn("irrMonth", I18N.message("irr.month"), FormatType.AMOUNT),
         		new ReportColumn("advPaymentPer", I18N.message("advance.payment.percentage"), FormatType.AMOUNT),
         		new ReportColumn("installmentAmount", I18N.message("installment.amount"), FormatType.AMOUNT),
         		new ReportColumn("insurance", I18N.message("insurance.fee"), FormatType.AMOUNT),
         		new ReportColumn("registration", I18N.message("registration.fee"), FormatType.AMOUNT),
         		new ReportColumn("serviceFee", I18N.message("servicing.fee"), FormatType.AMOUNT),
         		new ReportColumn("advPayment", I18N.message("advance.payment"), FormatType.AMOUNT),
         		new ReportColumn("secondPay", I18N.message("second.payment"), FormatType.AMOUNT),
         		new ReportColumn("downPay", I18N.message("down.payment"), FormatType.AMOUNT),
         		new ReportColumn("loanAmount", I18N.message("loan.amount"), FormatType.AMOUNT),
         		new ReportColumn("totalInt", I18N.message("total.interest"), FormatType.AMOUNT),
         		new ReportColumn("intBalance", I18N.message("interest.balance"), FormatType.AMOUNT),
         		new ReportColumn("realIntBalance", I18N.message("real.interest.balance"), FormatType.AMOUNT),
         		new ReportColumn("prinBalance", I18N.message("principal.balance"), FormatType.AMOUNT),
         		new ReportColumn("realPrinBalance", I18N.message("real.principal.balance"), FormatType.AMOUNT),
         		new ReportColumn("totalReceive", I18N.message("total.receivable"), FormatType.AMOUNT),
				new ReportColumn("realTotalReceive", I18N.message("real.total.receivable"), FormatType.AMOUNT)
         		};
		 return reportColumns;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		restrictions.addCriterion(Restrictions.in(CONTRACT_STATUS, new EWkfStatus[] {ContractWkfStatus.FIN, ContractWkfStatus.EAR, ContractWkfStatus.CLO, ContractWkfStatus.LOS}));
		restrictions.addAssociation("contractApplicants", "contractapp", JoinType.INNER_JOIN);
		restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		
		restrictions.addCriterion("contractapp.applicantType", EApplicantType.C);
	
		// reference = "GLF-KDL-01-00006191";
		
		if (reference != null && !reference.isEmpty()) { 
			restrictions.addCriterion(Restrictions.like(REFERENCE, reference, MatchMode.ANYWHERE));
		}
		
		if (dealerCode != null && !dealerCode.isEmpty()) {
			restrictions.addCriterion(Restrictions.eq("dea." + INTERNAL_CODE, dealerCode));
		}
		
		if (familyName != null && !familyName.isEmpty()) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, familyName, MatchMode.ANYWHERE));
		}
		
		if (firstName != null && !firstName.isEmpty()) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, firstName, MatchMode.ANYWHERE));
		}
		
		restrictions.addOrder(Order.desc(START_DATE));
		return restrictions;
	}
	
	/**
	 * @return
	 */
	private List<LeaseInfo> getLeaseInfos() {
		
		List<Contract> contracts = contractService.getListContract(getRestrictions());
		
		List<LeaseInfo> leasesReports = new ArrayList<LeaseInfo>();
		
		Date calculDate = DateUtils.getDate("30092014 23:59:59", "ddMMyyyy hh:mm:ss");
		
		int i = 0;
		int nbContract = contracts.size();
		
		for (Contract contract : contracts) {
			i++;
			
			System.out.println("=== " + i + "/" + nbContract);
			
			LeaseInfo lease = new LeaseInfo();
			Applicant applicant = contract.getApplicant();
			Asset asset = contract.getAsset();
			Address address = applicant.getMainAddress();
			Dealer dealer = contract.getDealer();
			
			List<Cashflow> cashflows = contractService.getCashflowsNoCancel(contract.getId());
			
			Employment emp = applicant.getCurrentEmployment();
			
			lease.setId(contract.getId());
			lease.setPoNo(contract.getProductionOfficer() == null ? "" : contract.getProductionOfficer().getDesc());
			lease.setLidNo(contract.getReference());
			lease.setFullName(applicant.getLastNameEn() + " " + applicant.getFirstNameEn());
			
			if (emp != null) {
				lease.setBusinessType(emp.getEmploymentStatus().getDescEn());
			}
			lease.setTel(applicant.getMobile());
			
			lease.setHouseNo(getDefaultString(address.getHouseNo()));
			lease.setStreet(getDefaultString(address.getStreet()));
			lease.setVillage(address.getVillage().getDescEn());
			lease.setVillageKh(getDefaultString(address.getVillage().getDesc()));
			lease.setCommune(address.getCommune().getDescEn());
			lease.setCommuneKh(getDefaultString(address.getCommune().getDesc()));
			lease.setDistrict(address.getDistrict().getDescEn());
			lease.setDistrictKh(getDefaultString(address.getDistrict().getDesc()));
			lease.setProvince(address.getProvince().getDescEn());
			lease.setProvinceKh(getDefaultString(address.getProvince().getDesc()));
			
			lease.setVillage(address.getVillage().getDescEn());
			lease.setCommune(address.getCommune().getDescEn());
			lease.setDistrict(address.getDistrict().getDescEn());
			lease.setProvince(address.getProvince().getDescEn());
			lease.setSex(applicant.getGender().getDesc());
			lease.setDealerName(dealer.getNameEn());
			lease.setAssetModel(asset.getDescEn());
			lease.setCoName(contract.getCreditOfficer() == null ? "" : contract.getCreditOfficer().getDesc());
			lease.setDateOfContract(contract.getSigatureDate());
			lease.setAssetPrice(asset.getTiAssetPrice() == null ? 0.0d : asset.getTiAssetPrice());
			lease.setTerm(contract.getTerm());
			lease.setFirstInstallmentDate(contract.getFirstInstallmentDate());
			lease.setRate(contract.getInterestRate() == null ? 0.0d : contract.getInterestRate());
			lease.setIrrMonth(contract.getIrrRate() == null ? 0.0d : contract.getIrrRate() * 100);
			if (lease.getIrrMonth() != null) {
				lease.setIrrYear(lease.getIrrMonth() * 12);
			}
			lease.setLoanAmount(contract.getTiFinancedUsd() == null ? 0.0d : contract.getTiFinancedUsd());
			lease.setDownPay(contract.getTiAdvancePaymentUsd() == null ? 0.0d : contract.getTiAdvancePaymentUsd());
			lease.setAdvPaymentPer(contract.getAdvancePaymentPercentage() == null ? 0.0d : contract.getAdvancePaymentPercentage());
			
			lease.setInsurance(getServiceFee(ServiceEntityField.INSFEE, cashflows));
			lease.setRegistration(getServiceFee(ServiceEntityField.REGFEE, cashflows));
			lease.setServiceFee(getServiceFee(ServiceEntityField.SERFEE, cashflows));
			
			lease.setAdvPayment(MyMathUtils.roundAmountTo(lease.getDownPay() 
					+ getFirstInstallmentServiceFee(ServiceEntityField.INSFEE, cashflows) 
					+ getFirstInstallmentServiceFee(ServiceEntityField.REGFEE, cashflows)
					+ getFirstInstallmentServiceFee(ServiceEntityField.SERFEE, cashflows)));
			lease.setSecondPay(MyMathUtils.roundAmountTo(lease.getAssetPrice() - lease.getAdvPayment()));
			
			lease.setTotalInt(MyMathUtils.roundAmountTo(getTotalInterest(cashflows).getTiAmountUsd()));
			// TODO PYI
//			if (contract.getWkfStatus().equals(WkfContractStatus.EAR)) {
//				Date earlySettlementDate = getEarlySettlementDate(contract.getHistories());
//				if (earlySettlementDate.compareTo(calculDate) > 0) {
//					lease.setTotalInt(contract.getTiAdjustmentInterestUsd() + MyMathUtils.roundAmountTo(getTotalInterest(cashflows).getTiAmountUsd()));
//					lease.setPrinBalance(getTheoricalOutstanding(calculDate, cashflows).getTiAmountUsd());
//					lease.setRealPrinBalance(getRealOutstanding(calculDate, cashflows).getTiAmountUsd());
//					lease.setIntBalance(contract.getTiAdjustmentInterestUsd() + MyMathUtils.roundAmountTo(getInterestBalance(calculDate, cashflows).getTiAmountUsd()));
//					lease.setRealIntBalance(contract.getTiAdjustmentInterestUsd() + MyMathUtils.roundAmountTo(getRealInterestBalance(calculDate, cashflows).getTiAmountUsd()));
//					lease.setTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getIntBalance()) + MyNumberUtils.getDouble(lease.getPrinBalance())));
//					lease.setRealTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getRealIntBalance()) + MyNumberUtils.getDouble(lease.getRealPrinBalance())));
//				} else {
//					lease.setPrinBalance(0d);
//					lease.setRealPrinBalance(0d);
//					lease.setIntBalance(0d);
//					lease.setRealIntBalance(0d);
//					lease.setTotalReceive(0d);
//					lease.setRealTotalReceive(0d);
//				}
//			} else if (contract.getWkfStatus().equals(WkfContractStatus.LOS)) {
//				Date lossDate = getLossDate(contract.getHistories());
//				if (lossDate.compareTo(calculDate) > 0) {
//					lease.setPrinBalance(getTheoricalOutstanding(calculDate, cashflows).getTiAmountUsd());
//					lease.setRealPrinBalance(contract.getTiAdjustmentPrincipalUsd() + getRealOutstanding(calculDate, cashflows).getTiAmountUsd());
//					lease.setIntBalance(MyMathUtils.roundAmountTo(getInterestBalance(calculDate, cashflows).getTiAmountUsd()));
//					lease.setRealIntBalance(contract.getTiAdjustmentInterestUsd() + MyMathUtils.roundAmountTo(getRealInterestBalance(calculDate, cashflows).getTiAmountUsd()));
//					lease.setTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getIntBalance()) + MyNumberUtils.getDouble(lease.getPrinBalance())));
//					lease.setRealTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getRealIntBalance()) + MyNumberUtils.getDouble(lease.getRealPrinBalance())));
//				} else {
//					lease.setPrinBalance(0d);
//					lease.setRealPrinBalance(0d);
//					lease.setIntBalance(0d);
//					lease.setRealIntBalance(0d);
//					lease.setTotalReceive(0d);
//					lease.setRealTotalReceive(0d);
//				}
//			} else if (contract.getWkfStatus().equals(WkfContractStatus.CLO)) {
//				lease.setPrinBalance(0d);
//				lease.setRealPrinBalance(0d);
//				lease.setIntBalance(0d);
//				lease.setRealIntBalance(0d);
//				lease.setTotalReceive(0d);
//				lease.setRealTotalReceive(0d);
//			} else {
//				lease.setPrinBalance(getTheoricalOutstanding(calculDate, cashflows).getTiAmountUsd());
//				lease.setRealPrinBalance(getRealOutstanding(calculDate, cashflows).getTiAmountUsd());
//				lease.setIntBalance(MyMathUtils.roundAmountTo(getInterestBalance(calculDate, cashflows).getTiAmountUsd()));
//				lease.setRealIntBalance(MyMathUtils.roundAmountTo(getRealInterestBalance(calculDate, cashflows).getTiAmountUsd()));
//				lease.setTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getIntBalance()) + MyNumberUtils.getDouble(lease.getPrinBalance())));
//				lease.setRealTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getRealIntBalance()) + MyNumberUtils.getDouble(lease.getRealPrinBalance())));
//			}
			lease.setInstallmentAmount(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(contract.getTiInstallmentUsd())));
			lease.setPoNumber(cashflows.get(0).getPayment().getCode().replaceAll("-OR", ""));
			leasesReports.add(lease);
		}
		return leasesReports;
	}
	
	private Amount getTotalInterest(List<Cashflow> cashflows) {
		Amount interest = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()) {
				interest.plusTiAmountUsd(cashflow.getTiInstallmentUsd());
				interest.plusTeAmountUsd(cashflow.getTeInstallmentUsd());
				interest.plusVatAmountUsd(cashflow.getVatInstallmentUsd());
			}
		}
		return interest;
	}
	
	private Amount getTheoricalOutstanding(Date calculDate, List<Cashflow> cashflows) {
		Amount outstanding = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.CAP)
					&& !cashflow.isCancel()
					&& (cashflow.getInstallmentDate().compareTo(calculDate) >= 0 || !cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				outstanding.plusTiAmountUsd(cashflow.getTiInstallmentUsd());
				outstanding.plusTeAmountUsd(cashflow.getTeInstallmentUsd());
				outstanding.plusVatAmountUsd(cashflow.getVatInstallmentUsd());
			}
		}		
		return outstanding;
	}
	
	private Amount getRealOutstanding(Date calculDate, List<Cashflow> cashflows) {
		Amount outstanding = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.CAP)
					&& !cashflow.isCancel()
					&& (!cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				outstanding.plusTiAmountUsd(cashflow.getTiInstallmentUsd());
				outstanding.plusTeAmountUsd(cashflow.getTeInstallmentUsd());
				outstanding.plusVatAmountUsd(cashflow.getVatInstallmentUsd());
			}
		}
		
		return outstanding;
	}
	
	private Amount getInterestBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount interestBalance = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& (cashflow.getInstallmentDate().compareTo(calculDate) >= 0 || !cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				interestBalance.plusTiAmountUsd(cashflow.getTiInstallmentUsd());
				interestBalance.plusTeAmountUsd(cashflow.getTeInstallmentUsd());
				interestBalance.plusVatAmountUsd(cashflow.getVatInstallmentUsd());
			}
		}		
		return interestBalance;
	}
	
	private Amount getRealInterestBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount interestBalance = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& (!cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				interestBalance.plusTiAmountUsd(cashflow.getTiInstallmentUsd());
				interestBalance.plusTeAmountUsd(cashflow.getTeInstallmentUsd());
				interestBalance.plusVatAmountUsd(cashflow.getVatInstallmentUsd());
			}
		}
		
		return interestBalance;
	}
	
	/**
	 * @param histories
	 * @return
	 */
	private Date getEarlySettlementDate(List<ContractHistory> histories) {
		Date earlySettlementDate = null;
		// TODO PYI
//		for (ContractHistory history : histories) {
//			if (history.gethisReason().equals(EhisReason.CONTRACT_0003)) {
//				earlySettlementDate = history.getHistoryDate();
//			}
//		}
		return earlySettlementDate;
	}
	
	/**
	 * @param histories
	 * @return
	 */
	private Date getLossDate(List<ContractHistory> histories) {
		Date lossDate = null;
		// TODO PYI
//		for (ContractHistory history : histories) {
//			if (history.gethisReason().equals(EhisReason.CONTRACT_LOSS)) {
//				lossDate = history.getHistoryDate();
//			}
//		}
		return lossDate;
	}
	
	/**
	 * @param serviceCode
	 * @param services
	 * @return
	 */
	private Double getFirstInstallmentServiceFee(String code, List<Cashflow> cashflows) {
		double tiServiceAmount = 0d;
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.FEE) && code.equals(cashflow.getService().getCode()) && cashflow.getNumInstallment() == 0) {
				tiServiceAmount += cashflow.getTiInstallmentUsd();
			}
		}
		return tiServiceAmount;
	}
	
	/**
	 * @param code
	 * @param cashflows
	 * @return
	 */
	private Double getServiceFee(String code, List<Cashflow> cashflows) {
		double tiServiceAmount = 0d;
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.FEE) && code.equals(cashflow.getService().getCode())) {
				tiServiceAmount += cashflow.getTiInstallmentUsd();
			}
		}
		return tiServiceAmount;
	}
	/**
	 * @param value
	 * @return
	 */
	public String getDefaultString(String value) {
		return value == null ? "" : value;
	}
}
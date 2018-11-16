package com.nokor.efinance.glf.report.xls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author tek.thida
 *
 */
public class GLFLeasingContractKH implements Report {
	
    protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
    
    /**
	 * 
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		
		Long quotaId = (Long) reportParameter.getParameters().get("quotaId");
		
		Quotation quotation = quotationService.getById(Quotation.class, quotaId);
		Applicant applicant = quotation.getApplicant();
		Individual individual = applicant.getIndividual();
		Employment applicantEmployment = individual.getCurrentEmployment();
		Address applicantAddress = individual.getMainAddress();
		Address employmentAddress = applicantEmployment.getAddress();
		Asset asset = quotation.getAsset();
			    
	    // initialize list of departments in some way
	    Map<String, String> beans = new HashMap<String, String>();
	    beans.put("shopName", quotation.getDealer().getName());
	    beans.put("lesseeName", individual.getLastName() + " " + individual.getFirstName());
	    
	    beans.put("lesseeSex", getGenderDesc(individual.getGender().getCode()));
	    beans.put("lesseebd", DateUtils.getDateLabel(individual.getBirthDate(), DateUtils.FORMAT_DDMMYYYY_SLASH));
	    beans.put("lesseeAge", "" + DateUtils.getAge(individual.getBirthDate()));
	    
	    QuotationDocument idCardDocument = getDocument("N", quotation.getQuotationDocuments());
	    beans.put("isLesseeIdCard", idCardDocument != null ? "v" : "");
	    beans.put("lesseeIdCard", idCardDocument != null ? getDefaultString(idCardDocument.getReference()) : "");
	    
	    QuotationDocument reseidentBookDocument = getDocument("R", quotation.getQuotationDocuments());
	    beans.put("isLesseeResidentBookNum", reseidentBookDocument != null ? "v" : "");
	    beans.put("lesseeResidentBookNum", reseidentBookDocument != null ? getDefaultString(reseidentBookDocument.getReference()) : "");
	    
	    QuotationDocument villageCertifiedDocument = getDocument("G", quotation.getQuotationDocuments());
	    beans.put("isLesseeCertifyLetterNum", villageCertifiedDocument != null ? "v" : "");
	    beans.put("lesseeCertifyLetterNum", villageCertifiedDocument != null ? villageCertifiedDocument.getReference() : "");
	    
	    QuotationDocument familyBookDocument = getDocument("F", quotation.getQuotationDocuments());
	    beans.put("isLesseeFamilyBookNum", familyBookDocument != null ? "v" : "");
	    beans.put("lesseeFamilyBookNum", familyBookDocument != null ? getDefaultString(familyBookDocument.getReference()) : "");
	    
	    QuotationDocument birthCertificateDocument = getDocument("B", quotation.getQuotationDocuments());
	    beans.put("isLesseeBirthCertificateNum", birthCertificateDocument != null ? "v" : "");
	    beans.put("lesseeBirthCertificateNum", birthCertificateDocument != null ? getDefaultString(birthCertificateDocument.getReference()) : "");
	    
	    double totalNetIncome = 0d;
		double totalRevenus = 0d;
		double totalAllowance = 0d;
		double totalBusinessExpenses = 0d;
		List<Employment> employments = individual.getEmployments();
		for (Employment employment : employments) {
			totalRevenus += MyNumberUtils.getDouble(employment.getRevenue()) ;
			totalAllowance += MyNumberUtils.getDouble(employment.getAllowance()) ;
			totalBusinessExpenses += MyNumberUtils.getDouble(employment.getBusinessExpense());
		}
		totalNetIncome = totalRevenus + totalAllowance - totalBusinessExpenses;
	    
		// TODO YLY 
	    // beans.put("lesseePhoneNum1", getDefaultString(applicant.getMobilePhone()));
	    // beans.put("lesseePhoneNum2", getDefaultString(applicant.getMobilePhone2()));
	    beans.put("lesseeIncome", "" + AmountUtils.format(totalNetIncome));
	    beans.put("isLesseeSingle", individual.getMaritalStatus().equals(EMaritalStatus.SINGLE) ? "v" : "");
	    beans.put("isLesseeSpouse", individual.getMaritalStatus().equals(EMaritalStatus.MARRIED) ? "v" : "");
	    beans.put("isLesseeDivorce", (individual.getMaritalStatus().equals(EMaritalStatus.DIVORCED) || individual.getMaritalStatus().equals(EMaritalStatus.SEPARATED) || individual.getMaritalStatus().equals(EMaritalStatus.WIDOW))? "v" : "");
	    beans.put("lesseeNumChildren",  "" + MyNumberUtils.getInteger(individual.getNumberOfChildren()));
	    // TODO YLY
	    // beans.put("lesseeSpouseName", getDefaultString(applicant.getLastNameEnSpouse()) + " " + getDefaultString(applicant.getFirstNameEnSpouse()));
	    // beans.put("lesseeSpousePhoneNum", getDefaultString(applicant.getMobilePhoneSpouse()));
	    
	    Date contractStartDate = quotation.getContractStartDate();		
		Date firstPaymentDate = quotation.getFirstDueDate();
		
	    //Detail 1
	    beans.put("agreementNum", quotation.getReference());
	    beans.put("agreementDate", (contractStartDate == null ? "" : DateUtils.getDateLabel(contractStartDate, DateUtils.FORMAT_DDMMYYYY_SLASH)));
	    //Detail 2
	    beans.put("lesseeHouseNum", applicantAddress.getHouseNo());
	    beans.put("lesseeStreetNum", applicantAddress.getStreet());
	    beans.put("lesseeVillage", applicantAddress.getVillage().getDesc());
	    beans.put("lesseeCommune", applicantAddress.getCommune().getDesc());
	    beans.put("lesseeDistrict", applicantAddress.getDistrict().getDesc());
	    beans.put("lesseeProvince", applicantAddress.getProvince().getDesc());
	    beans.put("lesseeLengthStay", "" + (MyNumberUtils.getInteger(applicantAddress.getTimeAtAddressInYear())));
	    beans.put("lesseeLengthStayMonth", "" + (MyNumberUtils.getInteger(applicantAddress.getTimeAtAddressInMonth())));
	    beans.put("lesseeAvalableDateTime", getDefaultString(individual.getConvenientVisitTime()));
	    beans.put("isLesseeOwnHouse", "01".equals(applicantAddress.getProperty().getCode()) ? "v" : "");
	    beans.put("isLesseeParentsHouse", "02".equals(applicantAddress.getProperty().getCode()) ? "v" : "");
	    beans.put("isLesseeRelativeHouse", "04".equals(applicantAddress.getProperty().getCode()) ? "v" : "");
	    beans.put("isLesseeRentHouse", "03".equals(applicantAddress.getProperty().getCode()) ? "v" : "");
	    beans.put("isLesseeOtherHouse", "05".equals(applicantAddress.getProperty().getCode()) ? "v" : "");
	    
	    //Detail 3
	    beans.put("vehicleModel", asset.getDesc());
	    beans.put("vehicleColor", getDefaultString(asset.getColor().getDesc(), asset.getColor().getDescEn()));
	    beans.put("vehicleManufactureDate", StringUtils.defaultString(String.valueOf(asset.getYear())));
	    beans.put("vehicleMark", getDefaultString(getGenderDesc(asset.getAssetGender().getCode())));
	    beans.put("vehicleCc", getDefaultString(asset.getEngine().getDesc(), asset.getEngine().getDescEn()));
	    beans.put("vehicleEngineNum", asset.getEngineNumber());
	    beans.put("vehicleSerieNum", asset.getChassisNumber());

	    //Detail 4
	    beans.put("assetPrice", "" + AmountUtils.format(MyNumberUtils.getDouble(asset.getTiAssetPrice())));
	    beans.put("principalAmount", "" + AmountUtils.format(MyNumberUtils.getDouble(quotation.getTiFinanceAmount())));
	    beans.put("firstPaymentPrice", "" + AmountUtils.format(MyNumberUtils.getDouble(quotation.getTiAdvancePaymentAmount())));
	    beans.put("firstPaymentPrice", "" + AmountUtils.format(MyNumberUtils.getDouble(quotation.getTiAdvancePaymentAmount())));
	    beans.put("isFirstPayment10Percentage", (10 == quotation.getAdvancePaymentPercentage()) ? "v" : "");
	    beans.put("isFirstPayment20Percentage", (20 == quotation.getAdvancePaymentPercentage()) ? "v" : "");
	    beans.put("isFirstPayment30Percentage", (30 == quotation.getAdvancePaymentPercentage()) ? "v" : "");
	    beans.put("isFirstPayment40Percentage", (40 == quotation.getAdvancePaymentPercentage()) ? "v" : "");
	    beans.put("isFirstPayment50Percentage", (50 == quotation.getAdvancePaymentPercentage()) ? "v" : "");
	    beans.put("isFirstPayment60Percentage", (60 == quotation.getAdvancePaymentPercentage()) ? "v" : "");
	    beans.put("isFirstPayment70Percentage", (70 == quotation.getAdvancePaymentPercentage()) ? "v" : "");
	    
	    beans.put("monthlyPayment", "" + AmountUtils.format(quotation.getTiInstallmentAmount()));
	    beans.put("termPayment", "" + quotation.getTerm());
	    beans.put("interest", quotation.getInterestRate() + "%");
	    beans.put("monthlyPaymentDate", (firstPaymentDate == null ? "" : leftPad("0" + DateUtils.getDay(firstPaymentDate), 2)));
	    beans.put("firstPaymentDate", (firstPaymentDate == null ? "" : DateUtils.getDateLabel(firstPaymentDate, DateUtils.FORMAT_DDMMYYYY_SLASH)));
	    if(60 == quotation.getAdvancePaymentPercentage() 
	    		|| 70 == quotation.getAdvancePaymentPercentage()) {
	    	 beans.put("incomeNotAgree","v");
	    } else {
		    beans.put("isPayment6times", (quotation.getTerm() <= 12) ? "v" : "");
		    beans.put("isPayment9times", (quotation.getTerm() > 12 && quotation.getTerm() <= 24) ? "v" : "");
		    beans.put("isPayment10times", (quotation.getTerm() > 24 && quotation.getTerm() <= 30) ? "v" : "");
		    beans.put("isPayment12times", (quotation.getTerm() > 30 && quotation.getTerm() <= 36) ? "v" : "");
		    beans.put("isPayment15times", (quotation.getTerm() > 36 && quotation.getTerm() <= 48) ? "v" : "");
		    beans.put("isPayment18times", (quotation.getTerm() > 48) ? "v" : "");	
	    }
	    beans.put("insurancePayment",  "" + AmountUtils.format(getServiceFee("INSFEE", quotation.getQuotationServices())));
	    beans.put("registrationLabel","ចំណាយ​ក្នុង​ការ​ចុះ​បញ្ជី​លើក​ដំបូង");
	    beans.put("registrationPayment", "" + AmountUtils.format(getServiceFee("REGFEE", quotation.getQuotationServices())));
	    
	    // work place

	    beans.put("lesseeCompanyName", applicantEmployment.getEmployerName());
	    beans.put("lesseeCompanyTypeBusiness", getDefaultString(applicantEmployment.getEmploymentIndustry().getDesc(), applicantEmployment.getEmploymentIndustry().getDescEn()));
	    beans.put("lesseeCompanyHouseNum", getDefaultString(employmentAddress.getHouseNo()));
	    beans.put("lesseeCompanyStreetNum", getDefaultString(employmentAddress.getStreet()));
	    beans.put("lesseeCompanyVillage", employmentAddress.getVillage().getDesc());
	    beans.put("lesseeCompanyCommune", employmentAddress.getCommune().getDesc());
	    beans.put("lesseeCompanyDistrict", employmentAddress.getDistrict().getDesc());
	    beans.put("lesseeCompanyProvince", employmentAddress.getProvince().getDesc());
	    beans.put("lesseeCompanyPhoneNum", getDefaultString(applicantEmployment.getWorkPhone()));
	    beans.put("lesseeJob", applicantEmployment.getPosition());
	    
	    //Contact Address
	    /*beans.put("isLesseeAddress",(applicant.getCorrespondence().getCode().equals("current.address")) ? "v" : "");
	    beans.put("isLesseeCard", (applicant.getCorrespondence().getCode().equals("sated.in.id.card")) ? "v" : "");
	    beans.put("isLesseePost", (applicant.getCorrespondence().getCode().equals("house.registration")) ? "v" : "");
	    beans.put("isLesseeWorkPlace",(applicant.getCorrespondence().getCode().equals("work.place")) ? "v" : "");*/
	    beans.put("isLesseeOtherAddress","");
	    beans.put("pleaseDetailContactAddress", "");
	    //Payment
	    beans.put("isMontlyPayment", quotation.getFrequency().equals(EFrequency.M) ? "v" : "");
	    beans.put("isTrimesterPayment", quotation.getFrequency().equals(EFrequency.Q) ? "v" : "");
	    beans.put("isSemesterPayment", quotation.getFrequency().equals(EFrequency.H) ? "v" : "");
        
	    String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
	    String templateFileName = templatePath + "/GLFLeasingContractKH2.xlsx";
		
	    String outputPath = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	    
	    String prefixOutputName = "GLFLeasingContractKH";
        String sufixOutputName = "xlsx";
	    String uuid = UUID.randomUUID().toString().replace("-", "");
        String xlsFileName = outputPath + "/" + prefixOutputName + uuid + "." + sufixOutputName;
	    
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(templateFileName, beans, xlsFileName);
        
        /*Edit excel file to add photo*/
        /*InputStream inp = new FileInputStream(destFileName);
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);

        CreationHelper helper = wb.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();

            InputStream is = new FileInputStream("D:/Work_local/glf/glf_report/glf/in/client.JPG");
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();

            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setRow1(6);
            anchor.setCol1(23);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize();

        FileOutputStream fileOut1 = new FileOutputStream(destFileName);
        wb.write(fileOut1);
        fileOut1.close(); */
        
        
        return prefixOutputName + uuid + "." + sufixOutputName;
	}

	/**
	 * @param value
	 * @return
	 */
	private String getDefaultString(String value) {
		return StringUtils.defaultString(value);
	}
	
	/**
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	private String getDefaultString(String value, String defaultValue) {
		if (StringUtils.isNotEmpty(value)) {
			return value;
		} else {
			return StringUtils.defaultString(defaultValue);
		}
	}
	
	/**
	 * @param value
	 * @param size
	 * @return
	 */
	private String leftPad(String value, int size) {
		return value == null ? null : value.substring(value.length() - size);
	}
	
	/**
	 * @param documCode
	 * @param documents
	 * @return
	 */
	private QuotationDocument getDocument(String documCode, List<QuotationDocument> documents) {
		for (QuotationDocument document : documents) {
			if (document.getDocument().getApplicantType().equals(EApplicantType.C)
					&& documCode.equals(document.getDocument().getCode())) {
				return document;
			}
		}
		return null;
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
		return 0.0;
	}
	
	/**
	 * @param code
	 * @return
	 */
	private String getGenderDesc(String code) {
		if (EGender.M.getCode().equals(code)) {
			return "ប្រុស";
		} else if (EGender.F.getCode().equals(code)) {
			return "ស្រី";
		} else if (EGender.U.getCode().equals(code)) {
			return "មិនដឹង";
		}
		return "";
	}
}

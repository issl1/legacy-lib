package com.nokor.efinance.glf.report.xls;

import java.util.ArrayList;
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
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author tek.thida
 *
 */
public class GLFGuaranteeContractKH implements Report {
	
    protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
    
    /**
	 * 
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		Long quotaId = (Long) reportParameter.getParameters().get("quotaId");
		boolean stamp = (Boolean) reportParameter.getParameters().get("stamp");
		Map<String, String> beans = new HashMap<String, String>();
		
		Quotation quotation = quotationService.getById(Quotation.class, quotaId);
		Applicant guarantor = quotation.getGuarantor();
		
		if(guarantor != null){
			Applicant applicant     = quotation.getApplicant();
			Employment guarantorEmployment = guarantor.getIndividual().getCurrentEmployment();
			Address guarantorAddress       = guarantor.getIndividual().getMainAddress();
			Address employmentAddress      = null;
			
			if(guarantorEmployment != null) 
				employmentAddress = guarantorEmployment.getAddress();
			
			Asset asset = quotation.getAsset();
		    // initilize list of departments in some way
		    
		    beans.put("guaranteeName", guarantor.getIndividual().getLastName() + " " + guarantor.getIndividual().getFirstName());
		    beans.put("guaranteeSex", getGenderDesc(guarantor.getIndividual().getGender().getCode()));
		    beans.put("guaranteeDateBirthday", DateUtils.getDateLabel(guarantor.getIndividual().getBirthDate(), DateUtils.FORMAT_DDMMYYYY_SLASH));
		    
		    QuotationDocument idCardDocument = getDocument("N", quotation.getQuotationDocuments());
		    beans.put("isLesseeIdCard", idCardDocument != null ? "v" : "");
		    beans.put("guaranteeCard", idCardDocument != null ? getDefaultString(idCardDocument.getReference()) : "");
		    
		    QuotationDocument reseidentBookDocument = getDocument("R", quotation.getQuotationDocuments());
		    beans.put("isLesseeResidentBookNum", reseidentBookDocument != null ? "v" : "");
		    beans.put("guaranteeResidentBookNum", reseidentBookDocument != null ? getDefaultString(reseidentBookDocument.getReference()) : "");
		    
		    QuotationDocument villageCertifiedDocument = getDocument("G", quotation.getQuotationDocuments());
		    beans.put("isLesseeCertifyLetterNum", villageCertifiedDocument != null ? "v" : "");
		    beans.put("guaranteeCertifyLetterNum", villageCertifiedDocument != null ? villageCertifiedDocument.getReference() : "");
		    
		    QuotationDocument familyBookDocument = getDocument("F", quotation.getQuotationDocuments());
		    beans.put("isLesseeFamilyBookNum", familyBookDocument != null ? "v" : "");
		    beans.put("guaranteeFamilyBookNum", familyBookDocument != null ? getDefaultString(familyBookDocument.getReference()) : "");
		    
		    QuotationDocument birthCertificateDocument = getDocument("B", quotation.getQuotationDocuments());
		    beans.put("isLesseeBirthCertificateNum", birthCertificateDocument != null ? "v" : "");
		    beans.put("guaranteeBirthCertificateNum", birthCertificateDocument != null ? getDefaultString(birthCertificateDocument.getReference()) : "");
		    
		    double totalNetIncome = 0d;
			double totalRevenus   = 0d;
			double totalAllowance = 0d;
			double totalBusinessExpenses = 0d;
			List<Employment> employments = guarantor.getIndividual().getEmployments();
			if(employments == null)
				employments = new ArrayList<Employment>();
			
			for (Employment employment : employments) {
				totalRevenus += MyNumberUtils.getDouble(employment.getRevenue()) ;
				totalAllowance += MyNumberUtils.getDouble(employment.getAllowance()) ;
				totalBusinessExpenses += MyNumberUtils.getDouble(employment.getBusinessExpense());
			}
			totalNetIncome = totalRevenus + totalAllowance - totalBusinessExpenses;
		    
			// TODO YLY
		    // beans.put("guaranteePhoneNum1", getDefaultString(guarantor.getMobilePhone()));
		    // beans.put("guaranteePhoneNum2", getDefaultString(guarantor.getMobilePhone2()));
		    beans.put("guaranteeIncome", "" + AmountUtils.format(totalNetIncome));
		    beans.put("guaranteeContractPayment", "" + AmountUtils.format(quotation.getTiFinanceAmount())); 
		    Date contractStartDate = quotation.getContractStartDate();
			if (contractStartDate == null) {
				contractStartDate = DateUtils.today();
			}
			beans.put("date",DateUtils.getDateLabel(contractStartDate, DateUtils.FORMAT_DDMMYYYY_SLASH));
		    //Detail 1//
		    beans.put("guaranteeAgreeNum", quotation.getReference());
		    beans.put("guaranteeAgreeDate", DateUtils.getDateLabel(contractStartDate, DateUtils.FORMAT_DDMMYYYY_SLASH));
		    beans.put("guaranteeRenting", applicant.getIndividual().getLastName() + " " + applicant.getIndividual().getFirstName());
		    //Detail 3//
		    beans.put("guaranteeVehicleModel", asset.getDesc());
		    beans.put("guaranteeVehicleColor", getDefaultString(asset.getColor().getDesc(), asset.getColor().getDescEn()));
		    beans.put("guaranteeVehicleManufactureDate", StringUtils.defaultString(String.valueOf(asset.getYear())));
		    beans.put("guaranteeMark", "Honda");
		    beans.put("guaranteeVehicleCc", getDefaultString(asset.getEngine().getDesc(), asset.getEngine().getDescEn()));
		    beans.put("guaranteeVehicleEngineNum", asset.getEngineNumber());
		    beans.put("guaranteeVehicleSerieNum", asset.getChassisNumber());	
		  //current address
		    beans.put("guaranteeHouseNum", guarantorAddress== null? "": guarantorAddress.getHouseNo());
		    beans.put("guaranteeStreetNum", guarantorAddress== null? "": guarantorAddress.getStreet());
		    beans.put("guaranteeVillage", guarantorAddress== null? "": guarantorAddress.getVillage().getDesc());
		    beans.put("guaranteeCommune", guarantorAddress== null? "": guarantorAddress.getCommune().getDesc());
		    beans.put("guaranteeDistrict", guarantorAddress== null? "": guarantorAddress.getDistrict().getDesc());
		    beans.put("guaranteeProvince", guarantorAddress== null? "": guarantorAddress.getProvince().getDesc());
		    // TODO YLY
		    // beans.put("guaranteeHousePhone", getDefaultString(guarantor.getMobilePhone()));
		    beans.put("guaranteeAvalableDateTime", getDefaultString(guarantor.getIndividual().getConvenientVisitTime()));
		    // work place//
	
		    beans.put("guaranteeCompanyName", guarantorEmployment ==null? "": guarantorEmployment.getEmployerName());
		    beans.put("guaranteeCompanyTypeBusiness", guarantorEmployment==null? "": getDefaultString(guarantorEmployment.getEmploymentIndustry().getDesc(), guarantorEmployment.getEmploymentIndustry().getDescEn()));
		    beans.put("guaranteeCompanyHouseNum", employmentAddress== null? "": getDefaultString(employmentAddress.getHouseNo()));
		    beans.put("guaranteeCompanyStreetNum", employmentAddress==null? "": getDefaultString(employmentAddress.getStreet()));
		    beans.put("guaranteeCompanyVillage", employmentAddress==null? "": employmentAddress.getVillage().getDesc());
		    beans.put("guaranteeCompanyCommune",employmentAddress==null? "": employmentAddress.getCommune().getDesc());
		    beans.put("guaranteeCompanyDistrict", employmentAddress==null? "": employmentAddress.getDistrict().getDesc());
		    beans.put("guaranteeCompanyProvince", employmentAddress==null?"": employmentAddress.getProvince().getDesc());
		    beans.put("guaranteeCompanyPhoneNum", guarantorEmployment==null? "": getDefaultString(guarantorEmployment.getWorkPhone()));
		    beans.put("guaranteePosistion", guarantorEmployment==null? "": guarantorEmployment.getPosition());
		}else{
			Applicant applicant = quotation.getApplicant();			
			Asset asset = quotation.getAsset();
		    // initilize list of departments in some way		    
		    beans.put("guaranteeName", "");
		    beans.put("guaranteeSex", "");
		    beans.put("guaranteeDateBirthday", "");
		    
		    QuotationDocument idCardDocument = getDocument("N", quotation.getQuotationDocuments());
		    beans.put("isLesseeIdCard", idCardDocument != null ? "v" : "");
		    beans.put("guaranteeCard", idCardDocument != null ? getDefaultString(idCardDocument.getReference()) : "");
		    
		    QuotationDocument reseidentBookDocument = getDocument("R", quotation.getQuotationDocuments());
		    beans.put("isLesseeResidentBookNum", reseidentBookDocument != null ? "v" : "");
		    beans.put("guaranteeResidentBookNum", reseidentBookDocument != null ? getDefaultString(reseidentBookDocument.getReference()) : "");
		    
		    QuotationDocument villageCertifiedDocument = getDocument("G", quotation.getQuotationDocuments());
		    beans.put("isLesseeCertifyLetterNum", villageCertifiedDocument != null ? "v" : "");
		    beans.put("guaranteeCertifyLetterNum", villageCertifiedDocument != null ? villageCertifiedDocument.getReference() : "");
		    
		    QuotationDocument familyBookDocument = getDocument("F", quotation.getQuotationDocuments());
		    beans.put("isLesseeFamilyBookNum", familyBookDocument != null ? "v" : "");
		    beans.put("guaranteeFamilyBookNum", familyBookDocument != null ? getDefaultString(familyBookDocument.getReference()) : "");
		    
		    QuotationDocument birthCertificateDocument = getDocument("B", quotation.getQuotationDocuments());
		    beans.put("isLesseeBirthCertificateNum", birthCertificateDocument != null ? "v" : "");
		    beans.put("guaranteeBirthCertificateNum", birthCertificateDocument != null ? getDefaultString(birthCertificateDocument.getReference()) : "");
		    
		    double totalNetIncome = 0d;
			double totalRevenus = 0d;
			double totalAllowance = 0d;
			double totalBusinessExpenses = 0d;
			
			totalNetIncome = totalRevenus + totalAllowance - totalBusinessExpenses;
		    
		    beans.put("guaranteePhoneNum1", "");
		    beans.put("guaranteePhoneNum2", "");
		    beans.put("guaranteeIncome", "" + AmountUtils.format(totalNetIncome));
		    beans.put("guaranteeContractPayment", "" + AmountUtils.format(quotation.getTiFinanceAmount())); 
		    Date contractStartDate = quotation.getContractStartDate();
			if (contractStartDate == null) {
				contractStartDate = DateUtils.today();
			}
			beans.put("date",DateUtils.getDateLabel(contractStartDate, DateUtils.FORMAT_DDMMYYYY_SLASH));
		    //Detail 1//
		    beans.put("guaranteeAgreeNum", quotation.getReference());
		    beans.put("guaranteeAgreeDate", DateUtils.getDateLabel(contractStartDate, DateUtils.FORMAT_DDMMYYYY_SLASH));
		    beans.put("guaranteeRenting", applicant.getIndividual().getLastName() + " " + applicant.getIndividual().getFirstName());
		    //Detail 3//
		    beans.put("guaranteeVehicleModel", asset.getDesc());
		    beans.put("guaranteeVehicleColor", getDefaultString(asset.getColor().getDesc(), asset.getColor().getDescEn()));
		    beans.put("guaranteeVehicleManufactureDate", StringUtils.defaultString(String.valueOf(asset.getYear())));
		    beans.put("guaranteeMark", "Honda");
		    beans.put("guaranteeVehicleCc", getDefaultString(asset.getEngine().getDesc(), asset.getEngine().getDescEn()));
		    beans.put("guaranteeVehicleEngineNum", asset.getEngineNumber());
		    beans.put("guaranteeVehicleSerieNum", asset.getChassisNumber());	
		  //current address
		    beans.put("guaranteeHouseNum", "");
		    beans.put("guaranteeStreetNum", "");
		    beans.put("guaranteeVillage", "");
		    beans.put("guaranteeCommune", "");
		    beans.put("guaranteeDistrict", "");
		    beans.put("guaranteeProvince", "");
		    beans.put("guaranteeHousePhone", "");
		    beans.put("guaranteeAvalableDateTime", "");
		    // work place//
	
		    beans.put("guaranteeCompanyName", "");
		    beans.put("guaranteeCompanyTypeBusiness", "");
		    beans.put("guaranteeCompanyHouseNum", "");
		    beans.put("guaranteeCompanyStreetNum", "");
		    beans.put("guaranteeCompanyVillage", "");
		    beans.put("guaranteeCompanyCommune", "");
		    beans.put("guaranteeCompanyDistrict", "");
		    beans.put("guaranteeCompanyProvince", "");
		    beans.put("guaranteeCompanyPhoneNum", "");
		    beans.put("guaranteePosistion", "");
		}
        
	    String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
	    String templateFileName = templatePath + (stamp ? "/GLFGuaranteeContractKHStamp.xlsx" : "/GLFGuaranteeContractKH.xlsx");
	    String outputPath = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	    
	    String prefixOutputName = "GLFGuaranteeContractKH";
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
	 * @param documCode
	 * @param documents
	 * @return
	 */
	private QuotationDocument getDocument(String documCode, List<QuotationDocument> documents) {
		for (QuotationDocument document : documents) {
			if (document.getDocument().getApplicantType().equals(EApplicantType.G)
					&& documCode.equals(document.getDocument().getCode())) {
				return document;
			}
		}
		return null;
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

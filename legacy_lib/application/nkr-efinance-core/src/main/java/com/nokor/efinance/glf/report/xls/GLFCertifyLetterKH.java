package com.nokor.efinance.glf.report.xls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * Certify letter report
 * @author uhout.cheng
 */
public class GLFCertifyLetterKH implements Report {
	
    protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
    public static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    public static String NA = "N/A";
    
    /**
     * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
     */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		
		Long quotaId = (Long) reportParameter.getParameters().get("quotaId");
		
		Quotation quotation = quotationService.getById(Quotation.class, quotaId);
		Applicant applicant = quotation.getApplicant();
		Asset asset = quotation.getAsset();
			    
	    // initialize list of departments in some way
	    Map<String, String> beans = new HashMap<String, String>();
	    
	    Date contractStartDate = quotation.getContractStartDate();
	    beans.put("contractNumber", quotation.getReference());
	    beans.put("dateofContract", (DateUtils.getDateLabel(contractStartDate, DEFAULT_DATE_FORMAT)));
	    
	    //Integer numYear = quotation.getTerm() / 12;
	    //beans.put("contractdurationInMonth", numYear.toString());
	    beans.put("contractdurationInMonth", getDefaultString(quotation.getTerm().toString()));
	    
	    Date endDate = DateUtils.addMonthsDate(contractStartDate, quotation.getTerm());
	    Date contractEndDate = DateUtils.addDaysDate(endDate, -1);
	    beans.put("startDateOfContract", (DateUtils.getDateLabel(contractStartDate, DEFAULT_DATE_FORMAT)));
	    beans.put("endDateOfContract", DateUtils.getDateLabel(contractEndDate, DEFAULT_DATE_FORMAT));
	    beans.put("lesseeName", applicant.getIndividual().getLastName() + " " + applicant.getIndividual().getFirstName());
	    
	    QuotationDocument idCardDocument = getDocument("N", quotation.getQuotationDocuments());
	    beans.put("idNumber", idCardDocument != null ? getDefaultString(idCardDocument.getReference()) : NA);
	    beans.put("dateOfIssue", idCardDocument != null ? DateUtils.getDateLabel(idCardDocument.getIssueDate(), DEFAULT_DATE_FORMAT) : NA);
	    // TODO YLY
	    // beans.put("phonenumber", getDefaultString(applicant.getMobilePhone()));
	    beans.put("contractNumber", quotation.getReference());
	    beans.put("engineNumber", asset.getEngineNumber());
	    beans.put("chassisNumber", asset.getChassisNumber());
	    beans.put("plateNumber", asset.getPlateNumber());
	    beans.put("assetRange", asset.getDescEn());
	    beans.put("type", getDefaultString(getGenderDesc(asset.getAssetGender().getCode())));
	    beans.put("numberOfCC", getDefaultString(asset.getEngine().getDesc(), asset.getEngine().getDescEn()));
	    beans.put("color", getDefaultString(asset.getColor().getDesc(), asset.getColor().getDescEn()));
	    beans.put("year", asset.getYear().toString());
	    beans.put("quantityOfMotorcycle", "1");
	    
	    String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
	    String templateFileName = templatePath + "/GLFCertifyLetterKH.xlsx";
	    String outputPath = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	    
	    String prefixOutputName = "GLFCertifyLetterKH";
        String sufixOutputName = "xlsx";
	    String uuid = UUID.randomUUID().toString().replace("-", "");
        String xlsFileName = outputPath + "/" + prefixOutputName + uuid + "." + sufixOutputName;
	    
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(templateFileName, beans, xlsFileName);
        
        return prefixOutputName + uuid + "." + sufixOutputName;
	}
	
	/**
	 * 
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
	 * 
	 * @param value
	 * @return
	 */
	private String getDefaultString(String value) {
		return StringUtils.defaultString(value);
	}
	
	/**
	 * 
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

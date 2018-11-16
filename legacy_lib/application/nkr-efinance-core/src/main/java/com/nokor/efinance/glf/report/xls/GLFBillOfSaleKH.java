package com.nokor.efinance.glf.report.xls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetMake;
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
 * Bill of sale report
 * @author bunlong.taing
 */
public class GLFBillOfSaleKH implements Report {
	
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
		
		String brandInEnglish = "HONDA";
		AssetMake assetMake = getAssetMakeByCode("HON");
		if (assetMake != null && StringUtils.isNotEmpty(assetMake.getDescEn())) {
			brandInEnglish = assetMake.getDescEn();
		}
		StringBuffer brandNameMoto = new StringBuffer(); 
		brandNameMoto.append(brandInEnglish);
		brandNameMoto.append(" ");
		brandNameMoto.append(asset.getDescEn());

	    Map<String, String> beans = new HashMap<String, String>();
	    
	    beans.put("lesseeName", applicant.getIndividual().getLastName() + " " + applicant.getIndividual().getFirstName());
	    beans.put("sex", getGenderDesc(applicant.getIndividual().getGender().getCode()));
	    beans.put("dateOfBirth", DateUtils.getDateLabel(applicant.getIndividual().getBirthDate(), DEFAULT_DATE_FORMAT));
	    QuotationDocument idCardDocument = getDocument("N", quotation.getQuotationDocuments());
	    beans.put("id", idCardDocument != null ? getDefaultString(idCardDocument.getReference()) : "");
	    
	    Address address = applicant.getIndividual().getMainAddress();
	    if (address != null) {
	    	beans.put("houseNo", StringUtils.isNotEmpty(address.getHouseNo()) ? address.getHouseNo() : NA);
	    	beans.put("streetNo", StringUtils.isNotEmpty(address.getStreet()) ? address.getStreet() : NA);
	    	beans.put("village", address.getVillage() != null ? address.getVillage().getDesc() : NA);
	    	beans.put("commune", address.getCommune() != null ? address.getCommune().getDesc() : NA);
	    	beans.put("district", address.getDistrict() != null ? address.getDistrict().getDesc() : NA);
	    	beans.put("province", address.getProvince() != null ? address.getProvince().getDesc() : NA);
	    } else {
	    	beans.put("houseNo", NA);
	    	beans.put("streetNo", NA);
	    	beans.put("village", NA);
	    	beans.put("commune", NA);
	    	beans.put("district", NA);
	    	beans.put("province", NA);
	    }
	    
	    Date contractStartDate = quotation.getContractStartDate();	
	    beans.put("contractReference", quotation.getReference());
	    beans.put("contractDate", (contractStartDate == null ? "" : DateUtils.getDateLabel(contractStartDate, DEFAULT_DATE_FORMAT)));
	    
	    Date endDate = DateUtils.addMonthsDate(contractStartDate, quotation.getTerm());
	    Date contracEndDate = DateUtils.addDaysDate(endDate, -1);
	    beans.put("startDateContract", DateUtils.getDateLabel(contractStartDate, DEFAULT_DATE_FORMAT));
	    beans.put("endDateContract", DateUtils.getDateLabel(contracEndDate, DEFAULT_DATE_FORMAT));
	    
	    beans.put("assetRange", brandNameMoto.toString());
	    beans.put("color", getDefaultString(asset.getColor().getDesc(), asset.getColor().getDescEn()));
	    beans.put("year", asset.getYear().toString());
	    beans.put("assetModel", getDefaultString(getGenderDesc(asset.getAssetGender().getCode())));
	    beans.put("numbercc", getDefaultString(asset.getEngine().getDesc(), asset.getEngine().getDescEn()));
	    beans.put("engineNo", asset.getEngineNumber());
	    beans.put("chassisNo", asset.getChassisNumber());
	    
	    String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
	    String templateFileName = templatePath + "/GLFBillOfSaleKH.xlsx";
	    String outputPath = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	    
	    String prefixOutputName = "GLFBillOfSaleKH";
        String sufixOutputName = "xlsx";
	    String uuid = UUID.randomUUID().toString().replace("-", "");
        String xlsFileName = outputPath + "/" + prefixOutputName + uuid + "." + sufixOutputName;
	    
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(templateFileName, beans, xlsFileName);
        
        return prefixOutputName + uuid + "." + sufixOutputName;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public AssetMake getAssetMakeByCode(String code) {
		EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
		BaseRestrictions<AssetMake> restrictions = new BaseRestrictions<>(AssetMake.class);
		restrictions.addCriterion(Restrictions.eq("code", code));
		List<AssetMake> list = entityService.list(restrictions);
		return (list != null && !list.isEmpty()) ? list.get(0) : null;
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
	 * @return
	 */
	private String getDefaultString(String value) {
		return StringUtils.defaultString(value);
	}
	
	/**
	 * Get the Document
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
}

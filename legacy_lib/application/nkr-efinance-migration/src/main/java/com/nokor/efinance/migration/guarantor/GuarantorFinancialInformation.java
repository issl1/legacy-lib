package com.nokor.efinance.migration.guarantor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.migration.model.guarantor.GuarantorEmployment;
/**
 * 
 * @author buntha.chea
 *
 */
public class GuarantorFinancialInformation {
	
	private Logger LOG = LoggerFactory.getLogger(GuarantorFinancialInformation.class);
	private List<String> errors = new ArrayList<>();
	private List<GuarantorEmployment> guarantorEmployments = new ArrayList<>();

	public static void main(String[] args) {
		GuarantorFinancialInformation personalinfomation = new GuarantorFinancialInformation();
		//File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/211-1-201507211636.csv");
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/211-1-201507211636.csv");
		personalinfomation.validateData(f);
	}

	/**
	 * 
	 * @param url
	 * @param fileName
	 */
	public List<GuarantorEmployment> validateData(File file) {
		String cvsSplitBy = "\\|";
		
		String s = "211|System ID|Income Type|Category Income|Name company|Job title|Working period(year)|Working period(month)|Income 1|Number / Building|Moo|Soi|Street|Province|District|Subdistrict|Postal Code|Phone Company";
		int length = s.split(cvsSplitBy).length;
		
		try {
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] guarantorEmploymentDatas = lines.get(i).split(cvsSplitBy, -1);
				if ("211".equals(guarantorEmploymentDatas[0]) && !"System ID".equals(guarantorEmploymentDatas[1])) {
					if (length == guarantorEmploymentDatas.length) {
						GuarantorEmployment guarantorEmployment = new GuarantorEmployment();
						guarantorEmployment.setMigrationID(guarantorEmploymentDatas[1]);
						//guarantorEmployment.setEmploymentTypeCode(guarantorEmploymentDatas[2]);
						guarantorEmployment.setEmploymentTypeCode(StringUtils.isEmpty(guarantorEmploymentDatas[2]) ? "xxx" : guarantorEmploymentDatas[2]);
						//guarantorEmployment.setEmploymentCategoryCode(guarantorEmploymentDatas[3]);
						guarantorEmployment.setEmploymentCategoryCode(StringUtils.isEmpty(guarantorEmploymentDatas[3]) ? "xxx" : guarantorEmploymentDatas[3]);
						guarantorEmployment.setEmployerName(StringUtils.isEmpty(guarantorEmploymentDatas[4]) ? "xxx" : guarantorEmploymentDatas[4]);
						guarantorEmployment.setPosition(guarantorEmploymentDatas[5]);
						guarantorEmployment.setTimeWithEmployerInYear(StringUtils.isEmpty(guarantorEmploymentDatas[6]) ? 0 : Integer.valueOf(guarantorEmploymentDatas[6]));
						guarantorEmployment.setTimeWithEmployerInMonth(StringUtils.isEmpty(guarantorEmploymentDatas[7]) ? 0 : Integer.valueOf(guarantorEmploymentDatas[7]));
						guarantorEmployment.setRevenue(formatDouble(guarantorEmploymentDatas[8]));
						
						guarantorEmployment.setHouseNo(StringUtils.isEmpty(guarantorEmploymentDatas[9]) ? "xxx" : guarantorEmploymentDatas[9]);
						guarantorEmployment.setLine1(guarantorEmploymentDatas[10]);
						guarantorEmployment.setLine2(guarantorEmploymentDatas[11]);
						//guarantorEmployment.setStreet(guarantorEmploymentDatas[12]);
						guarantorEmployment.setStreet(StringUtils.isEmpty(guarantorEmploymentDatas[12]) ? "xxx" : guarantorEmploymentDatas[12]);
						guarantorEmployment.setProvinceCode(guarantorEmploymentDatas[13]);
						guarantorEmployment.setDistrictCode(guarantorEmploymentDatas[14]);
						guarantorEmployment.setCommuneCode(guarantorEmploymentDatas[15]);
						guarantorEmployment.setPostalCode(guarantorEmploymentDatas[16]);
						
						guarantorEmployment.setWorkPhone(guarantorEmploymentDatas[17]);	
						this.validate(guarantorEmployment, i + 1);
						guarantorEmployments.add(guarantorEmployment);
					} else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  guarantorEmploymentDatas.length + ", expected length =" + length);
						errors.add("");
					}
				}
			}
			if (errors.isEmpty()) {
				LOG.info("**********Validate Successfuly****************");
				return guarantorEmployments;
			} else {
				for (String error : errors) {
					LOG.error(error);
				}
			}
		} catch (Exception e) {
			LOG.error("Exception => ", e);
		} 
		guarantorEmployments.clear();
		return guarantorEmployments;
	}
	/**
	 * 
	 * @param guarantorEmployment
	 * @param line
	 */
	public void validate(GuarantorEmployment guarantorEmployment, int line) {
		String message = "";
		if (StringUtils.isEmpty(guarantorEmployment.getEmploymentTypeCode())) {
			message += "_INCOME_TYPE_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorEmployment.getEmploymentCategoryCode())) {
			message += "_CATEGORY_INCOME_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorEmployment.getEmployerName())) {
			message += "_NAME_COMPANY_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorEmployment.getPosition())) {
			message += "_JOB_TITLE_MANDATORY|";
		}
		if (guarantorEmployment.getTimeWithEmployerInYear() == null) {
			message += "_WORKING_PERIOD_IN_YEAR_MANDATORY|";
		}
		if (guarantorEmployment.getTimeWithEmployerInMonth() == null) {
			message += "_WORKING_PERIOD_IN_MONTH_MANDATORY|";
		}
		if (guarantorEmployment.getRevenue() == null) {
			message += "_INCOME_MANDATORY";
		}
		if (StringUtils.isEmpty(guarantorEmployment.getHouseNo())) {
			message += "_HOUSE_NO_MANDATORY";
		}
		if (StringUtils.isEmpty(guarantorEmployment.getStreet())) {
			message += "_STREET_MANDATORY";
		}
		if (StringUtils.isEmpty(guarantorEmployment.getProvinceCode())) {
			message += "_PROVINCE_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorEmployment.getDistrictCode())) {
			message += "_DISTRICT_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorEmployment.getCommuneCode())) {
			message += "_SUB_DISTRICT_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorEmployment.getPostalCode())) {
			message += "_POSTAL_CODE_MANDATORY|";
		}
		if (!StringUtils.isEmpty(message)) {
			errors.add("LINE : " + line + " " + "Guarantor_Employment" +" "+ message);
		}
	}
	
	private Double formatDouble(String income) {
		if (org.springframework.util.StringUtils.isEmpty(income)) {
			return 0d;
		} else {
			return Double.valueOf(income.replaceAll(",", ""));
		}
	}
}

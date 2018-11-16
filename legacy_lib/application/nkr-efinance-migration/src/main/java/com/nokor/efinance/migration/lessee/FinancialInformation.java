package com.nokor.efinance.migration.lessee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nokor.efinance.migration.model.lessee.LesseeEmployment;
/**
 * 
 * @author buntha.chea
 *
 */
public class FinancialInformation {
	
	private Logger LOG = LoggerFactory.getLogger(FinancialInformation.class);
	private List<String> errors = new ArrayList<>();
	private List<LesseeEmployment> lesseeEmployments = new ArrayList<>();
		
	public static void main(String[] args) {
		FinancialInformation personalinfomation = new FinancialInformation();
		//File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/013-1-201507211636.csv");
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/011-1-201507211636.csv");
		personalinfomation.validateData(f);
	}
	/**
	 * 
	 * @param file
	 * @return
	 */
	public List<LesseeEmployment> validateData(File file) {
		
		String cvsSplitBy = "\\|";
		
		String s = "011|System ID|Income Type|Category Income|Name company|Job title|Working period(year)|Working period(month)|Income 1|Number / Building|Moo|Soi|Street|Province|District|Subdistrict|Postal Code|Phone Company";
		int length = s.split(cvsSplitBy).length;
		
		try {
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] financialDatas = lines.get(i).split(cvsSplitBy, -1);
				if ("011".equals(financialDatas[0]) && !"System ID".equals(financialDatas[1])) {
					if (length == financialDatas.length) {
						LesseeEmployment lesseeEmployment = new LesseeEmployment();
						lesseeEmployment.setMigrationID(financialDatas[1]);
						lesseeEmployment.setEmploymentTypeCode(StringUtils.isEmpty(financialDatas[2]) ? "XXX" : financialDatas[2]);
						lesseeEmployment.setEmploymentCategoryCode(StringUtils.isEmpty(financialDatas[3]) ? "XXX" : financialDatas[3]);
						lesseeEmployment.setEmployerName(StringUtils.isEmpty(financialDatas[4]) ? "XXX" : financialDatas[4]);
						lesseeEmployment.setPosition(financialDatas[5]);
						lesseeEmployment.setTimeWithEmployerInYear(StringUtils.isEmpty(financialDatas[6]) ? 0 : Integer.valueOf(financialDatas[6]));
						lesseeEmployment.setTimeWithEmployerInMonth(StringUtils.isEmpty(financialDatas[7]) ? 0 : Integer.valueOf(financialDatas[7]));
						lesseeEmployment.setRevenue(StringUtils.isEmpty(financialDatas[8]) ? 0d : Double.valueOf(financialDatas[8]));
						
						lesseeEmployment.setHouseNo(StringUtils.isEmpty(financialDatas[9]) ? "xxx" : financialDatas[9]);
						lesseeEmployment.setLine1(financialDatas[10]);
						lesseeEmployment.setLine2(financialDatas[11]);
						lesseeEmployment.setStreet(StringUtils.isEmpty(financialDatas[12]) ? "xxx" : financialDatas[12]);
						lesseeEmployment.setProvinceCode(financialDatas[13]);
						lesseeEmployment.setDistrictCode(financialDatas[14]);
						lesseeEmployment.setCommuneCode(financialDatas[15]);
						lesseeEmployment.setPostalCode(financialDatas[16]);
						lesseeEmployment.setWorkPhone(financialDatas[17]);
						this.validate(lesseeEmployment, i + 1);
						lesseeEmployments.add(lesseeEmployment);
					}else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  financialDatas.length + ", expected length =" + length);
						errors.add("");
					}
				}
			}
			if (errors.isEmpty()) {
				LOG.info("Validate Succecss!!");
				return lesseeEmployments;
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
	 * @param lesseeEmployment
	 * @param line
	 */
	public void validate(LesseeEmployment lesseeEmployment, int line) {
		String message = "";
		if (StringUtils.isEmpty(lesseeEmployment.getEmploymentTypeCode())) {
			message += "_INCOME_TYPE_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getEmploymentCategoryCode())) {
			message += "_CATEGORY_INCOME_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getEmployerName())) {
			message += "_NAME_COMPANY_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getPosition())) {
			message += "_JOB_TITLE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getTimeWithEmployerInYear())) {
			message += "_WORKING_PERIOD_IN_YEAR_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getTimeWithEmployerInMonth())) {
			message += "_WORKING_PERIOD_IN_MONTH_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getRevenue())) {
			message += "_INCOME_MANDATORY";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getHouseNo())) {
			message += "_HOUSE_NO_MANDATORY";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getStreet())) {
			message += "_STREET_MANDATORY";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getProvinceCode())) {
			message += "_PROVINCE_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getDistrictCode())) {
			message += "_DISTRICT_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getCommuneCode())) {
			message += "_SUB_DISTRICT_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeEmployment.getPostalCode())) {
			message += "_POSTAL_CODE_MANDATORY|";
		}
		if (!StringUtils.isEmpty(message)) {
			errors.add("LINE : " + line + " " + "LESSEE-EMPLOYMENT" +" "+ message);
		}
	}
	
}

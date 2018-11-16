package com.nokor.efinance.migration.spouse;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nokor.efinance.migration.model.spouse.Spouse;
/**
 * 
 * @author buntha.chea
 *
 */
public class SpousePersonalInformation {
	
	private Logger LOG = LoggerFactory.getLogger(SpousePersonalInformation.class);
	private List<String> errors = new ArrayList<>();
	private List<Spouse> spouses = new ArrayList<>();
	
	private final static String DATE_FORMAT = "dd.MM.yyyy hh:mm";
	private final static String SPOUSE = "SPOUSE";
	
	
	public static void main(String[] args) {
		SpousePersonalInformation personalInformation = new SpousePersonalInformation();
		//File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/210-1-201507211636.csv");
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/110-1-201507211636.csv");
		personalInformation.validateData(f);
	}
	/**
	 * 
	 * @param url
	 * @param fileName
	 */
	public List<Spouse> validateData(File file) {
		String cvsSplitBy = "\\|";
		
		String s = "110|System ID|Contact No|ID type|IDNumber|Issuing ID date|Expiring ID date|Customer ID|Prefix|First name |Last name|Nickname|DateOfBirth|Status|Children|Education|Household|Nationality|Religion|Second language|Household Expenses|Household Income";
		int length = s.split(cvsSplitBy).length;
		
		try {
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] spouseDatas = lines.get(i).split(cvsSplitBy, -1);
				if ("110".equals(spouseDatas[0]) && !"System ID".equals(spouseDatas[1])) {
					if (length == spouseDatas.length) {
						//errors = ValidateDataInCSV.validateApplicant(SPOUSE, spouseDatas, i + 1);
						Spouse spouse = new Spouse();
						spouse.setMigrationID(spouseDatas[1]);
						//Contact No [2]
						spouse.setTypeIdNumber(StringUtils.isEmpty(spouseDatas[3]) ? "xxx" : spouseDatas[3]);
						spouse.setIdNumber(StringUtils.isEmpty(spouseDatas[4]) ? "xxx" : spouseDatas[4]);
						spouse.setIssuingIdNumberDate(checkDate(spouseDatas[5]) == null ? checkDate("10.07.2006 00:00") : checkDate(spouseDatas[5]));
						spouse.setExpiringIdNumberDate(checkDate(spouseDatas[6]) == null ? checkDate("10.07.2016 00:00") : checkDate(spouseDatas[6]));
						spouse.setReference(StringUtils.isEmpty(spouseDatas[7]) ? "xxx" : spouseDatas[7]);
						spouse.setCivilityCode(StringUtils.isEmpty(spouseDatas[8]) ? "xxx" : spouseDatas[8]);
						spouse.setFirstName(spouseDatas[9]);
						spouse.setLastNameEn(spouseDatas[10]);
						spouse.setNickName(spouseDatas[11]);
						spouse.setBirthDate(checkDate(spouseDatas[12]) == null ? checkDate("10.07.1991 00:00") : checkDate(spouseDatas[12]));
						spouse.setMaritalStatusCode(StringUtils.isEmpty(spouseDatas[13]) ? "xxx" : spouseDatas[13]);
						spouse.setNumberOfChildren(StringUtils.isEmpty(spouseDatas[14]) ? 0 : Integer.valueOf(spouseDatas[14]));
						spouse.setEducationCode(spouseDatas[15]);
						spouse.setNumberOfHousehold(StringUtils.isEmpty(spouseDatas[16]) ? 0 : Integer.valueOf(spouseDatas[16]));
						spouse.setNationalityCode(spouseDatas[17]);
						spouse.setReligionCode(spouseDatas[18]);
						spouse.setSecondLanguageCode(spouseDatas[19]);
						spouse.setHouseholdExpenses(StringUtils.isEmpty(spouseDatas[20]) ? 0d : Double.valueOf(spouseDatas[20]));
						spouse.setHouseholdIncome(StringUtils.isEmpty(spouseDatas[21]) ? 0d : Double.valueOf(spouseDatas[21]));
						this.validate(spouse, i + 1);
						spouses.add(spouse);
					} else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  spouseDatas.length + ", expected length =" + length);
						errors.add("");
					}
				}
			}
			if (errors.isEmpty()) {
				LOG.info("Validate Success!!");
				return spouses;
			} else {
				for (String error : errors) {
					LOG.info(error);
				}
			}
		} catch (Exception e) {
			LOG.error("Exception => ", e);
		} 
		spouses.clear();
		return spouses;
	}
	/**
	 * 
	 * @param spouse
	 * @param line
	 */
	public void validate(Spouse spouse , int line) {
		String message = "";
		if (StringUtils.isEmpty(spouse.getTypeIdNumber())) {
			message += "_ID_TYPE_MANDATORY|";
		}
		if (StringUtils.isEmpty(spouse.getIdNumber())) {
			message += "_ID_NUMBER_MANDATORY|";
		}
		if (spouse.getIssuingIdNumberDate() == null) {
			message += "_ISSUING_ID_DATE_MANDATORY|";
		}
		if (spouse.getExpiringIdNumberDate() == null) {
			message += "_EXPIRING_ID_DATE_MANDATORY|";
		}
		if (StringUtils.isEmpty(spouse.getReference())) {
			message += "_REFERENCE_MANDATORY|";
		}
		if (StringUtils.isEmpty(spouse.getCivilityCode())) {
			message += "_CIVILITY_MANDATORY|";
		}
		if (StringUtils.isEmpty(spouse.getFirstName())) {
			message += "_FIRSTNAME_MANDATORY|";
		}
		if (StringUtils.isEmpty(spouse.getLastNameEn())) {
			message += "_LASTNAME_MANDATORY|";
		}
		if (spouse.getBirthDate() == null) {
			message += "_DOB_MANDATORY|";
		}
		if (StringUtils.isEmpty(spouse.getMaritalStatusCode())) {
			message += "_MARITAL_STATUS_MANDATORY";
		}
		if (!StringUtils.isEmpty(message)) {
			errors.add("LINE : " + line + " " + SPOUSE +" "+ message);
		}
	}
	
	private Date checkDate(String spouseData) {
		return StringUtils.isEmpty(spouseData) ? null : DateUtils.getDate(spouseData, DATE_FORMAT);
	}
}

package com.nokor.efinance.migration.lessee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nokor.efinance.migration.model.lessee.Lessee;
/**
 * 
 * @author buntha.chea
 *
 */
public class PersonalInformation {

	private final static String DATE_FORMAT = "dd.MM.yyyy hh:mm";
	
	private Logger LOG = LoggerFactory.getLogger(PersonalInformation.class);
	List<String> errors = new ArrayList<>();
	List<Lessee> lessees = new ArrayList<>();
	private final static String LESSEE = "LESSEE";
	
	int i = 0;
	
	public static void main(String[] args) {
		PersonalInformation personalInformation = new PersonalInformation();
		//File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/013-1-201507211636.csv");
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/010-1-201507211636.csv");
		personalInformation.validateData(f);
	}
	/**
	 * 
	 * @param url
	 * @param fileName
	 * 
	 */
	public List<Lessee> validateData(File file) {
		String cvsSplitBy = "\\|";
		String s = "010|System ID|ID type|IDNumber|Issuing ID date|Expiring ID date|Customer ID|Prefix|First name |Last name|Nickname|DateOfBirth|Status|Children|Education|Household|Nationality|Religion|Second language|Household Expenses|Household Income";
		int length = s.split(cvsSplitBy).length;
		lessees.clear();
		try {
			List<String> lines = FileUtils.readLines(file);
			for (i = 0; i < lines.size(); i++) {
				String[] lesseesData = lines.get(i).split(cvsSplitBy, -1);
				if ("010".equals(lesseesData[0]) && !"System ID".equals(lesseesData[1])) {
					if (length == lesseesData.length) {
						Lessee lessee = new Lessee();
						lessee.setMigrationID(lesseesData[1]);
						lessee.setTypeIdNumberCode(lesseesData[2]);
						lessee.setIdNumber(lesseesData[3]);
						lessee.setIssuingIdNumberDate(StringUtils.isEmpty(lesseesData[4]) ? DateUtils.getDate("01.12.2007 00:00", DATE_FORMAT) : DateUtils.getDate(lesseesData[4], DATE_FORMAT));
						lessee.setExpiringIdNumberDate(StringUtils.isEmpty(lesseesData[5]) ? DateUtils.getDate("01.12.2017 00:00", DATE_FORMAT) : DateUtils.getDate(lesseesData[5], DATE_FORMAT));
						lessee.setReference(lesseesData[6]);
						lessee.setCivilityCode(lesseesData[7]);
						lessee.setFirstName(lesseesData[8]);
						lessee.setLastName(lesseesData[9]);
						lessee.setNickName(lesseesData[10]);
						lessee.setBirthDate(StringUtils.isEmpty(lesseesData[11]) ? DateUtils.getDate("10.10.1991 00:00", DATE_FORMAT) : DateUtils.getDate(lesseesData[11], DATE_FORMAT));
						lessee.setMaritalStatusCode(lesseesData[12]);
						lessee.setNumberOfChildren(StringUtils.isEmpty(lesseesData[13]) ? 0 : Integer.valueOf(lesseesData[13]));
						lessee.setEducationCode(lesseesData[14]);
						lessee.setNumberOfHousehold(StringUtils.isEmpty(lesseesData[15]) ? 0 : Integer.valueOf(lesseesData[15]));
						lessee.setNationalityCode(lesseesData[16]);
						lessee.setReligionCode(lesseesData[17]);
						lessee.setSecondLanguageCode(lesseesData[18]);
						lessee.setHouseholdExpenses(StringUtils.isEmpty(lesseesData[19]) ? 0d : Double.valueOf(lesseesData[19]));
						lessee.setHouseholdIncome(StringUtils.isEmpty(lesseesData[20]) ? 0d : Double.valueOf(lesseesData[20]));	
						this.validate(lessee, i + 1);
						lessees.add(lessee);
					} else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  lesseesData.length + ", expected length =" + length);
						errors.add("");
					}
					
				}
			}
			if (errors.isEmpty()){
				LOG.info("Validate Successfuly!!");
				return lessees;
			}else {
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
	 * @param code
	 * @param lessee
	 * @param line
	 * @return
	 */
	public void validate(Lessee lessee , int line) {
		String message = "";
		if (StringUtils.isEmpty(lessee.getTypeIdNumberCode())) {
			message += "_ID_TYPE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lessee.getIdNumber())) {
			message += "_ID_NUMBER_MANDATORY|";
		}
		if (lessee.getIssuingIdNumberDate() == null) {
			message += "_ISSUING_ID_DATE_MANDATORY|";
		}
		if (lessee.getExpiringIdNumberDate() == null) {
			message += "_EXPIRING_ID_DATE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lessee.getReference())) {
			message += "_REFERENCE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lessee.getCivilityCode())) {
			message += "_CIVILITY_MANDATORY|";
		}
		if (StringUtils.isEmpty(lessee.getFirstName())) {
			message += "_FIRSTNAME_MANDATORY|";
		}
		if (StringUtils.isEmpty(lessee.getLastName())) {
			message += "_LASTNAME_MANDATORY|";
		}
		if (lessee.getBirthDate() == null) {
			message += "_DOB_MANDATORY|";
		}
		if (StringUtils.isEmpty(lessee.getMaritalStatusCode())) {
			message += "_MARITAL_STATUS_MANDATORY";
		}
		if (!StringUtils.isEmpty(message)) {
			errors.add("LINE : " + line + " " + LESSEE +" "+ message);
		}
	}
	
}

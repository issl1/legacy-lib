package com.nokor.efinance.migration.guarantor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nokor.efinance.migration.model.guarantor.Guarantor;
/**
 * 
 * @author buntha.chea
 *
 */
public class GuarantorPersonalInformation {
	
	private final static String DATE_FORMAT = "dd.MM.yyyy hh:mm";
	private final static String GUARANTOR = "GUARANTOR";

	private Logger LOG = LoggerFactory.getLogger(GuarantorPersonalInformation.class);
	private List<String> errors = new ArrayList<>();
	private List<Guarantor> guarantors = new ArrayList<>();
	
	public static void main(String[] args) {
		GuarantorPersonalInformation personalinformation = new GuarantorPersonalInformation();
		//File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/210-1-201507211636.csv");
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/210-1-201507211636.csv");
		personalinformation.validataData(f);
	}
	/**
	 * 
	 * @param url
	 * @param fileName
	 */
	public List<Guarantor> validataData(File file) {
		String cvsSplitBy = "\\|";
		
		String s = "210|System ID|ID type|IDNumber|Issuing ID date|Expiring ID date|Customer ID|Prefix|First name |Last name|Nickname|DateOfBirth|Status|Children|Education|Household|Nationality|Religion|Second language|Household Expenses|Household Income";
		int length = s.split(cvsSplitBy).length;
		
		try {
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] guarantorDatas = lines.get(i).split(cvsSplitBy, -1);
				if ("210".equals(guarantorDatas[0]) && !"System ID".equals(guarantorDatas[1])) {
					if (length == guarantorDatas.length) {
						//errors = ValidateDataInCSV.validateApplicant(GUARANTOR, guarantorDatas, i + 1);
						Guarantor guarantor = new Guarantor();
						guarantor.setMigrationID(guarantorDatas[1]);
						guarantor.setTypeIdNumber(guarantorDatas[2]);
						guarantor.setIdNumber(guarantorDatas[3]);
						guarantor.setIssuingIdNumberDate(StringUtils.isEmpty(guarantorDatas[4]) ? null : DateUtils.getDate(guarantorDatas[4], DATE_FORMAT));
						guarantor.setExpiringIdNumberDate(StringUtils.isEmpty(guarantorDatas[5]) ? null : DateUtils.getDate(guarantorDatas[5], DATE_FORMAT));
						guarantor.setReference(guarantorDatas[6]);
						guarantor.setCivilityCode(guarantorDatas[7]);
						guarantor.setFirstName(guarantorDatas[8]);
						guarantor.setLastNameEn(guarantorDatas[9]);
						guarantor.setNickName(guarantorDatas[10]);
						guarantor.setBirthDate(StringUtils.isEmpty(guarantorDatas[11]) ? null : DateUtils.getDate(guarantorDatas[11], DATE_FORMAT));
						guarantor.setMaritalStatusCode(guarantorDatas[12]);
						guarantor.setNumberOfChildren(StringUtils.isEmpty(guarantorDatas[13]) ? 0 : Integer.valueOf(guarantorDatas[13]));
						guarantor.setEducationCode(guarantorDatas[14]);
						guarantor.setNumberOfHousehold(StringUtils.isEmpty(guarantorDatas[15]) ? 0 : Integer.valueOf(guarantorDatas[15]));
						guarantor.setNationalityCode(guarantorDatas[16]);
						guarantor.setReligionCode(guarantorDatas[17]);
						guarantor.setSecondLanguageCode(guarantorDatas[18]);
						guarantor.setHouseholdExpenses(StringUtils.isEmpty(guarantorDatas[19]) ? 0d : Double.valueOf(guarantorDatas[19]));
						guarantor.setHouseholdIncome(StringUtils.isEmpty(guarantorDatas[20]) ? 0d : Double.valueOf(guarantorDatas[20]));
						this.validate(guarantor, i + 1);
						guarantors.add(guarantor);
					}else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  guarantorDatas.length + ", expected length =" + length);
						errors.add("");
					}
				}
			}
			if (errors.isEmpty()) {
				LOG.info("*************validate success***************");
				return guarantors;
			} else {
				for (String error : errors) {
					LOG.info(error);
				}
			}
		} catch (Exception e) {
			LOG.error("Exception => ", e);
		} 
		guarantors.clear();
		return guarantors;
	}
	/**
	 * 
	 * @param guarantor
	 * @param line
	 */
	public void validate(Guarantor guarantor , int line) {
		String message = "";
		if (StringUtils.isEmpty(guarantor.getTypeIdNumber())) {
			message += "_ID_TYPE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantor.getIdNumber())) {
			message += "_ID_NUMBER_MANDATORY|";
		}
		if (guarantor.getIssuingIdNumberDate() == null) {
			message += "_ISSUING_ID_DATE_MANDATORY|";
		}
		if (guarantor.getExpiringIdNumberDate() == null) {
			message += "_EXPIRING_ID_DATE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantor.getReference())) {
			message += "_REFERENCE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantor.getCivilityCode())) {
			message += "_CIVILITY_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantor.getFirstName())) {
			message += "_FIRSTNAME_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantor.getLastNameEn())) {
			message += "_LASTNAME_MANDATORY|";
		}
		if (guarantor.getBirthDate() == null) {
			message += "_DOB_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantor.getMaritalStatusCode())) {
			message += "_MARITAL_STATUS_MANDATORY";
		}
		if (!StringUtils.isEmpty(message)) {
			errors.add("LINE : " + line + " " + GUARANTOR +" "+ message);
		}
	}
}

package com.nokor.efinance.migration.guarantor;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.util.StringUtils;

import com.nokor.common.app.hr.model.ContactInfo;
import com.nokor.common.app.hr.model.address.Commune;
import com.nokor.common.app.hr.model.address.District;
import com.nokor.common.app.hr.model.address.Province;
import com.nokor.common.app.hr.model.eref.ECivility;
import com.nokor.common.app.hr.model.eref.ELanguage;
import com.nokor.common.app.hr.model.eref.EMaritalStatus;
import com.nokor.common.app.hr.model.eref.ENationality;
import com.nokor.common.app.hr.model.eref.ETypeAddress;
import com.nokor.common.app.hr.model.eref.ETypeContactInfo;
import com.nokor.common.app.hr.model.eref.ETypeIdNumber;
import com.nokor.efinance.core.address.model.Address;
import com.nokor.efinance.core.address.model.EResidenceStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.ApplicantAddress;
import com.nokor.efinance.core.applicant.model.ApplicantContactInfo;
import com.nokor.efinance.core.applicant.model.EEducation;
import com.nokor.efinance.core.applicant.model.EEmploymentType;
import com.nokor.efinance.core.applicant.model.EReligion;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.common.organization.model.EEmploymentCategory;
import com.nokor.frmk.testing.BaseTestCase;
/**
 * 
 * @author buntha.chea
 *
 */
public class Guarantor extends BaseTestCase {

	ApplicantService applicantService;
	File file;
	String line = "";
	String cvsSplitBy = "\\|";
	int countLineNotEnought = 0;
	public Guarantor() {
		//
	}
	
	/**
	 * Migration PersoncalInformation Of Lessee
	 */
	public void testPersonalInformation() {
		
		String s = "210|System ID|ID type|IDNumber|Issuing ID date|Expiring ID date|Customer ID|Prefix|First name |Last name|Nickname|DateOfBirth|Status|Children|Education|Household|Nationality|Religion|Second language|Household Expenses|Household Income";
		int length = s.split(cvsSplitBy).length;
		
		try {
			applicantService = getBean(ApplicantService.class);
			file = readFile("210-1-201507211636.csv");
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i< lines.size(); i++) {
				String[] lesseesData = lines.get(i).split(cvsSplitBy, -1);
				if ("210".equals(lesseesData[0]) && !"System ID".equals(lesseesData[1])) {
					if (length == lesseesData.length) {
						Applicant lessee = getApplicantByReference(lesseesData[6]);
						if (lessee == null) {
							lessee = new Applicant();
						}
						lessee.setMigrationID(lesseesData[1]);
						lessee.setTypeIdNumber(ETypeIdNumber.getByCode(lesseesData[2]));
						lessee.setIdNumber(lesseesData[3]);
						lessee.setIssuingIdNumberDate(StringUtils.isEmpty(lesseesData[4]) ? null : DateUtils.getDate(lesseesData[4] + ":00", "dd.MM.yyyy HH:mm:ss"));
						lessee.setExpiringIdNumberDate(StringUtils.isEmpty(lesseesData[5]) ? null : DateUtils.getDate(lesseesData[5] + ":00", "dd.MM.yyyy HH:mm:ss"));
						lessee.setReference(lesseesData[6]);
						lessee.setCivility(ECivility.getByCode(lesseesData[7]));
						lessee.setFirstNameEn(lesseesData[8]);
						lessee.setLastNameEn(lesseesData[9]);
						lessee.setNickName(lesseesData[10]);
						lessee.setBirthDate(StringUtils.isEmpty(lesseesData[11]) ? null : DateUtils.getDate(lesseesData[11] + ":00", "dd.MM.yyyy HH:mm:ss"));
						lessee.setMaritalStatus(EMaritalStatus.getByCode(lesseesData[12]));
						lessee.setNumberOfChildren(StringUtils.isEmpty(lesseesData[13]) ? 0 : Integer.valueOf(lesseesData[13]));
						lessee.setEducation(EEducation.getByCode(lesseesData[14]));
						lessee.setNumberOfHousehold(StringUtils.isEmpty(lesseesData[15]) ? 0 : Integer.valueOf(lesseesData[15]));
						lessee.setNationality(ENationality.getByCode(lesseesData[16]));
						lessee.setReligion(EReligion.getByCode(lesseesData[17]));
						lessee.setSecondLanguage(ELanguage.getByCode(ELanguage.class, lesseesData[18]));
						lessee.setHouseholdExpenses(StringUtils.isEmpty(lesseesData[19]) ? 0d : Double.valueOf(lesseesData[19]));
						lessee.setHouseholdIncome(StringUtils.isEmpty(lesseesData[20]) ? 0d : Double.valueOf(lesseesData[20]));
						//applicantService.saveOrUpdate(lessee);
					}else {
						logger.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  lesseesData.length + ", expected length =" + length);
					}
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Migration FinancialInformation (Employment) of Lessee 
	 */
	public void xxtestFinancialInformation() {
		
		String s = "211|System ID|Income Type|Category Income|Name company|Job title|Working period(year)|Working period(month)|Income 1|Number / Building|Moo|Soi|Street|Province|District|Subdistrict|Postal Code|Phone Company";
		int length = s.split(cvsSplitBy).length;
		
		try {
			applicantService = getBean(ApplicantService.class);
			file = readFile("211-1-201507211636.csv");
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i< lines.size(); i++) {
				String[] financialDatas = lines.get(i).split(cvsSplitBy, -1);
				if ("211".equals(financialDatas[0]) && !"System ID".equals(financialDatas[1])) {
					if (length == financialDatas.length) {
						Applicant lessee = getApplicantByMigrationID(financialDatas[1]);
						if (lessee == null) {
							lessee = new Applicant();
							lessee.setMigrationID(financialDatas[1]);
							applicantService.saveOrUpdate(lessee);
						}
						//New Employment
						Employment employment = new Employment();
						employment.setApplicant(lessee);
						employment.setEmploymentType(EEmploymentType.getByCode(financialDatas[2]));
						employment.setEmploymentCategory(EEmploymentCategory.getByCode(financialDatas[3]));
						//employment.setLegalForm(ELegalForm.getByCode(financialDatas[4]));
						employment.setEmployerName(financialDatas[4]);
						employment.setPosition(financialDatas[5]);
						//employment.setTimeWithEmployerInYear(StringUtils.isEmpty(financialDatas[6]) ? 0 : Integer.valueOf(financialDatas[6]));
						//employment.setTimeWithEmployerInMonth(StringUtils.isEmpty(financialDatas[7]) ? 0 : Integer.valueOf(financialDatas[7]));
						
						//some row get data "8,000.00"
						NumberFormat format = NumberFormat.getInstance(Locale.US);
						double revenue = format.parse(financialDatas[8]).doubleValue(); 
						employment.setRevenue(StringUtils.isEmpty(financialDatas[8]) ? 0d : revenue);
						
						//address of employment 
						Address empAddress = new Address();
						
						//House No Incresse charater contain in DB form 30 -> 100
						empAddress.setHouseNo(financialDatas[9]);
						empAddress.setLine1(financialDatas[10]);
						empAddress.setLine2(financialDatas[11]);
						empAddress.setStreet(financialDatas[12]);
						empAddress.setProvince(applicantService.getByCode(Province.class, financialDatas[13]));
						empAddress.setDistrict(applicantService.getByCode(District.class, financialDatas[14]));
						empAddress.setCommune(applicantService.getByCode(Commune.class, financialDatas[15]));
						empAddress.setPostalCode(financialDatas[16]);
						//applicantService.saveOrUpdate(empAddress);
						
						//work phone Incresse charater contain in DB form 30 -> 100
						employment.setWorkPhone(financialDatas[17]);
						employment.setAddress(empAddress);
						//applicantService.saveOrUpdate(employment);
					} else {
						logger.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  financialDatas.length + ", expected length =" + length);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void xxtestAddressInformation() {
		
		String s = "212|System ID|Address type|Number / Building|Moo|Soi|Street|Province|District|Subdistrict|Postal Code|Living period(year)|Living period(month)|Residence status";
		int length = s.split(cvsSplitBy).length;
		
		try {
			applicantService = getBean(ApplicantService.class);
			file = readFile("212-1-201507211636.csv");
			
			List<String> lines = FileUtils.readLines(file);
			for (int i=0; i<lines.size(); i++) {
				String[] addressInfoDatas = lines.get(i).split(cvsSplitBy, -1);
				if ("212".equals(addressInfoDatas[0]) && !"System ID".equals(addressInfoDatas[1])) {
					if (length == addressInfoDatas.length) {
						Applicant lessee = getApplicantByMigrationID(addressInfoDatas[1]);
						if (lessee == null) {
							lessee = new Applicant();
							lessee.setMigrationID(addressInfoDatas[1]);
							applicantService.saveOrUpdate(lessee);
						}
						Address appAddress = new Address();
						appAddress.setType(ETypeAddress.getByCode(addressInfoDatas[2]));
						appAddress.setHouseNo(addressInfoDatas[3]);
						appAddress.setLine1(addressInfoDatas[4]);
						appAddress.setLine2(addressInfoDatas[5]);
						appAddress.setStreet(addressInfoDatas[6]);
						appAddress.setProvince(applicantService.getByCode(Province.class, addressInfoDatas[7]));
						appAddress.setDistrict(applicantService.getByCode(District.class, addressInfoDatas[8]));
						appAddress.setCommune(applicantService.getByCode(Commune.class, addressInfoDatas[9]));
						appAddress.setPostalCode(addressInfoDatas[10]);
						appAddress.setTimeAtAddressInYear(StringUtils.isEmpty(addressInfoDatas[11]) ? 0 : Integer.valueOf(addressInfoDatas[11]));
						appAddress.setTimeAtAddressInMonth(StringUtils.isEmpty(addressInfoDatas[12]) ? 0 : Integer.valueOf(addressInfoDatas[12]));
						appAddress.setResidenceStatus(EResidenceStatus.getByCode(EResidenceStatus.class, addressInfoDatas[13]));
						//applicantService.saveOrUpdate(appAddress);
						
						ApplicantAddress applicantAddress = new ApplicantAddress();
						applicantAddress.setApplicant(lessee);
						applicantAddress.setAddress(appAddress);
						//applicantService.saveOrUpdate(applicantAddress);
					} else {
						logger.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  addressInfoDatas.length + ", expected length =" + length);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public void xxtestContactInformation() {
		
		String s = "213|System ID|Contact info type|Address type|Contact info value";
		int length = s.split(cvsSplitBy).length;
		
		try {
			applicantService = getBean(ApplicantService.class);
			file = readFile("213-1-201507211636.csv");
			
			List<String> lines = FileUtils.readLines(file);
			for (int i=0; i<lines.size(); i++) {
				String[] contactInfoDatas = lines.get(i).split(cvsSplitBy, -1);
				if ("213".equals(contactInfoDatas[0]) && !"System ID".equals(contactInfoDatas[1])) {
					if (length == contactInfoDatas.length) {
						Applicant lessee = getApplicantByMigrationID(contactInfoDatas[1]);
						if (lessee == null) {
							lessee = new Applicant();
							lessee.setMigrationID(contactInfoDatas[1]);
							applicantService.saveOrUpdate(lessee);
						}
						ContactInfo contactInfo = new ContactInfo();
						contactInfo.setTypeInfo(ETypeContactInfo.getByCode(contactInfoDatas[2]));
						contactInfo.setTypeAddress(ETypeAddress.getByCode(contactInfoDatas[3]));
						contactInfo.setValue(contactInfoDatas[4]);
						//applicantService.saveOrUpdate(contactInfo);
						
						ApplicantContactInfo applicantContactInfo = new ApplicantContactInfo();
						applicantContactInfo.setApplicant(lessee);
						applicantContactInfo.setContactInfo(contactInfo);
						//applicantService.saveOrUpdate(applicantContactInfo);
					} else {
						logger.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  contactInfoDatas.length + ", expected length =" + length);
					}
				}
			
			
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param reference
	 * @return
	 */
	private Applicant getApplicantByReference(String reference) {
		BaseRestrictions<Applicant> restrictions = new BaseRestrictions<>(Applicant.class);
		restrictions.addCriterion(Restrictions.eq("reference", reference));
		List<Applicant> applicants = applicantService.list(restrictions);
		if (applicants.isEmpty()) {
			return null;
		}
		Applicant applicant = applicants.get(0);
		return applicant;
	}
	
	private Applicant getApplicantByMigrationID(String migrationId) {
		BaseRestrictions<Applicant> restrictions = new BaseRestrictions<>(Applicant.class);
		restrictions.addCriterion(Restrictions.eq("migrationID", migrationId));
		List<Applicant> applicants = applicantService.list(restrictions);
		if (applicants.isEmpty()) {
			return null;
		}
		Applicant applicant = applicants.get(0);
		return applicant;
	}
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	private File readFile(String fileName) {
		String url = "/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v2";
		File f = new File(url + "/" + fileName);
		return f;
	}
}

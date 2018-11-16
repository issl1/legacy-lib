package com.nokor.efinance.migration.guarantor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.migration.model.guarantor.GuarantorAddress;
/**
 * 
 * @author buntha.chea
 *
 */
public class GuarantorAddressInformation {
	
	private Logger LOG = LoggerFactory.getLogger(GuarantorAddressInformation.class);
	private List<String> errors = new ArrayList<>();
	List<GuarantorAddress> guarantorAddresses = new ArrayList<>();
	
	public static void main(String[] args) {
		GuarantorAddressInformation addressInformation = new GuarantorAddressInformation();
		//File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/212-1-201507211636.csv");
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/212-1-201507211636.csv");
		addressInformation.validateData(f);
	}
	
	/**
	 * 
	 * @param url
	 * @param fileName
	 */
	public List<GuarantorAddress> validateData(File file) {
		String cvsSplitBy = "\\|";
		
		String s = "212|System ID|Address type|Number/Building|Moo|Soi|Street|Province|District|Subdistrict|Postal Code|Living period(year)|Living period(month)|Residence status";
		int length = s.split(cvsSplitBy).length;
		
		try {
			
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] guaAddressDatas = lines.get(i).split(cvsSplitBy, -1);
				if ("212".equals(guaAddressDatas[0]) && !"System ID".equals(guaAddressDatas[1])) {
					if (length == guaAddressDatas.length) {		
						GuarantorAddress guarantorAddress = new GuarantorAddress();
						guarantorAddress.setMigrationID(guaAddressDatas[1]);
						guarantorAddress.setTypeAddressCode(guaAddressDatas[2]);
						guarantorAddress.setHouseNo(StringUtils.isEmpty(guaAddressDatas[3]) ? "xxx" : guaAddressDatas[3]);
						guarantorAddress.setLine1(guaAddressDatas[4]);
						guarantorAddress.setLine2(guaAddressDatas[5]);
						guarantorAddress.setStreet(StringUtils.isEmpty(guaAddressDatas[6]) ? "xxx" : guaAddressDatas[6]);
						guarantorAddress.setProvinceCode(guaAddressDatas[7]);
						guarantorAddress.setDistrictCode(guaAddressDatas[8]);
						guarantorAddress.setCommuneCode(guaAddressDatas[9]);
						guarantorAddress.setPostalCode(guaAddressDatas[10]);
						guarantorAddress.setTimeAtAddressInYear(StringUtils.isEmpty(guaAddressDatas[11]) ? 0 : Integer.valueOf(guaAddressDatas[11]));
						guarantorAddress.setTimeAtAddressInMonth(StringUtils.isEmpty(guaAddressDatas[12]) ? 0 : Integer.valueOf(guaAddressDatas[12]));
						guarantorAddress.setResidenceStatusCode(guaAddressDatas[13]);
						this.validateAddress(guarantorAddress, i + 1);
						guarantorAddresses.add(guarantorAddress);
					} else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  guaAddressDatas.length + ", expected length =" + length);
						errors.add("");
					}
				}
			}
			if (errors.isEmpty()) {
				LOG.info("Validate Successfuly!!!");
				return guarantorAddresses;
			} else {
				for (String erros : errors) {
					LOG.error(erros);
				}
			}
		} catch (Exception e) {
			LOG.error("Exception => ", e);
		}
		guarantorAddresses.clear();
		return guarantorAddresses;
	}
	/**
	 * 
	 * @param guarantorAddress
	 * @param line
	 */
	public void validateAddress(GuarantorAddress guarantorAddress, int line) {
		String message = "";
		if (StringUtils.isEmpty(guarantorAddress.getHouseNo())) {
			message += "_HOUSE_NO_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorAddress.getStreet())) {
			message += "_STREET_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorAddress.getProvinceCode())) {
			message += "_PROVINCE_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorAddress.getDistrictCode())) {
			message += "_DISTRICT_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorAddress.getCommuneCode())) {
			message += "_SUB_DISTRICT_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorAddress.getPostalCode())) {
			message += "_POSTAL_CODE_MANDATORY|";
		}
		if (guarantorAddress.getTimeAtAddressInYear() == null) {
			message += "_LIVING_PERIOD_IN_YEAR_MANDATORY|";
		}
		if (guarantorAddress.getTimeAtAddressInYear() == null) {
			message += "_LIVING_PERIOD_IN_MONTH_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorAddress.getResidenceStatusCode())) {
			message += "_RESIDENCE_STATUS_CODE_MANDATORY";
		}
		if (!StringUtils.isEmpty(message)) {
			errors.add("LINE : " + line + " " + "Guarantor_Address" +" "+ message);
		}
	}
}

package com.nokor.efinance.migration.lessee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nokor.efinance.migration.model.lessee.LesseeAddress;
/**
 * 
 * @author buntha.chea
 *
 */
public class AddressInformation {
	
	private Logger LOG = LoggerFactory.getLogger(AddressInformation.class);
	private List<String> errors = new ArrayList<>();
	private List<LesseeAddress> lesseeAddresses = new ArrayList<>();

	public static void main(String[] args) {
		AddressInformation addressInformation = new AddressInformation();
		//File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/013-1-201507211636.csv");
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/012-1-201507211636.csv");
		addressInformation.validateData(f);
	}
	/**
	 * 
	 * @param url
	 * @param fileName
	 */
	public List<LesseeAddress> validateData(File file) {
		String cvsSplitBy = "\\|";
		
		String s = "012|System ID|Address type|Number / Building|Moo|Soi|Street|Province|District|Subdistrict|Postal Code|Living period(year)|Living period(month)|Residence status";
		int length = s.split(cvsSplitBy).length;
		
		try {
			
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] addressInfoData = lines.get(i).split(cvsSplitBy, -1);
				if ("012".equals(addressInfoData[0]) && !"System ID".equals(addressInfoData[1])) {
					if (length == addressInfoData.length) {
						LesseeAddress lesseeAddress = new LesseeAddress();
						lesseeAddress.setMigrationID(addressInfoData[1]);
						lesseeAddress.setTypeAddressCode(addressInfoData[2]);
						lesseeAddress.setHouseNo(StringUtils.isEmpty(addressInfoData[3]) ? "xxx" : addressInfoData[3]);
						lesseeAddress.setLine1(addressInfoData[4]);
						lesseeAddress.setLine2(addressInfoData[5]);
						lesseeAddress.setStreet(StringUtils.isEmpty(addressInfoData[6]) ? "xxx" : addressInfoData[6]);
						lesseeAddress.setProvinceCode(addressInfoData[7]);
						lesseeAddress.setDistrictCode(addressInfoData[8]);
						lesseeAddress.setCommuneCode(addressInfoData[9]);
						lesseeAddress.setPostalCode(addressInfoData[10]);
						lesseeAddress.setTimeAtAddressInYear(StringUtils.isEmpty(addressInfoData[11]) ? 0 : Integer.valueOf(addressInfoData[11]));
						lesseeAddress.setTimeAtAddressInMonth(StringUtils.isEmpty(addressInfoData[12]) ? 0 : Integer.valueOf(addressInfoData[12]));
						lesseeAddress.setResidenceStatusCode(addressInfoData[13]);
						this.validateAddress(lesseeAddress, i + 1);
						lesseeAddresses.add(lesseeAddress);
					} else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  addressInfoData.length + ", expected length =" + length);
						errors.add("");
					}
				}
			}
			if (errors.isEmpty()) {
				LOG.info("Validate Success!!");
				return lesseeAddresses;
			} else {
				for (String error : errors) {
					LOG.error(error);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param lesseeAddress
	 * @param line
	 */
	public void validateAddress(LesseeAddress lesseeAddress, int line) {
		String message = "";
		if (StringUtils.isEmpty(lesseeAddress.getHouseNo())) {
			message += "_HOUSE_NO_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeAddress.getStreet())) {
			message += "_STREET_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeAddress.getProvinceCode())) {
			message += "_PROVINCE_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeAddress.getDistrictCode())) {
			message += "_DISTRICT_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeAddress.getCommuneCode())) {
			message += "_SUB_DISTRICT_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeAddress.getPostalCode())) {
			message += "_POSTAL_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeAddress.getTimeAtAddressInYear())) {
			message += "_LIVING_PERIOD_IN_YEAR_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeAddress.getTimeAtAddressInYear())) {
			message += "_LIVING_PERIOD_IN_MONTH_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeAddress.getResidenceStatusCode())) {
			message += "_RESIDENCE_STATUS_CODE_MANDATORY";
		}
		if (!StringUtils.isEmpty(message)) {
			errors.add("LINE : " + line + " " + "LESSEE-ADDRESS" +" "+ message);
		}
	}
	
}

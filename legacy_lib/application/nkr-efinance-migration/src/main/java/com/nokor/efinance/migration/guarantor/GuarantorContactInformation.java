package com.nokor.efinance.migration.guarantor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nokor.efinance.migration.model.guarantor.GuarantorContactInfo;
/**
 * 
 * @author buntha.chea
 *
 */
public class GuarantorContactInformation {
	
	private Logger LOG = LoggerFactory.getLogger(GuarantorContactInformation.class);
	private List<String> errors = new ArrayList<>();
	private List<GuarantorContactInfo> guarantorContactInfos = new ArrayList<>();
	
	public static void main(String[] args) {
		GuarantorContactInformation contactInformation = new GuarantorContactInformation();
		//File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/213-1-201507211636.csv");
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/213-1-201507211636.csv");
		contactInformation.validateData(f);
	}
	/**
	 * 
	 * @param file
	 * @return
	 */
	public List<GuarantorContactInfo> validateData(File file) {
		String cvsSplitBy = "\\|";
		
		String s = "213|System ID|Contact info type|Address type|Contact info value";
		int length = s.split(cvsSplitBy).length;
		
		try {
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] guacontactInfoDatas = lines.get(i).split(cvsSplitBy, -1);
				if ("213".equals(guacontactInfoDatas[0]) && !"System ID".equals(guacontactInfoDatas[1])) {
					if (length == guacontactInfoDatas.length) {
						GuarantorContactInfo guarantorContactInfo = new GuarantorContactInfo();
						guarantorContactInfo.setMigrationID(guacontactInfoDatas[1]);
						guarantorContactInfo.setTypeInfoCode(guacontactInfoDatas[2]);
						guarantorContactInfo.setTypeAddressCode(guacontactInfoDatas[3]);
						guarantorContactInfo.setValue(guacontactInfoDatas[4]);
						this.validateContactInfo(guarantorContactInfo, i + 1);
						guarantorContactInfos.add(guarantorContactInfo);
					} else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  guacontactInfoDatas.length + ", expected length =" + length);
						errors.add("");
					}
				}
			}
			if (errors.isEmpty()) {
				LOG.info("Validate Successfuly!!");
				return guarantorContactInfos;
			} else {
				for (String error : errors) {
					LOG.error(error);
				}
			}
		} catch (Exception e) {
			LOG.error("Exception => ", e);
		}
		guarantorContactInfos.clear();
		return guarantorContactInfos;
	}
	/**
	 * 
	 * @param guarantorContactInfo
	 * @param line
	 */
	public void validateContactInfo(GuarantorContactInfo guarantorContactInfo, int line) {
		String message = "";
		if (StringUtils.isEmpty(guarantorContactInfo.getTypeInfoCode())) {
			message += "_TYPE_INFO_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(guarantorContactInfo.getTypeAddressCode())) {
			message += "_TYPE_INFO_VALUE_MANDATORY|";
		}
		if (!StringUtils.isEmpty(message)) {
			errors.add("LINE : " + line + " " + "Guarantor_ContactInfo" +" "+ message);
		}
	}
}

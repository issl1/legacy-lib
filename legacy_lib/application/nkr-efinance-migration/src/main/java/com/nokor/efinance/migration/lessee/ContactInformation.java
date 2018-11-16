package com.nokor.efinance.migration.lessee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nokor.efinance.migration.model.lessee.LesseeContactInfo;
/**
 * 
 * @author buntha.chea
 *
 */
public class ContactInformation {
	
	private Logger LOG = LoggerFactory.getLogger(ContactInformation.class);
	private List<String> errors = new ArrayList<>();
	private List<LesseeContactInfo> lesseeContactInfos = new ArrayList<>();
	
	public static void main(String[] args) {
		ContactInformation contactInformation = new ContactInformation();
		//File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/013-1-201507211636.csv");
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/013-1-201507211636.csv");
		contactInformation.validateData(f);
	}

	/**
	 * 
	 * @param url
	 * @param fileName
	 */
	public List<LesseeContactInfo> validateData(File file) {
		String cvsSplitBy = "\\|";
		
		String s = "013|System ID|Contact info type|Address type|Contact info value";
		int length = s.split(cvsSplitBy).length;
		
		try {
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] contactInfoData = lines.get(i).split(cvsSplitBy, -1);
				if ("013".equals(contactInfoData[0]) && !"System ID".equals(contactInfoData[1])) {
					if (length == contactInfoData.length) {
						LesseeContactInfo lesseeContactInfo = new LesseeContactInfo();
						lesseeContactInfo.setMigrationID(contactInfoData[1]);
						lesseeContactInfo.setTypeInfoCode(contactInfoData[2]);
						lesseeContactInfo.setTypeAddressCode(contactInfoData[3]);
						lesseeContactInfo.setValue(contactInfoData[4]);
						this.validateContactInfo(lesseeContactInfo, i + 1);
						lesseeContactInfos.add(lesseeContactInfo);
					} else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  contactInfoData.length + ", expected length =" + length);
						errors.add("");
					}
				}
			}
			if (errors.isEmpty()) {
				LOG.info("Validate Success!!");
				return lesseeContactInfos;
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
	 * @param lesseeContactInfo
	 * @param line
	 */
	public void validateContactInfo(LesseeContactInfo lesseeContactInfo, int line) {
		String message = "";
		if (StringUtils.isEmpty(lesseeContactInfo.getTypeInfoCode())) {
			message += "_TYPE_INFO_CODE_MANDATORY|";
		}
		if (StringUtils.isEmpty(lesseeContactInfo.getTypeAddressCode())) {
			message += "_TYPE_INFO_VALUE_MANDATORY|";
		}
		if (!StringUtils.isEmpty(message)) {
			errors.add("LINE : " + line + " " + "Contact-Info" +" "+ message);
		}
	}
}

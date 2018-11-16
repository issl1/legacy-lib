package com.nokor.efinance.migration.lessee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.migration.model.lessee.LesseeReferenceInfo;
/**
 * 
 * @author buntha.chea
 *
 */
public class ReferenceInformation {

	private Logger LOG = LoggerFactory.getLogger(ContactInformation.class);
	private List<LesseeReferenceInfo> lesseeReferenceInfos = new ArrayList<>();
	private List<String> errors = new ArrayList<>();
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ReferenceInformation referenceInformation = new ReferenceInformation();
		File f = new File("D:/work/workspace-nkf/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/014-1-201507211636.csv");
		referenceInformation.validateData(f);
	}

	/**
	 * 
	 * @param url
	 * @param fileName
	 */
	public List<LesseeReferenceInfo> validateData(File file) {
		String cvsSplitBy = "\\|";
		
		String s = "014|Lessee ID|Reference Type|Reference relationship|Reference  firstname|Reference  lastname|Contact info type|Address type|Contact info value";
		int length = s.split(cvsSplitBy).length;
		
		try {
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] lesseeReferenceInfoData = lines.get(i).split(cvsSplitBy, -1);
				if ("014".equals(lesseeReferenceInfoData[0])  && !"System ID".equals(lesseeReferenceInfoData[1])) {
					if (length == lesseeReferenceInfoData.length) {
						//errors = ValidateDataInCSV.validateReference("LESSEE-REFERENCE", lesseeReferenceInfoData, i+1);
						LesseeReferenceInfo lesseeReferenceInfo = new LesseeReferenceInfo();
						lesseeReferenceInfo.setMigrationID(lesseeReferenceInfoData[1]);
						lesseeReferenceInfo.setReferenceTypeCode(lesseeReferenceInfoData[2]);
						lesseeReferenceInfo.setRelationshipCode(lesseeReferenceInfoData[3]);
						lesseeReferenceInfo.setFirstName(lesseeReferenceInfoData[4]);
						lesseeReferenceInfo.setLastName(lesseeReferenceInfoData[5]);
						
						lesseeReferenceInfo.setTypeInfoCode(lesseeReferenceInfoData[6]);
						lesseeReferenceInfo.setTypeAddressCode(lesseeReferenceInfoData[7]);
						lesseeReferenceInfo.setValue(lesseeReferenceInfoData[8]);
						lesseeReferenceInfos.add(lesseeReferenceInfo);
					} else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  lesseeReferenceInfoData.length + ", expected length =" + length);
						errors.add("");
					}
				}
			}
			if (errors.isEmpty()) {
				LOG.info("Validate Success!!!");
				return lesseeReferenceInfos;
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
}

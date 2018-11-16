package com.nokor.efinance.migration.loan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.migration.lessee.PersonalInformation;
import com.nokor.efinance.migration.model.loan.MDocument;
import com.nokor.efinance.migration.spouse.SpousePersonalInformation;
/**
 * 
 * @author buntha.chea
 *
 */
public class QuoDocument {
	
	private Logger LOG = LoggerFactory.getLogger(SpousePersonalInformation.class);
	private List<String> errors = new ArrayList<>();
	private List<MDocument> documents = new ArrayList<>();
	
	public static void main(String[] args) {
		QuoDocument quoDocument = new QuoDocument();
		File f = new File("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Migration/CSV/v3/312-1-201507211636.csv");
		quoDocument.validateData(f);
	}
	/**
	 * 
	 * @param file
	 * @return
	 */
	public List<MDocument> validateData(File file) {
		String cvsSplitBy = "\\|";
		String s = "312|System ID|Document code|Document Path";
		int length = s.split(cvsSplitBy).length;
		try {
			List<String> lines = FileUtils.readLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String[] documentDatas = lines.get(i).split(cvsSplitBy, -1);
				if ("312".equals(documentDatas[0]) && !"System ID".equals(documentDatas[1])) {
					if (length == documentDatas.length) {
						MDocument document = new MDocument();
						document.setMigrationID(documentDatas[1]);
						document.setDocumentCode(documentDatas[2]);
						document.setDocumentPath(documentDatas[3]);
						documents.add(document);
					} else {
						LOG.warn("Line = " + (i + 1) + ", not valid length -> current length = " +  documentDatas.length + ", expected length =" + length);
						errors.add("");
					}
				}
			}
			if (errors.isEmpty()) {
				LOG.info("Validate Success");
				return documents;
			}
		} catch (Exception e) {
			LOG.error("Exception => ", e);
		}
		documents.clear();
		return documents;
	}
	
}

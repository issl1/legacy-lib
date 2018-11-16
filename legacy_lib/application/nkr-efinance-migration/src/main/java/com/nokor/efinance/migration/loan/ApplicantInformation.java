package com.nokor.efinance.migration.loan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.EntityService;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
/**
 * 
 * @author buntha.chea
 *
 */
public class ApplicantInformation {
	
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	public static void main(String[] args) {
		ApplicantInformation applicantInformation = new ApplicantInformation();
		applicantInformation.countFileInDirectory();
	}
	
	/**
	 * Count File CSV in folder and migration 
	 */
	public void countFileInDirectory() {
		String fileName = "";
		String url = "/home/buntha.chea/WORK/Document/MyBug-GLF-Thai/Test migration";
		int i = 1;
		
		File[] files = new File(url).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				fileName = file.getName();
				if (fileName.startsWith("311-".concat(String.valueOf(i))) && fileName.endsWith(".csv")) {
					this.run(url, fileName);
					i++;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param url
	 * @param fileName
	 */
	public void run(String url, String fileName) {

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "\\|";
		
		try {
			br = new BufferedReader(new FileReader(url + "/" + fileName));
			while ((line = br.readLine()) != null) {
	 
				String[] applicantInfoData = line.split(cvsSplitBy);
				if ("311".equals(applicantInfoData[0])) {
					Quotation quotation = getQuotationByApplicationId(applicantInfoData[1]);
					if (quotation == null) {
						quotation = new Quotation();
						quotation.setWkfStatus(EWkfStatus.getByCode("NEW"));
						quotation.setExternalReference(applicantInfoData[1]);
					}
					//Lessee
					Applicant lessee = getApplicantByReference(applicantInfoData[2]);
					if (lessee == null) {
						lessee = new Applicant();
					}
					lessee.setReference(applicantInfoData[2]);
					quotation.setApplicant(lessee);
					quotation.setExternalReference(applicantInfoData[1]);
					entityService.saveOrUpdate(lessee);
					entityService.saveOrUpdate(quotation);
					
					//Spouse
					Applicant spouse = getApplicantByReference(applicantInfoData[3]);
					if (spouse == null) {
						spouse = new Applicant();
					}
					spouse.setReference(applicantInfoData[3]);
					entityService.saveOrUpdate(spouse);
					saveQuotationApplicant(quotation, spouse, EApplicantType.S);
					
					
					//Guarantor
					Applicant guarantor = getApplicantByReference(applicantInfoData[4]);
					if (guarantor == null) {
						guarantor = new Applicant();
					}
					guarantor.setReference(applicantInfoData[4]);
					entityService.saveOrUpdate(guarantor);
					saveQuotationApplicant(quotation, guarantor, EApplicantType.G);
					
				}
			}
	 
		} catch (FileNotFoundException e) {
			 Notification.show("Could not open file<br/>", e.getMessage(), Type.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 
	 * @param applicationId
	 * @return
	 */
	private Quotation getQuotationByApplicationId(String applicationId) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq("externalReference", applicationId));
		List<Quotation> quotations = entityService.list(restrictions);
		if (quotations.isEmpty()) {
			return null;
		}
		Quotation quotation = quotations.get(0);
		return quotation;
	}
	/**
	 * 
	 * @param reference
	 * @return
	 */
	private Applicant getApplicantByReference(String reference) {
		BaseRestrictions<Applicant> restrictions = new BaseRestrictions<>(Applicant.class);
		restrictions.addCriterion(Restrictions.eq("reference", reference));
		List<Applicant> applicants = entityService.list(restrictions);
		if (applicants.isEmpty()) {
			return null;
		}
		Applicant applicant = applicants.get(0);
		return applicant;
	}
	/**
	 * 
	 * @param quotation
	 * @param applicant
	 * @param eApplicantType
	 */
	private void saveQuotationApplicant(Quotation quotation, Applicant applicant, EApplicantType eApplicantType) {
		QuotationApplicant quotationApplicant = new QuotationApplicant();
		quotationApplicant.setQuotation(quotation);
		quotationApplicant.setApplicant(applicant);
		quotationApplicant.setApplicantType(eApplicantType);
		entityService.saveOrUpdate(quotationApplicant);
	}
}

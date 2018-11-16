package com.nokor.efinance.core.applicant.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.CompanyContactInfo;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;

/**
 * 
 * @author uhout.cheng
 */
public class CompanyContactInfoUtils {

	/**
	 * 
	 * @param company
	 * @return
	 */
	public static String getPrimaryContactInfo(Company company) {
		List<CompanyContactInfo> contactInfos = company.getCompanyContactInfos();
		if (contactInfos != null && !contactInfos.isEmpty()) {
			for (CompanyContactInfo contactInfo : contactInfos) {
				String primaryContact = getPrimaryContactInfo(contactInfo.getContactInfo());
				if (StringUtils.isNotEmpty(primaryContact)) {
					return primaryContact;
				}
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @param info
	 * @return
	 */
	private static String getPrimaryContactInfo(ContactInfo info) {
		if (info.isPrimary()) {
			if (ETypeContactInfo.LANDLINE.equals(info.getTypeInfo()) 
					|| ETypeContactInfo.MOBILE.equals(info.getTypeInfo())) {
				return info.getValue() == null ? "" : info.getValue();
			}
		} 
		return StringUtils.EMPTY;
	}
}

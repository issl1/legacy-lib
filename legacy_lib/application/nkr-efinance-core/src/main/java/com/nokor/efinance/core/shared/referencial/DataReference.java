package com.nokor.efinance.core.shared.referencial;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.DocumentGroup;
import com.nokor.efinance.core.quotation.model.ProfileDefaultQuotationStatus;
import com.nokor.efinance.core.quotation.model.SupportDecision;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.ersys.core.hr.model.PublicHoliday;
import com.nokor.frmk.security.model.SecUser;

/**
 * Data Reference
 * @author ly.youhort
 *
 */
public final class DataReference {

	private List<Document> documents  = new ArrayList<>();
	private List<DocumentGroup> documentGroups = new ArrayList<>();
	private List<Dealer> dealers = new ArrayList<>();
	private List<SupportDecision> supportDecisions = new ArrayList<>();	
	private List<SecUser> users = new ArrayList<>();
	private List<PublicHoliday> publicHoliday = new ArrayList<>();

	private List<ProfileDefaultQuotationStatus> profileDefaultQuotationStatus = new ArrayList<>();
			
	private DataReference() {}
	
	/** Holder */
	private static class SingletonHolder {
		private final static DataReference instance = new DataReference();
	}
	 
	/**
	 * @return
	 */
	public static DataReference getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * @return the documents
	 */
	public List<Document> getDocuments() {
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	
	/**
	 * @return the documentGroups
	 */
	public List<DocumentGroup> getDocumentGroups() {
		return documentGroups;
	}

	/**
	 * @param documentGroups the documentGroups to set
	 */
	public void setDocumentGroups(List<DocumentGroup> documentGroups) {
		this.documentGroups = documentGroups;
	}

	


	/**
	 * @return the dealers
	 */
	public List<Dealer> getDealers() {
		return dealers;
	}

	/**
	 * @param dealers the dealers to set
	 */
	public void setDealers(List<Dealer> dealers) {
		this.dealers = dealers;
	}

	

	/**
	 * @return the supportDecisions
	 */
	public List<SupportDecision> getSupportDecisions() {
		return supportDecisions;
	}

	/**
	 * @param supportDecisions the supportDecisions to set
	 */
	public void setSupportDecisions(
			List<SupportDecision> supportDecisions) {
		this.supportDecisions = supportDecisions;
	}
	
	/**
	 * Get support decisions
	 * @param quotationStatus
	 * @return
	 */
	public List<SupportDecision> getSupportDecisions(EWkfStatus quotationStatus) {
		List<SupportDecision> resultList = new ArrayList<SupportDecision>();
		for (SupportDecision supportDecision : supportDecisions) {
			if (supportDecision.getWkfStatus() == quotationStatus) {
				resultList.add(supportDecision);
			}
		}
		return resultList;
	}
	

	/**
	 * @return the users
	 */
	public List<SecUser> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<SecUser> users) {
		this.users = users;
	}
	
	/**
	 * @param profileId
	 * @return
	 */
	public List<SecUser> getUsers(Long profileId, EStatusRecord statusRecord) {
		List<SecUser> usersByProfile = new ArrayList<SecUser>();
		for (SecUser user : this.users) {
			if (ProfileUtil.isProfileExist(profileId, user.getProfiles())) {
				if (statusRecord == null) {
					usersByProfile.add(user);
				} else if (user.getStatusRecord() == statusRecord){
					usersByProfile.add(user);
				}
			}
		}
		return usersByProfile;
	}

	/**
	 * 
	 * @param profileId
	 * @return
	 */
	public List<SecUser> getUsers(Long profileId) {
		return getUsers(profileId, null);
	}
	
	/**
	 * @return the publicHoliday
	 */
	public List<PublicHoliday> getPublicHoliday() {
		return publicHoliday;
	}

	/**
	 * @param publicHoliday the publicHoliday to set
	 */
	public void setPublicHoliday(List<PublicHoliday> publicHoliday) {
		this.publicHoliday = publicHoliday;
	}

	

	/**
	 * @return the profileDefaultQuotationStatus
	 */
	public List<ProfileDefaultQuotationStatus> getProfileDefaultQuotationStatus() {
		return profileDefaultQuotationStatus;
	}

	/**
	 * @param profileDefaultQuotationStatus the profileDefaultQuotationStatus to set
	 */
	public void setProfileDefaultQuotationStatus(
			List<ProfileDefaultQuotationStatus> profileDefaultQuotationStatus) {
		this.profileDefaultQuotationStatus = profileDefaultQuotationStatus;
	}


}

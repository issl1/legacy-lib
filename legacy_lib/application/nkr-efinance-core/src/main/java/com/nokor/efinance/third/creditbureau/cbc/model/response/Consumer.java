package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;
import java.util.List;

import com.nokor.efinance.third.creditbureau.cbc.model.Cid;

/**
 * @author sun.vanndy
 *
 */
public class Consumer implements Serializable{

	private static final long serialVersionUID = -7437028049395068878L;
	
	private String capl;
	private Cid cid;
	private Provided provided;
	private Available available;
	private List<AdditionalId> additionalIds;
	private List<AdditionalName> additionalNames;
	private List<PrevEnquiry> prevEnquiries;
	private List<AccDetail> accDetails;
	private List<GrtAccDetail> guarantiedAccounts;
	private List<Loss> losses;
	private List<PublicNotice> publicNotices;
	private List<Narrative> narratives;
	private List<Address> addresses;
	private List<Employer> employers;
	private AdvisorySummaryText advisorySummaryText;
	private Summary summary;
		
	/**
	 * @return the capl
	 */
	public String getCapl() {
		return capl;
	}
	/**
	 * @param capl the capl to set
	 */
	public void setCapl(String capl) {
		this.capl = capl;
	}
	/**
	 * @return the cid
	 */
	public Cid getCid() {
		return cid;
	}
	/**
	 * @param cid the cid to set
	 */
	public void setCid(Cid cid) {
		this.cid = cid;
	}
	/**
	 * @return the provided
	 */
	public Provided getProvided() {
		return provided;
	}
	/**
	 * @param provided the provided to set
	 */
	public void setProvided(Provided provided) {
		this.provided = provided;
	}
	/**
	 * @return the available
	 */
	public Available getAvailable() {
		return available;
	}
	/**
	 * @param available the available to set
	 */
	public void setAvailable(Available available) {
		this.available = available;
	}
	/**
	 * @return the additionalIds
	 */
	public List<AdditionalId> getAdditionalIds() {
		return additionalIds;
	}
	/**
	 * @param additionalIds the additionalIds to set
	 */
	public void setAdditionalIds(List<AdditionalId> additionalIds) {
		this.additionalIds = additionalIds;
	}
	/**
	 * @return the additionalNames
	 */
	public List<AdditionalName> getAdditionalNames() {
		return additionalNames;
	}
	/**
	 * @param additionalNames the additionalNames to set
	 */
	public void setAdditionalNames(List<AdditionalName> additionalNames) {
		this.additionalNames = additionalNames;
	}
	/**
	 * @return the prevEnquiries
	 */
	public List<PrevEnquiry> getPrevEnquiries() {
		return prevEnquiries;
	}
	/**
	 * @param prevEnquiries the prevEnquiries to set
	 */
	public void setPrevEnquiries(List<PrevEnquiry> prevEnquiries) {
		this.prevEnquiries = prevEnquiries;
	}
	/**
	 * @return the accDetails
	 */
	public List<AccDetail> getAccDetails() {
		return accDetails;
	}
	/**
	 * @param accDetails the accDetails to set
	 */
	public void setAccDetails(List<AccDetail> accDetails) {
		this.accDetails = accDetails;
	}
	/**
	 * @return the guarantiedAccounts
	 */
	public List<GrtAccDetail> getGuarantiedAccounts() {
		return guarantiedAccounts;
	}
	/**
	 * @param guarantiedAccounts the guarantiedAccounts to set
	 */
	public void setGuarantiedAccounts(List<GrtAccDetail> guarantiedAccounts) {
		this.guarantiedAccounts = guarantiedAccounts;
	}
	/**
	 * @return the losses
	 */
	public List<Loss> getLosses() {
		return losses;
	}
	/**
	 * @param losses the losses to set
	 */
	public void setLosses(List<Loss> losses) {
		this.losses = losses;
	}
	/**
	 * @return the publicNotices
	 */
	public List<PublicNotice> getPublicNotices() {
		return publicNotices;
	}
	/**
	 * @param publicNotices the publicNotices to set
	 */
	public void setPublicNotices(List<PublicNotice> publicNotices) {
		this.publicNotices = publicNotices;
	}
	/**
	 * @return the narratives
	 */
	public List<Narrative> getNarratives() {
		return narratives;
	}
	/**
	 * @param narratives the narratives to set
	 */
	public void setNarratives(List<Narrative> narratives) {
		this.narratives = narratives;
	}
	/**
	 * @return the addresses
	 */
	public List<Address> getAddresses() {
		return addresses;
	}
	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	/**
	 * @return the employers
	 */
	public List<Employer> getEmployers() {
		return employers;
	}
	/**
	 * @param employers the employers to set
	 */
	public void setEmployers(List<Employer> employers) {
		this.employers = employers;
	}
	/**
	 * @return the advisorySummaryText
	 */
	public AdvisorySummaryText getAdvisorySummaryText() {
		return advisorySummaryText;
	}
	/**
	 * @param advisorySummaryText the advisorySummaryText to set
	 */
	public void setAdvisorySummaryText(AdvisorySummaryText advisorySummaryText) {
		this.advisorySummaryText = advisorySummaryText;
	}
	/**
	 * @return the summary
	 */
	public Summary getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(Summary summary) {
		this.summary = summary;
	}
}

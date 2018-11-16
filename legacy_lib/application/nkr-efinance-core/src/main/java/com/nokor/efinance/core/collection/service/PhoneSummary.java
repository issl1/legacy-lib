package com.nokor.efinance.core.collection.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * @author buntha.chea
 */
public class PhoneSummary implements Serializable {
	
	/**
	 */
	private static final long serialVersionUID = -5267202711781122937L;
	
	private List<Integer> dueDayDates = new ArrayList<>();
	private List<BranchSummary> summaries = new ArrayList<>();		

	/**
	 * @return the dueDayDates
	 */
	public List<Integer> getDueDayDates() {
		return dueDayDates;
	}

	/**
	 * @param dueDayDates the dueDayDates to set
	 */
	public void setDueDayDates(List<Integer> dueDayDates) {
		Collections.sort(dueDayDates);
		this.dueDayDates = dueDayDates;
	}

	/**
	 * @return the summaries
	 */
	public List<BranchSummary> getSummaries() {
		return summaries;
	}

	/**
	 * @param summaries the summaries to set
	 */
	public void setSummaries(List<BranchSummary> summaries) {
		this.summaries = summaries;
	}
	
	/**
	 * @param dueDayDate
	 * @return
	 */
	public int getNearestDueDayDate(int dueDayDate) {
		int nearestDueDayDate = dueDayDate;
		for (int day : dueDayDates) {
			if (day == dueDayDate || day > dueDayDate) {
				nearestDueDayDate = dueDayDate;
				break;
			}
		}
		return nearestDueDayDate;
	}
	
	/**
	 * @param contract
	 */
	public void addContract(Organization organization, ColContractDetail contract) {
		BranchSummary selectSummary = null; 
		for (BranchSummary summary : summaries) {
			if (summary.getBranchId().equals(organization.getId())) {
				selectSummary = summary;
				break;
			}
		}
		if (selectSummary == null) {
			selectSummary = new BranchSummary(this);
			selectSummary.setBranchId(organization.getId());
			selectSummary.setBranchName(organization.getDescEn());
			summaries.add(selectSummary);
		}		
		selectSummary.addContract(contract);
	
	}

	/**
	 * @author buntha.chea
	 */
	public static class BranchSummary implements Serializable {
		/** 
		 */
		private static final long serialVersionUID = -3485333202093992709L;
		
		private PhoneSummary phoneSummary;
		
		private Long branchId;
		private String branchName;
		private List<DueDateSummary> summaries = new ArrayList<>();
		
		/**
		 * @param phoneSummary
		 */
		public BranchSummary(PhoneSummary phoneSummary) {
			this.phoneSummary = phoneSummary;
		}	
		/**
		 * @return the phoneSummary
		 */
		public PhoneSummary getPhoneSummary() {
			return phoneSummary;
		}	
		/**
		 * @param phoneSummary the phoneSummary to set
		 */
		public void setPhoneSummary(PhoneSummary phoneSummary) {
			this.phoneSummary = phoneSummary;
		}
		/**
		 * @return the branchId
		 */
		public Long getBranchId() {
			return branchId;
		}
		/**
		 * @param branchId the branchId to set
		 */
		public void setBranchId(Long branchId) {
			this.branchId = branchId;
		}
		/**
		 * @return the branchName
		 */
		public String getBranchName() {
			return branchName;
		}
		/**
		 * @param branchName the branchName to set
		 */
		public void setBranchName(String branchName) {
			this.branchName = branchName;
		}
		/**
		 * @return the summaries
		 */
		public List<DueDateSummary> getSummaries() {
			return summaries;
		}
		/**
		 * @param summaries the summaries to set
		 */
		public void setSummaries(List<DueDateSummary> summaries) {
			this.summaries = summaries;
		}
		
		/**
		 * @param contract
		 */
		public void addContract(ColContractDetail contract) {
			DueDateSummary selectSummary = null; 
			
			int dueDayDate = phoneSummary.getNearestDueDayDate(DateUtils.getDay(contract.getFirstDueDate()));
			
			for (DueDateSummary summary : summaries) {
				if (summary.getDueDayDate() == dueDayDate) {
					selectSummary = summary;
					break;
				}
			}
			if (selectSummary == null) {
				selectSummary = new DueDateSummary();
				selectSummary.setDueDayDate(dueDayDate);
				summaries.add(selectSummary);
			}
			if (contract.getNumberGuarantors() != null && contract.getNumberGuarantors() > 0) {
				selectSummary.setNbContractGuaranteed(selectSummary.getNbContractGuaranteed() + 1);
			} else {
				selectSummary.setNbContractNonGuaranteed(selectSummary.getNbContractNonGuaranteed() + 1);
			}
		}
	}
	
	/**
	 * @author buntha.chea
	 *
	 */
	public static class DueDateSummary implements Serializable {

		/** 
		 */
		private static final long serialVersionUID = 1012185782257020575L;
		
		private int dueDayDate;
		private int nbContractGuaranteed;
		private int nbContractNonGuaranteed;
				
		/**
		 * @return the dueDayDate
		 */
		public int getDueDayDate() {
			return dueDayDate;
		}
		/**
		 * @param dueDayDate the dueDayDate to set
		 */
		public void setDueDayDate(int dueDayDate) {
			this.dueDayDate = dueDayDate;
		}
		/**
		 * @return the nbContractGuaranteed
		 */
		public int getNbContractGuaranteed() {
			return nbContractGuaranteed;
		}
		/**
		 * @param nbContractGuaranteed the nbContractGuaranteed to set
		 */
		public void setNbContractGuaranteed(int nbContractGuaranteed) {
			this.nbContractGuaranteed = nbContractGuaranteed;
		}
		/**
		 * @return the nbContractNonGuaranteed
		 */
		public int getNbContractNonGuaranteed() {
			return nbContractNonGuaranteed;
		}
		/**
		 * @param nbContractNonGuaranteed the nbContractNonGuaranteed to set
		 */
		public void setNbContractNonGuaranteed(int nbContractNonGuaranteed) {
			this.nbContractNonGuaranteed = nbContractNonGuaranteed;
		}
		/**
		 * @return
		 */
		public int getTotalNbContracts() {
			return this.nbContractGuaranteed + this.nbContractNonGuaranteed;
		}
	}	
}


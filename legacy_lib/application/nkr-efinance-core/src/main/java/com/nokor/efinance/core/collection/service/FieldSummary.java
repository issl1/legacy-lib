package com.nokor.efinance.core.collection.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author buntha.chea
 */
public class FieldSummary implements Serializable {
	
	public final static int FLAG = -1;
	public final static int ODM4 = 4;
	public final static int ODM5 = 5;
	
	/**
	 */
	private static final long serialVersionUID = -2566348339135194722L;
	
	private List<TeamSummary> summaries = new ArrayList<>();		

	/**
	 * @return the summaries
	 */
	public List<TeamSummary> getSummaries() {
		return summaries;
	}

	/**
	 * @param summaries the summaries to set
	 */
	public void setSummaries(List<TeamSummary> summaries) {
		this.summaries = summaries;
	}
	
	/**
	 * @param contract
	 */
	public void addContract(ColContractDetail contract) {
		TeamSummary selectSummary = null; 
		for (TeamSummary summary : summaries) {
			if (summary.getTeamId().equals(contract.getTeamId())) {
				selectSummary = summary;
				break;
			}
		}
		if (selectSummary == null) {
			selectSummary = new TeamSummary();
			selectSummary.setTeamId(contract.getTeamId());
			summaries.add(selectSummary);
		}
		selectSummary.addContract(contract);	
	}

	/**
	 * @author buntha.chea
	 */
	public static class TeamSummary implements Serializable {
				
		/**
		 */
		private static final long serialVersionUID = 6612554933823852788L;

		private Long teamId;
		private List<StaffSummary> summaries = new ArrayList<>();
				
		/**
		 */
		public TeamSummary() {
		}								
		/**
		 * @return the teamId
		 */
		public Long getTeamId() {
			return teamId;
		}
		/**
		 * @param teamId the teamId to set
		 */
		public void setTeamId(Long teamId) {
			this.teamId = teamId;
		}
		/**
		 * @return the summaries
		 */
		public List<StaffSummary> getSummaries() {
			return summaries;
		}
		/**
		 * @param summaries the summaries to set
		 */
		public void setSummaries(List<StaffSummary> summaries) {
			this.summaries = summaries;
		}		
		/**
		 * @param contract
		 */
		public void addContract(ColContractDetail contract) {
			StaffSummary selectSummary = null; 						
			for (StaffSummary summary : summaries) {
				if (summary.getStaffId().equals(contract.getStaffId())) {
					selectSummary = summary;
					break;
				}
			}
			if (selectSummary == null) {
				selectSummary = new StaffSummary();
				selectSummary.setStaffId(contract.getStaffId());
				summaries.add(selectSummary);
			}
			selectSummary.addContract(contract);
		}
	}
	
	/**
	 * @author buntha.chea
	 *
	 */
	public static class StaffSummary implements Serializable {		
		
		/**
		 */
		private static final long serialVersionUID = 5464545212722963064L;
		
		private Long staffId;
		private List<AreaSummary> summaries = new ArrayList<>();
		
		/**
		 * @return the staffId
		 */
		public Long getStaffId() {
			return staffId;
		}
		/**
		 * @param staffId the staffId to set
		 */
		public void setStaffId(Long staffId) {
			this.staffId = staffId;
		}
		/**
		 * @return the summaries
		 */
		public List<AreaSummary> getSummaries() {
			return summaries;
		}
		/**
		 * @param summaries the summaries to set
		 */
		public void setSummaries(List<AreaSummary> summaries) {
			this.summaries = summaries;
		}
		
		/**
		 * @param contract
		 */
		public void addContract(ColContractDetail contract) {
			AreaSummary selectSummary = null; 						
			for (AreaSummary summary : summaries) {
				if (summary.getAreaId().equals(contract.getArea().getId())) {
					selectSummary = summary;
					break;
				}
			}
			if (selectSummary == null) {
				selectSummary = new AreaSummary();
				selectSummary.setAreaId(contract.getArea().getId());
				summaries.add(selectSummary);
			}
			selectSummary.addContract(contract);
		}
	}
	
	/**
	 * @author buntha.chea
	 *
	 */
	public static class AreaSummary implements Serializable {
				
		/**
		 */
		private static final long serialVersionUID = 3730181588154690213L;
		
		private Long areaId;
		private List<DetailSummary> summaries = new ArrayList<>();
		
		/**
		 * @return the areaId
		 */
		public Long getAreaId() {
			return areaId;
		}
		/**
		 * @param areaId the areaId to set
		 */
		public void setAreaId(Long areaId) {
			this.areaId = areaId;
		}
		/**
		 * @return the summaries
		 */
		public List<DetailSummary> getSummaries() {
			return summaries;
		}
		/**
		 * @param summaries the summaries to set
		 */
		public void setSummaries(List<DetailSummary> summaries) {
			this.summaries = summaries;
		}
		
		/**
		 * @param contract
		 */
		public void addContract(ColContractDetail contract) {
			DetailSummary selectSummary = null;
			int debLevel = contract.getDebtLevel();
			for (DetailSummary summary : summaries) {
				if (summary.getDebLevel() == debLevel) {
					selectSummary = summary;
					break;
				}
			}
			if (selectSummary == null) {
				selectSummary = new DetailSummary();
				selectSummary.setDebLevel(debLevel);
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
	 */
	public static class DetailSummary implements Serializable {			
		/**
		 */
		private static final long serialVersionUID = 2431590500847194152L;
		
		private int debLevel;
		private int nbContractGuaranteed;
		private int nbContractNonGuaranteed;
		
		/**
		 * @return the debLevel
		 */
		public int getDebLevel() {
			return debLevel;
		}
		/**
		 * @param debLevel the debLevel to set
		 */
		public void setDebLevel(int debLevel) {
			this.debLevel = debLevel;
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


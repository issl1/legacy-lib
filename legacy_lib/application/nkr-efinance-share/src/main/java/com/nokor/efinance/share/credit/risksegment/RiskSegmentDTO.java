package com.nokor.efinance.share.credit.risksegment;

import java.io.Serializable;

/**
 * Risk Segment DTO
 * @author bunlong.taing
 */
public class RiskSegmentDTO implements Serializable {
	/** */
	private static final long serialVersionUID = 110243956679703260L;
	
	private Long id;
	private String name;
	
	private Double minScore;
	private Double maxScore;
	private Double probabilityDefault;
	private Double expectedDistr;
	private Double odds;
	private String recommendations;
	private String decision;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the minScore
	 */
	public Double getMinScore() {
		return minScore;
	}
	
	/**
	 * @param minScore the minScore to set
	 */
	public void setMinScore(Double minScore) {
		this.minScore = minScore;
	}
	
	/**
	 * @return the maxScore
	 */
	public Double getMaxScore() {
		return maxScore;
	}
	
	/**
	 * @param maxScore the maxScore to set
	 */
	public void setMaxScore(Double maxScore) {
		this.maxScore = maxScore;
	}
	
	/**
	 * @return the probabilityDefault
	 */
	public Double getProbabilityDefault() {
		return probabilityDefault;
	}
	
	/**
	 * @param probabilityDefault the probabilityDefault to set
	 */
	public void setProbabilityDefault(Double probabilityDefault) {
		this.probabilityDefault = probabilityDefault;
	}
	
	/**
	 * @return the expectedDistr
	 */
	public Double getExpectedDistr() {
		return expectedDistr;
	}
	
	/**
	 * @param expectedDistr the expectedDistr to set
	 */
	public void setExpectedDistr(Double expectedDistr) {
		this.expectedDistr = expectedDistr;
	}
	
	/**
	 * @return the odds
	 */
	public Double getOdds() {
		return odds;
	}
	
	/**
	 * @param odds the odds to set
	 */
	public void setOdds(Double odds) {
		this.odds = odds;
	}
	
	/**
	 * @return the recommendations
	 */
	public String getRecommendations() {
		return recommendations;
	}
	
	/**
	 * @param recommendations the recommendations to set
	 */
	public void setRecommendations(String recommendations) {
		this.recommendations = recommendations;
	}
	
	/**
	 * @return the decision
	 */
	public String getDecision() {
		return decision;
	}
	
	/**
	 * @param decision the decision to set
	 */
	public void setDecision(String decision) {
		this.decision = decision;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof RiskSegmentDTO)) {
			 return false;
		 }
		 RiskSegmentDTO riskSegmentDTO = (RiskSegmentDTO) arg0;
		 return getId() != null && getId().equals(riskSegmentDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}

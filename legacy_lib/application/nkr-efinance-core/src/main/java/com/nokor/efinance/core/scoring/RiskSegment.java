package com.nokor.efinance.core.scoring;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_score_risk_segment")
public class RiskSegment extends EntityA {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private Double minScore;
	private Double maxScore;
	private Double probabilityDefault;
	private Double expectedDistr;
	private Double odds;
	private String recommendations;
	private String decision;
	
    /**
     * @return
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sco_ris_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the name
	 */
    @Column(name = "sco_ris_name", nullable = true)
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
	@Column(name = "sco_ris_min", nullable = true)
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
	@Column(name = "sco_ris_max", nullable = true)
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
	@Column(name = "sco_ris_probability_default", nullable = true)
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
	@Column(name = "sco_ris_expected_distr", nullable = true)
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
	@Column(name = "sco_ris_odds", nullable = true)
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
	@Column(name = "sco_ris_recommendations", nullable = true, length = 150)
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
	@Column(name = "sco_ris_decision", nullable = true, length = 150)
	public String getDecision() {
		return decision;
	}

	/**
	 * @param decision the decision to set
	 */
	public void setDecision(String decision) {
		this.decision = decision;
	}	
}

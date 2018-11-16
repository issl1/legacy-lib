package com.nokor.efinance.core.scoring;

import org.seuksa.frmk.model.entity.MEntityA;

/**
 * Meta data of com.nokor.efinance.core.scoring.MRiskSegment
 * @author bunlong.taing
 */
public interface MRiskSegment extends MEntityA {
	
	public static final String NAME = "name";
	public static final String MIN_SCORE = "minScore";
	public static final String MAX_SCORE = "maxScore";
	public static final String PROBABILITY_DEFAULT = "probabilityDefault";
	public static final String EXPECTED_DISTR = "expectedDistr";
	public static final String ODDS = "odds";
	public static final String RECOMMENDATIONS = "recommendations";
	public static final String DECISION = "decision";

}

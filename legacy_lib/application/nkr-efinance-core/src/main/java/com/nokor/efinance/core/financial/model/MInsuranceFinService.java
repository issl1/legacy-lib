package com.nokor.efinance.core.financial.model;

import org.seuksa.frmk.model.entity.MEntityA;

public interface MInsuranceFinService extends MEntityA {
	
	public static final String INSURANCE = "insurance";
	public static final String PREMIUM_FIRST_YEAR = "premiumFirstYear";
	public static final String PREMIUM_SECOND_YEAR = "premiumSecondYear";
	public static final String CLAIM_AMOUNT_FIRST_YEAR = "claimAmountFirstYear";
	public static final String CLAIM_AMOUNT_SECOND_YEAR = "claimAmountSecondYear";
	public static final String SERVICE = "service";
	public static final String SERVICE_TYPE = "serviceType";
	
	public static final String CLAIM_AMOUNT_2Y_FIRST_YEAR = "claimAmount2YFirstYear";
	public static final String CLAIM_AMOUNT_2Y_SECOND_YEAR = "claimAmount2YSecondYear";

}

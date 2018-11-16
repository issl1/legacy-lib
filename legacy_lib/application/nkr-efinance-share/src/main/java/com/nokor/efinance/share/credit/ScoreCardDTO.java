package com.nokor.efinance.share.credit;

import java.io.Serializable;


/**
 * 
 * @author uhout.cheng
 */
public class ScoreCardDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 3750884293369349699L;
	
	private Double scoreMaritalStatusSingle;
	private Double scoreMaritalStatusMarried;
	private Double scoreMaritalStatusDivorced;
	private Double scoreMaritalStatusSeparated;
	private Double scoreMaritalStatusWidow;
	private Double scoreMaritalStatusDefacto;
	private Double scoreMaritalStatusUnknown;
	
	private Double scoreNbDependentLessThanZero;
	private Double scoreNbDependentZeroToOne;
	private Double scoreNbDependentOneToTwo;
	private Double scoreNbDependentTwoToThree;
	private Double scoreNbDependentThreePlus;
	
	private Double scoreEducationHighSchool;
	private Double scoreEducationTechnicalCollege;
	private Double scoreEducationBachelor;
	private Double scoreEducationMaster;
	private Double scoreEducationPhd;
	private Double scoreEducationMiddleSchool;
	private Double scoreEducationJuniorHighSchool;
	private Double scoreEducationSeniorHighSchool;
	private Double scoreEducationVocationalCertificate;
	private Double scoreEducationHighVocationalCertificate;
	private Double scoreEducationTechnicalCertificate;
	private Double scoreEducationNonFormalEducation;
	private Double scoreEducationCompulsoryEducation;
	private Double scoreEducationAdultEducation;
	private Double scoreEducationPolytechnicSchool;
	
	private Double scoreCompanySizeLessThanFifty;
	private Double scoreCompanySizeFiftyToOneHundred;
	private Double scoreCompanySizeOneHundredToTwoHundredFifty;
	private Double scoreCompanySizeTwoHundredFiftyPlus;
	
	private Double scoreEmploymentDurationLessThanSix;
	private Double scoreEmploymentDurationSixToTwelve;
	private Double scoreEmploymentDurationTwelveToThirtySix;
	private Double scoreEmploymentDurationThirtySixToSixty;
	private Double scoreEmploymentDurationSixtyPlus;
	
	private Double scoreIncomeTypeEmployed;
	private Double scoreIncomeTypeSelfEmployed;
	
	private Double scoreApartmentPropertyYes;
	private Double scoreApartmentPropertyNo;
	
	private Double scoreDebtIncomeLessThanTwentySix;
	private Double scoreDebtIncomeTwentySixToThirtySeven;
	private Double scoreDebtIncomeThirtySevenToFortyFour;
	private Double scoreDebtIncomeFortyFourPlus;
	
	private Double scoreFICOScoreA;
	private Double scoreFICOScoreB;
	private Double scoreFICOScoreBPlus;
	private Double scoreFICOScoreC;
	private Double scoreFICOScoreCPlus;
	private Double scoreFICOScoreD;
	
	/**
	 * @return the scoreMaritalStatusSingle
	 */
	public Double getScoreMaritalStatusSingle() {
		return scoreMaritalStatusSingle;
	}

	/**
	 * @param scoreMaritalStatusSingle the scoreMaritalStatusSingle to set
	 */
	public void setScoreMaritalStatusSingle(Double scoreMaritalStatusSingle) {
		this.scoreMaritalStatusSingle = scoreMaritalStatusSingle;
	}

	/**
	 * @return the scoreMaritalStatusMarried
	 */
	public Double getScoreMaritalStatusMarried() {
		return scoreMaritalStatusMarried;
	}

	/**
	 * @param scoreMaritalStatusMarried the scoreMaritalStatusMarried to set
	 */
	public void setScoreMaritalStatusMarried(Double scoreMaritalStatusMarried) {
		this.scoreMaritalStatusMarried = scoreMaritalStatusMarried;
	}

	/**
	 * @return the scoreMaritalStatusDivorced
	 */
	public Double getScoreMaritalStatusDivorced() {
		return scoreMaritalStatusDivorced;
	}

	/**
	 * @param scoreMaritalStatusDivorced the scoreMaritalStatusDivorced to set
	 */
	public void setScoreMaritalStatusDivorced(Double scoreMaritalStatusDivorced) {
		this.scoreMaritalStatusDivorced = scoreMaritalStatusDivorced;
	}

	/**
	 * @return the scoreMaritalStatusSeparated
	 */
	public Double getScoreMaritalStatusSeparated() {
		return scoreMaritalStatusSeparated;
	}

	/**
	 * @param scoreMaritalStatusSeparated the scoreMaritalStatusSeparated to set
	 */
	public void setScoreMaritalStatusSeparated(Double scoreMaritalStatusSeparated) {
		this.scoreMaritalStatusSeparated = scoreMaritalStatusSeparated;
	}

	/**
	 * @return the scoreMaritalStatusWidow
	 */
	public Double getScoreMaritalStatusWidow() {
		return scoreMaritalStatusWidow;
	}

	/**
	 * @param scoreMaritalStatusWidow the scoreMaritalStatusWidow to set
	 */
	public void setScoreMaritalStatusWidow(Double scoreMaritalStatusWidow) {
		this.scoreMaritalStatusWidow = scoreMaritalStatusWidow;
	}

	/**
	 * @return the scoreMaritalStatusDefacto
	 */
	public Double getScoreMaritalStatusDefacto() {
		return scoreMaritalStatusDefacto;
	}

	/**
	 * @param scoreMaritalStatusDefacto the scoreMaritalStatusDefacto to set
	 */
	public void setScoreMaritalStatusDefacto(Double scoreMaritalStatusDefacto) {
		this.scoreMaritalStatusDefacto = scoreMaritalStatusDefacto;
	}

	/**
	 * @return the scoreMaritalStatusUnknown
	 */
	public Double getScoreMaritalStatusUnknown() {
		return scoreMaritalStatusUnknown;
	}

	/**
	 * @param scoreMaritalStatusUnknown the scoreMaritalStatusUnknown to set
	 */
	public void setScoreMaritalStatusUnknown(Double scoreMaritalStatusUnknown) {
		this.scoreMaritalStatusUnknown = scoreMaritalStatusUnknown;
	}

	/**
	 * @return the scoreNbDependentLessThanZero
	 */
	public Double getScoreNbDependentLessThanZero() {
		return scoreNbDependentLessThanZero;
	}

	/**
	 * @param scoreNbDependentLessThanZero the scoreNbDependentLessThanZero to set
	 */
	public void setScoreNbDependentLessThanZero(Double scoreNbDependentLessThanZero) {
		this.scoreNbDependentLessThanZero = scoreNbDependentLessThanZero;
	}

	/**
	 * @return the scoreNbDependentZeroToOne
	 */
	public Double getScoreNbDependentZeroToOne() {
		return scoreNbDependentZeroToOne;
	}

	/**
	 * @param scoreNbDependentZeroToOne the scoreNbDependentZeroToOne to set
	 */
	public void setScoreNbDependentZeroToOne(Double scoreNbDependentZeroToOne) {
		this.scoreNbDependentZeroToOne = scoreNbDependentZeroToOne;
	}

	/**
	 * @return the scoreNbDependentOneToTwo
	 */
	public Double getScoreNbDependentOneToTwo() {
		return scoreNbDependentOneToTwo;
	}

	/**
	 * @param scoreNbDependentOneToTwo the scoreNbDependentOneToTwo to set
	 */
	public void setScoreNbDependentOneToTwo(Double scoreNbDependentOneToTwo) {
		this.scoreNbDependentOneToTwo = scoreNbDependentOneToTwo;
	}

	/**
	 * @return the scoreNbDependentTwoToThree
	 */
	public Double getScoreNbDependentTwoToThree() {
		return scoreNbDependentTwoToThree;
	}

	/**
	 * @param scoreNbDependentTwoToThree the scoreNbDependentTwoToThree to set
	 */
	public void setScoreNbDependentTwoToThree(Double scoreNbDependentTwoToThree) {
		this.scoreNbDependentTwoToThree = scoreNbDependentTwoToThree;
	}

	/**
	 * @return the scoreNbDependentThreePlus
	 */
	public Double getScoreNbDependentThreePlus() {
		return scoreNbDependentThreePlus;
	}

	/**
	 * @param scoreNbDependentThreePlus the scoreNbDependentThreePlus to set
	 */
	public void setScoreNbDependentThreePlus(Double scoreNbDependentThreePlus) {
		this.scoreNbDependentThreePlus = scoreNbDependentThreePlus;
	}

	/**
	 * @return the scoreEducationHighSchool
	 */
	public Double getScoreEducationHighSchool() {
		return scoreEducationHighSchool;
	}

	/**
	 * @param scoreEducationHighSchool the scoreEducationHighSchool to set
	 */
	public void setScoreEducationHighSchool(Double scoreEducationHighSchool) {
		this.scoreEducationHighSchool = scoreEducationHighSchool;
	}

	/**
	 * @return the scoreEducationTechnicalCollege
	 */
	public Double getScoreEducationTechnicalCollege() {
		return scoreEducationTechnicalCollege;
	}

	/**
	 * @param scoreEducationTechnicalCollege the scoreEducationTechnicalCollege to set
	 */
	public void setScoreEducationTechnicalCollege(
			Double scoreEducationTechnicalCollege) {
		this.scoreEducationTechnicalCollege = scoreEducationTechnicalCollege;
	}

	/**
	 * @return the scoreEducationBachelor
	 */
	public Double getScoreEducationBachelor() {
		return scoreEducationBachelor;
	}

	/**
	 * @param scoreEducationBachelor the scoreEducationBachelor to set
	 */
	public void setScoreEducationBachelor(Double scoreEducationBachelor) {
		this.scoreEducationBachelor = scoreEducationBachelor;
	}

	/**
	 * @return the scoreEducationMaster
	 */
	public Double getScoreEducationMaster() {
		return scoreEducationMaster;
	}

	/**
	 * @param scoreEducationMaster the scoreEducationMaster to set
	 */
	public void setScoreEducationMaster(Double scoreEducationMaster) {
		this.scoreEducationMaster = scoreEducationMaster;
	}

	/**
	 * @return the scoreEducationPhd
	 */
	public Double getScoreEducationPhd() {
		return scoreEducationPhd;
	}

	/**
	 * @param scoreEducationPhd the scoreEducationPhd to set
	 */
	public void setScoreEducationPhd(Double scoreEducationPhd) {
		this.scoreEducationPhd = scoreEducationPhd;
	}

	/**
	 * @return the scoreEducationMiddleSchool
	 */
	public Double getScoreEducationMiddleSchool() {
		return scoreEducationMiddleSchool;
	}

	/**
	 * @param scoreEducationMiddleSchool the scoreEducationMiddleSchool to set
	 */
	public void setScoreEducationMiddleSchool(Double scoreEducationMiddleSchool) {
		this.scoreEducationMiddleSchool = scoreEducationMiddleSchool;
	}

	/**
	 * @return the scoreEducationJuniorHighSchool
	 */
	public Double getScoreEducationJuniorHighSchool() {
		return scoreEducationJuniorHighSchool;
	}

	/**
	 * @param scoreEducationJuniorHighSchool the scoreEducationJuniorHighSchool to set
	 */
	public void setScoreEducationJuniorHighSchool(
			Double scoreEducationJuniorHighSchool) {
		this.scoreEducationJuniorHighSchool = scoreEducationJuniorHighSchool;
	}

	/**
	 * @return the scoreEducationSeniorHighSchool
	 */
	public Double getScoreEducationSeniorHighSchool() {
		return scoreEducationSeniorHighSchool;
	}

	/**
	 * @param scoreEducationSeniorHighSchool the scoreEducationSeniorHighSchool to set
	 */
	public void setScoreEducationSeniorHighSchool(
			Double scoreEducationSeniorHighSchool) {
		this.scoreEducationSeniorHighSchool = scoreEducationSeniorHighSchool;
	}

	/**
	 * @return the scoreEducationVocationalCertificate
	 */
	public Double getScoreEducationVocationalCertificate() {
		return scoreEducationVocationalCertificate;
	}

	/**
	 * @param scoreEducationVocationalCertificate the scoreEducationVocationalCertificate to set
	 */
	public void setScoreEducationVocationalCertificate(
			Double scoreEducationVocationalCertificate) {
		this.scoreEducationVocationalCertificate = scoreEducationVocationalCertificate;
	}

	/**
	 * @return the scoreEducationHighVocationalCertificate
	 */
	public Double getScoreEducationHighVocationalCertificate() {
		return scoreEducationHighVocationalCertificate;
	}

	/**
	 * @param scoreEducationHighVocationalCertificate the scoreEducationHighVocationalCertificate to set
	 */
	public void setScoreEducationHighVocationalCertificate(
			Double scoreEducationHighVocationalCertificate) {
		this.scoreEducationHighVocationalCertificate = scoreEducationHighVocationalCertificate;
	}

	/**
	 * @return the scoreEducationTechnicalCertificate
	 */
	public Double getScoreEducationTechnicalCertificate() {
		return scoreEducationTechnicalCertificate;
	}

	/**
	 * @param scoreEducationTechnicalCertificate the scoreEducationTechnicalCertificate to set
	 */
	public void setScoreEducationTechnicalCertificate(
			Double scoreEducationTechnicalCertificate) {
		this.scoreEducationTechnicalCertificate = scoreEducationTechnicalCertificate;
	}

	/**
	 * @return the scoreEducationNonFormalEducation
	 */
	public Double getScoreEducationNonFormalEducation() {
		return scoreEducationNonFormalEducation;
	}

	/**
	 * @param scoreEducationNonFormalEducation the scoreEducationNonFormalEducation to set
	 */
	public void setScoreEducationNonFormalEducation(
			Double scoreEducationNonFormalEducation) {
		this.scoreEducationNonFormalEducation = scoreEducationNonFormalEducation;
	}

	/**
	 * @return the scoreEducationCompulsoryEducation
	 */
	public Double getScoreEducationCompulsoryEducation() {
		return scoreEducationCompulsoryEducation;
	}

	/**
	 * @param scoreEducationCompulsoryEducation the scoreEducationCompulsoryEducation to set
	 */
	public void setScoreEducationCompulsoryEducation(
			Double scoreEducationCompulsoryEducation) {
		this.scoreEducationCompulsoryEducation = scoreEducationCompulsoryEducation;
	}

	/**
	 * @return the scoreEducationAdultEducation
	 */
	public Double getScoreEducationAdultEducation() {
		return scoreEducationAdultEducation;
	}

	/**
	 * @param scoreEducationAdultEducation the scoreEducationAdultEducation to set
	 */
	public void setScoreEducationAdultEducation(Double scoreEducationAdultEducation) {
		this.scoreEducationAdultEducation = scoreEducationAdultEducation;
	}

	/**
	 * @return the scoreEducationPolytechnicSchool
	 */
	public Double getScoreEducationPolytechnicSchool() {
		return scoreEducationPolytechnicSchool;
	}

	/**
	 * @param scoreEducationPolytechnicSchool the scoreEducationPolytechnicSchool to set
	 */
	public void setScoreEducationPolytechnicSchool(
			Double scoreEducationPolytechnicSchool) {
		this.scoreEducationPolytechnicSchool = scoreEducationPolytechnicSchool;
	}

	/**
	 * @return the scoreCompanySizeLessThanFifty
	 */
	public Double getScoreCompanySizeLessThanFifty() {
		return scoreCompanySizeLessThanFifty;
	}

	/**
	 * @param scoreCompanySizeLessThanFifty the scoreCompanySizeLessThanFifty to set
	 */
	public void setScoreCompanySizeLessThanFifty(
			Double scoreCompanySizeLessThanFifty) {
		this.scoreCompanySizeLessThanFifty = scoreCompanySizeLessThanFifty;
	}

	/**
	 * @return the scoreCompanySizeFiftyToOneHundred
	 */
	public Double getScoreCompanySizeFiftyToOneHundred() {
		return scoreCompanySizeFiftyToOneHundred;
	}

	/**
	 * @param scoreCompanySizeFiftyToOneHundred the scoreCompanySizeFiftyToOneHundred to set
	 */
	public void setScoreCompanySizeFiftyToOneHundred(
			Double scoreCompanySizeFiftyToOneHundred) {
		this.scoreCompanySizeFiftyToOneHundred = scoreCompanySizeFiftyToOneHundred;
	}

	/**
	 * @return the scoreCompanySizeOneHundredToTwoHundredFifty
	 */
	public Double getScoreCompanySizeOneHundredToTwoHundredFifty() {
		return scoreCompanySizeOneHundredToTwoHundredFifty;
	}

	/**
	 * @param scoreCompanySizeOneHundredToTwoHundredFifty the scoreCompanySizeOneHundredToTwoHundredFifty to set
	 */
	public void setScoreCompanySizeOneHundredToTwoHundredFifty(
			Double scoreCompanySizeOneHundredToTwoHundredFifty) {
		this.scoreCompanySizeOneHundredToTwoHundredFifty = scoreCompanySizeOneHundredToTwoHundredFifty;
	}

	/**
	 * @return the scoreCompanySizeTwoHundredFiftyPlus
	 */
	public Double getScoreCompanySizeTwoHundredFiftyPlus() {
		return scoreCompanySizeTwoHundredFiftyPlus;
	}

	/**
	 * @param scoreCompanySizeTwoHundredFiftyPlus the scoreCompanySizeTwoHundredFiftyPlus to set
	 */
	public void setScoreCompanySizeTwoHundredFiftyPlus(
			Double scoreCompanySizeTwoHundredFiftyPlus) {
		this.scoreCompanySizeTwoHundredFiftyPlus = scoreCompanySizeTwoHundredFiftyPlus;
	}

	/**
	 * @return the scoreEmploymentDurationLessThanSix
	 */
	public Double getScoreEmploymentDurationLessThanSix() {
		return scoreEmploymentDurationLessThanSix;
	}

	/**
	 * @param scoreEmploymentDurationLessThanSix the scoreEmploymentDurationLessThanSix to set
	 */
	public void setScoreEmploymentDurationLessThanSix(
			Double scoreEmploymentDurationLessThanSix) {
		this.scoreEmploymentDurationLessThanSix = scoreEmploymentDurationLessThanSix;
	}

	/**
	 * @return the scoreEmploymentDurationSixToTwelve
	 */
	public Double getScoreEmploymentDurationSixToTwelve() {
		return scoreEmploymentDurationSixToTwelve;
	}

	/**
	 * @param scoreEmploymentDurationSixToTwelve the scoreEmploymentDurationSixToTwelve to set
	 */
	public void setScoreEmploymentDurationSixToTwelve(
			Double scoreEmploymentDurationSixToTwelve) {
		this.scoreEmploymentDurationSixToTwelve = scoreEmploymentDurationSixToTwelve;
	}

	/**
	 * @return the scoreEmploymentDurationTwelveToThirtySix
	 */
	public Double getScoreEmploymentDurationTwelveToThirtySix() {
		return scoreEmploymentDurationTwelveToThirtySix;
	}

	/**
	 * @param scoreEmploymentDurationTwelveToThirtySix the scoreEmploymentDurationTwelveToThirtySix to set
	 */
	public void setScoreEmploymentDurationTwelveToThirtySix(
			Double scoreEmploymentDurationTwelveToThirtySix) {
		this.scoreEmploymentDurationTwelveToThirtySix = scoreEmploymentDurationTwelveToThirtySix;
	}

	/**
	 * @return the scoreEmploymentDurationThirtySixToSixty
	 */
	public Double getScoreEmploymentDurationThirtySixToSixty() {
		return scoreEmploymentDurationThirtySixToSixty;
	}

	/**
	 * @param scoreEmploymentDurationThirtySixToSixty the scoreEmploymentDurationThirtySixToSixty to set
	 */
	public void setScoreEmploymentDurationThirtySixToSixty(
			Double scoreEmploymentDurationThirtySixToSixty) {
		this.scoreEmploymentDurationThirtySixToSixty = scoreEmploymentDurationThirtySixToSixty;
	}

	/**
	 * @return the scoreEmploymentDurationSixtyPlus
	 */
	public Double getScoreEmploymentDurationSixtyPlus() {
		return scoreEmploymentDurationSixtyPlus;
	}

	/**
	 * @param scoreEmploymentDurationSixtyPlus the scoreEmploymentDurationSixtyPlus to set
	 */
	public void setScoreEmploymentDurationSixtyPlus(
			Double scoreEmploymentDurationSixtyPlus) {
		this.scoreEmploymentDurationSixtyPlus = scoreEmploymentDurationSixtyPlus;
	}

	/**
	 * @return the scoreIncomeTypeEmployed
	 */
	public Double getScoreIncomeTypeEmployed() {
		return scoreIncomeTypeEmployed;
	}

	/**
	 * @param scoreIncomeTypeEmployed the scoreIncomeTypeEmployed to set
	 */
	public void setScoreIncomeTypeEmployed(Double scoreIncomeTypeEmployed) {
		this.scoreIncomeTypeEmployed = scoreIncomeTypeEmployed;
	}

	/**
	 * @return the scoreIncomeTypeSelfEmployed
	 */
	public Double getScoreIncomeTypeSelfEmployed() {
		return scoreIncomeTypeSelfEmployed;
	}

	/**
	 * @param scoreIncomeTypeSelfEmployed the scoreIncomeTypeSelfEmployed to set
	 */
	public void setScoreIncomeTypeSelfEmployed(Double scoreIncomeTypeSelfEmployed) {
		this.scoreIncomeTypeSelfEmployed = scoreIncomeTypeSelfEmployed;
	}

	/**
	 * @return the scoreApartmentPropertyYes
	 */
	public Double getScoreApartmentPropertyYes() {
		return scoreApartmentPropertyYes;
	}

	/**
	 * @param scoreApartmentPropertyYes the scoreApartmentPropertyYes to set
	 */
	public void setScoreApartmentPropertyYes(Double scoreApartmentPropertyYes) {
		this.scoreApartmentPropertyYes = scoreApartmentPropertyYes;
	}

	/**
	 * @return the scoreApartmentPropertyNo
	 */
	public Double getScoreApartmentPropertyNo() {
		return scoreApartmentPropertyNo;
	}

	/**
	 * @param scoreApartmentPropertyNo the scoreApartmentPropertyNo to set
	 */
	public void setScoreApartmentPropertyNo(Double scoreApartmentPropertyNo) {
		this.scoreApartmentPropertyNo = scoreApartmentPropertyNo;
	}

	/**
	 * @return the scoreDebtIncomeLessThanTwentySix
	 */
	public Double getScoreDebtIncomeLessThanTwentySix() {
		return scoreDebtIncomeLessThanTwentySix;
	}

	/**
	 * @param scoreDebtIncomeLessThanTwentySix the scoreDebtIncomeLessThanTwentySix to set
	 */
	public void setScoreDebtIncomeLessThanTwentySix(
			Double scoreDebtIncomeLessThanTwentySix) {
		this.scoreDebtIncomeLessThanTwentySix = scoreDebtIncomeLessThanTwentySix;
	}

	/**
	 * @return the scoreDebtIncomeTwentySixToThirtySeven
	 */
	public Double getScoreDebtIncomeTwentySixToThirtySeven() {
		return scoreDebtIncomeTwentySixToThirtySeven;
	}

	/**
	 * @param scoreDebtIncomeTwentySixToThirtySeven the scoreDebtIncomeTwentySixToThirtySeven to set
	 */
	public void setScoreDebtIncomeTwentySixToThirtySeven(
			Double scoreDebtIncomeTwentySixToThirtySeven) {
		this.scoreDebtIncomeTwentySixToThirtySeven = scoreDebtIncomeTwentySixToThirtySeven;
	}

	/**
	 * @return the scoreDebtIncomeThirtySevenToFortyFour
	 */
	public Double getScoreDebtIncomeThirtySevenToFortyFour() {
		return scoreDebtIncomeThirtySevenToFortyFour;
	}

	/**
	 * @param scoreDebtIncomeThirtySevenToFortyFour the scoreDebtIncomeThirtySevenToFortyFour to set
	 */
	public void setScoreDebtIncomeThirtySevenToFortyFour(
			Double scoreDebtIncomeThirtySevenToFortyFour) {
		this.scoreDebtIncomeThirtySevenToFortyFour = scoreDebtIncomeThirtySevenToFortyFour;
	}

	/**
	 * @return the scoreDebtIncomeFortyFourPlus
	 */
	public Double getScoreDebtIncomeFortyFourPlus() {
		return scoreDebtIncomeFortyFourPlus;
	}

	/**
	 * @param scoreDebtIncomeFortyFourPlus the scoreDebtIncomeFortyFourPlus to set
	 */
	public void setScoreDebtIncomeFortyFourPlus(Double scoreDebtIncomeFortyFourPlus) {
		this.scoreDebtIncomeFortyFourPlus = scoreDebtIncomeFortyFourPlus;
	}

	/**
	 * @return the scoreFICOScoreA
	 */
	public Double getScoreFICOScoreA() {
		return scoreFICOScoreA;
	}

	/**
	 * @param scoreFICOScoreA the scoreFICOScoreA to set
	 */
	public void setScoreFICOScoreA(Double scoreFICOScoreA) {
		this.scoreFICOScoreA = scoreFICOScoreA;
	}

	/**
	 * @return the scoreFICOScoreB
	 */
	public Double getScoreFICOScoreB() {
		return scoreFICOScoreB;
	}

	/**
	 * @param scoreFICOScoreB the scoreFICOScoreB to set
	 */
	public void setScoreFICOScoreB(Double scoreFICOScoreB) {
		this.scoreFICOScoreB = scoreFICOScoreB;
	}

	/**
	 * @return the scoreFICOScoreBPlus
	 */
	public Double getScoreFICOScoreBPlus() {
		return scoreFICOScoreBPlus;
	}

	/**
	 * @param scoreFICOScoreBPlus the scoreFICOScoreBPlus to set
	 */
	public void setScoreFICOScoreBPlus(Double scoreFICOScoreBPlus) {
		this.scoreFICOScoreBPlus = scoreFICOScoreBPlus;
	}

	/**
	 * @return the scoreFICOScoreC
	 */
	public Double getScoreFICOScoreC() {
		return scoreFICOScoreC;
	}

	/**
	 * @param scoreFICOScoreC the scoreFICOScoreC to set
	 */
	public void setScoreFICOScoreC(Double scoreFICOScoreC) {
		this.scoreFICOScoreC = scoreFICOScoreC;
	}

	/**
	 * @return the scoreFICOScoreCPlus
	 */
	public Double getScoreFICOScoreCPlus() {
		return scoreFICOScoreCPlus;
	}

	/**
	 * @param scoreFICOScoreCPlus the scoreFICOScoreCPlus to set
	 */
	public void setScoreFICOScoreCPlus(Double scoreFICOScoreCPlus) {
		this.scoreFICOScoreCPlus = scoreFICOScoreCPlus;
	}

	/**
	 * @return the scoreFICOScoreD
	 */
	public Double getScoreFICOScoreD() {
		return scoreFICOScoreD;
	}

	/**
	 * @param scoreFICOScoreD the scoreFICOScoreD to set
	 */
	public void setScoreFICOScoreD(Double scoreFICOScoreD) {
		this.scoreFICOScoreD = scoreFICOScoreD;
	}
}

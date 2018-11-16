package com.nokor.efinance.core.shared.quotation;

import org.apache.commons.lang.StringUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;

/**
 * @author ly.youhort
 *
 */
public final class QuotationProfileUtils {

	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isSaveUpdateAvailable(Quotation quotation) {		
		return quotation.getWkfStatus().equals(QuotationWkfStatus.QUO)
				|| quotation.getWkfStatus().equals(QuotationWkfStatus.RAD)
				|| quotation.getWkfStatus().equals(QuotationWkfStatus.RFC)
				|| ProfileUtil.isAdmin();
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isDeclineAvailable(Quotation quotation) {
		if (!ProfileUtil.isPOS()) {
			return false;
		}
		return quotation.getId() != null && !quotation.isIssueDownPayment() && quotation.getFirstSubmissionDate() != null
				&& !quotation.getWkfStatus().equals(QuotationWkfStatus.ACT)
				&& !quotation.getWkfStatus().equals(QuotationWkfStatus.RVG)
				&& !quotation.getWkfStatus().equals(QuotationWkfStatus.REJ)
				&& !quotation.getWkfStatus().equals(QuotationWkfStatus.REU)
				&& !quotation.getWkfStatus().equals(QuotationWkfStatus.DEC)
				&& !quotation.getWkfStatus().equals(QuotationWkfStatus.LCG)
				&& !quotation.getWkfStatus().equals(QuotationWkfStatus.ACG);
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isCancelAvailable(Quotation quotation) {
		return quotation.getId() != null && 
			   quotation.getWkfStatus().equals(QuotationWkfStatus.QUO) && 
			   quotation.getFirstSubmissionDate() == null && 
			   ProfileUtil.isPOS();
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isRequestFieldCheckAvailable(Quotation quotation) {
		return quotation.getId() != null 
				&& quotation.getWkfStatus().equals(QuotationWkfStatus.PRA)
				&& !ProfileUtil.isAdmin();
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isChangeAssetAvailable(Quotation quotation) {		
		return quotation.getId() != null  && ProfileUtil.isPOS() && !quotation.isIssueDownPayment()
				&& (quotation.getWkfStatus().equals(QuotationWkfStatus.APV))
				&& quotation.getDocumentController() == null;
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isRequestChangeGuarantorAvailable(Quotation quotation) {		
		return quotation.getId() != null  
				&& ProfileUtil.isPOS()
				&& (quotation.getWkfStatus().equals(QuotationWkfStatus.ACT))
				&& (quotation.getQuotationApplicant(EApplicantType.G) != null);
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isChangeGuarantorAvailable(Quotation quotation) {		
		return quotation.getId() != null  
				&& ProfileUtil.isPOS()
				&& (quotation.getWkfStatus().equals(QuotationWkfStatus.LCG))
				&& (quotation.getQuotationApplicant(EApplicantType.G) != null);
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isRejectAvailable(Quotation quotation) {		
		return quotation.getId() != null 
				&& !ProfileUtil.isAdmin() 
				&& (quotation.getWkfStatus().equals(QuotationWkfStatus.PRA) 
					|| (quotation.getWkfStatus().equals(QuotationWkfStatus.REU) && ProfileUtil.isUnderwriterSupervisor()) 
					|| (quotation.getWkfStatus().equals(QuotationWkfStatus.APV) && ProfileUtil.isUW()));
	}
	
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isApproveAvailable(Quotation quotation) {		
		return quotation.getId() != null 
				&& (quotation.getWkfStatus().equals(QuotationWkfStatus.PRA) || quotation.getWkfStatus().equals(QuotationWkfStatus.RVG)) 
				&& !ProfileUtil.isAdmin();
	}
	
	/**
	 * @return
	 */
	public static boolean isAddQuotationAvailable() {
		return ProfileUtil.isPOS();
	}
	
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isSubmitAvailable(Quotation quotation) {
		return quotation.getId() != null 
				&& (quotation.getWkfStatus().equals(QuotationWkfStatus.QUO)
				       || quotation.getWkfStatus().equals(QuotationWkfStatus.AWT) 
				       || quotation.getWkfStatus().equals(QuotationWkfStatus.RAD)
				       || quotation.getWkfStatus().equals(QuotationWkfStatus.RFC)
				       || quotation.getWkfStatus().equals(QuotationWkfStatus.LCG))
				&& ProfileUtil.isPOS();
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isSaveDocumentAvailable(Quotation quotation) {
		return quotation.getId() != null 
				&& (
					(
						(ProfileUtil.isUnderwriter() || ProfileUtil.isUnderwriterSupervisor()) 
							&& (quotation.getWkfStatus().equals(QuotationWkfStatus.PRO) || quotation.getWkfStatus().equals(QuotationWkfStatus.PRA))
					)
					||
					(
						ProfileUtil.isPOS() && quotation.getWkfStatus().equals(QuotationWkfStatus.AWT)
					)
					||
					(ProfileUtil.isDocumentController() && 
						(quotation.getWkfStatus().equals(QuotationWkfStatus.APV)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.ACT))
					)
				);
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isSaveAssetAvailable(Quotation quotation) {
		return quotation.getId() != null 
				&& (
					(ProfileUtil.isPOS() && (quotation.getWkfStatus().equals(QuotationWkfStatus.APV) || quotation.getWkfStatus().equals(QuotationWkfStatus.PPO))) || 
					(ProfileUtil.isDocumentController() && 
						(quotation.getWkfStatus().equals(QuotationWkfStatus.APV)
								|| quotation.getWkfStatus().equals(QuotationWkfStatus.PPO)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.ACT))
					)
				);
	}
		
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isActivateAvailable(Quotation quotation) {
		Asset asset = quotation.getAsset();
		return quotation.getId() != null
				&& ProfileUtil.isPOS()
				&& quotation.getDocumentController() != null
				&& (quotation.getWkfStatus().equals(QuotationWkfStatus.PPO) || quotation.getWkfStatus().equals(QuotationWkfStatus.ACG))
				&& (quotation.isIssueDownPayment())
				&& (StringUtils.isNotEmpty(asset.getChassisNumber()))
				&& (StringUtils.isNotEmpty(asset.getEngineNumber()));
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isReportsAvailable(Quotation quotation) {
		Asset asset = quotation.getAsset();
		return quotation.getId() != null 
				&& ((ProfileUtil.isPOS()
						&& quotation.getDocumentController() != null
						&& quotation.getWkfStatus().equals(QuotationWkfStatus.ACT)
						&& StringUtils.isNotEmpty(asset.getChassisNumber())
						&& StringUtils.isNotEmpty(asset.getEngineNumber()))
					|| ProfileUtil.isUW()
					|| ProfileUtil.isDocumentController()
					|| ((ProfileUtil.isAccountingController() || ProfileUtil.isCollectionController() || ProfileUtil.isCollectionSupervisor() || ProfileUtil.isAuctionController())
							&& quotation.getWkfStatus().equals(QuotationWkfStatus.ACT)));
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isFieldCheckTabAvailable(Quotation quotation) {
		EWkfStatus quotationStatus = quotation.getWkfStatus();
		return (ProfileUtil.isPOS() || ProfileUtil.isAdmin())
				&& (quotationStatus == QuotationWkfStatus.RFC || quotation.isFieldCheckPerformed());
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isUnderwriterTabAvailable(Quotation quotation) {
		return (ProfileUtil.isUW() || ProfileUtil.isManager());
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isManagementTabAvailable(Quotation quotation) {
		EWkfStatus quotationStatus = quotation.getWkfStatus();
		return ProfileUtil.isManager() && (quotationStatus == QuotationWkfStatus.APS
				|| quotationStatus == QuotationWkfStatus.AWS
				|| quotationStatus == QuotationWkfStatus.AWT
				|| quotationStatus == QuotationWkfStatus.APV
				|| quotationStatus == QuotationWkfStatus.ACT
				|| quotationStatus == QuotationWkfStatus.RCG);
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isHistoryStatusTabAvailable(Quotation quotation) {
		return quotation.getId() != null && (ProfileUtil.isPOS() || ProfileUtil.isUW() || ProfileUtil.isManager());
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isNavigationUnderwriterAvailable(Quotation quotation) {
		EWkfStatus quotationStatus = quotation.getWkfStatus();
		return ProfileUtil.isUW()
				&& (
						quotationStatus == QuotationWkfStatus.PRO || quotationStatus == QuotationWkfStatus.APU
					 || quotationStatus == QuotationWkfStatus.AWU  || quotationStatus == QuotationWkfStatus.APS
				     || quotationStatus == QuotationWkfStatus.AWS
				   );
	}
	
	/**
	 * @param quotation
	 * @return
	 */
	public static boolean isNavigationManagerAvailable(Quotation quotation) {
		EWkfStatus quotationStatus = quotation.getWkfStatus();
		return ProfileUtil.isManager()
				&& (
						quotationStatus == QuotationWkfStatus.APS 
					 || quotationStatus == QuotationWkfStatus.AWS 
					 || quotationStatus == QuotationWkfStatus.RCG
				   );
	}
}

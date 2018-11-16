package com.nokor.efinance.core.shared.util;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

public final class ProfileUtil {

	/**
	 * @param profileId
	 * @param profiles
	 * @return
	 */
	public static boolean isProfileExist(Long profileId, List<SecProfile> profiles) {
		for (SecProfile profile : profiles) {
			if (profileId.equals(profile.getId())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return
	 */
	public static boolean isPOS() {
		return isCreditOfficer()
				|| isProductionOfficer()
				|| isCreditOfficerMovable();
	}
	
	/**
	 * @return
	 */
	public static boolean isPOS(SecUser secUser) {
		return isCreditOfficer(secUser)
				|| isProductionOfficer(secUser)
				|| isCreditOfficerMovable(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isPOS(SecProfile secProfile) {
		return FMProfile.CO.equals(secProfile.getId())
				|| FMProfile.PO.equals(secProfile.getId())
				|| FMProfile.CM.equals(secProfile.getId());
	}
	
	/**
	 * @return
	 */
	public static boolean isUW(SecProfile secProfile) {
		return FMProfile.UW.equals(secProfile.getId()) || FMProfile.US.equals(secProfile.getId());
	}
	
	
	/**
	 * @return
	 */
	public static boolean isUW() {
		return isUnderwriter()|| isUnderwriterSupervisor();
	}
	
	/**
	 * @return
	 */
	public static boolean isUW(SecUser secUser) {
		return isUnderwriter(secUser)|| isUnderwriterSupervisor(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isCreditOfficer() {
		return isCreditOfficer((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isCreditOfficer(SecUser secUser) {
		return FMProfile.CO.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * @return
	 */
	public static boolean isProductionOfficer() {
		return isProductionOfficer((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isProductionOfficer(SecUser secUser) {
		return FMProfile.PO.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * @return
	 */
	public static boolean isManager() {
		return isManager((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isManager(SecUser secUser) {
		return FMProfile.MA.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * @return
	 */
	public static boolean isUnderwriter() {
		return isUnderwriter((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isUnderwriter(SecUser secUser) {
		return FMProfile.UW.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * @return
	 */
	public static boolean isUnderwriterSupervisor() {
		return isUnderwriterSupervisor((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isUnderwriterSupervisor(SecUser secUser) {
		return FMProfile.US.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * @return
	 */
	public static boolean isDocumentController() {
		return isDocumentController((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isDocumentController(SecUser secUser) {
		return FMProfile.DC.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * @return
	 */
	public static boolean isAccountingController() {
		return isAccountingController((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isAccountingController(SecUser secUser) {
		return FMProfile.AC.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * @return
	 */
	public static boolean isCollectionController() {
		return isCollectionController((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isCollectionController(SecUser secUser) {
		return FMProfile.CC.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * @return
	 */
	public static boolean isCollectionSupervisor() {
		return isCollectionSupervisor((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isCollectionSupervisor(SecUser secUser) {
		return FMProfile.CS.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * @return
	 *//*
	public static boolean isMarketingProfile() {
		return isMarketingProfile((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	*//**
	 * @return
	 *//*
	public static boolean isMarketingProfile(SecUser secUser) {
		return FMProfile.MK.equals(secUser.getDefaultProfile().getId());
	}*/
	
	/**
	 * @return
	 *//*
	public static boolean isMartketingProfile(SecProfile secProfile) {
		return FMProfile.MK.equals(secProfile.getId());
	}*/
	
	/**
	 * @return
	 */
	public static boolean isSeniorCO() {
		return isSeniorCO((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isSeniorCO(SecUser secUser) {
		return FMProfile.SC.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isCreditOfficerMovable () {
		return isCreditOfficerMovable((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isCreditOfficerMovable (SecUser secUser) {
		return FMProfile.CM.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isAdmin () {
		return isAdmin((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isAdmin (SecUser secUser) {
		return FMProfile.AD.equals(secUser.getDefaultProfile().getId());
	}
	
	/**
	 * @return
	 */
	public static boolean isAuctionController () {
		return isAuctionController((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @param secUser
	 * @return
	 */
	public static boolean isAuctionController (SecUser secUser) {
		return FMProfile.UC.equals(secUser.getDefaultProfile().getId());
	}
	
// EFINANCE-TH	
	
	/**
	 * @return
	 */
	public static boolean isCMProfile() {
		return isCMProfile((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @param secUser
	 * @return
	 */
	public static boolean isCMProfile(SecUser secUser) {
		return IProfileCode.CMSTAFF.equals(secUser.getDefaultProfile().getCode())
			   || IProfileCode.CMLEADE.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isCMLeader(SecUser secUser) {
		return IProfileCode.CMLEADE.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isCMLeader() {
		return isCMLeader((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isCMStaff(SecUser secUser) {
		return IProfileCode.CMSTAFF.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isCMStaff() {
		return isCMStaff((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isCollection(SecUser secUser) {
		return isColPhoneStaff(secUser) 
			   || isColPhoneLeader(secUser) 
			   || isColPhoneSupervisor(secUser)
			   || isColFieldStaff(secUser)
			   || isColFieldLeader(secUser)
			   || isColFieldSupervisor(secUser)
			   || isColInsideRepoStaff(secUser)
			   || isColInsideRepoLeader(secUser)
			   || isColInsideRepoSupervisor(secUser)
			   || isColOAStaff(secUser)
			   || isColOALeader(secUser)
			   || isColOASupervisor(secUser)
			   || isColBillStaff(secUser) 
			   || isColBillLeader(secUser)
			   || isColOutsourceRepoLeader(secUser)
			   || isColOutsourceRepoStaff(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isCollection() {
		return isCollection((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColPhone(SecUser secUser) {
		return isColPhoneStaff(secUser) || isColPhoneLeader(secUser) || isColPhoneSupervisor(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isColPhone() {
		return isColPhone((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColField(SecUser secUser) {
		return isColFieldStaff(secUser) || isColFieldLeader(secUser) || isColFieldSupervisor(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isColField() {
		return isColField((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColInsideRepo(SecUser secUser) {
		return isColInsideRepoStaff(secUser) || isColInsideRepoLeader(secUser) || isColInsideRepoSupervisor(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isColInsideRepo() {
		return isColInsideRepo((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColOA(SecUser secUser) {
		return isColOAStaff(secUser) || isColOALeader(secUser) || isColOASupervisor(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isColOA() {
		return isColOA((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColBill(SecUser secUser) {
		return isColBillStaff(secUser) || isColBillLeader(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isColBill() {
		return isColBill((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColOutsourceRepo(SecUser secUser) {
		return isColOutsourceRepoStaff(secUser) || isColOutsourceRepoLeader(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isColOutsourceRepo() {
		return isColOutsourceRepo((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColPhoneStaff(SecUser secUser) {
		return IProfileCode.COL_PHO_STAFF.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColPhoneLeader(SecUser secUser) {
		return IProfileCode.COL_PHO_LEADE.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColPhoneSupervisor(SecUser secUser) {
		return IProfileCode.COL_PHO_SUPER.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isColPhoneStaff() {
		return isColPhoneStaff((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isColPhoneLeader() {
		return isColPhoneLeader((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isColPhoneSupervisor() {
		return isColPhoneSupervisor((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColBillStaff(SecUser secUser) {
		return IProfileCode.COL_BIL_STAFF.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColBillLeader(SecUser secUser) {
		return IProfileCode.COL_BIL_LEADE.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isColBillLeader() {
		return isColBillLeader((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColInsideRepoSupervisor(SecUser secUser) {
		return IProfileCode.COL_INS_SUPER.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isColInsideRepoSupervisor() {
		return isColInsideRepoSupervisor((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColInsideRepoLeader(SecUser secUser) {
		return IProfileCode.COL_INS_LEADE.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isColInsideRepoLeader() {
		return isColInsideRepoLeader((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColInsideRepoStaff(SecUser secUser) {
		return IProfileCode.COL_INS_STAFF.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isColInsideRepoStaff() {
		return isColInsideRepoStaff((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColOutsourceRepoLeader(SecUser secUser) {
		return IProfileCode.COL_OUT_LEADE.equals(secUser.getDefaultProfile().getCode());
	}

	/**
	 * @return
	 */
	public static boolean isColOutsourceRepoLeader() {
		return isColOutsourceRepoLeader((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColOutsourceRepoStaff(SecUser secUser) {
		return IProfileCode.COL_OUT_STAFF.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColOAStaff(SecUser secUser) {
		return IProfileCode.COL_OA_STAFF.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColOALeader(SecUser secUser) {
		return IProfileCode.COL_OA_LEADE.equals(secUser.getDefaultProfile().getCode());
	}

	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColOASupervisor(SecUser secUser) {
		return IProfileCode.COL_OA_SUPER.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isColOAStaff() {
		return isColOAStaff((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isColOALeader() {
		return isColOALeader((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isColOASupervisor() {
		return isColOASupervisor((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColFieldStaff(SecUser secUser) {
		return IProfileCode.COL_FIE_STAFF.equals(secUser.getDefaultProfile().getCode());
	}

	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColFieldLeader(SecUser secUser) {
		return IProfileCode.COL_FIE_LEADE.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isColFieldSupervisor(SecUser secUser) {
		return IProfileCode.COL_FIE_SUPER.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isColFieldStaff() {
		return isColFieldStaff((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isColFieldLeader() {
		return isColFieldLeader((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isColFieldSupervisor() {
		return isColFieldSupervisor((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isColLeader() {
		return isColPhoneLeader() || isColFieldLeader() || isColInsideRepoLeader() || isColOALeader();
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isColSupervisor() {
		return isColPhoneSupervisor() || isColFieldSupervisor() || isColInsideRepoSupervisor() || isColOASupervisor();
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isCollectionStaff() {
		return isColPhoneStaff() || isColFieldStaff() || isColInsideRepoStaff() || isColOAStaff();
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isCollectionStaff(SecUser secUser) {
		return isColPhoneStaff(secUser) || isColFieldStaff(secUser) || isColInsideRepoStaff(secUser);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isColSupervisor(SecUser secUser) {
		return isColPhoneSupervisor(secUser) || isColFieldSupervisor(secUser) || isColInsideRepoSupervisor(secUser) || isColOASupervisor(secUser);
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isCallCenter(SecUser secUser) {
		return isCallCenterStaff(secUser) || isCallCenterLeader(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isCallCenter() {
		return isCallCenter((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isCallCenterStaff(SecUser secUser) {
		return IProfileCode.CAL_CEN_STAFF.equals(secUser.getDefaultProfile().getCode());
	}

	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isCallCenterLeader(SecUser secUser) {
		return IProfileCode.CAL_CEN_LEADE.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isCallCenterStaff() {
		return isCallCenterStaff((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @return
	 */
	public static boolean isCallCenterLeader() {
		return isCallCenterLeader((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isARStaff(SecUser secUser) {
		return IProfileCode.AR_STAFF.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isARStaff() {
		return isARStaff((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isARSupervisor(SecUser secUser) {
		return IProfileCode.AR_SUPER.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isARSupervisor() {
		return isARSupervisor((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @param secUser
	 * @return
	 */
	public static boolean isARProfile(SecUser secUser) {
		return isARStaff(secUser) || isARSupervisor(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isARProfile() {
		return isARProfile((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * @param secUser
	 * @return
	 */
	public static boolean isMarketingProfile(SecUser secUser) {
		return isMarketingLeader(secUser);
	}
	
	/**
	 * @return
	 */
	public static boolean isMarketingProfile() {
		return isMarketingProfile((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isMarketingLeader(SecUser secUser) {
		return IProfileCode.MKT_LEADE.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * @return
	 */
	public static boolean isMarketingLeader() {
		return isMarketingLeader((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isAccountingStaff() {
		return isAccountingStaff((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isAccountingStaff(SecUser secUser) {
		return IProfileCode.ACC_STAFF.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isAccountingSupervisor() {
		return isAccountingSupervisor((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isAccountingSupervisor(SecUser secUser) {
		return IProfileCode.ACC_SUPER.equals(secUser.getDefaultProfile().getCode());
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isAccounting() {
		return isAccountingStaff() || isAccountingSupervisor();
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	public static boolean isAccounting(SecUser secUser) {
		return isAccountingStaff(secUser) || isAccountingSupervisor(secUser);
	}
}

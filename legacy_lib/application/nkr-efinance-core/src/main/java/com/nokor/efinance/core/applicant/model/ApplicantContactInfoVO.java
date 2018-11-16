package com.nokor.efinance.core.applicant.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;



/**
 * 
 * @author uhout.cheng
 */
public class ApplicantContactInfoVO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -672934102552528433L;
	
	private List<AppContactTypeVO> appContactTypeVOs;
	private ETypeContactInfo[] typeContactInfos;
	
	/**
	 * @return the appContactTypeVOs
	 */
	public List<AppContactTypeVO> getAppContactTypeVOs() {
		return appContactTypeVOs;
	}
	
	/**
	 * 
	 * @param contract
	 * @param typeContactInfos
	 */
	public ApplicantContactInfoVO(Contract contract, ETypeContactInfo[] typeContactInfos) {
		this.typeContactInfos = typeContactInfos;
		appContactTypeVOs = new ArrayList<>();
		appContactTypeVOs.addAll(getAppContactTypeVOs(EApplicantType.C, contract.getApplicant()));
		
		List<ContractApplicant> contractApplicants = contract.getContractApplicants();
		if (contractApplicants != null  && !contractApplicants.isEmpty()) {
			for (int i = 0; i < contractApplicants.size(); i++) {
				ContractApplicant contractApplicant = contractApplicants.get(i);
				if (EApplicantType.G.equals(contractApplicant.getApplicantType())) {
					appContactTypeVOs.addAll(getAppContactTypeVOs(EApplicantType.G, contractApplicant.getApplicant()));
				}
			}
		}
		
		List<IndividualReferenceInfo> referenceInfos = new ArrayList<IndividualReferenceInfo>();
		Applicant appRef = contract.getApplicant();
		if (appRef != null) {
			Individual indRef = appRef.getIndividual();
			if (indRef != null) {
				referenceInfos = indRef.getIndividualReferenceInfos();
			}
		}
		if (!referenceInfos.isEmpty()) {
			for (int i = 0; i < referenceInfos.size(); i++) {
				IndividualReferenceInfo referenceInfo = referenceInfos.get(i);
				List<IndividualReferenceContactInfo> referenceContactInfos = referenceInfo.getIndividualReferenceContactInfos();
				if (referenceContactInfos != null && !referenceContactInfos.isEmpty()) {
					for (IndividualReferenceContactInfo referenceContactInfo : referenceContactInfos) {
						if (this.typeContactInfos != null && this.typeContactInfos.length > 0) {
							for (int j = 0; j < typeContactInfos.length; j++) {
								if (typeContactInfos[j].equals(referenceContactInfo.getContactInfo().getTypeInfo())) {
									appContactTypeVOs.add(new AppContactTypeVO(EApplicantType.R.getCode(), 
											referenceContactInfo.getContactInfo().getTypeInfo().getCode(), 
											referenceContactInfo.getContactInfo().getValue()));
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param appType
	 * @param app
	 * @return
	 */
	private List<AppContactTypeVO> getAppContactTypeVOs(EApplicantType appType, Applicant app) {
		List<AppContactTypeVO> appContactTypeVOs = new ArrayList<AppContactTypeVO>();
		List<IndividualContactInfo> indContactInfos = new ArrayList<IndividualContactInfo>();
		if (app != null) {
			Individual indApp = app.getIndividual();
			if (indApp != null) {
				indContactInfos = indApp.getIndividualContactInfos();
			}
		}
		if (!indContactInfos.isEmpty()) {
			for (IndividualContactInfo indContactInfo : indContactInfos) {
				if (this.typeContactInfos != null && this.typeContactInfos.length > 0) {
					for (int j = 0; j < typeContactInfos.length; j++) {
						if (typeContactInfos[j].equals(indContactInfo.getContactInfo().getTypeInfo())) {
							appContactTypeVOs.add(new AppContactTypeVO(appType.getCode(), 
									indContactInfo.getContactInfo().getTypeInfo().getCode(), 
									indContactInfo.getContactInfo().getValue()));
						}
					}
				}
			}
		}
		return appContactTypeVOs;
	}
}
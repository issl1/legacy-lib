package com.nokor.efinance.core.applicant.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.ersys.core.hr.model.address.Address;



/**
 * 
 * @author uhout.cheng
 */
public class ApplicantAddressInfoVO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -6376160768070794884L;
	
	private List<Address> addresses;
	
	/**
	 * @return the addresses
	 */
	public List<Address> getAddresses() {
		return addresses;
	}

	/**
	 * 
	 * @param contract
	 * @param typeApplicants
	 */
	public ApplicantAddressInfoVO(Contract contract, EApplicantType[] typeApplicants) {
		addresses = new ArrayList<>();
		if (typeApplicants != null && typeApplicants.length > 0) {
			for (int i = 0; i < typeApplicants.length; i++) {
				if (EApplicantType.C.equals(typeApplicants[i])) {
					if (contract.getApplicant() != null && contract.getApplicant().getIndividual() != null) {
						addresses.addAll(getAddresses(contract.getApplicant().getIndividual()));
					}
				} else if (EApplicantType.G.equals(typeApplicants[i])) {
					List<ContractApplicant> contractApplicants = contract.getContractApplicants();
					if (contractApplicants != null && !contractApplicants.isEmpty()) {
						for (ContractApplicant contractApplicant : contractApplicants) {
							if (contractApplicant.getApplicantType().equals(EApplicantType.G)) {
								if (contract.getApplicant() != null && contract.getApplicant().getIndividual() != null) {
									addresses.addAll(getAddresses(contract.getApplicant().getIndividual()));
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
	 * @param individual
	 * @return
	 */
	private List<Address> getAddresses(Individual individual) {
		List<Address> addresses = new ArrayList<>();
		List<IndividualAddress> individualAddresses = individual.getIndividualAddresses();
		if (individualAddresses!= null && !individualAddresses.isEmpty()) {
			for (IndividualAddress individualAddress : individualAddresses) {
				addresses.add(individualAddress.getAddress());
			}
		}
		return addresses;
	}
	
}
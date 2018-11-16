package com.nokor.efinance.core.collection.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.security.model.SecUser;

public class UserContractDetail implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 5432779268264698696L;
	
	private SecUser user;
		
	private long nbCurrentContracts;
	private List<ContractDetail> newContracts = new ArrayList<>();
	private long nbAdjustedContracts;
	private long nbGuaranteedContracts;
	private long nbNonGuaranteedContracts;
	private long nbAssistedContracts;
	private long nbFlaggedContracts;
	private long nbDD1to5;
	private long nbDD6to10;
	private long nbDD11to15;
	private long nbDD16to20;
	
	/**
	 * @param user
	 */
	public UserContractDetail(SecUser user) {
		this.user = user;
	}
	
	/**
	 * @return the user
	 */
	public SecUser getUser() {
		return user;
	}	
	
	/**
	 * @return the newContracts
	 */
	public List<ContractDetail> getNewContracts() {
		return newContracts;
	}

	/**
	 * @param contract
	 */
	public void addNewContract(ContractDetail contract) {
		newContracts.add(contract);
	}
		
	/**
	 * @return
	 */
	public long getNbNewContract() {
		return newContracts.size();
	}

	/**
	 * @return the nbCurrentContracts
	 */
	public long getNbCurrentContracts() {
		return nbCurrentContracts;
	}

	/**
	 * @param nbCurrentContracts the nbCurrentContracts to set
	 */
	public void setNbCurrentContracts(long nbCurrentContracts) {
		this.nbCurrentContracts = nbCurrentContracts;
	}

	/**
	 * @return the nbAdjustedContracts
	 */
	public long getNbAdjustedContracts() {
		return nbAdjustedContracts;
	}

	/**
	 * @param nbAdjustedContracts the nbAdjustedContracts to set
	 */
	public void setNbAdjustedContracts(long nbAdjustedContracts) {
		this.nbAdjustedContracts = nbAdjustedContracts;
	}

	/**
	 * @return the nbGuaranteedContracts
	 */
	public long getNbGuaranteedContracts() {
		return nbGuaranteedContracts;
	}

	/**
	 * @param nbGuaranteedContracts the nbGuaranteedContracts to set
	 */
	public void setNbGuaranteedContracts(long nbGuaranteedContracts) {
		this.nbGuaranteedContracts = nbGuaranteedContracts;
	}

	/**
	 * @return the nbNonGuaranteedContracts
	 */
	public long getNbNonGuaranteedContracts() {
		return nbNonGuaranteedContracts;
	}

	/**
	 * @param nbNonGuaranteedContracts the nbNonGuaranteedContracts to set
	 */
	public void setNbNonGuaranteedContracts(long nbNonGuaranteedContracts) {
		this.nbNonGuaranteedContracts = nbNonGuaranteedContracts;
	}

	/**
	 * @return the nbAssistedContracts
	 */
	public long getNbAssistedContracts() {
		return nbAssistedContracts;
	}

	/**
	 * @param nbAssistedContracts the nbAssistedContracts to set
	 */
	public void setNbAssistedContracts(long nbAssistedContracts) {
		this.nbAssistedContracts = nbAssistedContracts;
	}

	/**
	 * @return the nbFlaggedContracts
	 */
	public long getNbFlaggedContracts() {
		return nbFlaggedContracts;
	}

	/**
	 * @param nbFlaggedContracts the nbFlaggedContracts to set
	 */
	public void setNbFlaggedContracts(long nbFlaggedContracts) {
		this.nbFlaggedContracts = nbFlaggedContracts;
	}

	/**
	 * @return the nbDD1to5
	 */
	public long getNbDD1to5() {
		return nbDD1to5;
	}

	/**
	 * @param nbDD1to5 the nbDD1to5 to set
	 */
	public void setNbDD1to5(long nbDD1to5) {
		this.nbDD1to5 = nbDD1to5;
	}

	/**
	 * @return the nbDD6to10
	 */
	public long getNbDD6to10() {
		return nbDD6to10;
	}

	/**
	 * @param nbDD6to10 the nbDD6to10 to set
	 */
	public void setNbDD6to10(long nbDD6to10) {
		this.nbDD6to10 = nbDD6to10;
	}

	/**
	 * @return the nbDD11to15
	 */
	public long getNbDD11to15() {
		return nbDD11to15;
	}

	/**
	 * @param nbDD11to15 the nbDD11to15 to set
	 */
	public void setNbDD11to15(long nbDD11to15) {
		this.nbDD11to15 = nbDD11to15;
	}

	/**
	 * @return the nbDD16to20
	 */
	public long getNbDD16to20() {
		return nbDD16to20;
	}

	/**
	 * @param nbDD16to20 the nbDD16to20 to set
	 */
	public void setNbDD16to20(long nbDD16to20) {
		this.nbDD16to20 = nbDD16to20;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(SecUser user) {
		this.user = user;
	}

	/**
	 * @param newContracts the newContracts to set
	 */
	public void setNewContracts(List<ContractDetail> newContracts) {
		this.newContracts = newContracts;
	}
	
	/**
	 * @return
	 */
	public long getTotalConracts() {
		return nbCurrentContracts
			+ newContracts.size();
	}
	
	/**
	 * @return
	 */
	public List<Contract> toContracts() {
		List<Contract> contracts = new ArrayList<>();
		for (ContractDetail contractDetail : newContracts) {
			contracts.add(contractDetail.getContract());
		}
		return contracts;
	}
}

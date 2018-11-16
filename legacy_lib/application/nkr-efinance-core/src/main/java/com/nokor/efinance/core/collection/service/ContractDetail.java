package com.nokor.efinance.core.collection.service;

import java.io.Serializable;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.contract.model.Contract;

public class ContractDetail implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -788926629347819403L;
	
	private Contract contract;
	private Area area;
	
	/**
	 * @param contract
	 */
	public ContractDetail(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the contract
	 */
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the area
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}
}

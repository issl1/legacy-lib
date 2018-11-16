package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.MBaseAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;



/**
 * 
 * @author uhout.cheng
 */
public class ColContactAddressPanel extends AbstractControlPanel implements FinServicesHelper, MBaseAddress {

	/** */
	private static final long serialVersionUID = -5769700741042965096L;
	
	private AddressTablePanel addressLesseeTablePanel;
	private AddressTablePanel addressGuarantorTablePanel;
	
	private Contract contract;
	
	/**
	 * 
	 * @param caption
	 */
	public ColContactAddressPanel() {
		init();
	}
	
	/**
	 * 
	 * @param caption
	 */
	private void init() {
		addressLesseeTablePanel = new AddressTablePanel("lessee", this);
		addressGuarantorTablePanel = new AddressTablePanel("guarantor", this);
		setMargin(true);
		setSpacing(true);
		addComponent(addressLesseeTablePanel);
		addComponent(addressGuarantorTablePanel);
	}
	
	/**
	 * refresh
	 */
	public void refresh() {
		ENTITY_SRV.refresh(contract);
		assignValues(contract);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		Map<EApplicantType, Applicant> applicants = new HashMap<>();
		applicants.put(EApplicantType.C, contract.getApplicant());
		ContractApplicant conApp = contract.getContractApplicant(EApplicantType.G);
		if (conApp != null) {
			applicants.put(EApplicantType.G, conApp.getApplicant());
		}
		Map<EApplicantType, List<Address>> addresses = new HashMap<>();
		if (!applicants.isEmpty()) {
			for (EApplicantType appType : applicants.keySet()) {
				Applicant applicant = applicants.get(appType);
				Individual individual = applicant.getIndividual();
				
				if (individual != null) {
					List<IndividualAddress> indAddrs = individual.getIndividualAddresses();
					if (indAddrs != null && !indAddrs.isEmpty()) {
						List<Address> addrs = new ArrayList<Address>();
						for (IndividualAddress indaddr : indAddrs) {
							addrs.add(indaddr.getAddress());
						}
						addresses.put(appType, addrs);
					}
					if (EApplicantType.C.equals(appType)) {
						addressLesseeTablePanel.assignValues(addresses.get(EApplicantType.C), individual);
					} else if (EApplicantType.G.equals(appType)) {
						addressGuarantorTablePanel.assignValues(addresses.get(EApplicantType.G), individual);
					}
				}
			}
		}
	}

}

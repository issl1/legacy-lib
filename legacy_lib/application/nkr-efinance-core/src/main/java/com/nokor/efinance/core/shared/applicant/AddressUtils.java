package com.nokor.efinance.core.shared.applicant;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.CompanyAddress;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAddress;
import com.nokor.ersys.core.hr.model.address.Address;

public class AddressUtils {

	/**
	 * Copy an address
	 * @param source
	 * @param dest
	 * @return
	 */
	public static Address copy(Address source, Address dest) {
		if (source != null) {
			dest.setType(source.getType());
			dest.setCountry(source.getCountry());
			dest.setProvince(source.getProvince());
			dest.setDistrict(source.getDistrict());
			dest.setCommune(source.getCommune());
			dest.setVillage(source.getVillage());
			dest.setHouseNo(source.getHouseNo());
			dest.setStreet(source.getStreet());
			dest.setFreeField1(source.getFreeField1());
			dest.setFreeField2(source.getFreeField2());
			dest.setFreeField3(source.getFreeField3());
			dest.setFreeField4(source.getFreeField4());
			dest.setLine1(source.getLine1());
			dest.setLine2(source.getLine2());
			dest.setLine3(source.getLine3());
		}
		return dest;
	}
	
	/**
	 * 
	 * @param applicant
	 * @return
	 */
	public static List<Address> getAddressesByApplicant(Applicant applicant) {
		List<Address> addresses = new ArrayList<>();
		if (applicant != null) {
			Individual individual = null;
			Company company = null;
			if (EApplicantCategory.INDIVIDUAL.equals(applicant.getApplicantCategory())) {
				individual = applicant.getIndividual();
			} else if (EApplicantCategory.COMPANY.equals(applicant.getApplicantCategory())) {
				company = applicant.getCompany();
			}
			if (individual != null) {
				List<IndividualAddress> individualAddresses = individual.getIndividualAddresses();
				if (individualAddresses!= null && !individualAddresses.isEmpty()) {
					for (IndividualAddress individualAddress : individualAddresses) {
						addresses.add(individualAddress.getAddress());
					}
				}
				/*List<Employment> employments = individual.getEmployments();
				if (employments != null && !employments.isEmpty()) {
					for (Employment employment : employments) {
						addresses.add(employment.getAddress());
					}
				}*/
			} else if (company != null) {
				List<CompanyAddress> companyAddresses = company.getCompanyAddresses();
				if (companyAddresses != null && !companyAddresses.isEmpty()) {
					for (CompanyAddress companyAddress : companyAddresses) {
						addresses.add(companyAddress.getAddress());
					}
				}
				/*List<CompanyEmployee> companyEmployees = company.getCompanyEmployees();
				if (companyEmployees != null && !companyEmployees.isEmpty()) {
					for (CompanyEmployee companyEmployee : companyEmployees) {
						addresses.add(companyEmployee.getAddress());
					}
				}*/
			}
		}
		return addresses;
	}
	
	/**
	 * 
	 * @param dealer
	 * @return
	 */
	public static List<Address> getAddressesByDealer(Dealer dealer) {
		List<Address> addresses = new ArrayList<>();
		if (dealer != null) {
			List<DealerAddress> dealerAddresses = dealer.getDealerAddresses();
			if (dealerAddresses!= null && !dealerAddresses.isEmpty()) {
				for (DealerAddress dealerAddress : dealerAddresses) {
					addresses.add(dealerAddress.getAddress());
				}
			}
		}
		return addresses;
	}
	
	/**
	 * 
	 * @param dealer
	 * @return
	 */
	public static final String getDealerAddress(Dealer dealer) {
		String address = StringUtils.EMPTY;
		
		try {
			address = dealer.getDealerAddresses().get(0).getAddress().getProvince().getDescEn();
		} catch (Exception e) {
			return address;
		}
		
		return address;
	}
}

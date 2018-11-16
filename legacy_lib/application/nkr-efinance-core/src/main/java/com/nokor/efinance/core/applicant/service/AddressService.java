package com.nokor.efinance.core.applicant.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.common.app.eref.ECountry;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;

/**
 * Address service interface
 * @author ly.youhort
 *
 */
public interface AddressService extends BaseEntityService {

	/**
	 * Get province by code
	 * @param country
	 * @param code
	 * @return
	 */
	Province getProvineByCode(ECountry country, String provinceCode);
	
	/**
	 * Get District by code
	 * @param country
	 * @param code
	 * @return
	 */
	District getDistrictByCode(Province province, String districtCode);
	
	/**
	 * Get Commune by code
	 * @param country
	 * @param code
	 * @return
	 */
	Commune getCommuneByCode(District district, String communeCode);
	
	/**
	 * Get Village by code
	 * @param country
	 * @param code
	 * @return
	 */
	Village getVillageByCode(Commune commune, String villageCode);
	
	/**
	 * @param address
	 * @param applicant
	 * @return
	 */
	boolean isAddressUsedByOtherApplicant(Address address, Applicant applicant);
	
	/**
	 * Add unknown addresses reference
	 */
	void addUnknownAddressesReference();
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	String getDetailAddress(Address address);
	
	/**
	 * 
	 * @param orgStructure
	 * @return
	 */
	String getBranchAddress(OrgStructure orgStructure);
	
	/**
	 * 
	 * @param dealer
	 * @return
	 */
	String getDealerAddress(Dealer dealer);
	
	/**
	 * Get Addres to Return / Repo
	 * @param contract
	 * @param typeAddress
	 * @return
	 */
	Address getAddress(Contract contract, ETypeAddress typeAddress);
}

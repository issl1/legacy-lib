package com.nokor.efinance.core.applicant.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.service.AddressService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.common.app.eref.ECountry;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;

/**
 * Address service
 * @author ly.youhort
 *
 */
@Service("addressService")
public class AddressServiceImpl extends BaseEntityServiceImpl implements AddressService, FMEntityField {

	/**
	 */
	private static final long serialVersionUID = 3949195646920822163L;

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
    private EntityDao dao;
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public EntityDao getDao() {
		return dao;
	}

	
	/**
	 * Get province by code
	 * @param country
	 * @param code
	 * @return
	 */
	@Override
	public Province getProvineByCode(ECountry country, String provinceCode) {
		BaseRestrictions<Province> restrictions = new BaseRestrictions<Province>(Province.class);
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq(CODE, provinceCode));
		criterions.add(Restrictions.eq("country", country));
		restrictions.setCriterions(criterions);
		List<Province> provinces = list(restrictions);
		if (provinces != null && !provinces.isEmpty()) {
			return provinces.get(0);
		} 
		return null;
	}
	
	/**
	 * Get District by code
	 * @param country
	 * @param code
	 * @return
	 */
	@Override
	public District getDistrictByCode(Province province, String districtCode) {
		BaseRestrictions<District> restrictions = new BaseRestrictions<District>(District.class);
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq(CODE, districtCode));
		criterions.add(Restrictions.eq("province.id", province.getId()));
		restrictions.setCriterions(criterions);
		List<District> districts = list(restrictions);
		if (districts != null && !districts.isEmpty()) {
			return districts.get(0);
		} 
		return null;
	}
	
	/**
	 * Get Commune by code
	 * @param country
	 * @param code
	 * @return
	 */
	@Override
	public Commune getCommuneByCode(District district, String communeCode) {
		BaseRestrictions<Commune> restrictions = new BaseRestrictions<Commune>(Commune.class);
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq(CODE, communeCode));
		criterions.add(Restrictions.eq("district.id", district.getId()));
		restrictions.setCriterions(criterions);
		List<Commune> communes = list(restrictions);
		if (communes != null && !communes.isEmpty()) {
			return communes.get(0);
		} 
		return null;
	}
	
	/**
	 * Get Village by code
	 * @param country
	 * @param code
	 * @return
	 */
	@Override
	public Village getVillageByCode(Commune commune, String villageCode) {
		BaseRestrictions<Village> restrictions = new BaseRestrictions<Village>(Village.class);
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq(CODE, villageCode));
		criterions.add(Restrictions.eq("commune.id", commune.getId()));
		restrictions.setCriterions(criterions);
		List<Village> villages = list(restrictions);
		if (villages != null && !villages.isEmpty()) {
			return villages.get(0);
		} 
		return null;
	}
	
	/**
	 * @param address
	 * @param applicant
	 * @return
	 */
	public boolean isAddressUsedByOtherApplicant(Address address, Applicant applicant) {
		BaseRestrictions<IndividualAddress> restrictions = new BaseRestrictions<>(IndividualAddress.class);
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("addressType", ETypeAddress.MAIN));
		criterions.add(Restrictions.eq("address.id", address.getId()));
		criterions.add(Restrictions.ne("applicant.id", applicant.getId()));
		List<IndividualAddress> individualAddresses = list(restrictions);
		return individualAddresses != null && !individualAddresses.isEmpty();
	}
	
	/**
	 * 
	 */
	public void addUnknownAddressesReference() {
		
		
		List<Province> provinces = list(Province.class);		
		
		for (Province province : provinces) {			
			if (getDistrictByCode(province, "OTH") != null) {
				return;
			}			
			District disctrict = new District();
			disctrict.setCode("OTH");
			disctrict.setDescEn("Other");
			disctrict.setDesc("Other");
			disctrict.setUpdateUser("admin");
			disctrict.setCreateUser("admin");
			disctrict.setProvince(province);
			saveOrUpdate(disctrict);
		}
		
		List<District> districts = list(District.class);
		
		for (District district : districts) {
			Commune commune = new Commune();
			commune.setCode("OTH");
			commune.setDescEn("Other");
			commune.setDesc("Other");
			commune.setUpdateUser("admin");
			commune.setCreateUser("admin");
			commune.setDistrict(district);
			saveOrUpdate(commune);
		}
		
		List<Commune> communes = list(Commune.class);
		
		for (Commune commune : communes) {
			Village village = new Village();
			village.setCode("OTH");
			village.setDescEn("Other");
			village.setDesc("Other");
			village.setUpdateUser("admin");
			village.setCreateUser("admin");
			village.setCommune(commune);
			saveOrUpdate(village);
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.applicant.service.AddressService#getDetailAddress(com.nokor.ersys.core.hr.model.address.Address)
	 */
	@Override
	public String getDetailAddress(Address address) {
		List<String> descriptions = new ArrayList<>();
		descriptions.add(address.getHouseNo());
		if (StringUtils.isNotEmpty(address.getLine1())) {
			descriptions.add(address.getLine1());
		}
		if (StringUtils.isNotEmpty(address.getLine2())) {
			descriptions.add(address.getLine2());
		}
		if (StringUtils.isNotEmpty(address.getLine3())) {
			descriptions.add(address.getLine3());
		}
		descriptions.add(address.getStreet());
		descriptions.add(address.getCommune() != null ? address.getCommune().getDesc() : StringUtils.EMPTY);
		descriptions.add(address.getDistrict() != null ? address.getDistrict().getDesc() : StringUtils.EMPTY);
		descriptions.add(address.getProvince() != null ? address.getProvince().getDesc() : StringUtils.EMPTY);
		descriptions.add(address.getPostalCode());
		
		return StringUtils.join(descriptions, ",");
	}

	/**
	 * get Address by Branch
	 */
	@Override
	public String getBranchAddress(OrgStructure orgStructure) {
		if (orgStructure.getOrgAddresses() != null && !orgStructure.getOrgAddresses().isEmpty()) {
			return getDetailAddress(orgStructure.getOrgAddresses().get(0).getAddress());
		}
		return null;
	}

	/**
	 * get address by dealer
	 * @param dealer
	 * @return
	 */
	@Override
	public String getDealerAddress(Dealer dealer) {
		if (dealer.getDealerAddresses() != null && !dealer.getDealerAddresses().isEmpty()) {
			return getDetailAddress(dealer.getDealerAddresses().get(0).getAddress());
		}
		return null;
	}
	
	/**
	 * 
	 * @param contract
	 * @param typeAddress
	 * @return
	 */
	@Override
	public Address getAddress(Contract contract, ETypeAddress typeAddress) {
		Individual individual = contract.getApplicant().getIndividual();
		BaseRestrictions<IndividualAddress> restrictions = new BaseRestrictions<>(IndividualAddress.class);
		restrictions.addAssociation("address", "add", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("individual", individual));
		restrictions.addCriterion(Restrictions.eq("add.type", typeAddress));
		List<IndividualAddress> individualAddresses = list(restrictions);
		if (!individualAddresses.isEmpty()) {
			return individualAddresses.get(0).getAddress();
		}
		return null;
	}
}

package com.nokor.ersys.core.hr.service.impl;

import java.util.List;

import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.ersys.core.hr.dao.OrganizationDao;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrgAddress;
import com.nokor.ersys.core.hr.model.organization.OrgBankAccount;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.service.OrganizationRestriction;
import com.nokor.ersys.core.hr.service.OrganizationService;

/**
 * 
 * @author prasnar
 *
 */
@Service("organizationService")
public class OrganizationServiceImpl extends MainEntityServiceImpl implements OrganizationService {
	/** */
	private static final long serialVersionUID = -5371512953945676570L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private OrganizationDao dao;
	
	
	/**
	 * 
	 */
    public OrganizationServiceImpl() {
    	super();
    }


	@Override
	public OrganizationDao getDao() {
		return dao;
	}
       
	/**
	 * 
	 */
	@Override
	public List<Organization> listChildrenOrganizations(ETypeOrganization typeOrganization, Long parentOrgId) {
		return listOrganizations(typeOrganization, null, null, parentOrgId);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @return
	 */
	@Override
	public List<Organization> listRootOrganizations(ETypeOrganization typeOrganization) {
		return listOrganizations(typeOrganization, null, true, null);
	}
	
	@Override
	public List<Organization> listOrganizations(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization, Boolean rootOnly, Long parentOrgId) {
		return list(OrganizationRestriction.buildRestrictions(typeOrganization, subTypeOrganization, rootOnly, parentOrgId));
	}
	

	/**
	 * 
	 * @param typeOrganization
	 * @param orgId
	 * @return
	 */
	@Override
	public Organization getRootOrganization(ETypeOrganization typeOrganization, Long orgId) {
		return getOrganization(typeOrganization, true, null, orgId);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param rootOnly
	 * @param parentOrgId
	 * @param orgId
	 * @return
	 */
	@Override
	public Organization getOrganization(ETypeOrganization typeOrganization, Boolean rootOnly, Long parentOrgId, Long orgId) {
		return getOrganization(typeOrganization, null, rootOnly, parentOrgId, orgId);
	}
	
	/**
	 * 
	 */
	@Override
	public Organization getOrganization(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization, Boolean rootOnly, Long parentOrgId, Long orgId) {
		OrganizationRestriction restrictions = new OrganizationRestriction();
		restrictions.setTypeOrganization(typeOrganization);
		restrictions.addSubTypeOrganization(subTypeOrganization);
		restrictions.setOrgId(orgId);
		restrictions.setRootOnly(rootOnly);
		restrictions.setParentOrgId(parentOrgId);
		return dao.getUnique(restrictions);
	}
	
	
	@Override
	public void createProcess(MainEntity mainEntity) throws DaoException {
		super.createProcess(mainEntity);
		List<OrgAddress> orgAddresses = ((Organization) mainEntity).getOrgAddresses();
		if (orgAddresses != null) {
			for (OrgAddress orgAddress : orgAddresses) {
				super.saveOrUpdate(orgAddress.getAddress());
				super.saveOrUpdate(orgAddress);
			}
		}
		List<OrgBankAccount> orgBankAccounts = ((Organization) mainEntity).getOrgBankAccounts();
		if (orgBankAccounts != null) {
			for (OrgBankAccount orgBankAccount : orgBankAccounts) {
				super.saveOrUpdate(orgBankAccount);
			}
		}
	}

	@Override
	public void updateProcess(MainEntity mainEntity) throws DaoException {
		super.updateProcess(mainEntity);
		List<OrgAddress> orgAddresses = ((Organization) mainEntity).getOrgAddresses();
		if (orgAddresses != null) {
			for (OrgAddress orgAddress : orgAddresses) {
				super.saveOrUpdate(orgAddress.getAddress());
				super.saveOrUpdate(orgAddress);
			}
		}
		List<OrgBankAccount> orgBankAccounts = ((Organization) mainEntity).getOrgBankAccounts();
		if (orgBankAccounts != null) {
			for (OrgBankAccount orgBankAccount : orgBankAccounts) {
				super.saveOrUpdate(orgBankAccount);
			}
		}
	}

	@Override
	public void deleteProcess(MainEntity mainEntity) throws DaoException {
		super.deleteProcess(mainEntity);
	}
}

package com.nokor.efinance.core.applicant.service.impl;

import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.applicant.model.CompanyContactInfo;
import com.nokor.efinance.core.applicant.service.CompanyService;

/**
 * CompanyServiceImpl service
 * @author uhout.cheng
 */
@Service("companyService")
public class CompanyServiceImpl extends BaseEntityServiceImpl implements CompanyService {
	
	/** */
	private static final long serialVersionUID = 8369687610555674474L;

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
	 * @see com.nokor.efinance.core.applicant.service.CompanyService#saveOrUpdateCompanyContactInfo(com.nokor.efinance.core.applicant.model.CompanyContactInfo)
	 */
	@Override
	public void saveOrUpdateCompanyContactInfo(CompanyContactInfo companyContactInfo) {
		if (companyContactInfo != null) {
			saveOrUpdate(companyContactInfo.getContactInfo());
			saveOrUpdate(companyContactInfo);
		}
	}

}

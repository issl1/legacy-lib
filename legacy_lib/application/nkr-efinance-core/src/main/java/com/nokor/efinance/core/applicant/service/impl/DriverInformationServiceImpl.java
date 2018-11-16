package com.nokor.efinance.core.applicant.service.impl;

import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.applicant.model.Driver;
import com.nokor.efinance.core.applicant.service.DriverInformationService;
import com.nokor.efinance.core.shared.FMEntityField;

/**
 * 
 * @author uhout.cheng
 */
@Service("driverInformationService")
public class DriverInformationServiceImpl extends BaseEntityServiceImpl implements DriverInformationService, FMEntityField {

	/** */
	private static final long serialVersionUID = 4269670409353088052L;
	
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
	 * @see com.nokor.efinance.core.applicant.service.DriverInformationService#saveOrUpdateDriverAddress(com.nokor.efinance.core.applicant.model.Driver)
	 */
	@Override
	public void saveOrUpdateDriverAddress(Driver driver) {
		if (driver.getAddress() != null) {
			saveOrUpdate(driver.getAddress());
		}
		saveOrUpdate(driver);
	}
	
}

package com.nokor.ersys.core.partner.service.impl;

import java.util.List;

import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.ersys.core.partner.model.Partner;
import com.nokor.ersys.core.partner.service.PartnerService;

/**
 * 
 * @author prasnar
 *
 */
@Service("partnerService")
public class PartnerServiceImpl extends MainEntityServiceImpl implements PartnerService {
	/** */
	private static final long serialVersionUID = 4293927361192513290L;

	

	@Autowired
    private EntityDao dao;
	
	
	/**
	 * 
	 */
    public PartnerServiceImpl() {
    	super();
    }


	@Override
	public EntityDao getDao() {
		return dao;
	}
       
	@Override
	public void createProcess(MainEntity mainEntity) throws DaoException {
		Partner partner = (Partner) mainEntity;

        saveOnAction(partner.getSubEntitiesToCascadeAction());
        
        getDao().create(partner);
        
        if (!partner.getSubListEntitiesToCascade().isEmpty()) {
        	for (final List<EntityA> child : partner.getSubListEntitiesToCascade()) {
                saveOnAction(child);
            }
        }
	}

	@Override
	public void updateProcess(MainEntity mainEntity) throws DaoException {
		Partner partner = (Partner) mainEntity;

        saveOnAction(partner.getSubEntitiesToCascadeAction());
        if (!partner.getSubListEntitiesToCascade().isEmpty()) {
        	for (final List<EntityA> child : partner.getSubListEntitiesToCascade()) {
                saveOnAction(child);
            }
        }
        
        partner = getDao().merge(partner);
	}

	@Override
	public void deleteProcess(MainEntity mainEntity) throws DaoException {
		throwIntoRecycledBin(mainEntity);
	}
	

	

}

package com.nokor.common.app.workflow.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EMainEntity;

import com.nokor.common.app.workflow.model.WkfHistoConfig;

/**
 * @author prasnar
 * 
 */
public class WkfHistoConfigRestriction extends BaseRestrictions<WkfHistoConfig> {
	/** */
	private static final long serialVersionUID = 7544739739306574566L;

	private EMainEntity entity;
	   
    /**
     *
     */
    public WkfHistoConfigRestriction() {
        super(WkfHistoConfig.class);
    }

    @Override
	public void preBuildAssociation() {
    	if (entity != null) {
    		addCriterion(Restrictions.eq(WkfHistoConfig.ENTITY, entity));
    	}
	}

    
    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {

    	
    }

	/**
	 * @return the entity
	 */
	public EMainEntity getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(EMainEntity entity) {
		this.entity = entity;
	}

	
    
}

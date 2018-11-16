package com.nokor.common.app.scheduler.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.scheduler.model.ScTriggerTask;

/**
 * @author prasnar
 * \
 */
public class SchedulerRestriction extends BaseRestrictions<ScTriggerTask> {
    private String desc;

    /**
     *
     */
    public SchedulerRestriction() {
        super(ScTriggerTask.class);
    }

    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {
    	addAssociation("task", "t", JoinType.INNER_JOIN);

    	if (StringUtils.isNotEmpty(desc)) {
    		addCriterion(Restrictions.or(Restrictions.ilike("desc", desc), 
    									Restrictions.ilike("t.desc", desc)));
    	}
    	
    }

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	

    
}

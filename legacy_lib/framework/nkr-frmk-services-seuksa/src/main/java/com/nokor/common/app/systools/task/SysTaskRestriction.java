package com.nokor.common.app.systools.task;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.systools.model.EnSysTaskType;
import com.nokor.common.app.systools.model.EnSysTaskWhere;
import com.nokor.common.app.systools.model.SysTask;

/**
 * @author prasnar
 * 
 */
public class SysTaskRestriction extends BaseRestrictions<SysTask> {
	/** */
	private static final long serialVersionUID = -8053594939567868823L;

	private Boolean isExecuted;
	private EnSysTaskType type;
	private EnSysTaskWhere where;
	
	/**
	 * 
	 */
    public SysTaskRestriction() {
		super(SysTask.class);
	}

    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildAssociation()
     */
    public void preBuildAssociation() {
    }
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {

    	
    	if (Boolean.TRUE.equals(isExecuted)) {
			addCriterion(Restrictions.isNotNull("executedDate"));
    	} else if (Boolean.FALSE.equals(isExecuted)) {
			addCriterion(Restrictions.isNull("executedDate"));
    	}
    	
    	if (type != null) {
			addCriterion(Restrictions.eq("type", type));
    	} 
    	
    	if (where != null) {
			addCriterion(Restrictions.eq("where", where));
    	} 
    	
	}

	/**
	 * @return the isExecuted
	 */
	public Boolean getIsExecuted() {
		return isExecuted;
	}

	/**
	 * @param isExecuted the isExecuted to set
	 */
	public void setIsExecuted(Boolean isExecuted) {
		this.isExecuted = isExecuted;
	}

	/**
	 * @return the type
	 */
	public EnSysTaskType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EnSysTaskType type) {
		this.type = type;
	}

	/**
	 * @return the where
	 */
	public EnSysTaskWhere getWhere() {
		return where;
	}

	/**
	 * @param where the where to set
	 */
	public void setWhere(EnSysTaskWhere where) {
		this.where = where;
	}

    
}

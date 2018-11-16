package com.nokor.efinance.core.collection.model;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.MEMainEntity;

/**
 * 
 * @author uhout.cheng
 */
public class LockSplitTypeRestriction extends BaseRestrictions<ELockSplitType> {
	
	/**  */
	private static final long serialVersionUID = -7873072578992278893L;
	
	private String[] codes;
	private ELockSplitGroup group;
	
	/**
	 * 
	 */
    public LockSplitTypeRestriction() {
		super(ELockSplitType.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (codes != null && codes.length > 0) {
			addCriterion(Restrictions.in(MEMainEntity.CODE, codes));
		}
    	if (group != null) {
    		addCriterion(Restrictions.eq(ELockSplitType.LOCKSPLITGROUP, group));
    	}
	}

	/**
	 * @return the codes
	 */
	public String[] getCodes() {
		return codes;
	}

	/**
	 * @param codes the codes to set
	 */
	public void setCodes(String[] codes) {
		this.codes = codes;
	}

	/**
	 * @return the group
	 */
	public ELockSplitGroup getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(ELockSplitGroup group) {
		this.group = group;
	}
	
}

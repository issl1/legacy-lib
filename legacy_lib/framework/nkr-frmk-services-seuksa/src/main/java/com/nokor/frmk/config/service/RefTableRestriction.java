package com.nokor.frmk.config.service;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.frmk.config.model.ERefType;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;

/**
 * @author prasnar
 * @version $revision$
 */
public class RefTableRestriction <T extends RefDataId> extends BaseRestrictions<RefTable> {
	/** */
	private static final long serialVersionUID = 2397693350182901463L;

	private Class<T> tableClazz;
	private String tableClazzName;
    private String desc;
	private Boolean isVisible;
	private Boolean isReadonly;
    private ERefType type;
 
    /**
     *
     */
    public RefTableRestriction() {
        super(RefTable.class);
    }
    
    /**
     * 
     * @param tableClazz
     */
    public RefTableRestriction(Class<T> tableClazz) {
        super(RefTable.class);
    }
    
    /**
     * 
     * @param tableClazzName
     */
    public RefTableRestriction(String tableClazzName) {
        super(RefTable.class);
    }

    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {
    	if (type != null) {
            addCriterion(Restrictions.eq(RefTable.TYPE, type));
        }
    	
    	if (isVisible != null) {
    		addCriterion(Restrictions.eq(RefTable.VISIBLE, isVisible));
    	}
    	if (isReadonly != null) {
    		addCriterion(Restrictions.eq(RefTable.READONLY, isReadonly));
    	}
    	
        if (desc != null) {
            addCriterion(Restrictions.or(
            				Restrictions.ilike(RefData.DESC, desc), 
            				Restrictions.ilike(RefData.DESCEN, desc),
            				Restrictions.ilike(RefTable.DESC, desc),
            				Restrictions.ilike(RefTable.SHORT_NAME, desc))
            				);
        }
    }



	/**
	 * @return the tableClazz
	 */
	public Class<T> getTableClazz() {
		return tableClazz;
	}

	/**
	 * @param tableClazz the tableClazz to set
	 */
	public void setTableClazz(Class<T> tableClazz) {
		this.tableClazz = tableClazz;
	}

	/**
	 * @return the tableClazzName
	 */
	public String getTableClazzName() {
		return tableClazzName;
	}

	/**
	 * @param tableClazzName the tableClazzName to set
	 */
	public void setTableClazzName(String tableClazzName) {
		this.tableClazzName = tableClazzName;
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

	/**
	 * @return the isVisible
	 */
	public Boolean getIsVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible the isVisible to set
	 */
	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * @return the isReadonly
	 */
	public Boolean getIsReadonly() {
		return isReadonly;
	}

	/**
	 * @param isReadonly the isReadonly to set
	 */
	public void setIsReadonly(Boolean isReadonly) {
		this.isReadonly = isReadonly;
	}

	/**
	 * @return the type
	 */
	public ERefType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ERefType type) {
		this.type = type;
	}

	
	    
}

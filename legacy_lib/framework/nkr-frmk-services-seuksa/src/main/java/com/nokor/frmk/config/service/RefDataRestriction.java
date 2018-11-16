package com.nokor.frmk.config.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
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
public class RefDataRestriction <T extends RefDataId> extends BaseRestrictions<RefData> {
	/** */
	private static final long serialVersionUID = 623694425697411791L;

	private Class<T> tableClazz;
	private String tableClazzName;
	private Long ide;
    private String code;
    private String desc;
	private Boolean fetchValuesFromDB;
	private List<ERefType> types = new ArrayList<>();

    /**
     *
     */
    public RefDataRestriction() {
        super(RefData.class);
    }
    
    /**
     * 
     * @param tableClazz
     */
    public RefDataRestriction(Class<T> tableClazz) {
        super(RefData.class);
        this.tableClazz = tableClazz;
    }
    
    /**
     * 
     * @param tableClazzName
     */
    public RefDataRestriction(String tableClazzName) {
        super(RefData.class);
        this.tableClazzName = tableClazzName;
    }

    /**
     * @see com.nokor.frmk.dao.hql.BaseRestrictions#preBuildAssociation()
     */
    @Override
    public void preBuildAssociation() {
    	addAssociation(RefData.TABLE);
    }
 
    /**
     * @see com.nokor.frmk.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {

    	if (tableClazz != null) {
    		addCriterion(Restrictions.eq(RefData.TABLE + DOT + RefTable.CODE , tableClazz.getCanonicalName()));
    	} else if (!StringUtils.isEmpty(tableClazzName)) {
    		addCriterion(Restrictions.eq(RefData.TABLE + DOT + RefTable.CODE , tableClazzName));
    	}
    	
    	if (types == null || types.size() == 0) {
    		types = Arrays.asList(ERefType.REFDATA);
        }
    	Criterion critTypes = Restrictions.in(RefData.TABLE + DOT + RefTable.TYPE, types);
    	addCriterion(critTypes);
    	
    	if (ide != null) {
            addCriterion(Restrictions.eq(RefData.IDE, ide));
        }
    	
    	if (fetchValuesFromDB != null) {
            addCriterion(Restrictions.eq(RefData.TABLE + DOT + RefTable.FETCHVALUESFROMDB, fetchValuesFromDB));
        }
    	
    	if (!StringUtils.isEmpty(code)) {
            addCriterion(Restrictions.eq(RefData.CODE, code));
        }
    	
    	if (!StringUtils.isEmpty(desc)) {
            addCriterion(Restrictions.or(
            				Restrictions.ilike(RefData.DESC, desc, MatchMode.ANYWHERE), 
            				Restrictions.ilike(RefData.DESCEN, desc, MatchMode.ANYWHERE)));
        }
    	
    	setOrder(Order.asc(RefData.TABLE + DOT + RefTable.CODE));
    	addOrder(Order.asc(RefData.IDE));
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
	public void setTableClazz(Class<T> refDataTableClazz) {
		this.tableClazz = refDataTableClazz;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return the ide
	 */
	public Long getIde() {
		return ide;
	}

	/**
	 * @param ide the ide to set
	 */
	public void setIde(Long ide) {
		this.ide = ide;
	}

	/**
	 * @return the fetchValuesFromDB
	 */
	public Boolean getFetchValuesFromDB() {
		return fetchValuesFromDB;
	}

	/**
	 * @param fetchValuesFromDB the fetchValuesFromDB to set
	 */
	public void setFetchValuesFromDB(Boolean fetchValuesFromDB) {
		this.fetchValuesFromDB = fetchValuesFromDB;
	}

	/**
	 * @return the types
	 */
	public List<ERefType> getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(List<ERefType> types) {
		this.types = types;
	}

}

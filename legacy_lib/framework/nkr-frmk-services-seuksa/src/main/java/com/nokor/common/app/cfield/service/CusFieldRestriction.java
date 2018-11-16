package com.nokor.common.app.cfield.service;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.cfield.model.CusField;

/**
 * 
 * @author prasnar
 *
 */
public class CusFieldRestriction extends BaseRestrictions<CusField> {
    private String tableName;
	private Long tableId;
	private String name;
	private Long typeId;
	private Boolean isMandatory;

	/**
	 * 
	 */
    public CusFieldRestriction() {
		super(CusField.class);
	}

    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
       	addAssociation("table", "ta", JoinType.INNER_JOIN);
       	addAssociation("type", "ty", JoinType.INNER_JOIN);
    	
    	if (tableName != null) {
			addCriterion(Restrictions.eq("ta.code", tableName));
		}
    	if (tableId != null) {
			addCriterion(Restrictions.eq("ta.id", tableId));
		}
    	if (typeId != null) {
			addCriterion(Restrictions.eq("ty.id", typeId));
		}
    	if (name != null) {
			addCriterion(Restrictions.eq("code", name));
		}
    	if (isMandatory != null) {
			addCriterion(Restrictions.eq("isMandatory", isMandatory));
		}
		
	}


	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}


	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	/**
	 * @return the tableId
	 */
	public Long getTableId() {
		return tableId;
	}


	/**
	 * @param tableId the tableId to set
	 */
	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the typeId
	 */
	public Long getTypeId() {
		return typeId;
	}


	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}


	/**
	 * @return the isMandatory
	 */
	public Boolean getIsMandatory() {
		return isMandatory;
	}


	/**
	 * @param isMandatory the isMandatory to set
	 */
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	
}

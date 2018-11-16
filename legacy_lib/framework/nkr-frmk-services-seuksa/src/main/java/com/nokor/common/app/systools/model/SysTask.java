package com.nokor.common.app.systools.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.seuksa.frmk.model.entity.SimpleEntity;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_sys_task")
public class SysTask extends SimpleEntity {
	/** */
	private static final long serialVersionUID = 9027627160707433374L;

	private String code;
	private String desc;
	private String method;
	private EnSysTaskWhere where;
	private EnSysTaskType type;
	private Date executedDate;
	private String errorMsg;
    private Integer sortIndex;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sys_tas_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sys_tas_code", nullable = false)
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
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sys_tas_desc", nullable = true)
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
	 * @return the method
	 */
    @Column(name = "sys_tas_method", nullable = true)
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the where
	 */
	@Column(name = "sys_tas_where_code", nullable = false)
	@Enumerated(EnumType.STRING)
	public EnSysTaskWhere getWhere() {
		return where;
	}

	/**
	 * @param where the where to set
	 */
	public void setWhere(EnSysTaskWhere where) {
		this.where = where;
	}

	/**
	 * @return the type
	 */
	@Column(name = "sys_tas_typ_code", nullable = false)
	@Enumerated(EnumType.STRING)
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
	 * @return the executedDate
	 */
    @Column(name = "sys_tas_executed_date", nullable = true)
	public Date getExecutedDate() {
		return executedDate;
	}

	/**
	 * @param executedDate the executedDate to set
	 */
	public void setExecutedDate(Date executedDate) {
		this.executedDate = executedDate;
	}

	/**
	 * @return the errorMsg
	 */
    @Column(name = "sys_tas_error_msg", nullable = true, columnDefinition = "TEXT")
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	 /**
     * @return SortIndex
     */
    @Column(name = "sort_index")
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * @param sortIndex
     */
    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    /**
     * @return the createUser
     */
    @Column(name = "usr_cre", nullable = false, length = 30)
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
    public void setCreateUser(final String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return the createDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_cre", nullable = false, updatable=false)
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateUser
     */
    @Column(name = "usr_upd", nullable = false, length = 30)
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the updateUser to set
     */
    public void setUpdateUser(final String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the updateDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_upd", nullable = false)
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(final Date updateDate) {
        this.updateDate = updateDate;
    }
    
    @Override
	public String toString() {
		return "Task [" + (getId() != null ? getId() : "<NULL>") + "]"
				+ " - " + getCode() 
				+ " - "  + (getDesc() != null ? getDesc() : "N/A");
	}
	
}

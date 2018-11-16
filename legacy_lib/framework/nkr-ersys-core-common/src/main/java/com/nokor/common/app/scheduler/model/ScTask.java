package com.nokor.common.app.scheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author kong.phirun
 *
 */
@Entity
@Table(name = "tu_sc_task")
public class ScTask extends EntityRefA {
	
	/**	 */
	private static final long serialVersionUID = 5685799514292933250L;
	
	private String jobClassName;
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sch_tas_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sch_tas_code", nullable = true, length = 10)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sch_tas_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }
	
	@Column(name = "sch_tas_desc_en", nullable = false, length = 100)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * @return the jobClassName
	 */
    @Column(name = "sch_tas_job_classname", nullable = false)
	public String getJobClassName() {
		return jobClassName;
	}

	/**
	 * @param jobClassName the jobClassName to set
	 */
	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

    
	
}

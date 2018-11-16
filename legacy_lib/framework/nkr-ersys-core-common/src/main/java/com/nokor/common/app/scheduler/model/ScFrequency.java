package com.nokor.common.app.scheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Time Unit
 * @author kong.phirun
 */
@Entity
@Table(name = "ts_sc_frequency")
public class ScFrequency extends EntityRefA {
	/** */
	private static final long serialVersionUID = 7780359893758137270L;

	public static long DAILY = 1;
    public static long WEEKLY = 2;
    public static long MONTHLY = 3;
    public static long IN_MINUTES = 4;
    public static long IN_HOURS = 5;

    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sch_fre_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "sch_fre_code", nullable = true, length = 30)
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "sch_fre_desc", nullable = false, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }
	
	@Column(name = "sch_fre_desc_en", nullable = false, length = 100)
	@Override
    public String getDescEn() {
        return descEn;
    }
    
}

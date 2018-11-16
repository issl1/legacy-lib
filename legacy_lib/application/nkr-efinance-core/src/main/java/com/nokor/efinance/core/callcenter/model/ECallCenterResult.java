package com.nokor.efinance.core.callcenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Entity
@Table(name = "tu_call_center_result")
public class ECallCenterResult extends EntityRefA {
	
	/** */
	private static final long serialVersionUID = -92530575513368122L;

	public static final String KO = "KO";
	public static final String OK = "OK";
	public static final String OTHER = "OTHER";
	
	/**
     * 
     * @return
     */
    public static ECallCenterResult createInstance() {
    	ECallCenterResult instance = EntityFactory.createInstance(ECallCenterResult.class);
        return instance;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cal_ctr_res_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "cal_ctr_res_code", nullable = false, length=10)
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "cal_ctr_res_desc", nullable = true, length=255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "cal_ctr_res_desc_en", nullable = false, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }

}
